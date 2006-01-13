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
import de.berlios.jedi.common.entity.jisp.JispText;
import de.berlios.jedi.logic.editor.EditorLogicService;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.JispIdManager;
import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Removes a Jisp Text from a Jisp Icon.<br>
 * Text and icon are identified by the id, and therefore both should have been
 * registered in the JispIdManager.
 * </p>
 * <p>
 * If everything went right, a forward to "success" is made.<br>
 * Else, a forward to ADD_STATUS_FORWARD_NAME Key is made and the
 * NEXT_FORWARD_NAME Key is set to the error forward name.
 * </p>
 */
public class RemoveTextFromIconAction extends ActionWithErrorSupport {

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
		String jispTextId = request.getParameter("jispTextId");

		ActionForward actionForward = checkIds(mapping, request, jispIconId,
				jispTextId);
		if (actionForward != null) {
			return actionForward;
		}

		JispIcon jispIcon = jispIdManager.getJispIcon(jispIconId);
		JispText jispText = jispIdManager.getJispText(jispTextId);

		actionForward = checkObjects(mapping, request, jispIcon, jispText);
		if (actionForward != null) {
			return actionForward;
		}

		new EditorLogicService().removeJispTextFromJispIcon(jispText, jispIcon);
		jispIdManager.deregisterJispText(jispText);

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
	 * @param jispTextId
	 *            The jispTextId to check.
	 * @return Null if the ids aren't null; otherwise, a forward to
	 *         ADD_STATUS_FORWARD_NAME Key.
	 */
	private ActionForward checkIds(ActionMapping mapping,
			HttpServletRequest request, String jispIconId, String jispTextId) {
		if (jispIconId == null) {
			return errorForward(mapping, request, new ActionMessage("missedId",
					"jispIconId"), "missedId", Keys.ADD_STATUS_FORWARD_NAME);
		}

		if (jispTextId == null) {
			return errorForward(mapping, request, new ActionMessage("missedId",
					"jispTextId"), "missedId", Keys.ADD_STATUS_FORWARD_NAME);
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
	 * @param jispText
	 *            The jispText to check.
	 * @return Null if the ids aren't null; otherwise, a forward to
	 *         ADD_STATUS_FORWARD_NAME Key.
	 */
	private ActionForward checkObjects(ActionMapping mapping,
			HttpServletRequest request, JispIcon jispIcon, JispText jispText) {
		if (jispIcon == null) {
			return errorForward(mapping, request, new ActionMessage(
					"invalidId", "jispIconId"), "invalidId",
					Keys.ADD_STATUS_FORWARD_NAME);
		}

		if (jispText == null) {
			return errorForward(mapping, request, new ActionMessage(
					"invalidId", "jispTextId"), "invalidId",
					Keys.ADD_STATUS_FORWARD_NAME);
		}

		return null;
	}
}
