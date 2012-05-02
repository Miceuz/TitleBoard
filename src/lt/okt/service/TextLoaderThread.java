package lt.okt.service;

import java.awt.Component;
import java.io.File;
import java.util.List;

import lt.okt.LedBoardModel;
import lt.okt.Model;

public class TextLoaderThread extends Thread {

		private WordLoader textLoader;
		private File file;
		private Model model;
		private TextLoaderListener listener;
		private LineSplitter splitter;
		private Component lineComponent;
		public TextLoaderThread(TextLoaderListener listener, Model model, Component lineComponent, File f) {
			super();
			this.textLoader = new WordLoader();
			this.file = f;
			this.model = model;
			this.listener = listener;
			this.splitter = new LineSplitter();
			this.lineComponent = lineComponent;
		}
		@Override
		public void run() {
			List<String> textLines = null;
			if(model instanceof LedBoardModel) {
				if(LedBoardModel.LED_PROTOCOL_ASCII == ((LedBoardModel) model).getLedProtocol()) {
					textLines = splitter.split(model.getMaxChars(), textLoader.loadText(file));
				} else if(LedBoardModel.LED_PROTOCOL_BITMAP == ((LedBoardModel) model).getLedProtocol()) {
					textLines = splitter.split(lineComponent, textLoader.loadText(file));
				}
			} else {
				textLines = splitter.split(model.getMaxChars(), textLoader.loadText(file));
			}
			listener.onTextLoaded(textLines);
		}
	}