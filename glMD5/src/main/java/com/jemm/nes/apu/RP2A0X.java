package com.jemm.nes.apu;

enum APU_CHANNLE_FLAGS
{
	PULSE1 		((byte)0x01), // (0000.0001)
	PULSE2 		((byte)0x02), // (0000.0010)
	TRIANGLE 	((byte)0x04), // (0000.0100)
	NOISE 		((byte)0x08), // (0000.1000)
	DMC 		((byte)0x10); // (0001.0000)
	
	private byte mask;
	APU_CHANNLE_FLAGS(int mask){
		this.mask = (byte)mask;
	}
	public byte mask(){ return mask; }
	
	/**
	 * Check if the channel is enabled in the apu status register.
	 * @param apu_status_reg
	 * @return
	 */
	public boolean isEnabled(int apu_status_reg){
		return (apu_status_reg & mask) != 0;
	}
}
public class RP2A0X {
	private int cyclecount = 0;
	Square1 sq1 = new Square1();
	Square2 sq2 = new Square2();
	Triangle tri = new Triangle();
	
	public RP2A0X() {
//		sq1.realPlayer();
//		sq1.initiate();
//		sq2.initiate();
//		tri.initiate();
        Thread t = new Thread(sq1);
        t.start();
        Thread t2 = new Thread(sq2);
        t2.start();
	}
	
	public void update() {
		ApuRegisters apuReg = ApuRegisters.getInstance();
//	Step	|APU cycles	|Envelopes & triangle's linear counter (Quarter frame)	|Length counters & sweep units (Half frame)	|Frame interrupt flag
//	--------|-----------|-------------------------------------------------------|-------------------------------------------|----------------------------------
//	1		|3728.5		|Clock	 	                                            |                                          	|
//	2		|7456.5		|Clock													|Clock	                                  	|
//	3		|11185.5	|Clock	 	                                            |                                          	|
//	4		|14914	 	| 														|											|Set if interrupt inhibit is clear
//			|14914.5	|Clock													|Clock										|Set if interrupt inhibit is clear
//			|0 (14915)	| 	 													|											|Set if interrupt inhibit is clear
//	 		|			|240 Hz (approx.)										|120 Hz (approx.)							|60 Hz (approx.)
		
		cyclecount++;
		if(cyclecount==7457) {
			clockEnvelopeAndLinearCounter();
		} else if(cyclecount==14913) {
			clockEnvelopeAndLinearCounter();
			clockLengthAndSweepUnits(apuReg);
		} else if(cyclecount==22371) {
			clockEnvelopeAndLinearCounter();
		} else if(cyclecount==29828) {

		} else if(cyclecount==29829) {
			clockEnvelopeAndLinearCounter();
			clockLengthAndSweepUnits(apuReg);
		} else if(cyclecount==29830) {
			cyclecount = 0;
		}
		
		if(APU_CHANNLE_FLAGS.PULSE1.isEnabled(apuReg.APUFLAGS)){ // if pulse_1 is enabled
			sq1.update();
		}
		if((apuReg.APUFLAGS & 2) != 0){ // if pulse_2 is enabled
			sq2.update();
		}
		// sq1.update();
		//sq2.update();
		//tri.update();
	}

