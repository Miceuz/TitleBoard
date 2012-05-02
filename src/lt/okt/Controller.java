package lt.okt;

import java.awt.Component;
import java.io.File;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import lt.okt.service.TextLoaderListener;
import lt.okt.service.TextLoaderThread;
import lt.okt.view.View;

public abstract class Controller implements TextLoaderListener{

	protected View view;
	protected Model model;
	
	public Controller() {
		super();
	}

	public void setView(View view) {
		this.view = view;
	}

	public void lineChanged(Object selectedValue) {
		if(null == selectedValue || ((String)selectedValue).startsWith("<h1>")) {
			return;
		}
		view.setCurrentLine(((String)selectedValue).replaceAll("\\d+\\| ", ""));
		if(!model.isBlinded()) {
			sendTextToOutputDevice((String) selectedValue);
		}
	}

	protected abstract void sendTextToOutputDevice(String selectedValue);

	public void loadText(File f) {
		model.setCurrentFile(f);
		view.disableListComponent("Loading file...");
		TextLoaderThread textLoaderThread = new TextLoaderThread(this, model, view.getLine1Component(), f);
		textLoaderThread.start();
	}

	public void blind() {
		model.setBlinded(true);
	}

	public void unBlind() {
		model.setBlinded(false);
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Model getModel() {
		return model;
	}

	public void reloadText() {
		if(null != model.getCurrentFile()) {
			loadText(model.getCurrentFile());
		}
	}

	public void onTextLoaded(List<String> textLines) {
		view.fileLoaded(model.getCurrentFile());
		view.setTextLines(textLines);
	}

	protected String getTextFromComponent(Component component) {
		try {
			return ((JTextComponent) component).getDocument().getText(0, ((JTextComponent) component).getDocument().getLength()).trim();
		} catch (BadLocationException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void initialize() {
		
	}

}