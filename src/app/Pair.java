package app;

/**
 * A Pair consists of a shortForm Opus output and a longForm Opus output. Used by the GUI for 
 * displaying appropriate search output.
 * 
 * @author David Wickizer
 *
 */
public class Pair implements Comparable<Pair>
{
	private String shortForm;
	private String longForm;
	private String term;
	private int opus;
	private int document;
	
	/**
	 * Default Constructor.
	 * 
	 * @param shortForm Short form output
	 * @param longForm Long form output
	 */
	public Pair(String shortForm, String longForm, int opus, int document, String term)
	{
		this.shortForm = shortForm;
		this.longForm = longForm;
		this.term = term;
		this.opus = opus;
		this.document = document;
	}
	
	/**
	 * Getter method for shortForm.
	 * 
	 * @return The short form output
	 */
	public String getShortForm()
	{
		return this.shortForm;
	}
	
	/**
	 * Getter method for longForm.
	 * 
	 * @return The long form output
	 */
	public String getLongForm() 
	{
		return this.longForm;
	}
	
	/**
	 * Getter method for opus index.
	 * 
	 * @return The opus index
	 */
	public int getOpus()
	{
		return this.opus;
	}
	
	/**
	 * Getter method for document index.
	 * 
	 * @return The document index
	 */
	public int getDocument()
	{
		return this.document;
	}
	
	/**
	 * Getter method for term.
	 * 
	 * @return The term associated with this output pair
	 */
	public String getTerm() 
	{
		return this.term;
	}

	/**
	 * Overridden hashcode method.
	 * 
	 * @return The hash code value
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + document;
		result = prime * result + opus;
		return result;
	}

	/**
	 * Checks if two Pairs are equal by comparing the document and opus fields.
	 * 
	 * @param obj The object being compared to
	 * @return Returns true if equal. False if not
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Pair other = (Pair) obj;
		if (document != other.document)
			return false;
		if (opus != other.opus)
			return false;
		return true;
	}
	
	/**
	 * Determines if a given String is equal to the Pair's shortForm. If so, objects are equal.
	 * 
	 * @param other The String being used to compare equality
	 * @return Returns true if equal. False if not
	 */
	public boolean equals(String other) 
	{
		return this.getShortForm().equals(other);
	}
	
	/**
	 * Compares two Pair objects by first comparing Opus indexes, and then by comparing Document
	 * indexes.
	 * 
	 * @param other The other pair object being compared to
	 * @return 1 if this Pair is greater than the other Pair. 0 if equal. -1 if less than the 
	 * other
	 */
	@Override
	public int compareTo(Pair other) 
	{
		if (this.opus == other.opus && this.document == other.document)
			return 0;
		if (this.opus > other.opus)
			return 1;
		if (this.opus < other.opus)
			return -1;
		if (this.document > other.document)
			return 1;
		return -1;
	}
}
