package com.aafes.settlement.util;

public class MaoKeyGenerator {

	/**
	 * This method generates encrypted key for MAO
	 * 
	 * @param p_extSystem
	 * @param p_userName
	 * @param p_password
	 * @param p_secret
	 * @return String
	 */
	public String getEncryptedKey(
			String p_extSystem, String p_userName, String p_password,
			String p_secret
	)
	{
		String l_encrypted = Utils.getEncryptedMaoKey(
				p_extSystem, p_userName, p_password, p_secret
		);
		return l_encrypted;
	}

	/**
	 * This method decrypts the key sent from MAO for accessing getToken API
	 * 
	 * @param p_key
	 * @param p_secret
	 * @return
	 */
	public String getDecryptedKey(String p_key, String p_secret) {
		String l_decrypted = Utils.getDecryptedMaoKey(p_key, p_secret);
		return l_decrypted;
	}

	/*
	 * public static void main(String[] args) { MaoKeyGenerator keyGen = new
	 * MaoKeyGenerator(); String l_extSystem = "mao"; String l_username =
	 * "eise"; String l_password = "eagle"; // String l_password = //
	 * "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6"; String
	 * l_secret = "7Npc3RLrpd76";
	 * 
	 * if (args[0] != null) { l_extSystem = args[0]; } if (args[1] != null) {
	 * l_username = args[0]; } if (args[2] != null) { l_password = args[0]; }
	 * 
	 * String l_encryptedString = keyGen.getEncryptedKey( l_extSystem,
	 * l_username, l_password, l_secret );
	 * System.out.println("Encrypted key :");
	 * System.out.println(l_encryptedString);
	 * 
	 * String l_decryptedString = keyGen.getDecryptedKey( l_encryptedString,
	 * l_secret ); System.out.println("Decrypted key :");
	 * System.out.println(l_decryptedString);
	 * 
	 * }
	 */

}
