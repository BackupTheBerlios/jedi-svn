/**
 * The url of the Action to invoke to get the JispObjects.
 */
var jispObjectsViewerRequest;

/**
 * The event handler for the list's select event.
 */
var jispObjectsContentsOnSelectDo;



/**
 * Inits the viewer reloading the list of the JispObjects.
 */
function jispObjectsViewerInit() {
	reloadJispObjectsView();
}

/**
 * Reloads the list of the JispObjects.
 * It removes the previous list, and creates a new one containing the objects get from invoking
 * the request url specified in the global variable.
 * The new list created uses the global variable var jispObjectsContentsOnSelectDo as the event
 * handler for select event.
 */
function reloadJispObjectsView() {
	removeJispObjectsOldContents();
	
	try {
		request = new XMLHttpRequest();
		request.open("GET", jispObjectsViewerRequest, false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send('');
		
		var response = request.responseXML;
		
		createJispObjectContents(response);
	} catch (e) {
		alert("Jisp objects list error: " +e);
	}
}

/**
 * Removes the JispObjects' list and adds a new one.
 * The list uses the global variable var jispObjectsContentsOnSelectDo as the event
 * handler for select event.
 */
function removeJispObjectsOldContents() {
	var jispObjectsViewer = document.getElementById("jispObjectsViewer");
	jispObjectsViewer.removeChild(document.getElementById("jispObjectsContents"));
	
	var jispObjectsContents = document.createElement("listbox");
	jispObjectsContents.setAttribute("id", "jispObjectsContents");
	jispObjectsContents.setAttribute("flex", "1");
	jispObjectsContents.setAttribute("seltype", "multiple");
	jispObjectsContents.setAttribute("onselect", jispObjectsContentsOnSelectDo);
	
	jispObjectsViewer.appendChild(jispObjectsContents);
}

/**
 * Creates the contents of the JispObjects' list based on the response get from the invoked Action.
 *
 * @param response The response get from the invoked Action.
 */
function createJispObjectContents(response) {
	var jispObjectsContents = document.getElementById("jispObjectsContents");

	var jispObjects = response.getElementsByTagName("jispObject");

	var i;
	for (i=0; i<jispObjects.length; i++) {
		jispObjectsContents.appendChild(createJispObjectElement(jispObjects[i]));
	}
}

/**
 * Creates a new JispObjects' list child from a JispObject element get from the response.
 *
 * @param jispObject The jispObject element to create its representation.
 * @return The list item containing the JispObject representation.
 */
function createJispObjectElement(jispObject) {
	var jispObjectElement = document.createElement("listitem");
	jispObjectElement.setAttribute("label", "Object");
	jispObjectElement.setAttribute("value", jispObject.getAttribute("id"));
	
	
	var jispObjectElementChildren = document.createElement("vbox");
	jispObjectElement.appendChild(jispObjectElementChildren);

	var jispObjectElementHbox = document.createElement("hbox");
	jispObjectElementChildren.appendChild(jispObjectElementHbox);
	
	if (jispObject.getElementsByTagName("contentType")[0].childNodes[0].nodeValue.search(/image/i) == 0) {
		var jispObjectElementImage = document.createElement("image");
		jispObjectElementImage.setAttribute("src", prefix + jispObject.getElementsByTagName("src")[0].childNodes[0].nodeValue);
	
		jispObjectElementHbox.appendChild(jispObjectElementImage);
	} else {
		var jispObjectElementLabel = document.createElement("label");
		jispObjectElementLabel.setAttribute("value", "Object (" + jispObject.getElementsByTagName("contentType")[0].childNodes[0].nodeValue + ")");
	
		jispObjectElementHbox.appendChild(jispObjectElementLabel);
	}

	return jispObjectElement;
}