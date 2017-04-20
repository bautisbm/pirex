package gui;

/**
 * Represents and interval on a JTextArea to be highlighted.
 * 
 * @author David Wickizer
 *
 */
public class Interval {

	private int startIndex;
	private int endIndex;
	
	/**
	 * Constructs an Interval with a start and end index.
	 * 
	 * @param startIndex
	 * @param endIndex
	 */
	public Interval(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	/**
	 * Getter method for startIndex.
	 * 
	 * @return The start index to be highlighted
	 */
	public int getStartIndex() {
		return this.startIndex;
	}
	
	/**
	 * Getter method for endIndex.
	 * 
	 * @return The end index to be highlighted
	 */
	public int getEndIndex() {
		return this.endIndex;
	}
}

