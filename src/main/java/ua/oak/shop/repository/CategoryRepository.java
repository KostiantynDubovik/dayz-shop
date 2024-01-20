package ua.oak.shop.repository;

import ua.oak.shop.jpa.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByCategoryName(String categoryName);
}
