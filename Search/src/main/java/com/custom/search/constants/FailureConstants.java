package com.custom.search.constants;

/**
 * Enum for storing the common failure HTTP codes and messages that can be used
 * across Spring Boot projects.
 * 
 * 
 * @author Abhijeet
 *
 */
public enum FailureConstants {

	INTERNAL_SERVER_ERROR(-1999, "Oops! Looks like something went wrong."),
	NO_TITLES_FOUND(-2000, "No titles found by the search string"),
	NO_TYPE_RESULTS_FOUND(-2001, "No search results for the searched type");

	private final int failureCode;
	private final String failureMsg;

	private FailureConstants(int failureCode, String failureMsg) {
		this.failureCode = failureCode;
		this.failureMsg = failureMsg;
	}

	public int getFailureCode() {
		return failureCode;
	}

	public String getFailureMsg() {
		return failureMsg;
	}

	@Override
	public String toString() {
		return Integer.toString(failureCode) + "-" + failureMsg;
	}

}
