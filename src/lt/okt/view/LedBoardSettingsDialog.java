package lt.okt.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import lt.okt.Controller;

public class LedBoardSettingsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel ledBoardPanel = null;
	private JLabel jLabel2 = null;
	private JComboBox jComboBox2 = null;
	private JLabel jLabel3 = null;
	private JSpinner maxCharsTextField = null;
	private Controller controller;  //  @jve:decl-index=0:
	private JButton jButton = null;
	private LedBoardSettingsDialog dialog;  //  @jve:decl-index=0:visual-constraint="574,25"
	/**
	 * @param owner
	 */
	public LedBoardSettingsDialog(Frame owner, Controller c) {
		super(owner,true);
		setController(c);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(112, 156);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (screen.width - 406) / 2;
	    int y = (screen.height - 249) / 2;
	    setBounds(x, y, 406, 249);
	    this.dialog = this;
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = GridBagConstraints.EAST;
			gridBagConstraints9.gridy = 3;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.gridwidth = 2;
			gridBagConstraints8.gridheight = 1;
			gridBagConstraints8.ipady = 44;
			gridBagConstraints8.ipadx = 73;
			gridBagConstraints8.gridx = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.anchor = GridBagConstraints.EAST;
			gridBagConstraints7.insets = new Insets(0, 0, 10, 0);
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.insets = new Insets(0, 0, 10, 0);
			gridBagConstraints6.gridx = 1;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getLedBoardPanel(), gridBagConstraints8);
			jContentPane.add(getJButton(), gridBagConstraints9);
		}
		return jContentPane;
	}

	/**
	 * This method initializes ledBoardPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLedBoardPanel() {
		if (ledBoardPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = -1;
			gridBagConstraints11.gridy = -1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints5.ipadx = 23;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.anchor = GridBagConstraints.EAST;
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.anchor = GridBagConstraints.EAST;
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridwidth = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 2;
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.ipadx = 70;
			gridBagConstraints1.gridwidth = 1;
			gridBagConstraints1.weightx = 0.0;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridx = 1;
			jLabel3 = new JLabel();
			jLabel3.setText("Max chars in line:");
			jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel3.setName("jLabel3");
			jLabel3.setVisible(false);
			ledBoardPanel = new JPanel();
			ledBoardPanel.setLayout(new GridBagLayout());
			ledBoardPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			ledBoardPanel.setName("jPanel");
			ledBoardPanel.setVisible(true);
			ledBoardPanel.add(jLabel3, gridBagConstraints4);
			ledBoardPanel.add(getJTextField(), gridBagConstraints5);
		}
		return ledBoardPanel;
	}
	
	private void displayMaxCharsField() {
		jLabel3.setVisible(true);
		maxCharsTextField.setVisible(true);
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JSpinner getJTextField() {
		if (maxCharsTextField == null) {
			maxCharsTextField = new JSpinner(new SpinnerNumberModel(controller.getModel().getMaxChars(), 10, 100, 1));
			maxCharsTextField.setName("jTextField");
			//maxCharsTextField.setVisible(false);
			displayMaxCharsField();
			maxCharsTextField.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					controller.getModel().setMaxChars((Integer) maxCharsTextField.getValue());
				}
			});
		}
		return maxCharsTextField;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Controller getController() {
		return controller;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("OK");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//((AppViewBase)getOwner()).settingsUpdated();
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
		}
		return jButton;
	}
}  //  @jve:decl-index=0:visual-constraint="72,10"
