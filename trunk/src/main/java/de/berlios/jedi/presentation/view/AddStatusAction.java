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

package de.berlios.jedi.presentation.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;

import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Adds the status to the request document which root element is named "jedi".<br>
 * Status tells if a jisp package is being created.
 * </p>
 * 
 * <p>
 * Forward is made to the NEXT_FORWARD_NAME Key attribute saved in the
 * request.
 * </p>
 * 
 * TODO: add reference to generated XML format
 */
public class AddStatusAction extends ViewAction {

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

		Document document = getDocument("jedi", request);

		Element statusElement = new Element("status");
		statusElement.addContent(createStatusElement(request, "package",
				"jispPackage"));

		document.getRootElement().addContent(statusElement);

		return mapping.findForward((String) request
				.getAttribute(Keys.NEXT_FORWARD_NAME));
	}

	/**
	 * Returns a new status element based on the existance of a request
	 * attribute.<br>
	 * The status element has the specified name, and the text is "true" if the
	 * requestAttribute exists, "false" otherwise.
	 * 
	 * @param request
	 *            The request.
	 * @param elementName
	 *            The name of the status element.
	 * @param requestAttribute
	 *            The id of the request attribute to check.
	 * @return The status element.
	 */
	private Element createStatusElement(HttpServletRequest request,
			String elementName, String requestAttribute) {
		Element statusElement = new Element(elementName);

		if (request.getSession().getAttribute(requestAttribute) != null) {
			statusElement.setText("true");
		} else {
			statusElement.setText("false");
		}

		return statusElement;
	}
}
