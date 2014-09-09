package com.jemm.nes.cpu;

//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.Charset;
import java.util.HashMap;

public class Instructions6502 {
	static final byte FLAGS_CY_SHIFT = 0;
	static final byte FLAGS_ZE_SHIFT = 1;
	static final byte FLAGS_IN_SHIFT = 2;
	static final byte FLAGS_DE_SHIFT = 3;
	static final byte FLAGS_BR_SHIFT = 4;
	static final byte FLAGS_A1_SHIFT = 5;
	static final byte FLAGS_OV_SHIFT = 6;
	static final byte FLAGS_NE_SHIFT = 7;
	
	static final byte FLAGS_CY_MASK = (1 << FLAGS_CY_SHIFT);
	static final byte FLAGS_ZE_MASK = (1 << FLAGS_ZE_SHIFT);
	static final byte FLAGS_IN_MASK = (1 << FLAGS_IN_SHIFT);
	static final byte FLAGS_DE_MASK = (1 << FLAGS_DE_SHIFT);
	static final byte FLAGS_BR_MASK = (1 << FLAGS_BR_SHIFT);
	static final byte FLAGS_A1_MASK = (1 << FLAGS_A1_SHIFT);
	static final byte FLAGS_OV_MASK = (1 << FLAGS_OV_SHIFT);
	static final byte FLAGS_NE_MASK = (byte) (1 << FLAGS_NE_SHIFT);
	
	private static HashMap<Byte, FunctionInterface> instructionMap = new HashMap<Byte, FunctionInterface>();
	
	private static CpuRegisters registers = CpuRegisters.getInstance();
	
	private static Memory memory = Memory.getInstance();
	
	private int instructionCounter = 0;
//	//DEBUG
//	private InputStream    fis;
//	private BufferedReader br;
//	///DEBUG
	
	public void execute(byte command) {
//		//DEBUG
//		String line = "";
//		try {
//			line = br.readLine();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println(line);
//		
//		String address = line.substring(0,4);
//		
//		String cmp = String.format("%04X", registers.getPC()-1);
//		
//		String tokens[] = new String[5];
//		int ctrTokens = 0;
//		
//		for (String p : line.substring(line.indexOf("A:")).split("\\s")) {
//			String[] keyValue = p.split(":");
//			if(keyValue[0].equals("CYC"))
//				break;
//			tokens[ctrTokens++] = keyValue[1];
//		}
//		
//		if(!cmp.equals(address) || !String.format("%02X", registers.AC & 0xFF).equals(tokens[0]) || !String.format("%02X", registers.X & 0xFF).equals(tokens[1]) || !String.format("%02X", registers.Y & 0xFF).equals(tokens[2]) || !String.format("%02X", registers.SR).equals(tokens[3]) || !String.format("%02X", registers.SP & 0xFF).equals(tokens[4])) {
//			System.out.println(cmp + " " + address);
//		}
//		///DEBUG
		
//		System.out.println(String.format("%02X", registers.AC));
//		System.out.println(++instructionCounter + ":" + String.format("%04X", registers.getPC()-1) + ": $" + String.format("%02X", command));
//		if( memory.cmGet(0xD4) != 0) {
//			System.out.println("Key pressed:" + memory.cmGet(0xD4));
//		}
		try {//0xC293
			instructionMap.get(command).execute(command);
		} catch(NullPointerException nex) {
			//Non valid opcode log and move on.
			System.out.println("Invalid opcode: " + String.format("%02X", command));
		}
 		
	}
	
