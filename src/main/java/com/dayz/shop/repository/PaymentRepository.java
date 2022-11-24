package com.dayz.shop.repository;


import com.dayz.shop.jpa.entities.Payment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

}
