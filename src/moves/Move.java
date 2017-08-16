package moves;
import environment.Board;
import environment.Piece;
import game.Position;

public abstract class Move {

	protected Piece pieceToMove;
	protected Position startPosition;
	protected Position targetPosition;
	
	public Move(Piece pieceToMove,Position startPosition, Position targetPosition) throws IllegalArgumentException {
		if (!isLegal(pieceToMove, pieceToMove.getPosition(), targetPosition))
			throw new IllegalArgumentException();
		this.pieceToMove = pieceToMove;
		this.startPosition = startPosition;
		this.targetPosition = targetPosition;
	}
	
	public static boolean isLegal(Piece pieceToMove, Position startPosition, Position targetPosition) {
		Board board = pieceToMove.getBoard();
		boolean withinBoundaries = board.isWithinBoundaries(targetPosition);
		boolean diagonal = startPosition.isDiagonal(targetPosition);
		boolean correctDirection = pieceToMove.isCorrectDirection(startPosition, targetPosition);
//		System.out.println("correctDirection: "+ correctDirection);
		return withinBoundaries && diagonal && correctDirection;
	}

	public boolean isLegalNow() {
		return !getBoard().isOccupied(targetPosition);
	}
	
	public Piece getPieceToMove() {
		return pieceToMove;
	}
	
	public Position getStartPosition() {
		return startPosition;
	}
	
	public Position getTargetPosition() {
		return targetPosition;
	}
	
	public Board getBoard() {
		return pieceToMove.getBoard();
	}
	
	public int getDiagonalDistance() {
		return getStartPosition().diagonalDistance(targetPosition);
	}
	
	public abstract void execute() throws IllegalStateException;

	public abstract boolean hasFollowUp();

}


