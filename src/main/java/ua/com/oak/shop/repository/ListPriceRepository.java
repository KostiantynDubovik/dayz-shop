package ua.com.oak.shop.repository;

import ua.com.oak.shop.jpa.entities.ListPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListPriceRepository extends JpaRepository<ListPrice, Long> {
}
