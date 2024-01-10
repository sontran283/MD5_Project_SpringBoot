package com.ra.controller;

import com.ra.model.entity.Category;
import com.ra.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*") // dung de truy cap moi duong dan ca noi bo, lan ngoai bo
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    // index
    @GetMapping("/category")
    public ResponseEntity<List<Category>> index() {
        List<Category> categoryList = categoryService.findAll();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    // add
    @PostMapping("/category")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category newCategory = categoryService.saveOrUpdate(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // update
    @GetMapping("/category/{id}")
    public ResponseEntity<?> editCategory(@PathVariable("id") Long id) {
        Category idEdit = categoryService.findById(id);
        if (idEdit != null) {
            return new ResponseEntity<>(idEdit, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    // update
    @PutMapping("/category/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        Category category1 = categoryService.findById(id);
        category1.setCategoryName(category.getCategoryName());
        category1.setStatus(category.getStatus());
        Category category2 = categoryService.saveOrUpdate(category1);
        return new ResponseEntity<>(category2, HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/category/{id}")
    public ResponseEntity<Category> delete_category(@PathVariable("id") Long id) {
        Category cat = categoryService.findById(id);
        if (cat != null) {
            categoryService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/category/sort+search+pagination")
    public ResponseEntity<Page<Category>> getCategories(@RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                                        @RequestParam(name = "sort", defaultValue = "id") String sort,
                                                        @RequestParam(name = "order", defaultValue = "asc") String order,
                                                        @RequestParam(name = "search") String search) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<Category> categoryPage = categoryService.searchByName(pageable, search);
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }
}