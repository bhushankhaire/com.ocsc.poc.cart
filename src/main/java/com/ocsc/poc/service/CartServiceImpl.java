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
		try {
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
					ProductDetails pd = productDetailsList.stream()
							.filter(e -> e.getProductId().equals(oe.getProductId())).findFirst().get();
					OrderDetails od = new OrderDetails(oe.getOrderDetailsId(), pd, oe.getProductQuantity());
					orderDetailsList.add(od);
				}
				order.setOrderDetailsList(orderDetailsList);
				return order;
			} else {
				throw new RecordNotFoundException("No cart details found");
			}
		} catch (Exception ex) {
			if (!(ex instanceof RecordNotFoundException)) {
				throw new TechnicalException(ex.getMessage());
			} else {
				throw ex;
			}
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
		try {
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
		} catch (Exception ex) {
			throw new TechnicalException(ex.getMessage());
		}
	}

	@Override
	public Integer getProductCountInCart(Integer userId) {
		return orderRepository.getProductCountInCart(userId);
	}

	@Override
	public void updateCartDetailsByUserId(Integer userId, Order order) {

		try {
			Optional<OrderEntity> oe = orderRepository.findById(order.getOrderId());
			if (oe.get().getUserId().equals(userId)) {
				oe.get().setOrderStatus("INITIATED");

				List<OrderDetailsEntity> od = orderDetailsRepository.findOrderDetailsByOrderId(order.getOrderId());
				od.stream().forEach(e -> {
					order.getOrderDetailsList().stream().forEach(e1 -> {
						if (e.getProductId().equals(e1.getProductDetails().getProductId())) {
							e.setProductQuantity(e1.getProductQuantity());
						}
					});

				});

				od.stream().forEach(e -> orderDetailsRepository.save(e));
				orderRepository.save(oe.get());
			}
		} catch (Exception ex) {
			throw new TechnicalException(ex.getMessage());
		}
	}

	@Override
	public void deleteFromCart(Integer userId, Integer productId, Integer orderId) {
		try {
			Optional<OrderEntity> oe = orderRepository.findById(orderId);
			if (oe.get().getUserId().equals(userId)) {
				OrderDetailsEntity ode = orderDetailsRepository.findbyProductIdAndOrderId(productId, orderId);
				orderDetailsRepository.delete(ode);
			}
		} catch (Exception ex) {
			throw new TechnicalException(ex.getMessage());
		}
	}

}
