package oxo;

import java.io.IOException;
import java.util.Scanner;

public class OxoController {
	
	private char[][] board = new char[3][3];
	private char currChar = 'o';
	private Scanner sc = new Scanner(System.in);
	private int moveCount = 0;

	public OxoController() {
		for (int i=0; i<this.board.length; i++) {
			for (int j=0; j<this.board[i].length; j++) {
				this.board[i][j] = '_';
			}
		}
	}	

	public static void main(String[] args) {
		OxoController oc = new OxoController();
		oc.go();
	}
	
	public void go() {
		drawBoard();
		while (!this.isFinished()) {
			executeTurn();
			switchCurrChar();
			drawBoard();
		}
	}
	
	public void executeTurn() {
		System.out.println(this.currChar+"-player his turn.");
		int move = getMoveOf(this.currChar+"-player");
		try {
			executeMove(move);
			this.moveCount++;
		} catch (IllegalArgumentException exc) {
			executeTurn();
		}
	}
	
	public int getMoveOf(String player) {
		System.out.println("Enter your move "+ player);
		try {
	        int move = Integer.parseInt(this.sc.nextLine());
		    if (move<0 || move>8) {
		    	throw new IOException();
		    }
		    return move;
		}
		catch (IOException e){
		    System.out.println("Error reading from user, try again");
		    return getMoveOf(player);
		}
	}
	
	public void executeMove(int move) throws IllegalArgumentException {
		int i = Math.floorDiv(move, 3);
		int j = Math.floorMod(move, 3);
		if (this.board[i][j] != '_') {
			System.out.println("Invalid move, try again");
			throw new IllegalArgumentException();
		}
		this.board[i][j] = this.currChar;
	}
	
	public void switchCurrChar() {
		if (this.currChar == 'x') {
			this.currChar = 'o';
		} else {
			this.currChar = 'x';
		}
	}
	
	public void drawBoard() {
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
	
	public boolean isFinished() {
		// Check rows
		for (char[] row : this.board) {
			if (row[0] != '_' && row[0] == row[1] && row[1] == row[2]) {
				System.out.println(row[0]+"-player has won1!");
				return true;
			}
		}
		// Check columns
		int[][] columns = { {0,3,6}, {1,4,7}, {2,5,8} };
		for (int[] col : columns) {
			if (this.threeInARow(col)) {
				return true;
			}
		}
		// Check diagonals
		int[][] diagonals = { {0,4,8}, {2,4,6} };
		for (int[] diag : diagonals) {
			if (this.threeInARow(diag)) {
				return true;
			}
		}
		if (this.moveCount == 9) {
			System.out.println("It's a draw!");
			return true;
		}
		return false;			
	}
	
	public boolean threeInARow(int[] intArr) {
		char[] charArr = new char[3];
		for (int i=0; i<3; i++) {
			int row = Math.floorDiv(intArr[i], 3);
			int col = Math.floorMod(intArr[i], 3);
			charArr[i] = this.board[row][col];
		}
		if (charArr[0] != '_' && charArr[0] == charArr[1] && charArr[1] == charArr[2]) {
			System.out.println(charArr[0]+"-player has won1!");
			return true;
		}
		return false;
	}

}
