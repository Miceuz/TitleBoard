package lt.okt.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import lt.okt.EtherLedsModel;
import lt.okt.view.SenderThreadListener;

public class EtherLedsService implements IBitmapBoardService {

	private PixelDataEncoder pixelDataEncoder = new PixelDataEncoder();
	private EtherLedsModel model;
//	private InputStream is;
	private OutputStream os;
	
	private SenderThreadListener senderThreadListener;

	public EtherLedsService(EtherLedsModel model) {
		this.model = model;
		//connect(model.getHostAddress(), model.getPort());
	}
	
	
	public boolean connect() {
		return connect(model.getHostAddress(), model.getPort());
	}
	Socket s;
	private boolean connect(String hostAddress, int port) {
		try {
			System.out.println("Connecting to host...");
			s = new Socket("192.168.1.42", 23);
			s.setTcpNoDelay(true);
			s.setKeepAlive(true);
			s.setSoLinger(true, 10);
			
//			s.setTrafficClass();
//			is = s.getInputStream();
			os = s.getOutputStream();
			
			s.setSendBufferSize(1537);
			s.setReceiveBufferSize(2);
			System.out.println("connected");
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see lt.okt.service.IBitmapBoardService#postData(byte[])
	 */
	public void postData(byte[] data) {
		long writeStart = System.currentTimeMillis();
		try {
			if(null == s || s.isClosed() || s.isOutputShutdown()) {
				reconnect();
			}
			data[0] = 0;
			os.write(data);
			System.out.println("Flusing...");
			os.flush();
			System.out.println(data.length + " written in " + (System.currentTimeMillis() - writeStart) + "ms");
			
			s.close();
			s = null;
		} catch (IOException e) {
			e.printStackTrace();
			reconnect();
		} catch (Exception e) {
			e.printStackTrace();
			reconnect();
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getSenderThreadListener().onSendFinish();
	}

	
	public void sendBitmapToBoard(int[][] pixels1, int[][] pixels2) {
		byte[] data = pixelDataEncoder.pixelsToBytes(pixels1, pixels2);
		new BitmapBoardSenderThread(this, data).start();
	}

	private void reconnect() {
		try {
			if(null != s){
				s.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		connect(model.getHostAddress(), model.getPort());		
	}

	public void blind() {
		try {
			if(null == s || s.isClosed() || s.isOutputShutdown()) {
				reconnect();
			}

			os.write("off\n".getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see lt.okt.service.IBitmapBoardService#setSenderThreadListener(lt.okt.view.SenderThreadListener)
	 */
	public void setSenderThreadListener(SenderThreadListener senderThreadListener) {
		this.senderThreadListener = senderThreadListener;
	}


	/* (non-Javadoc)
	 * @see lt.okt.service.IBitmapBoardService#getSenderThreadListener()
	 */
	public SenderThreadListener getSenderThreadListener() {
		return senderThreadListener;
	}
}
