package com.ghut.passbox.common.crypto.impl;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.ghut.passbox.common.crypto.AbstractCipher;

/**
 * HMAC为单向加密算法，只能加密而不能解密
 * 
 * @author Gary
 * 
 */
public class HMacCipher extends AbstractCipher {
	/**
	 * HmacSHA512/HmacSHA384/HmacSHA256/HmacSHA1/HmacMD5
	 */
	private final static String ALGORITHM = "HmacSHA512";
	private final static int KEY_SIZE = 512;

	private static final int[] SALT_FACTOR = new int[] { -50, 125, -113, -53,
			53, 60, 29, -4, -123, -67, -103, -57, 125, 100, -75, 73, 79, -27,
			-22, 79, 31, 95, 80, -8, 28, 14, -95, 35, -11, 76, -110, 92, -58,
			-57, 43, -25, -122, -48, 35, 20, 93, 45, -108, 94, -8, 60, -33, 30,
			-9, 25, -15, -109, -82, 98, 55, -80, 113, 92, 107, 98, -104, -108,
			-30, 38 };

	@Override
	public String getAlgorithm() {
		return ALGORITHM;
	}

	/**
	 * 跟采用的算法对应，分别有：512 bit, 384 bit, 256 bit, 128 bit
	 */
	@Override
	protected int getKeySize() {
		return KEY_SIZE;
	}

	@Override
	public byte[] encrypt(byte[] key, byte[] plainData) throws Exception {

		SecretKeySpec secret = new SecretKeySpec(generateKey(key), ALGORITHM);

		Mac mac = Mac.getInstance(ALGORITHM);

		mac.init(secret);
		mac.update(getSalt(key));

		byte[] digest = mac.doFinal(plainData);

		return digest;
	}

	private byte[] getSalt(byte[] seed) throws Exception {
		byte[] salt = new byte[SALT_FACTOR.length];
		for (int i = 0; i < SALT_FACTOR.length; i++) {
			int k = i % seed.length;
			salt[i] = (byte) (seed[k] ^ SALT_FACTOR[i]);
		}

		// 生成随机数作为盐
		salt = generateKey(salt);

		return salt;
	}

}