	private void clockLengthAndSweepUnits(ApuRegisters apuReg) {
//			When the enabled bit is cleared (via $4015), the length counter is forced to 0 and cannot be changed until enabled is set again (the length counter's previous value is lost). There is no immediate effect when enabled is set.
//					When clocked by the frame counter, the length counter is decremented except when:
//					The length counter is 0, or
//					The halt flag is set
		if(!APU_CHANNLE_FLAGS.PULSE1.isEnabled(apuReg.APUFLAGS)) {
			sq1.lengthCounter = 0;
		} else if(sq1.lengthCounter != 0 && (apuReg.SQ1_ENV & 0x20) == 0) { // The length counter is 0, or The halt flag is set
			sq1.lengthCounter--;
		}
		if(!APU_CHANNLE_FLAGS.PULSE2.isEnabled(apuReg.APUFLAGS)) {
			sq2.lengthCounter = 0;
		} else if(sq2.lengthCounter != 0 && (apuReg.SQ2_ENV & 0x20) == 0) { // The length counter is 0, or The halt flag is set
			sq2.lengthCounter--;
		}
		if(!APU_CHANNLE_FLAGS.TRIANGLE.isEnabled(apuReg.APUFLAGS)) {
			tri.lengthCounter = 0;
		} else if(tri.lengthCounter != 0 && (apuReg.TRI_CTRL & 0x80) == 0) { // The length counter is 0, or The halt flag is set
			tri.lengthCounter--;
		}
//			if(!APU_CHANNLE_FLAGS.NOISE.isEnabled(apuReg.APUFLAGS)) {
//				noise.lengthCounter = 0;
//			} else if(noise.lengthCounter != 0 && (apuReg.NOISE_ENV & 0x20) == 0) { // The length counter is 0, or The halt flag is set
//				noise.lengthCounter--;
//			}
		
		
//			When clocked by the frame counter, one of three things happen:
//				If the reload flag is set, the divider's counter is set to the period P. If the divider's counter
//					was zero before the reload and the sweep is enabled, the pulse's period is also adjusted
//					(if the target period is in range; see below). The reload flag is then cleared.
//				If the reload flag is clear and the divider's counter is non-zero, it is decremented.
//				If the reload flag is clear and the divider's counter is zero and the sweep is enabled,
//					the counter is set to P and the pulse's period is adjusted (if the target period is in range; see below).
		if(sq1.sweepReloadFlag) {
			if(sq1.dividerCounter == 0 && (apuReg.SQ1_SWEEP & 0x80) != 0) {
//					The channel's 11-bit raw timer period is shifted right by the shift count (using a barrel shifter),
//					then either added to or subtracted from the channel's raw period, yielding the target period.
//					When the channel's current period is less than 8 or the target period is greater than $7FF,
//					the channel is silenced (0 is sent to the mixer) but the channel's current period remains unchanged.
//					Otherwise, if the enable flag is set and the shift count is non-zero, when the divider outputs a clock,
//					the channel's period is updated.
				
				int nShift = (apuReg.SQ1_SWEEP & 0x07);
				short rawTimer = (short) (((apuReg.SQ1_HI & 0x07) << 8) + apuReg.SQ1_LO);
				int pulsePeriodDelta = ((rawTimer >>> nShift) | (rawTimer << (10 - nShift))) & 0x07FF;
				if((apuReg.SQ1_SWEEP & 0x08) != 0) {
					sq1.pulsePeriod -= pulsePeriodDelta;
				} else {
					sq1.pulsePeriod += pulsePeriodDelta;
				}
				sq1.realPlayer();
			}
			sq1.dividerCounter = ((apuReg.SQ1_SWEEP & 0x70) >>> 4) + 1; // TODO: Investigate + 1 http://wiki.nesdev.com/w/index.php/APU_Sweep
			sq1.sweepReloadFlag = false;
		} else {
			// if sweepReloadFlag is cleared then...
			if(sq1.dividerCounter != 0) {
				sq1.dividerCounter--;
			} else if((apuReg.SQ1_SWEEP & 0x80) != 0) {
				sq1.dividerCounter = ((apuReg.SQ1_SWEEP & 0x70) >>> 4) + 1; // TODO: Investigate + 1 http://wiki.nesdev.com/w/index.php/APU_Sweep
				
				int nShift = (apuReg.SQ1_SWEEP & 0x07);
				short rawTimer = (short) (((apuReg.SQ1_HI & 0x07) << 8) + apuReg.SQ1_LO);
				int pulsePeriodDelta = ((rawTimer >>> nShift) | (rawTimer << (10 - nShift))) & 0x07FF;
				if((apuReg.SQ1_SWEEP & 0x08) != 0) {
					sq1.pulsePeriod -= pulsePeriodDelta;
				} else {
					sq1.pulsePeriod += pulsePeriodDelta;
				}
				sq1.realPlayer();
			}
		}
		
		if(sq2.sweepReloadFlag) {
			if(sq2.dividerCounter == 0 && (apuReg.SQ2_SWEEP & 0x80) != 0) {
//					The channel's 11-bit raw timer period is shifted right by the shift count (using a barrel shifter),
//					then either added to or subtracted from the channel's raw period, yielding the target period.
//					When the channel's current period is less than 8 or the target period is greater than $7FF,
//					the channel is silenced (0 is sent to the mixer) but the channel's current period remains unchanged.
//					Otherwise, if the enable flag is set and the shift count is non-zero, when the divider outputs a clock,
//					the channel's period is updated.
				
				int nShift = (apuReg.SQ2_SWEEP & 0x07);
				short rawTimer = (short) (((apuReg.SQ2_HI & 0x07) << 8) + apuReg.SQ2_LO);
				int pulsePeriodDelta = ((rawTimer >>> nShift) | (rawTimer << (10 - nShift))) & 0x07FF;
				if((apuReg.SQ2_SWEEP & 0x08) != 0) {
					sq2.pulsePeriod -= pulsePeriodDelta;
				} else {
					sq2.pulsePeriod += pulsePeriodDelta;
				}
				sq2.realPlayer();
			}
			sq2.dividerCounter = ((apuReg.SQ2_SWEEP & 0x70) >>> 4) + 1; // TODO: Investigate + 1 http://wiki.nesdev.com/w/index.php/APU_Sweep
			sq2.sweepReloadFlag = false;
		} else {
			// if sweepReloadFlag is cleared then...
			if(sq2.dividerCounter != 0) {
				sq2.dividerCounter--;
			} else if((apuReg.SQ2_SWEEP & 0x80) != 0) {
				sq2.dividerCounter = ((apuReg.SQ2_SWEEP & 0x70) >>> 4) + 1; // TODO: Investigate + 1 http://wiki.nesdev.com/w/index.php/APU_Sweep
				
				int nShift = (apuReg.SQ2_SWEEP & 0x07);
				short rawTimer = (short) (((apuReg.SQ2_HI & 0x07) << 8) + apuReg.SQ2_LO);
				int pulsePeriodDelta = ((rawTimer >>> nShift) | (rawTimer << (10 - nShift))) & 0x07FF;
				if((apuReg.SQ2_SWEEP & 0x08) != 0) {
					sq2.pulsePeriod -= pulsePeriodDelta;
				} else {
					sq2.pulsePeriod += pulsePeriodDelta;
				}
				sq2.realPlayer();
			}
		}
	}

