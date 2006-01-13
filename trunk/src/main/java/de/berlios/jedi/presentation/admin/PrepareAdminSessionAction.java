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

import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.logic.LogicService;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.JispIdManager;
import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Prepares the session attributes needed for the admin actions.<br>
 * The JispPackageList containing all the saved JispPackages, and a new
 * JispIdManager in which the JispPackageList was registered are stored in
 * session using JISP_PACKAGES_LIST_KEY and JISP_ID_MANAGER_KEY in AdminKeys.
 * </p>
 * <p>
 * A forward is made to the NEXT_FORWARD_NAME Key. It should be stored where the
 * Action is called.
 * </p>
 */
public class PrepareAdminSessionAction extends ActionWithErrorSupport {

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
					"adminFailedJispPackagesListGetting");
		}

		setAttributesInRequest(request, jispPackagesList);

		return mapping.findForward((String) request
				.getAttribute(Keys.NEXT_FORWARD_NAME));
	}

	/**
	 * Set the needed attributes in the request's session.<br>
	 * The JispPackageList containing all the saved JispPackages, and a new
	 * JispIdManager in which the JispPackageList was registered are stored in
	 * session using JISP_PACKAGES_LIST_KEY and JISP_ID_MANAGER_KEY in
	 * AdminKeys.
	 * 
	 * @param request
	 *            The request to save the elements in.
	 * @param jispPackagesList
	 *            The JispPackagesList to save.
	 */
	private void setAttributesInRequest(HttpServletRequest request,
			JispPackagesList jispPackagesList) {
		JispIdManager jispIdManager = new JispIdManager();
		jispIdManager.registerJispPackagesList(jispPackagesList);

		request.getSession().setAttribute(AdminKeys.JISP_PACKAGES_LIST_KEY,
				jispPackagesList);
		request.getSession().setAttribute(AdminKeys.JISP_ID_MANAGER_KEY,
				jispIdManager);
	}
}