	public Instructions6502() {
//		//DEBUG
//		try {
//			fis = new FileInputStream(NesMain.class.getResource("/nestest.log").getFile());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//		}
//		br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
//		/*
//		try {
//			br.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		*/
//		registers.SP = 0xFD;
//		
//		///DEBUG
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
		
		instructionMap.put((byte) 0x0A, asl);
		instructionMap.put((byte) 0x06, asl);
		instructionMap.put((byte) 0x16, asl);
		instructionMap.put((byte) 0x0E, asl);
		instructionMap.put((byte) 0x1E, asl);
		
		instructionMap.put((byte) 0x90, bcc);
		
		instructionMap.put((byte) 0xB0, bcs);
		
		instructionMap.put((byte) 0xF0, beq);
		
		instructionMap.put((byte) 0x24, bit);
		instructionMap.put((byte) 0x2C, bit);
		
		instructionMap.put((byte) 0x30, bmi);
		
		instructionMap.put((byte) 0xD0, bne);
		
		instructionMap.put((byte) 0x10, bpl);
		
		instructionMap.put((byte) 0x00, brk);
		
		instructionMap.put((byte) 0x50, bvc);
		
		instructionMap.put((byte) 0x70, bvs);
		
		instructionMap.put((byte) 0x18, clc);
		
		instructionMap.put((byte) 0xD8, cld);
		
		instructionMap.put((byte) 0x58, cli);
		
		instructionMap.put((byte) 0xB8, clv);
		
		instructionMap.put((byte) 0xC9, cmp);
		instructionMap.put((byte) 0xC5, cmp);
		instructionMap.put((byte) 0xD5, cmp);
		instructionMap.put((byte) 0xCD, cmp);
		instructionMap.put((byte) 0xDD, cmp);
		instructionMap.put((byte) 0xD9, cmp);
		instructionMap.put((byte) 0xC1, cmp);
		instructionMap.put((byte) 0xD1, cmp);
		
		instructionMap.put((byte) 0xE0, cpx);
		instructionMap.put((byte) 0xE4, cpx);
		instructionMap.put((byte) 0xEC, cpx);
		
		instructionMap.put((byte) 0xC0, cpy);
		instructionMap.put((byte) 0xC4, cpy);
		instructionMap.put((byte) 0xCC, cpy);
		
		instructionMap.put((byte) 0xC6, dec);
		instructionMap.put((byte) 0xD6, dec);
		instructionMap.put((byte) 0xCE, dec);
		instructionMap.put((byte) 0xDE, dec);
		
		instructionMap.put((byte) 0xCA, dex);
		
		instructionMap.put((byte) 0x88, dey);
		
		instructionMap.put((byte) 0x49, eor);
		instructionMap.put((byte) 0x45, eor);
		instructionMap.put((byte) 0x55, eor);
		instructionMap.put((byte) 0x4D, eor);
		instructionMap.put((byte) 0x5D, eor);
		instructionMap.put((byte) 0x59, eor);
		instructionMap.put((byte) 0x41, eor);
		instructionMap.put((byte) 0x51, eor);
		
		instructionMap.put((byte) 0xE6, inc);
		instructionMap.put((byte) 0xF6, inc);
		instructionMap.put((byte) 0xEE, inc);
		instructionMap.put((byte) 0xFE, inc);
		
		instructionMap.put((byte) 0xE8, inx);
		
		instructionMap.put((byte) 0xC8, iny);
		
		instructionMap.put((byte) 0x4C, jmp);
		instructionMap.put((byte) 0x6C, jmp);
		
		instructionMap.put((byte) 0x20, jsr);
		
		instructionMap.put((byte) 0xA9, lda);
		instructionMap.put((byte) 0xA5, lda);
		instructionMap.put((byte) 0xB5, lda);
		instructionMap.put((byte) 0xAD, lda);
		instructionMap.put((byte) 0xBD, lda);
		instructionMap.put((byte) 0xB9, lda);
		instructionMap.put((byte) 0xA1, lda);
		instructionMap.put((byte) 0xB1, lda);
		
		instructionMap.put((byte) 0xA2, ldx);
		instructionMap.put((byte) 0xA6, ldx);
		instructionMap.put((byte) 0xB6, ldx);
		instructionMap.put((byte) 0xAE, ldx);
		instructionMap.put((byte) 0xBE, ldx);
		
		instructionMap.put((byte) 0xA0, ldy);
		instructionMap.put((byte) 0xA4, ldy);
		instructionMap.put((byte) 0xB4, ldy);
		instructionMap.put((byte) 0xAC, ldy);
		instructionMap.put((byte) 0xBC, ldy);
		
		instructionMap.put((byte) 0x4A, lsr);
		instructionMap.put((byte) 0x46, lsr);
		instructionMap.put((byte) 0x56, lsr);
		instructionMap.put((byte) 0x4E, lsr);
		instructionMap.put((byte) 0x5E, lsr);
		
		instructionMap.put((byte) 0xEA, nop);
		
		instructionMap.put((byte) 0x09, ora);
		instructionMap.put((byte) 0x05, ora);
		instructionMap.put((byte) 0x15, ora);
		instructionMap.put((byte) 0x0D, ora);
		instructionMap.put((byte) 0x1D, ora);
		instructionMap.put((byte) 0x19, ora);
		instructionMap.put((byte) 0x01, ora);
		instructionMap.put((byte) 0x11, ora);
		
		instructionMap.put((byte) 0x48, pha);
		
		instructionMap.put((byte) 0x08, php);
		
		instructionMap.put((byte) 0x68, pla);
		
		instructionMap.put((byte) 0x28, plp);
		
		instructionMap.put((byte) 0x2A, rol);
		instructionMap.put((byte) 0x26, rol);
		instructionMap.put((byte) 0x36, rol);
		instructionMap.put((byte) 0x2E, rol);
		instructionMap.put((byte) 0x3E, rol);
		
		instructionMap.put((byte) 0x6A, ror);
		instructionMap.put((byte) 0x66, ror);
		instructionMap.put((byte) 0x76, ror);
		instructionMap.put((byte) 0x6E, ror);
		instructionMap.put((byte) 0x7E, ror);
		
		instructionMap.put((byte) 0x40, rti);
		
		instructionMap.put((byte) 0x60, rts);
		
		instructionMap.put((byte) 0xE9, sbc);
		instructionMap.put((byte) 0xE5, sbc);
		instructionMap.put((byte) 0xF5, sbc);
		instructionMap.put((byte) 0xED, sbc);
		instructionMap.put((byte) 0xFD, sbc);
		instructionMap.put((byte) 0xF9, sbc);
		instructionMap.put((byte) 0xE1, sbc);
		instructionMap.put((byte) 0xF1, sbc);
		
		instructionMap.put((byte) 0x38, sec);
		
		instructionMap.put((byte) 0xF8, sed);
		
		instructionMap.put((byte) 0x78, sei);
		
		instructionMap.put((byte) 0x85, sta);
		instructionMap.put((byte) 0x95, sta);
		instructionMap.put((byte) 0x8D, sta);
		instructionMap.put((byte) 0x9D, sta);
		instructionMap.put((byte) 0x99, sta);
		instructionMap.put((byte) 0x81, sta);
		instructionMap.put((byte) 0x91, sta);
		
		instructionMap.put((byte) 0x86, stx);
		instructionMap.put((byte) 0x96, stx);
		instructionMap.put((byte) 0x8E, stx);
		
		instructionMap.put((byte) 0x84, sty);
		instructionMap.put((byte) 0x94, sty);
		instructionMap.put((byte) 0x8C, sty);
		
		instructionMap.put((byte) 0xAA, tax);
		
		instructionMap.put((byte) 0xA8, tay);
		
		instructionMap.put((byte) 0xBA, tsx);
		
		instructionMap.put((byte) 0x8A, txa);
		
		instructionMap.put((byte) 0x9A, txs);
		
		instructionMap.put((byte) 0x98, tya);
		
		//UNDOCUMENTED OPCODES
		
		instructionMap.put((byte) 0x04, dop);
		instructionMap.put((byte) 0x14, dop);
		instructionMap.put((byte) 0x34, dop);
		instructionMap.put((byte) 0x44, dop);
		instructionMap.put((byte) 0x54, dop);
		instructionMap.put((byte) 0x64, dop);
		instructionMap.put((byte) 0x74, dop);
		instructionMap.put((byte) 0x80, dop);
		instructionMap.put((byte) 0x82, dop);
		instructionMap.put((byte) 0x89, dop);
		instructionMap.put((byte) 0xC2, dop);
		instructionMap.put((byte) 0xD4, dop);
		instructionMap.put((byte) 0xE2, dop);
		instructionMap.put((byte) 0xF4, dop);
		
		instructionMap.put((byte) 0x0C, top);
		instructionMap.put((byte) 0x1C, top);
		instructionMap.put((byte) 0x3C, top);
		instructionMap.put((byte) 0x5C, top);
		instructionMap.put((byte) 0x7C, top);
		instructionMap.put((byte) 0xDC, top);
		instructionMap.put((byte) 0xFC, top);
		
		instructionMap.put((byte) 0x1A, nop);
		instructionMap.put((byte) 0x3A, nop);
		instructionMap.put((byte) 0x5A, nop);
		instructionMap.put((byte) 0x7A, nop);
		instructionMap.put((byte) 0xDA, nop);
		instructionMap.put((byte) 0xFA, nop);
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
	private static final LDY ldy = new LDY();// ....	load Y
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
	
	private static final DOP dop = new DOP();//	....	No operation (double NOP). The argument has no significance.
	private static final TOP top = new TOP();//	....	No operation (tripple NOP). The argument has no significance.
	
	//ADC	....	add with carry
	private static final class ADC implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
				case 0x69:
					//Immediate
					short immediate = memory.cmGet();//Remove signedness
					Adc(immediate);
					break;
				case 0x65:
					//Zeropage
					short zpAddr = memory.cmGet();//Remove signedness
					short zeropage = memory.cmGet(zpAddr);
					Adc((short) (zeropage & 0xFF));
					break;
				case 0x75:
					//Zeropage,x
					zpAddr = memory.cmGet();//Remove signedness
					short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
					Adc((short) (zeropageX & 0xFF));
					break;
				case 0x6D:
					//absolute
					int absAddr = memory.cmGetShort();
					short absolute = memory.cmGet(absAddr);
					Adc((short) (absolute & 0xFF));
					break;
				case 0x7D:
					//absolute,x
					absAddr = memory.cmGetShort();//Remove signedness
					short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
					Adc((short) (absoluteX & 0xFF));
					break;
				case 0x79:
					//absolute,y
					absAddr = memory.cmGetShort();//Remove signedness
					short absoluteY = memory.cmGet(absAddr +  (registers.Y & 0xFF));
					Adc((short) (absoluteY & 0xFF));
					break;
				case 0x61:
					//(Indirect,x)
					short idAddr = memory.cmGet();//Remove signedness
					int dAddr = 0;
					if(((idAddr + (registers.X & 0xFF)) & 0xFF) == 0xFF) {//Page boundary bug.
						dAddr = memory.cmGet((idAddr + (registers.X & 0xFF)));//Get low byte.
						dAddr += memory.cmGet((idAddr + (registers.X & 0xFF)) & 0xFF00) << 8;//Get high byte
					} else {
						dAddr = memory.cmGetShort((idAddr + (registers.X & 0xFF)) & 0xFF);
					}
					short indirectX = memory.cmGet(dAddr);
					Adc((short) (indirectX & 0xFF));
					break;
				case 0x71:
					//(Indirect),y
					idAddr = memory.cmGet();//Remove signedness
					dAddr = memory.cmGetShort(idAddr);
					short indirectY = memory.cmGet(dAddr + (registers.Y & 0xFF));
					Adc((short) (indirectY & 0xFF));
					break;
			}
			
