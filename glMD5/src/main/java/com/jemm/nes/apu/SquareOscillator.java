package com.jemm.nes.apu;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class SquareOscillator extends AudioInputStream{
	private byte[] m_abData;
	private int	m_nBufferPosition;
	private long m_lRemainingFrames;
	
	public SquareOscillator(
			float fSignalFrequency,
			float fAmplitude,
			AudioFormat audioFormat,
			long lLength) {
		super(new ByteArrayInputStream(new byte[0]),
			      new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					      audioFormat.getSampleRate(),
					      16,
					      2,
					      4,
					      audioFormat.getFrameRate(),
					      audioFormat.isBigEndian()),
			      lLength);
		m_lRemainingFrames = lLength;
		fAmplitude = (float) (fAmplitude * Math.pow(2, getFormat().getSampleSizeInBits() - 1));
		// length of one period in frames
		int nPeriodLengthInFrames = Math.round(getFormat().getFrameRate() / fSignalFrequency);
		int nBufferLength = nPeriodLengthInFrames * getFormat().getFrameSize();
		m_abData = new byte[nBufferLength];
		for (int nFrame = 0; nFrame < nPeriodLengthInFrames; nFrame++)
		{
			/**	The relative position inside the period
				of the waveform. 0.0 = beginning, 1.0 = end
			*/
			float	fPeriodPosition = (float) nFrame / (float) nPeriodLengthInFrames;
			float	fValue = 0;
			
			fValue = (fPeriodPosition < 0.5F) ? 1.0F : -1.0F;
			
			int	nValue = Math.round(fValue * fAmplitude);
			int nBaseAddr = (nFrame) * getFormat().getFrameSize();
			// this is for 16 bit stereo, little endian
			m_abData[nBaseAddr + 0] = (byte) (nValue & 0xFF);
			m_abData[nBaseAddr + 1] = (byte) ((nValue >>> 8) & 0xFF);
			m_abData[nBaseAddr + 2] = (byte) (nValue & 0xFF);
			m_abData[nBaseAddr + 3] = (byte) ((nValue >>> 8) & 0xFF);
		}
		m_nBufferPosition = 0;
	}
	
	public int read(byte[] abData, int nOffset, int nLength)
			throws IOException
		{
			if (nLength % getFormat().getFrameSize() != 0)
			{
				throw new IOException("length must be an integer multiple of frame size");
			}
			int	nConstrainedLength = Math.min(available(), nLength);
			int	nRemainingLength = nConstrainedLength;
			while (nRemainingLength > 0)
			{
				int	nNumBytesToCopyNow = m_abData.length - m_nBufferPosition;
				nNumBytesToCopyNow = Math.min(nNumBytesToCopyNow, nRemainingLength);
				System.arraycopy(m_abData, m_nBufferPosition, abData, nOffset, nNumBytesToCopyNow);
				nRemainingLength -= nNumBytesToCopyNow;
				nOffset += nNumBytesToCopyNow;
				m_nBufferPosition = (m_nBufferPosition + nNumBytesToCopyNow) % m_abData.length;
			}
			int	nFramesRead = nConstrainedLength / getFormat().getFrameSize();
			if (m_lRemainingFrames != AudioSystem.NOT_SPECIFIED)
			{
				m_lRemainingFrames -= nFramesRead;
			}
			int	nReturn = nConstrainedLength;
			if (m_lRemainingFrames == 0)
			{
				nReturn = -1;
			}
			return nReturn;
		}
}
