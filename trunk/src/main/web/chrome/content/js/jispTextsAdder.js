/**
 * The id of the JispIcon selected when the jispTextAdder window was opened.
 */
var jispIconSelectedId;

/**
 * Inits the jispTextsAdder.
 * It sets the jispIconSelectedId using the hidden jispIconIdElement in the opener JispTextsEditor.
 */
function jispTextsAdderInit() {
	jispIconSelectedId = window.opener.document.getElementById("jispIconIdElement").getAttribute("jispIconIdValue");
}

/**
 * Adds the selected text to the icon.
 * The addTextsToIcon hidden button command is invoked after adding the text to notify it to the JispTextsEditor.
 */
function addSelectedText() {
	var textToAdd = document.getElementById("jispText").value;
	addText(textToAdd);
	
	window.opener.document.getElementById("addTextsToIcon").doCommand();
}

/**
 * Adds the specified text to the icon which was selected when the window was opened.
 *
 * @param textToAdd The text to be added to the icon.
 */
function addText(textToAdd) {
	requestUrl = prefix + "AddTextToIcon.do?jispIconId=" + jispIconSelectedId + "&jispTextText=" + textToAdd;
	
	try {
		request = new XMLHttpRequest();
		request.open("GET", requestUrl, false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

		request.send("");
	} catch (e) {
		alert("Add text error: " + e);
	}
}

/**
 * Closes the jispTextAdder window.
 */
function closeJispTextsAdderWindow() {
	window.close();
}
