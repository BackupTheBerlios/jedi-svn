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

package de.berlios.jedi.presentation;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;

import de.berlios.jedi.presentation.admin.LoginFilter;

/**
 * <p>
 * Sets the selected stxx transform in every request.<br>
 * This transform will be used by stxx to generate the resulting XML page,
 * applying one XSL Transformation or another based on its value. The used
 * transform depends on the user interface being used.
 * </p>
 * <p>
 * The transform set is taken from a request parameter or, if there's no
 * transform set in request parameter, from a session attribute. If no interface
 * can be taken, stxx uses default output, which is html.
 * </p>
 */
public class StxxTransformSelectionFilter implements Filter {

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
	public void init(FilterConfig filterConfig) {
	}

	/**
	 * Sets the selected stxx transform in every request.<br>
	 * The transform set is taken from a request parameter or, if there's no
	 * transform set in request parameter, from a session attribute. If no
	 * interface can be taken, stxx uses default output, which is html.
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
			LogFactory.getLog(StxxTransformSelectionFilter.class).error(
					"Unexpected error: request in StxxTransformSelectionFilter"
							+ "isn't a HttpServletRequest");
			throw new ServletException(
					"Unexpected error: request in StxxTransformSelectionFilter"
							+ "isn't a HttpServletRequest");
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		try {
			String transform = httpRequest.getParameter("transform");

			if (transform != null) {
				httpRequest.getSession().setAttribute("transform", transform);
			}

			if ((transform = (String) httpRequest.getSession().getAttribute(
					"transform")) != null) {
				httpRequest.setAttribute("transform", transform);
			}

			chain.doFilter(request, response);
		} catch (IOException e) {
			LogFactory.getLog(LoginFilter.class).error(
					"IOException in StxxTransformSelectionFilter", e);
			throw e;
		} catch (ServletException e) {
			LogFactory.getLog(LoginFilter.class).error(
					"ServletException in StxxTransformSelectionFilter", e);
			throw e;
		}
	}
}
