package oxo;

import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer implements OxoPlayer {
	
	private char c;
	private OxoBoard board;
	private Random ran = new Random();

	public RandomPlayer(char c) {
		this.c = c;
		this.board = new OxoBoard();
	}

	@Override
	public char getChar() {
		return this.c;
	}

	@Override
	public int getMove() {
		ArrayList<Integer> options = this.board.getPossibleMoves();
		int[] std = new int[]{0,3,6};
		for (int i : std) {
			if (options.contains(i)) {
				return i;
			}
		}
		int randIndex = this.ran.nextInt(options.size());
		return options.get(randIndex);
	}

	@Override
	public void notifyOnMove(int move, char c) {
		this.board.executeMove(move, c);
	}

	@Override
	public void notifyOnEnd(char winner) {

	}

}
