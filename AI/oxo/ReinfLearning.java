package oxo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReinfLearning implements OxoPlayer {
	
	static final int[] MIDDLE = new int[] {4};
	static final int[] CORNERS = new int[] {0, 2, 6, 8};;
	static final int[] EDGES = new int[] {1, 3, 5, 7};
	
	static double ALPHA = 0.9; //Learning rate
	
	private OxoBoard board;
	private char c;
	private String url;
	/*
	 * A vector containing all the evaluations of the features of the boardstate.
	 * The order of the features is: middle, corners, edges, majority.
	 */
	private double[] weights = new double[4];

	public ReinfLearning(char c, String url) {
		this.board = new OxoBoard();
		this.c = c;
		this.url = url;
//		this.loadWeights();
		this.weights = new double[]{0.0,0.0,0.0,0.0};
	}
	
	@Override
	public char getChar() {
		return this.c;
	}
	
	@Override
	public int getMove() {
		OxoMaxNode maxNode = new OxoMaxNode(this.board, this.c, this);
		SearchResult searchResult = maxNode.alphaBetaMinMax(3, -Double.MAX_VALUE, Double.MAX_VALUE);
		System.out.println("BoardEvalutation: "+ searchResult.getValue());
		return searchResult.getMove();
	}

	@Override
	public void notifyOnMove(int move, char c) {
		this.board.executeMove(move, c);
	}
	
	@Override
	public void notifyOnEnd(char winner) {
		double reward = this.getEndReward(winner);
//		this.learn(reward);
	}
	
	private double[] getFeatureVector(OxoBoard board) {
		double[] result = new double[] {
		board.evaluatePositions(MIDDLE, this.c),
		board.evaluatePositions(CORNERS, this.c)/2.0, // rescaling
		board.evaluatePositions(EDGES, this.c)/2.0,
		board.evaluateMajority(this.c) };
		return result;
	}
	
	private void storeWeights() {
		File oldFile = new File(this.url);
		if (oldFile.exists()) {
			oldFile.delete();
		}
		File file = new File(this.url);
		try {
			File directory = new File(file.getParent());
			if (!directory.exists()) {
				directory.mkdirs();
			}
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("Excepton Occured: " + e.toString());
		}
 
		try {
			// Convenience class for writing character files
			FileWriter writer;
			writer = new FileWriter(file.getAbsoluteFile(), false);
						
			// Store JSON representation
			JSONObject obj = new JSONObject();
			JSONArray list = new JSONArray(this.weights);
			obj.put("weights", list);
			
			// Writes text to a character-output stream
			BufferedWriter bufferWriter = new BufferedWriter(writer);
			bufferWriter.write(obj.toString());
			System.out.println("Writing data to "+this.url);
			bufferWriter.close();
 
		} catch (IOException e) {
			System.out.println("Hmm.. Got an error while saving data to file " + e.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void loadWeights() {
		File file = new File(this.url);
		if (!file.exists())
			System.out.println("File doesn't exist");
		
		try {
			byte[] encoded = Files.readAllBytes(file.toPath());
			String source = new String(encoded, "UTF-8");
			JSONObject obj = new JSONObject(source);
			JSONArray list = obj.getJSONArray("weights");
			for (int i=0; i<list.length(); i++) {
				this.weights[i] = list.getDouble(i);
			}
		} catch (Exception e) {
			System.out.println("error loading from file " + e.toString());
		}
	}
	
	public double evaluateBoard(OxoBoard board) {
		double[] featureVector = getFeatureVector(board);
		double result = 0;
		for (int i=0; i<this.weights.length; i++) {
			result += this.weights[i]*featureVector[i];
		}
		return result;
	}
	
	public double getEndReward(char winner) {
		if (winner=='-') {
			return 0;
		} else if (winner == this.c) {
			return 1;
		} else {
			return -1;
		}
	}
	
	private void restartSearch() {
		// TODO Auto-generated method stub
		
	}

	private void learn(double reward) {
		double prediction = this.evaluateBoard(this.board);
		double[] featureVector = this.getFeatureVector(this.board);
		double difference = prediction - reward;
		System.out.println("Prediction error: "+difference);
		System.out.println("Old weights: "+Arrays.toString(this.weights));
		for (int i=0; i<this.weights.length; i++) {
			this.weights[i] += ALPHA*difference*featureVector[i];
		}
		normalizeWeights();
		storeWeights();
		System.out.println("New weights: "+Arrays.toString(this.weights));
		System.out.println("featureVector: "+Arrays.toString(featureVector)+"\r\n");
	}
	
	/*
	 * Normalizes the weights so they lie between 0 and 1.
	 * TODO: Needs a different approach, we don't want a weight to be 0.
	 */
	private void normalizeWeights() {
		double w_max = this.weights[0];
		double w_min = this.weights[0];
		double mean =  Arrays.stream(this.weights)
				.reduce(0, (x,y) -> x+y)/this.weights.length;;
		double variance = Arrays.stream(this.weights)
				.map(x -> (x-mean)*(x-mean))
				.sum()/(this.weights.length-1);
		double standDev = Math.sqrt(variance);
		for (int i=0; i<this.weights.length; i++) {
			this.weights[i] = (this.weights[i] - mean)/standDev;
		}
//		for (double w : this.weights) {
//			if (w > w_max) {
//				w_max = w;
//			} else if (w < w_min) {
//				w_min = w;
//			}
//		}
//		double ampl = w_max-w_min;
//		for (int i=0; i<this.weights.length; i++) {
//			this.weights[i] = 0.1f +(this.weights[i] - w_min)/ampl;
//		}
	}

}
