/**
 * AccountBox - AbstractCipher
 * AbstractCipher.java 2016-6-11
 */
package com.ghut.passbox.common.crypto;

import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.http.impl.auth.UnsupportedDigestAlgorithmException;

/**
 * @author Gary
 * 
 */
public abstract class AbstractCipher implements ICipher {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ghut.passbox.common.crypto.ICipher#decrypt(byte[], byte[])
	 */
	@Override
	public byte[] decrypt(byte[] key, byte[] encryptedData) throws Exception {
		throw new UnsupportedDigestAlgorithmException(getAlgorithm()
				+ " algorithm does not support to decrypt.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ghut.passbox.common.crypto.ICipher#match(byte[], byte[],
	 * byte[])
	 */
	@Override
	public boolean match(byte[] key, byte[] plainData, byte[] encryptedData)
			throws Exception {
		byte[] inputEncrypted = encrypt(key, plainData);

		int diff = inputEncrypted.length ^ encryptedData.length;
		for (int i = 0; i < inputEncrypted.length && i < encryptedData.length; i++) {
			diff |= inputEncrypted[i] ^ encryptedData[i];
		}
		
		return diff == 0;
	}

	protected int getKeySize() {
		return 0;
	}

	protected byte[] generateKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(getAlgorithm());
		// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
		SecureRandom sr = null;
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		} else {
			sr = SecureRandom.getInstance("SHA1PRNG");
		}
		sr.setSeed(seed);
		kgen.init(getKeySize(), sr);
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

}
