package app;

import java.util.*;
import java.io.*;

/**
 * Represents a posting for an index term. Consists of an Opus index and a list of Document 
 * indexes.
 * 
 * @author Brandon Bautista
 * @author David Wickizer
 */
public class Posting implements Serializable 
{
	
	private static final long serialVersionUID = 1L;
	private Integer opusIndex;
	private ArrayList<Integer> documentIndexes;
	
	/**
	 * Default constructor. 
	 */
	public Posting()
	{
		
		opusIndex = 0;
		documentIndexes = new ArrayList<>();
	}
	
	/**
	 * Sets a posting by setting the opusIndex, and by adding the docIndex to the documentIndexes.
	 * 
	 * @param opusIndex The Opus index
	 * @param docIndex The Document index
	 */
	public void setPosting(int opusIndex, int docIndex) 
	{
		this.opusIndex = opusIndex;
		documentIndexes.add(docIndex);
	}
	
	/**
	 * Sets the opusIndex
	 * 
	 * @param opusIndex The Opus index
	 */
	public void setOpusIndex(int opusIndex)
	{
		this.opusIndex = opusIndex;
	}
	
	/**
	 * Adds the specified docIndex to the existing documentIndexes for this Posting .
	 * 
	 * @param docIndex The document index being added
	 */
	public void addDocument(int docIndex)
	{
		documentIndexes.add(docIndex);
	}
	
	/**
	 * Getter method for opusIndex.
	 * 
	 * @return The Opus index
	 */
	public Integer getOpusIndex()
	{
		return this.opusIndex;
	}
	
	/**
	 * Getter method for documentIndexes.
	 * 
	 * @return The Document indexes
	 */
	public ArrayList<Integer> getDocumentIndexes()
	{
		return this.documentIndexes;
	}
	
	/**
	 * Checks to see if two objects are equal by comparing their Opus numbers.
	 * 
	 * @param other The object being compared to
	 * @return True if contained, false if not
	 */
	@Override
	public boolean equals(Object other)
	{
		Posting temp = (Posting) other;
		return (this.getOpusIndex() == temp.getOpusIndex());
	}
	
	/**
	 * Returns a String representation of a Posting.
	 * 
	 * @return The String of the Posting
	 */
	@Override
	public String toString() 
	{
		String result = "<" + opusIndex + ", {";
		
		for (Integer index : documentIndexes)
			result += index + ", "; 
		
		// Remove unwanted comma/space at the end
		result = result.substring(0, result.length() - 2);
		result += "}>";
		return result;
	}
}
