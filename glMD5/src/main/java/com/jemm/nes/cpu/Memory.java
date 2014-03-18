package com.jemm.nes.cpu;

import java.nio.ByteBuffer;

public class Memory {
	private ByteBuffer CPU_MEM;
	
	public void initiateMemory(ByteBuffer bbPRGROM, ByteBuffer bbCHRROM) {
		bbPRGROM.limit(bbPRGROM.position() + 16384);
		bbPRGROM.clear();//Just resets the buffer marks, doesn't clear content.
		CPU_MEM = bbPRGROM.slice();//First 16kb slice in memory.
	}
	
	public short cmGet(int memIndex) {
		return CPU_MEM.get(memIndex);
	}
	
	public void cmPut(int memIndex, short data) {
		CPU_MEM.put(memIndex, (byte)data);
	}
	
	public int cmGetShort(int memIndex) {
		return CPU_MEM.getShort(memIndex);
	}
	
	private Memory() {}
	
	private static final Memory instance = new Memory();
	
    public static Memory getInstance() {
        return instance;
    }
}
