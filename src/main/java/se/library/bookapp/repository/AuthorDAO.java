package se.library.bookapp.repository;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.library.bookapp.entities.Author;
import se.library.bookapp.mappers.AuthorRowMapper;

@Transactional
@Repository
public class AuthorDAO implements CRUDEntity<Author>, Search<Author>{

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
	public int create(Author author) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sqlInsertAuthor = "INSERT INTO library.authors(authorFirstName, authorLastName)  VALUES (?,?);";
		int authorRows = jdbcTemplate.update(
				new HelperPreparedStatementCreator(sqlInsertAuthor, author.getFirstName(), author.getLastName()),
				keyHolder);
		if (authorRows == 0) {
			System.err.println("AuthorDAO::create: Could not create author");
			return -1;
		}
		return (int) keyHolder.getKeys().get("authorId");
	}

	@Override
	public int update(Author author) {
		String sqlUpdateAuthors = "UPDATE library.authors SET authorfirstname=?, authorlastname=?  WHERE authorId = ?;";
		int authorRows = jdbcTemplate.update(new HelperPreparedStatementCreator(sqlUpdateAuthors, author.getFirstName(), author.getLastName(),
				author.getId()));
		if (authorRows == 0) {
			System.err.println("AuthorDAO::update: Could not update author row for id: "+author.getId());
			return -1;
		}
		return 0;
	}

	@Override
	public int delete(int id) {
		String sqlDeleteAuthor = "DELETE FROM library.authors WHERE authorId = ?;";
		int authorRows = jdbcTemplate.update(new HelperPreparedStatementCreator(sqlDeleteAuthor, id));

		if (authorRows == 0) {
			System.err.println("AuthorDAO::delete: Could not delete author row for id: "+id);
			return -1;
		}
		return 0;
	}

	@Override
	public Author find(int id) {
		String sqlGetAuthors = "SELECT * FROM library.authors WHERE authorId = ?";
		try {
			return jdbcTemplate.queryForObject(sqlGetAuthors, new Integer[] { id }, new AuthorRowMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			System.out.println(
					"AuthorDAO::find: An IncorrectResultSizeDataAccessException was thrown, probably because of an invalid ID. With message: "
							+ e.getMessage());
			return null;
		}
	}

	@Override
	public List<Author> findAll() {
		return jdbcTemplate.query("SELECT * FROM library.authors", new AuthorRowMapper());
	}

}
