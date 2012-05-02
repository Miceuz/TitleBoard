package lt.okt.service;

import java.awt.Component;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

public class LineSplitter {

	
	public List<String> split(Component lineComponent, List<String> text) {
		LineFitter lineFitter = new FontMetricsLineFitter(lineComponent.getFontMetrics(lineComponent.getFont()), lineComponent.getWidth());
		return _split(text, lineFitter, false);
	}
	
	public List<String> split(Component lineComponent, List<String> text, boolean twoLine) {
		LineFitter lineFitter = new FontMetricsLineFitter(lineComponent.getFontMetrics(lineComponent.getFont()), lineComponent.getWidth());
		return _split(text, lineFitter, twoLine);
	}

	private List<String> _split(List<String> text, LineFitter lineFitter, boolean twoLine) {
		List<String> ret = new ArrayList<String>();
		boolean lastWasEmpty = false;
		boolean lastWasShort = false;
		for (String paragraph : text) {
			if(lineFitter.fitsToLine(paragraph)) {
				if("".equals(paragraph.trim())) {
					if(lastWasEmpty) {
						ret.add("");//nekartojam keliu tusciu eiluciu is eiles
						lastWasEmpty = false;
						lastWasShort = false;
					} else {
						lastWasEmpty = true;
						lastWasShort = false;						
					}
				} else {
					lastWasEmpty = false;
					if(lastWasShort) {
						if(twoLine) {
							String lastLine = ret.remove(ret.size()-1);
							ret.add(lastLine + "\n" + paragraph); //jeigu vieno po kitos eina dvi trumpos eilutes, tai dedam jas i viena ekrana
						} else {
							ret.add(paragraph.trim());
						}
						lastWasShort = false;
					} else {
						ret.add(paragraph);
						lastWasShort = true;
					}
				}
			} else {
				List<String> splitParts = splitPart(paragraph, lineFitter);
				int i;
				for(i = 0; i < splitParts.size(); i+=2) {
					if(twoLine) {
						ret.add(splitParts.get(i) + ((i+1) < splitParts.size()?("\n"+splitParts.get(i+1)):"\n"));
					} else {
						ret.add(splitParts.get(i));
						if((i+1) < splitParts.size()) {
							ret.add(splitParts.get(i+1));
						}
					}
				}
				if(i < splitParts.size()) {
					ret.add(splitParts.get(i));
				}
				if(i+1 < splitParts.size()) {
					ret.add(splitParts.get(i+1));
				}
				lastWasEmpty = false;
				lastWasShort = false;
			}
		}
		
		
		return ret;
	}
/*
	private Collection<? extends String> splitParagraph(String paragraph, LineFitter lineFitter) {
		List<String> ret = new ArrayList<String>();
		String[] sentences = paragraph.split("\\.");
		//TODO apeiti daugtaski ...
		for (String sentence : sentences) {
			if(lineFitter.fitsToLine(sentence)) {
				ret.add(sentence+".");
			} else {
				ret.addAll(splitSentence(sentence, lineFitter));
			}
		}
		return ret;
	}

	private Collection<? extends String> splitSentence(String sentence, LineFitter lineFitter) {
		List<String> ret = new ArrayList<String>();
		String[] parts = sentence.split(",");
		for (String part : parts) {
			if(lineFitter.fitsToLine(part)) {
				ret.add(part+",");
			} else {
				List<String> splitParts = splitPart(part, lineFitter);
				int i;
				for(i = 0; i+2 < splitParts.size(); i+=2) {
					ret.add(splitParts.get(i) + ((i+1) < splitParts.size()?("\n"+splitParts.get(i+1)):"\n"));
				}
				if((i+1) < splitParts.size()) {
					ret.add(splitParts.get(i+1));
				}
			}
		}
		return ret;
	}
*/
	private List<String> splitPart(String part, LineFitter lineFitter) {
		List<String> ret = new ArrayList<String>();
		String[] words = part.split(" ");
		int i =0;
		while(i < words.length) {
			StringBuffer line = new StringBuffer("");
			while(i < words.length && lineFitter.fitsToLine(line + words[i])) {
				line.append(words[i]).append(" ");
				i++;
			}
			ret.add(line.toString());
		}
		return ret;
	}

	public List<String> split(int maxLength, List<String> text) {
		return _split(text, new MaxCharsLineFitter(maxLength), true) ;
	}

	interface LineFitter {
		public boolean fitsToLine(String s);
	}
	class FontMetricsLineFitter implements LineFitter{
		private FontMetrics fontMetrics;
		int maxWidthPixels = 0;//384;

		public FontMetricsLineFitter(FontMetrics m, int maxWidth) {
			this.fontMetrics = m;
			this.maxWidthPixels = maxWidth;
		}

		public boolean fitsToLine(String s) {
//			System.out.println("WIDTH OF " + s + " is " + fontMetrics.stringWidth(s) + " vs " + maxWidthPixels);
			return fontMetrics.stringWidth(s)  <= maxWidthPixels;
		}
	}
	class MaxCharsLineFitter implements LineFitter {
		private int maxChars;

		public MaxCharsLineFitter(int maxChars) {
			this.maxChars = maxChars;
		}

		public boolean fitsToLine(String s) {
			return s.length() <= maxChars;
		}
	}
}
