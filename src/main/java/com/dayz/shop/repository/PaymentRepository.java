package com.dayz.shop.repository;


import com.dayz.shop.jpa.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

	Payment findByIdAndUser(Long id, User user);

	@Query("SELECT P FROM Payment P WHERE (P.type in :paymentTypes AND P.user <> P.userFrom) AND (((P.user = :user AND P.direction = 'INCOMING') OR (P.userFrom = :user AND P.direction = 'OUTGOING')) AND P.store = :store AND P.status = :status AND P.type in :paymentTypes)")
	List<Payment> findAllByUserAndStoreAndStatusAndTypeIn(User user, Store store, OrderStatus status, Collection<Type> paymentTypes);

	@Query("SELECT P FROM Payment P WHERE (P.type in :paymentTypes AND P.user <> P.userFrom) AND (((P.user = :user AND P.direction = 'INCOMING') OR (P.userFrom = :user AND P.direction = 'OUTGOING')) AND P.store = :store AND P.status = :status AND P.type = :paymentTypes)")
	Page<Payment> findAllByUserAndStoreAndStatusAndTypeIn(User user, Store store, OrderStatus status, List<Type> paymentTypes, Pageable pageable);
}
