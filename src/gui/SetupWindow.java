package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import environment.Board;
import environment.Timer;
import game.Game;
import game.Player;

public class SetupWindow {

	private JButton goButton;

	public SetupWindow() {
		createGUI();
	}
	
	private void createGUI() {
		JFrame ourFrame = new JFrame("Setup");
		JPanel mainPanel = (JPanel)ourFrame.getContentPane();
		goButton = new JButton("Go!");
		mainPanel.add(goButton);
		goButton.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						go(8, 10, 0);
						ourFrame.setVisible(false);
						ourFrame.dispose();
					}
				}
			);
		goButton.setPreferredSize(new Dimension(150, 25));
		ourFrame.setResizable(false);
		ourFrame.setVisible(true);
		ourFrame.setLocationRelativeTo(null);
		ourFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Call ourFrame.pack & ourFrame.validate methods to reassure us that all GUI components are in the correct locations
		//with correct positions and heights
		ourFrame.pack();
		ourFrame.validate();
	}
	
	private void go(int boardSize, int minutes, int seconds) {
		Player player1 = new Player();
		Player player2 = new Player();
		Timer timer = new Timer(minutes, seconds);
		Board board = new Board(player1, player2, boardSize, timer);
		new Game(board, player1, player2);
	}
}
