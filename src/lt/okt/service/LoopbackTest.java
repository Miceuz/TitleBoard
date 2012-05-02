package lt.okt.service;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class LoopbackTest {

    private SerialPort serialPort;
    private OutputStream outStream;
    private InputStream inStream;

    public void connect(String portName) throws IOException {
        try {
            // Obtain a CommPortIdentifier object for the port you want to open
            CommPortIdentifier portId =
                    CommPortIdentifier.getPortIdentifier(portName);

            // Get the port's ownership
            serialPort =
                    (SerialPort) portId.open("Demo application", 5000);

            // Set the parameters of the connection.
            setSerialPortParameters();

            // Open the input and output streams for the connection.
            // If they won't open, close the port before throwing an
            // exception.
            outStream = serialPort.getOutputStream();
            inStream = serialPort.getInputStream();
        } catch (NoSuchPortException e) {
            throw new IOException(e.getMessage());
        } catch (PortInUseException e) {
            throw new IOException(e.getMessage());
        } catch (IOException e) {
            serialPort.close();
            throw e;
        }
    }

    /**
     * Get the serial port input stream
     * @return The serial port input stream
     */
    public InputStream getSerialInputStream() {
        return inStream;
    }

    /**
     * Get the serial port output stream
     * @return The serial port output stream
     */
    public OutputStream getSerialOutputStream() {
        return outStream;
    }

    /**
     * Register event handler for data available event
     *
     * @param eventHandler Event handler
     */
    public void addDataAvailableEventHandler(
            SerialPortEventListener eventHandler) {
        try {
            // Add the serial port event listener
            serialPort.addEventListener(eventHandler);
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Disconnect the serial port
     */
    public void disconnect() {
        if (serialPort != null) {
            try {
                // close the i/o streams.
                outStream.close();
                inStream.close();
            } catch (IOException ex) {
                // don't care
                }
            // Close the port.
            serialPort.close();
        }
    }

    /**
     * Sets the serial port parameters to 57600bps-8N1
     */
    private void setSerialPortParameters() throws IOException {
        int baudRate = 57600; // 57600bps

        try {
            // Set serial port to 57600bps-8N1..my favourite
            serialPort.setSerialPortParams(
                    baudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(
                    SerialPort.FLOWCONTROL_NONE);
        } catch (UnsupportedCommOperationException ex) {
            throw new IOException("Unsupported serial port parameter");
        }
    }

    public static class SerialEventHandler implements SerialPortEventListener {

        private InputStream inStream;
        private int readBufferLen;
        private int readBufferOffset;
        private byte[] readBuffer;

        public SerialEventHandler(InputStream inStream, int readBufferLen) {
            this.inStream = inStream;
            this.readBufferLen = readBufferLen;
            readBuffer = new byte[readBufferLen];
        }

        public boolean isBufferFull() {
            return (readBufferOffset == readBufferLen);
        }

        public String getReadBuffer() {
            return new String(readBuffer);
        }

        public void serialEvent(SerialPortEvent event) {
            switch (event.getEventType()) {
                case SerialPortEvent.DATA_AVAILABLE:
                    readSerial();
                    break;
            }
        }

        private void readSerial() {
            try {
                int availableBytes = inStream.available();
                if (availableBytes > 0) {
                    // Read the serial port
                    readBufferOffset +=
                            inStream.read(readBuffer, readBufferOffset,
                            availableBytes);
                }
            } catch (IOException e) {
            }
        }

        public static void main(String[] args) {
            // Timeout = 1s
            final int TIMEOUT_VALUE = 10000;

            LoopbackTest loopbackTest = new LoopbackTest();
            try {
                // Open serial port
                loopbackTest.connect("/dev/tty.usbmodem1a21");

                // Register the serial event handler
                String testString = "The quick brown fox jumps over " +
                        "the lazy dog";
                InputStream inStream =
                        loopbackTest.getSerialInputStream();

                SerialEventHandler serialEventHandler =
                        new SerialEventHandler(inStream, testString.length());
                loopbackTest.addDataAvailableEventHandler(serialEventHandler);

                // Send the testing string
                OutputStream outStream =
                        loopbackTest.getSerialOutputStream();
                outStream.write(testString.getBytes());

                // Wait until all the data is received
                long startTime = System.currentTimeMillis();
                long elapsedTime;
                do {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                    elapsedTime = System.currentTimeMillis() - startTime;
                } while ((elapsedTime < TIMEOUT_VALUE) &&
                        (!serialEventHandler.isBufferFull()));

                // Check the data if not TIMEOUT
                if (elapsedTime < TIMEOUT_VALUE) {
                    if (testString.equals(serialEventHandler.getReadBuffer())) {
                        System.out.println("All data is received successfully");
                    } else {
                        System.out.println("Test failed");
                        System.out.println("Sent:" + testString);
                        System.out.println("Received:" +
                                serialEventHandler.getReadBuffer());
                    }
                } else {
                    System.err.println("Timeout");
                }

                System.out.println("Test done");
                loopbackTest.disconnect();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}

