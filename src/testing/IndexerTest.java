package testing;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import app.Indexer;
import app.SourceProcessor;
import app.Store;

/**
 * Test suite for Indexer.
 * 
 * @author David Wickizer
 *
 */
public class IndexerTest
{
	/**
	 * Test for performing one add operation
	 */
	@Test
	public void testAddTermsSimple() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		String expected = "red: <0, {0, 1}>\n"
						+ "orange: <0, {2}>\n"
						+ "blue: <0, {1}>\n"
						+ "barn: <0, {0}>\n"
						+ "fox: <0, {2}>\n";
	
		assertEquals(expected, sp.getIndexer().toString());
	}
	
	/**
	 * Test for performing multiple add operation
	 */
	@Test
	public void testAddTermsMultiple() 
	{
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		String expected = "rain: <2, {0}>\n"
						+ "yeet: <2, {3, 4}>\n"
						+ "tail: <1, {4}>\n"
						+ "yellow: <1, {4}>\n"
						+ "fox: <0, {2}>\n"
						+ "red: <0, {0, 1}>, <1, {1}>, <2, {1}>\n"
						+ "orange: <0, {2}>\n"
						+ "hen: <1, {1}>, <2, {1}>\n"
						+ "blue: <0, {1}>, <1, {0, 1}>, <2, {2}>\n"
						+ "bob: <2, {0}>\n"
						+ "barn: <0, {0}>, <1, {0}>, <2, {1}>\n"
						+ "purple: <1, {2, 3}>, <2, {0, 1, 2}>\n"
						+ "sagot: <2, {0}>\n";
						
		assertEquals(expected, sp.getIndexer().toString());
	}
	
	/**
	 * Test the removal of the first item
	 */
	@Test
	public void testRemoveBeginning() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(0);
		
		String expected = "rain: <1, {0}>\n"
						+ "yeet: <1, {3, 4}>\n"
						+ "tail: <0, {4}>\n"
						+ "yellow: <0, {4}>\n"
						+ "red: <0, {1}>, <1, {1}>\n"
						+ "hen: <0, {1}>, <1, {1}>\n"
						+ "blue: <0, {0, 1}>, <1, {2}>\n"
						+ "bob: <1, {0}>\n"
						+ "barn: <0, {0}>, <1, {1}>\n"
						+ "purple: <0, {2, 3}>, <1, {0, 1, 2}>\n"
						+ "sagot: <1, {0}>\n";
				
		assertEquals(expected, sp.getIndexer().toString());
	}
	
	/**
	 * Test the removal of a middle item
	 */
	@Test
	public void testRemoveMiddle()
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(1);
		
		String expected = "rain: <1, {0}>\n"
						+ "yeet: <1, {3, 4}>\n"
						+ "fox: <0, {2}>\n"
						+ "red: <0, {0, 1}>, <1, {1}>\n"
						+ "orange: <0, {2}>\n"
						+ "hen: <1, {1}>\n"
						+ "blue: <0, {1}>, <1, {2}>\n"
						+ "bob: <1, {0}>\n"
						+ "barn: <0, {0}>, <1, {1}>\n"
						+ "purple: <1, {0, 1, 2}>\n"
						+ "sagot: <1, {0}>\n";
		
		assertEquals(expected, sp.getIndexer().toString());
	}
	
	/**
	 * Test the removal of the end item
	 */
	@Test
	public void testRemoveEnd() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(2);
		
		String expected = "tail: <1, {4}>\n"
						+ "yellow: <1, {4}>\n"
						+ "fox: <0, {2}>\n"
						+ "red: <0, {0, 1}>, <1, {1}>\n"
						+ "orange: <0, {2}>\n"
						+ "hen: <1, {1}>\n"
						+ "blue: <0, {1}>, <1, {0, 1}>\n"
						+ "barn: <0, {0}>, <1, {0}>\n"
						+ "purple: <1, {2, 3}>\n";

		assertEquals(expected, sp.getIndexer().toString());
	}
	
	/**
	 * Test the removal of the end item
	 */
	@Test
	public void testRemoveRemove() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		

		
		String expected = "rain: <0, {0}>\n"
						+ "yeet: <0, {3, 4}>\n"
						+ "red: <0, {1}>\n"
						+ "hen: <0, {1}>\n"
						+ "blue: <0, {2}>\n"
						+ "bob: <0, {0}>\n"
						+ "barn: <0, {1}>\n"
						+ "purple: <0, {0, 1, 2}>\n"
						+ "sagot: <0, {0}>\n";

		sp.removeOpus(1);
		sp.removeOpus(0);
		
		assertEquals(expected, sp.getIndexer().toString());
	}
	
	/**
	 * Test the removal till all Opuses are cleared.
	 */
	@Test
	public void testTillEmpty() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		sp.removeOpus(0);
		
		assertEquals("", sp.getIndexer().toString());
	}
	
	/**
	 * Test to make sure the correct total/new index terms and postings are being calculated.
	 */
	@Test
	public void testCalculateAddedSingleFile()
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		assertEquals(5, sp.getIndexer().getTotalIndexTerms());
		assertEquals(5, sp.getIndexer().getUpdatedIndexTerms());
		assertEquals(5, sp.getIndexer().getTotalPostings());
		assertEquals(5, sp.getIndexer().getUpdatedPostings());
	}

	/**
	 * Test to make sure the correct total/new index terms and postings are being calculated.
	 */
	@Test
	public void testCalculateMultipleFiles()
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		assertEquals(13, sp.getIndexer().getTotalIndexTerms());
		assertEquals(4, sp.getIndexer().getUpdatedIndexTerms());
		assertEquals(21, sp.getIndexer().getTotalPostings());
		assertEquals(9, sp.getIndexer().getUpdatedPostings());
	}
	
	/**
	 * Test to make sure the correct total/new index terms and postings are being calculated.
	 */
	@Test
	public void testCalculateAddedNoChange() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = "/Users/David/Desktop/MyRex/pirex08/TestSources/Tester0Clone.txt";
		sp.extractOpus(fileName1, title, author);
	
		assertEquals(5, sp.getIndexer().getTotalIndexTerms());
		assertEquals(0, sp.getIndexer().getUpdatedIndexTerms());
		assertEquals(10, sp.getIndexer().getTotalPostings());
		assertEquals(5, sp.getIndexer().getUpdatedPostings());
	}
	
	/**
	 * Test to make sure the correct total/removed index terms and postings are being calculated.
	 */
	@Test
	public void testCalculateRemovedSingleFile() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		sp.removeOpus(0);
		
		assertEquals(7, sp.getIndexer().getTotalIndexTerms());
		assertEquals(2, sp.getIndexer().getUpdatedIndexTerms());
		assertEquals(7, sp.getIndexer().getTotalPostings());
		assertEquals(5, sp.getIndexer().getUpdatedPostings());
	}
	
	/**
	 * Test to make sure the correct total/removed index terms and postings are being calculated.
	 */
	@Test
	public void testCalculateRemovedMultipleFiles() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(1);
		
		assertEquals(11, sp.getIndexer().getTotalIndexTerms());
		assertEquals(2, sp.getIndexer().getUpdatedIndexTerms());
		assertEquals(14, sp.getIndexer().getTotalPostings());
		assertEquals(7, sp.getIndexer().getUpdatedPostings());
	}
	
	/**
	 * Test to make sure the correct total/removed index terms and postings are being calculated.
	 */
	@Test
	public void testCalculateRemovedNoChange() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = "/Users/David/Desktop/MyRex/pirex08/TestSources/Tester0Clone.txt";
		sp.extractOpus(fileName1, title, author);
		
		sp.removeOpus(0);
		
		assertEquals(5, sp.getIndexer().getTotalIndexTerms());
		assertEquals(0, sp.getIndexer().getUpdatedIndexTerms());
		assertEquals(5, sp.getIndexer().getTotalPostings());
		assertEquals(5, sp.getIndexer().getUpdatedPostings());
	}
	
	
	
	/**
	 * Tests deletion of all elements.
	 */
	@Test
	public void testDelete() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName0 = System.getProperty("user.dir") + "/Sources/Dracula.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/Sources/CommonSense.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/Sources/TheJungleBook.txt";
		sp.extractOpus(fileName2, title, author);

		
		String fileName3 = System.getProperty("user.dir") + "/Sources/TheScarletLetter.txt";
		sp.extractOpus(fileName3, title, author);

		sp.removeOpus(0);
		sp.removeOpus(0);		
		sp.removeOpus(0);
		sp.removeOpus(0);
		
		assertEquals(0, sp.getIndexer().getIndex().size());
	}

	
	/**
	 * Tests the tokenization of the terms.
	 */
	@Test
	public void testTokenizeLines() 
	{
		Indexer indexer = new Indexer();
		LinkedList<String> lines = new LinkedList<>();
		String[] words;
		String[] expected = {"basic", "line", "line", "bunch", "stuff", "twenty-six",
							"good", "number", "3.45", "all", "i", "got", "it's", "not",
							"lot", "will", "do'", "scholars'", "great", "peoples'"};
				
		lines.add("This is a--basic line");
		lines.add("This line has a bunch of stuff ! @ # $ % ^ & * ( ) _ + = { } [ ] | ? , < > : ;");
		lines.add("Twenty-six is--a - good number");
		lines.add("$3.45 is all i got.");
		lines.add("It's--not a lot, but it will 'do'.");
		lines.add("()");
		lines.add("Scholars' are great--peoples')");

		words = indexer.tokenizeLines(lines);
		
		for (int i = 0; i < words.length; i++)
			assertEquals(words[0], expected[0]);
	}
}
