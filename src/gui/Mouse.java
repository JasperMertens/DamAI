package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import environment.Board;
import environment.Piece;
import game.Player;
import game.Position;

public class Mouse extends MouseAdapter {
	
	public static enum State {
		START, PIECE_SELECTED;
	}
	
	private static final int BOARDSIZE = 722;
	private BoardPanel boardPanel;
	private State state = State.START;
	private Piece selectedPiece;
	private List<Position> moveSelections = new LinkedList<>();

	public Mouse(BoardPanel boardPanel) {
		this.boardPanel = boardPanel;
	}
	
	private void selectPiece(Piece selectedPiece) {
		this.selectedPiece = selectedPiece;
	}
	
	private void deselectPiece() {
		this.selectedPiece = null;
	}
	
	private void returnToStart() {
		System.out.println("return to start");
		deselectPiece();
		moveSelections.clear();
		state = State.START;
	}

	public void mousePressed(MouseEvent e) {
    	int index = Position.pixelCoordinatesToIndex(e.getX(), e.getY(), BOARDSIZE);
    	Position selectedPosition = new Position(index);
    	switch (state) {
    	
    	case START: 
    		if (isValidPieceSelection(selectedPosition)) {
    			selectPiece(boardPanel.getPieceAtPosition(selectedPosition));
    			boardPanel.showSelectOptions(selectedPiece);
    			state = State.PIECE_SELECTED;
    			System.out.println("piece selected: " + selectedPiece.getPosition().getIndex());
    		}
    		break;
    		
    	case PIECE_SELECTED:
    		if (isValidMoveSelection(selectedPosition)) {
    			if (isPremove()) {
    				moveSelections.add(selectedPosition);
    			}
    			else {
    				boardPanel.addMove(selectedPiece, selectedPosition);
    				boardPanel.removeSelectOptions();
    			}
    		} else if (isValidPieceSelection(selectedPosition)) {
    			boardPanel.removeSelectOptions();
    			selectPiece(boardPanel.getPieceAtPosition(selectedPosition));
    			boardPanel.showSelectOptions(selectedPiece);
    			System.out.println("piece selected: " + selectedPiece.getPosition().getIndex());
    		} else {
    			boardPanel.removeSelectOptions();
    			returnToStart();
    		}
    		break;
    	}
//    	if (index==0)
//    		positionSelected = false;
//    	else {
//    		
//
//    		
//
//    		if (positionSelected) {
//    			System.out.println("Something already selected, now selected: "+ index);
//    			game.addPossibleMove(player, startPosition, selectedPosition);
//    			positionSelected = false;
//    		}
//    		else if (game.legalFirstMoveElement(selectedPosition, player)) {
//    			System.out.println("Nothing selected before, now piece selected: "+ index);
//    			startPosition = selectedPosition;
//    			positionSelected = true;
//    		}
//    		else
//    			System.out.println("Nothing selected before, nothing now, "+ index);
//    	}
    }

	private boolean isPremove() {
		return false;
//		Player player = this.boardPanel.getGameWindow().getPlayer();
//		return !this.boardPanel.getGameWindow().getGame().isActivePlayer(player);
	}

	private boolean selectedPieceHasNextMove(Position startingPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isValidMoveSelection(Position selectedPosition) {
		return boardPanel.getSelectionOptions().contains(selectedPosition);
	}

	private boolean isValidPieceSelection(Position selectedPosition) {
		Board board = boardPanel.getGameWindow().getGame().getBoard();
		if (!board.isOccupied(selectedPosition))
			return false;
		Piece piece = board.getPieceAt(selectedPosition);
		Player player = boardPanel.getGameWindow().getPlayer();
		if (board.getPieceColor(player) == piece.getColor())
			return true;
		return false;
					
	}

}
