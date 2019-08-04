package com.stackroute.keepnote.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.repository.CategoryRepository;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category createCategory(Category category) throws CategoryNotCreatedException {
		try {
			category.setCategoryCreationDate(new Date());
			return this.categoryRepository.insert(category);
		} catch (Exception e) {
			log.info(e.getClass() + " -- " + e.getMessage());
			throw new CategoryNotCreatedException(e.getMessage());
		}
	}

	public boolean deleteCategory(String categoryId) throws CategoryDoesNoteExistsException {
		try {
			Optional<Category> category = Optional.ofNullable(this.categoryRepository.findById(categoryId)).get();
			if (!category.isPresent()) {
				throw new CategoryDoesNoteExistsException("category does not exists exception");
			}
			this.categoryRepository.delete(category.get());
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new CategoryDoesNoteExistsException(e.getMessage());
		}
	}

	public Category updateCategory(Category category, String categoryId) throws CategoryNotFoundException {
		try {
			Optional<Category> categoryOptional = Optional.ofNullable(this.categoryRepository.findById(categoryId))
					.get();
			if (!categoryOptional.isPresent()) {
				throw new CategoryNotFoundException("category not found exception");
			}
			this.categoryRepository.save(category);
			return this.categoryRepository.findById(categoryId).get();
		} catch (CategoryNotFoundException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new CategoryNotFoundException(e.getMessage());
		}
	}

	public Category getCategoryById(String categoryId) throws CategoryNotFoundException {
		try {
			Optional<Category> category = Optional.ofNullable(this.categoryRepository.findById(categoryId)).get();
			if (!category.isPresent()) {
				throw new CategoryNotFoundException("category not found exception");
			}
			return category.get();
		} catch (CategoryNotFoundException e) {
			log.info(e.getClass() + " -- " + e.getMessage());
			throw new CategoryNotFoundException(e.getMessage());
		}
	}

	public List<Category> getAllCategoryByUserId(String userId) {
		return this.categoryRepository.findAllCategoryByCategoryCreatedBy(userId);
	}

}
