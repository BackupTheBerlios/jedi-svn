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
		Creates the representation of a jisp package.
		If the formType is 'addPackageForm', a form to add all the icons from the matched jisp package
		to the package being created is made.
		All the icons have a button to be added to the package being made. They're not editable.
		@param formType 'addPackageForm' to create a form to add all the icons from the matched jisp package.
	-->
	<xsl:template match="jispPackage">
		<xsl:param name="formType"/>
		<div class="jispPackage">
			<h3>Package</h3>
			<xsl:apply-templates select="jispMetadata"/>
			<xsl:if test="$formType='addPackageForm'">
				<xsl:call-template name="actionElementForm">
					<xsl:with-param name="actionType" select="'Add'"/>
					<xsl:with-param name="formAction" select="'AddPackageToPackage.do'"/>
					<xsl:with-param name="jispElementId" select="@id"/>
					<xsl:with-param name="jispElementName" select="'Package'"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="jispIcon">
				<div class="jispIcons">
					<xsl:for-each select="jispIcon">
						<xsl:apply-templates select=".">
							<xsl:with-param name="editable" select="'false'"/>
							<xsl:with-param name="formType" select="'addIconForm'"/>
						</xsl:apply-templates>
					</xsl:for-each>
				</div>
			</xsl:if>
		</div>
	</xsl:template>
	
	<!--
		Creates the representation of the jisp metadata.
		All the elements in the metadata are showed, a paragraph for each element.
	-->
	<xsl:template match="jispMetadata">
		<div class="jispMetadata">
			<xsl:element name="p">
				<xsl:value-of select="name"/>
				<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
				<xsl:value-of select="version"/>
			</xsl:element>
			<xsl:element name="p">
				<xsl:text disable-output-escaping="yes">Description:&amp;nbsp;</xsl:text>
				<xsl:value-of select="description"/>
			</xsl:element>
			<xsl:for-each select="jispAuthor">
				<xsl:element name="p">
					<xsl:text disable-output-escaping="yes">Author:&amp;nbsp;</xsl:text>
					<xsl:value-of select="name"/>
				</xsl:element>
			</xsl:for-each>
			<xsl:element name="p">
				<xsl:text disable-output-escaping="yes">Creation date:&amp;nbsp;</xsl:text>
				<xsl:value-of select="creation"/>
			</xsl:element>
			<xsl:element name="p">
				<xsl:text disable-output-escaping="yes">Home:&amp;nbsp;</xsl:text>
				<xsl:element name="a">
					<xsl:attribute name="href">
						<xsl:value-of select="home"/>
					</xsl:attribute>
					<xsl:value-of select="home"/>
				</xsl:element>
			</xsl:element>
		</div>
	</xsl:template>
	
	<!--
		Creates the representation of a jisp icon.
		This will be all its objects, all its texts.
		The JispIcon representation can be editable. If editable, forms to edit the objects and texts
		are made.
		The type of form can be 'addIconForm', 'removeIconForm' or empty.
		@param editable If the JispIcon can be editable.
		@param formType The type of the form to be created after the JispIcon representation.
	-->
	<xsl:template match="jispIcon">
		<xsl:param name="editable"/>
		<xsl:param name="formType"/>
		<div class="jispIcon">
			<h4>Icon</h4>
			<div class="jispObjects">
				<xsl:apply-templates select="jispObject"/>
				<xsl:if test="$editable='true'">
					<xsl:call-template name="editIconChildrenForm">
						<xsl:with-param name="jispIconId" select="@id"/>
						<xsl:with-param name="childrenName" select="'Object'"/>
					</xsl:call-template>
				</xsl:if>
			</div>
			<div class="jispTexts">
				<xsl:apply-templates select="jispText"/>
				<xsl:if test="$editable='true'">
					<xsl:call-template name="editIconChildrenForm">
						<xsl:with-param name="jispIconId" select="@id"/>
						<xsl:with-param name="childrenName" select="'Text'"/>
					</xsl:call-template>
				</xsl:if>
			</div>
			<xsl:choose>
				<xsl:when test="$formType='addIconForm'">
					<xsl:call-template name="actionElementForm">
						<xsl:with-param name="actionType" select="'Add'"/>
						<xsl:with-param name="formAction" select="'AddIconToPackage.do'"/>
						<xsl:with-param name="jispElementId" select="@id"/>
						<xsl:with-param name="jispElementName" select="'Icon'"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$formType='removeIconForm'">
					<xsl:call-template name="actionElementForm">
						<xsl:with-param name="actionType" select="'Remove'"/>
						<xsl:with-param name="formAction" select="'RemoveIconFromPackage.do'"/>
						<xsl:with-param name="jispElementId" select="@id"/>
						<xsl:with-param name="jispElementName" select="'Icon'"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>

	<!--
		Creates the representation of an object from a JispObject.
		If the contentType of the object is an image, an img element is created.
		Otherwise, a link to the object is made with the contentType as its text.
	-->
	<xsl:template match="jispObject">
		<div class="jispObject">
			<xsl:choose>
				<xsl:when test="starts-with(contentType, 'image')">
					<xsl:call-template name="img">
						<xsl:with-param name="src">
							<xsl:value-of select="src"/>
						</xsl:with-param>
						<xsl:with-param name="alt" select="concat('JispObject ', string(position()), '(', contentType, ')')"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:element name="a">
						<xsl:attribute name="href">
							<xsl:value-of select="src"/>
						</xsl:attribute>
						<xsl:value-of select="contentType"/>
					</xsl:element>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>

	<!--
		Creates a new paragraph containing the text from a JispText.
	-->
	<xsl:template match="jispText">
		<div class="jispText">
			<xsl:element name="p">
				<xsl:value-of select="text"/>
			</xsl:element>
		</div>
	</xsl:template>

</xsl:stylesheet>