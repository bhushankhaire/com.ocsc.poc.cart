package com.ocsc.poc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ocsc.poc.model.Order;
import com.ocsc.poc.service.CartService;

@RestController
@RequestMapping(path = "")
@Validated
public class CartController {

	@Autowired
	CartService cartService;

	@GetMapping(path = "/cart", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Order getCartDetailsByUserId(@RequestHeader(value = "X-OCSC-UserId") Integer userId) {

		return cartService.getCartDetailsByUserId(userId);
	}

	@GetMapping(path = "/cart/count", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Integer getProductCountInCart(@RequestHeader(value = "X-OCSC-UserId") Integer userId) {

		return cartService.getProductCountInCart(userId);
	}

	@PostMapping(path = "/cart/product/{productId}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void addToCart(@RequestHeader(value = "X-OCSC-UserId") Integer userId,
			@PathVariable("productId") Integer productId) {

		cartService.addToCart(userId, productId);

	}

}
