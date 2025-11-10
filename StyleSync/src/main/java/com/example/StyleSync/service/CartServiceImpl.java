package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.cart.CartResponse;
import com.example.StyleSync.entity.Cart;
import com.example.StyleSync.entity.CartItem;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.entity.User;
import com.example.StyleSync.exceptions.cart.CartIsAlreadyEmpty;
import com.example.StyleSync.exceptions.product.ProductNotFoundException;
import com.example.StyleSync.exceptions.user.UserNotFoundException;
import com.example.StyleSync.mapper.Cart_CartItemMapper;
import com.example.StyleSync.mapper.ProductMapper;
import com.example.StyleSync.repository.CartItemRepository;
import com.example.StyleSync.repository.CartRepository;
import com.example.StyleSync.repository.ProductRepository;
import com.example.StyleSync.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final Cart_CartItemMapper mapper;

    public CartServiceImpl(ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, UserRepository userRepository, ProductMapper mapper, Cart_CartItemMapper mapper1) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;

        this.mapper = mapper1;
    }

    @Override
    public void addItemToCart(String email, Integer productId, int quantity) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found."));
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found."));

        Cart cart = user.getCart();
        if(cart == null){
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            user.setCart(cart);
        }

        Optional<CartItem> existingItem = user.getCart().getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId)).findFirst();

        if(existingItem.isPresent()){
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else{
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void removeProductFromCart(String email, Integer productId) {
    User user = userRepository.findUserByEmail(email)
            .orElseThrow(()-> new RuntimeException("User not found"));
    Cart cart = user.getCart();
    if(cart == null || cart.getItems() == null){
        throw new RuntimeException("The cart is empty.");
    }

    CartItem itemToRemove = cart.getItems().stream()
            .filter(ex ->ex.getProduct().getId().equals(productId)).findFirst()
            .orElseThrow(()-> new RuntimeException("Item not found in cart."));

    cart.getItems().remove(itemToRemove);
    cartItemRepository.delete(itemToRemove);

    cartRepository.save(cart);
    }

    @Override
    public void updateProductQuantity(String email, Integer productId, int newQuantity) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not found."));
        Cart cart = user.getCart();
        if(cart == null || cart.getItems() == null){
            throw new RuntimeException("The cart is empty");
        }

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(()-> new ProductNotFoundException("Product not found in cart."));

        if(newQuantity <= 0){
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else{
            cartItem.setQuantity(newQuantity);
        }

        cartRepository.save(cart);
    }

    @Override
    public CartResponse getUserCart(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not found exception."));

        Cart cart = user.getCart();
        if(cart == null || cart.getItems() == null || cart.getItems().isEmpty()){
            throw new CartIsAlreadyEmpty("Cart is empty.");
        }

      return mapper.toCartResponse(cart);
    }

    @Override
    public void clearCart(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not found."));

        Cart cart = user.getCart();
        if(cart == null || cart.getItems() == null || cart.getItems().isEmpty()){
            throw new CartIsAlreadyEmpty("Cart is already empty.");
        }

        cart.getItems().clear(); // removing the items from memory
        cartRepository.save(cart);
    }
}
