package com.aafes.settlement.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * This class exposes the methods for encrypting and decrypting strings
 *
 */

public class AESEncryptDecryptUtil {

	private static SecretKeySpec secretKey;
	private static byte[]		 key;

	private static final Logger	 LOGGER	= LogManager.getLogger(
			AESEncryptDecryptUtil.class
	);

	// -------------------------------------------------------------------------
	/**
	 * This method sets the AES SecretKeySpec used for encryption or decryption
	 * 
	 * @param myKey
	 */
	public static void setKey(String p_myKey) {
		MessageDigest sha = null;
		try {
			key = p_myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-256");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException l_e) {
			LOGGER.error(
					"setKey(). Exception: " + l_e.getMessage()
			);
			l_e.printStackTrace();
		} catch (UnsupportedEncodingException l_e) {
			LOGGER.error(
					"setKey(). Exception: " + l_e.getMessage()
			);
			l_e.printStackTrace();
		}
	}
	// -------------------------------------------------------------------------

	/**
	 * This method outputs the encrypted string for the input parameter
	 * p_strToEncrypt using secret key p_secret
	 * 
	 * @param p_strToEncrypt
	 * @param p_secret
	 * @return
	 */
	public static String encrypt(String p_strToEncrypt, String p_secret) {
		try {
			setKey(p_secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(
					cipher.doFinal(p_strToEncrypt.getBytes("UTF-8"))
			);
		} catch (Exception l_e) {
			LOGGER.error(
					"encrypt(). Exception: " + l_e.getMessage()
			);
		}
		return null;
	}

	// -------------------------------------------------------------------------

	/**
	 * This method decrypts the encrypted string passed in for the parameter
	 * p_strToEncrypt using secret key p_secret
	 * 
	 * @param p_strToDecrypt
	 * @param p_secret
	 * @return
	 */
	public static String decrypt(String p_strToDecrypt, String p_secret) {
		try {
			setKey(p_secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(
					cipher.doFinal(Base64.getDecoder().decode(p_strToDecrypt))
			);
		} catch (Exception l_e) {
			LOGGER.error(
					"decrypt(). Exception: " + l_e.getMessage()
			);
		}
		return null;
	}

	// -------------------------------------------------------------------------
}
// ------------------------------------------------------------------------------
// END OF FILE
// ------------------------------------------------------------------------------
