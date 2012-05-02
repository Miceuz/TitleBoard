package lt.okt.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import lt.okt.ProjectorTitles;

public class FullscreenWindowApp extends JFrame {
	private static final long serialVersionUID = 8754404803062037215L;
	private boolean isFullScreen = false;

	public FullscreenWindowApp() {
		super();
		initialize();
	}

	private void initialize() {
		setLayout(null);
		setTitle("Drag me to projector window");
		setBounds(0, 0, 800, 600);
		this.setBackground(Color.black);
		this.setContentPane(getJPanel());
		addComponentListener(new ComponentAdapter(){
			@Override
			public void componentShown(ComponentEvent e) {
				text.setBounds(0, text.getParent().getBounds().height - text.getFontMetrics(text.getFont()).getHeight() * lineCount, e.getComponent().getBounds().width, text.getFontMetrics(text.getFont()).getHeight() * lineCount);
				updateStyleSheet(text);
			}
			@Override
			public void componentResized(ComponentEvent e) {
				text.setBounds(0, text.getParent().getBounds().height - text.getFontMetrics(text.getFont()).getHeight() * lineCount, e.getComponent().getBounds().width, text.getFontMetrics(text.getFont()).getHeight() * lineCount);
				resetDrag();				
			}
		});
		
//		addMouseWheelListener(new MouseAdapter() {
//
//			public void mouseWheelMoved(MouseWheelEvent e) {
//				Font f = text.getFont();
//				Font newFont = new Font(f.getName(), f.getStyle(), f.getSize() + -1 * e.getWheelRotation());
//				text.setFont(newFont);
//				Rectangle bounds = text.getBounds();
//				int fontHeight = text.getFontMetrics(newFont).getHeight() + text.getFontMetrics(newFont).getLeading() *2;
//				text.setBounds(bounds.x, bounds.y, bounds.width, fontHeight * lineCount );
//				updateStyleSheet(text);
//				fixTextFieldPosition();
//			}
//
//		});
		
//		addMouseMotionListener(new MouseAdapter() {
//			public void mouseDragged(MouseEvent e) {
//				if(0 == lastDragY ) {
//					lastDragY = e.getY();
//				}
//				if(0 == lastDragX) {
//					lastDragX = e.getX();
//				}
//				Rectangle currBounds = text.getBounds();
//				if(text.getParent().contains(currBounds.x, currBounds.y + (e.getY() - lastDragY) + currBounds.height) &&
//					text.getParent().contains(currBounds.x, currBounds.y + (e.getY() - lastDragY))
//				) {
//					text.setBounds(currBounds.x, currBounds.y + (e.getY() - lastDragY), currBounds.width, currBounds.height);
//				}
//				lastDragY = e.getY();
//				lastDragX = e.getX();
//			}
//		});
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(2 == e.getClickCount()) {
					if(isFullScreen) {
						leaveFullScreen();
					} else {
						goFullScreen();
					}
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
				resetDrag();
			}
		});
	}


	private int lineCount = 2;

	int lastDragY = 0;
	int lastDragX = 0;
	
	public void goFullScreen() {
		setVisible(false);
		dispose();
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		//setResizable(false);
		setVisible(true);
		isFullScreen = true;
	}

	public void leaveFullScreen() {
		setVisible(false);
		dispose();
		setBounds(0, 0, 800, 600);
		setExtendedState(NORMAL);
		setUndecorated(false);
		setResizable(true);
		setVisible(true);
		isFullScreen = false;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBackground(Color.black);
			jPanel.add(getText(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes text	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getText() {
		if (text == null) {
			text = new TransparentJTextPane();
			text.setBackground(Color.black);
			text.setForeground(Color.white);
			text.setFont(new Font("Arial", Font.PLAIN, 48));
			text.setBounds(new Rectangle(72, 92, 289, 91));
			text.setEditorKit(ProjectorTitles.getDefaultLineEditorKit());
			text.setText("laba <b>diena</b> su <i>vištiena</i><br/>viso gero su gaidiena");
			text.setEditable(false);
			text.setFocusable(false);
		}
		return text;
	}

	private void updateStyleSheet(JTextPane text) {
		String text2 = text.getText();
		HTMLEditorKit htmlEditorKit = (HTMLEditorKit)text.getEditorKit();

		StyleSheet s = new StyleSheet();
		s.addRule("body {font-family: " + text.getFont().getFamily() + ";font-size:" +text.getFont().getSize()+ "pt;color:white;}");
		htmlEditorKit.setStyleSheet(s);
		text.setEditorKit(htmlEditorKit);
		text.setText(text2);
	}

	
	public static void main(String[] args) {
		new FullscreenWindowApp().setVisible(true);
	}

	PopUpMenu popup = new PopUpMenu();
	private JPanel jPanel = null;
	private JTextPane text = null;

	class PopUpMenu extends JPopupMenu {
		private static final long serialVersionUID = -5446830299914841003L;
		JMenuItem fsItem;

		public PopUpMenu() {
			fsItem = new JMenuItem("Go full screen");
			add(fsItem);
			fsItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (isFullScreen) {
						leaveFullScreen();
						fsItem.setText("Go full screen");
					} else {
						goFullScreen();
						fsItem.setText("Leave full screen");
					}
				}
			});
		}
	}

	public void setText(String selectedValue) {
		text.setText(selectedValue);
	}

	void resetDrag() {
		lastDragX = 0;
		lastDragY = 0;
	}

	void fixTextFieldPosition() {
		Rectangle bounds = text.getBounds();
		if(!text.getParent().contains(bounds.x, bounds.y + bounds.height)) {
			text.setBounds(bounds.x, text.getParent().getBounds().height - bounds.height, bounds.width, bounds.height);
		}
	}
}  //  @jve:decl-index=0:visual-constraint="117,-4"

class TransparentJTextPane extends JTextPane {
	private static final long serialVersionUID = 6781017053938550333L;

	@Override
	public boolean contains(int x, int y) {
		return false;
	}
}
