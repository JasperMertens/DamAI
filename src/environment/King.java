package environment;

import javax.swing.ImageIcon;

import game.PieceColor;
import game.Position;

public class King extends Piece {

	public King(Position position, Board board, PieceColor color) {
		super(position, board, color);
		String path = (color==PieceColor.WHITE) ? "/whiteCircleKing.png" : "/blackCircleKing.png";
		image = new ImageIcon(this.getClass().getResource(path)).getImage();
	}
	
	@Override
	public Piece copyToBoard(Board newBoard) {
		return new King(this.position,newBoard, this.color);
	}


	@Override
	public boolean isPathObstructed(Position startPosition, Position endPosition) {
		int distance = startPosition.diagonalDistance(endPosition);
		for (int i=1; i<distance; i++) {
			Position intermediatePosition = startPosition.getDiagExtended(endPosition, i);
			if (board.isOccupied(intermediatePosition))
				return true;
		}
		return false;
	}

	@Override
	public boolean isCorrectDirection(Position startPosition, Position targetPosition) {
		return true;
	}

	//TODO Change to adjustable allowed distances in the rules
	@Override
	public boolean isCorrectSimpleMoveDistance(int distance) {
		//		return true;
		return distance == 1;
	}

	@Override
	public boolean isCorrectJumpDistance(int distance) {
		//		return true;
		return distance == 2;
	}

	@Override
	public boolean isPromoting(Position targetPosition) {
		return false;
	}

	/*
	 * This has to have a different implementation from NormalPiece if a King can move
	 * multiple tiles, or when you can choose these rules at the start.
	 */
	//	@Override
	//	public Set<Position> getPossibleSelections() {
	//		Set<Position> jumpSelections = getJumpSelectionsAt(getPosition());
	//		if (jumpSelections.isEmpty())
	//			return getSimpleMoveSelections();
	//		return jumpSelections;
	//	}
	//	
	//	private Set<Position> getSimpleMoveSelections() {
	//		Set<Position> result = new HashSet<>();
	//		Set<Position> diagonalNeighbours = getPosition().getDiagonalNeigbours(1,1);
	//		for (Position targetPosition : diagonalNeighbours) {
	//			try {
	//				MoveFactory.createSimpleMove(this, getPosition(), targetPosition, true);
	//				result.add(targetPosition);
	//			} catch (IllegalArgumentException e) {}
	//		}
	//		return result;
	//	}
	//
	//	public Set<Position> getJumpSelectionsAt(Position position) {
	//		Set<Position> result = new HashSet<>();
	//		Set<Position> diagonalNeighbours = position.getDiagonalNeigbours(2,2);
	//		for (Position targetPosition : diagonalNeighbours) {
	//			try {
	//				MoveFactory.createJump(this, position, targetPosition, true);
	//				result.add(targetPosition);
	//			} catch (IllegalArgumentException e) {}
	//		}
	//		return result;
	//	}
}
