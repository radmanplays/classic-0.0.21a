package me.radmanplays;

public class Util {
	public static void alert(String message, String title) {
		JOptionPane.showMessageDialog((Component)null, message, title, 0);
	}
    @JSBody(params = { "message", "title" }, script = "alert(title + '\n\n' + message)")
    public static native void alert(String message, String title);
}
