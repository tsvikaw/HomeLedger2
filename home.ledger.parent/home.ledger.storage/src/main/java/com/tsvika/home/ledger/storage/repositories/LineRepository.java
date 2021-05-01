package com.tsvika.home.ledger.storage.repositories;

import com.tsvika.home.ledger.storage.entities.dao.Line;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineRepository extends JpaRepository<Line, String>, QueryByExampleExecutor<Line>, PagingAndSortingRepository<Line, String> {
    Page<Line> findAll(Specification<Line> specification, Pageable pageable);
    @Query(value = "SELECT DISTINCT account FROM line ORDER BY account DESC", nativeQuery = true)
    List<String> findDistinctAccount();
    long count(Specification<Line> specification);
}

