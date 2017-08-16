package moves;
import java.util.List;

import environment.Piece;
import game.Position;
// used in MoveFactory for executing parsed moves, not used for moves from GUI
public class MultiJump extends Move {

	private List<Jump> jumps;
	
	public MultiJump(Piece pieceToMove, Position targetPosition, List<Jump> jumps) throws IllegalArgumentException {
		super(pieceToMove, pieceToMove.getPosition(), targetPosition);
		if (!isLegal(pieceToMove, jumps))
			throw new IllegalArgumentException();
		this.jumps = jumps;
	}

	public boolean isLegal(Piece pieceToMove, List<Jump> jumps) {
		for (Jump jump : jumps) {
			if ((jump.getPieceToMove() != pieceToMove) ||
					!super.isLegal(pieceToMove, jump.getStartPosition(), jump.getTargetPosition()))
				return false;
		}
		return true;
	}
	
	@Override
	public void execute() throws IllegalStateException {
		if (!isLegalNow())
			throw new IllegalStateException();
		for (Jump jump : jumps) {
			jump.execute();
		}
	}

	@Override
	public boolean isLegalNow() {
		for (Jump jump : jumps) {
			if (!jump.isLegalNow())
				return false;
		}
		return true;
	}

	@Override
	// After move was executed
	public boolean hasFollowUp() {
		// Check if pieceToMove was removed after promotion.
		if (pieceToMove.isRemoved())
			return false;
		Position finalPosition = jumps.get(jumps.size()-1).getTargetPosition();
		return (pieceToMove.getJumpSelectionsAt(finalPosition).size() > 0);
	}
}
