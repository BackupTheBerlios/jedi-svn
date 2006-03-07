/*
 * Copyright 2005-2006 Daniel Calviño Sánchez
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 *
 * See the COPYING file for more details.
 */

package de.berlios.jedi.presentation.admin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Checks if the admin is logged in<br>
 * If admin isn't logged in, a forward to the login view is made.<br>
 * If admin is already logged in, the next element in the chain is invoked.
 * </p>
 * <p>
 * This filter prevents that not logged in users enter in the admin section of
 * the application.
 * </p>
 */
public class LoginFilter implements Filter {

	/**
	 * Empty implementation.
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * Empty implementation.
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) {
	}

	/**
	 * Checks if the admin is logged in<br>
	 * If admin isn't logged in, a forward to the login view is made.<br>
	 * If admin is already logged in, the next element in the chain is invoked.
	 * 
	 * @throws IOException
	 *             If an IOException occurs when filtering.
	 * @throws ServletException
	 *             If a ServletException occurs when filtering.
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		if (!(request instanceof HttpServletRequest)) {
			LogFactory.getLog(LoginFilter.class).error(
					"Unexpected error: request in LoginFilter"
							+ "isn't a HttpServletRequest");
			throw new ServletException(
					"Unexpected error: request in LoginFilter"
							+ "isn't a HttpServletRequest");
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Boolean isLoggedIn = (Boolean) httpRequest.getSession().getAttribute(
				AdminKeys.IS_LOGGED_IN);

		try {
			if (isLoggedIn == null || !isLoggedIn.booleanValue()) {
				httpRequest.getSession().getServletContext()
						.getRequestDispatcher("/Admin/LoginView.do").forward(
								request, response);
				return;
			}

			chain.doFilter(request, response);
		} catch (IOException e) {
			LogFactory.getLog(LoginFilter.class).error(
					"IOException in LoginFilter", e);
			throw e;
		} catch (ServletException e) {
			LogFactory.getLog(LoginFilter.class).error(
					"ServletException in LoginFilter", e);
			throw e;
		}
	}
}