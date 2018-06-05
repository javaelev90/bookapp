package se.library.bookapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class LibraryDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Book> fetchAllBooks(){
		return jdbcTemplate.query("SELECT * FROM library.books;", new BookRowMapper());
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
			int bookRows = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					String sql = "INSERT INTO library.books(bookTitle, bookDescription)  VALUES (?,?);";
					PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, book.getTitle());
					preparedStatement.setString(2, book.getDescription());
					return preparedStatement;
				}
			}, keyHolder);
			
			if(bookRows == 0) {
				System.err.println("Could not insert book");
				return -1;
			}
			int bookKey = (int) keyHolder.getKeys().get("bookId");	
			
			keyHolder = new GeneratedKeyHolder();
			// Insert author
			int authorRows = jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					String sql = "INSERT INTO library.authors(authorFirstName, authorLastName)  VALUES (?,?);";
					PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, author.getFirstName());
					preparedStatement.setString(2, author.getLastName());
					return preparedStatement;
				}
			}, keyHolder);
			
			if(authorRows == 0) {
				System.err.println("Could not insert author");
				return -1;
			}
			int authorKey = (int) keyHolder.getKeys().get("authorId");	
			
			keyHolder = new GeneratedKeyHolder();
			// Insert bookId and authorId into associative entity
			int bookToAuthorRows = jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					String sql = "INSERT INTO library.authors_books(authorId, bookId)  VALUES (?,?);";
					PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setInt(1, authorKey);
					preparedStatement.setInt(2, bookKey);
					return preparedStatement;
				}
			}, keyHolder);
			if(bookToAuthorRows == 0) {
				System.err.println("Could not insert book to author association");
				return -1;
			}
			// All updates where valid
			return 0;
		} catch(DataAccessException e) {
			System.out.println("Error: LibraryDAO::addBook::execute::doInTransaction:"+" A DataAccessException was thrown with message: "+e.getMessage());
			return -1;
		} 

	}
	
	
}
