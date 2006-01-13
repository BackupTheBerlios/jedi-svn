/**
 * The id of the JispIcon being edited.
 */
var jispIconId;



/**
 * Inits the JispIconEditor.
 * It inits the JispObjectsEditor and the JispTextEditor.
 */
function jispIconEditorInit() {
	jispIconId = "";
	jispObjectsEditorInit();
	jispTextsEditorInit();
}

/**
 * Sets the JispIconId and reloads the JispObjectEditor and the JispTextEditor
 * with the new JispIconId to be used.
 */
function setJispIconId(jispIconIdToSet) {
	jispIconId = jispIconIdToSet;
	reloadJispIconEditor();
}

/**
 * Reloads the JispIconEditor.
 * It reloads the JispObjectsEditor and the JispTextEditor.
 */
function reloadJispIconEditor() {
	reloadJispObjectsEditor();
	reloadJispTextsEditor();
}