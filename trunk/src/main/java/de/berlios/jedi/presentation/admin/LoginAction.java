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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import de.berlios.jedi.common.admin.exception.IncorrectPasswordException;
import de.berlios.jedi.logic.admin.AdminLogicService;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Logs in the admin.<br>
 * If everything went right, a Boolean is stored in session to indicate that the
 * admin is now logged in. A forward is made to "success", and the
 * NEXT_FORWARD_NAME Key is set to "adminView".
 * <p>
 * Else, if the password isn't correct, a forward to "incorrectPassword" is made
 * if the admin wasn't logged in. If it was already logged in, and tried to
 * login again with an incorrect password, a forward to "logout" is made, and
 * the NEXT_FORWARD_NAME Key is set to the error forward name.
 * </p>
 */
public class LoginAction extends ActionWithErrorSupport {

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

		String password = request.getParameter("password");
		try {
			new AdminLogicService().login(password);
		} catch (IncorrectPasswordException e) {
			LogFactory.getLog(LoginAction.class).warn(
					"An invalid password was used to try logging in", e);

			// Already logged in
			if (isLoggedIn != null && isLoggedIn.booleanValue()) {
				return errorForward(mapping, request, new ActionMessage(
						"incorrectPassword"), "loginIncorrectPassword",
						"logout");
			}

			return errorForward(mapping, request, new ActionMessage(
					"incorrectPassword"), "loginIncorrectPassword");
		}

		// Already logged in
		if (isLoggedIn != null && isLoggedIn.booleanValue()) {
			return mapping.findForward("success");
		}

		request.getSession().setAttribute(AdminKeys.IS_LOGGED_IN,
				new Boolean(true));

		request.setAttribute(Keys.NEXT_FORWARD_NAME, "adminView");

		return mapping.findForward("success");
	}
}