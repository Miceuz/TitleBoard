package lt.okt.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class CDCTest {

	private static final class CDCPortEventListener implements
			SerialPortEventListener {
		private final SerialPort port;

		private CDCPortEventListener(SerialPort port) {
			this.port = port;
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
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			final boolean[] stop = { false };

			final SerialPort port = setUpPort();
			
			try {
				FileInputStream is = new FileInputStream("/Users/mm/Desktop/data.gray");
				byte[] buff = new byte[1024];
				is.read(buff);
				
				port.getOutputStream().write("send 1024 3\n".getBytes());
				System.out.println("Writing big buffer");
				for(int j = 0; j < 1024; j++) {
					port.getOutputStream().write(buff, j, 1);
					port.getOutputStream().flush();
//					Thread.sleep(1);
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

		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}

	private static SerialPort setUpPort() throws NoSuchPortException,
			PortInUseException, TooManyListenersException {
		System.out.println("Getting idenfier");
		CommPortIdentifier id = CommPortIdentifier.getPortIdentifier("/dev/tty.usbmodem1d11");
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
		
		port.addEventListener(new CDCPortEventListener(port));
		return port;
	}

}
