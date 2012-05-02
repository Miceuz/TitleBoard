package lt.okt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.Rectangle;

import lt.okt.view.AppViewBase;
import lt.okt.view.FullscreenWindowApp;
import lt.okt.view.View;

public class ProjectorTitles extends AppViewBase implements View {
	private static final long serialVersionUID = -4442541119221690711L;
	private JTextPane line1;
	protected static ProjectorTitles self;
	FullscreenWindowApp projectorScreen = null;

	public ProjectorTitles(Controller c) {
		super(c);
	}

	public void setCurrentLine(Object selectedValue) {
		
		String text = (String) selectedValue;
		text = text.replace("\n", "<br/>");
		line1.setText(text);
		line1.repaint();
	}

	public Component getLine1Component() {
		return line1;
	}

	public Component getLine2Component() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void addOutputControlls(JPanel jContentPane2) {
		jContentPane.add(getJTextPane(), null);
	}

	JTextPane getJTextPane() {
		if (line1 == null) {
			line1 = new JTextPane() ;
			line1.setFont(new Font("Tahoma", Font.PLAIN, 13));
			line1.setText("gfdgfdgfdgfd");
			line1.setForeground(Color.white);
			line1.setPreferredSize(new Dimension(385, 16));
			line1.setBounds(new Rectangle(15, 5, 465, 58));
			line1.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, false);
			line1.setEditorKit(getDefaultLineEditorKit());
			line1.setEditable(true);
			line1.setBackground(Color.black);
			line1.setFocusable(false);
			line1.setBorder(null);
		}
		return line1;
	}
	
	public static HTMLEditorKit getDefaultLineEditorKit() {
		HTMLEditorKit kit = new HTMLEditorKit();
		StyleSheet s = new StyleSheet();
		s.addRule("body {font-family: Tahoma;font-size:13pt;color:white;}" );
		kit.setStyleSheet(s);
		return kit;
	}

	public void initialize() {
		int width = 600;
		int height = 489;
		this.setMainFrame(createMainFrame());
		this.getMainFrame().setSize(new Dimension(605, 489));
		this.getMainFrame().setJMenuBar(getJJMenuBar());
		this.getMainFrame().setContentPane(getJContentPane());
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (screen.width - width) / 2;
	    int y = (screen.height - height) / 2;
	    getMainFrame().setBounds(x, y, width, height);
	    jScrollPane.setBounds(new Rectangle(15, 175, 572, 211));
	    loadFileButton.setBounds(new Rectangle(450, 400, 136, 31));
	    reloadFile.setBounds(new Rectangle(300, 400, 76, 31));
	    loadedFileName.setBounds(new Rectangle(15, 400, 271, 31));
	    jProgressBar.setLocation(new Point(14, 68));
	    projectorScreen = new FullscreenWindowApp();
	    projectorScreen.setVisible(true);
	}
	
	
	
	public static void main(String[] args){
		ProjectorController projectorController = new ProjectorController();
		self = new ProjectorTitles(projectorController);
		self.initialize();
		self.getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		self.getMainFrame().setVisible(true);
		projectorController.setProjectorScreen(self.getProjectorScreen());
	}

	private FullscreenWindowApp getProjectorScreen() {
		return projectorScreen;
	}

	protected void showSettingsDialog() {
	}
}
