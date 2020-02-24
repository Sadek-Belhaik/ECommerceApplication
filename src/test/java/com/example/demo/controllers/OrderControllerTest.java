package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    private OrderController orderController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private OrderRepository orderRepo;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitOder() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");

        Cart cart = new Cart();

        Item item1 = TestUtils.createItem(1L, "Item1", "Item 1 Description", BigDecimal.valueOf(10));
        cart.addItem(item1);
        Item item2 = TestUtils.createItem(2L, "Item2", "Item 2 Description", BigDecimal.valueOf(20));
        cart.addItem(item2);

        cart.setUser(user);

        user.setCart(cart);
        when(userRepo.findByUsername("username")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("username");
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();

        assertNotNull(userOrder);
        assertEquals(Arrays.asList(item1, item2), userOrder.getItems());
        assertEquals(user, userOrder.getUser());
        //assertEquals(BigDecimal.valueOf(30), userOrder.getTotal());
        Mockito.verify(userRepo, times(1)).findByUsername("username");
        Mockito.verify(orderRepo, times(1)).save(userOrder);

    }

    @Test
    public void submitWithInvalidUsername() {
        when(userRepo.findByUsername("username")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("username");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        Mockito.verify(userRepo, times(1)).findByUsername("username");
        Mockito.verify(orderRepo , never()).save(Mockito.any());
    }

    @Test
    public void getHistoryOrder() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");

        Item item1 = TestUtils.createItem(1L, "Item 1", "Item 1 Description", BigDecimal.TEN);

        UserOrder userOrder1 = new UserOrder();
        userOrder1.setUser(user);
        userOrder1.setItems(Arrays.asList(item1));
        userOrder1.setTotal(BigDecimal.TEN);

        UserOrder userOrder2 = new UserOrder();
        userOrder2.setUser(user);
        userOrder2.setItems(Arrays.asList(item1));
        userOrder2.setTotal(BigDecimal.TEN);

        when(userRepo.findByUsername("username")).thenReturn(user);
        when(orderRepo.findByUser(user)).thenReturn(Arrays.asList(userOrder1, userOrder2));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("username");
        assertNotNull(response);
        List<UserOrder> responseBody = response.getBody();
        assertEquals(Arrays.asList(userOrder1, userOrder2), responseBody);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Mockito.verify(userRepo, times(1)).findByUsername("username");
        Mockito.verify(orderRepo, times(1)).findByUser(user);
    }

    @Test
    public void getHistoryOrderWithInvalidUsername() {
        when(userRepo.findByUsername(ArgumentMatchers.any())).thenReturn(null);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("username");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }
}
