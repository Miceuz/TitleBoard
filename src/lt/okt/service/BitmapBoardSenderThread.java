package lt.okt.service;

class BitmapBoardSenderThread extends Thread {
	private IBitmapBoardService service;
	private byte[] data;

	public BitmapBoardSenderThread(IBitmapBoardService service, byte[] data) {
		this.service = service;
		this.data = data;
	}
	
	@Override
	public void run() {
		this.service.postData(this.data);
	}
}