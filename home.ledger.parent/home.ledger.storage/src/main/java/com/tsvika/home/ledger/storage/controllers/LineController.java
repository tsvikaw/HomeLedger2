package com.tsvika.home.ledger.storage.controllers;

import com.tsvika.home.ledger.dto.*;
import com.tsvika.home.ledger.storage.entities.adapters.CategoryAdapter;
import com.tsvika.home.ledger.storage.entities.adapters.LineAdapter;
import com.tsvika.home.ledger.storage.entities.dao.Category;
import com.tsvika.home.ledger.storage.entities.dao.Line;
import com.tsvika.home.ledger.storage.repositories.CategoryRepository;
import com.tsvika.home.ledger.storage.repositories.LineRepository;
import com.tsvika.home.ledger.storage.utilities.LedgerLogger;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/line")
public class LineController {

    public static final int DEFAULT_PAGE_SIZE = 10;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LineAdapter lineAdapter;

    @Autowired
    private CategoryAdapter categoryAdapter;

    @RequestMapping(
            value = {""},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<LineDisplayable>> getAllLines() {
        LedgerLogger.info(getClass(), "getting all lines");
        Collection<LineDisplayable> allLines =
                lineRepository.findAll().stream()
                        .map(lineAdapter::generateLineDisplayable)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(new ArrayList<>(allLines), HttpStatus.OK);

    }

    @RequestMapping(
            value = {"/accounts"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<String>> getAllAccounts() {
        LedgerLogger.info(getClass(), "getting all accounts");

        return new ResponseEntity<>(lineRepository.findDistinctAccount(), HttpStatus.OK);
    }

    @RequestMapping(
            value = "query",
            method = {RequestMethod.POST})
    ResponseEntity<LineDisplayableQueryResult> getByquery(@Valid @RequestBody LineQuery lineQuery) {

        if(StringUtils.isEmpty(lineQuery.getSortBy())){
            lineQuery.setSortBy("date");
            lineQuery.setSortAsc(false);
        }

        if(lineQuery.getPageSize() == 0){
            lineQuery.setPageSize(DEFAULT_PAGE_SIZE);
            lineQuery.setPageNumber(0);
        }

        Sort sort = lineQuery.isSortAsc() ?  Sort.by(lineQuery.getSortBy()).ascending()
                : Sort.by(lineQuery.getSortBy()).descending();
        Pageable pageable = PageRequest.of(lineQuery.getPageNumber(), lineQuery.getPageSize(), sort);

        Specification<Line> specification = new Specification<Line>() {
            @Override

            public Predicate toPredicate(Root<Line> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                if (lineQuery.getStartDate() == null) {
                    lineQuery.setStartDate(new Date(0));
                }

                if (lineQuery.getEndDate() == null) {
                    lineQuery.setEndDate(new Date());
                }

                Predicate betweenDatesClause = cb.between(root.<Date>get("date"), lineQuery.getStartDate(), lineQuery.getEndDate());
                Predicate whereClause = betweenDatesClause;

                if (StringUtils.isNotEmpty(lineQuery.getDescription())) {
                    whereClause = cb.and(whereClause, cb.like(root.get("description"),
                            "%" + lineQuery.getDescription() + "%"));
                }

                if (lineQuery.getStartAmount() > 0) {
                    whereClause = cb.and(whereClause, cb.greaterThanOrEqualTo(root.get("amount"), lineQuery.getStartAmount()));
                }

                if (lineQuery.getEndAmount() > 0) {
                    whereClause = cb.and(whereClause, cb.lessThan(root.get("amount"), lineQuery.getEndAmount()));
                }

                if (lineQuery.getCategories() != null && Arrays.stream(lineQuery.getCategories()).count() > 0) {
                    whereClause = cb.and(whereClause, root.get("category").in(
                            Arrays.stream(lineQuery.getCategories()).map(categoryAdapter::generateCategory).
                                    collect(Collectors.toList())));
                }

                if (lineQuery.getAccount() != null && StringUtils.isNotEmpty(lineQuery.getAccount())) {
                    whereClause = cb.and(whereClause, cb.equal(root.get("account"), lineQuery.getAccount()));
                }

                return whereClause;
            }
        };

        Page<Line> pagedLines = lineRepository.findAll(specification, pageable);
        long total = lineRepository.count(specification);

       List<LineDisplayable> lineDisplayables = pagedLines.stream()
                    .map(lineAdapter::generateLineDisplayable)
                    .collect(Collectors.toList());

        LineDisplayableQueryResult queryResult = new LineDisplayableQueryResult(new ArrayList<>(lineDisplayables), total);
        queryResult.setPageNumber(pageable.getPageNumber());
        queryResult.setPageSize(pageable.getPageSize());
        queryResult.setSortBy(pageable.getSort().toString());

        return new ResponseEntity<>(queryResult, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<LineDisplayable> getLine(@PathVariable("id") String id) {
        Optional<Line> found = lineRepository.findById(id);
        if (!found.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        LineDisplayable displayable = lineAdapter.generateLineDisplayable(found.get());
        return new ResponseEntity<>(displayable, HttpStatus.OK);
    }

    @RequestMapping(
            value = "",
            method = {RequestMethod.POST})
    ResponseEntity<LineDisplayable> create(@Valid @RequestBody LineForCreation lineForCreation) {
        LedgerLogger.info(getClass(), String.format("setting line %s", lineForCreation.getDescription()));

        Line line = lineAdapter.generateLine(lineForCreation);
        List<Line> existingMatchingLines = getExistingMatchingLines(lineForCreation);

        if (existingMatchingLines.size() == 0) {
            Optional<Category> category = categoryRepository.findById(lineForCreation.getCategoryId());
            if(category.isPresent()) {
                line.setCategory(category.get());
                lineRepository.save(line);
                LineDisplayable lineDisplayable = lineAdapter.generateLineDisplayable(line);

                return new ResponseEntity<>(lineDisplayable, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    private List<Line> getExistingMatchingLines(@RequestBody @Valid LineForCreation lineForCreation) {
        Line exampleEntity = new Line();
        exampleEntity.setDescription(lineForCreation.getDescription());
        exampleEntity.setAmount(lineForCreation.getAmount());
        exampleEntity.setDate(lineForCreation.getDate());
        Example<Line> employeeExample = Example.of(exampleEntity);
        return lineRepository.findAll(employeeExample);
    }

    @RequestMapping(
            value = "/{id}",
            method = {RequestMethod.PUT})
    ResponseEntity<LineDisplayable> update(@PathVariable("id") String id,
                                               @Valid @RequestBody LineForUpdate lineForUpdate) {
        LedgerLogger.info(getClass(), String.format("updating line by id %s", id));

        Optional<Line> found = lineRepository.findById(id);
        if (!found.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Optional<Category> category = categoryRepository.findById(lineForUpdate.getCategoryId());

        if(!category.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Line updated = lineAdapter.generateLine(lineForUpdate, found.get(), category.get());
        lineRepository.save(updated);

        LineDisplayable lineDisplayable = lineAdapter.generateLineDisplayable(updated);

        return new ResponseEntity<>(lineDisplayable, HttpStatus.OK);

    }
//
//    @RequestMapping(
//            value = {"/{id}"},
//            method = {RequestMethod.DELETE},
//            produces = {"application/json"}
//    )
//    public ResponseEntity deleteCategory(@PathVariable("id") String id) {
//        LedgerLogger.info(getClass(), String.format("deleting category by id %s", id));
//
//        Optional<Category> found = categoryRepository.findById(id);
//        if (!found.isPresent()) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//
//        categoryRepository.delete(found.get());
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
}