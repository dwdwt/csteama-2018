package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cs.domain.Role;
import com.cs.domain.User;

@Repository
public class UserRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<User> findAllUsers() {
		return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
	}
	
	public User findUserById(int userId) {
		return jdbcTemplate.queryForObject("SELECT * FROM users WHERE userId = ?", new UserRowMapper(), userId);
	}
	
	class UserRowMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = null;
			int userId = rs.getInt("id");
			String firstName = rs.getString("firstName");
			String lastName = rs.getString("lastName");
			String contact = rs.getString("contact");
			String email = rs.getString("email");
			user = new User(userId, firstName, lastName, contact, email, Role.TRADER);
			return user;
		}
		
	}
}
