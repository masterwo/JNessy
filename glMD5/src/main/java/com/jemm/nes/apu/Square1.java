package com.jemm.nes.apu;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.jemm.glMD5.Oscillator;

public class Square1 implements Runnable {
    private final static float SAMPLE_RATE = 44100f;
    protected int currentHI = ApuRegisters.getInstance().SQ1_HI;
    protected int currentLO = ApuRegisters.getInstance().SQ1_LO;
    private byte[] buffer;
    private int offset = ApuRegisters.getInstance().FRAME_COUNTER;
    
    private byte oldFrameCounter = 0;
    
    private byte[] duty125 = {0,1,0,0,0,0,0,0};
    private byte[] duty25  = {0,1,1,0,0,0,0,0};
    private byte[] duty50  = {0,1,1,1,1,0,0,0};
    private byte[] duty75  = {1,0,0,1,1,1,1,1};
    
    int sequencer = 0;
    
    private SourceDataLine sdl;
	public static boolean startFlag;
	public int dividerCounter;
	public int dividerPeriod;
	public boolean sweepReloadFlag;		
	public int pulsePeriod;				// The pitch of the sound.
	public static int lengthCounter; 	// Length of the tone.
    
    public Square1() {
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
    	if(ApuRegisters.getInstance().FRAME_COUNTER == oldFrameCounter)
    		return;
    		
    	oldFrameCounter = ApuRegisters.getInstance().FRAME_COUNTER; 
    	
    	
    	
    	float hz = -1;
    	ApuRegisters reg = ApuRegisters.getInstance();
    	if(currentHI != ApuRegisters.getInstance().SQ1_HI || currentLO != ApuRegisters.getInstance().SQ1_LO) {
    		double timer = ((ApuRegisters.getInstance().SQ1_HI << 8) + (ApuRegisters.getInstance().SQ1_LO & 0xFF)) & 0xFFFF;
    		hz = (float) ((1789772.67)/(16*((float)timer + 1.0f)));
    		try {
				play(hz,10000,0.1);
    			//play(hz, timer, 0.1);
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

	public void clockDivider() {
		// TODO Auto-generated method stub
		
	}
	
	byte[]		abData;
	int	nWaveformType = Oscillator.WAVEFORM_SINE;
	float	fSampleRate = 44100.0F;
	float	fSignalFrequency = 1000.0F;
	float	fAmplitude = 0.7F;
	
	AudioFormat	audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
			   fSampleRate, 16, 2, 4, fSampleRate, false);
	
	SourceDataLine	line = null;
	DataLine.Info	info = new DataLine.Info(
		SourceDataLine.class,
		audioFormat);
	
	AudioInputStream oscillator = null;
	
	private static final int	BUFFER_SIZE = 128000;
	
	void realPlayer() {
	    float l_pulsePeriod = ((ApuRegisters.getInstance().SQ1_HI << 8) + (ApuRegisters.getInstance().SQ1_LO & 0xFF)) & 0xFFFF;
		float fSignalFrequency = 0;
		if(l_pulsePeriod > 8) {
			fSignalFrequency = (float) ((1789772.67)/(16*((float)l_pulsePeriod + 1.0f)));
		}
		
		oscillator = new Oscillator(
			nWaveformType,
			fSignalFrequency,
			fAmplitude,
			audioFormat,
			AudioSystem.NOT_SPECIFIED);
		try
		{
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		line.start();
	
		abData = new byte[BUFFER_SIZE];
	}
	
	void realPlayerUpdate() {
		while(oscillator == null);
		int nRead = 0;
		try {
			nRead = oscillator.read(abData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		line.write(abData, 0, nRead);
	}

	@Override
	public void run() {
		while(true) {
			realPlayerUpdate();
		}
	}
}