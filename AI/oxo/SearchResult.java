package oxo;

public class SearchResult {
	
	double value;
	int move;

	public SearchResult(double value, int move) {
		this.value = value;
		this.move = move;
	}
	
	public double getValue() {
		return this.value;
	}

	public int getMove() {
		return move;
	}
}
