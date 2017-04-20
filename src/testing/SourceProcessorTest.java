package testing;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import app.SourceProcessor;
import app.Store;
import app.Opus;
import app.Indexer;

/**
 * Test Suite for SourceProcessor class.
 *  
 * @author David Wickizer
 */
public class SourceProcessorTest 
{

	/**
	 * Test to make sure store objects are equal.
	 */
	@Test
	public void testConstructor()
	{
		Store store = new Store();
		SourceProcessor sp = new SourceProcessor(store);
		assertEquals(store, sp.store);
	}
	
	/**
	 * Test for extractAuthor().
	 */
	@Test
	public void testExtractAuthor() 
	{
		SourceProcessor sp = new SourceProcessor(new Store());
		String fileName = System.getProperty("user.dir") + "/TestSources/Dracula.txt";
		assertEquals("Bram Stoker", sp.extractAuthor(fileName));
	}
	
	/**
	 * Test for extractAuthorNull().
	 */
	@Test
	public void testExtractAuthorNull() 
	{
		SourceProcessor sp = new SourceProcessor(new Store());
		String fileName = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		assertEquals(null, sp.extractAuthor(fileName));
	}
	
	/**
	 * Test for extractTitle().
	 */
	@Test
	public void testExtractTitle() 
	{
		SourceProcessor sp = new SourceProcessor(new Store());
		String fileName = System.getProperty("user.dir") + "/TestSources/Dracula.txt";
		assertEquals("Dracula", sp.extractTitle(fileName));
	}
	
	/**
	 * Test for extractTitleNull().
	 */
	@Test
	public void testExtractTitleNull() 
	{
		SourceProcessor sp = new SourceProcessor(new Store());
		String fileName = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		assertEquals(null, sp.extractTitle(fileName));
	}
	
	/**
	 * Test for extractOpus().
	 */
	@Test
	public void testExtractOpus()
	{
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String fileName = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		String title = "Dracula";
		String author = "Bram Stoker";
		Opus opus = sp.extractOpus(fileName, title, author);
		
		assertEquals(3, opus.getDocuments().size());
		assertEquals("Bram Stoker  Dracula   1  blue red",
					 opus.shortForm(1));
	}
	
	/**
	 * Test for correct Opus ordinal numbers every time a new Opus is added.
	 */
	@Test 
	public void testOpusOrdinalNumberAdd()
	{
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		Opus opus0 = sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		Opus opus1 = sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		Opus opus2 = sp.extractOpus(fileName2, title, author);
		
		int[] expected = {0, 1, 2};
		int[] actual = new int[3];
		
		actual[0] = opus0.getOrdinalNumber();
		actual[1] = opus1.getOrdinalNumber();
		actual[2] = opus2.getOrdinalNumber();
		
		assertArrayEquals(expected, actual);
	}
	
	/**
	 * Test for correct Opus ordinal numbers after an Opus from the beginning has been removed 
	 * from a list of Opuses.
	 */
	@Test 
	public void testOpusOrdinalNumberRemoveBeginning() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		Opus opus1 = sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		Opus opus2 = sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(0);
		
		int[] expected = {0, 1};
		int[] actual = new int[2];
		
		actual[0] = opus1.getOrdinalNumber();
		actual[1] = opus2.getOrdinalNumber();
		
