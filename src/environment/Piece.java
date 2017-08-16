package environment;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.PieceColor;
import game.Player;
import game.Position;
import moves.Jump;
import moves.Move;
import moves.MoveFactory;
import moves.MultiJump;

public abstract class Piece {

	protected Position position;
	protected Position previousPosition;
	protected Board board;
	protected PieceColor color;
	private static final int BOARDSIZE = 722;
	private static final int width = BOARDSIZE/8;
	private static final int height = BOARDSIZE/8;
	protected Image image;
	private boolean isRemoved = false;
	
	public Piece(Position position, Board board, PieceColor color) {
		this.setPosition(position);
		this.setPreviousPosition(position);
		this.board = board;
		this.color = color;
	}
	
	public abstract Piece copyToBoard(Board newBoard);

	public Position getPosition() {
		return this.position;
	}
	
	public void setPosition(Position position) throws IllegalArgumentException {
		if ((position.getIndex() <= 0) || (position.getIndex() > 32))
			throw new IllegalArgumentException();
		this.position = position;
	}
	
	public Position getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(Position previousPosition) {
		this.previousPosition = previousPosition;
	}

	public Board getBoard() {
		return this.board;
	}
	
	public PieceColor getColor() {
		return this.color;
	}
	
	public Player getPlayer() {
		return this.board.getPlayerOfPieceColor(this.color);
	}
	
	public void remove() {
		// TODO Actually remove piece from memory.
		isRemoved = true;
		System.out.println("I'm ded  :(");
	}
	
	public boolean isRemoved() {
		return isRemoved ;
	}

	public void paintImage(Graphics g){
		int pixelX = position.getPixelCoordinateX(BOARDSIZE);
		int pixelY = position.getPixelCoordinateY(BOARDSIZE);
		g.drawImage(image, pixelX, pixelY, pixelX+width, pixelY+height,
				0, 0, width, height, null);
	}
	
	public abstract boolean isCorrectDirection(Position startPosition, Position targetPosition);
	
	public Image getImage() {
		return image;
	}
	
	public boolean sameColor(Piece otherPiece) {
		return (otherPiece.getColor() == getColor());
	}
	
	public abstract boolean isCorrectSimpleMoveDistance(int distance);

	public abstract boolean isCorrectJumpDistance(int distance);

	public abstract boolean isPathObstructed(Position startPosition, Position targetPosition);

	public Set<Position> getPossibleSelections() {
		Set<Position> jumpSelections = getJumpSelectionsAt(getPosition());
		if (jumpSelections.isEmpty())
			return getSimpleMoveSelections();
		return jumpSelections;
	}
	
	private Set<Position> getSimpleMoveSelections() {
		Set<Position> result = new HashSet<>();
		Set<Position> diagonalNeighbours = getPosition().getDiagonalNeigbours(1,1);
		for (Position targetPosition : diagonalNeighbours) {
			try {
				MoveFactory.createSimpleMove(this, getPosition(), targetPosition, true);
				result.add(targetPosition);
			} catch (IllegalArgumentException e) {}
		}
		return result;
	}
	
	/*
	 * Returns the possible jump options of this Piece if it were at Position.
	 */
	public Set<Position> getJumpSelectionsAt(Position position) {
		Set<Position> result = new HashSet<>();
		Set<Position> diagonalNeighbours = position.getDiagonalNeigbours(2,2);
		for (Position targetPosition : diagonalNeighbours) {
			try {
				MoveFactory.createJump(this, position, targetPosition, true);
				result.add(targetPosition);
			} catch (IllegalArgumentException e) {}
		}
		return result;
	}

	public abstract boolean isPromoting(Position targetPosition);

	public Set<Move> getPossibleMoves() {
		Set<Move> jumpMoves = getJumpMovePossibilitiesAt(getPosition());
		if (jumpMoves.isEmpty())
			return getSimpleMovePossibilities();
		return jumpMoves;
	}
	
	private Set<Move> getSimpleMovePossibilities() {
		Set<Move> result = new HashSet<>();
		Set<Position> diagonalNeighbours = getPosition().getDiagonalNeigbours(1,1);
		for (Position targetPosition : diagonalNeighbours) {
			try {
				Move simpleMove = MoveFactory.createSimpleMove(this, getPosition(), targetPosition, true);
				result.add(simpleMove);
			} catch (IllegalArgumentException e) {}
		}
		return result;
	}
	
	private Set<Move> getJumpMovePossibilitiesAt(Position position) {
		Set<Move> result = new HashSet<>();
		Set<Position> diagonalNeighbours = position.getDiagonalNeigbours(2,2);
		for (Position targetPosition : diagonalNeighbours) {
			try {
				Jump jump = MoveFactory.createJump(this, position, targetPosition, true);
				if (jump.hasFollowUp()) {
					List<Position> positions = new ArrayList<>();
					positions.add(position);
					positions.add(targetPosition);
					Set<MultiJump> multiJumps = constructPossibleMultiJumps(positions);
					result.addAll(multiJumps);
				} else
					result.add(jump);
			} catch (IllegalArgumentException e) {}
		}
		return result;
	}
	
	private Set<MultiJump> constructPossibleMultiJumps(List<Position> positions) {
		Position lastPos = positions.get(positions.size()-1);
		Set<Position> jumpSelections = getJumpSelectionsAt(lastPos);
		Set<MultiJump> result = new HashSet<>();
		// trivial case
		if (jumpSelections.size() == 0) {
			MultiJump multiJump = MoveFactory.createMultiJumpExplicit(this, positions, true);
			result.add(multiJump);
		} 
		else {
			for (Position jumpSelection : jumpSelections) {
				positions.add(jumpSelection);
				result.addAll(constructPossibleMultiJumps(positions));
				positions.remove(positions.size()-1);
			}
		}
		return result;
	}
}
