package com.aafes.settlement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsUtils;

import com.aafes.settlement.filter.WebSecurityCorsFilter;
import com.aafes.settlement.sso.EiseSAMLUserDetailsService;
import com.github.ulisesbocchio.spring.boot.security.saml.annotation.EnableSAMLSSO;
import com.github.ulisesbocchio.spring.boot.security.saml.configurer.ServiceProviderBuilder;
import com.github.ulisesbocchio.spring.boot.security.saml.configurer.ServiceProviderConfigurerAdapter;

@EnableSAMLSSO
@Configuration
@Order(1)
public class SSOServiceProviderConfig
	extends
	ServiceProviderConfigurerAdapter
{

	/**
	 * This method will Configure the SAML SSO Properties
	 * 
	 * @param http
	 *            : HttpSecurity
	 * @throws Exception
	 *             throws an exception
	 */
	@Override
	public void configure(HttpSecurity http)
			throws Exception
	{
		/*
		 * http.authorizeRequests().antMatchers("/unprotected/**").permitAll ()
		 * .and().anonymous();
		 */

		http
				.authorizeRequests()
				.requestMatchers(CorsUtils::isCorsRequest).permitAll()
				.antMatchers("/probe").permitAll()
				.antMatchers("/api/**").permitAll()
				.antMatchers("/swagger-resources/**").permitAll()
				.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/v2/api-docs").permitAll()
				// .antMatchers("/**").permitAll()
				.and()
				/*
				 * .addFilterBefore( metadataGeneratorFilter(),
				 * ChannelProcessingFilter.class )
				 */
				// .addFilterAfter(metadataGeneratorFilter(),
				// BasicAuthenticationFilter.class)
				.addFilterBefore(
						new WebSecurityCorsFilter(),
						ChannelProcessingFilter.class
				);
	}

	/**
	 * This method will Configure the SAML SSO Properties
	 * 
	 * @param serviceProvider
	 *            Service builder instance
	 * 
	 */
	@Override
	public void configure(ServiceProviderBuilder serviceProvider) {

		WebSSOProfileConsumerImpl consumerImpl = new WebSSOProfileConsumerImpl();
		consumerImpl.setMaxAuthenticationAge(300000000);
		serviceProvider
				.authenticationProvider().userDetailsService(
						new EiseSAMLUserDetailsService()
				).and().metadataGenerator()
				.and()
				.sso()
				.samlEntryPoint(
						new EISESAMLEntryPoint()
				)
				// .profileOptions(profileOptions)
				.and()
				.metadataManager()
				.refreshCheckInterval(
						0
				)
				.and()
				.extendedMetadata()
				.idpDiscoveryEnabled(
						false
				)
				.and()
				.ssoProfileConsumer(consumerImpl);

	}

	@Bean
	public static org.springframework.security.saml.SAMLBootstrap
			SAMLBootstrap()
	{
		return new CustomSAMLBootstrap();
	}

}