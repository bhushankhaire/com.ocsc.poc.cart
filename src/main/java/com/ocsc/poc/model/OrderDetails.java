package com.ocsc.poc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDetails {

	@Getter
	@Setter
	private Integer orderDetailsId;

	@Getter
	@Setter
	private ProductDetails productDetails;

	@Getter
	@Setter
	private Integer productQuantity;
}
