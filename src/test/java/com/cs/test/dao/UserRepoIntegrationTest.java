package com.cs.test.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.dao.UserRepository;
import com.cs.domain.Role;
import com.cs.domain.User;

import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class UserRepoIntegrationTest {

	
    @Autowired
    UserRepository userRepository;

    @Test
    public void canFindAllUsers() {
        assertThat(userRepository.findAllUsers().size(), not(0));
    }

    @Test
    public void canAddUserAndIDWillBeAutoIncrement() {
        User user = new User(4,"aa","Na","123","jondoe1@gmail.com",Role.TRADER,"smu");
        userRepository.insertUser(user);
        assertThat(userRepository.findUserById(4), samePropertyValuesAs(user));
    }

    @Test (expected = EmptyResultDataAccessException.class)
    public void canDeleteUser() {
        User user = new User("someName","someLastName","12345667","jondoe1@gmail.com",Role.TRADER,"smu");
        userRepository.insertUser(user);
        User userFromRepo = userRepository.findAllUsers().stream().filter(o-> o.getFirstName().equals(user.getFirstName()) && o.getLastName().equals(user.getLastName())).collect(Collectors.toList()).get(0);
        assert userRepository.findUserById(userFromRepo.getId()) != null;
        userRepository.deleteUserById(userFromRepo.getId());
        userRepository.findUserById(userFromRepo.getId());
    }
    
    
}
