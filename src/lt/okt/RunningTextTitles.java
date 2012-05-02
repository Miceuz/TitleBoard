package lt.okt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import lt.okt.view.AppViewBase;
import lt.okt.view.RunningTextController;

public class RunningTextTitles extends AppViewBase {

	private static RunningTextTitles self;
	private JLabel line1;

	public RunningTextTitles(Controller c) {
		super(c);
		c.setView(this);
	}

	public void setCurrentLine(Object selectedValue, int color) {
		String s = (String) selectedValue;
		s = s.replaceAll("\\<.*?\\>", "");
		String[] ss = s.split("\n");
		if(ss.length >1) {
			getLine1().setIcon(new ImageIcon(getTextOnImage(ss[0], color)));
		} else {
			getLine1().setIcon(new ImageIcon(getTextOnImage(s, color)));
		}			
		getLine1().repaint();
		System.out.println(s);
	}
	
	JPopupMenu jPopupMenu = getListPopup();
	
	ListItem setColorItem;
	@Override
	protected JList getJList() {
		JList list = super.getJList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				System.out.println("Click!");
				if (e.isPopupTrigger()) { //if the event shows the menu
					setColorItem = (ListItem) jList.getModel().getElementAt(jList.locationToIndex(e.getPoint()));
			        //jList.setSelectedIndex(jList.locationToIndex(e.getPoint())); //select the item
			        jPopupMenu.show(jList, e.getX(), e.getY()); //and show the menu
			    }
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
			}
		});
		return list;
	}

	private JPopupMenu getListPopup() {
		jPopupMenu = new JPopupMenu("Color");
		JMenuItem green = new JMenuItem("Green");
		green.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(null != setColorItem) {
					setColorItem.color = ListItem.COLOR_GREEN;
				}
			}
		});
		jPopupMenu.add(green);
		JMenuItem red = new JMenuItem("Red");
		red.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(null != setColorItem) {
					setColorItem.color = ListItem.COLOR_RED;
				}
			}
		});
		jPopupMenu.add(red);
		JMenuItem yellow = new JMenuItem("Yellow");
		yellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(null != setColorItem) {
					setColorItem.color = ListItem.COLOR_YELLOW;
				}
			}
		});
		jPopupMenu.add(yellow);
		return jPopupMenu;
	}

	private void runUpload() {
		Runnable uploadThread = new Runnable() {
			public void run() {
				ProgressMonitor pm = new ProgressMonitor(null, "Uploading text lines to board", "Uploading...", 0, jList.getModel().getSize());
				int i = 0;
				pm.setMillisToDecideToPopup(0);
				pm.setMillisToPopup(0);
				pm.setProgress(i);
				pm.setNote(i + " of " + jList.getModel().getSize());
				while(i < (jList.getModel().getSize())) {
					if(pm.isCanceled()) {
						break;
					}
					pm.setProgress(i);
					pm.setNote(i + " of " + jList.getModel().getSize());
					((RunningTextController) getController()).uploadToBoard(jList.getModel().getElementAt(i), i);
					try {
						synchronized (((RunningTextController)getController()).sendingSemaphore) {
							((RunningTextController)getController()).sendingSemaphore.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i++;
					System.out.println( i + " of " + jList.getModel().getSize());
				}
				((RunningTextController) getController()).onUploadFinished();
				pm.close();
			}
		};
		
		new Thread(uploadThread).start();
	}

	protected void initPort() {
		Runnable portScanThread = new Runnable() {
			public void run() {
				ProgressMonitor pm = new ProgressMonitor(null, "Waiting for LED board...", "Connecting...", 0, 10);
				pm.setMillisToDecideToPopup(0);
				pm.setMillisToPopup(0);
				int i = 0;
				pm.setProgress(i);
				
				while(!((RunningTextController)getController()).initPort()) {
					if(pm.isCanceled()) {
						break;
					}
					i++;
					if(i > 9) {
						i = 0;
					}
					pm.setProgress(i);
					pm.setNote("still not connected");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				pm.close();
			}
		};
		
		new Thread(portScanThread).start();
	}

	@Override
	protected JMenu getFileMenu() {
		JMenu fileMenu = super.getFileMenu();
		
		JMenuItem menuItem = new JMenuItem("Upload text to board");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				runUpload();
			}
		});
		fileMenu.add(menuItem);
		return fileMenu;
	}
	
	@Override
	protected JMenuBar getJJMenuBar() {
	if (jJMenuBar == null) {
		jJMenuBar = new JMenuBar();
		jJMenuBar.add(getFileMenu());
		jJMenuBar.add(getSpeedMenu());
		jJMenuBar.add(super.getHelpMenu());
	}
	return jJMenuBar;
	}
	
	private JMenu getSpeedMenu() {
		JMenu m = new JMenu();
		m.setText("Scolling");
		JMenuItem stopScrolling = new JMenuItem("Stop scrolling");
		stopScrolling.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().stopScroll();
			}
		});
		m.add(stopScrolling);
		JMenuItem startScrolling = new JMenuItem("Start scrolling");
		startScrolling.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().startScroll();
			}
		});
		m.add(startScrolling);
		JMenuItem speed1 = new JMenuItem("Too fast");
		speed1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().setSpeed(3);
			}
		});
		m.add(speed1);
		JMenuItem speed2 = new JMenuItem("Very fast");
		speed2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().setSpeed(6);
			}
		});
		m.add(speed2);

		JMenuItem speed22 = new JMenuItem("Fast");
		speed22.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().setSpeed(10);
			}
		});
		m.add(speed22 );

		JMenuItem speed23 = new JMenuItem("Faster");
		speed23.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().setSpeed(11);
			}
		});
		m.add(speed23);

		JMenuItem speed3 = new JMenuItem("Normal");
		speed3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().setSpeed(12);
			}
		});
		m.add(speed3);
		JMenuItem speed4 = new JMenuItem("Slower");
		speed4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().setSpeed(13);
			}
		});
		m.add(speed4);
		JMenuItem speed5 = new JMenuItem("Slow");
		speed5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().setSpeed(14);
			}
		});
		m.add(speed5);
		JMenuItem speed6 = new JMenuItem("Too Slow");
		speed6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				((RunningTextController)getController()).getBoardService().setSpeed(15);
			}
		});
		m.add(speed6);
		return m;
	}

	public Component getLine1Component() {
		return line1;
	}

	public Component getLine2Component() {
		return null;
	}

	@Override
	protected void showSettingsDialog() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addOutputControlls(JPanel jContentPane2) {
		jContentPane2.add(getLine1());
	}
	
	private void setupOutputComponent(JLabel component) {
		component.setPreferredSize(new Dimension(256, 16));
		component.setSize(new Dimension(256, 16));
	}
	
	JLabel getLine1() {
		if(null == line1){
			line1 = new JLabel(new ImageIcon(getTextOnImage("Bėganti eilutė", ListItem.COLOR_GREEN)));
			setupOutputComponent(line1);
			line1.setLocation(new Point(15, 15));
			line1.setFont(getOutputFont());
		}
		return line1;
	}
	
	private BufferedImage getTextOnImage(String text, int color) {
		BufferedImage line1image = new BufferedImage(256,16,BufferedImage.TYPE_INT_RGB);

		Graphics2D g = line1image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g.setFont(getOutputFont());
		if(ListItem.COLOR_RED == color) {
			g.setColor(Color.RED);
		}
		if(ListItem.COLOR_GREEN == color) {
			g.setColor(Color.GREEN);
		}
		if(ListItem.COLOR_YELLOW == color) {
			g.setColor(Color.YELLOW);
		}
		g.drawString(text.trim(), 1,13);
		return line1image;
	}

	private Font getOutputFont() {
		return new Font("Tahoma", Font.PLAIN, 13);
	}
	
	@Override
	public void setTextLines(final List<String> textLines) {
		Runnable t = new Runnable() {
			public void run() {
//				int i = jList.getSelectedIndex();
//				String[] lines = new String[textLines.size()];
//				int lineNum = 1;
//				for (int j = 0; j < textLines.size(); j++) {
//					if(!textLines.get(j).startsWith("---")) {
//						lines[j] = (lineNum++) + "| " + textLines.get(j);
//					} else {
//						System.out.println(textLines.get(j));
//						lines[j] = "<h1>"+ textLines.get(j).replace("---", "") +"</h1>";
//					}
//				}
				int i = jList.getSelectedIndex();
				ListItem[] lines = new ListItem[textLines.size()];
				int lineNum = 1;
				for (int j = 0; j < textLines.size(); j++) {
					lines[j] = new ListItem();
					if(!textLines.get(j).startsWith("---")) {
						lines[j].text = (lineNum++) + "| " + textLines.get(j);
					} else {
						System.out.println(textLines.get(j));
						lines[j].text = "<h1>"+ textLines.get(j).replace("---", "") +"</h1>";
					}
				}
				
				jList.setListData(lines);
				enableListComponent();
				jList.setSelectedIndex(i);
			}
		};
		SwingUtilities.invokeLater(t);
	}	
	public static void main(String[] args) {
		self = new RunningTextTitles(new RunningTextController());
		if(EventQueue.isDispatchThread()) {
			self.initialize();
			self.getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			self.getMainFrame().setVisible(true);
			self.initPort();
		} else {
			try {
				EventQueue.invokeAndWait(new Runnable() {
					public void run() {
						self.initialize();
						self.getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						self.getMainFrame().setVisible(true);
						self.getController().initialize();
						self.initPort();
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public void setCurrentLine(Object selectedValue) {
		setCurrentLine(selectedValue, ListItem.COLOR_GREEN);
	}
}

