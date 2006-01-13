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

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import de.berlios.jedi.common.entity.jisp.JispObject;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.JispIdManager;

/**
 * <p>
 * Sends the specified Jisp Object in response's output stream.<br>
 * Response content type is set to Jisp Object's mimetype.
 * </p>
 */
public class GetJispObjectAction extends ActionWithErrorSupport {

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
				.getAttribute(AdminKeys.JISP_ID_MANAGER_KEY);

		String jispObjectId = request.getParameter("jispObjectId");

		if (jispObjectId == null) {
			return errorForward(mapping, request, new ActionMessage("missedId",
					"jispObjectId"), "adminMissedId");
		}

		JispObject jispObject = jispIdManager.getJispObject(jispObjectId);

		if (jispObject == null) {
			return errorForward(mapping, request, new ActionMessage(
					"invalidId", "jispObjectId"), "adminInvalidId");
		}

		response.setContentType(jispObject.getMimeType());

		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(jispObject.getData());
		} catch (IOException e) {
			LogFactory.getLog(GetJispObjectAction.class).error(
					"IOException when writing the JispObject to the"
							+ " ServlerOutputStream", e);

			return errorForward(mapping, request, new ActionMessage(
					"failedJispObjectWriteToOutputStream"),
					"adminFailedJispObjectWriteToOutputStream");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return null;
	}
}
