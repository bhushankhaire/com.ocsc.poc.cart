package com.ocsc.poc.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

	@Getter
	@Setter
	private Integer userId;
	@Getter
	@Setter
	private Integer orderId;
	@Getter
	@Setter
	private String orderStatus;
	@Getter
	@Setter
	private List<OrderDetails> orderDetailsList;
	@Getter
	@Setter
	private Date date_of_order;

}
