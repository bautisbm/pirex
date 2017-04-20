package app;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.*;

/**
 * This layer is responsible for extracting documents from a text file, having
 * the Indexer index them, and having the Store update its inverted index
 * and document store.
 * 
 * @author David Wickizer
 * @author Zach Berman
 *
 */
public class SourceProcessor
{
	public Store store;
	private int prevOpus;

	/**
	 * Constructor for SourceProcessor.
	 */
	public SourceProcessor(Store store) 
	{
		this.store = store;
	}
  
	/**
	 * Extracts a title from a GutenBerg file.
	 * 
	 * @param fileName The name of the file to extract from
	 * @return The title
	 */
	public String extractTitle(String fileName)
	{
		LinkedList<String> lines = readHeader(fileName);
    
		// Search lines for "Title:" and return what comes after it
		for (String line : lines) 
			if (line.contains("Title:")) 
				return line.substring(7);
		return null; // No title found
	}

  
	/**
	 * Extracts an author from a GutenBerg file.
	 * 
	 * @param fileName The name of the file to extract from
	 * @return The author
	 */
	public String extractAuthor(String fileName)
	{
		LinkedList<String> lines = readHeader(fileName);
    
		// Search lines for "Author:" and return what comes after it
		for (String line : lines) 
			if (line.contains("Author:")) 
				return line.substring(8);
		return null; // No author found
	}
  
	/**
	 * Extracts an Opus from a file and adds it to the Document Store.
	 * 
	 * @param fileName The name of the file to extract from
	 * @param title The current title in the text box on the GUI
	 * @param author The current author in the text box on the GUI
	 * @return The Opus
	 */
	public Opus extractOpus(String fileName, String title, String author)
	{
		LinkedList<String> lines = readFile(fileName);
		Opus opus = new Opus(title, author, fileName, lines);
    
		store.documentStore.add(opus); // Add Opus to Document Store
		opus.setOrdinalNumber(store.documentStore.size() - 1); // Set ordinal number to position 
		prevOpus = opus.getOrdinalNumber(); 
		store.indexer.addTerms(opus); // Update the index to be in sync
		return opus;
	}
  
	/**
	 * Removes the specified Opus from the documentStore, updates all of the ordinal numbers
	 * of the affected Opuses within the documentStore, reserializes the updated documentStore,
	 * and updates the inverted index to match the updated documentStore.
	 * 
	 * @param opusIndex The index of the Opus to be removed
	 * @return The Opus that was removed
	 */
	public Opus removeOpus(int opusIndex) 
	{
		if (store.documentStore.size() > 0) 
		{
			// Remove Opus from documentStore
			Opus removed = store.documentStore.remove(opusIndex);
    
			// Update the ordinal numbers of Opuses affected by change
			for (int i = opusIndex; i < store.documentStore.size(); i++)
			{
				Opus curr = store.documentStore.get(i);
				curr.setOrdinalNumber(curr.getOrdinalNumber() - 1); // Decrement ordinal number
			}
      
			store.indexer.removeTerms(removed); // Update the index to be in sync 
			return removed;
		}
		return null;
	}
  
	/**
	 * Call the loadSummaryState() methods. Used for being displayed on the GUI.
	 * 
	 * @return The String result of the method calls
	 */
	public String loadSummary()
	{ 
		return store.documentStore.get(prevOpus).loadSummaryState() + "\n" + store.indexer.loadSummaryState();
	}
  
	/**
	 * Call the opusSummaryState() methods. Used for being displayed on the GUI.
	 * 
	 * @return The String result of the method calls
	 */
	public String opusSummary()
	{ 
		String result = "";
		for (Opus opus : store.documentStore)
			result += opus.opusSummaryState() + "\n";
		result += "\n" + store.indexer.opusSummaryState();
		return result;
	}
  
	/**
	 * Helper method for reading the header of a file so that the author and title can be 
	 * extracted.
	 * 
	 * @param fileName The name of the file to be read 
	 * @return The header of the file 
	 */
	public LinkedList<String> readHeader(String fileName)
	{
		// Create file from fileName
		File file = new File(fileName);
		LinkedList<String> lines = new LinkedList<>();
		String line;
    
		// Read contents of file up to "*** START OF"
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
        
			// Read contents of file to rawText
			while (!(line = in.readLine()).contains("*** START OF")) lines.add(line);
			in.close();
        
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * Helper method for reading contents of a file (excluding header and tail).
	 * @param fileName The name of the file to be read
	 * @return The lines of the file
	 */
	public LinkedList<String> readFile(String fileName) 
	{
		// Create file from fileName
		File file = new File(fileName);
		LinkedList<String> lines = new LinkedList<>();
		String line = "";
    	boolean copyLines = false;
    
    	try 
    	{
    		BufferedReader in = new BufferedReader(new FileReader(file));
      
    		// Only read contents of file from "*** START OF.." to "*** END OF.."
    		while((line = in.readLine()) != null)
    		{
    			if (line.contains("*** START OF")) copyLines = true;
    			if (line.contains("*** END OF")) break;
    			if (copyLines) lines.add(line);
    		} 
      
    		in.close();
    		lines.remove(0); // Remove first unwanted line
    	} 
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return lines;
	}
    
	/**
	 * Getter method for documentStore.
	 * 
	 * @return The documentStore
	 */
	public ArrayList<Opus> getDocumentStore()
	{
		return this.store.documentStore;
	}
  
	/**
	 * Getter method for indexer.
	 * 
	 * @return The documentStore
	 */
	public Indexer getIndexer()
	{
		return this.store.indexer;
	}
  
	/**
	 * Sets a new location for the Data Store.
	 * 
	 * @param path The new path to the Data Store
	 */
	public void setDataStoreLocation(String path)
	{
		store.saveToFile();
		store.setDataStoreLocation(path);
	}
}