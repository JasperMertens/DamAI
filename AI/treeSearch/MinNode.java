package treeSearch;
import environment.Board;
import game.PieceColor;
import moves.Move;

public class MinNode extends TreeNode {
	
	public MinNode(Board board, Move move, PieceColor pieceColor) {
		super(board, move, pieceColor);
	}
	
	public MinNode(TreeNode parent, Board board, Move move, PieceColor pieceColor) {
		super(parent,board, move, pieceColor);
	}

	@Override
	public TreeNode expandOne() {
		if (super.isFullyExpanded())
			throw new IllegalStateException();
		Move move = super.unexpandedMoves.remove(0);
		Board boardCopy = (super.unexpandedMoves.size() == 0) ? null : super.board.copyBoard();
		move.execute();
		TreeNode result = new MaxNode(this, super.board, move,
				super.pieceColor.equals(PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE);
		super.board = boardCopy;
		return result;
	}

	@Override
	public float alphaBetaMinMax(int depth, float alpha, float beta) {
		// base case
		if (depth==0) {
			return getValue();
		}

		// Beta is the minimal heuristic value that has been explored by this node
		//		or one of its parent nodes. The beta value of a node that has
		//		no more unexplored children is 
		//		min(the minimum value of all its children, beta from parent nodes)
		int N = this.nbOfChildren;
		for (int i=0; i<N; i++) {
			TreeNode child = expandOne();
			beta = Math.min(beta, child.alphaBetaMinMax(depth-1, alpha, beta));
			// Prune
			// Alpha can only grow larger and beta can only become smaller.
			// The rest of the children don't have to be explored because a parent node
			// has already found another equal or preferred path.
			if (alpha >= beta)
				break;
		}
		return beta;
	}
}
