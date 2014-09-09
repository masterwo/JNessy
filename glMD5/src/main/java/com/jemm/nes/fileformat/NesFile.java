package com.jemm.nes.fileformat;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import com.jemm.nes.fileformat.Header;

public class NesFile {
	private Header header;
	public ByteBuffer bbPRGROM;
	public ByteBuffer bbCHRROM;
	
	public void loadNesFile(String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileName);
        FileChannel inputChannel = inputStream.getChannel();
        
        ByteBuffer bbHeader = ByteBuffer.allocate(16);
		inputChannel.read(bbHeader);
		bbHeader.rewind();
        
        this.header = Header.createFromBytes(bbHeader);
        
        if(this.header.isTrainerPresent()) {
        	ByteBuffer bbTrainer = ByteBuffer.allocate(512);
        	inputChannel.read(bbTrainer);
        }
        
        bbPRGROM = ByteBuffer.allocate(16384 * this.header.getSizePRGROM());
        inputChannel.read(bbPRGROM);
        bbPRGROM.order(ByteOrder.LITTLE_ENDIAN);
        bbPRGROM.rewind();
        
        bbCHRROM = ByteBuffer.allocate(8192  * this.header.getSizeCHRROM());
        inputChannel.read(bbCHRROM);
        bbCHRROM.rewind();
        
        inputChannel.close();
        inputStream.close();
	}
	
	public Header getHeader() {
		return header;
	}
}
