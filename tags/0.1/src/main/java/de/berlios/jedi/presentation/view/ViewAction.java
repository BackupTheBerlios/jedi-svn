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

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;

import com.oroad.stxx.action.Action;

/**
 * Base class to all Actions which work with XML Documents.<br>
 * It extends stxx Action and provides a method to get a saved document with a
 * specified named root element. This allows Actions to use the same JDOM
 * Document, no matter where the Action is in the Actions chain.
 */
public abstract class ViewAction extends Action {

	/**
	 * <p>
	 * Gets the JDOM Document used by stxx from the request which root element
	 * has the specified name.<br>
	 * If more than one document have the specified root element, the first
	 * occurrence is returned. If no document which this root element has been
	 * specified, a new one is created and saved.
	 * </p>
	 * 
	 * <p>
	 * This method implementation depends on stxx implementation of Actions (and
	 * this should be fixed).<br>
	 * stxx stores XML documents saved in each Action in an ArrayList, and then
	 * it composes the final XML based on those documents.
	 * </p>
	 * 
	 * @param rootElementName
	 *            The name of the root element from the document to get.
	 * @param request
	 *            The request.
	 * @return The JDOM document.
	 */
	protected Document getDocument(String rootElementName,
			HttpServletRequest request) {
		// TODO: should be made as stxx implementation independent as possible.
		// Should xslt be made accepting more than 1 jedi element? Or deleting
		// jedi element, not attaching stxx request elements and adding all jedi
		// children directly on stxx element?

		Document document = null;

		if (request.getAttribute(Action.DOCUMENT_KEY) != null) {

			Iterator documentsIterator = ((List) request
					.getAttribute(Action.DOCUMENT_KEY)).iterator();

			while (documentsIterator.hasNext() && document == null) {
				Document actualDocument = (Document) documentsIterator.next();

				if (actualDocument.getRootElement().getName().equals(
						rootElementName)) {
					document = actualDocument;
				}
			}
		}

		if (document == null) {
			document = new Document(new Element(rootElementName));
			saveDocument(request, document);
		}

		return document;
	}
}
