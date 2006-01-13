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

	<xsl:output method="xml" encoding="utf-8" indent="yes" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"/>

	<!--
		Path to the common images (used also in the editor), such as the validation images
	-->
	<xsl:variable name="commonImagesPath" select="'images'"/>

	<!--
		Sets the page title
	-->
	<xsl:template name="title">
		<xsl:text>J.E.D.I.</xsl:text>
	</xsl:template>

	<!--
		Sets the CSS style href
	-->
	<xsl:template name="styleHref">
		<xsl:text>style.css</xsl:text>
	</xsl:template>



	<!--
		Creates a img html element.
		Each parameter corresponds with the img attribute with the same name.
		src and alt must be present when calling the template. height and width are optional,
		so if they're not specified when calling the template, they won't exist in the output.
	-->
	<xsl:template name="img">
		<xsl:param name="src"/>
		<xsl:param name="alt"/>
		<xsl:param name="height"/>
		<xsl:param name="width"/>
		<xsl:element name="img">
			<xsl:attribute name="src">
				<xsl:value-of select="$src"/>
			</xsl:attribute>
			<xsl:attribute name="alt">
				<xsl:value-of select="$alt"/>
			</xsl:attribute>
			<xsl:if test="$height != ''">
				<xsl:attribute name="height">
					<xsl:value-of select="$height"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$height != ''">
				<xsl:attribute name="width">
					<xsl:value-of select="$width"/>
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>



	<!--
		Entry point.
		stxx is the base element of the generated xml.
	-->
	<xsl:template match="stxx">
		<xsl:apply-templates select="jedi"/>
	</xsl:template>

	<!--
		Creates the common code for all pages: the head section and the body (calling the body template).
	-->
	<xsl:template match="jedi">
		<html>
			<head>
				<title>
					<xsl:call-template name="title"/>
				</title>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
				<meta name="language" content="en"/>
				<xsl:element name="link">
					<xsl:attribute name="rel">stylesheet</xsl:attribute>
					<xsl:attribute name="type">text/css</xsl:attribute>
					<xsl:attribute name="title">Style</xsl:attribute>
					<xsl:attribute name="href">
						<xsl:call-template name="styleHref"/>
					</xsl:attribute>
				</xsl:element>
			</head>
			<body>
				<xsl:call-template name="body"/>
			</body>
		</html>
	</xsl:template>



	<!--
		Creates the body of the pages.
	-->
	<xsl:template name="body">
		<div id="main">
			<xsl:call-template name="footer"/>
		</div>
	</xsl:template>

	<!--
		Creates the footer for all pages including the XHTML and CSS validation.
	-->
	<xsl:template name="footer">
		<div id="footer">
			<div id="validation">
				<a href="http://validator.w3.org/check?uri=referer">
					<xsl:call-template name="img">
						<xsl:with-param name="src" select="concat($commonImagesPath,'/valid-xhtml10.png')"/>
						<xsl:with-param name="alt" select="'Valid XHTML 1.0 Strict!'"/>
						<xsl:with-param name="height" select="'31'"/>
						<xsl:with-param name="width" select="'88'"/>
					</xsl:call-template>
				</a>
				<a href="http://jigsaw.w3.org/css-validator/check/referer">
					<xsl:call-template name="img">
						<xsl:with-param name="src" select="concat($commonImagesPath,'/valid-css.png')"/>
						<xsl:with-param name="alt" select="'Valid CSS!'"/>
						<xsl:with-param name="height" select="'31'"/>
						<xsl:with-param name="width" select="'88'"/>
					</xsl:call-template>
				</a>
			</div>
		</div>
	</xsl:template>
</xsl:stylesheet>