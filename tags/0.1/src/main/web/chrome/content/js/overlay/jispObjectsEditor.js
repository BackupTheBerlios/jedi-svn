/**
 * Inits the JispObjectsEditor.
 * It sets the jispObjectsContentsOnSelectDo.
 */
function jispObjectsEditorInit() {
	jispObjectsContentsOnSelectDo = "jispObjectsContentsOnSelect();";
}

/**
 * Event handler for select event in jispObjectsContent.
 * It enables the removeSelectedObjectsCmd if there is any object selected. Otherwise, it disables the command.
 */
function jispObjectsContentsOnSelect() {
	var removeSelectedObjectsCmd = document.getElementById("removeSelectedObjectsCmd");
	if (document.getElementById("jispObjectsContents").selectedItems.length == 0) {
		removeSelectedObjectsCmd.setAttribute("disabled", "true");
	} else {
		removeSelectedObjectsCmd.setAttribute("disabled", "false");
	}
}

/**
 * Sets the jispObjectsViewerRequest and reloads the jispObjectsView.
 * If there is no jispIcon selected, it disables the addObjectCmd.
 */
function reloadJispObjectsEditor() {
	jispObjectsViewerRequest = prefix + "JispObjectsView.do?jispIconId=" + jispIconId;
	reloadJispObjectsView();

	var addObjectButton = document.getElementById("addObjectCmd");
	if (jispIconId == "") {
		addObjectButton.setAttribute("disabled", "true");
		document.getElementById("removeSelectedObjectsCmd").setAttribute("disabled", "true");
	} else {
		addObjectButton.setAttribute("disabled", "false");
	}
}

/**
 * Reloads the JispObjectsView and the JispPackagesView.
 * Called by the JispObjectsAdder through the hidden button in the JispObjectsEditor linked to it when an object is added
 * in the JispObjectsAdder.
 */
function addObjectsToIcon() {
	reloadJispObjectsView();
	reloadJispPackagesView();
}

/**
 * Removes all the selected objects and reloads the JispObjectsView and the JispPackagesView.
 */
function removeSelectedObjects() {
	var objectsList = document.getElementById("jispObjectsContents");
	var selectedObjects = objectsList.selectedItems;
	
	var i;
	for (i=0; i<selectedObjects.length; i++) {
		removeObject(selectedObjects[i]);
	}
	objectsList.clearSelection();

	reloadJispObjectsView();
	reloadJispPackagesView();
}

/**
 * Removes the specified object from the JispIcon invoking the remove action.
 *
 * @param object The object to be removed.
 */
function removeObject(object) {
	requestUrl = prefix + "RemoveObjectFromIcon.do?jispIconId=" + jispIconId + "&jispObjectId=" + object.value;

	try {
		request = new XMLHttpRequest();
		request.open("GET", requestUrl, false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send("");
	} catch (e) {
		alert("Remove object error: " +e);
	}
}

/**
 * Opens a new window with a JispObjectsAdder.
 */
function addObject() {
	var newWindow = window.open("jispObjectsAdder.xul", "jispObjectsAdder", "chrome,dialog,modal,width=300,height=450,centerscreen,resizable"); 
}