package com.jemm.nes.apu;

public class ApuRegisters {
	public byte SQ1_ENV;	//0x4000
	public byte SQ1_SWEEP;	//0x4001
	public byte SQ1_LO;		//0x4002
	public byte SQ1_HI;		//0x4003
	
	public byte SQ2_ENV;	//0x4004
	public byte SQ2_SWEEP;	//0x4005
	public byte SQ2_LO;		//0x4006
	public byte SQ2_HI;		//0x4007
	
	public byte TRI_CTRL;	//0x4008
	public byte TRI_LO;		//0x400A
	public byte TRI_HI;		//0x400B
	
	public byte APUFLAGS;	//0x4015
	
	private ApuRegisters() {}
	
	public static ApuRegisters getInstance() {
		return instance;
	}
	private static ApuRegisters instance = new ApuRegisters();
}
