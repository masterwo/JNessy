package com.jemm.nes.cpu;

public class Registers {
	private static final Registers instance = new Registers();

	public int PC;		//program counter				(16 bit)
	public short AC;	//accumulator					(8 bit)
	public short X;		//X register					(8 bit)
	public short Y;		//Y register					(8 bit)
	public byte SR;		//status register [NV-BDIZC]	(8 bit)
	public short SP;	//stack pointer					(8 bit)
	
	private Registers() {
		
	}
	
    public static Registers getInstance() {
        return instance;
    }
	
}
