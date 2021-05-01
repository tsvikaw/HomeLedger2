package com.tsvika.home.ledger.storage.repositories;

import com.tsvika.home.ledger.storage.entities.dao.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByName(String name);
}

