package oxo;

import java.util.ArrayList;

public class OxoBoard {
	
	private char[][] charTable;
	private ArrayList<Integer> moveHistory = new ArrayList<>();

	public OxoBoard() {
		char[][] result = new char[3][3];
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				result[i][j] = '_';
			}
		}
		this.charTable = result;
	}
	
	public OxoBoard(char[][] charTable, ArrayList<Integer> moveHistory) {
		this.charTable = charTable;
		this.moveHistory = moveHistory;
	}
	
	public OxoBoard copyBoard() {
		char[][] tableCopy = new char[3][3];
		for (int i=0; i<3; i++) {
			tableCopy[i] = this.charTable[i].clone();
		}
		ArrayList<Integer> moveHistoryCopy = new ArrayList<>(this.moveHistory);
		return new OxoBoard(tableCopy, moveHistoryCopy);
	}
	
	public static int getRow(int position) {
		return Math.floorDiv(position, 3);
	}
	
	public static int getColumn(int position) {
		return Math.floorMod(position, 3);
	}
	
	public char[][] getCharTable() {
		return this.charTable;
	}
	
	public ArrayList<Integer> getMoveHistory() {
		return moveHistory;
	}

	public void executeMove(int position, char c) throws IllegalArgumentException {
		int i = OxoBoard.getRow(position);
		int j = OxoBoard.getColumn(position);
		if (this.charTable[i][j] != '_') {
			System.out.println("Invalid move "+position+", try again");
			throw new IllegalArgumentException();
		}
		this.moveHistory.add(position);
		this.charTable[i][j] = c;
	}
	
	public void undoMove(int position, char c)throws IllegalArgumentException {
		int i = OxoBoard.getRow(position);
		int j = OxoBoard.getColumn(position);
		if (this.charTable[i][j] != '_') {
			System.out.println("Invalid undo!");
			throw new IllegalArgumentException();
		}
		int index = this.moveHistory.indexOf(position);
		this.moveHistory.remove(index);
		this.charTable[i][j] = '_';
	}
	
	public void draw() {
		System.out.println();
		for (int i=0; i<this.charTable.length; i++) {
			for (int j=0; j<this.charTable[i].length-1; j++) {
				System.out.print(this.charTable[i][j]);
				System.out.print(" ");
			}
			System.out.println(this.charTable[i][this.charTable.length-1]);
		}
		System.out.println();
	}
	
	public ArrayList<char[]> getRowColDiagChars() {
		ArrayList<char[]> result = new ArrayList<>();
		// Add rows
		for (char[] row : this.charTable) {
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
			int row = getRow(intArr[i]);
			int col = getColumn(intArr[i]);
			charArr[i] = this.charTable[row][col];
		}
		return charArr;
	}
	
	public int evaluatePositions(int[] positions, char character) {
		char[] posChars = this.mapToChar(positions);
		int result = 0;
		for (char posChar : posChars) {
			if (posChar != '_') {
				if (posChar == character) {
					result++;
				} else {
					result--;
				}
			}
		}
		return result;
	}
	
	public int evaluateMajority(char character) {
		ArrayList<char[]> lines = this.getRowColDiagChars();
		int maj_count_x = 0;
		int maj_count_o = 0;
		for (char[] line : lines) {
			int x_count = 0;
			int o_count = 0;
			for (char c : line) {
				if (c == 'x') { x_count++; }
				else if (c == 'o') { o_count++;}
			}
			if (x_count > o_count) {
				maj_count_x++;
			} else if (o_count > x_count) {
				maj_count_o++;
			}
		}
		if (character == 'x') {
			return maj_count_x - maj_count_o;
		} else {
			return maj_count_o - maj_count_x;
		}
	}

	public ArrayList<Integer> getPossibleMoves() {
		ArrayList<Integer> result = new ArrayList<>();
		for (int i=0; i<9; i++) {
			if (!this.moveHistory.contains(i)) {
				result.add(i);
			}
		}
		return result;
	}
	
	public FinishedCheck isFinished() {
		for (char[] line : this.getRowColDiagChars()) {
			if (line[0] != '_' && line[0] == line[1] && line[1] == line[2]) {
				return new FinishedCheck(true, line[0],line[0]+"-player has won!");
			}
		}
		if (this.getPossibleMoves().isEmpty()) {
			System.out.println("It's a draw!");
			return new FinishedCheck(true, '-', "It's a draw!");
		}
		return new FinishedCheck(false, '-', "");			
	}

}
