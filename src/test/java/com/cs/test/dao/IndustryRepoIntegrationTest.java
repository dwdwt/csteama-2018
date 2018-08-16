package com.cs.test.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.Is.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.dao.IndustryRepository;
import com.cs.domain.Industry;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class IndustryRepoIntegrationTest {

    @Autowired
    IndustryRepository industryRepo;

    //Story 9 Tests
    @Test
    public void findAllIndustries() {
    	assertThat(industryRepo.findAllIndustries().size(), is(3));
    }
    
    @Test
    public void findIndustryByName() {
    	Industry industry = new Industry("IT Services", "Services");
    	assertThat(industryRepo.findIndustryByName("IT Services"), samePropertyValuesAs(industry));
    }
    
    @Test
    public void updateIndustryInformation() {
    	Industry industry = new Industry("IT Services", "Technology Services");
    	industryRepo.updateIndustryInformation(industry);
    	try {
    		assertThat(industryRepo.findIndustryByName("IT Services"), samePropertyValuesAs(industry));
    	} finally {
    		// Database rollback
    		industry.setDescription("Services");
    		industryRepo.updateIndustryInformation(industry);
    	}
    }
    
    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteIndustry() {
    	industryRepo.insertIndustry("insert into industries(name,description) values ('Testing Services', 'Services')");
    	industryRepo.deleteIndustry("Testing Services");
    	industryRepo.findIndustryByName("Testing Services");
    }
    
}
