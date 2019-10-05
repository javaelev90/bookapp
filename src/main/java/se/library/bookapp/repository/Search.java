package se.library.bookapp.repository;

import java.util.List;

public interface Search<T> {
	
	/**
	 * Searches for objects of type T based on a search string and returns all matches
	 * @param searchString the string to match object attributes against
	 * @return a list of the found objects
	 */
	List<T> search(String searchString);
	
}
