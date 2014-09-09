package com.jemm.nes.ppu;

import com.jemm.nes.fileformat.Mirroring;

public class PpuRegisters {
	private static final PpuRegisters instance = new PpuRegisters();
	
	public byte PPUCTRL;
	public byte PPUMASK;
	private byte PPUSTATUS;
	public byte OAMADDR;
	public byte OAMDATA;
	//public byte PPUSCROLL;
	//private byte PPUADDR;
	//private byte PPUDATA;
	
	public short pPUScrollX = 0;
	public short pPUScrollY = 0;
	
	private int ppuAdress = 0;
	private boolean writeToggle = false;
	
	private PpuRegisters() {}
	
    public static PpuRegisters getInstance() {
        return instance;
    }

	public byte getPPUSTATUS() {
		writeToggle = false;//Clear address latch.
		return PPUSTATUS;
	}

	public void setPPUSTATUS(byte pPUSTATUS) {
		PPUSTATUS = pPUSTATUS;
	}
	
	int hitCntr = 0;

	public void setPPUADDR(byte pPUADDR) {
		hitCntr++;
		if(writeToggle) {
			ppuAdress += (pPUADDR & 0xFF);
//			System.out.print("PPUADDR(" + hitCntr + "): " + String.format("%04X", ppuAdress) + ": ");
		} else {
			ppuAdress = pPUADDR<<8;
		}
		writeToggle = !writeToggle;
	}

	public byte getPPUDATA() {
		byte resultingData = PpuMem.getInstance().PPU_ROM.get(ppuAdress);
		
		if((PPUCTRL & 0x04) != 0) {
			ppuAdress+=32;
		} else {
			ppuAdress+=1;
		}
		
		return resultingData;
	}

	public void setPPUDATA(byte pPUDATA) {
		Mirroring mirroring = PpuMem.getInstance().mirroring;
		int internalAddress = 0;
		
//		System.out.println("PPUDATA: " + String.format("%02X", pPUDATA));
		
		if(ppuAdress>= 0x2000 && ppuAdress <= 0x3EFF) {//Name Tables (And attribute tables.)
			internalAddress = ppuAdress & 0x2FFF;//Move mirrored adresses down too 0x2FFF range.
			if(internalAddress < 0x2F00)
				PpuMem.getInstance().PPU_ROM.put((internalAddress | 0x1000), pPUDATA);//Write to mirror range.
			if(mirroring == Mirroring.Vertical) {
				if(internalAddress < 0x2400 || (internalAddress > 0x27FF && internalAddress < 0x2C00))
				{
					internalAddress = internalAddress & 0x23FF;
				}
				else
				{
					internalAddress = internalAddress & 0x27FF;
				}
				PpuMem.getInstance().PPU_ROM.put(internalAddress, pPUDATA);
				PpuMem.getInstance().PPU_ROM.put(internalAddress ^ 0x0800, pPUDATA);
			} else if(mirroring == Mirroring.Horizontal) {
				if(internalAddress < 0x2800)
				{
					internalAddress = internalAddress & 0x23FF;
				}
				else
				{
					internalAddress = internalAddress & 0x2BFF;
				}
				PpuMem.getInstance().PPU_ROM.put(internalAddress, pPUDATA);
				PpuMem.getInstance().PPU_ROM.put(internalAddress ^ 0x0400, pPUDATA);
			}
		}
		/*
		 * VRAM address increment per CPU read/write of PPUDATA as determined by bit 2 of PPUCTRL
		 * (0: increment by 1, going across; 1: increment by 32, going down)
		 */
		if((PPUCTRL & 0x04) != 0) {
			ppuAdress+=32;
		} else {
			ppuAdress+=1;
		}
	}
	
	public void setPPUSCROLL(short pPUSCROLL) {
		if(writeToggle) {
			pPUScrollX = pPUSCROLL;
		} else {
			pPUScrollY = pPUSCROLL;
		}
		writeToggle = !writeToggle;
	}
}
