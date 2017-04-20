package testing;

import static org.junit.Assert.*;
import org.junit.Test;
import app.Posting;
import java.util.*;

/**
 * Tests suite for the Posting class.
 * 
 * @author David Wickizer
 *
 */
public class PostingTest 
{
	/**
	 * Tests the getOpusIndex() method.
	 */
	@Test
	public void testGetOpusIndex() 
	{
		Posting posting = new Posting();
		assertEquals(0, (int) posting.getOpusIndex());
	}
	
	/**
	 * Tests the getDocumentIndexes() method.
	 */
	@Test
	public void testGetDcoumentIndexes() 
	{
		Posting posting = new Posting();
		assertEquals(0, posting.getDocumentIndexes().size());
	}
	
	/**
	 * Tests the setPosting() method.
	 */
	@Test
	public void testSetPosting() 
	{
		Posting posting = new Posting();
		posting.setPosting(45, 36);
		
		assertEquals(45, (int) posting.getOpusIndex());
		assertEquals(36, (int) posting.getDocumentIndexes().get(0));
		assertEquals(1, posting.getDocumentIndexes().size());
	}
	
	/**
	 * Tests the setOpusIndex() method.
	 */
	@Test
	public void testSetOpusIndex()
	{
		Posting posting = new Posting();
		posting.setOpusIndex(45);
		
		assertEquals(45, (int) posting.getOpusIndex());
	}
	
	/**
	 * Tests the addDocument() method.
	 */
	@Test
	public void testAddDocument() 
	{
		Posting posting = new Posting();
		posting.setPosting(45, 36);
		posting.addDocument(57);

		assertEquals(36, (int) posting.getDocumentIndexes().get(0));
		assertEquals(57, (int) posting.getDocumentIndexes().get(1));
		assertEquals(2, posting.getDocumentIndexes().size());
	}
	
	/**
	 * Tests the equals() method.
	 */
	@Test
	public void testEquals()
	{
		Posting posting = new Posting();
		Posting other = new Posting();
		
		posting.setPosting(45, 36);
		other.setPosting(45, 21);
		
		assertEquals(true, posting.equals(other));
	}
	
	/**
	 * Tests that an ArrayList properly uses the equals function when checking to see if a 
	 * Posting is contained within the list.
	 */
	@Test
	public void testContains() 
	{
		ArrayList<Posting> postings = new ArrayList<>();
		Posting posting0 = new Posting();
		Posting posting1 = new Posting();
		Posting other = new Posting();
		
		posting0.setPosting(45, 36);
		posting1.setPosting(50, 21);
		
		postings.add(posting0);
		postings.add(posting1);
		
		assertEquals(false, postings.contains(other));
		
		other.setPosting(50, 24);
		
		assertEquals(true, postings.contains(other));
	}
	
	/**
	 * Tests the toString() method.
	 */
	@Test
	public void testToString() 
	{
		Posting posting = new Posting();
		
		posting.setPosting(45, 36);
		posting.addDocument(37);
		posting.addDocument(38);
		posting.addDocument(45);
		
		assertEquals("<45, {36, 37, 38, 45}>", posting.toString());
	}
}
