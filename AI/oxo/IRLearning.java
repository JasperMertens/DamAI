package oxo;

import java.util.ArrayList;

public class IRLearning implements OxoPlayer {
	
	static final int[] MIDDLE = new int[] {4};
	static final int[] CORNERS = new int[] {0, 2, 6, 8};;
	static final int[] EDGES = new int[] {1, 3, 5, 7};
	
	private OxoController oc;
	private char[][] board;
	private char c;
	private ArrayList<Integer> possibleMoves = new ArrayList<>(9);
	/*
	 * A vector containing all the evaluations of the features of the boardstate.
	 * The order of the features is: middle, corners, edges, majority.
	 */
	private int[] featureVector = new int[] {0,0,0,0};
	private float[] weights = new float[] {0,0,0,0};
	

	public IRLearning(OxoController oc, char c) {
		this.oc = oc;
		this.board = oc.getBoard();
		this.c = c;
		for (int i=0; i<9; i++) {
			this.possibleMoves.add(i);
		}
	}
	
	@Override
	public char getChar() {
		return this.c;
	}
	
	@Override
	public int getMove() {
		return this.possibleMoves.get(0);
	}

	@Override
	public void notifyOnMove(int move) {
		this.possibleMoves.remove(this.possibleMoves.indexOf(move));
	}
	
	private void updateFeatureVector() {
		this.featureVector[0] = this.evaluatePositions(MIDDLE);
		this.featureVector[1] = this.evaluatePositions(CORNERS);
		this.featureVector[2] = this.evaluatePositions(EDGES);
		this.featureVector[3] = this.evaluateMajority();
	}
	
	private float evaluateBoard() {
		float result = 0;
		for (int i=0; i<this.weights.length; i++) {
			result += this.weights[i]*this.featureVector[i];
		}
		return result;
	}
	
	private int evaluatePositions(int[] positions) {
		char[] posChars = this.oc.mapToChar(positions);
		int result = 0;
		for (char posChar : posChars) {
			if (posChar != '_') {
				if (posChar == this.c) {
					result++;
				} else {
					result--;
				}
			}
		}
		return result;
	}
	
	private int evaluateMajority() {
		ArrayList<char[]> lines = this.oc.getRowColDiagChars();
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
		if (this.c == 'x') {
			return maj_count_x - maj_count_o;
		} else {
			return maj_count_o - maj_count_x;
		}
	}

	

	

}
