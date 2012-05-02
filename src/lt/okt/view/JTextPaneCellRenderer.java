package lt.okt.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class JTextPaneCellRenderer extends JTextPane implements ListCellRenderer {
	private static final long serialVersionUID = 1039763479919968767L;
	private StyleSheet regularStyleSheet;
	private StyleSheet selectedStyleSheet;

	public JTextPaneCellRenderer() {
		HTMLEditorKit kit = new HTMLEditorKit();
		regularStyleSheet = new StyleSheet();
		regularStyleSheet.addRule("body {font-family: Tahoma;font-size:10px;color:black;}" );
		regularStyleSheet.addRule("h1 {font-family: Tahoma;font-size:14px;color:black;text-align:center;}" );
		selectedStyleSheet = new StyleSheet();
		selectedStyleSheet.addRule("body {font-family: Tahoma;font-size:10px;color:white}" );
		selectedStyleSheet.addRule("h1 {font-family: Tahoma;font-size:14px;color:white;text-align:center;}" );

		kit.setStyleSheet(regularStyleSheet);
		setEditorKit(kit);
		setEditable(true);
		Border border = new BottomBorder();
		setBorder(border);
	}
	
	public Component getListCellRendererComponent(
       JList list,              // the list
       Object value,            // value to display
       int index,               // cell index
       boolean isSelected,      // is the cell selected
       boolean cellHasFocus)    // does the cell have focus
     {
         String s = value.toString().replace("\n", "<br>");
         setText(s);
/*         String[] ss = s.split("\n");
         if(ss.length > 1) {
        	 setText(new StringBuffer(ss[0]).append("<br/>").append(ss[1]).toString());
         } else {
        	 setText(s);
         }
*/
         if (isSelected) {
             setBackground(list.getSelectionBackground());
             changeStyleSheet(selectedStyleSheet);
         } else {
             setBackground(list.getBackground());
             changeStyleSheet(regularStyleSheet);
         }
         setEnabled(list.isEnabled());
         //setFont(list.getFont());
         setOpaque(true);
         return this;
     }

	private void changeStyleSheet(StyleSheet s) {
		//String text = getText();
		//HTMLEditorKit kit = (HTMLEditorKit) getEditorKit();
		//kit.setStyleSheet(s);
		//setEditorKit(kit);
		//setText(text);
	}
 }

class BottomBorder extends AbstractBorder {
	private static final long serialVersionUID = 1445750561415888799L;
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.setColor(Color.lightGray);
		g.drawLine(x+1, y+height-1, x+width-1, y+height-1);
    }	
}