package moves;
import environment.Piece;
import game.Player;
import game.Position;

public class SimpleMove extends Move {

	public SimpleMove(Piece pieceToMove, Position startPosition, Position selectedPosition) throws IllegalArgumentException {
		super(pieceToMove, startPosition, selectedPosition);
		if (!isLegal(pieceToMove, targetPosition))
			throw new IllegalArgumentException();
	}

	@Override
	public void execute() throws IllegalStateException {
		if (!isLegalNow())
			throw new IllegalStateException();
		getBoard().movePiece(pieceToMove, targetPosition);
	}
	
	public static boolean isLegal(Piece pieceToMove, Position targetPosition) {
		Position startPosition = pieceToMove.getPosition();
		boolean correctDistance = pieceToMove.isCorrectSimpleMoveDistance(startPosition.diagonalDistance(targetPosition));
		return Move.isLegal(pieceToMove, startPosition, targetPosition) && correctDistance;
	}
	
	@Override
	public boolean isLegalNow() {
		Player player = pieceToMove.getPlayer();
		boolean hasJumpMove = pieceToMove.getBoard().hasJumpMove(player);
		boolean pathObstructed = pieceToMove.isPathObstructed(startPosition, targetPosition);
		return (super.isLegalNow() && !hasJumpMove && !pathObstructed);
	}

	@Override
	public boolean hasFollowUp() {
		return false;
	}
}
