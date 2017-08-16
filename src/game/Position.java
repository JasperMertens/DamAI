package game;

import java.util.HashSet;
import java.util.Set;

public class Position {
	
	private int index;
	private static final int BOARDSIZE = 8;

	public Position(int i) throws IllegalArgumentException {
//		if (i == 0)
//			throw new IllegalArgumentException("Position is "+i);
		this.index = i;
	}
	
	public int getIndex() {
		return this.index;
	}

	public int getX() {
		int y = getY();
		return (2*Math.floorMod(index-1, 4)- Math.floorMod(y, 2) + 1);
	}
	
	public int getY() {
		return Math.floorDiv(index-1, 4);
	}
	
	public static int coordinatesToIndex(int x, int y) throws IllegalArgumentException {
		if ((x < 0) || (y < 0) || (x > BOARDSIZE-1) || (y > BOARDSIZE-1))
			throw new IllegalArgumentException();
		if (Math.floorMod(x, 2) == Math.floorMod(y, 2))
			return 0;
		int a = Math.floorDiv(x, 2);
		int b = 4*y;
		return a+b+1;
	}
	
	public static int pixelCoordinatesToIndex(int x, int y, int boardPixelSize) {
		int xPos = Math.floorDiv(x, boardPixelSize/8);
		int yPos = Math.floorDiv(y, boardPixelSize/8);
		return coordinatesToIndex(xPos, yPos);
	}
	
	public int getPixelCoordinateX(int boardPixelSize) {
		return (int) (getX()*boardPixelSize/8.0);
	}
	
	public int getPixelCoordinateY(int boardPixelSize) {
		return (int) (getY()*boardPixelSize/8.0);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (index != other.index)
			return false;
		return true;
	}
	
	public boolean isDiagonal(Position other) {
		int XDistance = Math.abs(getX()-other.getX());
		int YDistance = Math.abs(getY()-other.getY());
		return XDistance == YDistance;
	}

	public boolean isForwards(Position targetPosition, PieceColor color) {
		boolean higher = targetPosition.getIndex() > index;
		return (color == PieceColor.WHITE) ? !higher : higher;
	}

	public boolean within(int boardsize) {
		return !(getX() < 0 || getY() <0 || getX() > boardsize || getY() > boardsize);
	}

	public Position getDiagExtended(Position other, int i) throws IllegalArgumentException {
		if (!this.isDiagonal(other))
			throw new IllegalArgumentException();
		int xSign = Integer.signum(other.getX()-getX());
		int ySign = Integer.signum(other.getY()-getY());
		int xResult = getX() + xSign*i;
		int yResult = getY() + ySign*i;
		return new Position(coordinatesToIndex(xResult, yResult));
	}

	public int diagonalDistance(Position other) throws IllegalArgumentException {
		if (!this.isDiagonal(other))
			throw new IllegalArgumentException();
		return Math.abs(getY()-other.getY());
	}
	
	public Set<Position> getAllDirectlyAdjacent() {
		Set<Position> directlyAdjacents = new HashSet<>();
		int X = getX();
		int Y = getY();
		int[] steps = new int[]{1, 1, -1, -1, 1, -1, 1, -1};
		for (int i=0; i<4; i++) {
			try {
				Position adjacentPos = new Position(Position.coordinatesToIndex(X+steps[i], Y+steps[i+4]));
				directlyAdjacents.add(adjacentPos);
			} catch (IllegalArgumentException e) {}
		}
		return directlyAdjacents;
	}
	
	public Set<Position> getDiagonalNeigbours(int lowerBound, int upperBound) 
			throws IllegalArgumentException {
		if (upperBound > lowerBound)
			throw new IllegalArgumentException();
		Set<Position> diagonalNeighbours = new HashSet<>();
		Set<Position> directlyAdjacents = getAllDirectlyAdjacent();
		for (int i=lowerBound; i<=upperBound; i++) {
			for (Position adjPos : directlyAdjacents) {
				try {
					diagonalNeighbours.add(this.getDiagExtended(adjPos, i));
				} catch (IllegalArgumentException e) {}
			}
		}
		return diagonalNeighbours;
	}

}
