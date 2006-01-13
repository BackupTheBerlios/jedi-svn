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

package de.berlios.jedi.presentation.editor.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;


import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.presentation.JispIdManager;
import de.berlios.jedi.presentation.Keys;
import de.berlios.jedi.presentation.editor.EditorKeys;
import de.berlios.jedi.presentation.view.JispPackageToXml;
import de.berlios.jedi.presentation.view.ViewAction;

/**
 * <p>
 * Saves to the request the XML representation of the Jisp Package being
 * created.<br>
 * The Jisp Package contains its id and all its Jisp Icons.<br>
 * Each Jisp Icon contains its id and all its Jisp Icons and Jisp Texts.<br>
 * Each Jisp Object contains its id, its content type and the url where it can
 * be got.<br>
 * Each Jisp Text contains its id and the text.
 * </p>
 * 
 * <p>
 * NEXT_FORWARD_NAME Key attribute is set to "jispPackageView".
 * </p>
 * 
 * TODO: add reference to generated XML format
 */
public class JispPackageViewAction extends ViewAction {

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

		Document document = getDocument("jedi", request);
		document.getRootElement().addContent(
				new JispPackageToXml(jispIdManager)
						.createJispPackageElement(jispPackage, false));

		request.setAttribute(Keys.NEXT_FORWARD_NAME, "jispPackageView");

		return mapping.findForward(Keys.ADD_STATUS_FORWARD_NAME);
	}
}
