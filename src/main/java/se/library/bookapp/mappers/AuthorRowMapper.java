package se.library.bookapp.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.library.bookapp.model.Author;

public class AuthorRowMapper implements RowMapper<Author>{

	@Override
	public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
		Author author = new Author();
		author.setId(rs.getInt("authorid"));
		author.setFirstName(rs.getString("authorfirstname"));
		author.setLastName(rs.getString("authorlastname"));
		return author;
	}

}
