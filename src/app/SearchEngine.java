package app;
import java.util.*;

/**
 * Responsible for handling all search operations. Communicates with the DocumentStore 
 * and the SourceProcessor.
 * 
 * @author David Wickizer
 *
 */
public class SearchEngine
{
	private Store store;
	private ArrayList<String> stopWords;
	private TreeSet<String> prevTerms;
	
	/**
	 * Default constructor. Add all desired stop words.
	 */
	public SearchEngine(Store store)
	{
		this.store = store;
		
		// Add words to stopWords
		stopWords = new ArrayList<>(24);
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
	 * Retrieves the appropriate documents associated with the search terms.
	 * 
	 * @param tokens The search terms
	 * @return The output pairs to be output
	 */
	public TreeSet<Pair> retrieveDocuments(String searchTerms) 
	{
		TreeSet<Pair> pairs = new TreeSet<>();
		TreeSet<Pair> base = new TreeSet<>();
		prevTerms = new TreeSet<>();
		int count = 0;
		
		String[] tokens = tokenizeSearchTerms(searchTerms);
		
		// Search on index and retrieve postings 
		for (String token : tokens)
		{ 
			// Determine token type
			if (token.contains("~")) pairs = negationExpression(token);
			else if (token.contains("^")) pairs = adjacencyExpression(token);
			else if (token.contains("*")) pairs = wildCardExpression(token);
			else if (token.contains("?")) pairs = wildCardExpression(token);
			else pairs = basicExpression(token);
				
			// Add terms to desired list (Used for highlighting output later on)
			if (!token.contains("~") && !token.contains("^"))
				for (Pair pair : pairs) prevTerms.add(pair.getTerm());
			
			// Return empty set if at any point a token is not found in the index
			if (pairs.isEmpty()) return new TreeSet<>(); 
			if (count == 0) base = pairs;
			base.retainAll(pairs);
			count++;
		}
		return base;
	}
	
	/**
	 * Gets all postings not associated with the token.
	 * 
	 * @param token The token to be run against the inverted index
	 * @return A set of pair output objects representing the postings
	 */
	public TreeSet<Pair> negationExpression(String token)
	{
		TreeSet<Pair> pairs = new TreeSet<>();
		TreeSet<Pair> negated = new TreeSet<>();

		// Strip token of tilde
		token = token.substring(1);
		
		// Get output pairs
		if (token.contains("^")) pairs = adjacencyExpression(token);
		else if (token.contains("*")) pairs = wildCardExpression(token);
		else if (token.contains("?")) pairs = wildCardExpression(token);
		else pairs = basicExpression(token);
		
		// Get all pairs and remove duplicates
		for (String key : store.indexer.getKeys()) negated.addAll(basicExpression(key));
		
		// Remove unwanted pairs
		negated.removeAll(pairs);
		return negated;
	}
	
	/**
	 * Gets all postings associated with the phrase.
	 * 
	 * @param token The phrase
	 * @return A set of pair output objects representing the postings
	 */
	public TreeSet<Pair> adjacencyExpression(String token)
	{
		TreeSet<Pair> pairs = new TreeSet<>();
				
		String[] terms = token.split("\\^");
		String phrase = token.replaceAll("\\^", " ");
		TreeSet<Pair> tempPairs = new TreeSet<>();
				
		// Find first non stop word to use in search
		for (String term : terms) 
		{
			if (!stopWords.contains(term))
			{
				tempPairs = basicExpression(term);
				break;
			}			
		}
				
		// Determine if phrase is in any of the documents
		for (Pair pair : tempPairs) 
			if (pair.getLongForm().contains(phrase)) pairs.add(pair);
		prevTerms.add(phrase); // Add phrase to prevTerms for later highlighting
		return pairs;
	}
	
	/**
	 * Gets all postings associated with wildcard expression.
	 * 
	 * @param token The wildcard
	 * @return A set of pair output objects representing the postings
	 */
	public TreeSet<Pair> wildCardExpression(String token)
	{
		TreeSet<Pair> pairs = new TreeSet<>();

		// Determine wild card
		if (token.contains("*") && token.contains("?")) 
			pairs = qMarkExpression(token.substring(0,  token.length() - 1), true);
		else if (token.contains("*")) 
			pairs = starExpression(token.substring(0,  token.length() - 1));
		else 
			pairs = qMarkExpression(token, false);

		return pairs;
	}

	/**
	 * Gets all postings associated with star wildcard expression.
	 * 
	 * @param token The star wildcard
	 * @return A set of pair output objects representing the postings
	 */
	public TreeSet<Pair> starExpression(String token)
	{
		TreeSet<Pair> pairs = new TreeSet<>();
		TreeSet<Pair> buffer = new TreeSet<>();
		
		// Any key that starts with token needs to be retrieved
		for (String key : store.indexer.getKeys()) 
			if (key.startsWith(token))
			{
				buffer = basicExpression(key);
				
				// Account for terms that will potentially be lost
				if (pairs.containsAll(buffer)) prevTerms.add(key);
				pairs.addAll(buffer);
			}
		
		return pairs;		
	}
	
	/**
	 * Gets all postings associated with question mark wildcard expression.
	 * 
	 * @param token The question mark wildcard
	 * @return A set of pair output objects representing the postings
	 */
	public TreeSet<Pair> qMarkExpression(String token, boolean containsStar)
	{
		TreeSet<Pair> pairs = new TreeSet<>();
		TreeSet<Pair> buffer = new TreeSet<>();
		
		// Find potential matches in index
		for (String key : store.indexer.getKeys())
		{
			boolean equal = true;
			if (token.length() == key.length() || (containsStar && token.length() <= key.length()))
			{
				// Compare Strings char by char
				for (int i = 0; i < token.length(); i++)
				{
					if (token.charAt(i) != '?')
					{
						if (token.charAt(i) != key.charAt(i))
						{
							equal = false;
							break; 
						}
					}
				}
			} 
			else 
			{
				equal = false;
			}
			
			if (equal)
			{
				// Account for terms that will potentially be lost
				buffer = basicExpression(key);
				if (pairs.containsAll(buffer)) prevTerms.add(key);
				pairs.addAll(basicExpression(key));
			}
		}
		return pairs;
	}
	
	/**
	 * Gets all postings associated with the basic search term.
	 * 
	 * @param token The basic search term
	 * @return A set of pair output objects representing the postings
	 */
	public TreeSet<Pair> basicExpression(String token)
	{
		TreeMap<Integer, Posting> map = new TreeMap<>(); 
		Collection<Posting> postings = new ArrayList<>();
		Opus opus;
		ArrayList<Integer> documents;
		TreeSet<Pair> pairs = new TreeSet<>();
		Pair pair;
		
		map = store.indexer.query(token);
			
		// Get postings if term is found
		if (map != null) 
		{
			postings = map.values();
				
			// Traverse postings 
			for (Posting posting : postings)
			{
				documents = posting.getDocumentIndexes();
				opus = store.documentStore.get(posting.getOpusIndex());
					
				// Traverse documents
				for (Integer doc : documents) 
				{
					pair = new Pair(opus.shortForm(doc), opus.longForm(doc),
							        posting.getOpusIndex(), doc, token); 
					pairs.add(pair); 
				}
			}
		}
		return pairs;
	}
	
	/**
	 * Helper method for tokenizing the search terms into a usable String array.
	 * 
	 * @param searchTerms The String to be tokenized
	 * @return The String array of tokenized search terms
	 */
	public String[] tokenizeSearchTerms(String searchTerms)
	{
		String[] words;
		ArrayList<String> alWords;
		boolean singleQuoteFound = false;
		
		searchTerms = searchTerms.toLowerCase(); // Convert all words to lower case
		words = searchTerms.split(" "); // Split on spaces
		
		// Traverse on words and then on characters of each word
		for (int w = 0; w < words.length; w++) 
			for (int c = 0; c < words[w].length(); c++)
			{
				char curr = words[w].charAt(c);
					
				// If word is single char that is non alphanumeric...remove it
				if (!isAlphaNumeric(curr) && words[w].length() <= 1) 
				{
					words[w] = "";
						
				
				} // If first char is not permitted...remove it 
				else if (c == 0 && !permittedFirst(curr)) 
				{
					// If char is "'" set flag
					if (curr == 0x27) singleQuoteFound = true;
						
					// Remove unwanted char
					words[w] = words[w].substring(1);
					c--;	
				
				} // If middle char is not permitted...remove it 
				else if (c > 0 && c < words[w].length() - 1 && !permittedMid(curr)) 
				{
					String beg = words[w].substring(0, c);
					String end = words[w].substring(c + 1, words[w].length());
					words[w] = beg + end;
					c--;	
				
				} // If last char is not permitted...remove it 
				else if (c == words[w].length() - 1 && !permittedLast(curr)) 
				{
					words[w] = words[w].substring(0, words[w].length() - 1);
					c--;	
				
				} // Handle potential single quote issue at last char
				else if (c == words[w].length() - 1 && curr == 0x27 && singleQuoteFound)
				{
					words[w] = words[w].substring(0, words[w].length() - 1);
					c--;	
				}
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
	 * Determines if a char is permitted on the front of a word.
	 * 
	 * @param c The char to being evaluated 
	 * @return True or False if depending on if the char is permitted
	 */
	public boolean permittedFirst(char c)
	{
		return isAlphaNumeric(c) || c == '~' || c == '?' || c == '.';
	}
	
	/**
	 * Determines if a char is permitted in the middle of a word.
	 * 
	 * @param c The char to being evaluated 
	 * @return True or False if depending on if the char is permitted
	 */
	public boolean permittedMid(char c) 
	{
		return isAlphaNumeric(c) || c == '-' || c == '?' || c == '.'
								 || c == '^' || c == 0x27 || c == '*';
	}
	
	/**
	 * Determines if a char is permitted on the end of a word.
	 * 
	 * @param c The char to being evaluated 
	 * @return True or False if depending on if the char is permitted
	 */
	public boolean permittedLast(char c) 
	{
		return isAlphaNumeric(c) || c == '?' || c == '*' || c == 0x27;
	}
	
	/**
	 * Getter method for prevTerms.
	 * 
	 * @return The previous search terms. Wildcard terms provide all potential search terms.
	 */
	public TreeSet<String> getPreviousTerms() 
	{
		return this.prevTerms;
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
