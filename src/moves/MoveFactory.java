package moves;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import environment.Piece;
import game.Position;

public class MoveFactory {
	// Is startPosition necessary? - Yes for createJump, to check MultiJumps while piece hasn't moved yet.

	public static Move createMove(Piece pieceToMove, Position startPosition, 
			Position targetPosition, Boolean mustBeLegalNow)
			throws IllegalArgumentException {
		try {
			// Common case first, but this implicitly checks if another same colored piece has a jumpMove
			return createSimpleMove(pieceToMove, startPosition, targetPosition, mustBeLegalNow);
		} catch (IllegalArgumentException exc) {
			return createJump(pieceToMove, startPosition, targetPosition, mustBeLegalNow);
		}
	}
	
	public static SimpleMove createSimpleMove(Piece pieceToMove, Position startPosition,
			Position targetPosition, Boolean mustBeLegalNow) 
			throws IllegalArgumentException {
		SimpleMove simpleMove = new SimpleMove(pieceToMove, startPosition, targetPosition);
		if (mustBeLegalNow && !simpleMove.isLegalNow())
			throw new IllegalArgumentException();
		return simpleMove;
	}
	
	public static Jump createJump(Piece pieceToMove, Position startPosition, 
			Position targetPosition, Boolean mustBeLegalNow) 
			throws IllegalArgumentException {
		Jump jump = new Jump(pieceToMove, startPosition, targetPosition);
//		System.out.println("Basic jump created: "+targetPosition.getIndex());
		if (mustBeLegalNow && !jump.isLegalNow())
			throw new IllegalArgumentException();
//		System.out.println("Jump is legal now: "+targetPosition.getIndex());
		return jump;
	}
	/**
	 * Create a MultiJump with explicit positions
	 * @param pieceToMove
	 * @param positions
	 * 		| The first position must be the startingPosition and the last position must be
	 * 		| the targetPosition, at least one intermediate position is required.
	 * @param mustBeLegalNow
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static MultiJump createMultiJumpExplicit(Piece pieceToMove, 
			List<Position> positions, Boolean mustBeLegalNow)
			throws IllegalArgumentException {
		System.out.println("Creating MultiJumpExplicit");
		if (positions.size() < 3)
			throw new IllegalArgumentException();
		List<Jump> jumps = new ArrayList<Jump>();
		for (int i=0; i<positions.size()-1; i++) {
			jumps.add(createJump(pieceToMove, positions.get(i), positions.get(i+1), mustBeLegalNow));
		}
		MultiJump multiJump = new MultiJump(pieceToMove, positions.get(positions.size()-1), jumps);
		return multiJump;
	}
	
	public static MultiJump createMultiJumpImplicit(Piece pieceToMove,
			Position startPosition, Position targetPosition, Boolean mustBeLegalNow)
			throws IllegalArgumentException {
		System.out.println("Creating MultiJumpImplicit");
		List<Position> lst = new ArrayList<>();
		lst.add(startPosition);
		List<Position> positions = getImplicitPositions(pieceToMove, targetPosition, lst);
		MultiJump multiJump = createMultiJumpExplicit(pieceToMove, positions, mustBeLegalNow);
		return multiJump;
	}
	// TODO: Replace by getImplicitMoves() with getJumpMovePossibilitiesAt(Position position)?
	
	/* Returns a list of all positions that are contained in the implicit multiJump.
	* Found by recursively adding the possible jump positions from the last found position.
	* Note: With implicit multiJump there can't be any ambigu paths, but there may be
	*		other jump targets available along the way.
	* When there are multiple choices of jumps at a position, backtracking is used to
	* find the single(can't be ambigu) correct path to the end position.
	**/ 
	public static List<Position> getImplicitPositions(Piece pieceToMove, Position targetPosition,
			List<Position> positions) throws IllegalArgumentException {
		
		// The last found position
		Position lastPos = positions.get(positions.size()-1);
		System.out.println("Position: "+lastPos.getIndex());
		// Get the next possible positions
		Set<Position> jumpTargets = pieceToMove.getJumpSelectionsAt(lastPos);
		System.out.println("jumpTargets size: "+jumpTargets.size());
		if (jumpTargets.size() == 0) {
			// The multiJump ends here
			if (lastPos.equals(targetPosition)) {
				// We have found the right path if the last found position is
				// the targetPosition
				return positions;
			
			} else {
				// We must backtrack to take another path or say that the input is invalid
				// if there are no unexplored possible paths left
				throw new IllegalArgumentException();
			}
		} 
		// A separate treatment of the common case to hopefully improve performance
		else if (jumpTargets.size() == 1) {
			positions.add(jumpTargets.iterator().next());
			return MoveFactory.getImplicitPositions(pieceToMove, targetPosition, positions);
		} 
		// Loop over all jumpTarget choices to implement backtracking
		else {
			Iterator<Position> it = jumpTargets.iterator();
			// A new list to hold the rest of the path, beginning with one of the jumpTargets.
			List<Position> positionStub = new ArrayList<>();
			while (it.hasNext()) {
				Position jumpTarget = it.next();
				// Adding one of the jumpTargets to the empty positionStub
				positionStub.add(jumpTarget);
				try {
					List<Position> resultStub = MoveFactory.getImplicitPositions(pieceToMove, 
							targetPosition, positionStub);
					// Appending the newly found end piece of the path to the already found beginning
					// and returning the full path
					positions.addAll(resultStub);
					return positions;
				} catch (IllegalArgumentException exc) {
					// Emptying the positionStub for the next iteration
					positionStub.clear();
				}
			// If this loop over all jumpTargets at this position fails, than we backtrack to another jumpTarget
			// at a previous position or, if there is none, report that the input was invalid.
			} throw new IllegalArgumentException();
		}
	}
}
