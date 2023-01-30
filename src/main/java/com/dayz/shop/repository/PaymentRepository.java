package com.dayz.shop.repository;


import com.dayz.shop.jpa.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

	Payment findByIdAndUser(Long id, User user);

	List<Payment> findAllByUserAndStoreAndStatusAndTypeIn(User user, Store store, OrderStatus status, Collection<Type> paymentTypes);
	Page<Payment> findAllByUserAndStoreAndStatusAndTypeIn(User user, Store store, OrderStatus status, Collection<Type> paymentTypes, Pageable pageable);
	Page<Payment> findAllByUserAndStoreAndStatusAndType(User user, Store store, OrderStatus status, Type paymentTypes, Pageable pageable);
}
