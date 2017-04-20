package testing;
import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import app.Document;

/**
 * Test Suite for Document class.
 *  
 * @author David Wickizer
 */
public class DocumentTest
{
	/**
	 * Helper method to produce a dummy Document.
	 * 
	 * @return An Document
	 */
	private Document createDocument() 
	{
		// Fields for Document
		LinkedList<String> docLines = new LinkedList<>();
		docLines.add("This is paragraph 1.");
		docLines.add("This is line 2.");
		
		// Create Documents 
		Document document = new Document(docLines);

		return document;
	}
	
	/**
	 * Tests the getDocumentID() method.
	 */
	@Test
	public void testGetDocumentID()
	{
		Document document = createDocument();
		document.setDocumentID(900);
		assertEquals(900, document.getDocumentID());
	}
	
	/**
	 * Tests the getLines() method.
	 */
	@Test
	public void testGetLines()
	{
		Document document = createDocument();
		LinkedList<String> docLines = new LinkedList<>();
		docLines.add("This is paragraph 1.");
		docLines.add("This is line 2.");
	
		for (int i = 0; i < docLines.size(); i++)
			assertEquals(docLines.get(i), document.getLines().get(i));
	}
	
	/**
	 * Tests the setDocumentID() method.
	 */
	@Test
	public void testSetDocumentID()
	{
		Document document = createDocument();
		document.setDocumentID(900);
		assertEquals(900, document.getDocumentID());
	}	

	/**
	 * Tests the toString(true) method.
	 */
	@Test
	public void testToStringTrue()
	{
		Document document = createDocument();
		assertEquals("This is paragraph 1.", document.toString(true));
	}	
	
	/**
	 * Tests the toString(false) method.
	 */
	@Test
	public void testToStringFalse()
	{
		Document document = createDocument();
		assertEquals("This is paragraph 1.\nThis is line 2.\n", document.toString(false));
	}
}
