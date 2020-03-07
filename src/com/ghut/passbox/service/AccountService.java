package com.ghut.passbox.service;

import java.util.List;

import com.ghut.passbox.model.Account;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.model.CategoryCount;


/**
 * @author Gary
 *
 */
public interface AccountService {
	
	String addNew(Account account) throws Exception;

	String modify(Account account) throws Exception;

	String remove(Account account) throws Exception;

	String remove(int acctId) throws Exception;

	String remove(Category category) throws Exception;

	List<Account> getAll() throws Exception;

	List<Account> getListByCategory(int catId) throws Exception;

	List<Account> getListByCategory(Category category) throws Exception;
	
	List<CategoryCount> getCategoryCounts() throws Exception;
}

