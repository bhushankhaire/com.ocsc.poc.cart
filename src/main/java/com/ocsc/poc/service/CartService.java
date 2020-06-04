package com.ocsc.poc.service;

import com.ocsc.poc.model.Order;

public interface CartService {

	public Order getCartDetailsByUserId(Integer userId);

	public void addToCart(Integer userId, Integer productId);

	public Integer getProductCountInCart(Integer userId);

}
