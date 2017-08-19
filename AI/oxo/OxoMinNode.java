package oxo;

public class OxoMinNode extends OxoTreeNode {
	
	public OxoMinNode(OxoBoard board, Integer position, char c, IRLearning ai) {
		super(board, position, c, ai);
	}
	
	public OxoMinNode(OxoTreeNode parent, OxoBoard board, int position, char c, IRLearning ai) {
		super(parent, board, position, c, ai);
	}

	@Override
	public OxoTreeNode expandOne() {
		if (super.isFullyExpanded())
			throw new IllegalStateException();
		Integer position = super.unexpandedMoves.remove(0);
		OxoBoard boardCopy = super.board.copyBoard();
		boardCopy.executeMove(position, super.c);
		OxoTreeNode result = new OxoMaxNode(this, boardCopy, position, super.c=='x' ? 'o' : 'x', super.ai);
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
			OxoTreeNode child = expandOne();
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
