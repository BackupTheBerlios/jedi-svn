/**
 * The url of the Action to invoke to get the JispTexts.
 */
var jispTextsViewerRequest;

/**
 * The event handler for the list's select event.
 */
var jispTextsContentsOnSelectDo;



/**
 * Inits the viewer reloading the list of the JispTexts.
 */
function jispTextsViewerInit() {
	reloadJispTextsView();
}

/**
 * Reloads the list of the JispTexts.
 * It removes the previous list, and creates a new one containing the texts get from invoking
 * the request url specified in the global variable.
 * The new list created uses the global variable var jispTextsContentsOnSelectDo as the event
 * handler for select event.
 */
function reloadJispTextsView() {
	removeJispTextsOldContents();
	
	try {
		request = new XMLHttpRequest();
		request.open("GET", jispTextsViewerRequest, false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send('');
		
		var response = request.responseXML;
		
		createJispTextContents(response);
	} catch (e) {
		alert("Jisp texts list error: " +e);
	}
}

/**
 * Removes the JispTexts' list and adds a new one.
 * The list uses the global variable var jispTextsContentsOnSelectDo as the event
 * handler for select event.
 */
function removeJispTextsOldContents() {
	var jispTextsViewer = document.getElementById("jispTextsViewer");
	jispTextsViewer.removeChild(document.getElementById("jispTextsContents"));
	
	var jispTextsContents = document.createElement("listbox");
	jispTextsContents.setAttribute("id", "jispTextsContents");
	jispTextsContents.setAttribute("flex", "1");
	jispTextsContents.setAttribute("seltype", "multiple");
	jispTextsContents.setAttribute("onselect", jispTextsContentsOnSelectDo);
	
	jispTextsViewer.appendChild(jispTextsContents);
}

/**
 * Creates the contents of the JispTexts' list based on the response get from the invoked Action.
 *
 * @param response The response get from the invoked Action.
 */
function createJispTextContents(response) {
	var jispTextsContents = document.getElementById("jispTextsContents");

	var jispTexts = response.getElementsByTagName("jispText");

	var i;
	for (i=0; i<jispTexts.length; i++) {
		jispTextsContents.appendChild(createJispTextElement(jispTexts[i]));
	}
}

/**
 * Creates a new JispText's list child from a JispText element get from the response.
 *
 * @param jispTextt The jispText element to create its representation.
 * @return The list item containing the JispText representation.
 */
function createJispTextElement(jispText) {
	var jispTextElement = document.createElement("listitem");
	jispTextElement.setAttribute("label", "Text");
	jispTextElement.setAttribute("value", jispText.getAttribute("id"));
	
	
	var jispTextElementChildren = document.createElement("vbox");
	jispTextElement.appendChild(jispTextElementChildren);

	var jispTextElementHbox = document.createElement("hbox");
	jispTextElementChildren.appendChild(jispTextElementHbox);
	
	var jispTextElementLabel = document.createElement("label");
	jispTextElementLabel.setAttribute("value", jispText.getElementsByTagName("text")[0].childNodes[0].nodeValue);

	jispTextElementHbox.appendChild(jispTextElementLabel);
	
	return jispTextElement;
}