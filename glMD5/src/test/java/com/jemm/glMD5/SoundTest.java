package com.jemm.glMD5;

/*
 *	OscillatorPlayer.java
 *
 *	This file is part of the Java Sound Examples.
 */

/*
 *  Copyright (c) 1999 -2001 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

import	java.io.IOException;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.SourceDataLine;


/*	+DocBookXML
	<title>Playing waveforms</title>

	<formalpara><title>Purpose</title>
	<para>
	Plays waveforms (sine, square, ...).
	</para></formalpara>

	<formalpara><title>Level</title>
	<para>Advanced</para></formalpara>

	<formalpara><title>Usage</title>
	<para>
	<cmdsynopsis>
	<command>java OscillatorPlayer</command>
	<arg><option>-t <replaceable>waveformtype</replaceable></option></arg>
	<arg><option>-f <replaceable>signalfrequency</replaceable></option></arg>
	<arg><option>-r <replaceable>samplerate</replaceable></option></arg>
	<arg><option>-a <replaceable>amplitude</replaceable></option></arg>
	</cmdsynopsis>
	</para>
	</formalpara>

	<formalpara><title>Parameters</title>
	<variablelist>
	<varlistentry>
	<term><option>-t <replaceable>waveformtype</replaceable></option></term>
	<listitem><para>the waveform to play. One of sine, sqaure, triangle and sawtooth. Default: sine.</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><option>-f <replaceable>signalfrequency</replaceable></option></term>
	<listitem><para>the frequency of the signal to create. Default: 1000 Hz.</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><option>-r <replaceable>samplerate</replaceable></option></term>
	<listitem><para>the sample rate to use. Default: 44.1 kHz.</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><option>-a <replaceable>amplitude</replaceable></option></term>
	<listitem><para>the amplitude of the generated signal. May range from 0.0 to 1.0. 1.0 means a full-scale wave. Default: 0.7.</para></listitem>
	</varlistentry>
	</variablelist>
	</formalpara>

	<formalpara><title>Bugs, limitations</title>
	<para>
	Full-scale waves can lead to clipping. It is currently not known
	which component is responsible for this.
	</para></formalpara>

	<formalpara><title>Source code</title>
	<para>
	<ulink url="OscillatorPlayer.java.html">OscillatorPlayer.java</ulink>,
	<ulink url="Oscillator.java.html">Oscillator.java</ulink>,
	<ulink url="http://www.urbanophile.com/arenn/hacking/download.html">gnu.getopt.Getopt</ulink>
	</para></formalpara>

-DocBookXML
*/
public class SoundTest /*implements Runnable*/ {

	private static final int	BUFFER_SIZE = 128000;
	private static boolean		DEBUG = false;
    private AudioInputStream oscillator;
    private float fAmplitude;
    private float fSignalFrequency;
    private float fSampleRate;


    private static void out(String strMessage)
	{
		System.out.println(strMessage);
	}


    public void setFrequency( double frequency ) {
        try {
            play( frequency, (double)fAmplitude, (double)fSampleRate );
        } catch( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
    }

    public void play( double frequency, double amplitude, double samplerate )
        throws	IOException
    {
        byte[]		abData;
        AudioFormat	audioFormat;
        int	nWaveformType = Oscillator.WAVEFORM_SINE;
        fSampleRate = 44100.0F;
        fSignalFrequency = 1000.0F;
        fAmplitude = 0.7F;

        fSampleRate = (float)samplerate;
        fSignalFrequency = (float)frequency;
        fAmplitude = (float)amplitude;

        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                       fSampleRate, 16, 2, 4, fSampleRate, false);
        oscillator = new Oscillator(
                    nWaveformType,
                    fSignalFrequency,
                    fAmplitude,
                    audioFormat,
                    AudioSystem.NOT_SPECIFIED);
        SourceDataLine	line = null;
        DataLine.Info	info = new DataLine.Info(
            SourceDataLine.class,
            audioFormat);
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
        while (true)
        {
            if (DEBUG) { out("OscillatorPlayer.main(): trying to read (bytes): " + abData.length); }
            int	nRead = oscillator.read(abData);
            if (DEBUG) { out("OscillatorPlayer.main(): in loop, read (bytes): " + nRead); }
            int	nWritten = line.write(abData, 0, nRead);
            if (DEBUG) { out("OscillatorPlayer.main(): written: " + nWritten); }
        }
    }
}
