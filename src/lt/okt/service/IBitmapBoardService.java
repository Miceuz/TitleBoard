package lt.okt.service;

import lt.okt.view.SenderThreadListener;

public interface IBitmapBoardService {

	public abstract void postData(byte[] data);

	public abstract void setSenderThreadListener(
			SenderThreadListener senderThreadListener);

	public abstract SenderThreadListener getSenderThreadListener();

}