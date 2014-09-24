package com.jemm.nes.apu;

public class ApuRegisters {
//	If the enabled flag is set, the length counter is loaded with entry L of the length table:
//	     |  0   1   2   3   4   5   6   7    8   9   A   B   C   D   E   F
//	-----+----------------------------------------------------------------
//	00-0F  10,254, 20,  2, 40,  4, 80,  6, 160,  8, 60, 10, 14, 12, 26, 14,
//	10-1F  12, 16, 24, 18, 48, 20, 96, 22, 192, 24, 72, 26, 16, 28, 32, 30
	public static final byte LENGTH_COUNTER_TABLE[] = {10,(byte)254, 20,  2, 40,  4, 80,  6, (byte)160,  8, 60, 10, 14, 12, 26, 14,
													   12, 16, 24, 18, 48, 20, 96, 22, (byte)192, 24, 72, 26, 16, 28, 32, 30};
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
	
	public byte FRAME_COUNTER; //0x4017
	
	private ApuRegisters() {}
	
	public static ApuRegisters getInstance() {
		return instance;
	}
	private static ApuRegisters instance = new ApuRegisters();
}
