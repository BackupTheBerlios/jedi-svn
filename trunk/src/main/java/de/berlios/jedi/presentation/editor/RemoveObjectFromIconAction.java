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
import de.berlios.jedi.common.entity.jisp.JispObject;
import de.berlios.jedi.logic.editor.EditorLogicService;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.JispIdManager;
import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Removes a Jisp Object from a Jisp Icon.<br>
 * Object and icon are identified by the id, and therefore both should have been
 * registered in the JispIdManager.
 * </p>
 * <p>
 * If everything went right, a forward to "success" is made.<br>
 * Else, a forward to ADD_STATUS_FORWARD_NAME Key is made and the
 * NEXT_FORWARD_NAME Key is set to the error forward name.
 * </p>
 */
public class RemoveObjectFromIconAction extends ActionWithErrorSupport {

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

		JispIdManager jispIdManager = (JispIdManager) request.getSession()
				.getAttribute(EditorKeys.JISP_ID_MANAGER_KEY);
		String jispIconId = request.getParameter("jispIconId");
		String jispObjectId = request.getParameter("jispObjectId");

		ActionForward actionForward = checkIds(mapping, request, jispIconId,
				jispObjectId);
		if (actionForward != null) {
			return actionForward;
		}

		JispIcon jispIcon = jispIdManager.getJispIcon(jispIconId);
		JispObject jispObject = jispIdManager.getJispObject(jispObjectId);

		actionForward = checkObjects(mapping, request, jispIcon, jispObject);
		if (actionForward != null) {
			return actionForward;
		}

		new EditorLogicService().removeJispObjectFromJispIcon(jispObject,
				jispIcon);
		jispIdManager.deregisterJispObject(jispObject);

		return mapping.findForward("success");
	}

	/**
	 * Checks if the ids aren't null.<br>
	 * If the ids aren't null returns null. Otherwise, a forward to
	 * ADD_STATUS_FORWARD_NAME Key is returned and the NEXT_FORWARD_NAME
	 * EditorKey is set to the error forward name
	 * 
	 * @param mapping
	 *            The mapping to use.
	 * @param request
	 *            The request to use.
	 * @param jispIconId
	 *            The jispIconId to check.
	 * @param jispObjectId
	 *            The jispObjectId to check.
	 * @return Null if the ids aren't null; otherwise, a forward to
	 *         ADD_STATUS_FORWARD_NAME Key.
	 */
	private ActionForward checkIds(ActionMapping mapping,
			HttpServletRequest request, String jispIconId, String jispObjectId) {
		if (jispIconId == null) {
			return errorForward(mapping, request, new ActionMessage("missedId",
					"jispIconId"), "missedId", Keys.ADD_STATUS_FORWARD_NAME);
		}

		if (jispObjectId == null) {
			return errorForward(mapping, request, new ActionMessage("missedId",
					"jispObjectId"), "missedId",
					Keys.ADD_STATUS_FORWARD_NAME);
		}

		return null;
	}

	/**
	 * Checks if the objects aren't null.<br>
	 * If the objects aren't null returns null. Otherwise, a forward to
	 * ADD_STATUS_FORWARD_NAME Key is returned and the NEXT_FORWARD_NAME
	 * EditorKey is set to the error forward name
	 * 
	 * @param mapping
	 *            The mapping to use.
	 * @param request
	 *            The request to use.
	 * @param jispIcon
	 *            The jispIcon to check.
	 * @param jispObject
	 *            The jispObject to check.
	 * @return Null if the ids aren't null; otherwise, a forward to
	 *         ADD_STATUS_FORWARD_NAME Key.
	 */
	private ActionForward checkObjects(ActionMapping mapping,
			HttpServletRequest request, JispIcon jispIcon, JispObject jispObject) {
		if (jispIcon == null) {
			return errorForward(mapping, request, new ActionMessage(
					"invalidId", "jispIconId"), "invalidId",
					Keys.ADD_STATUS_FORWARD_NAME);
		}

		if (jispObject == null) {
			return errorForward(mapping, request, new ActionMessage(
					"invalidId", "jispObjectId"), "invalidId",
					Keys.ADD_STATUS_FORWARD_NAME);
		}

		return null;
	}
}
