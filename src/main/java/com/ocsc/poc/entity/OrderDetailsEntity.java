package com.ocsc.poc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TBL_ORDER_DETAILS")
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class OrderDetailsEntity {

	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_details_id")
	private Integer orderDetailsId;

	@Getter
	@Setter
	@Column(name = "order_id")
	private Integer orderId;

	@Getter
	@Setter
	@Column(name = "product_id")
	private Integer productId;

	@Getter
	@Setter
	@Column(name = "product_quantity")
	private Integer productQuantity;

}
