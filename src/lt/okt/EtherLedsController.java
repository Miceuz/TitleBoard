package lt.okt;

import lt.okt.service.EtherLedsService;
import lt.okt.view.SenderThreadListener;

public class EtherLedsController extends BitmapController implements SenderThreadListener{

	private EtherLedsService boardService;
	public EtherLedsController() {
		super();
		setModel(new EtherLedsModel());
		setBoardService(new EtherLedsService((EtherLedsModel)getModel()));
		boardService.setSenderThreadListener(this);
	}

	@Override
	protected void sendTextToBoard() {
		int[][] line1pixels = pixelDataDecoder.decode(view.getLine1Component());
		int[][] line2pixels = pixelDataDecoder.decode(view.getLine2Component());
		getBoardService().sendBitmapToBoard(line1pixels, line2pixels);
		
	}

	public void blind() {
		super.blind();
		boardService.blind();
	}

	public void unBlind() {
		super.unBlind();
		sendTextToBoard();
	}

	public void setBoardService(EtherLedsService boardService) {
		this.boardService = boardService;
	}

	public EtherLedsService getBoardService() {
		return boardService;
	}
	
	@Override
	public void initialize() {
		view.onInitStart();
		if(boardService.connect()){
			view.onInitFinish();
		}
	}
}
