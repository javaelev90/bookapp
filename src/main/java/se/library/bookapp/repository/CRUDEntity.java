package se.library.bookapp.repository;

import java.util.List;

public interface CRUDEntity<T> {
	
	/**
	 * Puts the object T in the repository
	 * @param t the object to be stored in the repository
	 * @return the key for the created object, -1 if nothing was created
	 */
	int create(T t);
	/**
	 * Updates the object T in the repository
	 * @param t the object to be updated
	 * @return 0 if the object was updated, -1 if nothing was updated
	 */
	int update(T t);
	/**
	 * Delete a T from the repository based on id
	 * @param id the T id
	 * @return 0 if the delete was done, -1 if nothing was deleted
	 */
	int delete(int id);
	/**
	 * Find a T in repository based on id
	 * @param id the T id
	 * @return the T object or null
	 */
	T find(int id);
	/**
	 * Returns a list of T from repository
	 */
	List<T> findAll();
	
}
