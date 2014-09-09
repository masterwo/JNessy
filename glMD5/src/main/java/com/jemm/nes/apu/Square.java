package com.jemm.nes.apu;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Square {
    private final static float SAMPLE_RATE = 44000f;
    protected int currentHI;
    protected int currentLO;
    private byte[] buffer;
    private int offset = 0;
    
    private SourceDataLine sdl;
    
    public Square() {
        currentHI = ApuRegisters.getInstance().SQ1_HI;
        currentLO = ApuRegisters.getInstance().SQ1_LO;
    }
    
    public void initiate() {
        AudioFormat af = new AudioFormat(SAMPLE_RATE,8,1,true,false);
		try {
			sdl = AudioSystem.getSourceDataLine(af);
			sdl.open(af);
			sdl.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void update() {
    	float hz = -1;
    	if(currentHI != ApuRegisters.getInstance().SQ1_HI || currentLO != ApuRegisters.getInstance().SQ1_LO) {
    		int timer = ((ApuRegisters.getInstance().SQ1_HI << 8) + (ApuRegisters.getInstance().SQ1_LO & 0xFF)) & 0xFFFF;
    		hz = (float) ((1789772.67)/(16*(timer + 1)));
    		try {
				play(hz,10000,0.1);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(buffer != null) {
    		int available = sdl.available();
    		while(available > 0) {
	    		if(available + offset > buffer.length) {
	    			int resten = buffer.length - offset;
	    			available -= sdl.write(buffer,offset,resten);
	    			offset = 0;
	    		} else {
	    			offset += sdl.write(buffer,offset,available);
	    			available = 0;
	    		}
    		}
    	}
    }
    
    public void close() {
        sdl.drain();
        sdl.close();
    }

    private void play(float hz, int msecs, double vol) throws LineUnavailableException {
        if (hz <= 0)
            throw new IllegalArgumentException("Frequency <= 0 hz");

        if (msecs <= 0)
            throw new IllegalArgumentException("Duration <= 0 msecs");

        if (vol > 1.0 || vol < 0.0)
            throw new IllegalArgumentException("Volume out of range 0.0 - 1.0");

        buffer = new byte[(int) ((SAMPLE_RATE / hz)+0.5)];//new byte[((int)SAMPLE_RATE * msecs / 1000)];
        
        float byteFrequency = (SAMPLE_RATE / hz);
        float patternByteLength = (SAMPLE_RATE / hz)/8;
        
        for (int i=0; i<buffer.length; i++) {
       	 if((i%byteFrequency)>=patternByteLength && (i%byteFrequency)<(patternByteLength*5)) {
       		buffer[i] = ((byte) (127 * vol));
       	 } else {
       		buffer[i] = ((byte) (-127 * vol));
       	 }
        }
    }
}
