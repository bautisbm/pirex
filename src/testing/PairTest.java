package testing;

import static org.junit.Assert.*;
import org.junit.Test;
import app.Pair;

/**
 * Test suite for the Pair class.
 * 
 * @author David Wickizer
 *
 */
public class PairTest
{
	/**
	 * Tests the equals method that deals with a String parameter.
	 */
	@Test
	public void testEqualsString() 
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		assertTrue(pair1.equals("This is a short form"));
	}
	
	/**
	 * Tests the getter methods.
	 */
	@Test
	public void testGetters() 
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		String shortForm = "This is a short form";
		String longForm = "This is a long form";
		String term = "cat";
		int opus = 1;
		int doc = 3;
		
		assertEquals(shortForm, pair1.getShortForm());
		assertEquals(longForm, pair1.getLongForm());
		assertEquals(term, pair1.getTerm());
		assertEquals(opus, pair1.getOpus());
		assertEquals(doc, pair1.getDocument());
	}
	
	/**
	 * Tests the overridden equals method comparing itself to itself.
	 */
	@Test
	public void testEqualsSame()
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		assertTrue(pair1.equals(pair1));
	}
	
	/**
	 * Tests the overridden equals method comparing to a different but equal object.
	 */
	@Test
	public void testEqualsDifferentObjects() 
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		Pair pair2 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		assertTrue(pair1.equals(pair2));
	}
	
	/**
	 * Tests the overridden equals method comparing to a null object.
	 */
	@Test
	public void testEqualsNull() 
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		Pair pair2 = null;
		assertFalse(pair1.equals(pair2));
	}
	
	/**
	 * Tests the overridden equals method comparing to a object with a different opus field.
	 */
	@Test
	public void testEqualsDifferntOpusField()
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		Pair pair2 = new Pair("This is a short form", "This is a long form", 2, 3, "cat");
		assertFalse(pair1.equals(pair2));
	}
	
	/**
	 * Tests the overridden equals method comparing to a object with a different document field.
	 */
	@Test
	public void testEqualsDifferntDocumentField() 
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		Pair pair2 = new Pair("This is a short form", "This is a long form", 1, 2, "cat");
		assertFalse(pair1.equals(pair2));
	}
	
	/**
	 * Tests the overridden hashcode method.
	 */
	@Test
	public void testHashCode() 
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		assertEquals(1055, pair1.hashCode());
	}
	
	/**
	 * Tests the compareTo() method.
	 */
	@Test
	public void testCompareToEqual() 
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		Pair pair2 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		assertEquals(0, pair1.compareTo(pair2));
	}
	
	/**
	 * Tests the compareTo() method.
	 */
	@Test
	public void testCompareToDifOpus()
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 2, 3, "cat");
		Pair pair2 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		assertEquals(1, pair1.compareTo(pair2));
	}
	
	/**
	 * Tests the compareTo() method.
	 */
	@Test
	public void testCompareToDifDocument() 
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 2, "cat");
		Pair pair2 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		assertEquals(-1, pair1.compareTo(pair2));
	}
	
	/**
	 * Tests the compareTo() method.
	 */
	@Test
	public void testCompareToOpusLessThan()
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 0, 3, "cat");
		Pair pair2 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		assertEquals(-1, pair1.compareTo(pair2));
	}
	
	/**
	 * Tests the compareTo() method.
	 */
	@Test
	public void testCompareToDocumentGreaterThan()
	{
		Pair pair1 = new Pair("This is a short form", "This is a long form", 1, 4, "cat");
		Pair pair2 = new Pair("This is a short form", "This is a long form", 1, 3, "cat");
		assertEquals(1, pair1.compareTo(pair2));
	}
}
