package com.ghut.passbox.service;

/**
 * @author Gary
 * 
 */
public interface SystemService {

	/**
	 * У������
	 * 
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	boolean verifyPwd(String pwd) throws Exception;

	/**
	 * �޸�����
	 * 
	 * @param oldpwd
	 * @param newpwd
	 * @return json�ַ��� {"result":"ok"} ���� {"result":"error", "msg":"xx����"}
	 * @throws Exception
	 */
	String modifyPwd(String oldpwd, String newpwd) throws Exception;

	/**
	 * ��½ҵ��
	 * 
	 * @param name
	 * @param pwd
	 * @return json�ַ��� {"result":"ok"} ���� {"result":"error", "msg":"xx����"}
	 * @throws Exception
	 */
	String login(String pwd) throws Exception;

	/**
	 * �ж��Ƿ����ʹ��
	 * 
	 * @param user
	 * @return json�ַ��� {"result":"ok"} ���� {"result":"error", "msg":"xx����"}
	 * @throws Exception
	 */
	boolean hasRegistered() throws Exception;

	/**
	 * ������
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	String bindEmail(String email) throws Exception;

	/**
	 * ��ȡ����
	 * 
	 * @return
	 * @throws Exception
	 */
	String getEmail() throws Exception;

	int getVersion();
}
