package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import environment.Piece;
import game.Game;
import game.Player;
import game.Position;

public class BoardPanel extends JPanel {
	
	private static final int BOARDSIZE = 722;
    private Position startPosition;
    private GameWindow gameWindow;
    private Set<Position> selectionOptions = new HashSet<>();

    public BoardPanel(GameWindow gameWindow) {
    	
    	this.setGameWindow(gameWindow);
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new Mouse(this));
    }
    
	public GameWindow getGameWindow() {
		return gameWindow;
	}

	public void setGameWindow(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
	}

	public Set<Position> getSelectionOptions() {
		return selectionOptions;
	}

	public void setSelectionOptions(Set<Position> selectionOptions) {
		this.selectionOptions = selectionOptions;
	}

	public Dimension getPreferredSize() {
        return new Dimension(BOARDSIZE,BOARDSIZE);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // At creation of the BoardPandel, the Board hasn't been added to the game yet.
        // So we must avoid a NullPointerException.
        List<Piece> pieces = new ArrayList<>();
        if (getGameWindow().getGame().getBoard() != null)
        	pieces = getGameWindow().getGame().getBoard().getPieces();
        g.drawImage(new ImageIcon(this.getClass().getResource("/checkersBoard.gif")).getImage(),
        		0, 0, BOARDSIZE, BOARDSIZE, 0, 0, BOARDSIZE, BOARDSIZE, null);
        for (Piece piece : pieces) {
        	piece.paintImage(g);
        }
        Iterator<Position> selectionIter = this.getSelectionOptions().iterator();
        while (selectionIter.hasNext()) {
        	Position selectionOption = selectionIter.next();
        	int x = selectionOption.getPixelCoordinateX(BOARDSIZE);
        	int y = selectionOption.getPixelCoordinateY(BOARDSIZE);
        	g.drawImage(new ImageIcon(this.getClass().getResource("/selectedSquare.jpg")).getImage(),
        			x, y, x+BOARDSIZE/8, y+BOARDSIZE/8, 0, 0, BOARDSIZE/8, BOARDSIZE/8, null);
        }
    }
    
    public void addImage(Piece piece) {
    	final int CURR_X = piece.getPosition().getPixelCoordinateX(BOARDSIZE);
		final int CURR_Y = piece.getPosition().getPixelCoordinateY(BOARDSIZE);
		final int OFFSET = 1;
		repaint(CURR_X, CURR_Y, WIDTH+OFFSET, HEIGHT+OFFSET);
    }
    
    public void removeImageAt(Position position) {
//		final int CURR_X = position.getPixelCoordinateX(BOARDSIZE);
//		final int CURR_Y = position.getPixelCoordinateY(BOARDSIZE);
//		final int OFFSET = 1;
		repaint();
	}
    
	public void movePiece(Piece piece) {
		//TODO Try to repaint only the changed part of the screen.
//		int PREV_X = piece.getPreviousPosition().getPixelCoordinateX(BOARDSIZE);
//		int PREV_Y = piece.getPreviousPosition().getPixelCoordinateY(BOARDSIZE);
//    	int CURR_X = piece.getPosition().getPixelCoordinateX(BOARDSIZE);
//		int CURR_Y = piece.getPosition().getPixelCoordinateY(BOARDSIZE);
//		System.out.println("Prev_x: "+ PREV_X+ ", Prev_y: "+ PREV_Y);
//		System.out.println("Curr_x: "+ CURR_X+ ", Curr_y: "+ CURR_Y);
//		final int OFFSET = 1;
//		repaint(PREV_X, PREV_Y,WIDTH+OFFSET,HEIGHT+OFFSET);
//		repaint(CURR_X, CURR_Y,WIDTH+OFFSET,HEIGHT+OFFSET);
		repaint();
    }
	
	public void showSelectOptions(Piece piece) {
		this.setSelectionOptions(piece.getPossibleSelections());
		repaint();
	}
	
	public void removeSelectOptions() {
		this.getSelectionOptions().clear();
		repaint();
	}


	public Piece getPieceAtPosition(Position position) {
		return gameWindow.getGame().getBoard().getPieceAt(position);
	}

	public void addMove(Piece selectedPiece, Position selectedPosition) {
		Player player = gameWindow.getPlayer();
		Game game = gameWindow.getGame();
		game.addMove(selectedPiece, selectedPosition, player);
	}
    
   
	
}
