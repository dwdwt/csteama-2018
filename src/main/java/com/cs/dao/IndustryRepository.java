package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cs.domain.Industry;

@Repository
public class IndustryRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Industry> findAllIndustries() {
		return jdbcTemplate.query("SELECT * FROM sectors", new IndustryRowMapper());
	}
	
	public Industry findIndustryByName(String name) {
		return jdbcTemplate.queryForObject("SELECT * FROM industries WHERE name = ?", new IndustryRowMapper(), name);
	}
	
	class IndustryRowMapper implements RowMapper<Industry> {

		@Override
		public Industry mapRow(ResultSet rs, int rowNum) throws SQLException {
			Industry industry = null;
			String name = rs.getString("name");
			String description = rs.getString("description");
			industry = new Industry(name, description);
			return industry;
		}
		
	}
}
