package app;
import java.io.*;
import java.util.*;

/**
 * Represents the text taken from a Source in a formatted manner. An Opus
 * contains a title, author, ordinal number, documents (paragraphs), 
 * words, raw text, a file name, and file pointer. 
 * 
 * @author David Wickizer
 */
public class Opus implements Serializable 
{
	// Opus Attributes 
	private static final long serialVersionUID = 1L;
	private LinkedList<String> lines;
	private String title, author, fileName;
	private ArrayList<Document> documents;
	private int ordinalNumber;
	
	/**
	 * Constructor for Opus class. 
	 * 
	 * @param title The title of the Opus
	 * @param author The author of the Opus
	 * @param fileName The fileName of the file related to the Opus
	 * @param lines The lines of the Opus
	 */
	public Opus (String title, String author, String fileName, LinkedList<String> lines) 
	{
		this.title = title;
		this.author = author;
		this.fileName = fileName;
		this.lines = lines;
		this.documents = extractDocuments();
	
		// Set Documents IDs
		for (int i = 0; i < this.documents.size(); i++)
			this.documents.get(i).setDocumentID(i);
	}
	
	/**
	 * Helper method for extracting the documents of an Opus.
	 * 
	 * @return The documents
	 */
	public ArrayList<Document> extractDocuments()
	{
		ArrayList<Document> docs = new ArrayList<>();
		LinkedList<String> documentLines = new LinkedList<>();
		boolean flag = false;
		
		// Create Documents from lines
		for (String line : lines) {

			if (line.equals("") && flag) 
			{
				docs.add(new Document(deepCopy(documentLines)));
				documentLines.clear();
				flag = false;
			} 
			else if (!line.equals("")) 
			{
				documentLines.add(line);
				flag = true;
			}	
		}
		
		// Handle no "\n's"
		if (flag) docs.add(new Document(documentLines));
		return docs;
	}
	
	/**
	 * Helper method for deep copying a list.
	 * 
	 * @param list The list to be deep copied
	 * @return The copied list
	 */
	public LinkedList<String> deepCopy(LinkedList<String> list)
	{
		LinkedList<String> copy = new LinkedList<>();
		for (String item: list)
			copy.add(item);
		return copy;
	}
	
	/**
	 * This method takes in the index position of a Document within an Opus and returns
	 * the Document associated with the index position.
	 * 
	 * @param index The index of the document within the Opus
	 * @return The Document associated with the index position
	 */
	public Document getDocument(int index) 
	{
		return documents.get(index);
	}
	
	/**
	 * Getter method for title.
	 * 
	 * @return The title of the Opus
	 */
	public String getTitle() 
	{
		return this.title;
	}
	
	/**
	 * Getter method for author.
	 * 
	 * @return The author of the Opus
	 */
	public String getAuthor() 
	{
		return this.author;
	}
	
	/**
	 * Getter method for title.
	 * 
	 * @return The ordinal number of the Opus
	 */
	public int getOrdinalNumber() 
	{
		return this.ordinalNumber;
	}
	
	/**
	 * Getter method for File name.
	 * 
	 * @return The File name associated with the Opus
	 */
	public String getFileName()
	{
		return this.fileName;
	}
	
	/**
	 * Getter method for documents.
	 * 
	 * @return The documents of the Opus
	 */
	public ArrayList<Document> getDocuments()
	{
		return this.documents;
	}
	
	/**
	 * Getter method for lines.
	 * 
	 * @return The lines of the Opus
	 */
	public LinkedList<String> getLines() 
	{
		return this.lines;
	}
	
	/**
	 * Setter method for title.
	 * 
	 * @param title The updated title
	 */
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	/**
	 * Setter method for author.
	 * 
	 * @param author The updated author
	 */
	public void setAuthor(String author) 
	{
		this.author = author;
	}
	
	/**
	 * Setter method for ordinalNumber.
	 * 
	 * @param number The updated ordinalNumber
	 */
	public void setOrdinalNumber(int number) 
	{
		this.ordinalNumber = number;
	}
	
	/**
	 * Returns a String representation in short form of the Author, Title, Document
	 * ID number, and the first line of the Document specified.
	 * 
	 * @param documentID Specifies which document is to be printed in short form
	 * @return A String representation of the short form output
	 */
	public String shortForm(int documentID) 
	{
		String result = "";
		
		result += this.author + "  " + this.title + "   " + documentID + "  ";
		result += this.documents.get(documentID).toString(true);
		
		return result;
	}
	
	/**
	 * Returns a String representation in long form of the Document specified
	 * 
	 * @param documentID Specifies which document is to be printed in short form
	 * @return A String representation of the long form output
	 */
	public String longForm(int documentID) 
	{
		return this.documents.get(documentID).toString(false);
	}
	
	/**
	 * Returns a String representation of the Opus information involved in the Load Summary State.
	 * 
	 * @return A String representation of the Opus information involved in the Load Summary State
	 */
	public String loadSummaryState() 
	{ 
		String result = "";
		result += "Opus: " + this.fileName + "\n";
		result += "Title: " + this.title + "\n";
		result += "Author: " + this.author + "\n";
		result += "Opus Size: " + this.documents.size() + " documents\n";
		result += "Opus Number: " + this.ordinalNumber;
		return result;
	}
	
	/**
	 * Returns a String representation of the Opus information involved in the Opus Summary State.
	 * 
	 * @return A String representation of the Opus information involved in the Opus Summary State
	 */
	public String opusSummaryState()
	{ 
		String result = "";
		
		result += "Opus " + this.ordinalNumber + ": " + this.author + "     " + this.title;
		result += "     " + this.documents.size() + " documents\n";
		result += "            " + this.fileName;
		
		return result;
	}
	
	/**
	 * Returns a String representation of the Opus.
	 * 
	 * @return A String representation of the Opus 
	 */
	public String toString() 
	{
		String result = "";
		
		result += "Title: " + this.title + "\n";
		result += "Author: " + this.author + "\n";
		result += "Ordinal Number: " + this.ordinalNumber + "\n";
		result += "Documents in this Opus: " + documents.size() + "\n";

		return result;
	}
}
