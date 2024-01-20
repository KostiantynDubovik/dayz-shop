package ua.com.oak.shop.repository;

import ua.com.oak.shop.jpa.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}