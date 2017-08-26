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
	
	public static SearchResult max(SearchResult sr1, SearchResult sr2) {
		if(sr2.getValue() > sr1.getValue()) {
			return sr2;
		} else {
			return sr1;
		}
	}
	
	public static SearchResult min(SearchResult sr1, SearchResult sr2) {
		if(sr2.getValue() < sr1.getValue()) {
			return sr2;
		} else {
			return sr1;
		}
	}
}
