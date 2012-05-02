package lt.okt.view;

import java.awt.BorderLayout;
import java.awt.Container;
//import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import lt.okt.Controller;

public abstract class AppViewBase implements View {

	private static final long serialVersionUID = -206557074726369175L;
	
	protected JPanel jContentPane = null;
	protected JList jList = null;
	protected JScrollPane jScrollPane = null;
	private Controller controller;
	protected JButton loadFileButton = null;
	protected JToggleButton blindToggle = null;
	protected JLabel loadedFileName = null;
	protected JButton reloadFile = null;
	protected JProgressBar jProgressBar = null;
	protected JMenuBar jJMenuBar = null;
	protected JMenu jMenu = null;
	protected JMenuItem jMenuItem = null;
	protected JFrame mainFrame;

	private JMenu fileMenu;

	private JMenu helpMenu;

	protected JDialog networkProbDialog;

	public AppViewBase(Controller c) {
		this.setController(c);
		c.setView(this);
		System.setProperty("awt.useSystemAAFontSettings", "false");
	}

	
	protected JList getJList() {
		if (jList == null) {
			jList = new JList();
//			setComponentRenderingHints(jList);
			jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jList.setFocusable(true);
			jList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					if(!e.getValueIsAdjusting()) {
						getController().lineChanged(jList.getSelectedValue());
						if(null != jList.getSelectedValue()) {
							jList.ensureIndexIsVisible(jList.getSelectedIndex());
						}
					}
				}
			});
			for(KeyListener kl : jList.getKeyListeners()) {
				jList.removeKeyListener(kl);
			}
			jList.addKeyListener(new java.awt.event.KeyListener() {
				public void keyPressed(java.awt.event.KeyEvent e) {
					if(KeyEvent.VK_ENTER == e.getKeyCode()){
						jList.setSelectedIndex(jList.getSelectedIndex()+1);
						jList.ensureIndexIsVisible(jList.getSelectedIndex());
					}else if(KeyEvent.VK_SPACE == e.getKeyCode()) {
						blindToggle.doClick();
					}
				}
				public void keyTyped(java.awt.event.KeyEvent e) {
				}
				public void keyReleased(java.awt.event.KeyEvent e) {
				}
			});
			jList.setCellRenderer(new JTextPaneCellRenderer());
		}
		//controller.loadText(null);
		return jList;
	}

	protected JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(15, 75, 572, 211));
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes loadFileButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	protected JButton getLoadFileButton() {
		if (loadFileButton == null) {
			loadFileButton = new JButton();
			loadFileButton.setBounds(new Rectangle(450, 300, 136, 31));
			loadFileButton.setText("Load new file...");
			loadFileButton.setFocusable(false);
			loadFileButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					openFileDialog();
				}

			});
		}
		return loadFileButton;
	}

	private void openFileDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Load file");
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileHidingEnabled(true);
		chooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Microsoft Word document";
			}

			@Override
			public boolean accept(File f) {
				if(f.isDirectory()) {
					return true;
				}
				return "doc".equals(Utils.getExtension(f));
			}
		});
		
		int chooseResult = chooser.showOpenDialog(null);
		if(JFileChooser.APPROVE_OPTION == chooseResult) {
			File f = chooser.getSelectedFile();
			getController().loadText(f);
		}
	}

	/**
	 * This method initializes blindToggle	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	protected JToggleButton getBlindToggle() {
		if (blindToggle == null) {
			blindToggle = new JToggleButton();
			blindToggle.setBounds(new Rectangle(494, 26, 91, 24));
			blindToggle.setText("Blind");
			blindToggle.setFocusable(false);
			blindToggle.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if(ItemEvent.SELECTED == e.getStateChange()) {
						getController().blind();
					} else {
						getController().unBlind();
					}
				}
			});
		}
		return blindToggle;
	}

	/**
	 * This method initializes reloadFile	
	 * 	
	 * @return javax.swing.JButton	
	 */
	protected JButton getReloadFile() {
		if (reloadFile == null) {
			reloadFile = new JButton();
			reloadFile.setBounds(new Rectangle(300, 300, 76, 31));
			reloadFile.setText("Reload");
			reloadFile.setVisible(false);
			reloadFile.setFocusable(false);
			reloadFile.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getController().reloadText();
				}
			});
		}
		return reloadFile;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	protected JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setStringPainted(true);
			jProgressBar.setIndeterminate(true);
			jProgressBar.setString("Sending text to board...");
			jProgressBar.setLocation(new Point(14, 53));
			jProgressBar.setSize(new Dimension(386, 16));
			jProgressBar.setVisible(false);
		}
		return jProgressBar;
	}

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	protected JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	protected JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");

			JMenuItem aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About this program");
			aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showAbout();
				}
			});

			helpMenu.add(aboutMenuItem);
		}
		return helpMenu;
	}
	
	public void onInitStart() {
	}
	
	public void onInitFinish() {
	}
	
	public void showAbout() {
		JOptionPane.showMessageDialog(mainFrame,
			    "OKT Titles v1.3\n\n" +
			    "(c) 2012 Albertas Mickenas\n" +
			    "mic@hardcore.lt mic@wemakethings.net\n" +
			    "http://blog.hardcore.lt/mic\n" +
			    "http://wemakethings.net\n\n" +
			    
			    "If you like this program and are going to use it, please contact me\n" +
			    "and support my programmer ass by sending me some money. \n\n" +
			    
			    "I'm an independent software/hardware technician trying to live my life by \n" +
			    "sticking to ideals of openness and freedom, but i still need to feed myself\n" +
			    "and a cat or two.\n\n" +
			    
			    "In return I can tailor it according to your specific needs. \nThank you.",
			    "About this program",
			    JOptionPane.INFORMATION_MESSAGE);
	}

	protected JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");

			JMenuItem openMenuItem = new JMenuItem();
			openMenuItem.setText("Open File...");
			openMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					openFileDialog();
				}
			});
			fileMenu.add(openMenuItem);

			
			JMenuItem settingsMenuItem = new JMenuItem();
			settingsMenuItem.setText("Settings...");
			settingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showSettingsDialog();
				}
			});
			
			fileMenu.add(settingsMenuItem);
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
//	protected JMenu getJMenu() {
//		if (jMenu == null) {
//			jMenu = new JMenu();
//			jMenu.setText("Menu");
//			jMenu.add(getJMenuItem());
//		}
//		return jMenu;
//	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
//	protected JMenuItem getJMenuItem() {
//		if (jMenuItem == null) {
//			jMenuItem = new JMenuItem();
//			jMenuItem.setText("Settings...");
//			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
//				public void actionPerformed(java.awt.event.ActionEvent e) {
//					showSettingsDialog();
//				}
//			});
//		}
//		return jMenuItem;
//	}

	
	protected abstract void showSettingsDialog();

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void initialize() {
		int width = 600;
		int height = 389;
		this.setMainFrame(createMainFrame());
		this.getMainFrame().setSize(new Dimension(605, 389));
		this.getMainFrame().setJMenuBar(getJJMenuBar());
		this.getMainFrame().setContentPane(getJContentPane());
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (screen.width - width) / 2;
	    int y = (screen.height - height) / 2;
	    getMainFrame().setBounds(x, y, width, height);
	}

	protected JFrame createMainFrame() {
		return new JFrame("Titles");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	protected JPanel getJContentPane() {
		if (jContentPane == null) {
			
			loadedFileName = new JLabel();
			loadedFileName.setBounds(new Rectangle(15, 300, 271, 31));
			loadedFileName.setText("Current file:");
			loadedFileName.setVisible(false);
			loadedFileName.setFocusable(false);
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJScrollPane(), null);
			addOutputControlls(jContentPane);
			jContentPane.add(getLoadFileButton(), null);
			jContentPane.add(getBlindToggle(), null);
			jContentPane.add(loadedFileName, null);
			jContentPane.add(getReloadFile(), null);
			jContentPane.add(getJProgressBar(), null);
		}
		return jContentPane;
	}

	protected abstract void addOutputControlls(JPanel jContentPane2) ;

	public void setTextLines(final List<String> textLines) {
		Runnable t = new Runnable() {
			public void run() {
				int i = jList.getSelectedIndex();
				String[] lines = new String[textLines.size()];
				int lineNum = 1;
				for (int j = 0; j < textLines.size(); j++) {
					if(!textLines.get(j).startsWith("---")) {
						lines[j] = (lineNum++) + "| " + textLines.get(j);
					} else {
						System.out.println(textLines.get(j));
						lines[j] = "<h1>"+ textLines.get(j).replace("---", "") +"</h1>";
					}
				}
				jList.setListData(lines);
				enableListComponent();
				jList.setSelectedIndex(i);
			}
		};
		SwingUtilities.invokeLater(t);
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

	public void fileLoaded(File f) {
		if(null != f) {
			loadedFileName.setText("Current file: " + f.getName());
			loadedFileName.setVisible(true);
			reloadFile.setVisible(true);
		}
	}

	public void settingsUpdated() {
		getController().reloadText();
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setComponentRenderingHints(JComponent line) {
		line.putClientProperty(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		line.putClientProperty(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		line.putClientProperty(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		line.putClientProperty(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		line.putClientProperty(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		line.putClientProperty(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		line.putClientProperty(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		line.putClientProperty(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		line.putClientProperty(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
//		line.putClientProperty(RenderingHints.KEY_TEXT_LCD_CONTRAST, 0);
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Controller getController() {
		return controller;
	}
}