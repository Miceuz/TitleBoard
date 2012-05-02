package lt.okt.service;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import lt.okt.view.SenderThreadListener;

public class RunningTextService implements IBitmapBoardService, SerialPortEventListener{
	private SenderThreadListener senderThreadListener;
	private PixelDataEncoder2bpp pixelDataEncoder = new PixelDataEncoder2bpp();
	SerialPort port = null;
	
	private CommPortIdentifier id;


	public RunningTextService() {
	}
	
	public static void main(String[] args) {
		RunningTextService s = new RunningTextService();
		try {
			s.port = s.setUpPort();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.port.getInputStream()));
			for(int i = 0; i < 1000; i++) {
				System.out.println("Screen: " + i);
				s.port.getOutputStream().write("send 1024 0 0\n".getBytes());
				//Thread.sleep(50);
				s.waitForOK(reader);  
				
				
				int chunkSize = 32;
				
				for(int j = 0; j < 1024 / chunkSize; j++) {
					System.out.println(i + " index: " + j + " of " + 1024 / chunkSize);
					byte[] b = new byte[chunkSize];
					for (int k = 0; k < b.length; k++) {
						b[k] = (byte) 0x7F;
					}
					s.port.getOutputStream().write(b);
					//Thread.sleep(50);
					s.waitForOK(reader);
				}
				Thread.sleep(0);
			}
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		s.port.close();
	}
	
	public void sendBitmapToBoard(int[][] linePixels, int color, final int index) {
		final byte[] data = pixelDataEncoder.pixelsToBytes(linePixels, color);
		Runnable r = new Runnable() {
			public void run() {
				postData(data, index);
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	public void postData(byte[] data) {
		postData(data, 0);
	}
	
//	public void postData(byte[] data, int index) {
//		try {
//			if(null == port) {
//				port = setUpPort2();
//			}
//			outputBufferEmptyFlag = false;
//			port.getOutputStream().write(("\n").getBytes());
//			Thread.sleep(1500);
//			port.getOutputStream().write(("send 1024 " + index + "\n").getBytes());
//			Thread.sleep(1500);
//			System.out.println("Writing big buffer");
//			for(int j = 0; j < 64; j++) {
//				outputBufferEmptyFlag = false;
//				System.out.println("Writing " + j + " of " + 64);
//				port.getOutputStream().write(data, j * 16, 16);
//				port.getOutputStream().flush();
//				Thread.sleep(250);
//			}
//			Thread.sleep(500);
//			System.out.println("Sending finished...");
//			//System.out.println("Closing port...");
//			//port.close();
//		} catch (NoSuchPortException e) {
//			e.printStackTrace();
//			port = null;
//		} catch (PortInUseException e) {
//			e.printStackTrace();
//			port = null;
//		} catch (TooManyListenersException e) {
//			e.printStackTrace();
//			port = null;
//		} catch (IOException e) {
//			e.printStackTrace();
//			port = null;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		getSenderThreadListener().onSendFinish();
//	}

	public void postData(byte[] data, int index) {
		int saveAfterSend = 1;
		if(index < 0) {
			index = 0;
			saveAfterSend = 0;
		}
		try {
			if(null == port) {
				port = setUpPort();
			}
			OutputStream outputStream = port.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(port.getInputStream()));

			outputStream.write(("send 1024 " + index + " " + saveAfterSend + "\n").getBytes());
			waitForOK(reader);
			
			System.out.println("Writing buffer");
			int chunkSize = 32;
			for(int j = 0; j < 1024 / chunkSize; j++) {
				System.out.println("Writing " + j + " of " + 1024 / chunkSize);
				outputStream.write(data, j * chunkSize, chunkSize);
				Thread.sleep(0);
				waitForOK(reader);
			}
			System.out.println("Sending finished...");
		} catch (NoSuchPortException e) {
			e.printStackTrace();
			port = null;
		} catch (PortInUseException e) {
			e.printStackTrace();
			port = null;
		} catch (TooManyListenersException e) {
			e.printStackTrace();
			port = null;
		} catch (IOException e) {
			e.printStackTrace();
			port = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getSenderThreadListener().onSendFinish();
	}

	private void waitForOK(BufferedReader reader) throws IOException {
		String s;
		s = reader.readLine();
		while(!s.equals("OK")) {
			System.out.println("*" + s);
			s = reader.readLine();
		}
		System.out.println("*" + s);
	}

	public void startScroll() {
		try {
			if(null == port) {
				port = setUpPort();
			}
			port.getOutputStream().write("startScroll\n".getBytes());
			waitForOK(new BufferedReader(new InputStreamReader(port.getInputStream())));
			port.close();
			port = null;
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopScroll() {
		try {
			if(null == port) {
				port = setUpPort();
			}
			port.getOutputStream().write("stopScroll\n".getBytes());
			waitForOK(new BufferedReader(new InputStreamReader(port.getInputStream())));
			port.close();
			port = null;
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSpeed(int speed) {
		try {
			if(null == port) {
				port = setUpPort();
			}
			port.getOutputStream().write(("setSpeed " + speed + "\n").getBytes());
			waitForOK(new BufferedReader(new InputStreamReader(port.getInputStream())));
			port.close();
			port = null;
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSenderThreadListener(SenderThreadListener senderThreadListener) {
		this.senderThreadListener = senderThreadListener;
	}

	public SenderThreadListener getSenderThreadListener() {
		return this.senderThreadListener;
	}

	@SuppressWarnings("unchecked")
	public boolean initPort() {
		Enumeration<CommPortIdentifier> ids = CommPortIdentifier.getPortIdentifiers();
		while(ids.hasMoreElements()) {
			CommPortIdentifier i = ids.nextElement();
			System.out.println(i.getName());
			if(i.getName().contains("usbmodem") || i.getName().contains("ttyACM")) {
				this.id = i;
				return true;
			}
		}
		return false;
	}
	
	private SerialPort setUpPort() throws NoSuchPortException, PortInUseException, TooManyListenersException {
		System.out.println("Getting idenfier");
		if(null == this.id) {
			this.id = CommPortIdentifier.getPortIdentifier("/dev/tty.usbmodem1d11");
		}
		System.out.println("Opening port");
		SerialPort port = (SerialPort) id.open("RunningBoardTitles", 0);
		try {
			port.setSerialPortParams(57600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			port.setInputBufferSize(4);
			port.disableReceiveTimeout();
			port.enableReceiveThreshold(1);
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}

		return port;
	}
	
	private SerialPort setUpPort2() throws NoSuchPortException, PortInUseException, TooManyListenersException {
		System.out.println("Getting idenfier");
		if(null == this.id) {
			this.id = CommPortIdentifier.getPortIdentifier("/dev/tty.usbmodem1d11");
		}
		System.out.println("Opening port");
		final SerialPort port = (SerialPort) id.open("Testapp", 0);
		port.disableReceiveThreshold();
		port.disableReceiveFraming();
		port.disableReceiveTimeout();
		port.setDTR(false);
		port.setRTS(false);
		try {
			port.setInputBufferSize(1024);
			port.setOutputBufferSize(1024);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			port.setSerialPortParams(115200, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e2) {
			e2.printStackTrace();
		}
		port.notifyOnDataAvailable(true);
		port.notifyOnOutputEmpty(true);
		port.notifyOnBreakInterrupt(true);
		port.notifyOnCarrierDetect(true);
		port.notifyOnCTS(true);
		port.notifyOnDSR(true);
		port.notifyOnFramingError(true);
		port.notifyOnOverrunError(true);
		port.notifyOnParityError(true);
		port.notifyOnRingIndicator(true);

		port.addEventListener(this);
		return port;
	}
	
		public void serialEvent(SerialPortEvent e) {
			if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				System.out.println("Data available");
				int n;
				try {
					n = port.getInputStream().available();
					byte[] input = new byte[n];
					port.getInputStream().read(input);
					System.out.println("Read " + n + " bytes: " + new String(input));
					//stop[0] = true;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if(e.getEventType() == SerialPortEvent.OUTPUT_BUFFER_EMPTY) {
				System.out.println("Output buffer empty");
			} else if(e.getEventType() == SerialPortEvent.BI) {
				System.out.println("BreakInterrupt");
			} else if(e.getEventType() == SerialPortEvent.CD) {
				System.out.println("CD");
			} else if(e.getEventType() == SerialPortEvent.CTS) {
				System.out.println("CTS");
			} else if(e.getEventType() == SerialPortEvent.DSR) {
				System.out.println("DSR");
			} else if(e.getEventType() == SerialPortEvent.FE) {
				System.out.println("FramingError");
			} else if(e.getEventType() == SerialPortEvent.OE) {
				System.out.println("OverrunError");
			} else if(e.getEventType() == SerialPortEvent.PE) {
				System.out.println("ParityError");
			} else if(e.getEventType() == SerialPortEvent.RI) {
				System.out.println("Ring indicator");
			}
		}

		public void blind() {
			try {
				if(null == port) {
					port = setUpPort();
				}
				port.getOutputStream().write(("stopScreen\n").getBytes());
				waitForOK(new BufferedReader(new InputStreamReader(port.getInputStream())));
				port.close();
				port = null;
			} catch (NoSuchPortException e) {
				e.printStackTrace();
			} catch (PortInUseException e) {
				e.printStackTrace();
			} catch (TooManyListenersException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void unblind() {
			try {
				if(null == port) {
					port = setUpPort();
				}
				port.getOutputStream().write(("startScreen\n").getBytes());
				waitForOK(new BufferedReader(new InputStreamReader(port.getInputStream())));
				port.close();
				port = null;
			} catch (NoSuchPortException e) {
				e.printStackTrace();
			} catch (PortInUseException e) {
				e.printStackTrace();
			} catch (TooManyListenersException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
