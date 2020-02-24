package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);




    @Before
    public void setUp(){
        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);


    }

    @Test
    public void addItemToCart() {
        User user = TestUtils.createUser("username", "password");
        Cart cart = TestUtils.createCart();
        user.setCart(cart);
        when(userRepo.findByUsername("username")).thenReturn(user);

        Item item1 = TestUtils.createItem(1L, "Item1", "Item1 Description", BigDecimal.TEN);

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(2);
        request.setUsername("username");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Cart returnedCart = response.getBody();

        assertNotNull(returnedCart);

        assertEquals(1, request.getItemId());
        assertEquals(2, request.getQuantity());
        assertEquals("username", request.getUsername());

        Mockito.verify(userRepo, times(1)).findByUsername("username");
        Mockito.verify(itemRepo, times(1)).findById(1L);
    }

    @Test
    public void addItemsWithInvalidUser() {
        when(userRepo.findByUsername("username")).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(2);
        request.setUsername("username");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void removeItemFromCart() {
        User user = TestUtils.createUser("username", "password");
        Cart cart = TestUtils.createCart();
        user.setCart(cart);
        when(userRepo.findByUsername("username")).thenReturn(user);

        Item item1 = TestUtils.createItem(1L, "Item 1", "Item 1 Description", BigDecimal.TEN);

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(2);
        request.setUsername("username");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Cart returnedCart = response.getBody();

        assertNotNull(returnedCart);
        assertEquals(returnedCart, cart);
        Mockito.verify(userRepo, times(1)).findByUsername("username");
        Mockito.verify(itemRepo, times(1)).findById(1L);
    }
}
