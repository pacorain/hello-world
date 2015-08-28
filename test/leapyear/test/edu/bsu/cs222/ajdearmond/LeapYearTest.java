package edu.bsu.cs222.ajdearmond;

import org.junit.Assert;
import org.junit.Test;

import edu.bsu.cs222.ajdearmond.LeapYear;

public class LeapYearTest {
	private LeapYear leapYear = new LeapYear();
	
	@Test
	public void test1980IsTrue() {
		boolean result = leapYear.isLeapYear(1980);
		Assert.assertTrue(result);
	}
	
	@Test
	public void test1981IsFalse() {
		boolean result = leapYear.isLeapYear(1981);
		Assert.assertFalse(result);
	}
	
	@Test
	public void test1700IsFalse() {
		boolean result = leapYear.isLeapYear(1700);
		Assert.assertFalse(result);
	}
}
