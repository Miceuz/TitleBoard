package lt.okt.view;

import java.awt.Component;
import java.io.File;
import java.util.List;

public interface View {

	void setCurrentLine(Object selectedValue);

	Component getLine1Component();
	Component getLine2Component();

	void setTextLines(List<String> textLines);

	void disableListComponent(String progressBarText);

	void enableListComponent();

	void fileLoaded(File f);

	void onInitStart();

	void onInitFinish();
}
