package top.wecoding.xuanwu.core.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import top.wecoding.xuanwu.core.helper.ExceptionHelper;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * @author liuyuhui
 */
public class RSAUtil {

	/** 数字签名，密钥算法 */
	public static final String RSA_ALGORITHM = "RSA";

	public static final String RSA_PADDING = "RSA/ECB/PKCS1Padding";

	/**
	 * 获取 KeyPair
	 * @return KeyPair
	 */
	public static KeyPair genKeyPair() {
		return genKeyPair(4096);
	}

	/**
	 * 获取 KeyPair
	 * @param keySize key size
	 * @return KeyPair
	 */
	public static KeyPair genKeyPair(int keySize) {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
			// 密钥位数
			keyPairGen.initialize(keySize);
			// 密钥对
			KeyPair keyPair = keyPairGen.generateKeyPair();
			return new KeyPair(keyPair.getPublic(), keyPair.getPrivate());
		}
		catch (NoSuchAlgorithmException e) {
			throw ExceptionHelper.unchecked(e);
		}
	}

	/**
	 * 生成RSA私钥
	 * @param modulus N特征值
	 * @param exponent d特征值
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(String modulus, String exponent) {
		return generatePrivateKey(new BigInteger(modulus), new BigInteger(exponent));
	}

	/**
	 * 生成RSA私钥
	 * @param modulus N特征值
	 * @param exponent d特征值
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(BigInteger modulus, BigInteger exponent) {
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw ExceptionHelper.unchecked(e);
		}
	}

	/**
	 * 生成RSA公钥
	 * @param modulus N特征值
	 * @param exponent e特征值
	 * @return {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(String modulus, String exponent) {
		return generatePublicKey(new BigInteger(modulus), new BigInteger(exponent));
	}

	/**
	 * 生成RSA公钥
	 * @param modulus N特征值
	 * @param exponent e特征值
	 * @return {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(BigInteger modulus, BigInteger exponent) {
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw ExceptionHelper.unchecked(e);
		}
	}

	/**
	 * 得到公钥
	 * @param base64PubKey 密钥字符串（经过base64编码）
	 * @return PublicKey
	 */
	public static PublicKey getPublicKey(String base64PubKey) {
		Objects.requireNonNull(base64PubKey, "base64 public key is null.");
		byte[] keyBytes = Base64.getDecoder().decode(base64PubKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw ExceptionHelper.unchecked(e);
		}
	}

	/**
	 * 得到公钥字符串
	 * @param base64PubKey 密钥字符串（经过base64编码）
	 * @return PublicKey String
	 */
	public static String getPublicKeyToBase64(String base64PubKey) {
		PublicKey publicKey = getPublicKey(base64PubKey);
		return getKeyString(publicKey);
	}

	/**
	 * 得到私钥
	 * @param base64PriKey 密钥字符串（经过base64编码）
	 * @return PrivateKey
	 */
	public static PrivateKey getPrivateKey(String base64PriKey) {
		Objects.requireNonNull(base64PriKey, "base64 private key is null.");
		byte[] keyBytes = Base64.getDecoder().decode(base64PriKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw ExceptionHelper.unchecked(e);
		}
	}

	/**
	 * 得到密钥字符串（经过base64编码）
	 * @param key key
	 * @return base 64 编码后的 key
	 */
	public static String getKeyString(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	/**
	 * 得到私钥 base64
	 * @param base64PriKey 密钥字符串（经过base64编码）
	 * @return PrivateKey String
	 */
	public static String getPrivateKeyToBase64(String base64PriKey) {
		PrivateKey privateKey = getPrivateKey(base64PriKey);
		return getKeyString(privateKey);
	}

	/**
	 * 公钥加密
	 * @param base64PublicKey base64 的公钥
	 * @param data 待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encrypt(String base64PublicKey, byte[] data) {
		return encrypt(getPublicKey(base64PublicKey), data);
	}

	/**
	 * 公钥加密
	 * @param publicKey 公钥
	 * @param data 待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		return rsa(publicKey, data, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 私钥加密，用于 qpp 内，公钥解密
	 * @param base64PrivateKey base64 的私钥
	 * @param data 待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encryptByPrivateKey(String base64PrivateKey, byte[] data) {
		return encryptByPrivateKey(getPrivateKey(base64PrivateKey), data);
	}

	/**
	 * 私钥加密，加密成 base64 字符串，用于 qpp 内，公钥解密
	 * @param base64PrivateKey base64 的私钥
	 * @param data 待加密的内容
	 * @return 加密后的内容
	 */
	public static String encryptByPrivateKeyToBase64(String base64PrivateKey, byte[] data) {
		return Base64.getEncoder().encodeToString(encryptByPrivateKey(base64PrivateKey, data));
	}

	/**
	 * 私钥加密，用于 qpp 内，公钥解密
	 * @param privateKey 私钥
	 * @param data 待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encryptByPrivateKey(PrivateKey privateKey, byte[] data) {
		return rsa(privateKey, data, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 公钥加密
	 * @param publicKey PublicKey
	 * @param data 待加密的内容
	 * @return 加密后的内容
	 */
	@Nullable
	public static String encryptToBase64(PublicKey publicKey, @Nullable String data) {
		if (!StringUtils.hasText(data)) {
			return null;
		}
		return Base64.getEncoder().encodeToString(encrypt(publicKey, data.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * 公钥加密
	 * @param base64PublicKey base64 公钥
	 * @param data 待加密的内容
	 * @return 加密后的内容
	 */
	@Nullable
	public static String encryptToBase64(String base64PublicKey, @Nullable String data) {
		return encryptToBase64(getPublicKey(base64PublicKey), data);
	}

	/**
	 * 解密
	 * @param base64PrivateKey base64 私钥
	 * @param data 数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(String base64PrivateKey, byte[] data) {
		return decrypt(getPrivateKey(base64PrivateKey), data);
	}

	/**
	 * 解密
	 * @param base64publicKey base64 公钥
	 * @param data 数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKey(String base64publicKey, byte[] data) {
		return decryptByPublicKey(getPublicKey(base64publicKey), data);
	}

	/**
	 * 解密
	 * @param privateKey privateKey
	 * @param data 数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		return rsa(privateKey, data, Cipher.DECRYPT_MODE);
	}

	/**
	 * 解密
	 * @param publicKey PublicKey
	 * @param data 数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKey(PublicKey publicKey, byte[] data) {
		return rsa(publicKey, data, Cipher.DECRYPT_MODE);
	}

	/**
	 * rsa 加、解密
	 * @param key key
	 * @param data 数据
	 * @param mode 模式
	 * @return 解密后的数据
	 */
	private static byte[] rsa(Key key, byte[] data, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_PADDING);
			cipher.init(mode, key);
			return cipher.doFinal(data);
		}
		catch (Exception e) {
			throw ExceptionHelper.unchecked(e);
		}
	}

	/**
	 * base64 数据解密
	 * @param publicKey PublicKey
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKeyFromBase64(PublicKey publicKey, byte[] base64Data) {
		return decryptByPublicKey(publicKey, Base64.getDecoder().decode(base64Data));
	}

	/**
	 * base64 数据解密
	 * @param base64PublicKey base64 公钥
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKeyFromBase64(String base64PublicKey, byte[] base64Data) {
		return decryptByPublicKeyFromBase64(getPublicKey(base64PublicKey), base64Data);
	}

	/**
	 * base64 数据解密
	 * @param privateKey PrivateKey
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptFromBase64(PrivateKey privateKey, @Nullable String base64Data) {
		if (!StringUtils.hasText(base64Data)) {
			return null;
		}
		return new String(decrypt(privateKey, Base64.getDecoder().decode(base64Data)), StandardCharsets.UTF_8);
	}

	/**
	 * base64 数据解密
	 * @param base64PrivateKey base64 私钥
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptFromBase64(String base64PrivateKey, @Nullable String base64Data) {
		return decryptFromBase64(getPrivateKey(base64PrivateKey), base64Data);
	}

	/**
	 * base64 数据解密
	 * @param base64PrivateKey base64 私钥
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptFromBase64(String base64PrivateKey, byte[] base64Data) {
		return decrypt(base64PrivateKey, Base64.getDecoder().decode(base64Data));
	}

	/**
	 * base64 数据解密
	 * @param publicKey PublicKey
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptByPublicKeyFromBase64(PublicKey publicKey, @Nullable String base64Data) {
		if (!StringUtils.hasText(base64Data)) {
			return null;
		}
		return new String(decryptByPublicKey(publicKey, Base64.getDecoder().decode(base64Data)),
				StandardCharsets.UTF_8);
	}

	/**
	 * base64 数据解密
	 * @param base64PublicKey base64 公钥
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptByPublicKeyFromBase64(String base64PublicKey, @Nullable String base64Data) {
		return decryptByPublicKeyFromBase64(getPublicKey(base64PublicKey), base64Data);
	}

}
