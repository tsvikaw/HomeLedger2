package com.tsvika.home.ledger.storage.controllers;

import com.tsvika.home.ledger.dto.LineQuery;
import com.tsvika.home.ledger.storage.entities.adapters.CategoryAdapter;
import com.tsvika.home.ledger.storage.entities.dao.Category;
import com.tsvika.home.ledger.storage.logic.ICategoryCompositeUtility;
import com.tsvika.home.ledger.storage.repositories.CategoryRepository;
import com.tsvika.home.ledger.storage.utilities.LedgerLogger;
import com.tsvika.home.ledger.dto.CategoryDisplayable;
import com.tsvika.home.ledger.dto.CategoryForCreation;
import com.tsvika.home.ledger.dto.CategoryForUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LineController lineController;

    @Autowired
    private CategoryAdapter categoryAdapter;

    @Autowired
    private ICategoryCompositeUtility categoryCompositeUtility;

    //    @ApiOperation("This method is used to retrieve all the available AwsAccounts")
//    @ApiResponses({@ApiResponse(
//            code = 404,
//            message = "No AwsAccounts were found"
//    )})
    @RequestMapping(
            value = {""},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<CategoryDisplayable>> getAllCategories() {
        LedgerLogger.info(getClass(), "getting all categories");

        Collection<CategoryDisplayable> allCategories =
                categoryRepository.findAll().stream()
                        .map(categoryAdapter::generateCategoryDisplayable)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(new ArrayList<>(allCategories), HttpStatus.OK);

    }

    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<CategoryDisplayable> getCategory(@PathVariable("id") String id) {
        Optional<Category> found = categoryRepository.findById(id);
        if (!found.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        CategoryDisplayable displayable = categoryAdapter.generateCategoryDisplayable(found.get());
        return new ResponseEntity<>(displayable, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/descendants/{id}"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<CategoryDisplayable>> getCategoryDescendants(@PathVariable("id") String id) {
        Optional<Category> found = categoryRepository.findById(id);
        if (!found.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        List<Category> all = categoryRepository.findAll();
        List<CategoryDisplayable> descendants = categoryCompositeUtility.getDecendentsOf(id, all)
                .stream().map(categoryAdapter::generateCategoryDisplayable).collect(Collectors.toList());
        return new ResponseEntity<>(descendants, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/children/{id}"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<CategoryDisplayable>> getCategoryChildren(@PathVariable("id") String id) {
        Optional<Category> found = categoryRepository.findById(id);
        if (!found.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        List<Category> all = categoryRepository.findAll();
        List<CategoryDisplayable> descendants = categoryCompositeUtility.getChildrenOf(id, all)
                .stream().map(categoryAdapter::generateCategoryDisplayable).collect(Collectors.toList());
        return new ResponseEntity<>(descendants, HttpStatus.OK);
    }

    @RequestMapping(
            value = "",
            method = {RequestMethod.POST})
    ResponseEntity<CategoryDisplayable> create(@Valid @RequestBody CategoryForCreation categoryForCreation) {
        LedgerLogger.info(getClass(), String.format("setting category %s", categoryForCreation.getName()));

        Category category = categoryAdapter.generateCategory(categoryForCreation);

        if (categoryRepository.findByName(categoryForCreation.getName()) == null) {
            categoryRepository.save(category);
            CategoryDisplayable categoryDisplayable = categoryAdapter.generateCategoryDisplayable(category);

            return new ResponseEntity<>(categoryDisplayable, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(
            value = "/{id}",
            method = {RequestMethod.PUT})
    ResponseEntity<CategoryDisplayable> update(@PathVariable("id") String id,
                                               @Valid @RequestBody CategoryForUpdate categoryForUpdate) {
        LedgerLogger.info(getClass(), String.format("updating category by id %s", id));

        Optional<Category> found = categoryRepository.findById(id);
        if (!found.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Category byName = categoryRepository.findByName(categoryForUpdate.getName());
        if (byName == null || byName.getId() == id) {
            Category updated = categoryAdapter.updateCategory(categoryForUpdate, found.get());
            categoryRepository.save(updated);
            CategoryDisplayable categoryDisplayable = categoryAdapter.generateCategoryDisplayable(updated);

            return new ResponseEntity<>(categoryDisplayable, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.DELETE},
            produces = {"application/json"}
    )
    public ResponseEntity deleteCategory(@PathVariable("id") String id) {
        LedgerLogger.info(getClass(), String.format("deleting category by id %s", id));

        Optional<Category> found = categoryRepository.findById(id);
        if (!found.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ResponseEntity forbidden = getForbiddenIfCategoryHasDirectLines(id);
        if (forbidden != null) return forbidden;

        fixChildCategoriesToHaveParentParentId(id, found);

        categoryRepository.delete(found.get());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    public void fixChildCategoriesToHaveParentParentId(@PathVariable("id") String id, Optional<Category> found) {
        List<Category> all = categoryRepository.findAll();
        Collection<Category> children = categoryCompositeUtility.getChildrenOf(id, all);

        for(Category child : children) {
            child.setParentId(found.get().getParentId());
            categoryRepository.save(child);
        }
    }

    public ResponseEntity getForbiddenIfCategoryHasDirectLines(@PathVariable("id") String id) {
        try {
            LineQuery lineQuery = new LineQuery();
            CategoryDisplayable categoryDisplayable = new CategoryDisplayable();
            categoryDisplayable.setId(id);
            lineQuery.setCategories(new CategoryDisplayable[]{categoryDisplayable});
            long linesRelated = lineController.getByquery(lineQuery).getBody().getTotal();

            if (linesRelated > 0) {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return null;
    }
}