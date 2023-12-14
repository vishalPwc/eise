package com.aafes.settlement.configuration.jwtconfig;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.aafes.settlement.configuration.GlobalParams;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * This Class is responsible for performing JWT operations like creation and
 * validation
 *
 */
@Component
public class JwtTokenUtil
	implements Serializable
{

	private static final long serialVersionUID	 = -2550185165626007488L;

	public static final long  JWT_TOKEN_VALIDITY = 28800;

	// -----------------------------------------------------------------------
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userName);
	}

	// -----------------------------------------------------------------------
	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	// -----------------------------------------------------------------------
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	// -----------------------------------------------------------------------
	public <T> T getClaimFromToken(
			String token, Function<Claims, T> claimsResolver
	)
	{
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// -----------------------------------------------------------------------
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(GlobalParams.jwtSecretkey)
				.parseClaimsJws(token).getBody();
	}

	// -----------------------------------------------------------------------
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// -----------------------------------------------------------------------
	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	// -----------------------------------------------------------------------

	/**
	 * This method will Generates the JWT Token
	 * 
	 * @param userDetails
	 * @return String
	 */

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	// -----------------------------------------------------------------------

	/**
	 * This method will Generates the JWT Token
	 * 
	 * @param claims
	 * @param subject
	 * @return String
	 */
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(
				new Date(System.currentTimeMillis())
		)
				.setExpiration(
						new Date(
								System.currentTimeMillis() + Integer.parseInt(
										GlobalParams.jwtExpiry
								)
										* 1000
						)
				).signWith(SignatureAlgorithm.HS512, GlobalParams.jwtSecretkey)
				.compact();
	}

	// -----------------------------------------------------------------------
	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	// -----------------------------------------------------------------------

	/**
	 * This method will Validates the JWT Token
	 * 
	 * @param token
	 * @param userDetails
	 * @return Boolean
	 */

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(
				token
		));
	}
	// -----------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------