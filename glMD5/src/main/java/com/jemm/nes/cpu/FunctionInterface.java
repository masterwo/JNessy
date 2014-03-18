package com.jemm.nes.cpu;

import java.nio.ByteBuffer;

public interface FunctionInterface {

	void execute(byte command, ByteBuffer PrgRom);

}
