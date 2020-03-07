package com.ghut.passbox.model;

import java.io.Serializable;

import com.ghut.passbox.util.AppUtils;

/**
 * @author Gary
 * 
 */
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	private int acctId = -1;
	private String title;
	private String loginName;
	private String loginPwd;
	private String site;
	private String remark;
	private Category category;
	private String encryptedLoginName;
	private String encryptedLoginPwd;

	public Account() {
		// TODO Auto-generated constructor stub
	}

	public int getAcctId() {
		return acctId;
	}

	public void setAcctId(int acctId) {
		this.acctId = acctId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
		this.encryptedLoginName = AppUtils.aesEncrypt(loginName);
	}

	public String getEncryptedLoginName() {
		return encryptedLoginName;
	}

	public void setEncryptedLoginName(String encryptedLoginName) {
		this.encryptedLoginName = encryptedLoginName;
		this.loginName = AppUtils.aesDecrypt(encryptedLoginName);
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
		this.encryptedLoginPwd = AppUtils.aesEncrypt(loginPwd);
	}

	public String getEncryptedLoginPwd() {
		return encryptedLoginPwd;
	}

	public void setEncryptedLoginPwd(String encryptedLoginPwd) {
		this.encryptedLoginPwd = encryptedLoginPwd;
		this.loginPwd = AppUtils.aesDecrypt(encryptedLoginPwd);
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + acctId;
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
		Account other = (Account) obj;
		if (acctId != other.acctId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [acctId=" + acctId + ", title=" + title
				+ ", loginName=" + loginName + ", site=" + site + ", remark="
				+ remark + ", category=" + category + "]";
	}

}
