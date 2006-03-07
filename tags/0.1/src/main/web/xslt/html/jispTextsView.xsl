<?xml version="1.0" encoding="utf-8"?>

<!--
	Copyright 2005-2006 Daniel Calviño Sánchez
 
	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY.
	
	See the COPYING file for more details.
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="jisp.xsl"/>
	<xsl:import href="common.xsl"/>



	<!--
		Creates the page with the list of the texts from a jisp icon.
		All the texts have a button to remove the text from the icon.
		A form to add new texts to the icon is made.
		A button to return to the package being created is added.
	-->
	<xsl:template match="jispTexts">
		<xsl:if test="jispText">
			<div id="jispTexts">
				<xsl:for-each select="jispText">
					<xsl:apply-templates select="."/>
					<xsl:call-template name="actionElementForm">
						<xsl:with-param name="actionType" select="'Remove'"/>
						<xsl:with-param name="formAction" select="'RemoveTextFromIcon.do'"/>
						<xsl:with-param name="jispElementId" select="@id"/>
						<xsl:with-param name="jispElementName" select="'Text'"/>
						<xsl:with-param name="jispAuxiliarElementId" select="/stxx/request/param[@name='jispIconId']/value"/>
						<xsl:with-param name="jispAuxiliarElementName" select="'Icon'"/>
					</xsl:call-template>
				</xsl:for-each>
			</div>
		</xsl:if>
		<div class="actionsButtons">
			<xsl:call-template name="addTextForm">
				<xsl:with-param name="jispIconId" select="/stxx/request/param[@name='jispIconId']/value"/>
			</xsl:call-template>
	
			<xsl:call-template name="returnToJispPackageViewForm"/>
		</div>
	</xsl:template>

	<!--
		Creates a form with a text field and a button to add a jisp text to a jips icon.
		Adding a new text doesn't follows the standard form, so it can't be done with actionElementForm template.
		The text added is the one entered in the text field by the user.
		@param jispIconId The id of the jisp icon to add the text to.
	-->
	<xsl:template name="addTextForm">
		<xsl:param name="jispIconId"/>
		<div class="addTextForm">
			<form method="get" action="AddTextToIcon.do">
				<p>
					<xsl:element name="input">
						<xsl:attribute name="type">hidden</xsl:attribute>
						<xsl:attribute name="name">jispIconId</xsl:attribute>
						<xsl:attribute name="value">
							<xsl:value-of select="$jispIconId"/>
						</xsl:attribute>
					</xsl:element>
					<input type="text" name="jispTextText"/>
					<input type="submit" value="Add text"/>
				</p>
			</form>
		</div>
	</xsl:template>



	<!--
		Prints the message errors.
	-->
	<xsl:template match="error">
		<div class="error">
			<h2 id="errorTitle">Error:</h2>
			<xsl:for-each select="/stxx/messages/message[@property='error']">
				<p class="errorMessage">
					<xsl:value-of select="text"/>
				</p>
				<p>
					Continue to the <a href="JispPackageView.do">Jisp Texts View</a>.
				</p>
			</xsl:for-each>
		</div>
	</xsl:template>

</xsl:stylesheet>