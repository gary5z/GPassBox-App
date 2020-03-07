package com.ghut.passbox.service;

/**
 * @author Gary
 * 
 */
public interface SystemService {

	/**
	 * 校验密码
	 * 
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	boolean verifyPwd(String pwd) throws Exception;

	/**
	 * 修改密码
	 * 
	 * @param oldpwd
	 * @param newpwd
	 * @return json字符串 {"result":"ok"} 或者 {"result":"error", "msg":"xx错误"}
	 * @throws Exception
	 */
	String modifyPwd(String oldpwd, String newpwd) throws Exception;

	/**
	 * 登陆业务
	 * 
	 * @param name
	 * @param pwd
	 * @return json字符串 {"result":"ok"} 或者 {"result":"error", "msg":"xx错误"}
	 * @throws Exception
	 */
	String login(String pwd) throws Exception;

	/**
	 * 判断是否初次使用
	 * 
	 * @param user
	 * @return json字符串 {"result":"ok"} 或者 {"result":"error", "msg":"xx错误"}
	 * @throws Exception
	 */
	boolean hasRegistered() throws Exception;

	/**
	 * 绑定邮箱
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	String bindEmail(String email) throws Exception;

	/**
	 * 获取邮箱
	 * 
	 * @return
	 * @throws Exception
	 */
	String getEmail() throws Exception;

	int getVersion();
}
