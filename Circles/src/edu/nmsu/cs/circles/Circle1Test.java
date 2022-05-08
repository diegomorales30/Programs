package edu.nmsu.cs.circles;

/***
 * Example JUnit testing class for Circle1 (and Circle)
 *
 * - must have your classpath set to include the JUnit jarfiles - to run the test do: java
 * org.junit.runner.JUnitCore Circle1Test - note that the commented out main is another way to run
 * tests - note that normally you would not have print statements in a JUnit testing class; they are
 * here just so you see what is happening. You should not have them in your test cases.
 ***/

import org.junit.*;

public class Circle1Test
{
	// Data you need for each test case
	private Circle1 circle1;

	//
	// Stuff you want to do before each test case
	//
	@Before
	public void setup()
	{
		System.out.println("\nTest starting...");
		circle1 = new Circle1(1, 2, 3);
	}

	//
	// Stuff you want to do after each test case
	//
	@After
	public void teardown()
	{
		System.out.println("\nTest finished.");
	}

	// Test for make circle bigger
	@Test
	public void testBigScale(){
		System.out.println("Test: For scale of a big circle");
		double factorForScale = 2.5;
		double orginalRadius = circle1.radius;
		double newRadius = circle1.scale(factorForScale);
		Assert.assertTrue(newRadius == orginalRadius * factorForScale);
	}

	@Test
	public void noInterSection(){
		Circle1 cir1 = new Circle1(0,0,10);
		Circle1 cir2 = new Circle1(30, 30, 10);
		System.out.println("The circles are far apart");
		Assert.assertFalse(cir1.intersects(cir2));
		Assert.assertFalse(cir2.intersects(cir1));

		System.out.println("The two circle are barley touching");
		Circle1 cir3 = new Circle1(0, 0,4.9999999);
		Circle1 cir4 = new Circle1(12,9,10);
		Assert.assertFalse(cir3.intersects(cir4)); 
		Assert.assertFalse(cir4.intersects(cir3)); 
	}

	@Test
	public void interSectionCircles(){
		System.out.println("Two circles on top of each other");
		Circle1 cir1 = new Circle1(10, 10, 10 );
		Circle1 cir2 = new Circle1(10, 10, 10);
		Assert.assertTrue(cir1.intersects(cir2));
		Assert.assertTrue(cir2.intersects(cir1));

		System.out.println("Two circles intersect at 2 points");
		Circle1 cir3 = new Circle1(10, 10, 10 );
		Circle1 cir4 = new Circle1(5, 6, 8);
		Assert.assertTrue(cir3.intersects(cir4));
		Assert.assertTrue(cir4.intersects(cir3));
	}

	

	//
	// Test a simple positive move
	//
	@Test
	public void simpleMove()
	{
		Point p;
		System.out.println("Running test simpleMove.");
		p = circle1.moveBy(1, 1);
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
		p = circle1.moveBy(-1, -1);
		Assert.assertTrue(p.x == 0 && p.y == 1);
	}

	/***
	 * NOT USED public static void main(String args[]) { try { org.junit.runner.JUnitCore.runClasses(
	 * java.lang.Class.forName("Circle1Test")); } catch (Exception e) { System.out.println("Exception:
	 * " + e); } }
	 ***/

}
