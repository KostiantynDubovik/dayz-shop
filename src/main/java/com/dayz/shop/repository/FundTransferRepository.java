package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {
}
