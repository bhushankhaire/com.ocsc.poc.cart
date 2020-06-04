package com.ocsc.poc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ocsc.poc.entity.OrderDetailsEntity;
import com.ocsc.poc.entity.OrderEntity;
import com.ocsc.poc.model.GetProductDetailsRequest;
import com.ocsc.poc.model.Order;
import com.ocsc.poc.model.OrderDetails;
import com.ocsc.poc.model.ProductDetails;
import com.ocsc.poc.repository.OrderDetailsRepository;
import com.ocsc.poc.repository.OrderRepository;
import com.ocsc.poc.ulti.RecordNotFoundException;
import com.ocsc.poc.ulti.TechnicalException;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailsRepository orderDetailsRepository;

	@Autowired
	RestTemplate restTemplate;

	final String GET_PRODUCTS = "http://localhost:8081/products/";

	Logger logger;

	@Override
	public Order getCartDetailsByUserId(Integer userId) {
		Optional<OrderEntity> orderEntity = orderRepository.findCartByUserId(userId);
		if (orderEntity.isPresent()) {
			Order order = new Order();
			order.setUserId(userId);
			order.setOrderId(orderEntity.get().getOrderId());
			order.setOrderStatus(orderEntity.get().getOrderStatus());

			List<OrderDetails> orderDetailsList = new ArrayList<>();
			List<ProductDetails> productDetailsList = new ArrayList<>();

			List<OrderDetailsEntity> orderDetailsEntityList = orderDetailsRepository
					.findOrderDetailsByOrderId(orderEntity.get().getOrderId());

			if (null != orderDetailsEntityList) {
				productDetailsList = getProductDetails(
						orderDetailsEntityList.stream().map(e -> e.getProductId()).collect(Collectors.toList()));
			}

			for (OrderDetailsEntity oe : orderDetailsEntityList) {
				ProductDetails pd = productDetailsList.stream().filter(e -> e.getProductId().equals(oe.getProductId()))
						.findFirst().get();
				OrderDetails od = new OrderDetails(oe.getOrderDetailsId(), pd, oe.getProductQuantity());
				orderDetailsList.add(od);
			}
			order.setOrderDetailsList(orderDetailsList);
			return order;
		} else {
			throw new RecordNotFoundException("No cart details found");
		}
	}

	private List<ProductDetails> getProductDetails(List<Integer> productIdList) {
		try {
			ResponseEntity<ProductDetails[]> response;
			GetProductDetailsRequest gpd = new GetProductDetailsRequest(productIdList);
			response = restTemplate.postForEntity(GET_PRODUCTS, gpd, ProductDetails[].class);
			return Arrays.asList(response.getBody());
		} catch (Exception ex) {
			throw new TechnicalException(ex.getMessage());
		}

	}

	@Override
	public void addToCart(Integer userId, Integer productId) {
		Optional<OrderEntity> oe = orderRepository.findCartByUserId(userId);
		OrderEntity orderEntity;
		if (!oe.isPresent()) {
			orderEntity = new OrderEntity();
			orderEntity.setUserId(userId);
			orderEntity.setOrderStatus("CART");
			orderEntity = orderRepository.save(orderEntity);
		} else {
			orderEntity = oe.get();
		}
		OrderDetailsEntity ode = new OrderDetailsEntity();
		ode.setProductId(productId);
		ode.setProductQuantity(1);
		ode.setOrderId(orderEntity.getOrderId());
		orderDetailsRepository.save(ode);

	}

	@Override
	public Integer getProductCountInCart(Integer userId) {
		return orderRepository.getProductCountInCart(userId);
	}

}
