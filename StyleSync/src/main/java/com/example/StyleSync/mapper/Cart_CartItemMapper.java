//package com.example.StyleSync.mapper;
//
//import com.example.StyleSync.dto.request.cart.CartItemRequest;
//import com.example.StyleSync.dto.request.cart.CartRequest;
//import com.example.StyleSync.dto.response.cart.CartItemResponse;
//import com.example.StyleSync.dto.response.cart.CartResponse;
//import com.example.StyleSync.entity.Cart;
//import com.example.StyleSync.entity.CartItem;
//import com.example.StyleSync.entity.Product;
//import com.example.StyleSync.entity.User;
//import com.example.StyleSync.repository.ProductRepository;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class Cart_CartItemMapper {
//    private final ProductRepository productRepository;
//
//    public Cart_CartItemMapper(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }
//
//    public CartItem fromCartItemRequest(CartItemRequest request, Cart cart){
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(()-> new RuntimeException("Product not found"));
//
//        CartItem cartItem = new CartItem();
//        cartItem.setProduct(product);
//        cartItem.setQuantity(request.getQuantity());
//        cartItem.setCart(cart);
//
//        return cartItem;
//    }
//
//    public CartItemResponse toCartItemResponse(CartItem cartItem){
//        Product product = cartItem.getProduct();
//
//        CartItemResponse response = new CartItemResponse();
//        response.setId(cartItem.getId());
//        response.setProductId(product.getId());
//        response.setProductName(product.getProductName());
//        response.setPrice(product.getPrice());
//        response.setQuantity(cartItem.getQuantity());
//        response.setTotalPrice(product.getPrice() * cartItem.getQuantity());
//
//        return response;
//    }
//
//    public Cart fromCartRequest(CartRequest cartRequest, User user){
//        Cart cart = new Cart();
//        cart.setUser(user);
//
//        List<CartItem> items = cartRequest.getItems().stream()
//                .map(itemReq -> {
//                    Product product = productRepository.findById(itemReq.getProductId())
//                            .orElseThrow(()-> new RuntimeException("Product not found"));
//
//                    CartItem cartItem = new CartItem();
//                    cartItem.setProduct(product);
//                    cartItem.setQuantity(itemReq.getQuantity());
//                    cartItem.setCart(cart);
//
//                    return cartItem;
//                })
//                .collect(Collectors.toList());
//
//        cart.setItems(items);
//        return cart;
//    }
//
//    public CartResponse toCartResponse(Cart cart){
//        List<CartItemResponse> responses = cart.getItems().stream()
//                .map(this::toCartItemResponse).collect(Collectors.toList());
//
//        double total = responses.stream()
//                .mapToDouble(CartItemResponse::getTotalPrice).sum();
//
//        CartResponse cartResponse = new CartResponse();
//        cartResponse.setId(cart.getId());
//        cartResponse.setItems(responses);
//        cartResponse.setTotal(total);
//
//        return cartResponse;
//    }
//}
