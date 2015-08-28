package edu.bsu.cs222.ajdearmond;

public class LeapYear {
	public boolean isLeapYear(int year) {
		if (isDivisibleByOneHundred(year)) 
			return isDivisibleByFourHundred(year);
		else return isDivisibleByFour(year);
	}

	private boolean isDivisibleByFour(int year) {
		return year % 4 == 0;
	}
	
	private boolean isDivisibleByOneHundred(int year) {
		return year % 100 == 0;
	}
	
	private boolean isDivisibleByFourHundred(int year) {
		return year % 400 == 0;
	}
}
