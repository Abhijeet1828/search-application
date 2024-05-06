package com.custom.search.service;

/**
 * This interface contains the functions to interact with the Netlfix index in
 * elasticsearch.
 * 
 * @author Abhijeet
 *
 */
public interface NetflixService {

	public Object findByTitle(String title, int page, int size);

}
