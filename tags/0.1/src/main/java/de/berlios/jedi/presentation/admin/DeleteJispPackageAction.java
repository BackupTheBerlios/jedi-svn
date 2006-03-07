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

import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.logic.admin.AdminLogicService;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.JispIdManager;

/**
 * <p>
 * Deletes a saved JispPackage.<br>
 * JispPackage is identified by the id in the JispIdManager.<br>
 * JispPackage is also removed from the JispPackagesList and the JispIdManager
 * in session.
 * </p>
 * <p>
 * A forward is made to "success".
 * </p>
 */
public class DeleteJispPackageAction extends ActionWithErrorSupport {

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

		JispPackagesList jispPackagesList = (JispPackagesList) request
				.getSession().getAttribute(AdminKeys.JISP_PACKAGES_LIST_KEY);
		JispIdManager jispIdManager = (JispIdManager) request.getSession()
				.getAttribute(AdminKeys.JISP_ID_MANAGER_KEY);
		String jispPackageId = request.getParameter("jispPackageId");

		if (jispPackageId == null) {
			return errorForward(mapping, request, new ActionMessage("missedId",
					"jispPackageId"), "adminMissedId");
		}

		JispPackage jispPackage = jispIdManager.getJispPackage(jispPackageId);

		if (jispPackage == null) {
			return errorForward(mapping, request, new ActionMessage(
					"invalidId", "jispPackageId"), "adminInvalidId");
		}

		try {
			deleteJispPackage(jispPackage, jispIdManager, jispPackagesList);
		} catch (DataException e) {
			LogFactory.getLog(DeleteJispPackageAction.class).error(
					"JispPackage couldn't be deleted", e);
			return errorForward(mapping, request, new ActionMessage(
					"failedJispPackageDeleting"),
					"adminFailedJispPackageDeleting");
		}

		return mapping.findForward("success");
	}

	/**
	 * Deletes the specified JispPackage.<br>
	 * It also deregisters it from the JispIdManager being used and from the
	 * JispPackagesList. Those actions aren't taken if a problem occurs when
	 * deleting the JispPackage.
	 * 
	 * @param jispPackage
	 *            The JispPackage to delete.
	 * @param jispIdManager
	 *            The JispIdManager to deregister the JispPackage from.
	 * @param jispPackagesList
	 *            The JispPackagesList to remove the JispPackage from.
	 * 
	 * @throws DataException
	 *             If an exception occured when deleting the JispPackage.
	 */
	private void deleteJispPackage(JispPackage jispPackage,
			JispIdManager jispIdManager, JispPackagesList jispPackagesList)
			throws DataException {
		new AdminLogicService().deleteJispPackage(jispPackage);
		jispIdManager.deregisterJispPackage(jispPackage);
		jispPackagesList.removeJispPackage(jispPackage);
	}
}
