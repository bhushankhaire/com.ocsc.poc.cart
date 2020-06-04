package com.ocsc.poc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ocsc.poc.entity.OrderDetailsEntity;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Integer> {

	@Query("SELECT o FROM OrderDetailsEntity o  WHERE o.orderId =?1")
	List<OrderDetailsEntity> findOrderDetailsByOrderId(Integer orderId);

}
