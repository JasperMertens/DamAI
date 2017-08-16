package treeSearch;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import environment.Board;
import game.PieceColor;
import moves.Move;

public abstract class TreeNode {
	
	protected TreeNode parent;
	protected Board board;
	protected ArrayList<TreeNode> children;
	protected float value;
	protected Move move;
	protected int nbOfChildren;
	protected ArrayList<Move> unexpandedMoves;
	protected PieceColor pieceColor;
	
	/*
	 * Constructor for tree root node
	 */
	public TreeNode(Board board, Move move, PieceColor pieceColor) {
		this.board = board;
		this.move = move;
		this.pieceColor = pieceColor;
		setUnexpandedMoves();
		this.nbOfChildren = this.unexpandedMoves.size();
	}
	
	/*
	 * Constructor for tree branch nodes
	 */
	public TreeNode(TreeNode parent, Board board, Move move, PieceColor pieceColor) {
		this.parent = parent;
		this.board = board;
		this.move = move;
		this.pieceColor = pieceColor;
		setUnexpandedMoves();
		this.nbOfChildren = this.unexpandedMoves.size();
	}
	
	private void setUnexpandedMoves() {
		this.unexpandedMoves = this.board.getPossibleMoves(this.pieceColor);
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}
	
	public boolean isLeaf() {
		return this.children.isEmpty();
	}
	
	public float getValue() {
		if (this.board == null)
			return this.value;
		else
			return this.board.getValue();
	}
	
	public boolean isFullyExpanded() {
		return this.unexpandedMoves.isEmpty();
	}
	
	public abstract TreeNode expandOne();
	
	public Set<TreeNode> expandAll() {
		if (this.isFullyExpanded())
			throw new IllegalStateException();
		Set<TreeNode> result = new HashSet<>();
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
	 * - should still be adjusted to return the next move from the root to obtain 
	 * 		that best heuristic value
	 * Initialize: alpha=-Infinity, Beta=+Infinity
	 * TODO: Optimize search ordering to improve cut-off speed
	 * 		narrow search window at endgame
	 */
	public abstract float alphaBetaMinMax(int depth, float alpha, float beta);
		
}
