/**
 * The url of the Action to invoke to get the JispPackages.
 */
var jispPackagesViewerRequest;

/**
 * The event handler for the list's select event.
 */
var jispPackagesContentsOnSelectDo;



/**
 * Inits the viewer reloading the list of the JispPackages.
 */
function jispPackagesViewerInit() {
	reloadJispPackagesView();
}

/**
 * Reloads the list of the JispPackages.
 * It removes the previous list, and creates a new one containing the packages get from invoking
 * the request url specified in the global variable.
 * The new list created uses the global variable var jispPackagesContentsOnSelectDo as the event
 * handler for select event.
 */
function reloadJispPackagesView() {
	removeJispPackageOldContents();

	try {
		request = new XMLHttpRequest();
		request.open("GET", jispPackagesViewerRequest, false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send('');
		
		var response = request.responseXML;
		
		createJispPackageContents(response);
	} catch (e) {
		alert("Jisp packages list error: " +e);
	}
}

/**
 * Removes the JispPackages' list and adds a new one.
 * The list uses the global variable var jispPackagesContentsOnSelectDo as the event
 * handler for select event.
 */
function removeJispPackageOldContents() {
	var jispPackagesViewer = document.getElementById("jispPackagesViewer");
	jispPackagesViewer.removeChild(document.getElementById("jispPackagesContents"));

	var jispPackagesContents = document.createElement("listbox");
	jispPackagesContents.setAttribute("id", "jispPackagesContents");
	jispPackagesContents.setAttribute("flex", "1");
	jispPackagesContents.setAttribute("seltype", "multiple");
	jispPackagesContents.setAttribute("onselect", jispPackagesContentsOnSelectDo);
	
	jispPackagesViewer.appendChild(jispPackagesContents);
}

/**
 * Creates the contents of the JispPackages' list based on the response get from the invoked Action.
 *
 * @param response The response get from the invoked Action.
 */
function createJispPackageContents(response) {
	var jispPackagesContents = document.getElementById("jispPackagesContents");

	var jispPackages = response.getElementsByTagName("jispPackage");
	
	var i;
	for (i=0; i<jispPackages.length; i++) {
		createJispPackageElement(jispPackages[i], jispPackagesContents);
	}
}

/**
 * Creates the JispPackage representation from the element get from the response.
 * All the JispIcons in the JispPackage are added to the JispPackages' list.
 * Every JispIcon contains all its JispObjects and JispTexts.
 *
 * @param jispPackage The JispPackage to create its representation.
 * @param jispPackagesContents The list to add the items to.
 */
function createJispPackageElement(jispPackage, jispPackagesContents) {
	var jispPackageElement = document.createElement("listitem");
	jispPackageElement.setAttribute("label", "Package");
	jispPackageElement.setAttribute("value", jispPackage.getAttribute("id"));
	
	jispPackagesContents.appendChild(jispPackageElement);
	
	var jispIcons = jispPackage.getElementsByTagName("jispIcon");
	
	var i;
	for (i=0; i<jispIcons.length; i++) {
		jispPackagesContents.appendChild(createJispIconElement(jispIcons[i]));
	}
}

/**
 * Creates a new list item with all the JispObjects and JispTexts in the JispIcon get from the response.
 *
 * @param jispIcon The jispIcon element to create its representation.
 * @return The list item containing the JispIcon representation.
 */
function createJispIconElement(jispIcon) {
	var jispIconElement = document.createElement("listitem");
	jispIconElement.setAttribute("label", "Icon");
	jispIconElement.setAttribute("value", jispIcon.getAttribute("id"));
	
	var jispIconElementChildren = document.createElement("vbox");
	jispIconElement.appendChild(jispIconElementChildren);
	
	var jispObjectsElementChildren = document.createElement("hbox");
	//jispObjectsElementChildren.setAttribute("pack", "center");
	jispObjectsElementChildren.setAttribute("align", "center");
	jispIconElementChildren.appendChild(jispObjectsElementChildren);
	
	
	var jispObjects = jispIcon.getElementsByTagName("jispObject");
	var i;
	for (i=0; i<jispObjects.length; i++) {
		jispObjectsElementChildren.appendChild(createJispObjectElement(jispObjects[i]));
	}
	
	
	var jispTextsElementChildren = document.createElement("hbox");
	//jispObjectsElementChildren.setAttribute("pack", "center");
	jispObjectsElementChildren.setAttribute("align", "center");
	jispIconElementChildren.appendChild(jispTextsElementChildren);
	
	var jispTexts = jispIcon.getElementsByTagName("jispText");
	var i;
	for (i=0; i<jispTexts.length; i++) {
		jispTextsElementChildren.appendChild(createJispTextElement(jispTexts[i]));
	}
	
	
	return jispIconElement;
}

/**
 * Creates a new image containing the JispObject representation.
 *
 * @param jispObject The jispObject element to create its representation.
 * @return A label containing the JispObject representation.
 */
function createJispObjectElement(jispObject) {
	var jispObjectElement;
	if (jispObject.getElementsByTagName("contentType")[0].childNodes[0].nodeValue.search(/image/i) == 0) {
		jispObjectElement = document.createElement("image");
		jispObjectElement.setAttribute("src", prefix + jispObject.getElementsByTagName("src")[0].childNodes[0].nodeValue);
//		jispObjectElement.setAttribute("onerror", "jispObjectElement.setAttribute('class', 'error-icon');");
//		TODO onerror doesn't work
	} else {
		jispObjectElement = document.createElement("label");
		jispObjectElement.setAttribute("value", "Object (" + jispObject.getElementsByTagName("contentType")[0].childNodes[0].nodeValue + ")");
	}
	
	return jispObjectElement;
}

/**
 * Creates a new label containing the JispText representation.
 *
 * @param jispText The jispText element to create its representation.
 * @return A label containing the JispText representation.
 */
function createJispTextElement(jispText) {
	var jispTextElement = document.createElement("label");
	jispTextElement.setAttribute("value", jispText.getElementsByTagName("text")[0].childNodes[0].nodeValue);
	
	return jispTextElement;
}