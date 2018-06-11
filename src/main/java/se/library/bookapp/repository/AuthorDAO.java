package se.library.bookapp.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import se.library.bookapp.entities.Author;
import se.library.bookapp.mappers.AuthorRowMapper;


public class AuthorDAO implements CRUDOperations<Author>, Search<Author>{

	private JdbcTemplate jdbcTemplate;
	
	public AuthorDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Author> search(String searchString) {
		if (searchString.equals("")) {
			return null;
		}
		searchString = "%"+searchString+"%";
		String sqlLookForAuthor = "SELECT * FROM library.authors WHERE (authorFirstname || authorLastname) LIKE ?;";
		List<Author> searchAuthor = jdbcTemplate
				.query(new HelperPreparedStatementCreator(sqlLookForAuthor, searchString), new AuthorRowMapper());
		return searchAuthor;
	}

	@Override
	public int create(Author t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Author t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Author fetch(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Author> fetchAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
