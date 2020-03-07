package com.ghut.passbox.common.crypto.impl;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ghut.passbox.common.crypto.AbstractCipher;

/**
 * @author Gary
 * 
 */
public class AESCipher extends AbstractCipher {

	private static final String ALGORITHM = "AES";

	/**
	 * AES256/AES192/AES128
	 */
	private final static int KEY_SIZE = 256;

	// private static final String CIPHER_ALGORITHM_ECB =
	// "AES/ECB/PKCS5Padding";

	private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";

	private final static int[] IV_FACTOR = new int[] { -82, 61, -58, -44, -66,
			-92, 77, 2, -77, -33, -98, 3, 74, 104, 19, -31 };

	@Override
	public String getAlgorithm() {
		return ALGORITHM;
	}

	@Override
	public byte[] encrypt(byte[] key, byte[] src) throws Exception {
		byte[] rawKey = generateKey(key);
		SecretKeySpec skeySpec = new SecretKeySpec(rawKey, ALGORITHM);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
				getIV(rawKey)));
		byte[] encrypted = cipher.doFinal(src);
		return encrypted;
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
		byte[] rawKey = generateKey(key);
		SecretKeySpec skeySpec = new SecretKeySpec(rawKey, ALGORITHM);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(
				getIV(rawKey)));
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	@Override
	protected int getKeySize() {
		return KEY_SIZE;
	}

	/**
	 * IV固定为16个字节
	 * 
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	private byte[] getIV(byte[] seed) throws Exception {

		byte[] result = new byte[IV_FACTOR.length];
		for (int i = 0; i < IV_FACTOR.length; i++) {
			int k = i % seed.length;
			result[i] = (byte) (seed[k] ^ IV_FACTOR[i]);
		}

		byte[] randomKey = generateKey(result);
		if (randomKey.length > IV_FACTOR.length) {
			System.arraycopy(randomKey, 0, result, 0, IV_FACTOR.length);
		} else {
			System.arraycopy(randomKey, 0, result, 0, randomKey.length);
		}

		return result;
	}

}
