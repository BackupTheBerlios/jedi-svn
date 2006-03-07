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
		Creates the page with the list of the available packages to be added.
		All the packages have a button to add all its icons to the package being created.
		A button to return to the package being created is added at the end of the package's list.
	-->
	<xsl:template match="jedi/jispPackagesList">
		<div class="jispPackages">
			<xsl:choose>
				<xsl:when test="jispPackage">
					<xsl:apply-templates select="jispPackage">
						<xsl:with-param name="formType" select="'addPackageForm'"/>
					</xsl:apply-templates>
				</xsl:when>
				<xsl:otherwise>
					<p>There are no available packages.</p>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		<div class="actionsButtons">
			<xsl:call-template name="returnToJispPackageViewForm"/>
		</div>
	</xsl:template>

</xsl:stylesheet>