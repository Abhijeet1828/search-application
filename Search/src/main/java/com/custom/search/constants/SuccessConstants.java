package com.custom.search.constants;

/**
 * Enum for storing the common success HTTP codes and messages that can be used
 * across Spring Boot projects.
 * 
 * @author Abhijeet
 *
 */
public enum SuccessConstants {

	FIND_BY_TITLE(2000, "Titles searched successfully"), 
	FIND_BY_TYPE(2001, "Type searched successfully");

	private final int successCode;
	private final String successMsg;

	private SuccessConstants(int successCode, String successMsg) {
		this.successCode = successCode;
		this.successMsg = successMsg;
	}

	public int getSuccessCode() {
		return successCode;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	@Override
	public String toString() {
		return Integer.toString(successCode) + "-" + successMsg;
	}

}
