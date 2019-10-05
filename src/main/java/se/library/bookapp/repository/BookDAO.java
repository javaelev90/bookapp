package se.library.bookapp.repository;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import se.library.bookapp.entities.Book;
import se.library.bookapp.mappers.BookRowMapper;


@Transactional
@Repository
public class BookDAO implements CRUDEntity<Book>, Search<Book>{

	private JdbcTemplate jdbcTemplate;
	
	public BookDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Book> search(String searchString) {

		if (searchString.equals("")) {
			return null;
		}
		searchString = "%"+searchString+"%";
		String sqlLookForBook = "SELECT * FROM library.books WHERE bookTitle LIKE ?;";
		return jdbcTemplate.query(new HelperPreparedStatementCreator(sqlLookForBook, searchString),
				new BookRowMapper());
	}

	@Override
	public int create(Book book) {
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

	@Override 
	public int update(Book book) {
		String sqlUpdateBook = "UPDATE library.books SET bookTitle=?, bookDescription=?  WHERE bookId = ?;";
		int updatedRows = jdbcTemplate.update(new HelperPreparedStatementCreator(sqlUpdateBook, book.getTitle(), book.getDescription(),
				book.getId()));
		if(updatedRows == 0) {
			System.err.println("BookDAO::update: Could not update book row for id: "+book.getId());
			return -1;
		}
		return 0;
	}

	
	@Override 
	public int delete(int id) {
		String sqlDeleteBook = "DELETE FROM library.books WHERE bookId = ?;";
		int bookRows = jdbcTemplate.update(new HelperPreparedStatementCreator(sqlDeleteBook, id));

		if (bookRows == 0) {
			System.err.println("BookDAO::delete: Could not delete book row for id: "+id);
			return -1;
		}
		return 0;
	}

	
	@Override 
	public Book find(int id) {
		String sqlGetBook = "SELECT * FROM library.books WHERE bookId = ?";
		try {
			return jdbcTemplate.queryForObject(sqlGetBook, new Integer[] { id }, new BookRowMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			System.out.println(
					"BookDAO::find: An IncorrectResultSizeDataAccessException was thrown, probably because of an invalid ID. With message: "
							+ e.getMessage());
			return null;
		}
	}

	@Override 
	public List<Book> findAll() {
		return jdbcTemplate.query("SELECT * FROM library.books;", new BookRowMapper());
	}

	
}
