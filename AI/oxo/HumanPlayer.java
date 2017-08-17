package oxo;

import java.io.IOException;
import java.util.Scanner;

public class HumanPlayer implements OxoPlayer {

	private OxoController oc;
	private char c;
	private Scanner sc = new Scanner(System.in);

	public HumanPlayer(OxoController oc, char c) {
		this.oc = oc;
		this.c = c;
	}
	
	@Override
	public char getChar() {
		return this.c;
	}

	@Override
	public int getMove() {
		System.out.println("Enter your move "+ this.c+"-player...");
		try {
	        int move = Integer.parseInt(this.sc.nextLine());
		    if (move<0 || move>8) {
		    	throw new IOException();
		    }
		    return move;
		}
		catch (IOException e){
		    System.out.println("Error reading from user, try again");
		    return getMove();
		}
	}

	@Override
	public void notifyOnMove(int move) {
		System.out.println("Move "+move+" executed.");
		
	}


}