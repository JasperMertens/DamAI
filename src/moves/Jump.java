package moves;
import environment.Board;
import environment.Piece;
import game.Position;

public class Jump extends Move {

	public Jump(Piece pieceToMove, Position startPosition, Position targetPosition) {
		super(pieceToMove, startPosition, targetPosition);
		if (!isLegal(pieceToMove, startPosition, targetPosition))
			throw new IllegalArgumentException();
	}

	@Override
	public void execute() throws IllegalStateException {
		if (!isLegalNow())
			throw new IllegalStateException();
		getBoard().movePiece(pieceToMove, targetPosition);
		Position removePosition = targetPosition.getDiagExtended(startPosition, 1);
		getBoard().removePieceAt(removePosition);
	}
	
	public static boolean isLegal(Piece pieceToMove, Position startPosition, Position targetPosition) {
		boolean correctDistance = pieceToMove.isCorrectJumpDistance(startPosition.diagonalDistance(targetPosition));
		return Move.isLegal(pieceToMove, startPosition, targetPosition) && correctDistance;
	}
	
	public boolean isLegalNow() {
		Position position = targetPosition.getDiagExtended(startPosition, 1);
//		System.out.println("jumpTarget: "+ position.getIndex());
		Board board = getBoard();
		boolean pathObstructed = pieceToMove.isPathObstructed(startPosition, position);
//		System.out.println("PathObstructed: "+ pathObstructed);
		boolean hasJumpTarget = board.isOccupied(position);
//		System.out.println("HasJumpTarget: "+ hasJumpTarget);
		if (!hasJumpTarget || pathObstructed)
			return false;
		Piece jumpTarget = board.getPieceAt(position);
		boolean oppositeColor = !pieceToMove.sameColor(jumpTarget);
		return (super.isLegalNow() && oppositeColor);
	}

	@Override
	public boolean hasFollowUp() {
		// Check if pieceToMove was removed after promotion.
		if (pieceToMove.isRemoved())
			return false;
		return (pieceToMove.getJumpSelectionsAt(targetPosition).size() > 0);
	}
}
