package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.FundTransfer;
import com.dayz.shop.jpa.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {
	List<FundTransfer> getAllByStatusAndPercentageGreaterThan(OrderStatus orderStatus, BigDecimal percentage);
	List<FundTransfer> getAllByStatusAndPercentage(OrderStatus orderStatus, BigDecimal percentage);
	List<FundTransfer> getAllByStatus(OrderStatus orderStatus);
}
