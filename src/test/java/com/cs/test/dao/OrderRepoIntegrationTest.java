package com.cs.test.dao;

import com.cs.Csteama2018Application;
import com.cs.dao.TransactionRepository;
import com.cs.dao.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class OrderRepoIntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void canFindAllOrders() {
        assertThat(orderRepository.findAll().size(), is(3));
    }
}
