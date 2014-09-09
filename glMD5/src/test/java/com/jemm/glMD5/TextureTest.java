package com.jemm.glMD5;
 
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
 
import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;
 
public class TextureTest {
	public TextureTest() {
		JFrame frame = new JFrame("2D Graphics");
		frame.setSize(300,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GLCanvas canvas = new GLCanvas();
		canvas.addGLEventListener(new JOGLListener());
		frame.add(canvas);
		frame.setVisible(true);
	}
	public static void main(String args[]){
		new TextureTest();
	}
 
	private class JOGLListener implements GLEventListener {
 
		@Override
		public void display(GLAutoDrawable drawable) {
			System.out.println("DISPLAY CALLED");
			GL2 gl = drawable.getGL().getGL2();
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrtho(0, 300, 300, 0, 0, 1);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glDisable(GL2.GL_DEPTH_TEST);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
			gl.glBlendFunc (GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA); 
			gl.glEnable (GL2.GL_BLEND);
			BufferedImage bufferedImage = null;
			int w = 0;
			int h = 0;
			try {
				bufferedImage = ImageIO.read(TextureTest.class.getResource("/leaf.jpg"));
				w = bufferedImage.getWidth();
				h = bufferedImage.getHeight();
			} catch (IOException e) {
				e.printStackTrace();
			}
			WritableRaster raster = 
				Raster.createInterleavedRaster (DataBuffer.TYPE_BYTE,
						w,
						h,
						4,
						null);
			ComponentColorModel colorModel=
				new ComponentColorModel (ColorSpace.getInstance(ColorSpace.CS_sRGB),
						new int[] {8,8,8,8},
						true,
						false,
						ComponentColorModel.TRANSLUCENT,
						DataBuffer.TYPE_BYTE);
			BufferedImage dukeImg = 
				new BufferedImage (colorModel,
						raster,
						false,
						null);
 
			Graphics2D g = dukeImg.createGraphics();
			g.drawImage(bufferedImage, null, null);
			DataBufferByte dukeBuf =
				(DataBufferByte)raster.getDataBuffer();
			byte[] dukeRGBA = dukeBuf.getData();
			ByteBuffer bb = ByteBuffer.wrap(dukeRGBA);
			bb.position(0);
			bb.mark();
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 13);
			gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
			gl.glTexImage2D (GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, w, h, 0, GL2.GL_RGBA, 
					GL2.GL_UNSIGNED_BYTE, bb);
 
			int left = 100;
			int top = 100;
			gl.glEnable(GL2.GL_TEXTURE_2D);
			gl.glBindTexture (GL2.GL_TEXTURE_2D, 13);
			gl.glBegin (GL2.GL_POLYGON);
			gl.glTexCoord2d (0, 0);
			gl.glVertex2d (left,top);
			gl.glTexCoord2d(1,0);
			gl.glVertex2d (left + w, top);
			gl.glTexCoord2d(1,1);
			gl.glVertex2d (left + w, top + h);
			gl.glTexCoord2d(0,1);
			gl.glVertex2d (left, top + h);
			gl.glEnd ();	
			gl.glFlush();
		}
 
		@Override
		public void init(GLAutoDrawable drawable) {
			System.out.println("INIT CALLED");
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
	}
}