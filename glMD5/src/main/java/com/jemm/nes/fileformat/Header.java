/**
 * 
 */
package com.jemm.nes.fileformat;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author xjoslih
 *
 */
public class Header {
	public byte[] getNesConstant() {
		return nesConstant;
	}

	public int getSizePRGROM() {
		return sizePRGROM & 0xFF;
	}

	public int getSizeCHRROM() {
		return sizeCHRROM & 0xFF;
	}

	public int getSizePRGRAM() {
		return sizePRGRAM & 0xFF;
	}

	public byte getFlags10() {
		return flags10;
	}

	private byte[] nesConstant = new byte[4];
	private byte sizePRGROM;
	private byte sizeCHRROM;
	private byte flags6;
	private byte flags7;
	private byte sizePRGRAM;
	private byte flags9;
	private byte flags10;
	private byte[] zeroFilled = new byte[5];
	
	Header(){}
	
    public static Header createFromBytes(ByteBuffer buf) throws IOException {
        final Header header = new Header();

        // If needed...
        //buf.order(ByteOrder.LITTLE_ENDIAN);

        buf.get(header.nesConstant, 0, 4);
        header.sizePRGROM = buf.get();
        header.sizeCHRROM = buf.get();
        header.flags6 = buf.get();
        header.flags7 = buf.get();
        header.sizePRGRAM = buf.get();
        header.flags9 = buf.get();
        header.flags10 = buf.get();
        buf.get(header.zeroFilled, 0, 5);
        
        // ...extract other fields here

        // Example to convert unsigned short to a positive int
        //data.tc_2_as_int = buf.getShort() & 0xffff;

        return header;        
    }
    
    public boolean isPal() {
    	return ((flags9 & 0x01) != 0);
    }
    
    public boolean isVsSystem() {
    	return ((flags7 & 0x01) != 0);
    }
    
    //512-byte trainer at $7000-$71FF.
    public boolean isTrainerPresent() {
    	return ((flags6 & 0x04) != 0);
    }
}
