/**
 * Inits the JispTextsEditor.
 * It sets the jispTextsContentsOnSelectDo.
 */
function jispTextsEditorInit() {
	jispTextsContentsOnSelectDo = "jispTextsContentsOnSelect();";
}

/**
 * Event handler for select event in jispTextsContent.
 * It enables the removeSelectedTextsCmd if there is any text selected. Otherwise, it disables the command.
 */
function jispTextsContentsOnSelect() {
	var removeSelectedTextsCmd = document.getElementById("removeSelectedTextsCmd");
	if (document.getElementById("jispTextsContents").selectedItems.length == 0) {
		removeSelectedTextsCmd.setAttribute("disabled", "true");
	} else {
		removeSelectedTextsCmd.setAttribute("disabled", "false");
	}
}

/**
 * Sets the jispTextsViewerRequest and reloads the jispTextsView.
 * If there is no jispIcon selected, it disables the addTextCmd.
 */
function reloadJispTextsEditor() {
	jispTextsViewerRequest = prefix + "JispTextsView.do?jispIconId=" + jispIconId;
	reloadJispTextsView();

	var addTextCmd = document.getElementById("addTextCmd");
	if (jispIconId == "") {
		addTextCmd.setAttribute("disabled", "true");
		document.getElementById("removeSelectedTextsCmd").setAttribute("disabled", "true");
	} else {
		addTextCmd.setAttribute("disabled", "false");
	}
}

/**
 * Reloads the JispTextsView and the JispPackagesView.
 * Called by the JispTextsAdder through the hidden button in the JispTextsEditor linked to it when a text is added
 * in the JispTextsAdder.
 */
function addTextsToIcon() {
	reloadJispTextsView();
	reloadJispPackagesView();
}

/**
 * Removes all the selected texts and reloads the JispTextsView and the JispPackagesView.
 */
function removeSelectedTexts() {
	var textsList = document.getElementById("jispTextsContents");
	var selectedTexts = textsList.selectedItems;
	
	var i;
	for (i=0; i<selectedTexts.length; i++) {
		removeText(selectedTexts[i]);
	}
	textsList.clearSelection();

	reloadJispTextsView();
	reloadJispPackagesView();
}

/**
 * Removes the specified text from the JispIcon invoking the remove action.
 *
 * @param The text to be removed.
 */
function removeText(text) {
	requestUrl = prefix + "RemoveTextFromIcon.do?jispIconId=" + jispIconId + "&jispTextId=" + text.value;

	try {
		request = new XMLHttpRequest();
		request.open("GET", requestUrl, false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send("");
		
	} catch (e) {
		alert("Remove text error: " +e);
	}
}

/**
 * Opens a new window with a JispTextsAdder.
 */
function addText() {
	var newWindow = window.open("jispTextsAdder.xul", "jispTextsAdder", "chrome,dialog,modal,width=250,height=100"); 
}