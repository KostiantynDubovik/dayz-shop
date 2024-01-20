package ua.oak.shop.repository;


import com.dayz.shop.jpa.entities.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.oak.shop.jpa.entities.*;

import java.util.Collection;
import java.util.List;

@Repository
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

	Payment findByIdAndUser(Long id, User user);

	List<Payment> findAllByUser(User user);
	List<Payment> findAllByUserAndStoreAndTypeNotIn(User user, Store store, Collection<Type> paymentTypes);
	List<Payment> findAllByUserAndStoreAndTypeNotIn(User user, Store store, Collection<Type> paymentTypes, Pageable pageable);
	List<Payment> findAllByUserAndStoreAndStatusAndTypeIn(User user, Store store, OrderStatus status, Collection<Type> paymentTypes, Pageable pageable);
}
