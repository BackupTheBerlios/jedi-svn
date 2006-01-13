/*
 * Copyright 2005-2006 Daniel Calvi�o S�nchez
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

package de.berlios.jedi.presentation.admin.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;

import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import de.berlios.jedi.presentation.JispIdManager;
import de.berlios.jedi.presentation.admin.AdminKeys;
import de.berlios.jedi.presentation.view.JispPackageToXml;
import de.berlios.jedi.presentation.view.ViewAction;

/**
 * <p>
 * Saves to the request the XML representation of the JispPackagesList.<br>
 * Each Jisp Package contains its id, the Jisp Metadata and all its Jisp Icons.<br>
 * The Jisp Metadata contains the name, version, description, all its
 * JispAuthors, creation date and home.<br>
 * Each JispAuthor contains its name and jid.<br>
 * Each Jisp Icon contains its id and all its Jisp Icons and Jisp Texts.<br>
 * Each Jisp Object contains its id, its content type and the url where it can
 * be got.<br>
 * Each Jisp Text contains its id and the text.
 * </p>
 * 
 * <p>
 * Forward is made to "adminView".
 * </p>
 * 
 * TODO: add reference to generated XML format
 */
public class AdminViewAction extends ViewAction {

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

		JispPackageToXml jispPackageToXml = new JispPackageToXml(jispIdManager);

		Document document = getDocument("jedi", request);

		Element adminElement = new Element("admin");
		document.getRootElement().addContent(adminElement);

		adminElement.addContent(jispPackageToXml.createJispPackagesListElement(
				jispPackagesList, true));

		return mapping.findForward("adminView");
	}
}
