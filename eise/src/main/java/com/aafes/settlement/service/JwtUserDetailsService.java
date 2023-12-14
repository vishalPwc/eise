package com.aafes.settlement.service;

import java.util.ArrayList;
import java.util.Base64;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.GlobalParams;

@Service
public class JwtUserDetailsService
	implements UserDetailsService
{
	/**
	 * This method will returns Userdetails by username
	 * 
	 * @param username
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException
	{
		try {

			/*
			 * UserDetails userDetails = SAMLInfoService.getSAMLInfo(username);
			 * if (userDetails == null) {
			 */
			String base64password = GlobalParams.jwtPassword;
			byte[] decodedBytes = Base64.getDecoder().decode(
					base64password
			);
			String decodedString = new String(decodedBytes);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			if (GlobalParams.jwtUsername.equals(username)) {
				return new User(
						GlobalParams.jwtUsername, passwordEncoder.encode(
								decodedString
						),
						new ArrayList<>()
				);
			} else {
				throw new UsernameNotFoundException(
						"User not found with username: " + username
				);
			}
			/*
			 * } else { return userDetails; }
			 */
		} catch (Exception e) {
			throw new UsernameNotFoundException(
					"User not found with username: " + username
			);
		}
	}

}