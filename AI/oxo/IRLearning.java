package oxo;

import java.util.ArrayList;

public class IRLearning implements OxoPlayer {
	
	static final int[] MIDDLE = new int[] {4};
	static final int[] CORNERS = new int[] {0, 2, 6, 8};;
	static final int[] EDGES = new int[] {1, 3, 5, 7};
	
	private OxoController oc;
	private OxoBoard board;
	private char c;
	private ArrayList<Integer> possibleMoves = new ArrayList<>(9);
	/*
	 * A vector containing all the evaluations of the features of the boardstate.
	 * The order of the features is: middle, corners, edges, majority.
	 */
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
	
	private float[] getFeatureVector(OxoBoard board) {
		float[] result = new float[] {
		board.evaluatePositions(MIDDLE, this.c),
		board.evaluatePositions(CORNERS, this.c),
		board.evaluatePositions(EDGES, this.c),
		board.evaluateMajority(this.c) };
		return result;
	}
	
	public float evaluateBoard(OxoBoard board) {
		float[] featureVector = getFeatureVector(board);
		float result = 0;
		for (int i=0; i<this.weights.length; i++) {
			result += this.weights[i]*featureVector[i];
		}
		return result;
	}

}
