package com.ghut.passbox.model;

import java.io.Serializable;

/**
 * @author Gary
 *
 */
public class CategoryCount implements Serializable {

	private static final long serialVersionUID = 1L;

	private Category category;
	private int count = 0;
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		if(category == null) {
			return "";
		}
		
		return "" + category.getCatName() + " " + count;
	}
	
}
