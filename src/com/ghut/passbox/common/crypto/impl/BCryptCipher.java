/**
 * AccountBox - BCryptCipher
 * BCryptCipher.java 2016-6-11
 */
package com.ghut.passbox.common.crypto.impl;

import org.mindrot.jbcrypt.BCrypt;

import com.ghut.passbox.common.Constants;
import com.ghut.passbox.common.crypto.AbstractCipher;

/**
 * @author Gary
 * 
 */
public class BCryptCipher extends AbstractCipher {

	@Override
	public String getAlgorithm() {
		return "BCrypt";
	}

	@Override
	public byte[] encrypt(byte[] key, byte[] plainData) throws Exception {
		String s = BCrypt.hashpw(new String(plainData, Constants.UTF8),
				BCrypt.gensalt(12));
		return s.getBytes(Constants.UTF8);
	}

	@Override
	public boolean match(byte[] key, byte[] plainData, byte[] encryptedData)
			throws Exception {
		return BCrypt.checkpw(new String(plainData, Constants.UTF8),
				new String(encryptedData, Constants.UTF8));
	}

}
