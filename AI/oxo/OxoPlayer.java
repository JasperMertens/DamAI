package oxo;

public interface OxoPlayer {
	
	public char getChar();
	
	public int getMove();
	
	public void notifyOnMove(int move, char c);

	public void notifyOnEnd(char winner);

}
