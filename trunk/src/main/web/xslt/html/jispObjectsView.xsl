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
		Creates the page with the list of the objects from a jisp icon.
		All the objects have a button to remove the object from the icon.
		A form to add new objects to the icon is made.
		A button to return to the package being created is added.
	-->
	<xsl:template match="jedi/jispObjects">
		<xsl:if test="jispObject">
			<div id="jispObjects">
				<xsl:for-each select="jispObject">
					<xsl:apply-templates select="."/>
					<xsl:call-template name="actionElementForm">
						<xsl:with-param name="actionType" select="'Remove'"/>
						<xsl:with-param name="formAction" select="'RemoveObjectFromIcon.do'"/>
						<xsl:with-param name="jispElementId" select="@id"/>
						<xsl:with-param name="jispElementName" select="'Object'"/>
						<xsl:with-param name="jispAuxiliarElementId" select="/stxx/request/param[@name='jispIconId']/value"/>
						<xsl:with-param name="jispAuxiliarElementName" select="'Icon'"/>
					</xsl:call-template>
				</xsl:for-each>
			</div>
		</xsl:if>
		<div class="actionsButtons">
			<xsl:call-template name="addObjectsForm">
				<xsl:with-param name="jispIconId" select="/stxx/request/param[@name='jispIconId']/value"/>
			</xsl:call-template>

			<xsl:call-template name="returnToJispPackageViewForm"/>
		</div>
	</xsl:template>

	<!--
		Creates a form with a button to go to the objects list view to add.
		The objects list view to add needs the id of the icon which the objects will be added to.
		@param jispIconId The id of the icon which the objects will be added to.
	-->
	<xsl:template name="addObjectsForm">
		<xsl:param name="jispIconId"/>
		<div class="addObjectsForm">
			<form method="get" action="JispObjectsListView.do">
				<p>
					<xsl:element name="input">
						<xsl:attribute name="type">hidden</xsl:attribute>
						<xsl:attribute name="name">jispIconId</xsl:attribute>
						<xsl:attribute name="value">
							<xsl:value-of select="$jispIconId"/>
						</xsl:attribute>
					</xsl:element>
					<input type="submit" value="Add objects"/>
				</p>
			</form>
		</div>
	</xsl:template>

</xsl:stylesheet>