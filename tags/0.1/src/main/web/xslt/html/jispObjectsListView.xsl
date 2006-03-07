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
		Creates the page with the list of the available objects to be added to a icon.
		A button to return to the icon being edited is added at the end of the package's list.
	-->
	<xsl:template match="jedi/jispObjectsList">
		<xsl:if test="jispObject">
			<div class="jispObjects">
				<xsl:for-each select="jispObject">
					<xsl:apply-templates select="."/>
					<xsl:call-template name="actionElementForm">
						<xsl:with-param name="actionType" select="'Add'"/>
						<xsl:with-param name="formAction" select="'AddObjectToIcon.do'"/>
						<xsl:with-param name="jispElementId" select="@id"/>
						<xsl:with-param name="jispElementName" select="'Object'"/>
						<xsl:with-param name="jispAuxiliarElementId" select="/stxx/request/param[@name='jispIconId']/value"/>
						<xsl:with-param name="jispAuxiliarElementName" select="'Icon'"/>
					</xsl:call-template>
				</xsl:for-each>
			</div>
		</xsl:if>
		<div class="actionsButtons">
			<xsl:call-template name="returnToJispObjectsViewForm">
				<xsl:with-param name="jispIconId" select="/stxx/request/param[@name='jispIconId']/value"/>
			</xsl:call-template>
		</div>
	</xsl:template>

	<!---
		Creates a form with a button to return to the page of the icon's objects being edited.
		@param jispIconId The id of the jisp icon which objects are being edited.
	-->
	<xsl:template name="returnToJispObjectsViewForm">
		<xsl:param name="jispIconId"/>
		<div class="returnToJispObjectsViewForm">
			<form method="get" action="JispObjectsView.do">
				<p>
					<xsl:element name="input">
							<xsl:attribute name="type">hidden</xsl:attribute>
							<xsl:attribute name="name">jispIconId</xsl:attribute>
							<xsl:attribute name="value">
								<xsl:value-of select="$jispIconId"/>
							</xsl:attribute>
						</xsl:element>
					<input type="submit" value="Return"/>
				</p>
			</form>
		</div>
	</xsl:template>

</xsl:stylesheet>