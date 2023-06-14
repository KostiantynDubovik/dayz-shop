package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}