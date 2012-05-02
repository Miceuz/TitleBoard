package lt.okt;

import lt.okt.view.FullscreenWindowApp;

public class ProjectorController extends Controller {

	private FullscreenWindowApp projectorScreen;

	private String currentLine = "";
		
	public ProjectorController() {
		setModel(new Model());
		getModel().setMaxChars(60);
	}
	
	@Override
	protected void sendTextToOutputDevice(String text) {
		currentLine = text;
		if(!model.isBlinded()) {
			projectorScreen.setText(text.replace("\n", "<br/>").replaceAll("\\d+\\| ", ""));
		}
	}
	
	@Override
	public void blind() {
		super.blind();
		projectorScreen.setText("");
	}
	
	@Override
	public void unBlind() {
		super.unBlind();
		sendTextToOutputDevice(currentLine);
	}

	public void setProjectorScreen(FullscreenWindowApp projectorScreen) {
		this.projectorScreen = projectorScreen;
	}
}
