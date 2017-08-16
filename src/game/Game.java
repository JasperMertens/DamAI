package game;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import environment.Board;
import environment.Piece;
import gui.GameWindow;
import moves.Move;
import moves.MoveFactory;

public class Game {
	
	private Board board;
	private Player whitePlayer;
	private Player blackPlayer;
	private Player activePlayer;
	private Player passivePlayer;
	private LinkedList<Move> queueWhitePlayer = new LinkedList<Move>();
	private LinkedList<Move> queueBlackPlayer = new LinkedList<Move>();
	private GameWindow gameWindow1;
	private GameWindow gameWindow2;
	
	public Game(Board board, Player whitePlayer, Player blackPlayer) {
		setGameWindow1(new GameWindow(this, whitePlayer, PieceColor.WHITE));
		setGameWindow2(new GameWindow(this, blackPlayer, PieceColor.BLACK));
		this.setBoard(board);
		this.setWhitePlayer(whitePlayer);
		this.setBlackPlayer(blackPlayer);
		// In English draughts Black begins!
		this.activePlayer = blackPlayer;
		this.passivePlayer = whitePlayer;
		startTurn();
	}
	
	public GameWindow getGameWindow1() {
		return this.gameWindow1;
	}
	
	private void setGameWindow1(GameWindow gameWindow) {
		this.gameWindow1 = gameWindow;
	}
	
	public GameWindow getGameWindow2() {
		return this.gameWindow1;
	}
	
	private void setGameWindow2(GameWindow gameWindow) {
		this.gameWindow2 = gameWindow;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
		board.addBoardToGame(this);
	}

	public void setWhitePlayer(Player whitePlayer) {
		whitePlayer.setGame(this);
		this.whitePlayer = whitePlayer;
	}

	public void setBlackPlayer(Player blackPlayer) {
		this.blackPlayer = blackPlayer;
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<>();
		players.add(blackPlayer);
		players.add(whitePlayer);
		return players;
	}
	
	public Boolean isActivePlayer(Player player) {
		return (this.activePlayer == player);
	}

	public void addToQueue(Move move, Player player) {
		System.out.println("move added");
		if (player == whitePlayer)
			queueWhitePlayer.add(move);
		else
			queueBlackPlayer.add(move);
	}
	
	public void startTurn() {
		System.out.println("turn started");
		if (!hasLegalMove()) {
			System.out.println(getBoard().getPieceColor(passivePlayer)+ " has won!");
			exit();
		}
		startTimer();
		executeQueue();
	}
	
	private boolean hasLegalMove() {
		return board.hasLegalMove(activePlayer);
	}
	
	/*
	 * Executes the activePlayer's turn
	 */
	private void executeQueue() throws IllegalStateException {
		LinkedList<Move> queueToExecute = this.getActivePlayerQueue();
		while(!queueToExecute.isEmpty()) {
			System.out.println("Queue to execute size: "+queueToExecute.size());
			Move moveToExecute = queueToExecute.removeFirst();
			try {
				moveToExecute.execute();
				System.out.println("Move executed");
				if (!moveToExecute.hasFollowUp())
					endTurn();
			} catch (IllegalStateException e) {}
		}
	}
	
	public void endTurn() {
		stopTimer();
		getActivePlayerQueue().clear();
		switchActivePlayer();
		startTurn();
	}
	
	private LinkedList<Move> getActivePlayerQueue() {
		if (activePlayer == whitePlayer)
			return queueWhitePlayer;
		else
			return queueBlackPlayer;
	}
	
	private void switchActivePlayer() {
		Player current = activePlayer;
		this.activePlayer = this.passivePlayer;
		this.passivePlayer = current;
	}
	
	public void addPiece(Piece piece) {
		gameWindow1.addImage(piece);
		gameWindow2.addImage(piece);
	}
	
	public void removePieceAt(Position position) {
		gameWindow1.removeImageAt(position);
		gameWindow2.removeImageAt(position);
	}
	
	public void movePiece(Piece piece) {
		gameWindow1.movePiece(piece);
		gameWindow2.movePiece(piece);
	}
	
	/*
	 * Start timer of activePlayer
	 */
	private void startTimer() {
		
	}
	
	/*
	 * Stop timer of activePlayer
	 */
	private void stopTimer() {
		
	}
	
	/*
	 * Adds a move to the Player's execution queue that is legal now, and executes it if it's the
	 * activePlayer
	 */
	public void addMove(Piece selectedPiece, Position selectedPosition, Player player) {
		Position startPosition = selectedPiece.getPosition();
		Move move = MoveFactory.createMove(selectedPiece, startPosition, selectedPosition, true);
		addToQueue(move, player);
		if (isActivePlayer(player)) {
			executeQueue();
		}
	}
	
	private void exit() {
			System.out.println("Game ended");
		}
}
