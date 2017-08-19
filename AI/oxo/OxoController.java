package oxo;

public class OxoController {
	
	private OxoBoard board;
	private char currChar = 'x';
	private OxoPlayer x_player;
	private OxoPlayer o_player;
	
	private int moveCount = 0;

	public OxoController() {
		this.board = new OxoBoard();
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
	
	public OxoBoard getBoard() {
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
	
	private void executeMove(int position) throws IllegalArgumentException {
		this.board.executeMove(position, this.currChar);
		this.x_player.notifyOnMove(position);
		this.o_player.notifyOnMove(position);
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
		this.board.draw();
	}
	
	private boolean isFinished() {
		for (char[] line : this.board.getRowColDiagChars()) {
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
}
