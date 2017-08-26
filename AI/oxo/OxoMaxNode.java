package oxo;

public class OxoMaxNode extends OxoTreeNode {
	
	public OxoMaxNode(OxoBoard board, char c, ReinfLearning ai) {
		super(board, c, ai);
	}
	
	public OxoMaxNode(OxoTreeNode parent, OxoBoard board, int position, char c, ReinfLearning ai) {
		super(parent, board, position, c, ai);
	}

	@Override
	public OxoTreeNode expandOne() {
		if (super.isFullyExpanded())
			throw new IllegalStateException();
		Integer position = super.unexpandedMoves.remove(0);
		OxoBoard boardCopy = super.board.copyBoard();
		boardCopy.executeMove(position, super.c);
		OxoTreeNode result = new OxoMinNode(this, boardCopy, position, super.c=='x' ? 'o' : 'x', super.ai);
		return result;
	}

	@Override
	public SearchResult alphaBetaMinMax(int depth, SearchResult alpha, SearchResult beta) {
		// base cases
		FinishedCheck fc = super.board.isFinished();
		if (fc.result()){
			return new SearchResult(super.ai.getEndReward(fc.getWinner()), super.position);
		}
		if (depth==0) {
			return new SearchResult(getValue(), super.position);
		}
		
		// Alpha is the maximal heuristic value that has been explored by this node
		//		or one of its parent nodes. The alpha value of a node that has
		//		no more unexplored children is 
		//		max(the maximum value of all its children, alpha from parent nodes).
		//		Alpha is the best already explored option along the path to the root
		//		for the maximizer.
		int N = this.nbOfChildren;
		for (int i=0; i<N; i++) {
			OxoTreeNode child = expandOne();
			SearchResult childSr = child.alphaBetaMinMax(depth-1, alpha, beta);
			alpha = SearchResult.max(alpha, childSr);
			// Prune
			// Alpha can only grow larger and beta can only become smaller.
			// The rest of the children don't have to be explored because a parent node
			// has already found another equal or preferred path.
			if (alpha.getValue() >= beta.getValue())
				break;
		}
		if (this.parent != null) {
			alpha = new SearchResult(alpha.getValue(), super.position);
		}
		return alpha;
	}
}
