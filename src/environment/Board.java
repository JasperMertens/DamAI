package environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.Game;
import game.PieceColor;
import game.Player;
import game.Position;
import moves.Move;

public class Board {
	
	private Map<PieceColor,Player> pieceColorMap = new HashMap<>();
	private Map<Position, Piece> piecePositionMap = new HashMap<>();
	private List<Piece> whitePieces = new ArrayList<>();
	private List<Piece> blackPieces = new ArrayList<>();
	private int boardSize;
	private Timer timer;
	private Game game;
	
	public Board(Player player1, Player player2, int boardSize, Timer timer) {
		pieceColorMap.put(PieceColor.WHITE, player1);
		pieceColorMap.put(PieceColor.BLACK, player2);
		this.boardSize = boardSize;
		this.timer = timer;
		List<Position> startingPositionsWhite = getStartingPositions(PieceColor.WHITE);
		List<Position> startingPositionsBlack = getStartingPositions(PieceColor.BLACK);
		for (Position position: startingPositionsWhite)
			addPieceAt(new NormalPiece(position, this, PieceColor.WHITE), position);
		for (Position position: startingPositionsBlack)
			addPieceAt(new NormalPiece(position, this, PieceColor.BLACK), position);
	}
	
	public Board(Player player1, Player player2, int boardSize, Timer timer, List<Piece> pieces) {
		pieceColorMap.put(PieceColor.WHITE, player1);
		pieceColorMap.put(PieceColor.BLACK, player2);
		this.boardSize = boardSize;
		this.timer = timer;
		for (Piece piece : pieces) {
			Piece pieceCopy = piece.copyToBoard(this);
			addPieceAt(pieceCopy, pieceCopy.getPosition());
		}
	}
	
	public void addBoardToGame(Game game) {
		this.setGame(game);
		List<Piece> pieces = getPieces();
		for (Piece piece : pieces)
			game.addPiece(piece);
	}
	
	public Board copyBoard() {
		Board copy = new Board(pieceColorMap.get(PieceColor.WHITE),pieceColorMap.get(PieceColor.BLACK),
					boardSize, timer, getPieces());
		return copy;
	}
	
	public Game getGame() {
		return game;
	}

	private void setGame(Game game) {
		this.game = game;
	}
	
	public int getSize() {
		return boardSize;
	}

	public List<Position> getStartingPositions(PieceColor pieceColor) {
		List<Position> result = new ArrayList<>();
		int start = (pieceColor == PieceColor.BLACK) ? 1 : 21;
		for (int i = start; i<start+12; i++)
			result.add(new Position(i));
		return result;
	}
	
	public PieceColor getPieceColor(Player player) {
		return (pieceColorMap.get(PieceColor.WHITE).equals(player)) ? PieceColor.WHITE : PieceColor.BLACK;
	}
	
	public Player getPlayerOfPieceColor(PieceColor color) {
		return pieceColorMap.get(color);
	}
	
	public Timer getTimer() {
		return this.timer;
	}
	
	public void addPieceAt(Piece piece, Position position) throws IllegalArgumentException {
		if (isOccupied(position))
			throw new IllegalArgumentException();
		piece.setPosition(position);
		piecePositionMap.put(position, piece);
		if (piece.getColor() == PieceColor.WHITE)
			whitePieces.add(piece);
		else
			blackPieces.add(piece);
		if (game != null)
			game.addPiece(piece);
	}
	
	public void removePieceAt(Position position) throws IllegalArgumentException {
		if (!isOccupied(position))
			throw new IllegalArgumentException();
		Piece pieceToRemove = getPieceAt(position);
		piecePositionMap.remove(position);
		if (pieceToRemove.getColor() == PieceColor.WHITE)
			whitePieces.remove(pieceToRemove);
		else
			blackPieces.remove(pieceToRemove);
		pieceToRemove.remove();
		if (game != null)
			game.removePieceAt(position);
	}
	
	public void movePiece(Piece piece, Position targetPosition) throws IllegalArgumentException {
		Position startPosition = piece.getPosition();
		if (isOccupied(targetPosition) || !isOccupied(startPosition))
			throw new IllegalArgumentException();
		if (piece.isPromoting(targetPosition)) {
			System.out.println("Promoting");
			removePieceAt(startPosition);
			addPieceAt(new King(targetPosition, this, piece.getColor()), targetPosition);
		} else {
			piecePositionMap.remove(startPosition);
			piece.setPreviousPosition(startPosition);
			piece.setPosition(targetPosition);
			piecePositionMap.put(targetPosition, piece);
			if (game != null)
				game.movePiece(piece);
		}
	}
	
	public List<Piece> getPieces() {
		List<Piece> pieces = new ArrayList<>();
		pieces.addAll(piecePositionMap.values());
		return pieces;
	}
	
	public boolean isWithinBoundaries(Position targetPosition) {
		return targetPosition.within(boardSize);
	}

	public boolean isOccupied(Position position) {
		return piecePositionMap.containsKey(position);
	}

	public Piece getPieceAt(Position position) {
		return piecePositionMap.get(position);
	}

	public boolean hasLegalMove(Player player) {
		List<Piece> piecesOfPlayer = getPiecesOfColor(getPieceColor(player));
		for (Piece piece : piecesOfPlayer)
			if (hasLegalMove(piece))
				return true;
		return false;
	}
	
	public boolean hasLegalMove(Piece piece) {
		return (piece.getPossibleSelections().size() > 0);
	}
	
	public boolean hasJumpMove(Player player) {
		List<Piece> piecesOfPlayer = getPiecesOfColor(getPieceColor(player));
		for (Piece piece : piecesOfPlayer)
			if (hasJumpMove(piece))
				return true;
		return false;
	}
	
	public boolean hasJumpMove(Piece piece) {
		return (piece.getJumpSelectionsAt(piece.getPosition()).size() > 0);
	}
	
	public ArrayList<Move> getPossibleMoves(PieceColor pieceColor) {
		List<Piece> pieces = getPiecesOfColor(pieceColor);
		List<Move> possibleMoves = new ArrayList<>();
//		if (moveToExecute.hasFollowUpAt(moveToExecute.getTargetPosition()))
		for (Piece piece : pieces) {
		}
		return null;
	}

	public List<Piece> getPiecesOfColor (PieceColor pieceColor) {
		if (pieceColor == PieceColor.WHITE)
			return whitePieces;
		else
			return blackPieces;
	}

	public float getValue() {
		// TODO Auto-generated method stub
		return 0;
	}
}
