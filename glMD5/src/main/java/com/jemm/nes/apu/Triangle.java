package com.jemm.nes.apu;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Triangle {
    private final static float SAMPLE_RATE = 44100f;
    protected int currentHI = ApuRegisters.getInstance().TRI_HI;
    protected int currentLO = ApuRegisters.getInstance().TRI_LO;
    private byte[] buffer;
    private int offset = 0;
    
    public static boolean linearCounterReloadFlag = false;
    public byte linearCounter = 0;
    
    private SourceDataLine sdl;
	public static int lengthCounter;
    
    public Triangle() {
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
    	if(currentHI != ApuRegisters.getInstance().TRI_HI || currentLO != ApuRegisters.getInstance().TRI_LO) {
    		int timer = ((ApuRegisters.getInstance().TRI_HI << 8) + (ApuRegisters.getInstance().TRI_LO & 0xFF)) & 0xFFFF;
    		hz = (float) ((1789772.67)/(32*(timer + 1)));
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
        float sampleStep = (511/(byteFrequency-1));
        
        
        for(int i = 0; i < buffer.length-(buffer.length/2); i++) {
        	buffer[i] = (byte) (((i*sampleStep)-128)*vol);
        }
        for(int i = buffer.length-(buffer.length/2); i < buffer.length; i++) {
        	buffer[i] = (byte) ((511-((i*sampleStep)-128))*vol);
        }

    }
}