			if(registers.AC == 0x00) {
				registers.SR |=  FLAGS_ZE_MASK; // Set Zero Flag
			} else {
				registers.SR &=  ~FLAGS_ZE_MASK; // Set Zero Flag
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
			if ((registers.SR & FLAGS_CY_MASK) != 0x00) {
				carryOut = ((a >= 0xFF - b) ? 1 : 0);
				registers.AC = (byte) (a + b + 1);
			} else {
				carryOut = ((a > 0xFF - b) ? 1 : 0);
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
		public void execute(byte command) {
			switch(command) {
				case 0x29:
					//Immediate
					short immediate = memory.cmGet();//Remove signedness
					And(immediate);
					break;
				case 0x25:
					//Zeropage
					short zpAddr = memory.cmGet();//Remove signedness
					short zeropage = memory.cmGet(zpAddr);
					And(zeropage);
					break;
				case 0x35:
					//Zeroapge,x
					zpAddr = memory.cmGet();//Remove signedness
					short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
					And(zeropageX);
					break;
				case 0x2D:
					//Absolute
					int absAddr = memory.cmGetShort();//Remove signedness
					short absolute = memory.cmGet(absAddr);
					And(absolute);
					break;
				case 0x3D:
					//Absolute,x
					absAddr = memory.cmGetShort();//Remove signedness
					short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
					And(absoluteX);
					break;
				case 0x39:
					//Absolute,y
					absAddr = memory.cmGetShort();//Remove signedness
					short absoluteY = memory.cmGet(absAddr +  (registers.Y & 0xFF));
					And(absoluteY);
					break;
				case 0x21:
					//(Indirect,x)
					short idAddr = memory.cmGet();//Remove signedness
					int dAddr = 0;
					if(((idAddr + (registers.X & 0xFF)) & 0xFF) == 0xFF) {//Page boundary bug.
						dAddr = memory.cmGet((idAddr + (registers.X & 0xFF)));//Get low byte.
						dAddr += memory.cmGet((idAddr + (registers.X & 0xFF)) & 0xFF00) << 8;//Get high byte
					} else {
						dAddr = memory.cmGetShort((idAddr + (registers.X & 0xFF)) & 0xFF);
					}
					short indirectX = memory.cmGet(dAddr);
					And(indirectX);
					break;
				case 0x31:
					//(Indirect,y)
					idAddr = memory.cmGet();//Remove signedness
					dAddr = memory.cmGetShort(idAddr);
					short indirectY = memory.cmGet(dAddr + (registers.Y & 0xFF));
					And(indirectY);
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
		public void execute(byte command) {
			int result = 0;
			
			switch(command) {
			case 0x0A:
				//Accumulator
				updateCYFlag(registers.AC);
				registers.AC = (short) ((registers.AC << 1) & 0xFF);
				result = registers.AC;
				break;
			case 0x06:
				//Zeropage
				int zpAddr = memory.cmGet();
				short zeropage = memory.cmGet(zpAddr);
				updateCYFlag((short) (zeropage & 0xFF));
				memory.cmPut(zpAddr, (short)((zeropage << 1) & 0xFF));
				result = (short)((zeropage << 1) & 0xFF);
				break;
			case 0x16:
				//Zeroapge,x
				zpAddr = memory.cmGet();
				short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
				updateCYFlag(zeropageX);
				memory.cmPut(zpAddr + (registers.X & 0xFF), (short)((zeropageX << 1) & 0xFF));
				result = (short)((zeropageX << 1) & 0xFF);
				break;
			case 0x0E:
				//Absolute
				int absAddr = memory.cmGetShort();
				short absolute = memory.cmGet(absAddr);
				updateCYFlag(absolute);
				memory.cmPut(absAddr, (short)((absolute << 1) & 0xFF));
				result = (short)((absolute << 1) & 0xFF);
				break;
			case 0x1E:
				//Absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				updateCYFlag(absoluteX);
				memory.cmPut(absAddr +  (registers.X & 0xFF), (short)((absoluteX << 1) & 0xFF));
				result = (short)((absoluteX << 1) & 0xFF);
				break;
			}
			if(result == 0x00) {
				registers.SR |=  FLAGS_ZE_MASK; // Set Zero Flag
			} else {
				registers.SR &=  ~(FLAGS_ZE_MASK); // clear zero Flag
			}
			
			if((result & 0x80) != 0) { //Bit 7 Set
				registers.SR |=  FLAGS_NE_MASK; // Set Negative Flag
			} else {
				registers.SR &=  ~(FLAGS_NE_MASK); // clear Negative Flag
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
		public void execute(byte command) {
			//Relative
			byte rlAddr = (byte) memory.cmGet();//Signed value
			if((registers.SR & FLAGS_CY_MASK) == 0) {
				registers.setPC(registers.getPC() + rlAddr);
			}
		}
		
	}
	
	//BCS	....	branch on carry set
	private static  final class BCS implements FunctionInterface {

		@Override
		public void execute(byte command) {
			//Relative
			byte rlAddr = (byte) memory.cmGet();//Signed value
			if((registers.SR & FLAGS_CY_MASK) != 0) {
				registers.setPC(registers.getPC() + rlAddr);
			}
		}
		
	}
	
	//BEQ	....	branch on equal (zero set)
	private static  final class BEQ implements FunctionInterface {

		@Override
		public void execute(byte command) {
			byte rlAddr = (byte) memory.cmGet();//Signed value
			if((registers.SR & FLAGS_ZE_MASK) != 0) {
				registers.setPC(registers.getPC() + rlAddr);
			}
		}
		
	}
	
	//BIT	....	bit test
	private static  final class BIT implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case 0x24:
				//Zeropage
				int zpAddr = memory.cmGet();
				short zeropage = memory.cmGet(zpAddr);
        setZVNflags(zeropage);
				break;
      case 0x2C:
				//Absolute
				int absAddr = memory.cmGetShort();
				short absolute = memory.cmGet(absAddr);
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
       
       if((data & 0x40) != 0) { //Bit 6 set
          registers.SR |= FLAGS_OV_MASK; //set overflow flag
       } else {
          registers.SR &= ~FLAGS_OV_MASK; //clear overflow flag
       }
       if((data & 0x80) != 0) { //Bit 7 set
          registers.SR |= FLAGS_NE_MASK; //set negative flag
       } else {
          registers.SR &= ~FLAGS_NE_MASK; //clear negative flag
       }
		}
		
	}
	
	//BMI	....	branch on minus (negative set)
	private static  final class BMI implements FunctionInterface {

		@Override
		public void execute(byte command) {
			byte rlAddr = (byte) memory.cmGet();//Signed value
			if((registers.SR & FLAGS_NE_MASK) != 0) {
				registers.setPC(registers.getPC() + rlAddr);
			}
		}
		
	}
	
	//BNE	....	branch on not equal (zero clear)
	private static  final class BNE implements FunctionInterface {

		@Override
		public void execute(byte command) {
			byte rlAddr = (byte) memory.cmGet();//Signed value
			if((registers.SR & FLAGS_ZE_MASK) == 0) {
				registers.setPC(registers.getPC() + rlAddr);
			}
		}
		
	}
	
	//BPL	....	branch on plus (negative clear)
	private static  final class BPL implements FunctionInterface {

		@Override
		public void execute(byte command) {
			byte rlAddr = (byte) memory.cmGet();//Signed value
			if((registers.SR & FLAGS_NE_MASK) == 0) {
				registers.setPC(registers.getPC() + rlAddr);
			}
		}
		
	}
	
	//BRK	....	interrupt
	private static  final class BRK implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR |= FLAGS_BR_MASK; //set break flag
			registers.SP-=1;
			memory.cmPutShort(0x0100 + (registers.SP & 0xFF), registers.getPC());
			registers.SP-=1;
			memory.cmPut(0x0100 + (registers.SP & 0xFF),(short) (registers.SR | 0x10));//0x08 sets B FLAG this is only set when doing PHP or BRK and only on the stack pushed value.
			registers.SP-=1;
			int interuptAddr = memory.cmGetShort(0xFFFE);
			registers.setPC(interuptAddr);
		}
		
	}
	
	//BVC	....	branch on overflow clear
	private static  final class BVC implements FunctionInterface {

		@Override
		public void execute(byte command) {
			byte rlAddr = (byte) memory.cmGet();//Signed value
			if((registers.SR & FLAGS_OV_MASK) == 0) {
				registers.setPC(registers.getPC() + rlAddr);
			}
		}
		
	}
	
	//BVS	....	branch on overflow set
	private static  final class BVS implements FunctionInterface {

		@Override
		public void execute(byte command) {
			byte rlAddr = (byte) memory.cmGet();//Signed value
			if((registers.SR & FLAGS_OV_MASK) != 0) {
				registers.setPC(registers.getPC() + rlAddr);
			}
		}
		
	}
	
	//CLC	....	clear carry
	private static  final class CLC implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR &= ~FLAGS_CY_MASK; //clear carry flag
		}
		
	}
	
	//CLD	....	clear decimal
	private static  final class CLD implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR &= ~FLAGS_DE_MASK; //clear carry flag
		}
		
	}
	
	//CLI	....	clear interrupt disable
	private static  final class CLI implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR &= ~FLAGS_IN_MASK; //clear carry flag
		}
		
	}
	
	//CLV	....	clear overflow
	private static  final class CLV implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR &= ~FLAGS_OV_MASK; //clear carry flag
		}
		
	}
	
	//CMP	....	compare (with accumulator)
	private static  final class CMP implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xC9:
				//Immediate
				short immediate = memory.cmGet();//Remove signedness
				Cmp((short) (immediate & 0xFF));
				break;
			case (byte) 0xC5:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				short zeropage = memory.cmGet(zpAddr);
				Cmp((short) (zeropage & 0xFF));
				break;
			case (byte) 0xD5:
				//Zeroapge,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
				Cmp((short) (zeropageX & 0xFF));
				break;
			case (byte) 0xCD:
				//Absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				Cmp((short) (absolute & 0xFF));
				break;
			case (byte) 0xDD:
				//Absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				Cmp((short) (absoluteX & 0xFF));
				break;
			case (byte) 0xD9:
				//Absolute,y
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteY = memory.cmGet(absAddr +  (registers.Y & 0xFF));
				Cmp((short) (absoluteY & 0xFF));
				break;
			case (byte) 0xC1:
				//(Indirect,x)
				short idAddr = memory.cmGet();//Remove signedness
				int dAddr = 0;
				if(((idAddr + (registers.X & 0xFF)) & 0xFF) == 0xFF) {//Page boundary bug.
					dAddr = memory.cmGet((idAddr + (registers.X & 0xFF)));//Get low byte.
					dAddr += memory.cmGet((idAddr + (registers.X & 0xFF)) & 0xFF00) << 8;//Get high byte
				} else {
					dAddr = memory.cmGetShort((idAddr + (registers.X & 0xFF)) & 0xFF);
				}
				short indirectX = memory.cmGet(dAddr);
				Cmp((short) (indirectX & 0xFF));
				break;
			case (byte) 0xD1:
				//(Indirect,y)
				idAddr = memory.cmGet();//Remove signedness
				dAddr = memory.cmGetShort(idAddr);
				short indirectY = memory.cmGet(dAddr + (registers.Y & 0xFF));
				Cmp((short) (indirectY & 0xFF));
				break;
			}
		}
		
		private void Cmp(short data) {
			byte result = (byte) (registers.AC - data);
			if((registers.AC & 0xFF) >= data) {
				registers.SR |= FLAGS_CY_MASK; //Set carry flag
			} else {
				registers.SR &= ~FLAGS_CY_MASK; //Clear carry flag
			}
			if(result == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
			}
			
			if((result & 0x80) > 0) {
				registers.SR |= FLAGS_NE_MASK; //Set Negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear Negative flag
			}
		}
		
	}
	
	//CPX	....	compare with X
	private static  final class CPX implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xE0:
				//Immediate
				short immediate = memory.cmGet();//Remove signedness
				Cpx((short) (immediate & 0xFF));
				break;
			case (byte) 0xE4:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				short zeropage = memory.cmGet(zpAddr);
				Cpx((short) (zeropage & 0xFF));
				break;
			case (byte) 0xEC:
				//Absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				Cpx((short) (absolute & 0xFF));
				break;
			}
		}
		
		private void Cpx(short data) {
			byte result = (byte) (registers.X - data);
			if((registers.X & 0xFF) >= data) {
				registers.SR |= FLAGS_CY_MASK; //Set carry flag
			} else {
				registers.SR &= ~FLAGS_CY_MASK; //Clear carry flag
			}
			if(result == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
			}
			
			if((result & 0x80) > 0) {
				registers.SR |= FLAGS_NE_MASK; //Set Negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear Negative flag
			}
		}
		
	}
	
	//CPY	....	compare with Y
	private static  final class CPY implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xC0:
				//Immediate
				short immediate = memory.cmGet();//Remove signedness
				Cpy((short) (immediate & 0xFF));
				break;
			case (byte) 0xC4:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				short zeropage = memory.cmGet(zpAddr);
				Cpy((short) (zeropage & 0xFF));
				break;
			case (byte) 0xCC:
				//Absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				Cpy((short) (absolute & 0xFF));
				break;
			}
		}
		
		private void Cpy(short data) {
			byte result = (byte) (registers.Y - data);
			if((registers.Y & 0xFF) >= data) {
				registers.SR |= FLAGS_CY_MASK; //Set carry flag
			} else {
				registers.SR &= ~FLAGS_CY_MASK; //Clear carry flag
			}
			if(result == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
			}
			
			if((result & 0x80) > 0) {
				registers.SR |= FLAGS_NE_MASK; //Set Negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear Negative flag
			}
		}
		
	}
	
	//DEC	....	decrement
	private static  final class DEC implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xC6:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				short zeropage = memory.cmGet(zpAddr);
				zeropage--;
				Dec(zpAddr, (short) (zeropage & 0xFF));
				break;
			case (byte) 0xD6:
				//Zeroapge,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
				zeropageX--;
				Dec(zpAddr + (registers.X & 0xFF), (short) (zeropageX & 0xFF));
				break;
			case (byte) 0xCE:
				//Absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				absolute--;
				Dec(absAddr, (short) (absolute & 0xFF));
				break;
			case (byte) 0xDE:
				//Absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				absoluteX--;
				Dec(absAddr +  (registers.X & 0xFF), (short) (absoluteX & 0xFF));
				break;
			}
		}
		
		private void Dec(int address, short data) {
			if(data == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
			}
			if((data & 0x80) != 0) { //Bit 7 is set.
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
			memory.cmPut(address, data);
		}
		
	}
	
	//DEX	....	decrement X
	private static  final class DEX implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xCA:
				registers.X = (byte) ((registers.X & 0xFF)-1);
				if(registers.X == 0) {
					registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
				} else {
					registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
				}
				if((registers.X & 0x80) != 0) { //Bit 7 is set.
					registers.SR |= FLAGS_NE_MASK; //Set negative flag
				} else {
					registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
				}
				break;
			}
		}	
	}
	
	//DEY	....	decrement Y
	private static  final class DEY implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0x88:
				registers.Y = (byte) ((registers.Y-1) & 0xFF);
				if(registers.Y == 0) {
					registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
				} else {
					registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
				}
				if((registers.Y & 0x80) != 0) { //Bit 7 is set.
					registers.SR |= FLAGS_NE_MASK; //Set negative flag
				} else {
					registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
				}
				break;
			}
		}	
	}
	
	//EOR	....	exclusive or (with accumulator)
	private static  final class EOR implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case 0x49:
				//Immediate
				short immediate = memory.cmGet();//Remove signedness
				Eor(immediate);
				break;
			case 0x45:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				short zeropage = memory.cmGet(zpAddr);
				Eor(zeropage);
				break;
			case 0x55:
				//Zeropage,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
				Eor(zeropageX);
				break;
			case 0x4D:
				//absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				Eor(absolute);
				break;
			case 0x5D:
				//absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				Eor(absoluteX);
				break;
			case 0x59:
				//absolute,y
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteY = memory.cmGet(absAddr +  (registers.Y & 0xFF));
				Eor(absoluteY);
				break;
			case 0x41:
				//(Indirect,x)
				short idAddr = memory.cmGet();//Remove signedness
				int dAddr = 0;
				if(((idAddr + (registers.X & 0xFF)) & 0xFF) == 0xFF) {//Page boundary bug.
					dAddr = memory.cmGet((idAddr + (registers.X & 0xFF)));//Get low byte.
					dAddr += memory.cmGet((idAddr + (registers.X & 0xFF)) & 0xFF00) << 8;//Get high byte
				} else {
					dAddr = memory.cmGetShort((idAddr + (registers.X & 0xFF)) & 0xFF);
				}
				short indirectX = memory.cmGet(dAddr);
				Eor(indirectX);
				break;
			case 0x51:
				//(Indirect),y
				idAddr = memory.cmGet();//Remove signedness
				dAddr = memory.cmGetShort(idAddr);
				short indirectY = memory.cmGet(dAddr + (registers.Y & 0xFF));
				Eor(indirectY);
				break;
			}
		}
		
		private void Eor(short data) {
			registers.AC ^= data;
			if(registers.AC == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
			}
			if((registers.AC & 0x80) != 0) { //Bit 7 is set.
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
		}
		
	}
	
	//INC	....	increment
	private static  final class INC implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xE6:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				short zeropage = memory.cmGet(zpAddr);
				zeropage++;
				Inc(zpAddr, (short) (zeropage & 0xFF));
				break;
			case (byte) 0xF6:
				//Zeroapge,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
				zeropageX++;
				Inc(zpAddr + (registers.X & 0xFF), (short) (zeropageX & 0xFF));
				break;
			case (byte) 0xEE:
				//Absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				absolute++;
				Inc(absAddr, (short) (absolute & 0xFF));
				break;
			case (byte) 0xFE:
				//Absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				absoluteX++;
				Inc(absAddr +  (registers.X & 0xFF), (short) (absoluteX & 0xFF));
				break;
			}
		}
		
		private void Inc(int address, short data) {
			if(data == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
			}
			if((data & 0x80) != 0) { //Bit 7 is set.
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
			memory.cmPut(address, data);
		}
	}
	
	//INX	....	increment X
	private static  final class INX implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.X = (byte) ((registers.X & 0xFF)+1);
			if((registers.X & 0xFF) == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
			}
			if((registers.X & 0x80) != 0) { //Bit 7 is set.
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
		}
		
	}
	
	//INY	....	increment Y
	private static  final class INY implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xC8:
				registers.Y = (byte) ((registers.Y + 1) & 0xFF);
				if(registers.Y == 0) {
					registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
				} else {
					registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
				}
				if((registers.Y & 0x80) != 0) { //Bit 7 is set.
					registers.SR |= FLAGS_NE_MASK; //Set negative flag
				} else {
					registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
				}
				break;
			}
		}	
	}
	
	//JMP	....	jump
	private static  final class JMP implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case 0x4C:
				//Absolute
				int absolute = memory.cmGetShort();
				registers.setPC(absolute);
				break;
			case 0x6C:
				//(Indirect)
				int idAddr = memory.cmGetShort();//Remove signedness
				int indirect = 0;
				if((idAddr & 0xFF) == 0xFF) {//Page boundary bug.
					/*
					indirect = memory.cmGet(idAddr);//Get low byte.
					indirect += memory.cmGet(idAddr & 0xFF00) << 8;//Get high byte
					*/
					indirect = memory.cmGet(idAddr);//Get low byte.
					indirect += memory.cmGet(idAddr & 0xFF00) << 8;//Get high byte
				} else {
					indirect = memory.cmGetShort(idAddr);
				}
				registers.setPC(indirect);
				break;
			}
		}
		
	}
	
	//JSR	....	jump subroutine
	private static  final class JSR implements FunctionInterface {

		@Override
		public void execute(byte command) {
			int address = memory.cmGetShort();
			registers.SP-=1;//make room for 2 bytes
			memory.cmPutShort(0x0100 + (registers.SP & 0xFF), registers.getPC()-1);
			registers.SP-=1;//This puts us just "behind" the stack on the next available "empty" space.
			registers.setPC(address);
		}
		
	}
	
	//LDA	....	load accumulator
	private static  final class LDA implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xA9:
				//Immediate
				short immediate = memory.cmGet();//Remove signedness
				registers.AC = (byte)immediate;
				break;
			case (byte) 0xA5:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				short zeropage = memory.cmGet(zpAddr);
				registers.AC = (byte)zeropage;
				break;
			case (byte) 0xB5:
				//Zeropage,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet((zpAddr + (registers.X & 0xFF)) & 0xFF);
				registers.AC = (byte)zeropageX;
				break;
			case (byte) 0xAD:
				//absolute
				int absAddr = memory.cmGetShort();//Remove signedness TODO flags.
				short absolute = memory.cmGet(absAddr);
				registers.AC = (byte)absolute;
				break;
			case (byte) 0xBD:
				//absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				registers.AC = (byte)absoluteX;
				break;
			case (byte) 0xB9:
				//absolute,y
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteY = memory.cmGet((absAddr +  (registers.Y & 0xFF)));
				registers.AC = (byte)absoluteY;
				break;
			case (byte) 0xA1:
				//(Indirect,x)
				short idAddr = memory.cmGet();//Remove signedness
				int dAddr = 0;
				if(((idAddr + (registers.X & 0xFF)) & 0xFF) == 0xFF) {//Page boundary bug.
					dAddr = memory.cmGet((idAddr + (registers.X & 0xFF)));//Get low byte.
					dAddr += memory.cmGet((idAddr + (registers.X & 0xFF)) & 0xFF00) << 8;//Get high byte
				} else {
					dAddr = memory.cmGetShort((idAddr + (registers.X & 0xFF)) & 0xFF);
				}
				short indirectX = memory.cmGet(dAddr);
				registers.AC = indirectX;
				//registers.AC = (byte)indirectX;
				break;
			case (byte) 0xB1:
				//(Indirect),y
				idAddr = memory.cmGet();//Remove signedness
				if(((idAddr) & 0xFF) == 0xFF) {//Page boundary bug.
					dAddr = memory.cmGet((idAddr));//Get low byte.
					dAddr += memory.cmGet((idAddr) & 0xFF00) << 8;//Get high byte
				} else {
					dAddr = memory.cmGetShort((idAddr) & 0xFF);
				}
				short indirectY = memory.cmGet((dAddr + (registers.Y & 0xFF)) & 0xFFFF);
				registers.AC = (byte)indirectY;
				break;
			}
			
			if(registers.AC == 0x00) {
				registers.SR |= FLAGS_ZE_MASK; //Set zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear zero flag
			}
			if((registers.AC & 0x80) != 0) {//Bit 7 is set
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
		}
		
	}
	
	//LDX	....	load X
	private static  final class LDX implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xA2:
				//Immediate
				short immediate = memory.cmGet();
				registers.X = immediate;
				setFlags();
				break;
			case (byte) 0xA6:
				//Zeropage
				short zpAddr = memory.cmGet();
				short zeropage = memory.cmGet(zpAddr);
				registers.X = zeropage;
				setFlags();
				break;
			case (byte) 0xB6:
				//Zeropage,y
				zpAddr = memory.cmGet();
				short zeropageY = memory.cmGet((zpAddr + (registers.Y & 0xFF)) & 0xFF);
				registers.X = zeropageY;
				setFlags();
				break;
			case (byte) 0xAE:
				//absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				registers.X = absolute;
				setFlags();
				break;
			case (byte) 0xBE:
				//absolute,y
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteY = memory.cmGet((absAddr +  (registers.Y & 0xFF)));
				registers.X = absoluteY;
				setFlags();
				break;
			}
		}
		private void setFlags() {
			if(registers.X == 0x00) {
				registers.SR |= FLAGS_ZE_MASK; //Set zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear zero flag
			}
			if((registers.X & 0x80) != 0) {//Bit 7 is set
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
		}
	}
	
	//LDY	....	load Y
	private static  final class LDY implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0xA0:
				//Immediate
				short immediate = memory.cmGet();//Remove signedness
				registers.Y = immediate;
				setFlags();
				break;
			case (byte) 0xA4:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				short zeropage = memory.cmGet(zpAddr);
				registers.Y = zeropage;
				setFlags();
				break;
			case (byte) 0xB4:
				//Zeropage,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet((zpAddr + (registers.X & 0xFF)) & 0xFF);
				registers.Y = zeropageX;
				setFlags();
				break;
			case (byte) 0xAC:
				//absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				registers.Y = absolute;
				setFlags();
				break;
			case (byte) 0xBC:
				//absolute,X
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				registers.Y = absoluteX;
				setFlags();
				break;
			}
		}
		private void setFlags() {
			if(registers.Y == 0x00) {
				registers.SR |= FLAGS_ZE_MASK; //Set zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear zero flag
			}
			if((registers.Y & 0x80) != 0) {//Bit 7 is set
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
		}
		
	}
	
	//LSR	....	logical shift right
	private static  final class LSR implements FunctionInterface {

		@Override
		public void execute(byte command) {
			
			switch(command) {
			case 0x4A:
				//Accumulator
				updateFlags(registers.AC);
				registers.AC = (short) ((registers.AC & 0xFF) >>> 1);
				break;
			case 0x46:
				//Zeropage
				int zpAddr = memory.cmGet();
				short zeropage = memory.cmGet(zpAddr);
				Lsr(zeropage, zpAddr);
				break;
			case 0x56:
				//Zeroapge,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
				Lsr(zeropageX, zpAddr + (registers.X & 0xFF));
				break;
			case 0x4E:
				//Absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				Lsr(absolute, absAddr);
				break;
			case 0x5E:
				//Absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				Lsr(absoluteX, absAddr +  (registers.X & 0xFF));
				break;
			}
		}
		
		private void Lsr(short data, int address) {
			updateFlags(data);
			memory.cmPut(address, (short)((data & 0xFF) >>> 1));
		}
		
		private void updateFlags(short data) {
			if((data & 0x01) != 0) {//Bit 0 i set
				registers.SR |= FLAGS_CY_MASK; //Set carry flag
			} else {
				registers.SR &= ~FLAGS_CY_MASK; //Clear carry flag
			}
			
			short result = (short)((data & 0xFF) >>> 1);
			if(result == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear zero flag
			}
			if((result & 0x80) != 0) {
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
		}
		
	}
	
	//NOP	....	no operation
	private static  final class NOP implements FunctionInterface {

		@Override
		public void execute(byte command) {
			//wait 
		}
		
	}
	
	//ORA	....	or with accumulator
	private static  final class ORA implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case 0x09:
				//Immediate
				short immediate = memory.cmGet();//Remove signedness
				Ora(immediate);
				break;
			case 0x05:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				short zeropage = memory.cmGet(zpAddr);
				Ora(zeropage);
				break;
			case 0x15:
				//Zeropage,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
				Ora(zeropageX);
				break;
			case 0x0D:
				//absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				Ora(absolute);
				break;
			case 0x1D:
				//absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				Ora(absoluteX);
				break;
			case 0x19:
				//absolute,y
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteY = memory.cmGet(absAddr +  (registers.Y & 0xFF));
				Ora(absoluteY);
				break;
			case 0x01:
				//(Indirect,x)
				short idAddr = memory.cmGet();//Remove signedness
				int dAddr = 0;
				if(((idAddr + (registers.X & 0xFF)) & 0xFF) == 0xFF) {//Page boundary bug.
					dAddr = memory.cmGet((idAddr + (registers.X & 0xFF)));//Get low byte.
					dAddr += memory.cmGet((idAddr + (registers.X & 0xFF)) & 0xFF00) << 8;//Get high byte
				} else {
					dAddr = memory.cmGetShort((idAddr + (registers.X & 0xFF)) & 0xFF);
				}
				short indirectX = memory.cmGet(dAddr);
				Ora(indirectX);
				break;
			case 0x11:
				//(Indirect),y
				idAddr = memory.cmGet();//Remove signedness
				dAddr = memory.cmGetShort(idAddr);
				short indirectY = memory.cmGet(dAddr + (registers.Y & 0xFF));
				Ora(indirectY);
				break;
			}
		}
		
		private void Ora(short data) {
			registers.AC |= data;
			if(registers.AC == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set Zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear Zero flag
			}
			if((registers.AC & 0x80) != 0) { //Bit 7 is set.
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
		}
		
	}
	
	//PHA	....	push accumulator
	private static  final class PHA implements FunctionInterface {

		@Override
		public void execute(byte command) {
			memory.cmPut(0x0100 + (registers.SP & 0xFF),registers.AC);
			registers.SP-=1;
		}
		
	}
	
	//PHP	....	push processor status (SR)
	private static  final class PHP implements FunctionInterface {

		@Override
		public void execute(byte command) {
			memory.cmPut(0x0100 + (registers.SP & 0xFF),(short) (registers.SR | 0x10));//0x08 sets B FLAG this is only set when doing PHP or BRK and only on the stack pushed value.
			registers.SP-=1;
		}
		
	}
	
	//PLA	....	pull accumulator
	private static  final class PLA implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.AC = memory.cmGet(0x0100 + ((registers.SP+1) & 0xFF));//+1 stack read fix.
			registers.SP++;
			if(registers.AC == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear zero flag
			}
			
			if((registers.AC & 0x80) != 0) {//Bit 7 set
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
			
		}
		
	}
	
	//PLP	....	pull processor status (SR)
	private static  final class PLP implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR = (byte) (memory.cmGet(0x0100 + ((registers.SP+1) & 0xFF)) & 0xEF);//+1 stack read fix. 0xEF Should never be present on "real" SR.
			registers.SR |= FLAGS_A1_MASK;//This bit should always be set.
			registers.SP++;
		}
		
	}
	
	//ROL	....	rotate left
	private static  final class ROL implements FunctionInterface {

		@Override
		public void execute(byte command) {
			
			switch(command) {
			case 0x2A:
				//Accumulator
				int result = registers.AC << 1;
				if((registers.SR & FLAGS_CY_MASK) != 0) {//Carry flag is set
					result |= 0x01;//Set bit 0 
				}
				updateFlags(registers.AC);
				
				registers.AC = (short) result;
				break;
			case 0x26:
				//Zeropage
				int zpAddr = memory.cmGet();
				short zeropage = memory.cmGet(zpAddr);
				Rol(zeropage, zpAddr);
				break;
			case 0x36:
				//Zeroapge,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
				Rol(zeropageX, zpAddr + (registers.X & 0xFF));
				break;
			case 0x2E:
				//Absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				Rol(absolute, absAddr);
				break;
			case 0x3E:
				//Absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				Rol(absoluteX, absAddr +  (registers.X & 0xFF));
				break;
			}
		}
		
		private void Rol(short data, int address) {
			int result = data << 1;
			if((registers.SR & FLAGS_CY_MASK) != 0) {//Carry flag is set
				result |= 0x01;//Set bit 0 
			}
			
			updateFlags(data);
			
			memory.cmPut(address, (short) result);
		}
		
		private void updateFlags(short data) {
			if((data & 0x80) != 0) { //Bit 
				registers.SR |= FLAGS_CY_MASK; //Set carry flag
			} else {
				registers.SR &= ~FLAGS_CY_MASK; //Clear carry flag
			}
			
			short result = (short)(data << 1);
			if(result == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear zero flag
			}
			if((result & 0x80) != 0) {
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
		}
		
	}
	
	//ROR	....	rotate right
	private static  final class ROR implements FunctionInterface {

		@Override
		public void execute(byte command) {
			
			switch(command) {
			case 0x6A:
				//Accumulator
				int result = (registers.AC & 0xFF) >>> 1;
				
				if((registers.SR & FLAGS_CY_MASK) != 0) {//Carry flag is set
					result |= 0x80;//Set bit 7
				} else {
					result &= ~0x80;//Clear bit 7 TODO is this needed?
				}
				
				updateFlags(registers.AC);
				
				registers.AC = (short) (result & 0xFF);
				break;
			case 0x66:
				//Zeropage
				int zpAddr = memory.cmGet();
				short zeropage = memory.cmGet(zpAddr);
				RoR(zeropage, zpAddr);
				break;
			case 0x76:
				//Zeroapge,x
				zpAddr = memory.cmGet();//Remove signedness
				short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
				RoR(zeropageX, zpAddr + (registers.X & 0xFF));
				break;
			case 0x6E:
				//Absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				short absolute = memory.cmGet(absAddr);
				RoR(absolute, absAddr);
				break;
			case 0x7E:
				//Absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
				RoR(absoluteX, absAddr +  (registers.X & 0xFF));
				break;
			}
		}
		
		private void RoR(short data, int address) {
			int result = data >>> 1;
			if((registers.SR & FLAGS_CY_MASK) != 0) {//Carry flag is set
				result |= 0x80;//Set bit 7 
			} else {
				result &= ~0x80;//Clear bit 7 TODO is this needed?
			}
			
			updateFlags(data);
			
			memory.cmPut(address, (short) result);
		}
		
		private void updateFlags(short data) {
			short result = (short)(data >>> 1);
			if((registers.SR & FLAGS_CY_MASK) != 0) {//Carry flag is set
				result |= 0x80;//Set bit 7
			} else {
				result &= ~0x80;//Clear bit 7
			}
			
			if((result & 0x80) != 0) {
				registers.SR |= FLAGS_NE_MASK; //Set negative flag
			} else {
				registers.SR &= ~FLAGS_NE_MASK; //Clear negative flag
			}
			if(result == 0) {
				registers.SR |= FLAGS_ZE_MASK; //Set zero flag
			} else {
				registers.SR &= ~FLAGS_ZE_MASK; //Clear zero flag
			}
			
			if((data & 0x01) != 0) { //Bit 0 is set 
				registers.SR |= FLAGS_CY_MASK; //Set carry flag
			} else {
				registers.SR &= ~FLAGS_CY_MASK; //Clear carry flag
			}
		}
		
	}
	
	//RTI	....	return from interrupt
	private static  final class RTI implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR = (byte) memory.cmGet(0x0100 + ((registers.SP+1) & 0xFF));//+1 stack read fix.
			registers.SP++;
			
			registers.SR |= FLAGS_A1_MASK;//A1 bit should always be set.
			
			int memset = memory.cmGetShort(0x0100 + ((registers.SP+1) & 0xFF));
			//System.out.println("SP: " + memset);
			registers.setPC(memset);//Added one because SP points to one place behind last added number, this is how it's supposed to be but must be replicated.
			registers.SP+=2;
		}
		
	}
	
	//RTS	....	return from subroutine
	private static  final class RTS implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.setPC(memory.cmGetShort(0x0100 + ((registers.SP+1) & 0xFF))+1);//Added one because SP points to one place behind last added number, this is how it's supposed to be but must be replicated.
			registers.SP+=2;
		}
		
	}
	
	//SBC	....	subtract with carry
	private static  final class SBC implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
				case (byte) 0xE9:
					//Immediate
					short immediate = memory.cmGet();//Remove signedness
					Sbc(immediate);
					break;
				case (byte) 0xE5:
					//Zeropage
					short zpAddr = memory.cmGet();//Remove signedness
					short zeropage = memory.cmGet(zpAddr);
					Sbc(zeropage);
					break;
				case (byte) 0xF5:
					//Zeropage,x
					zpAddr = memory.cmGet();//Remove signedness
					short zeropageX = memory.cmGet(zpAddr + (registers.X & 0xFF));
					Sbc(zeropageX);
					break;
				case (byte) 0xED:
					//absolute
					int absAddr = memory.cmGetShort();//Remove signedness
					short absolute = memory.cmGet(absAddr);
					Sbc(absolute);
					break;
				case (byte) 0xFD:
					//absolute,x
					absAddr = memory.cmGetShort();//Remove signedness
					short absoluteX = memory.cmGet(absAddr +  (registers.X & 0xFF));
					Sbc(absoluteX);
					break;
				case (byte) 0xF9:
					//absolute,y
					absAddr = memory.cmGetShort();//Remove signedness
					short absoluteY = memory.cmGet(absAddr +  (registers.Y & 0xFF));
					Sbc(absoluteY);
					break;
				case (byte) 0xE1:
					//(Indirect,x)
					short idAddr = memory.cmGet();//Remove signedness
					int dAddr = 0;
					if(((idAddr + (registers.X & 0xFF)) & 0xFF) == 0xFF) {//Page boundary bug.
						dAddr = memory.cmGet((idAddr + (registers.X & 0xFF)));//Get low byte.
						dAddr += memory.cmGet((idAddr + (registers.X & 0xFF)) & 0xFF00) << 8;//Get high byte
					} else {
						dAddr = memory.cmGetShort((idAddr + (registers.X & 0xFF)) & 0xFF);
					}
					short indirectX = memory.cmGet(dAddr);
					Sbc(indirectX);
					break;
				case (byte) 0xF1:
					//(Indirect),y
					idAddr = memory.cmGet();//Remove signedness
					dAddr = memory.cmGetShort(idAddr);
					short indirectY = memory.cmGet(dAddr + (registers.Y & 0xFF));
					Sbc(indirectY);
					break;
			}
			
			if(registers.AC == 0x00) {
				registers.SR |=  FLAGS_ZE_MASK; // Set Zero Flag
			} else {
				registers.SR &=  ~FLAGS_ZE_MASK; // Clear Zero Flag
			}
			
			if((registers.AC & 0x80) == 0x80) {
				registers.SR |=  FLAGS_NE_MASK; // Set Negative Flag
			} else {
				registers.SR &=  ~(FLAGS_NE_MASK); // clear Negative Flag
			}
			
		}

		private void Sbc(short data) {
			int result = (registers.AC + (byte)(~data + (registers.SR & FLAGS_CY_MASK)));
			registers.AC = (byte) result;
			
			if(result != (byte)result) {
				registers.SR |=  FLAGS_OV_MASK; // Set overflow Flag
			} else {
				registers.SR &=  ~(FLAGS_OV_MASK); // clear overflow Flag
			}
			
			if(((registers.AC & 0xFF + (~data & 0xFF)) & 0x100) != 0) {
				registers.SR &=  ~(FLAGS_CY_MASK); // clear carry Flag
			} else {
				registers.SR |=  FLAGS_CY_MASK; // Set carry Flag
			}
			
			/*
			int result = registers.AC - data;
			
			if(result != (byte)result) {
				registers.SR |=  FLAGS_OV_MASK; // Set overflow Flag
			} else {
				registers.SR &=  ~(FLAGS_OV_MASK); // clear overflow Flag
			}
			
			 registers.AC = (short) result;
			 */
		}
		
	}
	
	//SEC	....	set carry
	private static  final class SEC implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR |= FLAGS_CY_MASK;
		}
		
	}
	
	//SED	....	set decimal
	private static  final class SED implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR |= FLAGS_DE_MASK;
		}
		
	}
	
	//SEI	....	set interrupt disable
	private static  final class SEI implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SR |= FLAGS_IN_MASK;
		}
		
	}
	
	//STA	....	store accumulator
	private static  final class STA implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0x85:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				memory.cmPut(zpAddr, registers.AC);
				break;
			case (byte) 0x95:
				//Zeropage,x
				zpAddr = memory.cmGet();//Remove signedness
				memory.cmPut(zpAddr + (registers.X & 0xFF), registers.AC);
				break;
			case (byte) 0x8D:
				//absolute
				int absAddr = memory.cmGetShort();//Remove signedness
				memory.cmPut(absAddr, registers.AC);
				break;
			case (byte) 0x9D:
				//absolute,x
				absAddr = memory.cmGetShort();//Remove signedness
				memory.cmPut(absAddr +  (registers.X & 0xFF), registers.AC);
				break;
			case (byte) 0x99:
				//absolute,y
				absAddr = memory.cmGetShort();//Remove signedness
				memory.cmPut(absAddr +  (registers.Y & 0xFF), registers.AC);
				break;
			case (byte) 0x81:
				//(Indirect,x)
				short idAddr = memory.cmGet();//Remove signedness
				int dAddr = 0;
				if(((idAddr + (registers.X & 0xFF)) & 0xFF) == 0xFF) {//Page boundary bug.
					dAddr = memory.cmGet((idAddr + (registers.X & 0xFF)));//Get low byte.
					dAddr += memory.cmGet((idAddr + (registers.X & 0xFF)) & 0xFF00) << 8;//Get high byte
				} else {
					dAddr = memory.cmGetShort((idAddr + (registers.X & 0xFF)) & 0xFF);
				}
				memory.cmPut(dAddr, registers.AC);
				break;
			case (byte) 0x91:
				//(Indirect),y
				idAddr = memory.cmGet();//Remove signedness
				dAddr = (short) memory.cmGetShort(idAddr);
				memory.cmPut(dAddr + (registers.Y & 0xFF), registers.AC);
				break;
			}
		}
		
	}
	
	//STX	....	store X
	private static  final class STX implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0x86:
				//Zeropage
				short zpAddr = memory.cmGet();//Remove signedness
				memory.cmPut(zpAddr, registers.X);
				break;
			case (byte) 0x96:
				//Zeropage,y
				zpAddr = memory.cmGet();//Remove signedness
				memory.cmPut((zpAddr + (registers.Y & 0xFF)) & 0xFF, registers.X);
				break;
			case (byte) 0x8E:
				//absolute
				int absAddr = memory.cmGetShort();
				memory.cmPut(absAddr, registers.X);
				break;
			}
		}
		
	}
	
	//STY	....	store Y
	private static  final class STY implements FunctionInterface {

		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0x84:
				//Zeropage
				short zpAddr = memory.cmGet();
				memory.cmPut(zpAddr, registers.Y);
				break;
			case (byte) 0x94:
				//Zeropage,x
				zpAddr = memory.cmGet();
				memory.cmPut((zpAddr + (registers.X & 0xFF) & 0xFF), registers.Y);
				break;
			case (byte) 0x8C:
				//absolute
				int absAddr = memory.cmGetShort();
				memory.cmPut(absAddr, registers.Y);
				break;
			}
		}
		
	}
	
	//TAX	....	transfer accumulator to X
	private static  final class TAX implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.X = registers.AC;
			if(registers.X == 0) {
				registers.SR |= FLAGS_ZE_MASK;
			} else {
				registers.SR &= ~FLAGS_ZE_MASK;
			}
			if((registers.X & 0x80) != 0) {//Bit 7 set
				registers.SR |= FLAGS_NE_MASK;
			} else {
				registers.SR &= ~FLAGS_NE_MASK;
			}
		}
		
	}
	
	//TAY	....	transfer accumulator to Y
	private static  final class TAY implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.Y = registers.AC;
			if(registers.Y == 0) {
				registers.SR |= FLAGS_ZE_MASK;
			} else {
				registers.SR &= ~FLAGS_ZE_MASK;
			}
			if((registers.Y & 0x80) != 0) {//Bit 7 set
				registers.SR |= FLAGS_NE_MASK;
			} else {
				registers.SR &= ~FLAGS_NE_MASK;
			}
		}
		
	}
	
	//TSX	....	transfer stack pointer to X
	private static  final class TSX implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.X = registers.SP;
			if(registers.X == 0) {
				registers.SR |= FLAGS_ZE_MASK;
			} else {
				registers.SR &= ~FLAGS_ZE_MASK;
			}
			if((registers.X & 0x80) != 0) {//Bit 7 set
				registers.SR |= FLAGS_NE_MASK;
			} else {
				registers.SR &= ~FLAGS_NE_MASK;
			}
		}
		
	}
	
	//TXA	....	transfer X to accumulator
	private static  final class TXA implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.AC = registers.X;
			if(registers.AC == 0) {
				registers.SR |= FLAGS_ZE_MASK;
			} else {
				registers.SR &= ~FLAGS_ZE_MASK;
			}
			if((registers.AC & 0x80) != 0) {//Bit 7 set
				registers.SR |= FLAGS_NE_MASK;
			} else {
				registers.SR &= ~FLAGS_NE_MASK;
			}
		}
		
	}
	
	//TXS	....	transfer X to stack pointer
	private static  final class TXS implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.SP = registers.X;
		}
		
	}
	
	//TYA	....	transfer Y to accumulator
	private static  final class TYA implements FunctionInterface {

		@Override
		public void execute(byte command) {
			registers.AC = registers.Y;
			if(registers.AC == 0) {
				registers.SR |= FLAGS_ZE_MASK;
			} else {
				registers.SR &= ~FLAGS_ZE_MASK;
			}
			if((registers.AC & 0x80) != 0) {//Bit 7 set
				registers.SR |= FLAGS_NE_MASK;
			} else {
				registers.SR &= ~FLAGS_NE_MASK;
			}
		}
		
	}
	
	//UNDOCUMENTED OP CODES.
	
	//DOP	....	No operation (double NOP). The argument has no significance.
	private static final class DOP implements FunctionInterface {
		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0x04:
				//Zeropage
			case (byte) 0x14:
				//Zeropage,x
			case (byte) 0x34:
				//Zeropage,x
			case (byte) 0x44:
				//Zeropage
			case (byte) 0x54:
				//Zeropage,x
			case (byte) 0x64:
				//Zeropage
			case (byte) 0x74:
				//Zeropage,x
			case (byte) 0x80:
				//Immediate
			case (byte) 0x82:
				//Immediate
			case (byte) 0x89:
				//Immediate
			case (byte) 0xC2:
				//Immediate
			case (byte) 0xD4:
				//Zeropage,x
			case (byte) 0xE2:
				//Immediate
			case (byte) 0xF4:
				//Zeropage,x
				memory.cmGet();
			}
		}
		
	}
	
	//TOP	....	No operation (tripple NOP). The argument has no significance.
	private static final class TOP implements FunctionInterface {
		@Override
		public void execute(byte command) {
			switch(command) {
			case (byte) 0x0C:
				//Absolute
			case (byte) 0x1C:
				//Absolute,x
			case (byte) 0x3C:
				//Absolute,x
			case (byte) 0x5C:
				//Absolute,x
			case (byte) 0x7C:
				//Absolute,x
			case (byte) 0xDC:
				//Absolute,x
			case (byte) 0xFC:
				//Absolute,x
				memory.cmGetShort();
			}
		}
		
	}
	
}
