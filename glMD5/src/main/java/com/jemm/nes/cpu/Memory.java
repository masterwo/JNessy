package com.jemm.nes.cpu;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.jemm.nes.apu.ApuRegisters;
import com.jemm.nes.apu.Square1;
import com.jemm.nes.apu.Square2;
import com.jemm.nes.apu.Triangle;
import com.jemm.nes.peripherals.JoyPad;
import com.jemm.nes.ppu.PpuMem;
import com.jemm.nes.ppu.PpuRegisters;

public class Memory {
	private ByteBuffer CPU_ROM;
	
	private ByteBuffer internalRam;
	private PpuRegisters ppuRegisters;
	private ApuRegisters apuRegisters = ApuRegisters.getInstance();
	
	private ByteBuffer executionMem;
	
	private JoyPad joyPad = new JoyPad();
	
	private static Memory instance = new Memory();
	
	public static Memory getInstance() {
		return instance;
	}
	
	private Memory() {
		CPU_ROM = ByteBuffer.allocate(32768);
		
		executionMem = CPU_ROM;
		ppuRegisters = PpuMem.getInstance().getRegister();
	}
	
	public void initiateMemory(ByteBuffer bbPRGROM) {
		//CPU_ROM = ByteBuffer.allocate(32768); Done in constructor
		bbPRGROM.rewind();
		
		if(bbPRGROM.capacity() > 16384) {
			bbPRGROM.limit(32768);
			CPU_ROM.put(bbPRGROM);//Put 32kb into cpu mem.
		} else {
			bbPRGROM.limit(16384);
			CPU_ROM.position(16384);
			CPU_ROM.put(bbPRGROM);//Put 16kb into cpu mem.
		}
		
		CPU_ROM.rewind();
		CPU_ROM.order(ByteOrder.LITTLE_ENDIAN);
		bbPRGROM.rewind();
		
		internalRam = ByteBuffer.allocate(0x2000);
		internalRam.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	public short cmGet(int memIndex) {
		if(memIndex >= 0x8000) {
			return (short) (CPU_ROM.get(memIndex-0x8000) & 0xFF);//Offset to start at 0.
		} else if(memIndex >= 0x4016) {
			return joyPad.get();
		} else if(memIndex >= 0x4000) {
			switch((byte)(memIndex & 0xFF)) {
			case 0x00:
				return apuRegisters.SQ1_ENV;
			case 0x02:
				return apuRegisters.SQ1_LO;
			case 0x03:
				return apuRegisters.SQ1_HI;
			case 0x04:
				return apuRegisters.SQ2_ENV;
			case 0x05:
				return apuRegisters.SQ2_SWEEP;
			case 0x06:
				return apuRegisters.SQ2_LO;
			case 0x07:
				return apuRegisters.SQ2_HI;
			case 0x08:
				return apuRegisters.TRI_CTRL;
			case 0x0A:
				return apuRegisters.TRI_LO;
			case 0x0B:
				return apuRegisters.TRI_HI;
			case 0x15:
				return apuRegisters.APUFLAGS;
			default:
				//System.out.println("Unsupported Sound memory operation.");
				break;
			}
		} else if(memIndex >= 0x2000) {
			switch((memIndex & 0xFF)) {
				case 0x00:
					return ppuRegisters.PPUCTRL;
				case 0x01:
					return ppuRegisters.PPUMASK;
				case 0x02:
					return ppuRegisters.getPPUSTATUS();
				case 0x03:
					return ppuRegisters.OAMADDR;
				case 0x04:
					return ppuRegisters.OAMDATA;
				case 0x05:
					//return ppuRegisters.PPUSCROLL;
				case 0x06:
					//return ppuRegisters.PPUADDR;TODO fix?
				case 0x07:
					return ppuRegisters.getPPUDATA();
				default:
					System.out.println("Unimplemented video memory operation:" + memIndex);
			}
		} else {
			if(memIndex > 0x2000 || memIndex < 0) {//TODO remove
				//TODO Throw memory exception
				System.out.println("Unimplemented memory operation: " + memIndex);
			}
			return (short) (internalRam.get(memIndex) & 0xFF);
		}
		return -1;
	}
	
	public void cmPut(int memIndex, short data) {
		if(memIndex >= 0x8000){
			CPU_ROM.put(memIndex-0x8000, (byte)data);//Offset to start from 0.
		} else if(memIndex >= 0x6000) {
			System.out.print(data);
		} else if(memIndex == 0x4017) {
			apuRegisters.FRAME_COUNTER = (byte) data;
		} else if(memIndex >= 0x4016) {
			joyPad.set(data);
		} else if(memIndex == 0x4014) { //SPECIAL CASE OAM
			PpuMem.getInstance().OAM_DATA.clear();
			PpuMem.getInstance().OAM_DATA.put(internalRam.array(), 0x0100*data, 256);
		} else if(memIndex >= 0x4000) {
			switch((byte)(memIndex & 0xFF)) {
				case 0x00:
					apuRegisters.SQ1_ENV = (byte) data;
					break;
				case 0x01:
					apuRegisters.SQ1_SWEEP = (byte) data;
					break;
				case 0x02:
					apuRegisters.SQ1_LO = (byte) data;
					break;
				case 0x03:
					apuRegisters.SQ1_HI = (byte) data;
					Square1.lengthCounter = apuRegisters.LENGTH_COUNTER_TABLE[apuRegisters.SQ1_HI >>> 3];
					Square1.startFlag = true;
					break;
				case 0x04:
					apuRegisters.SQ2_ENV = (byte) data;
					break;
				case 0x05:
					apuRegisters.SQ2_SWEEP = (byte) data;
					break;
				case 0x06:
					apuRegisters.SQ2_LO = (byte) data;
					break;
				case 0x07:
					apuRegisters.SQ2_HI = (byte) data;
					Square2.lengthCounter = apuRegisters.LENGTH_COUNTER_TABLE[apuRegisters.SQ2_HI >>> 3];
					break;
				case 0x08:
					apuRegisters.TRI_CTRL = (byte) data;
					break;
				case 0x0A:
					apuRegisters.TRI_LO = (byte) data;
					break;
				case 0x0B:
					apuRegisters.TRI_HI = (byte) data;
					Triangle.linearCounterReloadFlag = true;
					Triangle.lengthCounter = apuRegisters.LENGTH_COUNTER_TABLE[apuRegisters.TRI_HI >>> 3];
					break;
				case 0x0C:
					//Noise generator.
					break;
				case 0x11:
					//apuRegisters.TRI_HI = (byte) data;//TODO DMC
					break;
				case 0x14:
					break;
				case 0x15:
					apuRegisters.APUFLAGS = (byte) data;
					break;
				default:
//					byte r = (byte)(memIndex & 0xFF);
//					System.out.println("Unsupported Sound memory operation.");
					break;
			}
		} else if(memIndex >= 0x2000) {
			switch((memIndex & 0xFF)) {
				case 0x00:
					ppuRegisters.PPUCTRL = (byte)data;
					break;
				case 0x01:
					ppuRegisters.PPUMASK = (byte)data;
					break;
				case 0x02:
					ppuRegisters.setPPUSTATUS((byte)data);
					break;
				case 0x03:
					ppuRegisters.OAMADDR = (byte)data;
					break;
				case 0x04:
					ppuRegisters.OAMDATA = (byte)data;
					break;
				case 0x05:
					ppuRegisters.setPPUSCROLL((byte)data);
					break;
				case 0x06:
					ppuRegisters.setPPUADDR((byte)data);
					break;
				case 0x07:
					ppuRegisters.setPPUDATA((byte)data);
					break;
				default:
					System.out.println("Unimplemented video memory operation:" + memIndex);
					break;
			}
		} else {
			if(memIndex > 0x2000 || memIndex < 0) {//TODO remove
				//TODO Throw memory exception
				System.out.println("Unimplemented memory operation:" + memIndex);
			}
			internalRam.put(memIndex, (byte) data);
		}
	}
	
	public void cmPutShort(int memIndex, int data) {
		if(memIndex >= 0x8000){
			CPU_ROM.putShort(memIndex-0x8000, (short)data);//Offset to start from 0.
		} else if(memIndex >= 0x4016) {
			System.out.print(data);
		} else if(memIndex >= 0x4000) {
			switch((byte)(memIndex & 0xFF)) {
				default:
					//System.out.println("Unsupported Sound memory operation.");
					break;
			}
		} else if(memIndex >= 0x2000) {
			switch((memIndex & 0xFF)) {
				default:
					System.out.println("Unimplemented video memory operation:" + memIndex);
					break;
			}
		} else {
			if(memIndex > 0x2000 || memIndex < 0) {//TODO remove
				//TODO Throw memory exception
				System.out.println("Unimplemented memory operation:" + memIndex);
			}
			internalRam.putShort(memIndex, (short) data);
		}
	}
	
	public int cmGetShort(int memIndex) {
		if(memIndex >= 0x8000){
			return CPU_ROM.getShort(memIndex-0x8000) & 0xFFFF;//Remove signedness
		} else if(memIndex >= 0x4016) {
			return joyPad.get();
		} else if(memIndex >= 0x2000) {
			switch((memIndex & 0xFF)) {
			case 0x00:
				return ppuRegisters.PPUCTRL;
			case 0x01:
				return ppuRegisters.PPUMASK;
			case 0x02:
				return ppuRegisters.getPPUSTATUS();
			case 0x03:
				return ppuRegisters.OAMADDR;
			case 0x04:
				return ppuRegisters.OAMDATA;
			case 0x05:
				//return ppuRegisters.PPUSCROLL;
			case 0x06:
				//return ppuRegisters.PPUADDR;//TODO fix?
			case 0x07:
				return ppuRegisters.getPPUDATA();
			default:
				System.out.println("Unimplemented video memory operation:" + memIndex);
				return -1;
			}
		} else {
			if(memIndex > 0x2000 || memIndex < 0) {//TODO remove
				//TODO Throw memory exception
				System.out.println("Unimplemented memory operation:" + memIndex);
			}
			return internalRam.getShort(memIndex) & 0xFFFF;
		}
	}

	public short cmGet() {
		return (short) (executionMem.get() & 0xFF);//Remove signedness
	}

	public int cmGetShort() {
		return executionMem.getShort()& 0xFFFF;//Remove signedness.
	}
	
	public int cmPosition() {
		//return CPU_ROM.position()+0x8000;
		if(executionMem.equals(CPU_ROM)) {
			return executionMem.position()+0x8000;
		} else {
			return executionMem.position();
		}
	}
	
	public void cmSetPosition(int position) {
		if(position >= 0x8000) {
			executionMem = CPU_ROM;
			executionMem.position(position-0x8000);//Offset to 0.
		} else {
			executionMem = internalRam;
			internalRam.position(position);
		}
		
	}
}
