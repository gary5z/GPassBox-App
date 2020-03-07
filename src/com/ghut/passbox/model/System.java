package com.ghut.passbox.model;

import java.io.Serializable;

/**
 * @author Gary
 *
 */
public class System implements Serializable {

	private static final long serialVersionUID = 1L;

	private String enterPwd;
	
	private String email;
	
	private int version;

	public String getEnterPwd() {
		return enterPwd;
	}

	public void setEnterPwd(String enterPwd) {
		this.enterPwd = enterPwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
}
