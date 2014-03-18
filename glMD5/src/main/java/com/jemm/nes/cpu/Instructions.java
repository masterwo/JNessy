package com.jemm.nes.cpu;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class Instructions {
	static final byte FLAGS_CY_SHIFT = 0;
	static final byte FLAGS_ZE_SHIFT = 1;
	static final byte FLAGS_IN_SHIFT = 2;
	static final byte FLAGS_DE_SHIFT = 3;
	static final byte FLAGS_BR_SHIFT = 4;
	static final byte FLAGS_OV_SHIFT = 6;
	static final byte FLAGS_NE_SHIFT = 7;
	
	static final byte FLAGS_CY_MASK = (1 << FLAGS_CY_SHIFT);
	static final byte FLAGS_ZE_MASK = (1 << FLAGS_ZE_SHIFT);
	static final byte FLAGS_ZE_MASK = (1 << FLAGS_IN_SHIFT);
	static final byte FLAGS_ZE_MASK = (1 << FLAGS_DE_SHIFT);
	static final byte FLAGS_ZE_MASK = (1 << FLAGS_BR_SHIFT);
	static final byte FLAGS_OV_MASK = (1 << FLAGS_OV_SHIFT);
	static final byte FLAGS_NE_MASK = (byte) (1 << FLAGS_NE_SHIFT);
	
	public static void main(String[] args) {
		
		/*
		Instructions instructions = new Instructions();
		
		static short testData[] =
		{
		  0,
		  1,
		  0x7F,
		   0x80,
		   0x81,
		   0xFF
		};
		
		  int aidx, bidx, c;

		  System.out.println("ADC:");
		  for (c = 0; c <= 1; c++)
		    for (aidx = 0; aidx < testData.length; aidx++)
		      for (bidx = 0; bidx < testData.length; bidx++)
		      {
		    	registers.AC = testData[aidx];
		        short b = testData[bidx];
		        registers.SR = (byte) (c << FLAGS_CY_SHIFT);
		        System.out.print((registers.AC & 0xFF) + "(" + registers.AC  + ") + " + (b & 0xFF) + "(" + b + ") + " + c + " = ");
		        Adc(b);
		        System.out.println((registers.AC & 0xFF) + "(" + registers.AC  + ") " + "CY=" + ((registers.SR & FLAGS_CY_MASK) != 0 ? 1 : 0) + "," + " 0V=" + ((registers.SR & FLAGS_OV_MASK) != 0 ? 1 : 0));
		      }
		*/
	}
	
	private static HashMap<Byte, FunctionInterface> instructionMap = new HashMap<Byte, FunctionInterface>();
	
	private static Registers registers = Registers.getInstance();
	
	public void execute(byte command, ByteBuffer PrgRom) {
		command = PrgRom.get();
		instructionMap.get(command).execute(command, PrgRom);
	}
	
	public Instructions() {
		instructionMap.put((byte) 0x69, adc);
		instructionMap.put((byte) 0x65, adc);
		instructionMap.put((byte) 0x75, adc);
		instructionMap.put((byte) 0x6D, adc);
		instructionMap.put((byte) 0x7D, adc);
		instructionMap.put((byte) 0x79, adc);
		instructionMap.put((byte) 0x61, adc);
		instructionMap.put((byte) 0x71, adc);
		
		instructionMap.put((byte) 0x29, and);
		instructionMap.put((byte) 0x25, and);
		instructionMap.put((byte) 0x35, and);
		instructionMap.put((byte) 0x2D, and);
		instructionMap.put((byte) 0x3D, and);
		instructionMap.put((byte) 0x39, and);
		instructionMap.put((byte) 0x21, and);
		instructionMap.put((byte) 0x31, and);
	}

	private static final ADC adc = new ADC();//	....	add with carry
	private static final AND and = new AND();//	....	and (with accumulator)
	private static final ASL asl = new ASL();//	....	arithmetic shift left
	private static final BCC bcc = new BCC();//	....	branch on carry clear
	private static final BCS bcs = new BCS();//	....	branch on carry set
	private static final BEQ beq = new BEQ();//	....	branch on equal (zero set)
	private static final BIT bit = new BIT();//	....	bit test
	private static final BMI bmi = new BMI();//	....	branch on minus (negative set)
	private static final BNE bne = new BNE();//	....	branch on not equal (zero clear)
	private static final BPL bpl = new BPL();//	....	branch on plus (negative clear)
	private static final BRK brk = new BRK();//	....	interrupt
	private static final BVC bvc = new BVC();//	....	branch on overflow clear
	private static final BVS bvs = new BVS();//	....	branch on overflow set
	private static final CLC clc = new CLC();//	....	clear carry
	private static final CLD cld = new CLD();//	....	clear decimal
	private static final CLI cli = new CLI();//	....	clear interrupt disable
	private static final CLV clv = new CLV();//	....	clear overflow
	private static final CMP cmp = new CMP();//	....	compare (with accumulator)
	private static final CPX cpx = new CPX();//	....	compare with X
	private static final CPY cpy = new CPY();//	....	compare with Y
	private static final DEC dec = new DEC();//	....	decrement
	private static final DEX dex = new DEX();//	....	decrement X
	private static final DEY dey = new DEY();//	....	decrement Y
	private static final EOR eor = new EOR();//	....	exclusive or (with accumulator)
	private static final INC inc = new INC();//	....	increment
	private static final INX inx = new INX();//	....	increment X
	private static final INY iny = new INY();//	....	increment Y
	private static final JMP jmp = new JMP();//	....	jump
	private static final JSR jsr = new JSR();//	....	jump subroutine
	private static final LDA lda = new LDA();//	....	load accumulator
	private static final LDX ldx = new LDX();//	....	load X
	private static final LDY ldy = new LDY();// 	....	load Y
	private static final LSR lsr = new LSR();//	....	logical shift right
	private static final NOP nop = new NOP();//	....	no operation
	private static final ORA ora = new ORA();//	....	or with accumulator
	private static final PHA pha = new PHA();//	....	push accumulator
	private static final PHP php = new PHP();//	....	push processor status (SR)
	private static final PLA pla = new PLA();//	....	pull accumulator
	private static final PLP plp = new PLP();//	....	pull processor status (SR)
	private static final ROL rol = new ROL();//	....	rotate left
	private static final ROR ror = new ROR();//	....	rotate right
	private static final RTI rti = new RTI();//	....	return from interrupt
	private static final RTS rts = new RTS();//	....	return from subroutine
	private static final SBC sbc = new SBC();//	....	subtract with carry
	private static final SEC sec = new SEC();//	....	set carry
	private static final SED sed = new SED();//	....	set decimal
	private static final SEI sei = new SEI();//	....	set interrupt disable
	private static final STA sta = new STA();//	....	store accumulator
	private static final STX stx = new STX();//	....	store X
	private static final STY sty = new STY();//	....	store Y
	private static final TAX tax = new TAX();//	....	transfer accumulator to X
	private static final TAY tay = new TAY();//	....	transfer accumulator to Y
	private static final TSX tsx = new TSX();//	....	transfer stack pointer to X
	private static final TXA txa = new TXA();//	....	transfer X to accumulator
	private static final TXS txs = new TXS();//	....	transfer X to stack pointer
	private static final TYA tya = new TYA();//	....	transfer Y to accumulator	
	
	//ADC	....	add with carry
	private static final class ADC implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			switch(command) {
				case 0x69:
					//Immediate
					short immediate = (short) (PrgRom.get() & 0xFF);//Remove signedness
					Adc(immediate);
					break;
				case 0x65:
					//Zeropage
					short zpAddr = (short) (PrgRom.get() & 0xFF);//Remove signedness
					short zeropage = Memory.getInstance().cmGet(zpAddr);
					Adc(zeropage);
					break;
				case 0x75:
					//Zeropage,x
					zpAddr = (short) (PrgRom.get() & 0xFF);//Remove signedness
					short zeropageX = Memory.getInstance().cmGet(zpAddr + registers.X);
					Adc(zeropageX);
					break;
				case 0x6D:
					//absolute
					int absAddr = (PrgRom.getShort() & 0xFFFF);//Remove signedness
					short absolute = Memory.getInstance().cmGet(absAddr);
					Adc(absolute);
					break;
				case 0x7D:
					//absolute,x
					absAddr = (PrgRom.getShort() & 0xFFFF);//Remove signedness
					short absoluteX = Memory.getInstance().cmGet(absAddr +  registers.X);
					Adc(absoluteX);
					break;
				case 0x79:
					//absolute,y
					absAddr = (PrgRom.getShort() & 0xFFFF);//Remove signedness
					short absoluteY = Memory.getInstance().cmGet(absAddr +  registers.Y);
					Adc(absoluteY);
					break;
				case 0x61:
					//(Indirect,x)
					short idAddr = (short) (PrgRom.get() & 0xFF);//Remove signedness
					short dAddr = Memory.getInstance().cmGet(idAddr);
					short indirectX = Memory.getInstance().cmGet(dAddr + registers.X);
					Adc(indirectX);
					break;
				case 0x71:
					//(Indirect),y
					idAddr = (short) (PrgRom.get() & 0xFF);//Remove signedness
					dAddr = Memory.getInstance().cmGet(idAddr);
					short indirectY = Memory.getInstance().cmGet(dAddr + registers.X);
					Adc(indirectY);
					break;
				default:
					//TODO Error.
					break;
			}
			
			if(registers.AC == 0x00) {
				registers.SR |=  FLAGS_ZE_MASK; // Set Zero Flag
			}
			
			if((registers.AC & 0x80) == 0x80) {
				registers.SR |=  FLAGS_NE_MASK; // Set Negative Flag
			} else {
				registers.SR &=  ~(FLAGS_NE_MASK); // clear Negative Flag
			}
			
		}
		
		private void Adc(short data) {
			int a = registers.AC;
			int b = data;
			int carryIns;
			int carryOut;

			// Calculate the carry-out depending on the carry-in and addends.
			//
			// carry-in = 0: carry-out = 1 IFF (a + b > 0xFF) or,
			// equivalently, but avoiding overflow in C: (a > 0xFF - b).
			//
			// carry-in = 1: carry-out = 1 IFF (a + b + 1 > 0xFF) or,
			// equivalently, (a + b >= 0xFF) or,
			// equivalently, but avoiding overflow in C: (a >= 0xFF - b).
			//
			// Also calculate the sum bits.
			if ((registers.SR & FLAGS_CY_MASK) == 0x01) {
				carryOut = ((a >= 0xFF - (b)) ? 1 : 0);
				registers.AC = (byte) (a + b + 1);
			} else {
				carryOut = ((a > (0xFF - b)) ? 1 : 0);
				registers.AC = (byte) (a + b);
			}
			/*
			 * #if 0 // Calculate the overflow by sign comparison. carryIns =
			 * ((a ^ b) ^ 0x80) & 0x80; if (carryIns) // if addend signs are
			 * different { // overflow if the sum sign differs from the sign of
			 * either of addends carryIns = ((*acc ^ a) & 0x80) != 0; } #else
			 */
			// Calculate all carry-ins.
			// Remembering that each bit of the sum =
			// addend a's bit XOR addend b's bit XOR carry-in,
			// we can work out all carry-ins from a, b and their sum.
			carryIns = registers.AC ^ a ^ b;

			// Calculate the overflow using the carry-out and
			// most significant carry-in.
			carryIns = (carryIns >> 7) ^ carryOut;
			// #endif

			// Update flags.
			registers.SR &= ~(FLAGS_CY_MASK | FLAGS_OV_MASK);
			registers.SR |= (carryOut << FLAGS_CY_SHIFT)
					| (carryIns << FLAGS_OV_SHIFT);
		}
	}
	
	//AND	....	and (with accumulator)
	private static final class AND implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			switch(command) {
				case 0x29:
					//Immediate
					short immediate = (short) (PrgRom.get() & 0xFF);//Remove signedness
					And(immediate);
					break;
				case 0x25:
					//Zeropage
					short zpAddr = (short) (PrgRom.get() & 0xFF);//Remove signedness
					short zeropage = Memory.getInstance().cmGet(zpAddr);
					And(zeropage);
					break;
				case 0x35:
					//Zeroapge,x
					zpAddr = (short) (PrgRom.get() & 0xFF);//Remove signedness
					short zeropageX = Memory.getInstance().cmGet(zpAddr + registers.X);
					And(zeropageX);
					break;
				case 0x2D:
					//Absolute
					int absAddr = (PrgRom.getShort() & 0xFFFF);//Remove signedness
					short absolute = Memory.getInstance().cmGet(absAddr);
					And(absolute);
					break;
				case 0x3D:
					//Absolute,x
					absAddr = (PrgRom.getShort() & 0xFFFF);//Remove signedness
					short absoluteX = Memory.getInstance().cmGet(absAddr +  registers.X);
					And(absoluteX);
					break;
				case 0x39:
					//Absolute,y
					absAddr = (PrgRom.getShort() & 0xFFFF);//Remove signedness
					short absoluteY = Memory.getInstance().cmGet(absAddr +  registers.Y);
					And(absoluteY);
					break;
				case 0x21:
					//(Indirect,x)
					short idAddr = (short) (PrgRom.get() & 0xFF);//Remove signedness
					short dAddr = Memory.getInstance().cmGet(idAddr);
					short indirectX = Memory.getInstance().cmGet(dAddr + registers.X);
					And(indirectX);
					break;
				case 0x31:
					//(Indirect,y)
					idAddr = (short) (PrgRom.get() & 0xFF);//Remove signedness
					dAddr = Memory.getInstance().cmGet(idAddr);
					short indirectY = Memory.getInstance().cmGet(dAddr + registers.X);
					And(indirectY);
					break;
				default:
					//TODO Error
					break;
			}
			
			if(registers.AC == 0x00) {
				registers.SR |=  FLAGS_ZE_MASK; // Set Zero Flag
			}
			
			if((registers.AC & 0x80) == 0x80) {
				registers.SR |=  FLAGS_NE_MASK; // Set Negative Flag
			} else {
				registers.SR &=  ~(FLAGS_NE_MASK); // clear Negative Flag
			}
		}
		
		private void And(short data) {
			registers.AC &= data;
		}
	}
	
	//ASL	....	arithmetic shift left
	private static  final class ASL implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			
			switch(command) {
			case 0x0A:
				//Accumulator
				updateCYFlag(registers.AC);
				registers.AC = (short) (registers.AC << 1);
				break;
			case 0x06:
				//Zeropage
				int zpAddr = (PrgRom.get() & 0xFF);//Remove signedness
				short zeropage = Memory.getInstance().cmGet(zpAddr);
				updateCYFlag(zeropage);
				Memory.getInstance().cmPut(zpAddr, (short)(zeropage << 1));
				break;
			case 0x16:
				//Zeroapge,x
				zpAddr = (short) (PrgRom.get() & 0xFF);//Remove signedness
				short zeropageX = Memory.getInstance().cmGet(zpAddr + registers.X);
				updateCYFlag(zeropageX);
				Memory.getInstance().cmPut(zpAddr + registers.X, (short)(zeropageX << 1));
				break;
			case 0x0E:
				//Absolute
				int absAddr = (PrgRom.getShort() & 0xFFFF);//Remove signedness
				short absolute = Memory.getInstance().cmGet(absAddr);
				updateCYFlag(absolute);
				Memory.getInstance().cmPut(absAddr, (short)(absolute << 1));
				break;
			case 0x1E:
				//Absolute,x
				absAddr = (PrgRom.getShort() & 0xFFFF);//Remove signedness
				short absoluteX = Memory.getInstance().cmGet(absAddr +  registers.X);
				updateCYFlag(absoluteX);
				Memory.getInstance().cmPut(absAddr, (short)(absoluteX << 1));
				break;
			default:
				//TODO Error
				break;
			}
		}
		
		void updateCYFlag(short data) {
			if((data & 0x80) != 0) {// bit 7 is set
				registers.SR |= FLAGS_CY_MASK; //set carry flag
			} else {
				registers.SR &= ~FLAGS_CY_MASK; //clear carry flag
			}
		}
	}
	
	//BCC	....	branch on carry clear
	private static  final class BCC implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//Relative
			byte rlAddr = PrgRom.get();//Signed value
			if((registers.SR & FLAGS_CY_MASK) == 0) {
				PrgRom.position(PrgRom.position() + rlAddr);
			}
		}
		
	}
	
	//BCS	....	branch on carry set
	private static  final class BCS implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//Relative
			byte rlAddr = PrgRom.get();//Signed value
			if((registers.SR & FLAGS_CY_MASK) != 0) {
				PrgRom.position(PrgRom.position() + rlAddr);
			}
		}
		
	}
	
	//BEQ	....	branch on equal (zero set)
	private static  final class BEQ implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			byte rlAddr = PrgRom.get();//Signed value
			if((registers.SR & FLAGS_ZE_MASK) != 0) {
				PrgRom.position(PrgRom.position() + rlAddr);
			}
		}
		
	}
	
	//BIT	....	bit test
	private static  final class BIT implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			switch(command) {
			case 0x24:
				//Zeropage
				int zpAddr = (PrgRom.get() & 0xFF);//Remove signedness
				short zeropage = Memory.getInstance().cmGet(zpAddr);
        setZVNflags(zeropage);
				break;
      case 0x2C:
				//Absolute
				int absAddr = (PrgRom.getShort() & 0xFFFF);//Remove signedness
				short absolute = Memory.getInstance().cmGet(absAddr);
        setZVNflags(absolute); 
				break;
			}
		}
		
		private void setZVNflags(short data) {
       if((registers.AC & data)==0) {
         registers.SR |= FLAGS_ZE_MASK; //set zero flag
       } else {
         registers.SR &= ~FLAGS_ZE_MASK; //clear zero flag
       }
       
       if(data & 0x40 != 0) { //Bit 6 set
          registers.SR |= FLAGS_OV_MASK; //set overflow flag
       } else {
          registers.SR &= ~FLAGS_OV_MASK; //clear overflow flag
       }
       if(data & 0x80 != 0) { //Bit 7 set
          registers.SR |= FLAGS_NE_MASK; //set negative flag
       } else {
          registers.SR &= ~FLAGS_NE_MASK; //clear negative flag
       }
		}
		
	}
	
	//BMI	....	branch on minus (negative set)
	private static  final class BMI implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			byte rlAddr = PrgRom.get();//Signed value
			if((registers.SR & FLAGS_NE_MASK) != 0) {
				PrgRom.position(PrgRom.position() + rlAddr);
			}
		}
		
	}
	
	//BNE	....	branch on not equal (zero clear)
	private static  final class BNE implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			byte rlAddr = PrgRom.get();//Signed value
			if((registers.SR & FLAGS_ZE_MASK) == 0) {
				PrgRom.position(PrgRom.position() + rlAddr);
			}
		}
		
	}
	
	//BPL	....	branch on plus (negative clear)
	private static  final class BPL implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			byte rlAddr = PrgRom.get();//Signed value
			if((registers.SR & FLAGS_NE_MASK) == 0) {
				PrgRom.position(PrgRom.position() + rlAddr);
			}
		}
		
	}
	
	//BRK	....	interrupt
	private static  final class BRK implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//TODO implement stack.
		}
		
	}
	
	//BVC	....	branch on overflow clear
	private static  final class BVC implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			byte rlAddr = PrgRom.get();//Signed value
			if((registers.SR & FLAGS_OV_MASK) == 0) {
				PrgRom.position(PrgRom.position() + rlAddr);
			}
		}
		
	}
	
	//BVS	....	branch on overflow set
	private static  final class BVS implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			byte rlAddr = PrgRom.get();//Signed value
			if((registers.SR & FLAGS_OV_MASK) != 0) {
				PrgRom.position(PrgRom.position() + rlAddr);
			}
		}
		
	}
	
	//CLC	....	clear carry
	private static  final class CLC implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			registers.SR &= ~FLAGS_CY_MASK; //clear carry flag
		}
		
	}
	
	//CLD	....	clear decimal
	private static  final class CLD implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			registers.SR &= ~FLAGS_DE_MASK; //clear carry flag
		}
		
	}
	
	//CLI	....	clear interrupt disable
	private static  final class CLI implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			registers.SR &= ~FLAGS_IN_MASK; //clear carry flag
		}
		
	}
	
	//CLV	....	clear overflow
	private static  final class CLV implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			registers.SR &= ~FLAGS_OV_MASK; //clear carry flag
		}
		
	}
	
	//CMP	....	compare (with accumulator)
	private static  final class CMP implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//CPX	....	compare with X
	private static  final class CPX implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//CPY	....	compare with Y
	private static  final class CPY implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//DEC	....	decrement
	private static  final class DEC implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//DEX	....	decrement X
	private static  final class DEX implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//DEY	....	decrement Y
	private static  final class DEY implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//EOR	....	exclusive or (with accumulator)
	private static  final class EOR implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//INC	....	increment
	private static  final class INC implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//INX	....	increment X
	private static  final class INX implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//INY	....	increment Y
	private static  final class INY implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//JMP	....	jump
	private static  final class JMP implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//JSR	....	jump subroutine
	private static  final class JSR implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//LDA	....	load accumulator
	private static  final class LDA implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//LDX	....	load X
	private static  final class LDX implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//LDY	....	load Y
	private static  final class LDY implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//LSR	....	logical shift right
	private static  final class LSR implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//NOP	....	no operation
	private static  final class NOP implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//ORA	....	or with accumulator
	private static  final class ORA implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//PHA	....	push accumulator
	private static  final class PHA implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//PHP	....	push processor status (SR)
	private static  final class PHP implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//PLA	....	pull accumulator
	private static  final class PLA implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//PLP	....	pull processor status (SR)
	private static  final class PLP implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//ROL	....	rotate left
	private static  final class ROL implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//ROR	....	rotate right
	private static  final class ROR implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//RTI	....	return from interrupt
	private static  final class RTI implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//RTS	....	return from subroutine
	private static  final class RTS implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//SBC	....	subtract with carry
	private static  final class SBC implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//SEC	....	set carry
	private static  final class SEC implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//SED	....	set decimal
	private static  final class SED implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//SEI	....	set interrupt disable
	private static  final class SEI implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//STA	....	store accumulator
	private static  final class STA implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//STX	....	store X
	private static  final class STX implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//STY	....	store Y
	private static  final class STY implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//TAX	....	transfer accumulator to X
	private static  final class TAX implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//TAY	....	transfer accumulator to Y
	private static  final class TAY implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//TSX	....	transfer stack pointer to X
	private static  final class TSX implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//TXA	....	transfer X to accumulator
	private static  final class TXA implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//TXS	....	transfer X to stack pointer
	private static  final class TXS implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
	
	//TYA	....	transfer Y to accumulator
	private static  final class TYA implements FunctionInterface {

		@Override
		public void execute(byte command, ByteBuffer PrgRom) {
			//wait 
		}
		
	}
}
