package app;

import java.io.*;
import java.util.*;

/**
 * This class is responsible for indexing all of the words from all of the documents/opuses, and 
 * for maintaining the inverted index.
 * 
 * @author Brandon Bautista
 * @author David Wickizer
 *
 */
public class Indexer implements Serializable
{
	// Class attributes
	private static final long serialVersionUID = 1L;
	private ArrayList<Opus> documentStore;
	private HashMap<String, TreeMap<Integer, Posting>> index;
	private ArrayList<String> stopWords;
	private int updatedIndexTerms;
	private int updatedPostings;
	private int totalIndexTerms;
	private int totalPostings;
	
	/**
	 * Default constructor. 
	 */
	public Indexer() 
	{
		documentStore = new ArrayList<>();
		index = new HashMap<>();
		stopWords = new ArrayList<>(24);
		updatedIndexTerms = 0;
		updatedPostings = 0;
		totalIndexTerms = 0;
		totalPostings = 0;
		
		// Add words to stopWords
		stopWords.add("a");
		stopWords.add("an");
		stopWords.add("and");
		stopWords.add("are");
		stopWords.add("but");
		stopWords.add("did");
		stopWords.add("do");
		stopWords.add("does");
		stopWords.add("for");
		stopWords.add("had");
		stopWords.add("has");
		stopWords.add("is");
		stopWords.add("it");
		stopWords.add("its");
		stopWords.add("of");
		stopWords.add("or");
		stopWords.add("that");
		stopWords.add("the");
		stopWords.add("this");
		stopWords.add("to");
		stopWords.add("were");
		stopWords.add("which");
		stopWords.add("with");
		stopWords.add("");
	}
	
	/**
	 * Getter method for updatedIndexTerms.
	 * 
	 * @return The number of either removed or new index terms
	 */
	public int getUpdatedIndexTerms()
	{
		return this.updatedIndexTerms;	
	}
	
	/**
	 * Getter method for updatedPostings.
	 * 
	 * @return The number of either removed or new postings
	 */
	public int getUpdatedPostings() 
	{
		return this.updatedPostings;
	}
	
	/**
	 * Getter method for totalIndexTerms.
	 * 
	 * @return The number of total index terms
	 */
	public int getTotalIndexTerms() 
	{
		return this.totalIndexTerms;
	}
	
	/**
	 * Getter method for totalPostings.
	 * 
	 * @return The number of total postings
	 */
	public int getTotalPostings() 
	{
		return this.totalPostings;
	}
	
	/**
	 * Getter method for index.
	 * 
	 * @return The HashMap index
	 */
	public HashMap<String, TreeMap<Integer, Posting>> getIndex()
	{
		return this.index;
	}
	
	/**
	 * Add the terms from the Opus (described by the given Opus number) to the index.
	 * 
	 * @param opusNumber The Opus number of the Opus for which terms are to be added
	 */	
	public void addTerms(Opus opus)
	{
		int opusIndex = opus.getOrdinalNumber();
		ArrayList<Document> documents = opus.getDocuments();

		// Traverse documents within current Opus
		for (int doc = 0; doc < documents.size(); doc++)
		{
			// Tokenize lines of current document into words
			String [] words = tokenizeLines(documents.get(doc).getLines());
			
			// Traverse words of document
			for (int w = 0; w < words.length; w++)
			{
				// Go through words and add word to index terms if it is NOT already present 
				// and the word is NOT a stop word
				if (!index.containsKey(words[w])) 
				{
					Posting posting = new Posting();
					TreeMap<Integer, Posting> postings = new TreeMap<>();
					
					posting.setPosting(opusIndex, doc);
					postings.put(opusIndex, posting);
					
					index.put(words[w], postings);
				} 
				else
				{
					TreeMap<Integer, Posting> currPostings = index.get(words[w]);
					
					// If posting exists for the index term add doc to posting
					if (currPostings.containsKey(opusIndex)) 
					{
						if (!currPostings.get(opusIndex).getDocumentIndexes().contains(doc))
							currPostings.get(opusIndex).addDocument(doc);
					}
					else 
					{
						Posting temp = new Posting();
						temp.setPosting(opusIndex, doc);
						index.get(words[w]).put(opusIndex, temp); // Add new posting
					}
				}
			}
		}	
		
		calculateAdded();
	}
	
	/**
	 * Remove the terms from the Opus (described by the given Opus number) from the index.
	 * 
	 * @param opusIndex The Opus index of the Opus for which terms are to be removed
	 */
	public void removeTerms(Opus opus)
	{		
		int opusIndex = opus.getOrdinalNumber();

		// Work-around for ConcurrentModificationException
		String[] keys = index.keySet().toArray(new String[0]);
		
		for (int i = 0; i < keys.length; i++)
		{
			// Get the current index term postings and remove posting if necessary
			TreeMap<Integer, Posting> map = index.get(keys[i]);
			
			// Update positions of affected postings
			map.remove(opusIndex);
				
			// If map is empty...remove index term and be done
			if (map.isEmpty())
			{
				index.remove(keys[i]);
			} 
			else
			{	
				// Get a Sorted map of all values higher than opusIndex 
				SortedMap<Integer, Posting> sorted = map.tailMap(opusIndex, false);
				
				// Work-around for ConcurrentModificationException
				Integer[] sortedKeys = sorted.keySet().toArray(new Integer[0]);

				// Iterate over sorted map and perform update
				for (int k = 0; k < sortedKeys.length; k++)
				{
					Integer sortedKey = sortedKeys[k];
					Posting sortedVal = sorted.get(sortedKey);
		
					sortedVal.setOpusIndex(sortedVal.getOpusIndex() - 1);
					map.put(sortedKey - 1, sortedVal); // Add decremented entry
					map.remove(sortedKey); // Remove old mapping					
				}
			}
		}
		
		calculateRemoved();
	}
	
