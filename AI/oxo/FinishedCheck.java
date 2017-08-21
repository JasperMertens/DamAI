package oxo;

public class FinishedCheck {
	
	private boolean bool;
	private char winner;
	private String message;

	public FinishedCheck(boolean bool, char winner, String message) {
		this.bool = bool;
		this.winner = winner;
		this.message = message;
	}
	
	public boolean result() {
		return this.bool;
	}
	
	public char getWinner() {
		return this.winner;
	}
	
	public void printMessage() {
		if (this.bool) {
			System.out.println(this.message);
		}
	}

}
