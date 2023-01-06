package com.dayz.shop.repository;


import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.Type;
import com.dayz.shop.jpa.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

	Payment findByIdAndUser(Long id, User user);

	List<Payment> findAllByUser(User user);
	List<Payment> findAllByUserAndStoreAndTypeNotIn(User user, Store store, Collection<Type> paymentTypes);
	List<Payment> findAllByUserAndStoreAndTypeNotIn(User user, Store store, Collection<Type> paymentTypes, Pageable pageable);
}
