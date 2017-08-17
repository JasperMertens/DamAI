package oxo;

import java.util.ArrayList;

public class OxoController {
	
	private char[][] board;
	private char currChar = 'x';
	private OxoPlayer x_player;
	private OxoPlayer o_player;
	
	private int moveCount = 0;

	public OxoController() {
		this.board = createEmptyBoard();
		this.x_player = new HumanPlayer(this,'x');
		this.o_player = new IRLearning(this,'o');
	}	

	public static void main(String[] args) {
		OxoController oc = new OxoController();
		oc.go();
	}
	
	public static char[][] createEmptyBoard() {
		char[][] result = new char[3][3];
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				result[i][j] = '_';
			}
		}
		return result;
	}
	
	public char[][] getBoard() {
		return this.board;
	}
	
	private void go() {
		drawBoard();
		while (!this.isFinished()) {
			executeTurn();
			switchCurrChar();
			drawBoard();
		}
	}
	
	private void executeTurn() {
		System.out.println(this.currChar+"-player his turn.");
		int move = getMoveOf(this.getCurrPlayer());
		try {
			executeMove(move);
			this.moveCount++;
		} catch (IllegalArgumentException exc) {
			executeTurn();
		}
	}
	
	private int getMoveOf(OxoPlayer player) {
		return player.getMove();
	}
	
	private void executeMove(int move) throws IllegalArgumentException {
		int i = Math.floorDiv(move, 3);
		int j = Math.floorMod(move, 3);
		if (this.board[i][j] != '_') {
			System.out.println("Invalid move, try again");
			throw new IllegalArgumentException();
		}
		this.board[i][j] = this.currChar;
		this.x_player.notifyOnMove(move);
		this.o_player.notifyOnMove(move);
	}
	
	private void switchCurrChar() {
		if (this.currChar == 'x') {
			this.currChar = 'o';
		} else {
			this.currChar = 'x';
		}
	}
	
	private OxoPlayer getCurrPlayer() {
		if (this.currChar == 'x') {
			return this.x_player;
		} else {
			return this.o_player;
		}
	}
	
	private void drawBoard() {
		System.out.println();
		for (int i=0; i<this.board.length; i++) {
			for (int j=0; j<this.board[i].length-1; j++) {
				System.out.print(this.board[i][j]);
				System.out.print(" ");
			}
			System.out.println(this.board[i][2]);
		}
		System.out.println();
	}
	
	private boolean isFinished() {
		for (char[] line : this.getRowColDiagChars()) {
			if (line[0] != '_' && line[0] == line[1] && line[1] == line[2]) {
				System.out.println(line[0]+"-player has won!");
				return true;
			}
		}
		if (this.moveCount == 9) {
			System.out.println("It's a draw!");
			return true;
		}
		return false;			
	}
	
	public ArrayList<char[]> getRowColDiagChars() {
		ArrayList<char[]> result = new ArrayList<>();
		// Add rows
		for (char[] row : this.board) {
			result.add(row);
		}
		// Add columns
		int[][] columns = { {0,3,6}, {1,4,7}, {2,5,8} };
		for (int[] col : columns) {
			char[] charArr = this.mapToChar(col);
			result.add(charArr);
			
		}
		// Add diagonals
		int[][] diagonals = { {0,4,8}, {2,4,6} };
		for (int[] diag : diagonals) {
			char[] charArr = this.mapToChar(diag);
			result.add(charArr);
		}
		return result;
	}
	
	public char[] mapToChar(int[] intArr) {
		char[] charArr = new char[intArr.length];
		for (int i=0; i<intArr.length; i++) {
			int row = Math.floorDiv(intArr[i], 3);
			int col = Math.floorMod(intArr[i], 3);
			charArr[i] = this.board[row][col];
		}
		return charArr;
	}
}
