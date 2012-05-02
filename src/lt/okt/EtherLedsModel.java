package lt.okt;

public class EtherLedsModel extends Model {
	private String hostAddress = "169.254.121.1";
	private int port = 23;
	
	
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}
	public String getHostAddress() {
		return hostAddress;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getPort() {
		return port;
	}
	

}
