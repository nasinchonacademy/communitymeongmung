package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.Cart;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.CartRepository;
import org.zerock.projectmeongmung.repository.ProductRepository;
import org.zerock.projectmeongmung.repository.UserRepository;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public void addProductToCart(User user, Long productId, int amount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        int price = product.getPprice();

        Cart cart = Cart.builder()
                .user(user)
                .product(product)
                .amount(amount)
                .price(price * amount)
                .build();

        cartRepository.save(cart); // 저장

    }

    public User findUserByUsername(String username) {
        return userRepository.findByUid(username)
                .orElseThrow(() -> new IllegalArgumentException(username + "을 찾지 못했습니다"));
    }

    // 장바구니에서 사용자 관련 항목을 가져오는 메서드
    public List<Cart> getCartItems(User user) {
        return cartRepository.findByUser(user);
    }

    // 장바구니에서 특정 상품을 삭제하는 메서드
    public void deleteProductFromCart(User user, Long productId) {

        // Product 엔티티를 먼저 조회
        // Product product = productRepository.findById(productId)
               // .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        // 장바구니에서 해당 사용자의 특정 상품을 찾고 삭제
        Cart cartItem = cartRepository.findByUserAndProduct_Pid(user, productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 항목을 찾을 수 없습니다."));

        cartRepository.delete(cartItem);  // 장바구니 항목 삭제
    }
}
