/**
 * The id of the JispIcon selected when the jispObjectsAdder window was opened.
 */
var jispIconSelectedId;

/**
 * Inits the jispObjectsAdder.
 * It sets the jispIconSelectedId using the hidden jispIconIdElement in the opener JispObjectsEditor.
 * It also sets the jispObjectsViewerRequest and the jispObjectsContentsOnSelectDo.
 * It also inits the jispObjectsViewer.
 */
function jispObjectsAdderInit() {
	jispIconSelectedId = window.opener.document.getElementById("jispIconIdElement").getAttribute("jispIconIdValue");

	jispObjectsViewerRequest = prefix + "JispObjectsListView.do";
	jispObjectsContentsOnSelectDo = "jispObjectsContentsOnSelect();";
	jispObjectsViewerInit();
}

/**
 * Event handler for select event in jispObjectsContent.
 * It enables the addSelectedObjectsCmd if there is any object selected. Otherwise, it disables the command.
 */
function jispObjectsContentsOnSelect() {
	var addSelectedObjectsCmd = document.getElementById("addSelectedObjectsCmd");
	if (document.getElementById("jispObjectsContents").selectedItems.length == 0) {
		addSelectedObjectsCmd.setAttribute("disabled", "true");
	} else {
		addSelectedObjectsCmd.setAttribute("disabled", "false");
	}
}

/**
 * Adds the selected objects to the icon.
 * The addObjectsToIcon hidden button command is invoked after adding the objects to notify it to the JispObjectsEditor.
 */
function addSelectedObjects() {
	var objectsList = document.getElementById("jispObjectsContents");
	var selectedObjects = objectsList.selectedItems;
	
	var i;
	for (i=0; i<selectedObjects.length; i++) {
		addObject(selectedObjects[i]);
	}

	window.opener.document.getElementById("addObjectsToIcon").doCommand();
}

/**
 * Adds the specified object to the icon which was selected when the window was opened.
 *
 * @param objectToAdd The object to be added to the icon.
 */
function addObject(objectToAdd) {
	requestUrl = prefix + "AddObjectToIcon.do?jispIconId=" + jispIconSelectedId + "&jispObjectId=" + objectToAdd.value;
	
	try {
		request = new XMLHttpRequest();
		request.open("GET", requestUrl, false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send("");
	} catch (e) {
		alert("Add object error: " + e);
	}
}

/**
 * Closes the jispObjectsAdder window.
 */
function closeObjectsAdderWindow() {
	window.close();
}
