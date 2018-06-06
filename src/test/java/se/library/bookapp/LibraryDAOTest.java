package se.library.bookapp;


import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryDAOTest {

	
	
	
	@Autowired
	private LibraryDAO libraryDAO;

	
	/**
	 * Testing to add a valid book and author
	 */
	@Test
	public void testAddBookCase1() {
		Book book = new Book();
		book.setTitle("Consider Phlebas");
		book.setDescription("A space opera");
		Author author = new Author();
		author.setFirstName("Lars");
		author.setLastName("Wipp");
		int retVal = libraryDAO.addBook(book, author);
		assertTrue(retVal == 0);
	}
	
	/**
	 * Test adding book when one value is not set(book title), i.e. null
	 */
	@Test
	public void testAddBookCase2() {
		Book book = new Book();
		book.setDescription("A space opera");
		Author author = new Author();
		author.setFirstName("Lars");
		author.setLastName("Wipp");
		int retVal = libraryDAO.addBook(book, author);
		assertTrue(retVal == -1);
	}
	
	/**
	 * Test adding book when one value is not set(author lastname), i.e. null
	 */
	@Test
	public void testAddBookCase3() {
		Book book = new Book();
		book.setTitle("Consider Phlebas");
		book.setDescription("A space opera");
		Author author = new Author();
		author.setFirstName("Lars");
		int retVal = libraryDAO.addBook(book, author);
		assertTrue(retVal == -1);
	}
	
	
	@Test
	public void testFetchAllBooks() {
		Book book = new Book();
		book.setTitle("Consider Phlebas");
		book.setDescription("A space opera");
		Author author = new Author();
		author.setFirstName("Lars");
		author.setLastName("Wipp");
		List<Book> books = libraryDAO.fetchAllBooks();
		books.forEach(System.out::println);
		assertTrue(books.get(0).getTitle().equals("Consider Phlebas") && books.get(0).getDescription().equals("A space opera"));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
