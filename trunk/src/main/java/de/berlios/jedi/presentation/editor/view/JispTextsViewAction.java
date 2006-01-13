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

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;


import de.berlios.jedi.common.entity.jisp.JispIcon;
import de.berlios.jedi.common.entity.jisp.JispText;
import de.berlios.jedi.presentation.JispIdManager;
import de.berlios.jedi.presentation.Keys;
import de.berlios.jedi.presentation.editor.EditorKeys;
import de.berlios.jedi.presentation.view.JispPackageToXml;
import de.berlios.jedi.presentation.view.ViewAction;

/**
 * <p>
 * Saves to the request the XML representation of the list with all the Jisp
 * Texts from the specified Jisp Icon.<br>
 * Each Jisp Text contains its id and the text.
 * </p>
 * 
 * <p>
 * If no Jisp Icon has been specified, it doesn't save nothing.
 * </p>
 * 
 * <p>
 * NEXT_FORWARD_NAME Key attribute is set to "jispTextsView".
 * </p>
 * 
 * TODO: add reference to generated XML format
 */
public class JispTextsViewAction extends ViewAction {

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

		JispIcon jispIcon = null;

		if (jispIconId != null) {
			jispIcon = jispIdManager.getJispIcon(jispIconId);
		}

		if (jispIcon != null) {
			JispPackageToXml jispPackageToXml = new JispPackageToXml(
					jispIdManager);

			Document document = getDocument("jedi", request);
			Element jispTextsElement = new Element("jispTexts");

			document.getRootElement().addContent(jispTextsElement);

			Iterator jispTexts = jispIcon.jispTextsIterator();
			while (jispTexts.hasNext()) {
				JispText jispText = (JispText) jispTexts.next();
				jispTextsElement.addContent(jispPackageToXml
						.createJispTextElement(jispText));
			}
		}

		request.setAttribute(Keys.NEXT_FORWARD_NAME, "jispTextsView");

		return mapping.findForward(Keys.ADD_STATUS_FORWARD_NAME);
	}
}
