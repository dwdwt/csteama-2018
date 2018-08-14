package com.cs.test.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.dao.OrderRepository;
import com.cs.domain.Company;
import com.cs.domain.Industry;
import com.cs.domain.Order;
import com.cs.domain.Role;
import com.cs.domain.User;

import static org.hamcrest.Matchers.samePropertyValuesAs;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class OrderRepoIntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void canFindAllOrders() {
        assertThat(orderRepository.findAllOrders().size(), is(3));
    }

    
    @Test
    public void canFindByOrderId() {
    	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
    	Industry industry = new Industry("IT Services","Services");
    	Company company = new Company("ABC.HK","ABC CO HONG KONG", industry);
    	User user = new User(1,"Jon","Doe", "1234","jondoe@gmail.com", Role.TRADER);
    	assertThat(orderRepository.getOrder(1), samePropertyValuesAs(new Order(1,company,"B","LIMIT",10.0,5,formatter.parseDateTime("16/08/2018 10:17:23"),user,"OPENED")));
    }
    

