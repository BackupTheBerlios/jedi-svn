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
		Creates the main page.
		It simply asks to create a new package using the menu if no package is being created.
		If the package is already being created, it indicate how to view the package.
	-->
	<xsl:template match="main">
		<xsl:choose>
			<xsl:when test="/stxx/jedi/status/package='true'">
				<p>Use the menu link or click here to <a href="JispPackageView.do">view the package being created</a>.</p>
			</xsl:when>
			<xsl:otherwise>
				<p>Use the menu link  or click here to <a href="NewJispPackage.do">create a new package</a>.</p>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>