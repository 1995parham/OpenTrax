/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham
 *
 * [] Creation Date : 12-04-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.core.domain;

import junit.framework.TestCase;
import org.junit.Test;

public class TraxMoveTest extends TestCase {

	public TraxMoveTest(String name){
		super(name);
	}

	@Test
	public void testMove1() throws Exception{
		TraxMove newMove;
		newMove = new TraxMove("@0/");
		assertEquals(0, newMove.getColumn());
		assertEquals(0, newMove.getRow());
	}

	@Test
	public void testMove2() throws Exception{
		TraxMove newMove;
		newMove = new TraxMove("@10/");
		assertEquals(0, newMove.getColumn());
		assertEquals(10, newMove.getRow());
	}

	@Test
	public void testMove3() throws Exception{
		TraxMove newMove;
		newMove = new TraxMove("A1000/");
		assertEquals(1, newMove.getColumn());
		assertEquals(1000, newMove.getRow());
	}

	@Test
	public void testMove4() throws Exception{
		TraxMove newMove;
		newMove = new TraxMove("AA100/");
		assertEquals(26, newMove.getColumn());
		assertEquals(100, newMove.getRow());
	}

}
