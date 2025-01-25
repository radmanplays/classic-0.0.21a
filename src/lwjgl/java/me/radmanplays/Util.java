package me.radmanplays;

import java.awt.Component;

import javax.swing.JOptionPane;

public class Util {
	public static void alert(String message, String title) {
		JOptionPane.showMessageDialog((Component)null, message, title, 0);
	}
}
