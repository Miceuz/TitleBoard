package lt.okt.service;

public class PixelDataEncoder {
	private int pixelsToBytes(int[][] linepixels, byte[] data, int lineIndex, int startIndex) {
		int outByteIndex = startIndex;
		
		for(byte c = 47; c >= 0; c--) {
			int b = 0;
			for(byte bit = 0; bit < 8; bit++) {
				int pixel = linepixels[c*8+bit][lineIndex];
				if(pixel != -16777216) {
					b = b | (0x80 >> bit);
				}
			}
			data[outByteIndex ++] = (byte) b;
		}
		return outByteIndex;
	}
	
	public byte[] pixelsToBytes(int[][] line1pixels, int[][] line2pixels) {
		int outByteIndex = 1;
		byte[] data = new byte[48*2*16 + 1];
		
		for(byte l = 15; l >= 0; l--) {
			outByteIndex = pixelsToBytes(line2pixels, data, l, outByteIndex);
			outByteIndex = pixelsToBytes(line1pixels, data, l, outByteIndex);
		}
		return data;
	}
}
