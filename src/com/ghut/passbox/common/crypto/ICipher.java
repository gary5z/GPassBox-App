/**
 * AccountBox - ICipher
 * ICipher.java 2016-6-11
 */
package com.ghut.passbox.common.crypto;

/**
 * @author Gary
 * 
 */
public interface ICipher {

	/**
	 * 采用的加密算法名称
	 * 
	 * @return
	 */
	String getAlgorithm();

	/**
	 * 加密
	 * 
	 * @param key
	 * @param plainData
	 * @return
	 * @throws Exception
	 */
	byte[] encrypt(byte[] key, byte[] plainData) throws Exception;

	/**
	 * 解密
	 * 
	 * @param key
	 * @param encryptedData
	 * @return
	 * @throws Exception
	 */
	byte[] decrypt(byte[] key, byte[] encryptedData) throws Exception;

	/**
	 * 匹配明文与加密数据
	 * 
	 * @param plainData
	 * @param encryptedData
	 * @return
	 * @throws Exception
	 */
	boolean match(byte[] key, byte[] plainData, byte[] encryptedData)
			throws Exception;
}
