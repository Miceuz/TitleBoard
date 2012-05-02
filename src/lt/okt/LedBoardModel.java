package lt.okt;


public class LedBoardModel extends Model {
	public static final int LED_PROTOCOL_BITMAP =0;
	public static final int LED_PROTOCOL_ASCII =1;
	
	private int ledProtocol = LED_PROTOCOL_BITMAP;
	private String ledPort = "COM1";
	public void setLedProtocol(int ledProtocol) {
		this.ledProtocol = ledProtocol;
	}

	public int getLedProtocol() {
		return ledProtocol;
	}

	public void setLedPort(String ledPort) {
		this.ledPort = ledPort;
	}

	public String getLedPort() {
		return ledPort;
	}
}
