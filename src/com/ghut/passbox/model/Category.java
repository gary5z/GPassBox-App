package com.ghut.passbox.model;

import java.io.Serializable;

import com.ghut.passbox.common.CategoryTypeEnum;

/**
 * @author Gary
 * 
 */
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;
	private int catId = -1;
	private String catName;
	private CategoryTypeEnum catType = CategoryTypeEnum.NORMAL;
	private String remark;

	public Category() {
		// TODO Auto-generated constructor stub
	}

	public Category(int catId, String catName, String remark) {
		super();
		this.catId = catId;
		this.catName = catName;
		this.remark = remark;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public CategoryTypeEnum getCatType() {
		return catType;
	}

	public void setCatType(CategoryTypeEnum catType) {
		this.catType = catType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + catId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (catId != other.catId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Category [catId=" + catId + ", catName=" + catName
				+ ", catType=" + catType + ", remark=" + remark + "]";
	}

}
