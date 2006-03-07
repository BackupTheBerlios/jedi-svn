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
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import de.berlios.jedi.common.entity.jisp.JispIcon;
import de.berlios.jedi.common.entity.jisp.JispMetadata;
import de.berlios.jedi.common.entity.jisp.JispObject;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import de.berlios.jedi.common.jisp.JispUtil;
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
 * Package metadata is set in a predefined way (the user can't specify any
 * field):
 * <ul>
 * <li> name: JEDI package </li>
 * <li> version: 1.0 </li>
 * <li> description: name, version and home (if any) of the JispPackages used in
 * the package, and the default description got from the configuration </li>
 * <li> creation: date when the JispPackage is being downloaded </li>
 * <li> authors: those of the JispPackages used in the package </li>
 * <li> home: got from the configuration </li>
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

		JispPackagesList jispPackagesList = (JispPackagesList) request
				.getSession().getAttribute(EditorKeys.JISP_PACKAGES_LIST_KEY);
		JispPackage jispPackage = (JispPackage) request.getSession()
				.getAttribute(EditorKeys.JISP_PACKAGE);

		setJispMetadata(jispPackage, jispPackagesList);

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
		// Filename should be equal to the name of the root directory of the
		// jisp file
		response
				.setHeader("Content-disposition", "attachment; filename=\""
						+ JispUtil.getDefaultRootDirectoryName(jispPackage)
						+ ".jisp\"");

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
	 * Package metadata is set in a predefined way (the user can't specify any
	 * field):
	 * <ul>
	 * <li> name: JEDI package </li>
	 * <li> version: 1.0 </li>
	 * <li> description: name, version and home (if any) of the JispPackages
	 * used in the package, and the default description got from the
	 * configuration </li>
	 * <li> creation: date when the JispPackage is being downloaded </li>
	 * <li> authors: those of the JispPackages used in the package </li>
	 * <li> home: got from the configuration </li>
	 * </ul>
	 * 
	 * @param jispPackage
	 *            The JispPackage to set its JispMetadata.
	 * @param jispPackagesList
	 */
	private void setJispMetadata(JispPackage jispPackage,
			JispPackagesList jispPackagesList) {
		Map jispPackagesMap = new Hashtable();
		JispUtil.indexJispPackagesByDefaultRootDirectoryName(jispPackagesMap,
				jispPackagesList);
		List usedJispPackagesList = getUsedJispPackagesFromMap(jispPackagesMap,
				jispPackage);

		JispMetadata jispMetadata = getJispMetadata(jispPackage);

		jispMetadata.setName("JEDI package");
		jispMetadata.setVersion("1.0");
		jispMetadata.setCreation(new Date());
		jispMetadata.setHome(ConfigurationFactory.getConfiguration().getString(
				"editor.metadata.home"));

		setDescription(jispMetadata, usedJispPackagesList.iterator());
		setJispAuthors(jispMetadata, usedJispPackagesList.iterator());
	}

	/**
	 * Returns the JispMetadata from a JispPackage.<br>
	 * If the JispPackage doesn't have any JispMetadata, a new one is created,
	 * added to the JispPackage and returned.
	 * 
	 * @param jispPackage
	 *            The JispPackage to get the JispMetadata from.
	 * @return The JispMetadata from the JispPackage.
	 */
	private JispMetadata getJispMetadata(JispPackage jispPackage) {
		JispMetadata jispMetadata = jispPackage.getJispMetadata();

		if (jispMetadata == null) {
			jispMetadata = new JispMetadata();
			jispPackage.setJispMetadata(jispMetadata);
		}

		return jispMetadata;
	}

	/**
	 * Returns a list containing only the JispPackages from the JispPackagesMap
	 * used in the JispPackage.<br>
	 * All the JispObjects of the JispPackage are checked to add the JispPackage
	 * they come from. To get the JispPackage used for a JispObject, the
	 * JispPackage saved using the root directory of the JispObject as a key is
	 * get from the map.
	 * 
	 * @param jispPackagesMap
	 *            The Map where all the JispPackages are saved.
	 * @param jispPackage
	 *            The JispPackage to get the JispPackages used in it.
	 * @return A list containing only the JispPackages used in the JispPackage.
	 */
	private List getUsedJispPackagesFromMap(Map jispPackagesMap,
			JispPackage jispPackage) {
		List usedJispPackagesList = new ArrayList();

		Iterator jispIconsIterator = jispPackage.jispIconsIterator();
		while (jispIconsIterator.hasNext()) {
			JispIcon jispIcon = (JispIcon) jispIconsIterator.next();

			Iterator jispObjectsIterator = jispIcon.jispObjectsIterator();
			while (jispObjectsIterator.hasNext()) {
				JispObject jispObject = (JispObject) jispObjectsIterator.next();
				String jispObjectName = jispObject.getName();
				String rootDirectory = jispObjectName.substring(0,
						jispObjectName.indexOf("/"));

				if (!usedJispPackagesList.contains(jispPackagesMap
						.get(rootDirectory))) {
					usedJispPackagesList
							.add(jispPackagesMap.get(rootDirectory));
				}
			}
		}

		return usedJispPackagesList;
	}

	/**
	 * Sets the information of the JispPackages in the JispPackagesIterator to
	 * the JispMetadata.<br>
	 * The information for every package is the name, version and homepage (if
	 * any). After adding the information of all the packages, the default
	 * description get from the configuration is added.
	 * 
	 * @param jispMetadata
	 *            The JispMetadata to add the description to.
	 * @param jispPackagesIterator
	 *            The iterator with the JispPackages which information add.
	 */
	private void setDescription(JispMetadata jispMetadata,
			Iterator jispPackagesIterator) {
		StringBuffer description = new StringBuffer();

		description.append("Created using J.E.D.I. from those packages:\n");

		while (jispPackagesIterator.hasNext()) {
			JispMetadata jispMetadataToAdd = ((JispPackage) jispPackagesIterator
					.next()).getJispMetadata();

			description.append(jispMetadataToAdd.getName());
			description.append(" " + jispMetadataToAdd.getVersion());
			String home = jispMetadataToAdd.getHome();
			if (home != null && !home.equals("")) {
				description.append(", Homepage: " + home);
			}
			description.append("\n");
		}

		description.append("\n"
				+ ConfigurationFactory.getConfiguration().getString(
						"editor.metadata.description"));

		jispMetadata.setDescription(description.toString());
	}

	/**
	 * Sets the JispAuthors of the JispPackages in the JispPackagesIterator to
	 * the JispMetadata.<br>
	 * The JispAuthors added are a clone of the JispAuthors in the JispPackages.
	 * The name of the JispAuthors is the name of the JispAuthor plus the name
	 * of the JispPackage they belong to.
	 * 
	 * @param jispMetadata
	 *            The JispMetadata to add the JispAuthors to.
	 * @param jispPackagesIterator
	 *            The iterator with the JispPackages which JispAuthors add.
	 */
	private void setJispAuthors(JispMetadata jispMetadata,
			Iterator jispPackagesIterator) {
		jispMetadata.clearJispAuthors();

		JispAuthor jispAuthorJedi = new JispAuthor();
		jispAuthorJedi.setName("J.E.D.I.");
		jispMetadata.addJispAuthor(jispAuthorJedi);

		while (jispPackagesIterator.hasNext()) {
			JispMetadata jispMetadataToAdd = ((JispPackage) jispPackagesIterator
					.next()).getJispMetadata();

			addJispAuthorsFromJispMetadata(jispMetadata, jispMetadataToAdd);
		}
	}

	/**
	 * Adds the JispAuthors from JispMetadataToAdd to the JispMetadata.<br>
	 * The added JispAuthor are a clone of the ones contained in
	 * JispMetadataToAdd. The name of the JispAuthors added is the name of the
	 * JispAuthor plus the name of the JispPackage they belong to.
	 * 
	 * @param jispMetadata
	 *            The JispMetadata to add the JispAuthors to.
	 * @param jispMetadataToAdd
	 *            The JispMetadata to add the JispAuthors from.
	 */
	private void addJispAuthorsFromJispMetadata(JispMetadata jispMetadata,
			JispMetadata jispMetadataToAdd) {
		Iterator jispAuthorsIterator = jispMetadataToAdd.jispAuthorsIterator();
		while (jispAuthorsIterator.hasNext()) {
			JispAuthor jispAuthorToAdd = (JispAuthor) ((JispAuthor) jispAuthorsIterator
					.next()).clone();

			jispAuthorToAdd.setName(jispAuthorToAdd.getName() + " - "
					+ jispMetadataToAdd.getName());

			if (!jispMetadata.containsJispAuthor(jispAuthorToAdd)) {
				jispMetadata.addJispAuthor(jispAuthorToAdd);
			}
		}
	}
}