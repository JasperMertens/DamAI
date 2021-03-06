package oxo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class OxoTreeNode {
	
	protected OxoTreeNode parent;
	protected OxoBoard board;
	protected ArrayList<OxoTreeNode> children;
	protected float value;
	protected int nbOfChildren;
	protected int position;
	protected char c;
	protected ReinfLearning ai;
	protected ArrayList<Integer> unexpandedMoves;
	
	/*
	 * Constructor for tree root node
	 */
	public OxoTreeNode(OxoBoard board, char c, ReinfLearning ai) {
		this.board = board;
		this.c = c;
		this.ai = ai;
		setUnexpandedMoves();
		this.nbOfChildren = this.unexpandedMoves.size();
	}
	
	/*
	 * Constructor for tree branch nodes
	 */
	public OxoTreeNode(OxoTreeNode parent, OxoBoard board, int position, char c, ReinfLearning ai) {
		this.parent = parent;
		this.board = board;
		this.position = position;
		this.c = c;
		this.ai = ai;
		setUnexpandedMoves();
		this.nbOfChildren = this.unexpandedMoves.size();
	}
	
	private void setUnexpandedMoves() {
		this.unexpandedMoves = this.board.getPossibleMoves();
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}
	
	public boolean isLeaf() {
		return this.children.isEmpty();
	}
	
	public double getValue() {
		if (this.board == null)
			return this.value;
		else
			return this.ai.evaluateBoard(this.board);
	}
	
	public boolean isFullyExpanded() {
		return this.unexpandedMoves.isEmpty();
	}
	
	public abstract OxoTreeNode expandOne();
	
	public Set<OxoTreeNode> expandAll() {
		if (this.isFullyExpanded())
			throw new IllegalStateException();
		Set<OxoTreeNode> result = new HashSet<>();
		int N = this.unexpandedMoves.size();
		for (int i=1; i<N; i++) {
			result.add(this.expandOne());
		}
		return result;
	}
	
	/*
	 * Standard backtracking alphaBetaMinMax algorithm
	 * - depth gives the search depth still left to explore and is counted down along the way
	 * - returns the best possible reliably obtainable heuristic value at the given depth
	 * Initialize: alpha= new SearchResult(-Infinity, -1), Beta= new SearchResult(+Infinity, -1);
	 * TODO: Optimize search ordering to improve cut-off speed
	 * 		narrow search window at endgame
	 */
	public abstract SearchResult alphaBetaMinMax(int depth, SearchResult alpha, SearchResult beta);
		
}
