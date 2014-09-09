package com.jemm.nes.ppu;

import java.nio.ByteBuffer;

import com.jemm.nes.fileformat.Mirroring;
import com.jemm.nes.ppu.PpuRegisters;

public class PpuMem {
	public ByteBuffer PPU_ROM;
	public ByteBuffer OAM_DATA;
	private PpuRegisters registers = PpuRegisters.getInstance();
	public Mirroring mirroring;
	
	public PpuRegisters getRegister() {
		return registers;
	}
	
	public void reset() {
		//TODO implement http://wiki.nesdev.com/w/index.php/PPU_power_up_state
	}
	
	public void init(ByteBuffer bbCHRROM, Mirroring mirroring){
		this.mirroring = mirroring;
		
		PPU_ROM = ByteBuffer.allocate(0x4000);
		bbCHRROM.rewind();
		bbCHRROM.limit(0x2000);
		
		PPU_ROM.put(bbCHRROM);//Put 8Kb into ppu mem.
		PPU_ROM.rewind();
	}
	
	private PpuMem() {
		OAM_DATA = ByteBuffer.allocate(0x0100);
		
		registers.PPUCTRL = 0x00; 				//0x00 0000
		registers.PPUMASK = 0x00;				//0000 0xx0
		registers.setPPUSTATUS((byte) 0x80);	//+0+x xxxx //TODO fix 80 or A0?
		registers.OAMADDR = 0x00;	 			//$00
		//$2005/$2006 latch	 		//cleared
		//registers.PPUSCROLL = 0x00;	 		//$0000
		//registers.PPUADDR = 0x00;	 	 		//$0000
		//registers.PPUDATA = 0x00;				//Random
		/*
		odd frame	 ?	 ?
		CHR RAM	 pattern	 unchanged
		NT RAM	 mostly $FF	 unchanged
		SPR RAM	 pattern	 pattern
		*/

	}
	
	public static PpuMem getInstance() {
		return instance;
	}
	
	private static PpuMem instance = new PpuMem();
}
