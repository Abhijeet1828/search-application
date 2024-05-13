package com.custom.search.service;

/**
 * This interface contains the functions to interact with the Netlfix index in
 * elasticsearch.
 * 
 * @author Abhijeet
 *
 */
public interface NetflixService {

	/***
	 * This method is used to query the netflix index using the title field.
	 * 
	 * @param title - search string
	 * @param page
	 * @param size
	 * 
	 * @return
	 */
	public Object findByTitle(String title, int page, int size);

	/**
	 * This method is used to query the netflix index using the title field.
	 * 
	 * @param type
	 * @param page
	 * @param size
	 * @return
	 */
	public Object findByType(String type, int page, int size);

}
