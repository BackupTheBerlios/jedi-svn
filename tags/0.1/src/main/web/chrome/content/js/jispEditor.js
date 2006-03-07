/**
 * Inits the JispPackagesEditor.
 * It sets the jispPackagesViewerRequest and the jispPackagesContentsOnSelectDo.
 * It also inits the JispPackagesViewer and the JispIconEditor.
 */
function jispEditorInit() {
	jispPackagesViewerRequest = prefix + "JispPackageView.do";
	jispPackagesContentsOnSelectDo = "jispPackagesContentsOnSelect();";
	jispPackagesViewerInit();
	jispIconEditorInit();
}

/**
 * Event handler for select event in jispPackagesContent.
 * It enables the removeSelectedIconsCmd if there is any icon selected, or the package element (and there is also at least an icon). Otherwise, it disables the command.
 * If there is an icon selected it also sets the jispIconId (if there are no icons selected, or more than one, it sets it to "").
 */
function jispPackagesContentsOnSelect() {
	setJispIconId("");
	var removeSelectedIconsCmd = document.getElementById("removeSelectedIconsCmd");
	if (document.getElementById("jispPackagesContents").selectedItems.length == 0) {
		removeSelectedIconsCmd.setAttribute("disabled", "true");
	} else {
		var selectedItem = document.getElementById("jispPackagesContents").selectedItem;
		if (selectedItem.label == "Icon") {
			if (document.getElementById("jispPackagesContents").selectedItems.length == 1) {
				setJispIconId(document.getElementById("jispPackagesContents").selectedItem.value);
			}
			removeSelectedIconsCmd.setAttribute("disabled", "false");
		} else {
			if (document.getElementById("jispPackagesContents").getRowCount()==1) {
				removeSelectedIconsCmd.setAttribute("disabled", "true");
			} else {
				removeSelectedIconsCmd.setAttribute("disabled", "false");
			}
		}
	}
	document.getElementById("jispIconIdElement").setAttribute("jispIconIdValue", jispIconId);
}

/**
 * Reloads the JispPackagesView and enables the downloadPackageCmd.
 * Called by the JispPackagesAdder through the hidden button in the JispPackagesEditor linked to it when an icon is added
 * in the JispPackagesAdder.
 */
function addIconsToPackage() {
	reloadJispPackagesView();

	document.getElementById("downloadPackageCmd").setAttribute("disabled", "false");
}

/**
 * Removes all the selected icons.
 * If the Package element is selected, all the icons in the package are removed.
 * It sets the jispIconIdElement to "" and reloads the JispPackagesView.
 * If no icons are left in the package after removing the selected icons, it disables the downloadPackageCmd.
 */
function removeSelectedIcons() {
	var packagesList = document.getElementById("jispPackagesContents");
	var selectedItems = packagesList.selectedItems;
	
	var i;
	var packageSelected = false;
	for (i=0; !packageSelected && i<selectedItems.length; i++) {
		if (selectedItems[i].label == "Package") {
			packagesList.selectItem(selectedItems[i]);
			packagesList.invertSelection();
			packageSelected = true;
		}
	}
	
	var selectedIcons = packagesList.selectedItems;
	for (i=0; i<selectedIcons.length; i++) {
		removeIcon(selectedIcons[i]);
	}
	packagesList.clearSelection();

	reloadJispPackagesView();

	//The jispPackagesContents must be get again because it was modified by reloadJispPackagesView.
	//Not getting it again throws an exception
	packagesList = document.getElementById("jispPackagesContents");
	if (packagesList.getRowCount()==1) {
		document.getElementById("downloadPackageCmd").setAttribute("disabled", "true");
	}
}

/**
 * Removes the specified icon from the JispPackage invoking the remove action.
 *
 * @param icon The icon to be removed.
 */
function removeIcon(icon) {
	requestUrl = prefix + "RemoveIconFromPackage.do?jispIconId=" + icon.value;

	try {
		request = new XMLHttpRequest();
		request.open("GET", requestUrl, false);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
		request.send("");
		
	} catch (e) {
		alert("Remove icon error: " +e);
	}
}

/**
 * Opens a new window with a jispIconsAdder.
 */
function openIconsAdder() {
	window.open("jispIconsAdder.xul", "jispIconsAdder", "chrome,dialog,modal,centerscreen,width=250,height=500"); 
}

/**
 * Downloads the JispPackage being created.
 * Opens a new browser window pointing to the Download action.
 */
function downloadPackage() {
	try {
		window.open(prefix + "DownloadPackage.do", "DownloadPackage", "");
	} catch (e) {
		alert("Download package error: " +e);
	}
}
