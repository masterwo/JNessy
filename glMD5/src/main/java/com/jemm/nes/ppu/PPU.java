package com.jemm.nes.ppu;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;

import com.jemm.debug.ResourceWindow;
import com.jogamp.opengl.util.FPSAnimator;

public class PPU implements GLEventListener {
	ByteBuffer memory = PpuMem.getInstance().PPU_ROM;
	PpuRegisters registers = PpuRegisters.getInstance();

	public PPU() {
		JFrame frame = new JFrame("2D Graphics");
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GLCanvas canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		frame.add(canvas);
		frame.setVisible(true);
		
        // Create a animator that drives canvas' display() at the specified FPS.
        final FPSAnimator animator = new FPSAnimator(canvas, 60, true);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               // Use a dedicate thread to run the stop() to ensure that the
               // animator stops before program exits.
               new Thread() {
                  @Override
                  public void run() {
                     if (animator.isStarted()) animator.stop();
                     System.exit(0);
                  }
               }.start();
            }
         });
        animator.start(); // start the animation loop
	}

	public static void main(String args[]) {
		new PPU();
	}

	private int[] calculatePixelRow(byte hi, byte lo) {
		int[] returnValues = new int[8];

		for (int i = 7; i >= 0; i--) {
			int value = (((hi >> i) << 1) & 0x02) + (((lo >> i)) & 0x01);
			if (value == 0x00) {
				returnValues[7 - i] = 0x00;
			} else if (value == 0x01) {
				returnValues[7 - i] = 0xFF;
			} else if (value == 0x02) {
				returnValues[7 - i] = 0xFF00;
			} else {
				returnValues[7 - i] = 0xFF0000;
			}
		}
		return returnValues;
	}

	IntBuffer texture = IntBuffer.allocate(1);
	BufferedImage chrRight = new BufferedImage(128, 128,
			BufferedImage.TYPE_INT_RGB);
	BufferedImage chrLeft = new BufferedImage(128, 128,
			BufferedImage.TYPE_INT_RGB);
	BufferedImage background = new BufferedImage(256, 240,
			BufferedImage.TYPE_INT_RGB);

	private void createTexture() {

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				for (int k = 0; k < 8; k++) {
					int[] pixelRow = calculatePixelRow(
							PpuMem.getInstance().PPU_ROM.get(j * 16 + i * 256
									+ k),
							PpuMem.getInstance().PPU_ROM.get(j * 16 + i * 256
									+ 8 + k));
					chrLeft.setRGB(j * 8 + 0, k + i * 8, pixelRow[0]);
					chrLeft.setRGB(j * 8 + 1, k + i * 8, pixelRow[1]);
					chrLeft.setRGB(j * 8 + 2, k + i * 8, pixelRow[2]);
					chrLeft.setRGB(j * 8 + 3, k + i * 8, pixelRow[3]);
					chrLeft.setRGB(j * 8 + 4, k + i * 8, pixelRow[4]);
					chrLeft.setRGB(j * 8 + 5, k + i * 8, pixelRow[5]);
					chrLeft.setRGB(j * 8 + 6, k + i * 8, pixelRow[6]);
					chrLeft.setRGB(j * 8 + 7, k + i * 8, pixelRow[7]);
				}
			}
		}
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				for (int k = 0; k < 8; k++) {
					int[] pixelRow = calculatePixelRow(
							PpuMem.getInstance().PPU_ROM.get(0x1000+(j * 16 + i * 256
									+ k)),
							PpuMem.getInstance().PPU_ROM.get(0x1000+(j * 16 + i * 256
									+ 8 + k)));
					chrRight.setRGB(j * 8 + 0, k + i * 8, pixelRow[0]);
					chrRight.setRGB(j * 8 + 1, k + i * 8, pixelRow[1]);
					chrRight.setRGB(j * 8 + 2, k + i * 8, pixelRow[2]);
					chrRight.setRGB(j * 8 + 3, k + i * 8, pixelRow[3]);
					chrRight.setRGB(j * 8 + 4, k + i * 8, pixelRow[4]);
					chrRight.setRGB(j * 8 + 5, k + i * 8, pixelRow[5]);
					chrRight.setRGB(j * 8 + 6, k + i * 8, pixelRow[6]);
					chrRight.setRGB(j * 8 + 7, k + i * 8, pixelRow[7]);
				}
			}
		}
	}
	
	int nameTableEntry;

	@Override
	public void display(GLAutoDrawable drawable) {
		//System.out.println("DISPLAY CALLED");
		GL2 gl = drawable.getGL().getGL2();
		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_BLEND);
			
		BufferedImage tileMap;
		if((registers.PPUMASK&0x08)!=0){//Background enabled.
			for(int i = 0; i < 30; i++) {
				for(int j = 0; j < 32; j++) {
					nameTableEntry = (memory.get(0x2000 + (i+registers.pPUScrollY)*32 + j + registers.pPUScrollX) & 0xFF);//Unsign
					
					int yCoord = nameTableEntry/16;
					int xCoord = nameTableEntry%16;
					
					if((PpuRegisters.getInstance().PPUCTRL & 0x10) == 0) {
						tileMap = chrLeft;
					} else {
						tileMap = chrRight;
					}
					
					for(int dy = 0; dy < 8; dy++) {
						for(int dx = 0; dx < 8; dx++) {
							try {
								background.setRGB(j*8+dx, i*8+dy, tileMap.getRGB(xCoord*8+dx, yCoord*8+dy));
							} catch(Exception ex) {
								System.out.println("xCoord*8+dx: " + xCoord*8+dx+PpuRegisters.getInstance().pPUScrollX);
								System.out.println("yCoord*8+dy: " + yCoord*8+dy+PpuRegisters.getInstance().pPUScrollY);
							}
						}
					}
				}
			}
		} else {
			
			//256, 240,
		}
		if((registers.PPUMASK&0x10)!=0){ //Sprites enabled.
			for(int i = 0; i < 64; i++) {
				short yPos = (short) (PpuMem.getInstance().OAM_DATA.get(i*4) & 0xFF);
				short tileSelect = (short) (PpuMem.getInstance().OAM_DATA.get(i*4+1) & 0xFF);
				short sprtCtrl = (short) (PpuMem.getInstance().OAM_DATA.get(i*4+2) & 0xFF);
				//if((sprtCtrl&0x20) == 0) { //Drawn infront of background.
					short xPos = (short) (PpuMem.getInstance().OAM_DATA.get(i*4+3) & 0xFF);
					if((tileSelect&0x01)==0) {
						tileMap = chrLeft;
					} else {
						tileMap = chrRight;
					}
					int xCoord = (tileSelect&0xFE)%16;
					int yCoord = (tileSelect&0xFE)/16;
					
					for(int dy = 0; dy < 8; dy++) {
						for(int dx = 0; dx < 8; dx++) {
							try {
								background.setRGB(xPos+dx, yPos+1+dy, tileMap.getRGB(xCoord*8+dx, yCoord*8+dy));
							} catch(Exception ex) {
								//System.out.println("xCoord*8+dx: " + xCoord*8+dx);
								//System.out.println("yCoord*8+dy: " + yCoord*8+dy);
							}
						}
					}
				//}
			}
		}

		BufferedImage bufferedImage = background;
		int w = 0;
		int h = 0;
		w = bufferedImage.getWidth();
		h = bufferedImage.getHeight();

		WritableRaster raster = Raster.createInterleavedRaster(
				DataBuffer.TYPE_BYTE, w, h, 4, null);
		ComponentColorModel colorModel = new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8,
						8, 8 }, true, false, ComponentColorModel.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);
		BufferedImage dukeImg = new BufferedImage(colorModel, raster, false,
				null);

		Graphics2D g = dukeImg.createGraphics();
		g.drawImage(bufferedImage, null, null);
		DataBufferByte dukeBuf = (DataBufferByte) raster.getDataBuffer();
		byte[] dukeRGBA = dukeBuf.getData();
		ByteBuffer bb = ByteBuffer.wrap(dukeRGBA);
		bb.position(0);
		bb.mark();
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.get(0));
		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_REPLACE);
		gl.glTexSubImage2D(GL2.GL_TEXTURE_2D, 0 ,0, 0, w, h, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, bb);
		
		gl.glBegin(GL2.GL_POLYGON);
		gl.glTexCoord2d(0, 0);
		gl.glVertex2d(0, 0);
		gl.glTexCoord2d(1, 0);
		gl.glVertex2d(1, 0);
		gl.glTexCoord2d(1, 1);
		gl.glVertex2d(1, 1);
		gl.glTexCoord2d(0, 1);
		gl.glVertex2d(0, 1);
		gl.glEnd();
		gl.glFlush();
	}
	
	ResourceWindow reWindowChrRight;
	ResourceWindow reWindowChrLeft;

	@Override
	public void init(GLAutoDrawable drawable) {
		System.out.println("INIT CALLED");
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, 1, 1, 0, 0, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		createTexture();
		
		reWindowChrRight = new ResourceWindow(chrRight);
		reWindowChrLeft = new ResourceWindow(chrLeft);
		
		for(int i = 0; i < 30; i++) {
			for(int j = 0; j < 32; j++) {
				nameTableEntry = (memory.get(0x2000 + i*32 + j) & 0xFF);//Unsign
				
				int yCoord = nameTableEntry/16;
				int xCoord = nameTableEntry%16;
				
				for(int dy = 0; dy < 8; dy++) {
					for(int dx = 0; dx < 8; dx++) {
						try {
							background.setRGB(j*8+dx, i*8+dy, chrLeft.getRGB(xCoord*8+dx, yCoord*8+dy));
						} catch(Exception ex) {
							
							System.out.println("xCoord*8+dx: " + xCoord*8+dx);
							System.out.println("yCoord*8+dy: " + yCoord*8+dy);
						}
					}
				}
			}
		}

		BufferedImage bufferedImage = background;
		int w = 0;
		int h = 0;
		w = bufferedImage.getWidth();
		h = bufferedImage.getHeight();

		WritableRaster raster = Raster.createInterleavedRaster(
				DataBuffer.TYPE_BYTE, w, h, 4, null);
		ComponentColorModel colorModel = new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8,
						8, 8 }, true, false, ComponentColorModel.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);
		BufferedImage dukeImg = new BufferedImage(colorModel, raster, false,
				null);

		Graphics2D g = dukeImg.createGraphics();
		g.drawImage(bufferedImage, null, null);
		DataBufferByte dukeBuf = (DataBufferByte) raster.getDataBuffer();
		byte[] dukeRGBA = dukeBuf.getData();
		ByteBuffer bb = ByteBuffer.wrap(dukeRGBA);
		bb.position(0);
		bb.mark();
		
		gl.glGenTextures(1, texture);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.get(0));
		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_REPLACE);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, w, h, 0,
				GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, bb);
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		System.out.println("RESHAPE CALLED");

	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}
	
	int latchCounter = 0;

	public void execute() {
		/*
		for(int i = 0; i < 128; i++) {
			for(int j = 0; j < 128; j++) {
				background.setRGB(i, j, image.getRGB(i, j));
			}
		}
	
		for(int i = 0; i < 30; i++) {
			for(int j = 0; j < 32; j++) {
				nameTableEntry = memory.get(0x2000 + i*32 + j);
				
				int yCoord = nameTableEntry/16;
				int xCoord = nameTableEntry%16;
				
				for(int dy = 0; dy < 8; dy++) {
					for(int dx = 0; dx < 8; dx++) {
						background.setRGB(j*8+dx, i*8+dy, image.getRGB(xCoord*8+dx, yCoord*8+dy));
					}
				}
			}
		}
		
		if(latchCounter == 8) {
			latchCounter = 0;
			nameTableEntry = memory.get(0x2000);
		}
		latchCounter++;
		*/
	}
}