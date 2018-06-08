package se.library.bookapp;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class LibraryDAO {

	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public LibraryDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * Helper class for making prepared statements. One time use.
	 * On creation it takes the sql string and the corresponding values for the statment to be created. 
	 */
	class HelperPreparedStatementCreator implements PreparedStatementCreator{

		private String sql;
		private Object[] values;
		
		public HelperPreparedStatementCreator(String sql, Object... values) {
			this.sql = sql;
			this.values = values;
		}
		
		@Override
		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			PreparedStatement prepStmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for(int i = 0; i < values.length; i++) {
				prepStmt.setObject(i+1, values[i]);
			}
			return prepStmt;
		}
		
	}
	
	public List<Book> fetchAllBooks(){
		return jdbcTemplate.query("SELECT * FROM library.books;", new BookRowMapper());
	}
	
	public Book fetchBook(int bookId) {
		String sqlGetBook = "SELECT * FROM library.books WHERE bookId = ?";
		return jdbcTemplate.queryForObject(sqlGetBook, new Integer[] {bookId}, new BookRowMapper());
	}
	
	
	public List<Book> searchBook(String searchString){
		if(searchString.equals("")) {
			return null;
		}
		String sqlLookForBook = "SELECT * FROM library.books WHERE to_tsvector(bookTitle) @@ plainto_tsquery(?);";
		return jdbcTemplate.query(new HelperPreparedStatementCreator(
				sqlLookForBook, searchString), new BookRowMapper());
	}
	
	public List<Author> searchAuthor(String searchString){
		if(searchString.equals("")) {
			return null;
		}
		String sqlLookForAuthor = "SELECT * FROM library.authors WHERE to_tsvector(authorFirstname || ' ' || authorLastname) @@ plainto_tsquery(?);";
		List<Author> searchAuthor = jdbcTemplate.query(new HelperPreparedStatementCreator(
				sqlLookForAuthor, searchString), new AuthorRowMapper());
		return searchAuthor;
	} 
	
	
	public List<Book> fetchBooksForAuthor(int authorId){
		String sqlGetBookIds = "SELECT bookId FROM library.authors_books WHERE authorId = ?";
		List<Integer> bookIds = jdbcTemplate.query(new HelperPreparedStatementCreator(
				sqlGetBookIds, authorId), new AuthorsBooksRowMapper("bookId"));
		String sqlGetBooks = "SELECT * FROM library.books WHERE bookId = ?";
		List<Book> allBooks = new ArrayList<>();
		PreparedStatementCreator prepStmtCreator;
		for(Integer bookId : bookIds) {
			prepStmtCreator = new HelperPreparedStatementCreator(sqlGetBooks, bookId);

			List<Book> books = jdbcTemplate.query(prepStmtCreator, new BookRowMapper());
			allBooks.addAll(books);
		}
		//Remove duplicates
		return allBooks.stream().distinct().collect(Collectors.toList());
	}
	
	public List<BookWithAuthors> fetchBooksWithAuthorsForAuthors(List<Author> authors){
		List<BookWithAuthors> booksAndAuthors = new ArrayList<>();
		List<Book> books; 
		
		for(Author author : authors) {
			books = fetchBooksForAuthor(author.getId());
			booksAndAuthors.addAll(fetchBooksWithAuthorsForBooks(books));

		}
		return booksAndAuthors;
	}
	
	public List<BookWithAuthors> fetchBooksWithAuthorsForBooks(List<Book> books){
		List<BookWithAuthors> booksAndAuthors = new ArrayList<>();
		List<Author> authors; 
		BookWithAuthors bookWithAuthors;
		
		for(Book book : books) {
			authors = fetchAuthorsForBook(book.getId());
			bookWithAuthors = new BookWithAuthors(book, authors);
			booksAndAuthors.add(bookWithAuthors);
		}
		return booksAndAuthors;
	}
	
	public BookWithAuthors fetchBookWithAuthors(int bookId){
		Book book = fetchBook(bookId);
		if(book == null) {
			return null;
		}
		List<Author> authors = fetchAuthorsForBook(book.getId());
		if(authors.isEmpty()) {
			return null;
		}

		return new BookWithAuthors(book, authors);
	}

	public List<Author> fetchAuthorsForBook(int bookId){
		String sqlGetAuthorIds = "SELECT authorId FROM library.authors_books WHERE bookId = ?";
		List<Integer> authorIds = jdbcTemplate.query(new HelperPreparedStatementCreator(
				sqlGetAuthorIds, bookId), new AuthorsBooksRowMapper("authorId"));
		String sqlGetAuthors = "SELECT * FROM library.authors WHERE authorId = ?";
		List<Author> authors = new ArrayList<>();
		PreparedStatementCreator prepStmtCreator;
		for(Integer authorId : authorIds) {
			prepStmtCreator = new HelperPreparedStatementCreator(sqlGetAuthors, authorId);
			//Just one author will be in list since IDs are unique
			List<Author> author = jdbcTemplate.query(prepStmtCreator, new AuthorRowMapper());
			authors.add(author.get(0));
		}
		
		return authors;
	}
	
	/**
	 * Adds a book with corresponding author to the database
	 * 
	 * @param book the book that should be inserted
	 * @param author the author to the corresponding book
	 * @return -1 if something went wrong, 0 if the everything went okay
	 */
	public int addBook(Book book, Author author) {
		
		try {
			// Holds returned keys
			KeyHolder keyHolder = new GeneratedKeyHolder();
			// Insert book
			String sqlInsertBook = "INSERT INTO library.books(bookTitle, bookDescription)  VALUES (?,?);";
			int bookRows = jdbcTemplate.update(new HelperPreparedStatementCreator(
					sqlInsertBook, book.getTitle(),book.getDescription()), keyHolder);
			
			if(bookRows == 0) {
				System.err.println("Could not insert book");
				return -1;
			}
			int bookKey = (int) keyHolder.getKeys().get("bookId");	
			
			keyHolder = new GeneratedKeyHolder();
			// Insert author
			String sqlInsertAuthor = "INSERT INTO library.authors(authorFirstName, authorLastName)  VALUES (?,?);";
			int authorRows = jdbcTemplate.update(new HelperPreparedStatementCreator(
					sqlInsertAuthor, author.getFirstName(), author.getLastName()), keyHolder);
			
			if(authorRows == 0) {
				System.err.println("Could not insert author");
				return -1;
			}
			int authorKey = (int) keyHolder.getKeys().get("authorId");	
			
			keyHolder = new GeneratedKeyHolder();
			// Insert bookId and authorId into associative entity
			String sqlInsertAuthorIdAndBookId = "INSERT INTO library.authors_books(authorId, bookId)  VALUES (?,?);";
			int bookToAuthorRows = jdbcTemplate.update(new HelperPreparedStatementCreator(
					sqlInsertAuthorIdAndBookId, authorKey, bookKey), keyHolder);
			if(bookToAuthorRows == 0) {
				System.err.println("Could not insert book to author association");
				return -1;
			}
			// All updates were valid
			return 0;
		} catch(DataAccessException e) {
			System.out.println("Error: LibraryDAO::addBook::execute::doInTransaction:"+" A DataAccessException was thrown with message: "+e.getMessage());
			return -1;
		} 

	}
	
	
}
