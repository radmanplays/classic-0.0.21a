package me.radmanplays;

import org.teavm.jso.JSBody;

public class Util {
    @JSBody(params = { "message", "title" }, script = "alert(title + '\n\n' + message)")
    public static native void alert(String message, String title);
}
