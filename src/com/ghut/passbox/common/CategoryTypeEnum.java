/**
 * AccountBox-v2.0 - CategoryTypeEnum
 * CategoryTypeEnum.java 2016-10-18
 */
package com.ghut.passbox.common;

/**
 * @author Gary
 * 
 */
public enum CategoryTypeEnum {

	NORMAL("常规", 1), EMAIL("邮箱", 2), FINANCIAL("金融", 3), NOTE("记事", 4);

	public String name;
	public int value;

	private CategoryTypeEnum(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public static CategoryTypeEnum valueOf(int value) {
		CategoryTypeEnum result = null;

		for (CategoryTypeEnum e : values()) {
			if (e.value == value) {
				result = e;
				break;
			}
		}

		return result;
	}

	public static CategoryTypeEnum valueOfName(String name) {
		CategoryTypeEnum result = null;

		for (CategoryTypeEnum e : values()) {
			if (e.name.equals(name)) {
				result = e;
				break;
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return name;
	}

}
