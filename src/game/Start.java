package game;

import gui.SetupWindow;

public class Start {

	public Start() {
		javax.swing.SwingUtilities.invokeLater
		(
				new Runnable(){
					@Override
					public void run(){
						new SetupWindow();
					}
				}
		);
	}

	public static void main(String[] args) {
		new Start();
	}
}
