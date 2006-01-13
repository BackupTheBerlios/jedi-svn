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

	<xsl:import href="base.xsl"/>



	<!--
		Creates the body of the pages.
		It creates the menu, the content and the footer.
	-->
	<xsl:template name="body">
		<div id="main">
			<xsl:call-template name="menu"/>
			<div id="content">
				<xsl:apply-templates/>
			</div>
			<xsl:call-template name="footer"/>
		</div>
	</xsl:template>


	<!--
		Creates the menu.
		The elements in the menu will depend on the status of the user session.
		If there is a package being created, the links to view the package or close it are shown.
		If there is no package being created, the link to create a new one is shown.
		There is also always a link to the index.html page to exit the application.
	-->
	<xsl:template name="menu">
		<div id="menu">
			<ul>
				<xsl:choose>
					<xsl:when test="/stxx/jedi/status/package='true'">
						<li><a href="JispPackageView.do">View package</a></li>
						<li><a href="CloseJispPackage.do">Close package</a></li>
					</xsl:when>
	
					<xsl:otherwise>
						<li><a href="NewJispPackage.do">New package</a></li>
					</xsl:otherwise>
				</xsl:choose>
				<li><a href="index.html">Exit</a></li>
			</ul>
		</div>
	</xsl:template>


	<!--
		Matchs the package status information preventing it from showing in the output.
	-->
	<xsl:template match="status/package">
	</xsl:template>



	<!--
		Creates a form with a button to add or remove a jisp element.
		The form contains a visible button and two hidden fields. The hidden fields contains the parameters
		needed to do the action with the element. The form's action to be called when the button is pressed should
		be specified when calling the template.
		The element to do the action with is mandatory, while the auxiliar element is optional. If no auxiliar element
		is specified, the hidden field containing the element's id isn't created. This auxiliar element can be an
		icon to remove objects from, for example.
		The name of the element, and the name of the auxiliar element must begin with a capital letter.
		@param actionType 'Add' or 'Remove'.
		@param formAction The action to be called in the form.
		@param jispElementId The id of the element to be added or removed.
		@param jispElementName The name of the element to be added or removed.
		@param jispAuxiliarElementId The id of the auxiliar element.
		@param jispAuxiliarElementName The name of the auxiliar element.
	-->
	<xsl:template name="actionElementForm">
		<xsl:param name="actionType"/>
		<xsl:param name="formAction"/>
		<xsl:param name="jispElementId"/>
		<xsl:param name="jispElementName"/>
		<xsl:param name="jispAuxiliarElementId"/>
		<xsl:param name="jispAuxiliarElementName"/>
		<xsl:element name="div">
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="$actionType = 'Add'">
						<xsl:text>add</xsl:text>
					</xsl:when>
					<xsl:when test="$actionType = 'Remove'">
						<xsl:text>remove</xsl:text>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:value-of select="concat($jispElementName,'Form')"/>
			</xsl:attribute>
			<xsl:element name="form">
				<xsl:attribute name="method">get</xsl:attribute>
				<xsl:attribute name="action">
					<xsl:value-of select="$formAction"/>
				</xsl:attribute>
				<p>
					<xsl:element name="input">
						<xsl:attribute name="type">hidden</xsl:attribute>
						<xsl:attribute name="name">
							<xsl:value-of select="concat('jisp', $jispElementName, 'Id')"/>
						</xsl:attribute>
						<xsl:attribute name="value">
							<xsl:value-of select="$jispElementId"/>
						</xsl:attribute>
					</xsl:element>
					<xsl:if test="$jispAuxiliarElementId != ''">
						<xsl:element name="input">
							<xsl:attribute name="type">hidden</xsl:attribute>
							<xsl:attribute name="name">
								<xsl:value-of select="concat('jisp', $jispAuxiliarElementName, 'Id')"/>
							</xsl:attribute>
							<xsl:attribute name="value">
								<xsl:value-of select="$jispAuxiliarElementId"/>
							</xsl:attribute>
						</xsl:element>
					</xsl:if>
					<xsl:element name="input">
						<xsl:attribute name="type">submit</xsl:attribute>
						<xsl:attribute name="value">
							<xsl:value-of select="concat($actionType, ' ', $jispElementName)"/>
						</xsl:attribute>
					</xsl:element>
				</p>
			</xsl:element>
		</xsl:element>
	</xsl:template>


	<!--
		Creates a new form with a button to edit the objects or texts of a JispIcon, identified by its id.
		The name of the children elements must be 'Object' or 'Text', in singular form, and with the first
		letter capitalized.
		@param jispIconId The id of the jispIcon to create the form for.
		@param childrenName The name of the children elements of the jispIcon.
	-->
	<xsl:template name="editIconChildrenForm">
		<xsl:param name="jispIconId"/>
		<xsl:param name="childrenName"/>
		<xsl:element name="div">
			<xsl:attribute name="class">
				<xsl:value-of select="concat('edit', $childrenName, 's', 'Form')"/>
			</xsl:attribute>
			<xsl:element name="form">
				<xsl:attribute name="method">get</xsl:attribute>
				<xsl:attribute name="action">
					<xsl:value-of select="concat('Jisp', $childrenName, 's', 'View.do')"/>
				</xsl:attribute>
				<p>
					<xsl:element name="input">
						<xsl:attribute name="type">hidden</xsl:attribute>
						<xsl:attribute name="name">jispIconId</xsl:attribute>
						<xsl:attribute name="value">
							<xsl:value-of select="$jispIconId"/>
						</xsl:attribute>
					</xsl:element>
					<xsl:element name="input">
						<xsl:attribute name="type">submit</xsl:attribute>
						<xsl:attribute name="value">
							<xsl:value-of select="concat('Edit ', $childrenName, 's')"/>
						</xsl:attribute>
					</xsl:element>
				</p>
			</xsl:element>
		</xsl:element>
	</xsl:template>


	<!---
		Creates a form with a button to return to the page of the package being created.
	-->
	<xsl:template name="returnToJispPackageViewForm">
		<div class="returnToJispPackageViewForm">
			<form method="get" action="JispPackageView.do">
				<p>
					<input type="submit" value="Return"/>
				</p>
			</form>
		</div>
	</xsl:template>

</xsl:stylesheet>