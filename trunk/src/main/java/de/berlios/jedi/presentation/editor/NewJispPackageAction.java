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

import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.logic.LogicService;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.JispIdManager;
import de.berlios.jedi.presentation.Keys;
import de.berlios.jedi.presentation.admin.LoginAction;

/**
 * <p>
 * Prepares the session attributes needed for the jisp customizing actions.<br>
 * The JispPackageList containing all the saved JispPackages, a new JispPackage
 * (the one being made by the user) and a new JispIdManager in which the
 * JispPackageList and the JispPackage were registered are stored in session
 * using JISP_PACKAGES_LIST_KEY, JISP_PACKAGE and JISP_ID_MANAGER_KEY in
 * EditorKeys.
 * </p>
 * <p>
 * If the NEXT_FORWARD_NAME Key is set (it should have been stored where the
 * Action was called), a forward to it is made. Otherwise, a forward to
 * "success" is made.
 * </p>
 */
public class NewJispPackageAction extends ActionWithErrorSupport {

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
		JispPackagesList jispPackagesList = null;
		try {
			jispPackagesList = new LogicService().getJispPackagesList();
		} catch (DataException e) {
			LogFactory.getLog(LoginAction.class).error(
					"JispPackages list couldn't be get", e);

			return errorForward(mapping, request, new ActionMessage(
					"failedJispPackagesListGetting"),
					"failedJispPackagesListGetting",
					Keys.ADD_STATUS_FORWARD_NAME);
		}

		setAttributesInRequest(request, jispPackagesList);

		String forwardName = (String) request
				.getAttribute(Keys.NEXT_FORWARD_NAME);
		if (forwardName != null) {
			return mapping.findForward(forwardName);
		}

		return mapping.findForward("success");
	}

	/**
	 * Set the needed attributes in the request's session.<br>
	 * The JispPackageList containing all the saved JispPackages, a new
	 * JispPackage (the one being made by the user) and a new JispIdManager in
	 * which the JispPackageList and the JispPackage were registered are stored
	 * in session using JISP_PACKAGES_LIST_KEY, JISP_PACKAGE and
	 * JISP_ID_MANAGER_KEY in EditorKeys.
	 * 
	 * @param request
	 *            The request to save the elements in.
	 * @param jispPackagesList
	 *            The JispPackagesList to save.
	 */
	private void setAttributesInRequest(HttpServletRequest request,
			JispPackagesList jispPackagesList) {
		JispPackage jispPackage = new JispPackage();

		JispIdManager jispIdManager = new JispIdManager();
		jispIdManager.registerJispPackagesList(jispPackagesList);
		jispIdManager.registerJispPackage(jispPackage);

		request.getSession().setAttribute(EditorKeys.JISP_PACKAGES_LIST_KEY,
				jispPackagesList);
		request.getSession().setAttribute(EditorKeys.JISP_PACKAGE, jispPackage);
		request.getSession().setAttribute(EditorKeys.JISP_ID_MANAGER_KEY,
				jispIdManager);
	}
}
