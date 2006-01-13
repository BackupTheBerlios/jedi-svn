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
		Creates the page with the package being created.
		It shows all the icons in the package (with the posibility to edit them), and two buttons, one to add new icons
		and one to download the package.
	-->
	<xsl:template match="jedi/jispPackage">
		<div class="jispPackages">
			<div class="jispPackage">
				<h3>Package</h3>
				<xsl:if test="jispIcon">
					<div class="jispIcons">
						<xsl:for-each select="jispIcon">
							<xsl:apply-templates select=".">
								<xsl:with-param name="editable" select="'true'"/>
								<xsl:with-param name="formType" select="'removeIconForm'"/>
							</xsl:apply-templates>
						</xsl:for-each>
					</div>
				</xsl:if>
			</div>
		</div>
		<div class="actionsButtons">
			<xsl:call-template name="addIconsForm"/>
			<xsl:call-template name="downloadForm"/>
		</div>
	</xsl:template>


	<!--
		Creates a form with a button to go to the icons list view to add.
	-->
	<xsl:template name="addIconsForm">
		<div class="addIconsForm">
			<form method="get" action="JispPackagesListView.do">
				<p>
					<input type="submit" value="Add icons"/>
				</p>
			</form>
		</div>
	</xsl:template>

	<!---
		Creates a form with a button to download the package being created.
	-->
	<xsl:template name="downloadForm">
		<div class="downloadForm">
			<form method="get" action="DownloadPackage.do">
				<p>
					<input type="submit" value="Download package"/>
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
					Continue to the <a href="JispPackageView.do">Jisp Package View</a> (maybe you won't be able to do so).
				</p>
			</xsl:for-each>
		</div>
	</xsl:template>

</xsl:stylesheet>