		assertArrayEquals(expected, actual);
	}

	/**
	 * Test for correct Opus ordinal numbers after an Opus from the middle has been removed 
	 * from a list of Opuses.
	 */
	@Test 
	public void testOpusOrdinalNumberRemoveMiddle()
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		Opus opus0 = sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		Opus opus2 = sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(1);
		
		int[] expected = {0, 1};
		int[] actual = new int[2];
		
		actual[0] = opus0.getOrdinalNumber();
		actual[1] = opus2.getOrdinalNumber();
		
		assertArrayEquals(expected, actual);
	}
	
	/**
	 * Test for correct Opus ordinal numbers after an Opus from the end has been removed 
	 * from a list of Opuses.
	 */
	@Test 
	public void testOpusOrdinalNumberRemoveEnd() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		Opus opus0 = sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		Opus opus1 = sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(2);
		
		int[] expected = {0, 1};
		int[] actual = new int[2];
		
		actual[0] = opus0.getOrdinalNumber();
		actual[1] = opus1.getOrdinalNumber();
		
		assertArrayEquals(expected, actual);
	}
	
	/**
	 * Test for correct Opus ordinal numbers after an Opus from the beginning has been removed 
	 * from a list of Opuses, and then a new Opus is added.
	 */
	@Test 
	public void testOpusOrdinalNumberRemoveBeginningAdd()
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		Opus opus1 = sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		Opus opus2 = sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(0);
		
		String fileName3 = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		Opus opus3 = sp.extractOpus(fileName3, title, author);
		
		int[] expected = {0, 1, 2};
		int[] actual = new int[3];
		
		actual[0] = opus1.getOrdinalNumber();
		actual[1] = opus2.getOrdinalNumber();
		actual[2] = opus3.getOrdinalNumber();
		
		assertArrayEquals(expected, actual);
	}
	
	/**
	 * Test for correct Opus ordinal numbers after an Opus from the middle has been removed 
	 * from a list of Opuses, and then a new Opus is added.
	 */
	@Test 
	public void testOpusOrdinalNumberRemoveMiddleAdd() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		Opus opus0 = sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		Opus opus2 = sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(1);
		
		String fileName3 = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		Opus opus3 = sp.extractOpus(fileName3, title, author);
		
		int[] expected = {0, 1, 2};
		int[] actual = new int[3];
		
		actual[0] = opus0.getOrdinalNumber();
		actual[1] = opus2.getOrdinalNumber();
		actual[2] = opus3.getOrdinalNumber();
		
		assertArrayEquals(expected, actual);
	}
	
	/**
	 * Test for correct Opus ordinal numbers after an Opus from the beginning has been removed 
	 * from a list of Opuses, and then a new Opus is added.
	 */
	@Test 
	public void testOpusOrdinalNumberRemoveEndAdd()
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		Opus opus0 = sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		Opus opus1 = sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(2);
		
		String fileName3 = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		Opus opus3 = sp.extractOpus(fileName3, title, author);
		
		int[] expected = {0, 1, 2};
		int[] actual = new int[3];
		
		actual[0] = opus0.getOrdinalNumber();
		actual[1] = opus1.getOrdinalNumber();
		actual[2] = opus3.getOrdinalNumber();
		
		assertArrayEquals(expected, actual);
	}
	
	/**
	 * Test for correct Opus ordinal numbers after an Opus from the beginning has been removed 
	 * from a list of Opuses, and then a new Opus is added, and then one is removed.
	 */
	@Test 
	public void testOpusOrdinalNumberRemoveAddRemove() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		Opus opus0 = sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		Opus opus2 = sp.extractOpus(fileName2, title, author);
		
		sp.removeOpus(1);
		
		String fileName3 = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		sp.extractOpus(fileName3, title, author);
		
		sp.removeOpus(2);
		
		int[] expected = {0, 1};
		int[] actual = new int[2];
		
		actual[0] = opus0.getOrdinalNumber();
		actual[1] = opus2.getOrdinalNumber();
		
		assertArrayEquals(expected, actual);
	}
	
	/**
	 * Test for correct Opus ordinal numbers after an Opus from the beginning has been removed 
	 * from a list of Opuses, and then a new Opus is added, and then one is removed.
	 */
	@Test 
	public void testOpusOrdinalNumberRemoveRemove() 
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		Opus opus0 = sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		Opus opus2 = sp.extractOpus(fileName2, title, author);
		
		String fileName3 = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		sp.extractOpus(fileName3, title, author);
		
		sp.removeOpus(1);
		sp.removeOpus(2);
		
		int[] expected = {0, 1};
		int[] actual = new int[2];
		
		actual[0] = opus0.getOrdinalNumber();
		actual[1] = opus2.getOrdinalNumber();
		
		assertArrayEquals(expected, actual);
	}
	
	/**
	 * Test for correct documentStore size after an Opus has been removed.
	 */
	@Test 
	public void testDocumentStoreSizeRemove()
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		String fileName3 = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		sp.extractOpus(fileName3, title, author);
		
		sp.removeOpus(1);
		
		assertEquals(3, sp.getDocumentStore().size());
	}
	
	/**
	 * Test for correct documentStore size after all elements are removed.
	 */
	@Test 
	public void testDocumentStoreSizeRemoveTillEmpty() 
	{
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		String fileName3 = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		sp.extractOpus(fileName3, title, author);
		
		sp.removeOpus(0);
		sp.removeOpus(0);
		sp.removeOpus(0);
		sp.removeOpus(0);
		
		assertEquals(0, sp.getDocumentStore().size());
	}
	
	/**
	 * Test for removal from empty documentStore.
	 */
	@Test 
	public void testDocumentStoreEmptyRemoval() 
	{
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		assertEquals(null, sp.removeOpus(0));
	}
	
	/**
	 * Test for load summary output.
	 */
	@Test 
	public void testLoadSummary()
	{	
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String expected = "";
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		String fileName3 = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		sp.extractOpus(fileName3, title, author);
		
		expected += "Opus: " + System.getProperty("user.dir") + "/TestSources/TesterNull.txt\n";
		expected += "Title: Dracula\n";
		expected += "Author: Bram Stoker\n";
		expected += "Opus Size: 3 documents\n";
		expected += "Opus Number: 3\n";
		expected += "New index terms: 0\n";
		expected += "New postings: 8\n";
		expected += "Total index terms: 13\n";
		expected += "Total postings: 29";
		
		assertEquals(expected, sp.loadSummary());
	}
	
	/**
	 * Test for opus summary output.
	 */
	@Test 
	public void testOpusSummary()
	{
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		String expected = "";
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		String fileName1 = System.getProperty("user.dir") + "/TestSources/Tester1.txt";
		sp.extractOpus(fileName1, title, author);
		
		String fileName2 = System.getProperty("user.dir") + "/TestSources/Tester2.txt";
		sp.extractOpus(fileName2, title, author);
		
		String fileName3 = System.getProperty("user.dir") + "/TestSources/TesterNull.txt";
		sp.extractOpus(fileName3, title, author);
		
		expected += "Opus 0: Bram Stoker     Dracula     3 documents\n";
		expected += "            " + System.getProperty("user.dir") + "/TestSources/Tester0.txt\n";
		expected += "Opus 1: Bram Stoker     Dracula     5 documents\n";
		expected += "            " + System.getProperty("user.dir") + "/TestSources/Tester1.txt\n";
		expected += "Opus 2: Bram Stoker     Dracula     5 documents\n";
		expected += "            " + System.getProperty("user.dir") + "/TestSources/Tester2.txt\n";
		expected += "Opus 3: Bram Stoker     Dracula     3 documents\n";
		expected += "            " + System.getProperty("user.dir") + "/TestSources/TesterNull.txt\n\n";
		expected += "Index terms: 13\n";
		expected += "Postings: 29";
		
		assertEquals(expected, sp.opusSummary());
	}
	
	/**
	 * Tests for the getter methods.
	 */
	@Test 
	public void testGetters()
	{
		SourceProcessor sp = new SourceProcessor(new Store(-1));
		ArrayList<Opus> opuses;
		Indexer index;
		String expected = "";
		String title = "Dracula";
		String author = "Bram Stoker";
		
		String fileName0 = System.getProperty("user.dir") + "/TestSources/Tester0.txt";
		sp.extractOpus(fileName0, title, author);
		
		opuses = sp.getDocumentStore();
		index = sp.getIndexer();
		
		expected += "Title: Dracula\n";
		expected += "Author: Bram Stoker\n";
		expected += "Ordinal Number: 0\n";
		expected += "Documents in this Opus: 3\n";
		
		for (Opus opus : opuses) assertEquals(expected, opus.toString());
		
		expected = "";
		expected += "red: <0, {0, 1}>\n";
		expected += "orange: <0, {2}>\n";
		expected += "blue: <0, {1}>\n";
		expected += "barn: <0, {0}>\n";
		expected += "fox: <0, {2}>\n";
	
		assertEquals(expected, index.toString());
	}
}
