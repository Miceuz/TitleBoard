package lt.okt;

public class ListItem {
	public static int COLOR_RED = 1;
	public static int COLOR_GREEN = 2;
	public static int COLOR_YELLOW = 3;
	
	public int color = COLOR_GREEN;
	public String text;
	@Override
	public String toString() {
		return text;
	}
}