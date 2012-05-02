package lt.okt.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

public class WordLoader {

	public List<String> loadText(File f) {
		if(null == f) {
			String[] test = new String[] {"Mano batai\nratai kvadratai", "Buvo du\nggggghh hhhhggh ggggghhg yyyyyyt", "Vienas <b>dingo</b>\nasdfghj dfghjdfgh ertyuierty sdfghjdfgh", "Nerandu\nwertyuert ertyu wertyuiert rtyugg", "Aš su <i>vienu</i>\n asdfghjasdfgh sdfghjsdfgh", "Batuku\nsdfghj dfghjsdfgh wertyuwert", "Niekur eiti\nzxcvbzxcv sdfghjsdfg wertwert", "Negaliu\nertyuirtyu dfghjdfg zxcvxcv", "Mano batai\nwertyui ertyuierty zxcvbnm", "Buvo du\ndfghdf sdfgdfg werwert", "Vienas dingo", "Nerandu", "Aš su vienu", "Batuku", "Niekur eiti", "Negaliu"};
			return Arrays.asList(test);
		}
		List<String> ret = new ArrayList<String>();
		try {
			HWPFDocument document = new HWPFDocument(new FileInputStream(f));
			Range mainRange = document.getRange();
			int paragraphCount = mainRange.numParagraphs();
			for(int i = 0; i < paragraphCount; i++) {
				String s = "";
				Paragraph p = mainRange.getParagraph(i);
				int runs = p.numCharacterRuns();
				for(int j = 0; j < runs; j++) {
					CharacterRun run = p.getCharacterRun(j);
					if(run.isBold()) {
						s+= "<b>";
					}
					if(run.isItalic()) {
						s+= "<i>";
					}
					s += run.text().replace("<", "&lt;").replace(">", "&gt;");
					if(run.isItalic()) {
						s+= "</i>";
					}
					if(run.isBold()) {
						s+= "</b>";
					}
				}
				ret.add(s);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}

}
