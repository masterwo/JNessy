package com.jemm.nes.cpu;

public class CPU6502 {
	private Memory memory = Memory.getInstance();
	private Instructions6502 instructions = new Instructions6502();
	
	public CPU6502() {
	}
	
	public void reset() {
		//PC = byte at $FFFD * 256 + byte at $FFFC
		short pcA = memory.cmGet(0xFFFD);
		short pcB = memory.cmGet(0xFFFC);
		CpuRegisters.getInstance().setPC(((pcA * 256) + pcB));
		//DEBUG
		//CpuRegisters.getInstance().setPC(0xC000);
	}
	
	public void execute() {
		instructions.execute((byte)memory.cmGet());
	}
}
