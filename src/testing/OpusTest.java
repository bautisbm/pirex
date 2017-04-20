package testing;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.Test;
import app.Document;
import app.Opus;

/**
 * Test Suite for Opus class.
 *  
 * @author David Wickizer
 */
public class OpusTest
{
	/**
	 * Helper method to produce a dummy Opus.
	 * 
	 * @return An Opus
	 */
	private Opus createOpus() 
	{
		// Fields for Opus
		String title = "Some Title";
		String author = "David Wickizer";
		String fileName = "File.txt";
		LinkedList<String> lines = new LinkedList<>();
		
		lines.add("This is paragraph 1");
		lines.add("");
		lines.add("This is paragraph 2");
		lines.add("");
		
		Opus opus = new Opus(title, author, fileName, lines);	
		return opus;
	}	
	
	/**
	 * Tests the getDocuments() method.
	 */
	@Test
	public void testExtractDocuments()
	{
		// Fields for Opus
		String title = "Some Title";
		String author = "David Wickizer";
		String fileName = "File.txt";
		LinkedList<String> lines = new LinkedList<>();
		
		lines.add("");
		lines.add("This is paragraph 1");
		lines.add("");
		lines.add("");
		lines.add("");
		lines.add("This is paragraph 2");
		lines.add("");
		lines.add("This is paragraph 3");
		lines.add("This is still paragraph 3");
		lines.add("");
		
		Opus opus = new Opus(title, author, fileName, lines);
		ArrayList<Document> docs = opus.getDocuments();
		ArrayList<String> expected = new ArrayList<>();
		
		expected.add("This is paragraph 1");
		expected.add("This is paragraph 2");
		expected.add("This is paragraph 3");
		expected.add("This is still paragraph 3");
		
		for (int i = 0; i < docs.size(); i++)
			assertEquals(expected.get(i), docs.get(i).toString(true));
	}
	
	/**
	 * Tests the getDocuments() method.
	 */
	@Test
	public void testExtractDocumentsSingleParagraph()
	{
		// Fields for Opus
		String title = "Some Title";
		String author = "David Wickizer";
		String fileName = "File.txt";
		LinkedList<String> lines = new LinkedList<>();
		
		lines.add("This is paragraph 1");
		lines.add("This is still paragraph 1");

		Opus opus = new Opus(title, author, fileName, lines);
		ArrayList<Document> docs = opus.getDocuments();
		ArrayList<String> expected = new ArrayList<>();
		
		expected.add("This is paragraph 1");
		expected.add("This is still paragraph 1");
		
		for (int i = 0; i < docs.size(); i++)
			assertEquals(expected.get(i), docs.get(i).toString(true));
	}
	
	/**
	 * Tests the deepCopy() method.
	 */
	@Test
	public void testDeepCopy()
	{
		Opus opus = createOpus();
		LinkedList<String> list = new LinkedList<>();
		list.add("Hello");
		list.add("Goodbye");
		
		LinkedList<String> copy = opus.deepCopy(list);
		
		for (int i = 0; i < copy.size(); i++)
			assertTrue(copy.get(i).equals(list.get(i)));
	}
	
	/**
	 * Tests the getTitle() method.
	 */
	@Test
	public void testGetTitle()
	{
		Opus opus = createOpus();
		assertEquals("Some Title", opus.getTitle());
	}

	/**
	 * Tests the getAuthor() method.
	 */
	@Test
	public void testGetAuthor()
	{
		Opus opus = createOpus();
		assertEquals("David Wickizer", opus.getAuthor());
	}
	
	/**
	 * Tests the getOrdinalNumber() method.
	 */
	@Test
	public void testGetOrdinalNumber()
	{
		Opus opus = createOpus();
		assertEquals(0, opus.getOrdinalNumber());
	}
	
	/**
	 * Tests the getFileName() method.
	 */
	@Test
	public void testGetFileName()
	{
		Opus opus = createOpus();
		assertEquals("File.txt", opus.getFileName());
	}
	
