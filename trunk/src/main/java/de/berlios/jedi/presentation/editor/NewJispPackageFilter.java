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

package de.berlios.jedi.presentation.editor;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;

import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.presentation.Keys;
import de.berlios.jedi.presentation.admin.LoginFilter;

/**
 * <p>
 * Checks if there is a JispPackage being created stored in session.<br>
 * If the hasn't the JispPackage,  it forwards to the
 * NewJispPackageAction, using the name of the intercepted Action as the
 * value of the NEXT_FORWARD_NAME key.<br>
 * If the session already has the JispPackage, the next element in the chain is
 * invoked. 
 * </p>
 * <p>
 * This filter ensures that a Jisp Package, the Jisp Packages list and the
 * JispIdManager exist in session when actions which need them are called.
 * </p>
 */
public class NewJispPackageFilter implements Filter {

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
	 * Sets a new Jisp Package, the list of existent Jisp Packages and a
	 * JispIdManager in request session.<br>
	 * Jisp Packages from list are registered in JispIdManager.<br>
	 * If there is already a Jisp Package being created, nothing is done.
	 * 
	 * @throws ServletException
	 *             If a ServletException occurs when doing the filter.
	 * @throws IOException
	 *             If a IOException occurs when doing the filter.
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (!(request instanceof HttpServletRequest)) {
			LogFactory.getLog(LoginFilter.class).error(
					"Unexpected error: request in NewJispPackageFilter"
							+ "isn't a HttpServletRequest");
			throw new ServletException(
					"Unexpected error: request in NewJispPackageFilter"
							+ "isn't a HttpServletRequest");
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		JispPackage jispPackage = (JispPackage) ((HttpServletRequest) request)
				.getSession().getAttribute(EditorKeys.JISP_PACKAGE);

		try {
			if (jispPackage == null) {
				httpRequest.setAttribute(Keys.NEXT_FORWARD_NAME, httpRequest
						.getServletPath());
				httpRequest.getSession().getServletContext()
						.getRequestDispatcher("/NewJispPackage.do")
						.forward(request, response);
				return;
			}

			chain.doFilter(request, response);
		} catch (IOException e) {
			LogFactory.getLog(LoginFilter.class).error(
					"IOException in NewJispPackageFilter", e);
			throw e;
		} catch (ServletException e) {
			LogFactory.getLog(LoginFilter.class).error(
					"ServletException in NewJispPackageFilter", e);
			throw e;
		}
	}
}
