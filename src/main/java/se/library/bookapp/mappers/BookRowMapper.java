package se.library.bookapp.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import se.library.bookapp.entities.Book;

public class BookRowMapper implements RowMapper<Book> {

	@Override
	public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Book book = new Book();
		book.setId(rs.getInt("bookid"));
		book.setTitle(rs.getString("booktitle"));
		book.setDescription(rs.getString("bookdescription"));
		return book; 
	}


}
