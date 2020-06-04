package com.ocsc.poc.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TBL_ORDER")
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class OrderEntity {

	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_id")
	private Integer orderId;

	@Getter
	@Setter
	@Column(name = "user_id")
	private Integer userId;

	@Getter
	@Setter
	@Column(name = "order_status")
	private String orderStatus;

	@Getter
	@Setter
	@Column(name = "date_of_order")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_of_order;

}
