/**
 * Inits the jispIconsAdder.
 * It sets the jispPackagesViewerRequest and the jispPackagesContentsOnSelectDo.
 * It also inits the jispPackagesViewer.
 */
function jispIconsAdderInit() {
	jispPackagesViewerRequest = prefix + "JispPackagesListView.do";
	jispPackagesContentsOnSelectDo = "jispPackagesContentsOnSelect();";
	jispPackagesViewerInit();
}

/**
 * Event handler for select event in jispPackagesContent.
 * It enables the addSelectedItemsCmd if there is any icon selected, or the package element. Otherwise, it disables the command.
 */
function jispPackagesContentsOnSelect() {
	var addSelectedItemsCmd = document.getElementById("addSelectedItemsCmd");
	if (document.getElementById("jispPackagesContents").selectedItems.length == 0) {
		addSelectedItemsCmd.setAttribute("disabled", "true");
	} else {
		addSelectedItemsCmd.setAttribute("disabled", "false");
	}
}

/**
 * Adds the selected items to the JispPackage.
 * If the selected item is a pacakge, all the icons from the package are added. If it is an icon, the icon is added.
 * The addIconsToPackage hidden button command is invoked after adding the icons to notify it to the JispIconsEditor.
 */
function addSelectedItems() {
	var packagesList = document.getElementById("jispPackagesContents");
	var selectedItems = packagesList.selectedItems;
	
	var i;
	for (i=0; i<selectedItems.length; i++) {
		if (selectedItems[i].label == "Package") {
			addPackage(selectedItems[i]);
		} else {
			addIcon(selectedItems[i]);
		}
	}
	window.opener.document.getElementById("addIconsToPackage").doCommand();
}

/**
 * Adds all the icons from the specified JispPackage to the JispPackage being created.
 */
function addPackage(packageToAdd) {
	addItem(prefix + "AddPackageToPackage.do?jispPackageId=" + packageToAdd.value);
}

/**
 * Adds the specified icon to the JispPackage being created.
 */
function addIcon(icon) {
	addItem(prefix + "AddIconToPackage.do?jispIconId=" + icon.value);
}

/**
 * Adds an item using the requestUrl needed to do the adding.
 * It takes care of creating the request and so on.
 *
 * @param requestUrl The url to be called to do the adding.
 */
function addItem(requestUrl) {
	try {
		request = new XMLHttpRequest();
		request.open("GET", requestUrl, false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send("");
	} catch (e) {
		alert("Add element error: " +e);
	}
}

/**
 * Closes the jispIconsAdder window.
 */
function closeJispIconsAdderWindow() {
	window.close();
}