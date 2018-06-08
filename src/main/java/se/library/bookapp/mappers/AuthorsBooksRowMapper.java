package se.library.bookapp.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class AuthorsBooksRowMapper implements RowMapper<Integer> {
	
	private String columnToGet;
	
	public AuthorsBooksRowMapper(String columnToGet) {
		this.columnToGet = columnToGet;
	}

	@Override
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
		return rs.getInt(columnToGet);
	}

	

}