	/**
	 * Tests the getDocuments() method.
	 */
	@Test
	public void testGetDocuments()
	{
		Opus opus = createOpus();
		ArrayList<Document> expected = new ArrayList<>();
		
		// Fields for Document
		LinkedList<String> lines0 = new LinkedList<>();
		lines0.add("This is paragraph 1");
		
		LinkedList<String> lines1 = new LinkedList<>();
		lines1.add("This is paragraph 2");
		
		// Create Documents and add them to ArrayList of Documents
		expected.add(new Document(lines0));
		expected.add(new Document(lines1));
		
		ArrayList<Document> actual = opus.getDocuments();
		
		for (int i = 0; i < actual.size(); i++)
			assertArrayEquals(expected.get(i).getLines().toArray(), actual.get(i).getLines().toArray());
	}
	
	/**
	 * Tests the getDocument() method.
	 */
	@Test
	public void testGetDocument()
	{
		Opus opus = createOpus();
		LinkedList<String> lines = new LinkedList<>();
		lines.add("This is paragraph 1");
		Document document = new Document(lines);

		assertEquals(document.toString(true), opus.getDocument(0).toString(true));
	}
	
	/**
	 * Tests the getLines() method.
	 */
	@Test
	public void testGetLines()
	{
		String[] lines = {"This is paragraph 1","", "This is paragraph 2", ""};
		Opus opus = createOpus();
	
		assertArrayEquals(lines, opus.getLines().toArray());
	}
	
	/**
	 * Tests the setTitle() method.
	 */
	@Test
	public void testSetTitle()
	{
		Opus opus = createOpus();
		opus.setTitle("New Title");
		assertEquals("New Title", opus.getTitle());
	}	
	
	/**
	 * Tests the setAuthor() method.
	 */
	@Test
	public void testSetAuthor()
	{
		Opus opus = createOpus();
		opus.setAuthor("Joseph Raines");
		assertEquals("Joseph Raines", opus.getAuthor());
	}	
	
	/**
	 * Tests the setOrdinalNumber() method.
	 */
	@Test
	public void testSetOrdinalNumber() 
	{
		Opus opus = createOpus();
		opus.setOrdinalNumber(999);
		assertEquals(999, opus.getOrdinalNumber());
	}	
	
	/**
	 * Tests the shortForm() method.
	 */
	@Test
	public void testShortForm() 
	{
		Opus opus = createOpus();
		String expected = "David Wickizer  Some Title   1  This is paragraph 2";
		assertEquals(expected, opus.shortForm(1));
	}
	
	/**
	 * Tests the longForm() method.
	 */
	@Test
	public void testLongForm() 
	{
		Opus opus = createOpus();
		String expected = "This is paragraph 2\n";
		assertEquals(expected, opus.longForm(1));
	}
	
	/**
	 * Tests the opusSummaryState() method.
	 */
	@Test
	public void testOpusSummaryState()
	{
		Opus opus = createOpus();
		String expected = "";
		expected += "Opus 0: David Wickizer     Some Title     2 documents\n";
		expected += "            File.txt";
		assertEquals(expected, opus.opusSummaryState());
	}
	
	/**
	 * Tests the loadSummaryState() method.
	 */
	@Test
	public void testLoadSummaryState()
	{
		Opus opus = createOpus();
		String expected = "";
		expected += "Opus: File.txt\n";
		expected += "Title: Some Title\n";
		expected += "Author: David Wickizer\n";
		expected += "Opus Size: 2 documents\n";
		expected += "Opus Number: 0";
		
		assertEquals(expected, opus.loadSummaryState());
	}
	
	/**
	 * Tests the toString() method.
	 */
	@Test
	public void testToString()
	{
		Opus opus = createOpus();
		String expected = "Title: Some Title\nAuthor: David Wickizer\nOrdinal "
				+ "Number: 0\nDocuments in this Opus: 2\n";
		assertEquals(expected, opus.toString());
	}	
}
