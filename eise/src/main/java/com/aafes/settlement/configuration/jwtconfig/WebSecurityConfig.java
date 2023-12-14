package com.aafes.settlement.configuration.jwtconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class extends the WebSecurityConfigurerAdapter is a convenience class
 * that allows customization to both WebSecurity and HttpSecurity.
 *
 */
@Configuration
@EnableWebSecurity
@Order(2)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig
	extends
	WebSecurityConfigurerAdapter
{

	@Autowired
	private JwtAuthenticationEntryPoint	jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService			jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter			jwtRequestFilter;

	private AuthenticationManager		authManager	= null;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception
	{
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(
				passwordEncoder()
		);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public void setAuthenticationManagerBean()
			throws Exception
	{
		this.authManager = super.authenticationManagerBean();
	}

	public AuthenticationManager getAuthenticationManagerBean()
			throws Exception
	{
		return this.authManager;
	}

	/**
	 * This method will configure the security for every request
	 * 
	 * @param httpSecurity
	 * @throws Exception
	 */
	@Override
	protected void configure(
			HttpSecurity httpSecurity
	)
			throws Exception
	{
		setAuthenticationManagerBean();
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers(
						"/api/authenticate/**", "/swagger-resources/**",
						"/api/login/doSimulatedLogin", "/swagger-ui.html",
						"/v2/api-docs",
						"/probe",
						"/webjars/**"
				).permitAll().
				// all other requests need to be authenticated
				anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(
						jwtAuthenticationEntryPoint
				).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		jwtRequestFilter.setAuthenticationManagerBean(
				this.getAuthenticationManagerBean()
		);
		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(
				jwtRequestFilter, UsernamePasswordAuthenticationFilter.class
		);
	}
	// ------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------