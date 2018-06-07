package se.library.bookapp;


import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryDAOTest {
	
//	@Mock
	private LibraryDAO libraryDAO;

	
	 
//	public void setUp() {
//		
//		JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
//		List<Book> books = new ArrayList<>();
//		Book book = new Book();
//		book.setTitle("Consider Phlebas");
//		book.setDescription("A space opera");
//		books.add(book);
//		book = new Book();
//		book.setTitle("Vänner förevigt");
//		book.setDescription("En riktig Klassiker");
//		books.add(book);
//		when(jdbcTemplate.query("SELECT * FROM library.authors_books WHERE bookId = ?")).
//		
//		libraryDAO = new LibraryDAO(jdbcTemplate);
//	}
	
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
	
	/**
	 * Testing that 
	 */
	@Test
	public void testFetchAllBooks() {
		Book book = new Book();
		book.setTitle("Consider Phlebas");
		book.setDescription("A space opera");
		Author author = new Author();
		author.setFirstName("Lars");
		author.setLastName("Wipp");
		libraryDAO.addBook(book, author);
		book = new Book();
		book.setTitle("Brake a leg");
		book.setDescription("Cool beans");
		author = new Author();
		author.setFirstName("Steven");
		author.setLastName("Khan");
		libraryDAO.addBook(book, author);
		List<Book> books = libraryDAO.fetchAllBooks();
		assertTrue(books.get(0).getTitle().equals("Consider Phlebas") && books.get(1).getDescription().equals("Cool beans"));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
