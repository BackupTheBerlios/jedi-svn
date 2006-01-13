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

package de.berlios.jedi.presentation.admin.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;

import de.berlios.jedi.presentation.admin.AdminKeys;
import de.berlios.jedi.presentation.view.ViewAction;

/**
 * <p>
 * Saves to the request the state of the login process: logged in or not logged in.
 * </p>
 * 
 * <p>
 * Forward is made to "loginView".
 * </p>
 * 
 * TODO: add reference to generated XML format
 */
public class LoginViewAction extends ViewAction {

	/**
	 * Handle server requests.
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance.
	 * @param form
	 *            The optional ActionForm bean for this request (if any).
	 * @param request
	 *            The HTTP request we are processing.
	 * @param response
	 *            The HTTP response we are creating.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Boolean isLoggedIn = (Boolean) request.getSession().getAttribute(
				AdminKeys.IS_LOGGED_IN);

		Document document = getDocument("jedi", request);

		Element login = new Element("login");

		document.getRootElement().addContent(login);

		Element loginStatus = new Element("status");
		login.addContent(loginStatus);
		
		if (isLoggedIn != null && isLoggedIn.booleanValue()) {
			loginStatus.setAttribute("id", "loggedIn");
		} else {
			loginStatus.setAttribute("id", "notLoggedIn");
		}

		return mapping.findForward("loginView");
	}
}