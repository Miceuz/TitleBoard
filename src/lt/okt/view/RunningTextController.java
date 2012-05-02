package lt.okt.view;

import lt.okt.BitmapController;
import lt.okt.LedBoardModel;
import lt.okt.RunningTextTitles;
import lt.okt.service.RunningTextService;
import lt.okt.ListItem;

public class RunningTextController extends BitmapController {

	private RunningTextService boardService;
	public Object sendingSemaphore = new Object();
	
	public RunningTextController() {
		setModel(new LedBoardModel());
		this.boardService = new RunningTextService();
		boardService.setSenderThreadListener(this);
	}
	
	@Override
	protected void sendTextToBoard() {
		sendTextToBoard(-1, ListItem.COLOR_GREEN);
	}
	
	private void sendTextToBoard(int index, int color) {
		int[][] linePixels = pixelDataDecoder.decode(view.getLine1Component());
		getBoardService().sendBitmapToBoard(linePixels, color, index);		
	}

	public void setBoardService(RunningTextService boardService) {
		this.boardService = boardService;
	}

	public RunningTextService getBoardService() {
		return boardService;
	}

	public void uploadToBoard(Object selectedValue, int index) {
		ListItem li = (ListItem) selectedValue;
		
		if(null == li || li.text.startsWith("<h1>")) {
			return;
		}
		((RunningTextTitles)view).setCurrentLine(li.text.replaceAll("\\d+\\| ", ""), li.color);
		if(!model.isBlinded()) {
			view.disableListComponent("Sending text to board bank " + index + "...");
			sendTextToBoard(index, li.color);
		}
	}
	
	public void lineChanged(Object selectedValue) {
		ListItem li = (ListItem) selectedValue;

		if(null == li || li.text.startsWith("<h1>")) {
			return;
		}
		((RunningTextTitles)view).setCurrentLine(li.text.replaceAll("\\d+\\| ", ""), li.color);
		if(!model.isBlinded()) {
			sendTextToBoard(-1, li.color);
		}
	}
	
	public void onSendFinish() {
		super.onSendFinish();
		synchronized (sendingSemaphore) {
			sendingSemaphore.notify();
		}
	}

	public void onUploadFinished() {
		boardService.startScroll();
	}

	public boolean initPort() {
		return boardService.initPort();
	}
	
	@Override
	public void blind() {
		super.blind();
		boardService.blind();
	}
	
	@Override
	public void unBlind() {
		boardService.unblind();
		super.unBlind();
	}
}
