package com.cs.test.dao;

import static com.cs.domain.Operation.OPEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.Is.is;

import com.cs.domain.Role;
import com.cs.domain.Transaction;
import com.cs.domain.User;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Test
    public void canAddUserAndIDWillBeAutoIncrement() {
        User user = new User(4,"peter","parker","12345678","pp@gmail.com",Role.TRADER,"someWhere");
        userRepository.insertUser(user);
        assertThat(userRepository.findUserById(4), samePropertyValuesAs(user));
    }

    @Test (expected = EmptyResultDataAccessException.class)
    public void canDeleteUser() {
        assert userRepository.findUserById(3) != null;
        userRepository.deleteUserById(3);
        userRepository.findUserById(3);
    }
    
    
}
