package com.ocsc.poc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ocsc.poc.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

	@Query("SELECT o FROM OrderEntity o  WHERE o.userId =?1 and o.orderStatus='CART'")
	Optional<OrderEntity> findCartByUserId(Integer userId);

	@Query(value = "select count(distinct od.product_id)from ocsc_poc.tbl_order o inner join ocsc_poc.tbl_order_details od on o.order_id=od.order_id where o.user_id=?1 and order_status='CART'", nativeQuery = true)
	Integer getProductCountInCart(Integer userId);

}
