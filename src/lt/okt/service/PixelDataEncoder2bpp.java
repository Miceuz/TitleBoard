package lt.okt.service;

import lt.okt.ListItem;

public class PixelDataEncoder2bpp {
	private int pixelsToBytes(int[][] linepixels, byte[] data, int lineIndex, int startIndex, int color) {
		int outByteIndex = startIndex;

		int colorBits = 0x80;
		
		if(ListItem.COLOR_GREEN == color) {
			colorBits = 0x80;
		} else if(ListItem.COLOR_RED == color) {
			colorBits = 0x40;
		} else if(ListItem.COLOR_YELLOW == color) {
			colorBits = 0xC0;
		} 

		for(byte c = 31; c >= 0; c--) {
			byte b = 0x00000000;
			for(byte bit = 0; bit < 4; bit++) {
				int pixel = linepixels[c*8+bit +4][lineIndex];
				if(pixel != -16777216) {
//					System.out.print("O");
					b = (byte) (b | (colorBits >> bit*2));
				} else {
//					System.out.print(".");
				}
			}
			data[outByteIndex ++] = (byte) ~b;
			b = 0x00000000;
			for(byte bit = 0; bit < 4; bit++) {
				int pixel = linepixels[c*8+bit][lineIndex];
				if(pixel != -16777216) {
//					System.out.print("O");
					b = (byte) (b | (colorBits >> bit*2));
				} else {
//					System.out.print(".");
				}
			}
			data[outByteIndex ++] = (byte) ~b;
		}
//		System.out.println("");
		return outByteIndex;
	}
	
	public byte[] pixelsToBytes(int[][] line1pixels, int color) {
		int outByteIndex = 0;
		byte[] data = new byte[64*16];
		
		for(byte l = 15; l >= 0; l--) {
			outByteIndex = pixelsToBytes(line1pixels, data, l, outByteIndex, color);
		}
//		for(int i = 0; i < 64*16; i++) {
//			System.out.print(data[i] + " ");
//			if(0 == i % 64) {
//				System.out.println("");
//			}
//		}
//		for(int i = 0; i < 64*16; i++) {
//			int mask = 0x80;
//			
//			while(mask != 0) {
//				if((data[i] & mask) != 0) {
//					System.out.print("O");
//				} else {
//					System.out.print(".");
//				}
//				mask = (mask >> 1);
//			}
//			if(0 == i % 64) {
//				System.out.println("");
//			}
//		}
		
		return data;
	}
}
