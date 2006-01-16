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

	<xsl:output method="xml" />
	
	<!--
		Copies the jedi and messages elements and its children.
	-->
	<xsl:template match="stxx">
		<stxx>
			<xsl:copy-of select="jedi"/>
			<xsl:copy-of select="messages"/>
		</stxx>
	</xsl:template>
	
</xsl:stylesheet>