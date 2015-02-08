import java.awt.Color;

public class Entry {
	private String content;
	private String level;
	private Color color;
	
	private boolean isType(String type, String str) {
		return str.startsWith("[" + type.toUpperCase() + "]");
	}
	
	Entry (String message) {
		//Analyze to find color
		if (isType("error", message)) {
			level = "error";
			color = new Color(255, 70, 70);
		} else if (isType("info", message)) {
			level = "info";
			color = Color.GREEN;
		} else if (isType("warning", message)) {
			level = "warning";
			color = Color.YELLOW;
		} else {
			level = "log";
			color = Color.WHITE;
		}
		
		// store
		content = message;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getLevel() {
		return level;
	}

	public Color getColor() {
		return color;
	}
}
