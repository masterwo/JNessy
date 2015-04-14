package com.jemm.nes.cpu;

import java.io.IOException;

import com.jemm.nes.apu.RP2A0X;
import com.jemm.nes.fileformat.NesFile;
import com.jemm.nes.ppu.PPU;
import com.jemm.nes.ppu.PpuMem;

public class NesMain {
	public static void main(String args[]) {
		NesFile file = new NesFile();
		CPU6502 cpu = new CPU6502();
		RP2A0X apu = new RP2A0X();
		
		try {
			file.loadNesFile(NesMain.class.getResource("/MarioBros.nes").getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Memory.getInstance().initiateMemory(file.bbPRGROM);
		PpuMem.getInstance().init(file.bbCHRROM, file.getHeader().getMirroring());
		
		PPU ppu = new PPU();
		//Must run this to setup PC.
		cpu.reset();
		
		int cNMI = 0;
		int ppuCycle = 0;
		int cpuCycle = 0;
		
		long FrameStart = System.nanoTime();
		
		while(true) {
			/*
			 * software instructions BRK & PHP will push the B flag as being 1.
			 * hardware interrupts IRQ & NMI will push the B flag as being 0.
			 */
			if(++cNMI == 29781) {//Interrupt recieved. //TODO recalculate in reference to PPU cycles.
				cNMI= 0;
				CpuRegisters.getInstance().SP-=1;//make room for 2 bytes
				Memory.getInstance().cmPutShort(0x0100 + (CpuRegisters.getInstance().SP & 0xFF), CpuRegisters.getInstance().getPC());
				CpuRegisters.getInstance().SP-=1;//This puts us just "behind" the stack on the next available "empty" space.
				
				Memory.getInstance().cmPut(0x0100 + (CpuRegisters.getInstance().SP & 0xFF),CpuRegisters.getInstance().SR);
				CpuRegisters.getInstance().SP-=1;
				
				CpuRegisters.getInstance().setPC(Memory.getInstance().cmGetShort(0xFFFA));
				while((System.nanoTime()-FrameStart) < 17832754);
				FrameStart = System.nanoTime();
			}
			ppu.execute();
			ppuCycle++;
			
			if(ppuCycle == 3) {
				cpu.execute();
				ppuCycle = 0;
				cpuCycle++;
			}
//			apu.update();
		}
	}
}
