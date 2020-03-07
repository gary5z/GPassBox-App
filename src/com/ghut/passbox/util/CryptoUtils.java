package com.ghut.passbox.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.util.Base64;

import com.ghut.passbox.common.Constants;
import com.ghut.passbox.common.crypto.ICipher;
import com.ghut.passbox.common.crypto.impl.AESCipher;
import com.ghut.passbox.common.crypto.impl.BCryptCipher;
import com.ghut.passbox.common.crypto.impl.HMacCipher;

/**
 * 
 * @author Gary
 * 
 */
public class CryptoUtils {

	/**
	 * ALGORITHM 算法 <br>
	 * 可替换为以下任意一种算法，同时key值的size相应改变。
	 * 
	 * <pre>
	 * DES          		key size must be equal to 56
	 * DESede(TripleDES) 	key size must be equal to 112 or 168
	 * AES          		key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
	 * Blowfish     		key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
	 * RC2          		key size must be between 40 and 1024 bits
	 * RC4(ARCFOUR) 		key size must be between 40 and 1024 bits
	 * </pre>
	 * 
	 * 在Key toKey(byte[] key)方法中使用下述代码
	 * <code>SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);</code> 替换
	 * <code>
	 * DESKeySpec dks = new DESKeySpec(key);
	 * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
	 * SecretKey secretKey = keyFactory.generateSecret(dks);
	 * </code>
	 */
	public static final String ALGORITHM = "AES";

	private static ICipher aesCipher = new AESCipher();

	private static ICipher bcryptCipher = new BCryptCipher();

	private static ICipher hmacCipher = new HMacCipher();

	/**
	 * AES加密
	 * 
	 * @param plainText
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String aesEncrypt(byte[] key, String plainText)
			throws Exception {
		String result = null;
		try {
			byte[] plainData = plainText.getBytes(Constants.UTF8);
			byte[] encryptedData = aesCipher.encrypt(key, plainData);
			result = base64Encode(encryptedData);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * AES解密
	 * 
	 * @param ciphertext
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String aesDecrypt(byte[] key, String ciphertext)
			throws Exception {
		byte[] base64data = base64Decode(ciphertext);
		byte[] decryptedData = aesCipher.decrypt(key, base64data);
		String result = new String(decryptedData, Constants.UTF8);
		return result;
	}

	/**
	 * HMAC加密
	 * 
	 * @param plainText
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String hmacEncrypt(String plainText) {
		String result = null;
		try {
			byte[] plainData = plainText.getBytes(Constants.UTF8);
			byte[] encryptedData = hmacCipher.encrypt(plainData, plainData);
			result = base64Encode(encryptedData);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return result;
	}

	/**
	 * BCrypt加密
	 * 
	 * @param key
	 * @param plainText
	 * @return
	 */
	public static String bcryptEncrypt(String plainText) {
		String result = null;
		try {
			byte[] plainData = plainText.getBytes(Constants.UTF8);
			byte[] encryptedData = bcryptCipher.encrypt(plainData, plainData);
			result = base64Encode(encryptedData);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return result;
	}

	/**
	 * 匹配Hash值
	 * 
	 * @param input
	 * @param storedValue
	 *            数据库保存的字符串经过了base64处理
	 * @return
	 * @throws Exception
	 */
	public static boolean bcryptMatch(String input, String storedValue)
			throws Exception {
		byte[] storedData = base64Decode(storedValue);
		return bcryptMatch(input.getBytes(Constants.UTF8), storedData);
	}

	/**
	 * 匹配Hash值
	 * 
	 * @param input
	 * @param hashedValue
	 * @return
	 * @throws Exception
	 */
	public static boolean bcryptMatch(byte[] input, byte[] hashValue)
			throws Exception {
		return bcryptCipher.match(input, input, hashValue);
	}

	public static byte[] utf8Encode(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}

		byte[] result = null;

		try {
			result = source.getBytes(Constants.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}

		return result;
	}

	public static String utf8Decode(byte[] utf8Data) {
		if (utf8Data == null) {
			return null;
		}

		String result = null;

		try {
			result = new String(utf8Data, Constants.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}

		return result;
	}

	public static String base64Encode(byte[] source) {
		String result = null;
		try {
			byte[] base64Data = Base64.encode(source, Base64.DEFAULT);
			result = new String(base64Data, Constants.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return result;
	}

	public static byte[] base64Decode(String base64String) {
		byte[] result = null;
		try {
			byte[] base64Data = base64String.getBytes(Constants.UTF8);
			result = Base64.decode(base64Data, Base64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return result;
	}

	public static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
				LogUtils.error(CryptoUtils.class, e);
			}
		}
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
}
