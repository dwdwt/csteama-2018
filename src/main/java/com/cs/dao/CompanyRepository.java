package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	public List<Company> findAllCompanies(Map<String,String> filterMap) {
		String filterQuery = "SELECT * FROM COMPANIES WHERE ";
		if (filterMap != null) {
    		Iterator updateIterator = filterMap.entrySet().iterator();
    		
    		while (updateIterator.hasNext()) {
    		    Map.Entry pair = (Map.Entry)updateIterator.next();
    		    String key = (String) pair.getKey();
    		    String value = (String)pair.getValue();
    		    if (value != null && key.equals("filterByTickerSymbol")) {
    		    	filterQuery += ("tickersymbol like '" + value + "%' AND ");
    		    } else if (value != null && key.equals("filterByCompanyName")){
    		    	filterQuery += ("name like '" + value + "%' AND ");
    		    }
    		}
    		
    		// Remove the last AND
    		filterQuery = filterQuery.substring(0, filterQuery.length()-4);
    		return jdbcTemplate.query(filterQuery, new CompanyRowMapper());
    		
    	}
		
		return jdbcTemplate.query("SELECT * FROM companies", new CompanyRowMapper());
	}
	
	public Company findCompanyByTickerSymbol(String tickerSymbol) {
		return jdbcTemplate.queryForObject("SELECT * FROM companies WHERE tickerSymbol = ?", new CompanyRowMapper(), tickerSymbol);
	}
	
	public Company updateCompanyByTickerSymbol(Map<String,Object> updateMap,String tickerSymbol) {
		String query = "UPDATE companies SET ";
		
		if (updateMap != null) {
    		Iterator updateIterator = updateMap.entrySet().iterator();
    		
    		while (updateIterator.hasNext()) {
    		    Map.Entry pair = (Map.Entry)updateIterator.next();
    		    String key = (String) pair.getKey();
    		    Object value = pair.getValue();
    		    if (value != null && value instanceof String) {
    		    	query += (key + "='" + value + "',");
    		    } else {
    		    	query += (key + "=" + value + ",");
    		    }
    		}
    		
    		// Remove the last ,
    		query = query.substring(0, query.length()-1) + " WHERE tickerSymbol = ?";
    		jdbcTemplate.update(query, tickerSymbol);
    		
    	}
		
		String newTickerSymbol = tickerSymbol;
		if(updateMap.containsKey("tickerSymbol")) newTickerSymbol = (String) updateMap.get("tickerSymbol");
		return findCompanyByTickerSymbol(newTickerSymbol);
	}
	
	public void deleteCompany(Company company) {
		String query = "DELETE FROM COMPANIES WHERE tickersymbol = '" + company.getTickerSymbol() + "'";
		jdbcTemplate.update(query);
	}
	
	public List<Company> findCompaniesByIndustry(String industryName) {
		return jdbcTemplate.query("SELECT * FROM companies WHERE industryName = ?", new CompanyRowMapper(), industryName);

	}
	
	class CompanyRowMapper implements RowMapper<Company> {

		@Override
		public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
			Company company = null;
			String name = rs.getString("name");
			String tickerSymbol = rs.getString("tickerSymbol");
			String sectorName = rs.getString("industryName");
			company = new Company(tickerSymbol, name, industryRepo.findIndustryByName(sectorName));
			return company;
		}
		
	}
}
