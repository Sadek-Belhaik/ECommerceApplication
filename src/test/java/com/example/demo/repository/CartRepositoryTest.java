package com.example.demo.repository;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.*;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void injected_components_are_not_null(){
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
        assertNotNull(testEntityManager);
        assertNotNull(cartRepository);
    }
    @Test
    public void review_repository_returns_reviews_by_product(){
        // create user
        User user = new User();
        user.setUsername("userTest");
        user.setPassword("password");
        entityManager.persist(user);

        //creat item
        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setPrice(BigDecimal.valueOf(1.99));
        item1.setDescription("itemDescription 1");
        entityManager.persist(item1);

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setPrice(BigDecimal.valueOf(2.99));
        item2.setDescription("itemDescription 2");
        entityManager.persist(item2);

        List<Item> items = itemRepository.findByName(user.getUsername());


        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(items);
        entityManager.persist(cart);

        // check to make sure things are as expected
        assertNotNull(cart);
        assertEquals("userTest",cart.getUser().getUsername());
    }
}
