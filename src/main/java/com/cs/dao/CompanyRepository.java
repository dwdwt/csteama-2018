package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cs.domain.Company;

@Repository
public class CompanyRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IndustryRepository industryRepo;
	
	public List<Company> findAllCompanies() {
		return jdbcTemplate.query("SELECT * FROM companies", new CompanyRowMapper());
	}
	
	public Company findCompanyByTickerSymbol(String tickerSymbol) {
		return jdbcTemplate.queryForObject("SELECT * FROM companies WHERE tickerSymbol = ?", new CompanyRowMapper(), tickerSymbol);
	}
	
	class CompanyRowMapper implements RowMapper<Company> {

		@Override
		public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
			Company company = null;
			String name = rs.getString("name");
			String tickerSymbol = rs.getString("tickerSymbol");
			String sectorName = rs.getString("sectorName");
			company = new Company(name, tickerSymbol, industryRepo.findIndustryByName(sectorName));
			return company;
		}
		
	}
}
