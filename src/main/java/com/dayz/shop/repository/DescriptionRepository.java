package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Description;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescriptionRepository extends JpaRepository<Description, Long> {
}