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

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import de.berlios.jedi.common.config.ConfigurationFactory;
import de.berlios.jedi.common.entity.jisp.JispAuthor;
import de.berlios.jedi.common.entity.jisp.JispFile;
import de.berlios.jedi.common.entity.jisp.JispMetadata;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;
import de.berlios.jedi.logic.editor.EditorLogicService;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Sends the Jisp File for the Jisp Package being created in response's output
 * stream.<br>
 * Response content type is set to "application/vnd.jisp" and header
 * Content-disposition to "attachment; filename=\"package.jisp\""
 * </p>
 * <p>
 * Package metadata is created in a predefined way (the user can't specify any
 * field):
 * <ul>
 * <li> name: Jisp Editor Directly on Internet </li>
 * <li> version: 1.0 </li>
 * <li> description: made with JEDI </li>
 * <li> creation: date when Jisp Package is being downloaded </li>
 * <li> author: Unknown </li>
 * </ul>
 * </p>
 * <p>
 * If something went wrong, a forward to ADD_STATUS_FORWARD_NAME Key is made and
 * the NEXT_FORWARD_NAME Key is set to the error forward name.
 * </p>
 */
public class DownloadPackageAction extends ActionWithErrorSupport {

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

		setJispMetadata(jispPackage);

		JispFile jispFile = null;
		try {
			jispFile = new EditorLogicService()
					.getJispFileFromJispPackage(jispPackage);
		} catch (UnsupportedOperationException e) {
			LogFactory.getLog(DownloadPackageAction.class).fatal(
					"UTF-8 encoding not supported!!!", e);

			return errorForward(mapping, request, new ActionMessage(
					"utf8NotSupported"), "utf8NotSupported",
					Keys.ADD_STATUS_FORWARD_NAME);
		} catch (JispIncompletePackageException e) {
			LogFactory.getLog(DownloadPackageAction.class).error(
					"Incomplete JispPackage", e);

			return errorForward(mapping, request, new ActionMessage(
					"incompleteJispPackage", e.getMessage()),
					"incompleteJispPackage", Keys.ADD_STATUS_FORWARD_NAME);
		}

		response.setContentType("application/vnd.jisp");
		response.setHeader("Content-disposition",
				"attachment; filename=\"package.jisp\"");

		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(jispFile.getData());
		} catch (IOException e) {
			LogFactory.getLog(DownloadPackageAction.class).error(
					"IOException when writing the JispFile to the "
							+ "ServlerOutputStream", e);

			return errorForward(mapping, request, new ActionMessage(
					"failedJispFileWriteToOutputStream"),
					"failedJispFileWriteToOutputStream",
					Keys.ADD_STATUS_FORWARD_NAME);
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

	/**
	 * Sets the Jisp Metadata for the Jisp Package being downloaded.<br>
	 * 
	 * Package metadata is set in a predefined way (the user can't specify any
	 * field):
	 * <ul>
	 * <li> name: Jisp Editor Directly on Internet </li>
	 * <li> version: 1.0 </li>
	 * <li> description: made with JEDI </li>
	 * <li> creation: date when Jisp Package is being downloaded </li>
	 * <li> author: Those of the Jisp Elements being used in the package </li>
	 * </ul>
	 * 
	 * @param jispPackage
	 *            The JispPackage to set its JispMetadata.
	 */
	private void setJispMetadata(JispPackage jispPackage) {
		JispMetadata jispMetadata = jispPackage.getJispMetadata();

		if (jispMetadata == null) {
			jispMetadata = new JispMetadata();
			jispPackage.setJispMetadata(jispMetadata);
		}

		jispMetadata.setName("Jisp Editor Directly on Internet");
		jispMetadata.setVersion("1.0");
		jispMetadata.setDescription(ConfigurationFactory.getConfiguration()
				.getString("editor.metadata.description"));
		jispMetadata.setCreation(new Date());
		// TODO: authors from used Jisp Packages should be added to authors list
		Iterator authors = jispMetadata.jispAuthorsIterator();
		if (!authors.hasNext()) {
			JispAuthor jispAuthor = new JispAuthor();
			jispAuthor.setName("J.E.D.I.");
			jispMetadata.addJispAuthor(jispAuthor);			
		}
		jispMetadata.setHome(ConfigurationFactory.getConfiguration()
				.getString("editor.metadata.home"));
	}
}
