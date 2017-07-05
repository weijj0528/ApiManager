package cn.crap.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密处理
 * 
 */
public class SecretUtils {

	public static void main(String[] args) {

		String secret = secretString("13381607331$100001$" + System.currentTimeMillis(), "SHA");

		System.out.println(secret);
		System.out.println(secret.length());

	}

	/**
	 * 对字符串按照制定算法进行加密,并返回加密后的BASE64编码转换
	 * 
	 * @param source
	 * @param algorithm:
	 *            support such as -- MD2,MD5,SHA-1,SHA-256,SHA-384,SHA-512
	 * 
	 * @return String
	 */
	public static String secretString(String source, String algorithm) {

		if (source == null || source.length() < 1)

			throw new IllegalArgumentException("Incorrect string source: empty input!");

		try {

			MessageDigest alga = MessageDigest.getInstance(algorithm);

			alga.update(source.getBytes());

			byte[] hash = alga.digest();

			return base64Encode(hash);

		} catch (NoSuchAlgorithmException e) {

			System.err.println(e.getMessage());
			return "";

		}
	}

	/**
	 * 对字符串按照给定密钥进行HmacSHA1加密,并返回加密后的BASE64编码转换
	 * 
	 * @param encryptText
	 * @param encryptKey
	 * 
	 * @return String
	 */
	public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {

		String MAC_NAME = "HmacSHA1";
		String ENCODING = "UTF-8";

		byte[] data = encryptKey.getBytes(ENCODING);

		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);

		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance(MAC_NAME);

		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);

		byte[] text = encryptText.getBytes(ENCODING);

		// 完成 Mac 操作
		return base64Encode(mac.doFinal(text));
	}

	/**
	 * 将字符串转换成BASE64编码
	 * 
	 * @param src
	 * @return
	 */
	public static String base64Encode(String src) {

		return StringUtils.isEmpty(src) ? "" : base64Encode(src.getBytes());

	}

	/**
	 * 将字符串转换成BASE64编码
	 * 
	 * @param byte[]
	 * @return
	 */
	public static String base64Encode(byte b[]) {

		return (null == b || b.length < 1) ? "" : new BASE64Encoder().encode(b);

	}

	/**
	 * 还原BASE64编码的字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String base64Decode(String src) {

		try {

			return new String(new BASE64Decoder().decodeBuffer(src));

		} catch (Exception e) {

			return "";

		}

	}

}
