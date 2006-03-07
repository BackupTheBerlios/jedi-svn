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
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import de.berlios.jedi.common.entity.jisp.JispFile;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.logic.admin.AdminLogicService;
import de.berlios.jedi.presentation.ActionWithErrorSupport;
import de.berlios.jedi.presentation.Keys;

/**
 * <p>
 * Saves the JispFile in the persistent storing.<br>
 * The JipFile uploaded by the user is get from the UploadJispFileForm and
 * saved.
 * <p>
 * <p>
 * A forward is made to success, and the NEXT_FORWARD_NAME AdminKey is set to
 * "adminView".
 * </p>
 */
public class UploadJispFileAction extends ActionWithErrorSupport {

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

		if (!(form instanceof UploadJispFileForm)) {
			return errorForward(mapping, request, new ActionMessage(
					"invalidUploadForm"), "adminInvalidUploadForm");
		}

		UploadJispFileForm uploadJispFileForm = (UploadJispFileForm) form;

		// retrieve the file representation
		FormFile file = uploadJispFileForm.getFile();

		if (file == null) {
			return errorForward(mapping, request, new ActionMessage(
					"missedUploadFile"), "adminMissedUploadFile");
		}

		byte[] data = null;

		// retrieve the file data
		try {
			data = getDataFromFile(file);
		} catch (IOException e) {
			LogFactory.getLog(UploadJispFileAction.class).error(
					"IOException when getting the data from the uploaded file",
					e);
			return errorForward(mapping, request, new ActionMessage(
					"failedFileUploading"), "adminFailedFileUploading");
		} finally {
			file.destroy();
		}

		try {
			new AdminLogicService().saveJispFile(new JispFile(data));
		} catch (JispInvalidIcondefException e) {
			LogFactory.getLog(UploadJispFileAction.class).error(
					"The icondef.xml in the JispFile isn't valid", e);
			return errorForward(mapping, request, new ActionMessage(
					"invalidIcondef", e.getMessage()), "adminInvalidIcondef");
		} catch (JispStoredException e) {
			LogFactory.getLog(UploadJispFileAction.class).error(
					"The JispFile isn't valid", e);
			return errorForward(mapping, request, new ActionMessage(
					"invalidJispFile", e.getMessage()), "adminInvalidJispFile");
		} catch (DataException e) {
			LogFactory.getLog(UploadJispFileAction.class).error(
					"Error saving the JispFile", e);
			return errorForward(mapping, request, new ActionMessage(
					"failedJispFileSaving"), "adminFailedJispFileSaving");
		}

		request.setAttribute(Keys.NEXT_FORWARD_NAME, "adminView");

		return mapping.findForward("success");
	}

	/**
	 * Returns the data from the file as a byte[].
	 * 
	 * @param file
	 *            The FormFile with the uploaded file.
	 * @return The data get from the file.
	 * @throws IOException
	 *             If an I/O exception occured when getting the data.
	 */
	private byte[] getDataFromFile(FormFile file) throws IOException {
		byte[] data;
		InputStream in = null;
		try {
			in = file.getInputStream();

			int dataLength = in.available();
			data = new byte[dataLength];
			int dataReaded = 0;

			while ((dataLength - dataReaded) > 0) {
				dataReaded += in
						.read(data, dataReaded, dataLength - dataReaded);
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return data;
	}
}
