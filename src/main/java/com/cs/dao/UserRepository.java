package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
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
	
	
	public User findUserById(int id) {
		return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", new UserRowMapper(), id);
	}

	public void deleteUserById(int id) {
		String sql = MessageFormat.format("DELETE from users where id = {0}", id);
		update(sql);
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
			String address = rs.getString("address");
			Role role = Role.valueOf(rs.getString("role"));
			user = new User(userId, firstName, lastName, contact, email, role, address);
			return user;
		}
		
	}
	
	
    public void insertUser(User usr) {
        String sql = MessageFormat.format("INSERT INTO users(firstName,lastName,contact,email,role, address) values ({0},{1},{2},{3},{4},{5})",
                "'" + usr.getFirstName() + "'",
                "'" + usr.getLastName() + "'",
                "'" + usr.getContact() + "'",
                "'" + usr.getEmail() + "'",
                "'" + usr.getRole() + "'",
                "'" + usr.getAddress() + "'");

        update(sql);
    }
    
    public void update(String sql) {
        jdbcTemplate.update(sql);
    }
    
    
}
