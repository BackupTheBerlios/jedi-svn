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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import de.berlios.jedi.common.entity.jisp.JispIcon;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.logic.editor.EditorLogicService;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.JispIdManager;
import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Removes a Jisp Icon from the Jisp Package being created.<br>
 * Icon is identified by the id, and therefore it should have been registered in
 * the JispIdManager.
 * </p>
 * <p>
 * If everything went right, a forward to "success" is made.<br>
 * Else, a forward to ADD_STATUS_FORWARD_NAME Key is made and the
 * NEXT_FORWARD_NAME Key is set to the error forward name.
 * </p>
 */
public class RemoveIconFromPackageAction extends ActionWithErrorSupport {

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

		JispPackage jispPackage = (JispPackage) request.getSession()
				.getAttribute(EditorKeys.JISP_PACKAGE);
		JispIdManager jispIdManager = (JispIdManager) request.getSession()
				.getAttribute(EditorKeys.JISP_ID_MANAGER_KEY);
		String jispIconId = request.getParameter("jispIconId");

		if (jispIconId == null) {
			return errorForward(mapping, request, new ActionMessage("missedId",
					"jispIconId"), "missedId", Keys.ADD_STATUS_FORWARD_NAME);
		}

		JispIcon jispIcon = jispIdManager.getJispIcon(jispIconId);

		if (jispIcon == null) {
			return errorForward(mapping, request, new ActionMessage(
					"invalidId", "jispIconId"), "invalidId",
					Keys.ADD_STATUS_FORWARD_NAME);
		}

		new EditorLogicService().removeJispIconFromJispPackage(jispIcon,
				jispPackage);
		jispIdManager.deregisterJispIcon(jispIcon);

		return mapping.findForward("success");
	}
}
