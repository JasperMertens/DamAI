package gui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;

import environment.Piece;
import game.Game;
import game.PieceColor;
import game.Player;
import game.Position;

public class GameWindow {

	private static final int BOARDSIZE = 722;
	BoardPanel boardPanel;
	private Game game;
	private Player player;
	private PieceColor pieceColor;

	public GameWindow(Game game, Player player, PieceColor pieceColor) {
		this.setGame(game);
		this.setPlayer(player);
		this.setPieceColor(pieceColor);
		createGUI();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PieceColor getPieceColor() {
		return pieceColor;
	}

	public void setPieceColor(PieceColor pieceColor) {
		this.pieceColor = pieceColor;
	}

	private void createGUI() {
		JFrame ourFrame = new JFrame("Game"+getPieceColor());
		boardPanel = new BoardPanel(this);
		ourFrame.add(boardPanel);
		ourFrame.setResizable(false);
		ourFrame.setVisible(true);
		ourFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Call ourFrame.pack & ourFrame.validate methods to reassure us that all GUI components are in the correct locations
		//with correct positions and heights
		ourFrame.pack();
//		ourFrame.setLocationRelativeTo(null);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int extra = (getPieceColor()==PieceColor.WHITE) ? ourFrame.getWidth()+20: 10;
        int x = (int) rect.getMaxX() - ourFrame.getWidth() - extra;
        int y = (int) (rect.getMaxY() - ourFrame.getHeight())/2;
        ourFrame.setLocation(x, y);
	}
	
	public void addImage(Piece piece) {
		boardPanel.addImage(piece);
	}

	public void removeImageAt(Position position) {
		boardPanel.removeImageAt(position);
	}

	public void movePiece(Piece piece) {
		boardPanel.movePiece(piece);
		
	}

}
