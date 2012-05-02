package lt.okt;

import java.io.File;

public class Model {

	private boolean blinded = false;
	private File currentFile;
	private int maxChars = 48;

	public Model() {
		super();
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setBlinded(boolean blinded) {
		this.blinded = blinded;
	}

	public boolean isBlinded() {
		return blinded;
	}

	public void setCurrentFile(File f) {
		this.currentFile = f;
	}

	public int getMaxChars() {
		return maxChars ;
	}

	public void setMaxChars(int ledMaxChars) {
		this.maxChars = ledMaxChars;
	}

}