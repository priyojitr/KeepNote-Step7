package com.stackroute.keepnote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.service.CategoryService;

import lombok.extern.apachecommons.CommonsLog;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@CommonsLog
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	/**
	 * This api should be able to create a new category based on info received.
	 * 
	 * @throws CategoryNotCreatedException
	 */
	@PostMapping("/category")
	public Category createCategory(@RequestBody Category category) throws CategoryNotCreatedException {
		log.info("calling service layer to store");
		try {
			this.categoryService.createCategory(category);
			return category;
		} catch (CategoryNotCreatedException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new CategoryNotCreatedException(e.getMessage());
		}
	}

	/**
	 * This api should be able to delete a category based on category id.
	 * 
	 * @param userId
	 * @return
	 * @throws CategoryDoesNoteExistsException
	 * @throws UserNotFoundException
	 */
	@GetMapping(value = "/category/delete/{categoryId}")
	public String deleteCategory(@PathVariable String categoryId) throws CategoryDoesNoteExistsException {
		try {
			this.categoryService.deleteCategory(categoryId);
			return "{\"isDeleted\":\"true\"}";
		} catch (CategoryDoesNoteExistsException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new CategoryDoesNoteExistsException(e.getMessage());
		}

	}

	/**
	 * This api should be able to update a category info based on category id.
	 * 
	 * @param categoryId
	 * @return
	 * @throws CategoryNotFoundException
	 */
	@PostMapping(value = "/category/update/{categoryId}")
	public Category updateCategory(@RequestBody Category category, @PathVariable String categoryId)
			throws CategoryNotFoundException {
		log.info("calling service layer to update");
		try {
			return this.categoryService.updateCategory(category, categoryId);
		} catch (CategoryNotFoundException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new CategoryNotFoundException(e.getMessage());
		}
	}

	/**
	 * This api should return a specific category based on category id
	 * 
	 * @throws CategoryDoesNoteExistsException
	 */
	@GetMapping("/category/{userId}/{categoryId}")
	public Category getCategoryById(@PathVariable String userId, @PathVariable String categoryId)
			throws CategoryNotFoundException {
		try {
			Category category = this.categoryService.getCategoryById(categoryId);
			if (null != category) {
				return category;
			} else {
				throw new CategoryNotFoundException("category not found exception");
			}
		} catch (CategoryNotFoundException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new CategoryNotFoundException(e.getMessage());
		}
	}

	/**
	 * This api should return list all notes based on user id
	 * 
	 * @throws CategoryDoesNoteExistsException
	 */
	@GetMapping("/category/{userId}")
	public List<Category> getAllCategoriesByUserId(@PathVariable String userId) throws CategoryDoesNoteExistsException {
		return this.categoryService.getAllCategoryByUserId(userId);
	}

}
