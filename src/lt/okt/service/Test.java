package lt.okt.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class Test {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		try {
			System.out.println("Getting idenfier");
			CommPortIdentifier id = CommPortIdentifier.getPortIdentifier("/dev/tty.usbmodem1d11");
			System.out.println("Opening port");
			final SerialPort port = (SerialPort) id.open("Testapp", 0);
			final boolean[] stop = { false };
			port.disableReceiveThreshold();
			port.disableReceiveFraming();
			port.disableReceiveTimeout();
			port.setDTR(false);
			port.setRTS(false);
			try {
				port.setInputBufferSize(1024);
				port.setOutputBufferSize(1024);
				port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
				port.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
			
			port.addEventListener(new SerialPortEventListener() {
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
			});
			System.out.println("Writing");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				FileInputStream is = new FileInputStream("/Users/mm/Desktop/data.gray");
				byte[] buff = new byte[1024];
				is.read(buff);
				
				port.getOutputStream().write("send 1024 3\n".getBytes());
				Thread.sleep(5000);
				System.out.println("Writing big buffer");
				for(int j = 0; j < 1024; j++) {
					port.getOutputStream().write(buff, j, 1);
					port.getOutputStream().flush();
					Thread.sleep(5);
				}
				port.getOutputStream().write("\n".getBytes());
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			//byte[] input = new byte[128];
			System.out.println("Reading...");
			while (!stop[0]) {
				try {
					Thread.sleep(100);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			try {
				port.getOutputStream().close();
				port.getInputStream().close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			port.close();

//			port.getInputStream().read(input);
//			int b = 0;
//			int i = 0;
//			while(b != -1 && b != '\n') {
//				b = port.getInputStream().read();
//				input[i++] = (byte) b;
//				System.out.println(b);
//			}
			
//			System.out.println("Red "+i+" bytes:" + (new String(input)));
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}

}
