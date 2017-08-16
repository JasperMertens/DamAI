package environment;
import javax.swing.ImageIcon;

import game.PieceColor;
import game.Position;

public class NormalPiece extends Piece {
	
	public NormalPiece(Position position, Board board, PieceColor color) {
		super(position, board, color);
		String path = (color==PieceColor.WHITE) ? "/whiteCircle.png" : "/blackCircle.png";
		image = new ImageIcon(this.getClass().getResource(path)).getImage();
	}
	
	@Override
	public Piece copyToBoard(Board newBoard) {
		return new NormalPiece(this.position,newBoard, this.color);
	}
	
	@Override
	public boolean isCorrectDirection(Position startPosition, Position targetPosition) {
		return startPosition.isForwards(targetPosition, color);
	}

	@Override
	public boolean isCorrectSimpleMoveDistance(int distance) {
		return distance == 1;
	}

	@Override
	public boolean isCorrectJumpDistance(int distance) {
//		System.out.println("jumpdistance normalPiece: "+distance);
		return distance == 2;
	}
	
	@Override
	public boolean isPathObstructed(Position startPosition, Position targetPosition) {
		return false;
	}

	@Override
	public boolean isPromoting(Position targetPosition) {
		int maxY = getBoard().getSize()-1;
		System.out.println("Promoting position? " +targetPosition.getY());
		return ((color == PieceColor.WHITE) ? (targetPosition.getY()==0) : (targetPosition.getY()==maxY));
	}
	
	

	//	@Override
	//	public Set<Position> getPossibleSelections() {
	//		Set<Position> jumpSelections = getJumpSelectionsAt(getPosition());
	//		if (jumpSelections.isEmpty())
	//			return getSimpleMoveSelections();
	//		return jumpSelections;
	//	}
		
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
		
	//	@Override
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
		
	//	@Override
	//	public Set<Move> getPossibleMoves() {
	//		Set<Move> jumpMoves = getJumpMovePossibilitiesAt(getPosition());
	//		if (jumpMoves.isEmpty())
	//			return getSimpleMovePossibilities();
	//		return jumpMoves;
	//	}
	//	
	//	private Set<Move> getSimpleMovePossibilities() {
	//		Set<Move> result = new HashSet<>();
	//		Set<Position> diagonalNeighbours = getPosition().getDiagonalNeigbours(1,1);
	//		for (Position targetPosition : diagonalNeighbours) {
	//			try {
	//				Move simpleMove = MoveFactory.createSimpleMove(this, getPosition(), targetPosition, true);
	//				result.add(simpleMove);
	//			} catch (IllegalArgumentException e) {}
	//		}
	//		return result;
	//	}
	//	
	//	private Set<Move> getJumpMovePossibilitiesAt(Position position) {
	//		Set<Move> result = new HashSet<>();
	//		Set<Position> diagonalNeighbours = position.getDiagonalNeigbours(2,2);
	//		for (Position targetPosition : diagonalNeighbours) {
	//			try {
	//				Move jumpMove = MoveFactory.createJump(this, position, targetPosition, true);
	//				result.add(jumpMove);
	//			} catch (IllegalArgumentException e) {}
	//		}
	//		return result;
	//	}
		
//	@Override
//	public Set<Position> getPossibleSelections() {
//		Set<Position> jumpSelections = getJumpSelectionsAt(getPosition());
//		if (jumpSelections.isEmpty())
//			return getSimpleMoveSelections();
//		return jumpSelections;
//	}
	
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
	
//	@Override
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
	
//	@Override
//	public Set<Move> getPossibleMoves() {
//		Set<Move> jumpMoves = getJumpMovePossibilitiesAt(getPosition());
//		if (jumpMoves.isEmpty())
//			return getSimpleMovePossibilities();
//		return jumpMoves;
//	}
//	
//	private Set<Move> getSimpleMovePossibilities() {
//		Set<Move> result = new HashSet<>();
//		Set<Position> diagonalNeighbours = getPosition().getDiagonalNeigbours(1,1);
//		for (Position targetPosition : diagonalNeighbours) {
//			try {
//				Move simpleMove = MoveFactory.createSimpleMove(this, getPosition(), targetPosition, true);
//				result.add(simpleMove);
//			} catch (IllegalArgumentException e) {}
//		}
//		return result;
//	}
//	
//	private Set<Move> getJumpMovePossibilitiesAt(Position position) {
//		Set<Move> result = new HashSet<>();
//		Set<Position> diagonalNeighbours = position.getDiagonalNeigbours(2,2);
//		for (Position targetPosition : diagonalNeighbours) {
//			try {
//				Move jumpMove = MoveFactory.createJump(this, position, targetPosition, true);
//				result.add(jumpMove);
//			} catch (IllegalArgumentException e) {}
//		}
//		return result;
//	}

}
