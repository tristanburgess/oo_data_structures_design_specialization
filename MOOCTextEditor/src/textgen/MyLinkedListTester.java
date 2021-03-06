/**
 * 
 */
package textgen;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author UC San Diego MOOC team
 *
 */
public class MyLinkedListTester {

	private static final int LONG_LIST_LENGTH =10; 

	MyLinkedList<String> shortList;
	MyLinkedList<Integer> emptyList;
	MyLinkedList<Integer> longerList;
	MyLinkedList<Integer> list1;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Feel free to use these lists, or add your own
	    shortList = new MyLinkedList<String>();
		shortList.add("A");
		shortList.add("B");
		emptyList = new MyLinkedList<Integer>();
		longerList = new MyLinkedList<Integer>();
		for (int i = 0; i < LONG_LIST_LENGTH; i++)
		{
			longerList.add(i);
		}
		list1 = new MyLinkedList<Integer>();
		list1.add(65);
		list1.add(21);
		list1.add(42);
		
	}

	
	/** Test if the get method is working correctly.
	 */
	/*You should not need to add much to this method.
	 * We provide it as an example of a thorough test. */
	@Test
	public void testGet()
	{
		//test empty list, get should throw an exception
		try {
			emptyList.get(0);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			
		}
		
		// test short list, first contents, then out of bounds
		assertEquals("Check first", "A", shortList.get(0));
		assertEquals("Check second", "B", shortList.get(1));
		
		try {
			shortList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			shortList.get(2);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		// test longer list contents
		for(int i = 0; i<LONG_LIST_LENGTH; i++ ) {
			assertEquals("Check "+i+ " element", (Integer)i, longerList.get(i));
		}
		
		// test off the end of the longer array
		try {
			longerList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			longerList.get(LONG_LIST_LENGTH);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		}
		
	}
	
	
	/** Test removing an element from the list.
	 * We've included the example from the concept challenge.
	 * You will want to add more tests.  */
	@Test
	public void testRemove()
	{
		int a = list1.remove(0);
		assertEquals("Remove: check a is correct ", 65, a);
		assertEquals("Remove: check element 0 is correct ", (Integer)21, list1.get(0));
		assertEquals("Remove: check size is correct ", 2, list1.size());
		
		try {
			longerList.remove(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		
		try {
			longerList.remove(LONG_LIST_LENGTH);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
	}
	
	/** Test adding an element into the end of the list, specifically
	 *  public boolean add(E element)
	 * */
	@Test
	public void testAddEnd()
	{
		list1.add(5);
		int a = list1.get(list1.size-1);
		assertEquals("Add End: check a is correct ", 5, a);
	}

	
	/** Test the size of the list */
	@Test
	public void testSize()
	{
		int before = list1.size;
		list1.add(5);
		int after = list1.size;
		assertEquals("Size: check list size is one greater after add ", before + 1, after);
		
		before = list1.size;
		list1.add(5);
		after = list1.size;
		assertEquals("Size: check list size is one less after remove ", before + 1, after);
		
		assertEquals("Size: check list size after many adds ", longerList.size, LONG_LIST_LENGTH);
	}

	
	
	/** Test adding an element into the list at a specified index,
	 * specifically:
	 * public void add(int index, E element)
	 * */
	@Test
	public void testAddAtIndex()
	{
		int testElem = 5;
		
		int prev = list1.get(2);
		list1.add(2, testElem);
		int next = list1.get(2);
		assertEquals("Remove: check a is correct ", testElem, next);
		int nextNext = list1.get(3);
		assertEquals("Remove: check a is correct ", prev, nextNext);
		
		try {
			longerList.add(-1, testElem);
			fail("Add Idx 1: Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		
		try {
			longerList.add(LONG_LIST_LENGTH + 1, testElem);
			fail("Add Idx 2: Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		
		try {
			longerList.add(LONG_LIST_LENGTH - 1, null);
			fail("Add Idx 3: Check null ptr");
		}
		catch (NullPointerException e) {
		
		}
	}
	
	/** Test setting an element in the list */
	@Test
	public void testSet()
	{
		longerList.set(LONG_LIST_LENGTH - 1, 5);
		int test = longerList.get(LONG_LIST_LENGTH - 1);
		assertEquals("Set: test is correct ", 5, test);
		
		try {
			longerList.set(-1, 5);
			fail("Set 1: Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		
		try {
			longerList.set(LONG_LIST_LENGTH, 5);
			fail("Set 2: Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		
		try {
			longerList.set(LONG_LIST_LENGTH - 1, null);
			fail("Set 3: Check null ptr");
		}
		catch (NullPointerException e) {
		
		}
	}
	
}
