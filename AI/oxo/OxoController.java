package oxo;

public class OxoController {
	
	private OxoBoard board;
	private char currChar = 'x';
	private OxoPlayer x_player;
	private OxoPlayer o_player;
	
	public OxoController() {
		this.board = new OxoBoard();
		this.x_player = new ReinfLearning('x', "AI/oxo/weights2.txt");
		this.o_player = new ReinfLearning('o', "AI/oxo/weights.txt");
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
	
	public OxoBoard copyBoard() {
		return this.board.copyBoard();
	}
	
	private void go() {
		drawBoard();
		FinishedCheck finCheck = this.board.isFinished();
		while (!finCheck.result()) {
			executeTurn();
			finCheck = this.board.isFinished();
			drawBoard();
			switchCurrChar();
		}
		finCheck.printMessage();
		this.x_player.notifyOnEnd(finCheck.getWinner());
		this.o_player.notifyOnEnd(finCheck.getWinner());
		
	}
	
	private void executeTurn() {
		System.out.println(this.currChar+"-player his turn.");
		int move = getMoveOf(this.getCurrPlayer());
		try {
			executeMove(move);
		} catch (IllegalArgumentException exc) {
			executeTurn();
		}
	}
	
	private int getMoveOf(OxoPlayer player) {
		return player.getMove();
	}
	
	private void executeMove(int position) throws IllegalArgumentException {
		this.board.executeMove(position, this.currChar);
		this.x_player.notifyOnMove(position, this.currChar);
		this.o_player.notifyOnMove(position, this.currChar);
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
	
}
