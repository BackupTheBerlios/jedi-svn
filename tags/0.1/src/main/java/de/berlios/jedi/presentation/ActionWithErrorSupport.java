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

package de.berlios.jedi.presentation;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * Base class to all Actions which need to report an error.<br>
 * Provides a method to forward to the error page, also saving the ActionMessage
 * with the error information to be displayed and the error identification.<br>
 * The forward to the error page can also be made with a previous forward to
 * another Action prior the error page.
 */
public abstract class ActionWithErrorSupport extends Action {

	/**
	 * Saves the ActionMessage and the errorId to the request and returns the
	 * forward to the ErrorActionView.<br>
	 * The ActionMessage contains the key with the error information to be
	 * looked for in the application resources, and also the parameters to this
	 * resource if needed. The errorId is the forward used in the error Action.
	 * 
	 * @param mapping
	 *            The ActionMapping to use.
	 * @param request
	 *            The request to save the elements to.
	 * @param actionMessage
	 *            The ActionMessage with the error message to save.
	 * @param errorId
	 *            The error id to forward to in the error Action.
	 * @return The forward to the error Action.
	 */
	protected ActionForward errorForward(ActionMapping mapping,
			HttpServletRequest request, ActionMessage actionMessage,
			String errorId) {
		ActionMessages errors = new ActionMessages();
		errors.add("error", actionMessage);

		saveErrors(request, errors);

		request.setAttribute("errorId", errorId);

		return mapping.findForward("error");
	}

	/**
	 * Saves the ActionMessage, the errorId and the name of the forward to the
	 * ErrorAction in the request and returns the forward to the
	 * firstForwardName.<br>
	 * The ActionMessage contains the key with the error information to be
	 * looked for in the application resources, and also the parameters to this
	 * resource if needed. The errorId is the forward used in the error Action.<br>
	 * The name of the error forward is saved under NEXT_FORWARD_NAME Key.
	 * 
	 * @param mapping
	 *            The ActionMapping to use.
	 * @param request
	 *            The request to save the elements to.
	 * @param actionMessage
	 *            The ActionMessage with the error message to save.
	 * @param errorId
	 *            The error id to forward to in the error Action.
	 * @param firstForwardName
	 *            The name of the forward to go before the error page.
	 * @return The forward to the error Action.
	 */
	protected ActionForward errorForward(ActionMapping mapping,
			HttpServletRequest request, ActionMessage actionMessage,
			String errorId, String firstForwardName) {
		ActionForward actionForward = errorForward(mapping, request,
				actionMessage, errorId);
		request.setAttribute(Keys.NEXT_FORWARD_NAME, actionForward.getName());

		return mapping.findForward(firstForwardName);
	}
}
