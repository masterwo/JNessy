package com.jemm.nes.cpu;

public class CpuRegisters {
	private static final CpuRegisters instance = new CpuRegisters();

	public int  PC;	//program counter				(16 bit)
	public short AC;	//accumulator					(8 bit)
	public short X;		//X register					(8 bit)
	public short Y;		//Y register					(8 bit)
	public byte  SR;	//status register [NV-BDIZC]	(8 bit)
	public short SP;	//stack pointer					(8 bit)
	
	public int getPC() {
		return PC = Memory.getInstance().cmPosition();
	}

	public void setPC(int pC) {
		Memory.getInstance().cmSetPosition(pC);
		PC = pC;
	}

	private CpuRegisters() {
		SP = 0xFF;
		SR = 0x24;
	}
	
    public static CpuRegisters getInstance() {
        return instance;
    }
	
}
