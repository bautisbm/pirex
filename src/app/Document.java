package app;
import java.io.*;
import java.util.LinkedList;

/**
 * Represents a Document within an Opus. Also referred to as "paragraph".
 * 
 * @author David Wickizer
 */
public class Document implements Serializable 
{
	// Attributes
	private static final long serialVersionUID = 1L;
	private LinkedList<String> lines;
	private int documentID;
	
	/**
	 * Constructor for Document.
	 * 
	 * @param lines The lines of a Document
	 */
	public Document(LinkedList<String> lines) 
	{
		this.lines = lines;
	}
	
	/**
	 * Getter method for documentID.
	 * 
	 * @return The Document ID
	 */
	public int getDocumentID()
	{
		return this.documentID;
	}
	
	/**
	 * Getter method for lines.
	 * 
	 * @return The lines of the Document
	 */
	public LinkedList<String> getLines()
	{
		return this.lines;
	}
	
	/**
	 * Setter method for Document ID.
	 *  
	 * @param number The number to set the Document ID to
	 */
	public void setDocumentID(int number) 
	{
		this.documentID = number;
	}
	
	/**
	 * Returns a String representation of the Document in either short form or
	 * long form.
	 * 
	 * @param shortForm Determines which form should be returned 
	 * @return A String representation of the Document
	 */
	public String toString(boolean shortForm) 
	{
		// Variables
		String result = "";
		
		// Handle short form: Display first line of Document
		if (shortForm)
			result += lines.get(0);
		else 
		{ 
			// Print all lines
			for (int i = 0; i < lines.size(); i++)
				result += lines.get(i) + "\n";
		}
			
		return result;
	}
}