	/**
	 * Helper method for calculating the total/new index terms and postings.
	 */
	public void calculateAdded() 
	{
		// Get new/total index terms
		int oldTotalTerms = totalIndexTerms;
		totalIndexTerms = index.size();
		updatedIndexTerms = totalIndexTerms - oldTotalTerms;
	
		// Get new/total postings
		int oldTotalPostings = totalPostings;
		totalPostings = 0;
		for (String key : index.keySet()) totalPostings += index.get(key).size();
		updatedPostings = totalPostings - oldTotalPostings;
	}
	
	/**
	 * Helper method for calculating the total/removed index terms and postings.
	 */
	public void calculateRemoved() 
	{
		// Get removed/total index terms
		int oldTotalTerms = totalIndexTerms;
		totalIndexTerms = index.size();
		updatedIndexTerms = oldTotalTerms - totalIndexTerms;
		
		// Get removed/total postings
		int oldTotalPostings = totalPostings;
		totalPostings = 0;
		for (String key : index.keySet()) totalPostings += index.get(key).size();
		updatedPostings = oldTotalPostings - totalPostings;
	}
	
	/**
	 * Tokenizes lines into words.
	 * 
	 * @param lines The lines to be tokenized
	 * @return The String[] of words
	 */
	public String[] tokenizeLines(LinkedList<String> lines) 
	{
		String block = "";
		String[] words;
		ArrayList<String> alWords;
		int i = 0;
		
		for (String line : lines) block += (line + " "); // Chunk words into String
		block = block.toLowerCase(); // Convert all words to lower case
		words = block.split("--| "); // Split on spaces and "--"
		
		// Iterate through twice due to special situations (i.e. "...end of sentences!?")
		while (i < 2) 
		{
			// Traverse on words and then on characters of each word
			for (int w = 0; w < words.length; w++) 
				for (int c = 0; c < words[w].length(); c++)
					if (!isAlphaNumeric(words[w].charAt(c)) && words[w].length() <= 1)
						words[w] = "";
					else if (!isAlphaNumeric(words[w].charAt(c)) && c == 0)
						words[w] = words[w].substring(1);
					else if (!isAlphaNumeric(words[w].charAt(c)) && c == words[w].length() - 1
							 && words[w].charAt(c) != 0x27)
						words[w] = words[w].substring(0, words[w].length() - 1);
			i++;
		}
		
		// Remove stop words
		alWords = new ArrayList<String>(Arrays.asList(words)); 
		alWords.removeAll(stopWords);
		return alWords.toArray(new String[0]);
	}
	
	/**
	 * Determines if a char is alphanumeric.
	 * 
	 * @param c The char to being evaluated 
	 * @return True or False if depending on if the char is alphanumeric
	 */
	public boolean isAlphaNumeric(char c) 
	{
		return Character.isDigit(c) || Character.isLetter(c);
	}
	
	/**
	 * Returns a String representation of the load summary state of the indexer.
	 * 
	 * @return The String of the load summary state
	 */
	public String loadSummaryState() 
	{
		String result = "";
		
		result += "New index terms: " + updatedIndexTerms + "\n";
		result += "New postings: " + updatedPostings + "\n";
		result += "Total index terms: " + totalIndexTerms + "\n";
		result += "Total postings: " + totalPostings;
		
		return result;
	}
	
	/**
	 * Returns a String representation of the opus summary state of the indexer.
	 * 
	 * @return The String of the opus summary state of the indexer
	 */
	public String opusSummaryState() 
	{
		String result = "";
		
		result += "Index terms: " + totalIndexTerms + "\n";
		result += "Postings: " + totalPostings;
		
		return result;
	}

	/**
	 * Queries the index for a given term.
	 * 
	 * @param term The search term to look for.
	 * @return The results of the search.
	 */
	public TreeMap<Integer, Posting> query(String term)
	{
		return this.index.get(term);
	}

	/**
	 * @return Returns all the terms tracked by this index.
	 */
	public Set<String> getKeys()
	{
		return this.index.keySet();
	}
	
	/**
	 * Returns a String representation of the inverted index.
	 * 
	 * @return The String of the inverted index
	 */
	public String toString()
	{
		String result = "";
		
		for (String key : index.keySet()) 
		{
			result += key + ": ";
			
			Collection<Posting> postings = index.get(key).values();
			Iterator<Posting> iter = postings.iterator();
			
			while (iter.hasNext())
				result += iter.next().toString() + ", ";
			
			// Remove unwanted comma/space at the end
			result = result.substring(0, result.length() - 2);
			result += "\n";
		}
		return result;
	}
}
