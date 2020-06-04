package com.ocsc.poc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDetails {

	@Getter
	@Setter
	private Integer productId;
	@Getter
	@Setter
	private String productName;
	@Getter
	@Setter
	private String company;
	@Getter
	@Setter
	private Double price;
	@Getter
	@Setter
	private byte[] picByte;

}