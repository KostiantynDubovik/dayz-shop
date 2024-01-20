package ua.com.oak.shop.repository;

import ua.com.oak.shop.jpa.entities.ItemDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDescriptionRepository extends JpaRepository<ItemDescription, Long> {
}