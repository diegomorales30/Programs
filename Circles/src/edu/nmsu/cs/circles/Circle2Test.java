package edu.nmsu.cs.circles;

/***
 * Example JUnit testing class for Circle2 (and Circle)
 *
 * - must have your classpath set to include the JUnit jarfiles - to run the test do: java
 * org.junit.runner.JUnitCore Circle2Test - note that the commented out main is another way to run
 * tests - note that normally you would not have print statements in a JUnit testing class; they are
 * here just so you see what is happening. You should not have them in your test cases.
 ***/

import org.junit.*;

public class Circle2Test
{
	// Data you need for each test case
	private Circle2 circle2;

	//
	// Stuff you want to do before each test case
	//
	@Before
	public void setup()
	{
		System.out.println("\nTest starting...");
		circle2 = new Circle2(1, 2, 3);
	}

	//
	// Stuff you want to do after each test case
	//
	@After
	public void teardown()
	{
		System.out.println("\nTest finished.");
	}

    @Test
	public void interSectionCircles(){
		System.out.println("Two circles on top of each other");
		Circle2 cir1 = new Circle2(1, 2,3 );
		Assert.assertTrue(circle2.intersects(cir1) == true);

		System.out.println("Two circles intersect at 2 points");
		Circle2 cir3 = new Circle2(10, 10, 11 );
		Assert.assertTrue(circle2.intersects(cir3) == true);

		System.out.println("The two circles intersect at 1 point");
		Circle2 cir5 = new Circle2(0, 7, 3);
		Assert.assertTrue(circle2.intersects(cir5) == true);
	}

	@Test
	public void noInterSection(){
		Circle2 cir1 = new Circle2(10,10,8);
		System.out.println("The circles are far apart");
		Assert.assertFalse(circle2.intersects(cir1) == false);
				
	}

	@Test
	public void testBigScale(){
		System.out.println("Test: For scale of a big circle");
		double factorForScale = 2.5;
		double orginalRadius = circle2.radius;
		double newRadius = circle2.scale(factorForScale);
		Assert.assertTrue(newRadius == orginalRadius * factorForScale);
	}

	//
	// Test a simple positive move
	//
	@Test
	public void simpleMove()
	{
		Point p;
		System.out.println("Running test simpleMove.");
		p = circle2.moveBy(1, 1);
		Assert.assertTrue(p.x == 2 && p.y == 3);
	}

	//
	// Test a simple negative move
	//
	@Test
	public void simpleMoveNeg()
	{
		Point p;
		System.out.println("Running test simpleMoveNeg.");
		p = circle2.moveBy(-1, -1);
		Assert.assertTrue(p.x == 0 && p.y == 1);
	}

	/***
	 * NOT USED public static void main(String args[]) { try { org.junit.runner.JUnitCore.runClasses(
	 * java.lang.Class.forName("Circle2Test")); } catch (Exception e) { System.out.println("Exception:
	 * " + e); } }
	 ***/

}
