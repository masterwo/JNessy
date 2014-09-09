package com.jemm.nes.peripherals;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class JoyPad implements KeyEventDispatcher {
	boolean left = false;
	boolean right = false;
	boolean up = false;
	boolean down = false;
	boolean A = false;
	boolean B = false;
	boolean SELECT = false;
	boolean START = false;
	
	public JoyPad() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
	}
	
//	A, B, Select, Start, Up, Down, Left, Right

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				left = true;
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = true;
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				up = true;
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				down = true;
			} else if(e.getKeyCode() == KeyEvent.VK_D) {
				A = true;
			} else if(e.getKeyCode() == KeyEvent.VK_F) {
				B = true;
			} else if(e.getKeyCode() == KeyEvent.VK_S) {
				SELECT = true;
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				START = true;
			}
		} else if(e.getID() == KeyEvent.KEY_RELEASED) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				left = false;
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = false;
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				up = false;
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				down = false;
			} else if(e.getKeyCode() == KeyEvent.VK_D) {
				A = false;
			} else if(e.getKeyCode() == KeyEvent.VK_F) {
				B = false;
			} else if(e.getKeyCode() == KeyEvent.VK_S) {
				SELECT = false;
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				START = false;
			}
		}
		return false;
	}
	
	int sequencer = -1;
	boolean strobed = false;
	boolean lsb_hi = false;
	
	public byte get() {
//		System.out.println("Get key:");
//		System.out.println("Strobed: " + strobed);
//		System.out.println("Sequencer: " + sequencer);
		sequencer++;
		//TODO BUS bits?
		if(strobed) {
			switch(sequencer) {
			case 0:
				if(A) {
					return 0x01;
				} else {
					return 0x00;
				}
			case 1:
				if(B) {
					return 0x01;
				} else {
					return 0x00;
				}
			case 2:
				if(SELECT) {
					return 0x01;
				} else {
					return 0x00;
				}
			case 3:
				if(START) {
					return 0x01;
				} else {
					return 0x00;
				}
			case 4:
				if(up) {
					return 0x01;
				} else {
					return 0x00;
				}
			case 5:
				if(down) {
					return 0x01;
				} else {
					return 0x00;
				}
			case 6:
				if(left) {
					return 0x01;
				} else {
					return 0x00;
				}
			case 7:
				if(right) {
					return 0x01;
				} else {
					return 0x00;
				}
			default:
				return 0x01;
			}
		} else {
			return 0x01;
		}
	}
	
	public void set(short data) {
		if(!strobed) {
			if(lsb_hi) {
				if((data&0x01) == 0) {
					strobed = true;
					sequencer = -1;
				}
			} else {
				if((data&0x01) != 0) {
					lsb_hi = true;
				}
			}
		} else {
			if((data&0x01) != 0) {
				lsb_hi = true;
				strobed = false;
			}
		}
	}
	
}
