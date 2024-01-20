package ua.oak.shop.repository;

import ua.oak.shop.jpa.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}