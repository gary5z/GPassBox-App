package com.ghut.passbox.service;

import java.util.List;

import com.ghut.passbox.model.Category;


/**
 * @author Gary
 *
 */
public interface CategoryService {
	
	String addNew(Category model) throws Exception;

	String modify(Category model) throws Exception;

	String remove(Category model) throws Exception;

	String remove(int catId) throws Exception;
	
	List<Category> getAll() throws Exception;

	Category get(int catId) throws Exception;

}

