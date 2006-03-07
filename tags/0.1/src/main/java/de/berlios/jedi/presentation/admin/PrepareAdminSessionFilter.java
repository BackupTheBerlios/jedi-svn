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

import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import de.berlios.jedi.presentation.JispIdManager;
import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Checks if the session has the needed attributes for the admin actions.<br>
 * If the session hasn't those attributes, it forwards to the
 * PrepareAdminSessionAction, using the name of the intercepted Action as the
 * value of the NEXT_FORWARD_NAME key.<br>
 * If the session already has those attributes, the next element in the chain is
 * invoked.
 * </p>
 * <p>
 * This filter ensures that the admin actions have the needed attributes to
 * work.
 * </p>
 */
public class PrepareAdminSessionFilter implements Filter {

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
	 * Checks if the session has the needed attributes for the admin actions.<br>
	 * If the session hasn't those attributes, it forwards to the
	 * PrepareAdminSessionAction, using the name of the intercepted Action as
	 * the value of the NEXT_FORWARD_NAME key.<br>
	 * If the session already has those attributes, the next element in the
	 * chain is invoked.
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
					"Unexpected error: request in PrepareAdminSessionFilter"
							+ "isn't a HttpServletRequest");
			throw new ServletException(
					"Unexpected error: request in PrepareAdminSessionFilter"
							+ "isn't a HttpServletRequest");
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		JispPackagesList jispPackagesList = (JispPackagesList) httpRequest
				.getSession().getAttribute(AdminKeys.JISP_PACKAGES_LIST_KEY);
		JispIdManager jispIdManager = (JispIdManager) httpRequest.getSession()
				.getAttribute(AdminKeys.JISP_ID_MANAGER_KEY);

		try {
			if (jispPackagesList == null || jispIdManager == null) {
				httpRequest.setAttribute(Keys.NEXT_FORWARD_NAME, httpRequest
						.getServletPath());
				httpRequest.getSession().getServletContext()
						.getRequestDispatcher("/Admin/PrepareAdminSession.do")
						.forward(request, response);
				return;
			}

			chain.doFilter(request, response);
		} catch (IOException e) {
			LogFactory.getLog(LoginFilter.class).error(
					"IOException in PrepareAdminSessionFilter", e);
			throw e;
		} catch (ServletException e) {
			LogFactory.getLog(LoginFilter.class).error(
					"ServletException in PrepareAdminSessionFilter", e);
			throw e;
		}
	}
}
