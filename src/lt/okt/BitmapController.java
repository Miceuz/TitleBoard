package lt.okt;

import lt.okt.service.PixelDataDecoder;
import lt.okt.view.SenderThreadListener;

public abstract class BitmapController extends Controller  implements SenderThreadListener {

	protected PixelDataDecoder pixelDataDecoder;

	public BitmapController() {
		setPixelDataDecoder(new PixelDataDecoder());
	}
	
	@Override
	protected void sendTextToOutputDevice(String text) {
		view.disableListComponent("Sending text to board...");
		sendTextToBoard();
	}

	protected abstract void sendTextToBoard();

	public void setPixelDataDecoder(PixelDataDecoder pixelDataDecoder) {
		this.pixelDataDecoder = pixelDataDecoder;
	}

	public PixelDataDecoder getPixelDataDecoder() {
		return pixelDataDecoder;
	}

	public void onSendFinish() {
		System.out.println("Enabling list on send finish");
		view.enableListComponent();
	}

	public void unBlind() {
		super.unBlind();
		sendTextToBoard();
	}

}
