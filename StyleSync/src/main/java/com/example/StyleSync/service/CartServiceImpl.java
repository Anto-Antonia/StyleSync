package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.cart.CartResponse;
import com.example.StyleSync.entity.Cart;
import com.example.StyleSync.entity.CartItem;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.entity.User;
import com.example.StyleSync.mapper.ProductMapper;
import com.example.StyleSync.repository.CartItemRepository;
import com.example.StyleSync.repository.CartRepository;
import com.example.StyleSync.repository.ProductRepository;
import com.example.StyleSync.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductMapper mapper;

    public CartServiceImpl(ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, UserRepository userRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public void addItemToCart(Integer userId, Integer productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found."));
        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found."));

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

    @Override
    public void removeProductFromCart(Integer userId, Integer productId) {
    User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
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
    public void updateProductQuantity(Integer userId, Integer productId, int newQuantity) {

    }

    @Override
    public CartResponse getUserCat(Integer userId) {
        return null;
    }

    @Override
    public void clearCart(Integer userId) {

    }
}
