package lt.okt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lt.okt.view.AppViewBase;
import lt.okt.view.LedBoardSettingsDialog;
import lt.okt.view.View;

public class Titrai extends AppViewBase implements View {

	private static final long serialVersionUID = 1L;
	protected static AppViewBase self;
	
//	public JTextArea line1 = null;
//	public JTextArea line2 = null;
	private JLabel line1;
	private JLabel line2;
	private BufferedImage getTextOnImage(String text) {
		BufferedImage line1image = new BufferedImage(386,16,BufferedImage.TYPE_INT_RGB);

		Graphics2D g = line1image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g.setFont(new Font("Tahoma", Font.PLAIN, 13));
		g.drawString(text.trim(), 1,13);
		return line1image;
	}


	private void setupOutputComponent(JLabel component) {
		component.setPreferredSize(new Dimension(386, 16));
		component.setSize(new Dimension(386, 16));
	}

	JLabel getLine1() {
		if(null == line1){
			line1 = new JLabel(new ImageIcon(getTextOnImage("")));
			setupOutputComponent(line1);
			line1.setLocation(new Point(15, 15));
		}
		return line1;
	}

	JLabel getLine2() {
		if(null == line2){
			line2 = new JLabel(new ImageIcon(getTextOnImage("")));
			setupOutputComponent(line2);
			line2.setLocation(new Point(15, 34));
		}
		return line2;
	}

	
//	JTextArea getJTextPane() {
//		if (line1 == null) {
//			line1 = new JTextArea(){
//				public void paint(java.awt.Graphics g) {
//					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
//					super.paint(g);
//				};
//			};
//			Font font = new Font("Arial", Font.PLAIN, 13);
//			line1.setFont(font);
//			line1.setForeground(Color.white);
//			line1.setPreferredSize(new Dimension(385, 16));
//			line1.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, false);
//			line1.setEditorKit(getDefaultLineEditorKit());
//			line1.setEditable(false);
//			line1.setLocation(new Point(15, 15));
//			line1.setSize(new Dimension(385, 16));
//			line1.setBackground(Color.black);
//			line1.setFocusable(false);
//			line1.setBorder(null);
//			setComponentRenderingHints(line1);
//			
//		}
//		return line1;
//	}

//	private HTMLEditorKit getDefaultLineEditorKit() {
//		HTMLEditorKit kit = new HTMLEditorKit();
//		StyleSheet s = new StyleSheet();
//		s.addRule("body {font-family: Tahoma;font-size:13pt;color:white;}" );
//		kit.setStyleSheet(s);
//		return kit;
//	}

//	JTextArea getJTextPane2() {
//		if (line2 == null) {
//			line2 = new JTextArea() {public void paint(java.awt.Graphics g) {
//				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
//				super.paint(g);
//			};};
//			Font font = new Font("Arial", Font.PLAIN, 13);
//			line2.setFont(font);
//			line2.setForeground(Color.white);
//			line2.setUI(getJTextPane2().getUI());
//			line2.setEditorKit(getDefaultLineEditorKit());
//			line2.setEditable(false);
//			line2.setPreferredSize(new Dimension(385, 16));
//			line2.setLocation(new Point(15, 34));
//			line2.setSize(new Dimension(385, 16));
//			line2.setBackground(Color.black);
//			line2.setFocusable(false);
//			line2.setBorder(null);
//			setComponentRenderingHints(line2);
//		}
//		return line2;
//	}

	/**
	 * This is the default constructor
	 */
	public Titrai(Controller c) {
		super(c);		
//		System.setProperty("apple.awt.textantialiasing", "off");
//		System.setProperty("apple.awt.graphics.EnableLazyPixelConversion", "false");
//		System.setProperty("swing.aatext", "false");
//		System.setProperty("awt.useSystemAAFontSettings", "off");
//		System.setProperty("apple.awt.graphics.UseQuartz", "false");
//		System.setProperty("swing.aatext", "false");
//		System.setProperty("awt.useSystemAAFontSettings","off");
//		System.setProperty("swing.aatext", "false");
	}
	
	public void setCurrentLine(Object selectedValue) {
		String s = (String) selectedValue;
		s = s.replaceAll("\\<.*?\\>", "");
		String[] ss = s.split("\n");
		if(ss.length >1) {
			getLine1().setIcon(new ImageIcon(getTextOnImage(ss[0])));
			getLine2().setIcon(new ImageIcon(getTextOnImage(ss[1])));
		} else {
			getLine1().setIcon(new ImageIcon(getTextOnImage(s)));
			getLine2().setIcon(new ImageIcon(getTextOnImage("")));
		}			
		getLine1().repaint();
		getLine2().repaint();
		System.out.println(s);
	}
	
	public Component getLine1Component() {
		return line1;
	}

	public Component getLine2Component() {
		return line2;
	}

	protected void addOutputControlls(JPanel jContentPane2) {
		jContentPane2.add(getLine1());
		jContentPane2.add(getLine2());
	}

	public void onInitStart() {
		networkProblemsDialog();
	}

	protected void networkProblemsDialog() {
//		if(null == networkProbDialog) {
//			networkProbDialog = new JDialog(null, "asdf", ModalityType.MODELESS);
//			networkProbDialog.setResizable(false);
//			networkProbDialog.setSize(new Dimension(300, 150));
//			Container contentPane = networkProbDialog.getContentPane();
//	
//	        contentPane.setLayout(new BorderLayout());
//	        JTextArea textArea = new JTextArea("Establishing connection to the LED title board... \n\n" +
//					"If you see this message for long time, this means, that" +
//					" the program is having difficulties connecting to the board. \n\nPlease make sure:\n" +
//					"- no more instances of this programm are running\n" +
//					"- you are connected to wireless network \"OKT LED BOARD\". \n\n" +
//					"Pinging address 192.168.1.42 might help to debug the problem too.");
//	        textArea.setEditable(false);
//			contentPane.add(textArea, BorderLayout.CENTER);
//	        networkProbDialog.pack();
//	        networkProbDialog.setLocationRelativeTo(this.mainFrame);
//	
//		}
//		networkProbDialog.setVisible(true);
	}
	public void onInitFinish() {
		networkProbDialog.setVisible(false);
	}

	public static void main(String[] args){
		self = new Titrai(new EtherLedsController());
		if(EventQueue.isDispatchThread()) {
			self.initialize();
			
			self.getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			self.getMainFrame().setVisible(true);
		} else {
			try {
				EventQueue.invokeAndWait(new Runnable() {
					public void run() {
						self.initialize();
						self.getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						self.getMainFrame().setVisible(true);
						self.getController().initialize();
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	protected void showSettingsDialog() {
		LedBoardSettingsDialog d = new LedBoardSettingsDialog(self.getMainFrame(), (Controller) getController());
		d.setVisible(true);
	}

	
		
	public void fileLoaded(File f) {
		if(null != f) {
			loadedFileName.setText("Current file: " + f.getName());
			loadedFileName.setVisible(true);
			reloadFile.setVisible(true);
		}
	}
	
	public void disableListComponent(String progressBarText) {
		
		jList.setEnabled(false);
		jProgressBar.setString(progressBarText);
		jProgressBar.setVisible(true);
	}

	public void enableListComponent() {
		jProgressBar.setVisible(false);
		jList.setEnabled(true);
		jList.requestFocusInWindow();
	}
}  //  @jve:decl-index=0:visual-constraint="132,1"
