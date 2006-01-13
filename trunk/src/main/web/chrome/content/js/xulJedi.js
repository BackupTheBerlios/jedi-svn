/**
 * Inits the application setting the stxx transform to be used.
 */
function init() {
	try {
		request = new XMLHttpRequest();
		request.open("POST", prefix + "MainView.do", false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send("transform=xul");
	} catch (e) {
		alert("Initialization error: " +e);
	}
}

/**
 * Creates a new JispPackage to be edited.
 * It updates the menu to enable the "Close" command, and disable the "New" command.
 */
function newJisp() {
	var jispEditorFrame = document.getElementById("jispEditorFrame");
	jispEditorFrame.setAttribute("src","jispEditor.xul");

	var menuFileCloseCmd = document.getElementById("menuFileCloseCmd");
	menuFileCloseCmd.setAttribute("disabled", "false");

	var menuFileNewCmd = document.getElementById("menuFileNewCmd");
	menuFileNewCmd.setAttribute("disabled", "true");
}

/**
 * Closes the JispPackage being edited.
 * It updates the menu to enable the "New" command, and disable the "Close" command.
 */
function closeJisp() {
	var jispEditorFrame = document.getElementById("jispEditorFrame");
	jispEditorFrame.setAttribute("src", "");

	var menuFileNewCmd = document.getElementById("menuFileNewCmd");
	menuFileNewCmd.setAttribute("disabled", "false");

	var menuFileCloseCmd = document.getElementById("menuFileCloseCmd");
	menuFileCloseCmd.setAttribute("disabled", "true");

	try {
		request = new XMLHttpRequest();
		request.open("POST", prefix + "CloseJispPackage.do", false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send("");
	} catch (e) {
		alert("Package closing error: " +e);
	}
}


/**
 * Opens the aboutJedi window.
 */
function openAboutJediWindow() {
	window.open("aboutJedi.xul", "aboutJedi", "chrome,dialog,modal,centerscreen,width=250,height=150");
}

/**
 * Closes the xulJedi window.
 */
function closeWindow() {
  window.close();
}
