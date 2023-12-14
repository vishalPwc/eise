package com.aafes.settlement.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class WebSecurityCorsFilter
	implements Filter
{
	@Override
	public void init(FilterConfig filterConfig)
			throws ServletException
	{}

	/**
	 * This Method will filters the every request and will add headers in
	 * response .
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(
			ServletRequest request, ServletResponse response, FilterChain chain
	)
			throws IOException,
			ServletException
	{
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader(
				"Access-Control-Allow-Methods",
				"POST, GET, OPTIONS, DELETE, PUT"
		);
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader(
				"Access-Control-Allow-Headers",
				"Authorization, Content-Type, Accept, x-requested-with, Cache-Control"
		);
		chain.doFilter(request, res);
	}

	@Override
	public void destroy() {}
}
