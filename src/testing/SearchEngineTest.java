package testing;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.*;
import app.SourceProcessor;
import app.SearchEngine;
import app.Pair;
import app.Store;

/**
 * Test suite for SearchEngine
 * 
 * @author David Wickizer
 *
 */
public class SearchEngineTest 
{
	/**
	 * Test for tokenizeSearchTerms(). Tests a simple list of words.
	 */
	@Test
	public void testTokenizeSearchTermsSimple() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		
		String[] terms = se.tokenizeSearchTerms("hello goodbye hi");
		String[] expected = {"hello", "goodbye", "hi"};
		
		assertArrayEquals(expected, terms);
	}
	
	/**
	 * Test for tokenizeSearchTerms(). Tests a single simple word.
	 */
	@Test
	public void testTokenizeSearchTermsSingle() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		
		String[] terms = se.tokenizeSearchTerms("hello");
		String[] expected = {"hello"};
		
		assertArrayEquals(expected, terms);
	}
	
	/**
	 * Test for tokenizeSearchTerms(). Tests multiple terms simple and advanced.
	 */
	@Test
	public void testTokenizeSearchTermsAdvanced() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		
		String[] terms = se.tokenizeSearchTerms("d a * ^r@!!@@@!emove ~keep ?eep *eep .51 @@co#ol## ra*dom ra?dom 5.5 twenty-six cat's dogs' 'fish' 'goats'' 'ghost's' fine this^is^ok end en* en? 52. get- ~yell*^jac?et");
		String[] expected = {"d", "remove", "~keep", "?eep", "eep", ".51", "cool", "ra*dom", "ra?dom", "5.5", "twenty-six", "cat's", "dogs'", "fish", "goats'", "ghost's", "fine", "this^is^ok", "end", "en*", "en?", "52", "get", "~yell*^jac?et"};
	
		assertArrayEquals(expected, terms);
	}
	
	/**
	 * Test for a basic query.
	 */
	@Test
	public void testBasicQuery() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		TreeSet<Pair> pairs = new TreeSet<>();
		ArrayList<String> expected = new ArrayList<>();
		
		String title = "Opus 0";
		String author = "0 Author";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		pairs = se.retrieveDocuments("red barn");
		
		expected.add("0 Author  Opus 0	0  red barn");
		expected.add("2 Author  Opus 2	1  red barn hen purple");
		
		int i = 0;
		for (Pair pair : pairs) 
		{
			assertEquals(expected.get(i), pair.getShortForm());
			i++;
		}

	}
	
	/**
	 * Test for a getPreviousTerms().
	 */
	@Test
	public void testGetPreviousTerms() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		
		String title = "Opus 0";
		String author = "0 Author";
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);

		se.retrieveDocuments("red");
		
		TreeSet<String> actual = se.getPreviousTerms();
		
		for (String item : actual)
			item.equals("red");
		

	}
	
	/**
	 * Test for an empty query.
	 */
	@Test
	public void testEmptyQuery() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		TreeSet<Pair> pairs = new TreeSet<>();
		
		String title = "Opus 0";
		String author = "0 Author";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		pairs = se.retrieveDocuments(" ");
		 
		assertEquals(0, pairs.size());
	}

	/**
	 * Test for an advanced query using negation only with a single term.
	 */
	@Test
	public void testAdvancedQueryNegationSingleTerm() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		ArrayList<String> expected = new ArrayList<>();
		TreeSet<Pair> pairs = new TreeSet<>();
		
		String title = "Opus 0";
		String author = "0 Author";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		pairs = se.retrieveDocuments("~red");
		
		expected.add("0 Author  Opus 0	2  orange fox orange fox");
		expected.add("1 Author  Opus 1	0  blue barn");
		expected.add("1 Author  Opus 1	2  purple");
		expected.add("1 Author  Opus 1	3  purple purple");
		expected.add("1 Author  Opus 1	4  yellow tail");
		expected.add("2 Author  Opus 2	0  purple rain");
		expected.add("2 Author  Opus 2	2  blue purple");
		expected.add("2 Author  Opus 2	3  YEET");
		expected.add("2 Author  Opus 2	4  YEET");
		
		int i = 0;
		for (Pair pair : pairs)
		{
			assertEquals(expected.get(i), pair.getShortForm());
			i++;
		}
	}
	
	/**
	 * Test for an advanced query using negation only with multiple terms.
	 */
	@Test
	public void testAdvancedQueryNegationMultipleTerms() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		ArrayList<String> expected = new ArrayList<>();
		TreeSet<Pair> pairs = new TreeSet<>();
		
		String title = "Opus 0";
		String author = "0 Author";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);

		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, "Opus 1", "1 Author");
		
		pairs = se.retrieveDocuments("~red ~y?llow");
		 
		expected.add("0 Author  Opus 0	2  orange fox orange fox");
		expected.add("1 Author  Opus 1	0  blue barn");
		expected.add("1 Author  Opus 1	2  purple");
		expected.add("1 Author  Opus 1	3  purple purple");

		int i = 0;
		for (Pair pair : pairs)
		{
			assertEquals(expected.get(i), pair.getShortForm());
			i++;
		}
	}
	
	/**
	 * Test for an advanced query using adjacency only.
	 */
	@Test
	public void testAdvancedQueryAdjacency() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		ArrayList<String> expected = new ArrayList<>();
		TreeSet<Pair> pairs = new TreeSet<>();
		
		String title = "Opus 0";
		String author = "0 Author";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, "Opus 1", "1 Author");
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, "Opus 2", "2 Author");
		
		expected.add("0 Author  Opus 0	0  red barn");
		expected.add("2 Author  Opus 2	1  red barn hen purple");

		pairs = se.retrieveDocuments("red^barn");
		 
		int i = 0;
		for (Pair pair : pairs) 
		{
			assertEquals(expected.get(i), pair.getShortForm());
			i++;
		}
	}
	
	/**
	 * Test for an advanced query using the wild card character '*'.
	 */
	@Test
	public void testAdvancedQueryWildStar() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		ArrayList<String> expected = new ArrayList<>();
		TreeSet<Pair> pairs = new TreeSet<>();
		
		String title = "Opus 0";
		String author = "0 Author";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, "Opus 1", "1 Author");
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, "Opus 2", "2 Author");
		
		pairs = se.retrieveDocuments("r* bl*");
		 
		expected.add("0 Author  Opus 0	1  blue red");
		expected.add("1 Author  Opus 1	1  blue hen red");
		expected.add("2 Author  Opus 2	0  purple rain ");
		
		int i = 0;
		for (Pair pair : pairs)
		{
			assertEquals(expected.get(i), pair.getShortForm());
			i++;
		}
	}
	
	/**
	 * Test for an advanced query using the wild card character '?'.
	 */
	@Test
	public void testAdvancedQueryWildQuestion() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		ArrayList<String> expected = new ArrayList<>();
		TreeSet<Pair> pairs = new TreeSet<>();
		
		String title = "Opus 0";
		String author = "0 Author";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, "Opus 1", "1 Author");
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, "Opus 2", "2 Author");
		
		pairs = se.retrieveDocuments("h?n or?nge");
		
		expected.add("1 Author  Opus 1	1  blue hen red");
		expected.add("2 Author  Opus 2	1  red barn hen purple");
		expected.add("0 Author  Opus 0	2  orange fox");

		int i = 0;
		for (Pair pair : pairs)
		{
			assertEquals(expected.get(i), pair.getShortForm());
			i++;
		}
	}
	
	/**
	 * Test for an advanced query using all advanced aspects with a single term.
	 */
	@Test
	public void testAdvancedQueryCombinedSingleTerm() 
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		ArrayList<String> expected = new ArrayList<>();
		TreeSet<Pair> pairs = new TreeSet<>();
		
		String title = "Opus 0";
		String author = "0 Author";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		pairs = se.retrieveDocuments("~or?n*");
		 
		expected.add("0 Author  Opus 0	0  red barn");
		expected.add("0 Author  Opus 0	1  blue red");
		
		int i = 0;
		for (Pair pair : pairs) 
		{
			assertEquals(expected.get(i), pair.getShortForm());
			i++;
		}
	}
	
	/**
	 * Test for an advanced query using all advanced aspects with multiple terms.
	 */
	@Test
	public void testAdvancedQueryCombinedMultipleTerms()
	{
		SearchEngine se = new SearchEngine(new Store(-1));
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		ArrayList<String> expected = new ArrayList<>();
		TreeSet<Pair> pairs = new TreeSet<>();
		
		String title = "Opus 0";
		String author = "0 Author";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, "Opus 1", "1 Author");
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, "Opus 2", "2 Author");
		
		pairs = se.retrieveDocuments("~orange^fox^orange b*");
		 
		expected.add("0 Author  Opus 0	0  red barn");
		expected.add("0 Author  Opus 0	1  blue red");
		expected.add("1 Author  Opus 1	0  blue barn");
		expected.add("1 Author  Opus 1	1  blue hen red");
		expected.add("2 Author  Opus 2	0  purple rain");
		expected.add("2 Author  Opus 2	1  red barn hen purple");
		expected.add("2 Author  Opus 2	2  blue purple");

		int i = 0;
		for (Pair pair : pairs) 
		{
			assertEquals(expected.get(i), pair.getShortForm());
			i++;
		}
	}
}
