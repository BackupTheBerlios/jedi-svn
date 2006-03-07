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

	<xsl:import href="common.xsl"/>

	<!--
		Sets the page title
	-->
	<xsl:template name="title">
		<xsl:text>J.E.D.I. Admin Interface</xsl:text>
	</xsl:template>

	<!--
		Sets the CSS style href
	-->
	<xsl:template name="styleHref">
		<xsl:text>adminStyle.css</xsl:text>
	</xsl:template>


	<!--
		Creates the admin page.
		This page includes a Logout link, a form to upload new packages, and a list with all
		the saved packages.
	-->
	<xsl:template match="admin">
		<div id="logout">
			<a href="Logout.do">Logout</a>
		</div>
		<div id="upload">
			<h3>Upload new JispFile</h3>
			<form method="post" enctype="multipart/form-data" action="UploadJispFile.do">
				<input type="file" name="file"/>
				<input type="submit" value="Upload"/>
			</form>
		</div>
		<xsl:apply-templates/>
	</xsl:template>

	<!--
		Creates the list with the saved packages in the admin page.
	-->
	<xsl:template match="admin/jispPackagesList">
		<div id="jispPackagesList">
			<h3>Saved packages</h3>
			<xsl:apply-templates/>
		</div>
	</xsl:template>

	<!--
		Creates the package info, the icons and the form to remove the package.
	-->
	<xsl:template match="admin/jispPackagesList/jispPackage">
		<div class="jispPackage">
			<xsl:call-template name="jispPackageInfo"/>
			<xsl:call-template name="jispPackageIcons"/>
			<xsl:call-template name="deletePackageButton"/>
		</div>
	</xsl:template>
	
	<!--
		Creates the package info.
		This info includes the name and version of the package.
	-->
	<xsl:template name="jispPackageInfo">
		<div class="jispPackageInfo">
			<xsl:element name="p">
				<xsl:value-of select="jispMetadata/name"/>
				<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
				<xsl:value-of select="jispMetadata/version"/>
			</xsl:element>
		</div>
	</xsl:template>

	<!--
		Creates the example icons for the package.
		The first five images of all the objects in all the icons will be shown.
	-->
	<xsl:template name="jispPackageIcons">
		<div class="jispPackageIcons">
			<xsl:for-each select="jispIcon/jispObject[starts-with(contentType, 'image')]">
				<xsl:if test="position() &lt; 6">
					<xsl:call-template name="img">
						<xsl:with-param name="src">
							<xsl:value-of select="src"/>
						</xsl:with-param>
						<xsl:with-param name="alt" select="concat('JispObject ', string(position()), '(', contentType, ')')"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>
		</div>
	</xsl:template>

	<!--
		Creates the delete button for the package.
	-->
	<xsl:template name="deletePackageButton">
		<div class="deletePackageButton">
			<form method="GET" action="DeleteJispPackage.do">
				<p>
					<xsl:element name="input">
						<xsl:attribute name="type">hidden</xsl:attribute>
						<xsl:attribute name="name">jispPackageId</xsl:attribute>
						<xsl:attribute name="value">
							<xsl:value-of select="@id"/>
						</xsl:attribute>
					</xsl:element>
					<input type="submit" value="Delete"/>
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
					Continue to the <a href="AdminView.do">Admin Interface</a> (maybe you won't be able to do so).
				</p>
			</xsl:for-each>
		</div>
	</xsl:template>
</xsl:stylesheet>