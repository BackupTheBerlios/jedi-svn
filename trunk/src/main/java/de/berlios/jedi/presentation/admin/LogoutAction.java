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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Logs out the admin.<br>
 * It removes from session all the stored objects when logging in (the Boolean,
 * the JispPackagesList and the JispIdManager).
 * </p>
 * <p>
 * If the NEXT_FORWARD_NAME AdminKey was set, it forwards to it. Else, a forward is
 * made to "success".
 * </p>
 */
public class LogoutAction extends Action {

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

		if (request.getSession().getAttribute(AdminKeys.IS_LOGGED_IN) != null) {
			request.getSession().setAttribute(AdminKeys.IS_LOGGED_IN, null);
		}

		if (request.getSession().getAttribute(AdminKeys.JISP_PACKAGES_LIST_KEY) != null) {
			request.getSession().setAttribute(AdminKeys.JISP_PACKAGES_LIST_KEY,
					null);
		}

		if (request.getSession().getAttribute(AdminKeys.JISP_ID_MANAGER_KEY) != null) {
			request.getSession().setAttribute(AdminKeys.JISP_ID_MANAGER_KEY,
					null);
		}

		String forwardName = (String) request
				.getAttribute(Keys.NEXT_FORWARD_NAME);
		if (forwardName != null) {
			return mapping.findForward(forwardName);
		}

		return mapping.findForward("success");
	}
}