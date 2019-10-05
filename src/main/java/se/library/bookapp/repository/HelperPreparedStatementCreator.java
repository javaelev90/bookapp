package se.library.bookapp.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.PreparedStatementCreator;

public class HelperPreparedStatementCreator implements PreparedStatementCreator {

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