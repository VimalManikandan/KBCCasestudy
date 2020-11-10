package com.stackroute.keepnote.service;

import java.util.List;

import com.stackroute.keepnote.exception.CategoryAlreadyExistException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Category;

public interface CategoryService {

	/*
	 * Should not modify this interface. You have to implement these methods in
	 * corresponding Impl classes
	 */
	
	public Category createCategory(Category category) throws CategoryAlreadyExistException;

	public boolean deleteCategory(int noteId) throws CategoryNotFoundException;

	public Category updateCategory(Category category, int id) throws CategoryNotFoundException;

	public Category getCategoryById(int categoryId) throws CategoryNotFoundException;

	public List<Category> getAllCategoryByUserId(String userId) throws UserNotFoundException;

}
