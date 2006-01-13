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

/**
 * <p>
 * Saves to the request a notification that an error occured.<br>
 * Error information is stored in an ActionMessages object where the error
 * happens. The information stxx saves as XML to the request includes those
 * ActionMessages.
 * </p>
 * 
 * <p>
 * Forward is made to the errorId attribute saved in the request. It must be
 * saved where the error occurs.
 * </p>
 * 
 * TODO: add reference to generated XML format
 */
public class ErrorViewAction extends ViewAction {

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

		Element errorElement = new Element("error");

		document.getRootElement().addContent(errorElement);

		return mapping.findForward((String) request.getAttribute("errorId"));
	}
}
