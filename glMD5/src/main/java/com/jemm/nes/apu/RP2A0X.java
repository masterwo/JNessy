package com.jemm.nes.apu;

public class RP2A0X {
	Square1 sq1 = new Square1();
	Square2 sq2 = new Square2();
	Triangle tri = new Triangle();
	
	public RP2A0X() {
		sq1.initiate();
		sq2.initiate();
		tri.initiate();
	}
	
	public void update() {
		sq1.update();
		sq2.update();
		tri.update();
	}
}