	private void clockEnvelopeAndLinearCounter() {
//			When the frame counter generates a linear counter clock, the following actions occur in order:
//				If the linear counter reload flag is set, the linear counter is reloaded with the counter reload value, otherwise if the linear counter is non-zero, it is decremented.
//				If the control flag is clear, the linear counter reload flag is cleared.
		if(tri.linearCounterReloadFlag) {
			tri.linearCounter = (byte)(ApuRegisters.getInstance().TRI_CTRL & 0x7F); // 0x7F(0111.1111)
		} else if(tri.linearCounter != 0) {
			tri.linearCounter--;
		}
		if((byte)(ApuRegisters.getInstance().TRI_CTRL & 0x80) == 0) {
			tri.linearCounterReloadFlag = false;
		}
		
//			When clocked by the frame counter, one of two actions occurs: if the start flag is clear, the divider is clocked, otherwise the start flag is cleared, the counter is loaded with 15, and the divider's period is immediately reloaded.
		if(sq1.startFlag == false) {
			sq1.clockDivider();
		} else {
			sq1.startFlag = false;
			sq1.dividerCounter = 15;
			sq1.dividerPeriod = ApuRegisters.getInstance().SQ1_ENV & 0x0F;
			sq1.realPlayer();
		}
		if(sq2.startFlag == false) {
			sq2.clockDivider();
		} else {
			sq2.startFlag = false;
			sq2.dividerCounter = 15;
			sq2.dividerPeriod = ApuRegisters.getInstance().SQ2_ENV & 0x0F;
			sq2.realPlayer();
		}
//			if(noise.startFlag == false) {
//				noise.clockDivider();
//			} else {
//				noise.startFlag = false;
//				noise.dividerCounter = 15;
//				noise.period = ApuRegisters.getInstance().NOISE_ENV & 0x0F;
//			}
	}
}
