package lt.okt.service;

import java.awt.Component;
import java.awt.image.BufferedImage;

public class PixelDataDecoder {
	public int[][] decode(Component c){
		int[][] ret = null;
		
		BufferedImage bi = getSnapshot(c);
		bi.flush();
		ret = new int[c.getWidth()][c.getHeight()];
		for(int x = 0; x < c.getWidth(); x++ ) {
			for(int y = 0; y < c.getHeight(); y++) {
				ret[x][y] = bi.getRGB(x, y);
//				if(-16777216 == ret[x][y]) {
//					System.out.print(".");
//				} else {
//					System.out.print("O");
//				}
			}
//			System.out.println("");
		}
		
			
		return ret;
	}

	public static BufferedImage getSnapshot(Component c) {
		BufferedImage bi = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
		c.paint(bi.getGraphics());
		return bi;
	}
}
