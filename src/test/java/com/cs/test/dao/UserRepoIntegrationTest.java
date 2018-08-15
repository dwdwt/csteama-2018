package com.cs.test.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.dao.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class UserRepoIntegrationTest {

	
    @Autowired
    UserRepository userRepository;

    @Test
    public void canFindAllUsers() {
        assertThat(userRepository.findAllUsers().size(), is(3));
    }
    
    
}
