package se.library.bookapp.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import se.library.bookapp.entities.Author;
import se.library.bookapp.entities.Book;
import se.library.bookapp.entities.BookWithAuthors;
import se.library.bookapp.mappers.AuthorRowMapper;
import se.library.bookapp.mappers.AuthorsBooksRowMapper;
import se.library.bookapp.mappers.BookRowMapper;

@Transactional
@Repository
public class LibraryDAO {

	private JdbcTemplate jdbcTemplate;

	public LibraryDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Helper class for making prepared statements. One time use. On creation it
	 * takes the sql string and the corresponding values for the statment to be
	 * created.
	 */
	class HelperPreparedStatementCreator implements PreparedStatementCreator {

		private String sql;
		private Object[] values;

		public HelperPreparedStatementCreator(String sql, Object... values) {
			this.sql = sql;
			this.values = values;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			PreparedStatement prepStmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < values.length; i++) {
				prepStmt.setObject(i + 1, values[i]);
			}
			return prepStmt;
		}

	}

	public List<Book> fetchAllBooks() {
		return jdbcTemplate.query("SELECT * FROM library.books;", new BookRowMapper());
	}

	public Book fetchBook(Integer bookId) {
		String sqlGetBook = "SELECT * FROM library.books WHERE bookId = ?";
		try {
			return jdbcTemplate.queryForObject(sqlGetBook, new Integer[] { bookId }, new BookRowMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			System.out.println(
					"LibraryDAO::fetchBook: An IncorrectResultSizeDataAccessException was thrown, probably because of an invalid ID. With message: "
							+ e.getMessage());
			return null;
		}
	}

	public List<Book> searchBook(String searchString) {
		if (searchString.equals("")) {
			return null;
		}
		searchString = "%"+searchString+"%";
		String sqlLookForBook = "SELECT * FROM library.books WHERE bookTitle LIKE ?;";
		return jdbcTemplate.query(new HelperPreparedStatementCreator(sqlLookForBook, searchString),
				new BookRowMapper());
	}

	public List<Author> searchAuthor(String searchString) {
		if (searchString.equals("")) {
			return null;
		}
		searchString = "%"+searchString+"%";
		String sqlLookForAuthor = "SELECT * FROM library.authors WHERE (authorFirstname || authorLastname) LIKE ?;";
		List<Author> searchAuthor = jdbcTemplate
				.query(new HelperPreparedStatementCreator(sqlLookForAuthor, searchString), new AuthorRowMapper());
		return searchAuthor;
	}

	public List<Book> fetchBooksForAuthor(int authorId) {
		String sqlGetBookIds = "SELECT bookId FROM library.authors_books WHERE authorId = ?";
		List<Integer> bookIds = jdbcTemplate.query(new HelperPreparedStatementCreator(sqlGetBookIds, authorId),
				new AuthorsBooksRowMapper("bookId"));
		String sqlGetBooks = "SELECT * FROM library.books WHERE bookId = ?";
		List<Book> allBooks = new ArrayList<>();
		PreparedStatementCreator prepStmtCreator;
		for (Integer bookId : bookIds) {
			prepStmtCreator = new HelperPreparedStatementCreator(sqlGetBooks, bookId);

			List<Book> books = jdbcTemplate.query(prepStmtCreator, new BookRowMapper());
			allBooks.addAll(books);
		}
		// Remove duplicates
		return allBooks.stream().distinct().collect(Collectors.toList());
	}

	public List<BookWithAuthors> fetchBooksWithAuthorsForAuthors(List<Author> authors) {
		List<BookWithAuthors> booksAndAuthors = new ArrayList<>();
		List<Book> books;

		for (Author author : authors) {
			books = fetchBooksForAuthor(author.getId());
			booksAndAuthors.addAll(fetchBooksWithAuthorsForBooks(books));

		}
		return booksAndAuthors;
	}

	public List<BookWithAuthors> fetchBooksWithAuthorsForBooks(List<Book> books) {
		List<BookWithAuthors> booksAndAuthors = new ArrayList<>();
		List<Author> authors;
		BookWithAuthors bookWithAuthors;

		for (Book book : books) {
			authors = fetchAuthorsForBook(book.getId());
			bookWithAuthors = new BookWithAuthors(book, authors);
			booksAndAuthors.add(bookWithAuthors);
		}
		return booksAndAuthors;
	}

	public BookWithAuthors fetchBookWithAuthors(int bookId) {
		Book book = fetchBook(bookId);
		if (book == null) {
			return null;
		}
		List<Author> authors = fetchAuthorsForBook(book.getId());
		if (authors.isEmpty()) {
			return null;
		}

		return new BookWithAuthors(book, authors);
	}

	public List<Author> fetchAuthorsForBook(int bookId) {
		String sqlGetAuthorIds = "SELECT authorId FROM library.authors_books WHERE bookId = ?";
		List<Integer> authorIds = jdbcTemplate.query(new HelperPreparedStatementCreator(sqlGetAuthorIds, bookId),
				new AuthorsBooksRowMapper("authorId"));
		String sqlGetAuthors = "SELECT * FROM library.authors WHERE authorId = ?";
		List<Author> authors = new ArrayList<>();
		PreparedStatementCreator prepStmtCreator;
		for (Integer authorId : authorIds) {
			prepStmtCreator = new HelperPreparedStatementCreator(sqlGetAuthors, authorId);
			// Just one author will be in list since IDs are unique
			List<Author> author = jdbcTemplate.query(prepStmtCreator, new AuthorRowMapper());
			authors.add(author.get(0));
		}

		return authors;
	}

	// TODO check if author exist in database, if not create it
	public int editBook(BookWithAuthors bookWithAuthors) {
		Book book = bookWithAuthors.getBook();
		List<Author> dbAuthors = fetchAuthorsForBook(book.getId());
		List<Author> authorsToAdd = new ArrayList<>();
		List<Author> authorsToRemove = new ArrayList<>();
		List<Author> authorsToUpdate = new ArrayList<>();
		String sqlUpdateBook = "UPDATE library.books SET bookTitle=?, bookDescription=?  WHERE bookId = ?;";
		jdbcTemplate.update(new HelperPreparedStatementCreator(sqlUpdateBook, book.getTitle(), book.getDescription(),
				book.getId()));

		boolean foundFormToDBMatch = false;
		for (Author formAuthor : bookWithAuthors.getAuthors()) {
			if (formAuthor.getId() == -1) {
				authorsToAdd.add(formAuthor);
				continue;
			}

		}
		for (Author dbAuthor : dbAuthors) {
			for (Author formAuthor : bookWithAuthors.getAuthors()) {
				if (dbAuthor.getId() == formAuthor.getId()) {
					foundFormToDBMatch = true;
					authorsToUpdate.add(formAuthor);
					break;
				}
			}
			if (!foundFormToDBMatch) {
				authorsToRemove.add(dbAuthor);
			}
			foundFormToDBMatch = false;
		}
		// Add new author to db
		authorsToAdd.forEach(author -> {
			int key = addAuthor(author);
			addBookToAuthorAssociation(book.getId(), key);
		});
		// Remove authors from db that were dissociated with the book
		authorsToRemove.forEach(author -> deleteAuthorToBookAssociation(author.getId(), book.getId()));

		// Update the authors that remains
		String sqlUpdateAuthors = "UPDATE library.authors SET authorfirstname=?, authorlastname=?  WHERE authorId = ?;";
		authorsToUpdate.forEach(author -> {
			jdbcTemplate.update(new HelperPreparedStatementCreator(sqlUpdateAuthors, author.getFirstName(),
					author.getLastName(), author.getId()));
		});

		return 0;
	}

	/**
	 * Adds a book to the db
	 * 
	 * @param book,
	 *            the book that should be added to db
	 * @return the key that was generated in db, or -1 if something went wrong
	 */
	private int addBook(Book book) {
		// Holds returned keys
		KeyHolder keyHolder = new GeneratedKeyHolder();
		// Insert book
		String sqlInsertBook = "INSERT INTO library.books(bookTitle, bookDescription)  VALUES (?,?);";
		int bookRows = jdbcTemplate.update(
				new HelperPreparedStatementCreator(sqlInsertBook, book.getTitle(), book.getDescription()), keyHolder);

		if (bookRows == 0) {
			System.err.println("Could not insert book");
			return -1;
		}
		return (int) keyHolder.getKeys().get("bookId");
	}

	/**
	 * Adds the author to the db
	 * 
	 * @param author,
	 *            and author object
	 * @return the key that was generated in db, or -1 if something went wrong
	 */
	private int addAuthor(Author author) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sqlInsertAuthor = "INSERT INTO library.authors(authorFirstName, authorLastName)  VALUES (?,?);";
		int authorRows = jdbcTemplate.update(
				new HelperPreparedStatementCreator(sqlInsertAuthor, author.getFirstName(), author.getLastName()),
				keyHolder);

		if (authorRows == 0) {
			System.err.println("Could not insert author");
			return -1;
		}
		return (int) keyHolder.getKeys().get("authorId");
	}

	/**
	 * Adds a row in the book to author association entity
	 * 
	 * @param bookId
	 *            the book id
	 * @param authorId
	 *            the author id
	 * @return if all was well 0, else -1
	 */
	private int addBookToAuthorAssociation(int bookId, int authorId) {
		String sqlInsertAuthorIdAndBookId = "INSERT INTO library.authors_books(authorId, bookId)  VALUES (?,?);";
		int bookToAuthorRows = jdbcTemplate
				.update(new HelperPreparedStatementCreator(sqlInsertAuthorIdAndBookId, authorId, bookId));
		if (bookToAuthorRows == 0) {
			System.err.println("Could not insert book to author association");
			return -1;
		}
		return 0;
	}

	/**
	 * Adds a book with corresponding author to the database
	 * 
	 * @param book
	 *            the book that should be inserted
	 * @param author
	 *            the author to the corresponding book
	 * @return -1 if something went wrong, 0 if the everything went okay
	 */
	public int addBookWithAuthors(Book book, List<Author> authors) {

		try {
			int bookKey = addBook(book);

			// Insert author
			for (Author author : authors) {
				int authorKey = addAuthor(author);
				// Insert bookId and authorId into associative entity
				addBookToAuthorAssociation(bookKey, authorKey);
			}
			// All updates were valid
			return 0;
		} catch (DataAccessException e) {
			System.out.println("Error: LibraryDAO::addBook:" + " A DataAccessException was thrown with message: "
					+ e.getMessage());
			return -1;
		}

	}

	private int deleteBookToAuthorAssociation(int bookId) {
		String sqlDeleteBookAuthorAssociation = "DELETE FROM library.authors_books WHERE bookId = ?;";
		int bookToAuthorRows = jdbcTemplate
				.update(new HelperPreparedStatementCreator(sqlDeleteBookAuthorAssociation, bookId));
		if (bookToAuthorRows == 0) {
			System.err.println("Could not delete book to author association");
			return -1;
		}
		return 0;
	}

	private int deleteAuthorToBookAssociation(int authorId, int bookId) {
		String sqlDeleteAuthorToBookAssociation = "DELETE FROM library.authors_books WHERE bookId = ? AND authorId = ?;";
		int bookToAuthorRows = jdbcTemplate
				.update(new HelperPreparedStatementCreator(sqlDeleteAuthorToBookAssociation, bookId, authorId));
		if (bookToAuthorRows == 0) {
			System.err.println("Could not delete author to book association");
			return -1;
		}
		return 0;
	}

	private int deleteBook(int bookId) {

		String sqlDeleteBook = "DELETE FROM library.books WHERE bookId = ?;";
		int bookRows = jdbcTemplate.update(new HelperPreparedStatementCreator(sqlDeleteBook, bookId));

		if (bookRows == 0) {
			System.err.println("Could not delete book");
			return -1;
		}
		return 0;
	}

	private int deleteAuthor(int authorId) {
		String sqlDeleteAuthor = "DELETE FROM library.authors WHERE authorId = ?;";
		int authorRows = jdbcTemplate.update(new HelperPreparedStatementCreator(sqlDeleteAuthor, authorId));

		if (authorRows == 0) {
			System.err.println("Could not delete author");
			return -1;
		}
		return 0;
	}

	public int removeBook(int bookId) {

		try {
			deleteBookToAuthorAssociation(bookId);
			deleteBook(bookId);

		} catch (DataAccessException e) {
			System.out.println("Error: LibraryDAO::removeBook:" + " A DataAccessException was thrown with message: "
					+ e.getMessage());
			return -1;
		}

		return 0;
	}

}
