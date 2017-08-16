package parser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import environment.Board;
import environment.Piece;
import environment.Timer;
import game.Game;
import game.Player;
import game.Position;
import moves.Move;
import moves.MoveFactory;

public class PDNParser {
	
	private Scanner sc;
	private Board board;
	private Player player1 = new Player();
	private Player player2 = new Player();
	private boolean stop = false;
	
	public static void main(String args[]) throws IOException, InterruptedException {
		//TODO Use class.getResource() to make the path generic
		Path filePath = Paths.get("C:\\Users\\jasper\\workspace\\Checkers\\Games\\CheckerGame.txt");
		PDNParser parser = new PDNParser(filePath);
		parser.parse();
		new Game(parser.board, parser.player1, parser.player2);
	}

	public PDNParser(Path filePath) throws IOException {
		this.sc = new Scanner(filePath).useDelimiter("[0-9]+\\. *");
		this.board = new Board(player1, player2, 8, new Timer(10, 0));
	}
	
	public void parse() {
		int i = 1;
		while (sc.hasNext() && !stop) {
			String movePair = sc.next();
			System.out.println(movePair);
			try {
			processMovePair(movePair);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Error at move: "+i);
			}
			i++;
		}
	}

	private void processMovePair(String movePair) throws IllegalArgumentException {
		String[] moves = movePair.split(" ");
		for (String moveString : moves) {
			if (!stop)
				processMove(moveString);
		}
	}

	private void processMove(String moveString) throws IllegalArgumentException {
		// stop parsing when you read "*"
		Pattern stopPattern = Pattern.compile("\\*");
		// for example: "26-23" (simple move)
		Pattern simpleMovePattern = Pattern.compile("[0-9]{1,2}-[0-9]{1,2}");
		// for example: "26x19x10" (explicit multiJump)
		Pattern jumpLongPattern = Pattern.compile("[0-9]{1,2}(x[0-9]{1,2})+x[0-9]{1,2}");
		// for example: "26x19" (single jump) or "26x10" (implicit multiJump)
		Pattern jumpShortPattern = Pattern.compile("[0-9]{1,2}x[0-9]{1,2}");
		
		Move move = null;
		if (stopPattern.matcher(moveString).matches()) {
			System.out.println("Stop scanning!");
			this.stop = true;
			return;
			
		} else if (simpleMovePattern.matcher(moveString).matches()) {
			String[] stringPositions = moveString.split("-");
			Position startPosition = new Position(Integer.parseInt(stringPositions[0]));
			Position targetPosition = new Position(Integer.parseInt(stringPositions[1]));
			Piece pieceToMove = board.getPieceAt(startPosition);
			move = MoveFactory.createSimpleMove(pieceToMove, startPosition, targetPosition, true);
			
		} else if (jumpLongPattern.matcher(moveString).matches()) {
			String[] stringPositions = moveString.split("x");
			List<Position> positions = new ArrayList<>();
			for (String str : stringPositions) {
				positions.add(new Position(Integer.parseInt(str)));
			}
			Piece pieceToMove = board.getPieceAt(positions.get(0));
			move = MoveFactory.createMultiJumpExplicit(pieceToMove, positions, true);
			
		} else if (jumpShortPattern.matcher(moveString).matches()) {
			String[] stringPositions = moveString.split("x");
			Position startPosition = new Position(Integer.parseInt(stringPositions[0]));
			Position targetPosition = new Position(Integer.parseInt(stringPositions[1]));
			Piece pieceToMove = board.getPieceAt(startPosition);
			try {
				move = MoveFactory.createJump(pieceToMove, startPosition, targetPosition, true);
			} catch (IllegalArgumentException e) {
				move = MoveFactory.createMultiJumpImplicit(pieceToMove, startPosition, targetPosition, true);
			}
			
		} else {
			System.out.println("No match for: "+ moveString);
			throw new IllegalArgumentException();
		}
		
		move.execute();
	}

}
