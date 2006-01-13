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
		<xsl:text>J.E.D.I. Admin Login</xsl:text>
	</xsl:template>


	<!--
		Creates the login page.
		This page includes a form to put the password for login on it.
	-->	
	<xsl:template match="login">
		<div id="login">
			<xsl:apply-templates/>
		</div>
	</xsl:template>
	
	<!--
		Creates the login page contents based on the state of the login process.
		If the user isn't logged in, a login form is shown. If the user is already logged in,
		a link to the Admin interface is shown.
	-->
	<xsl:template match="login/status">
		<xsl:choose>
			<xsl:when test="@id='notLoggedIn'">
				<xsl:call-template name="userForm"/>
			</xsl:when>
			<xsl:when test="@id='loggedIn'">
				<p>You're logged in. Continue to the <a href="AdminView.do">Admin interface</a> (if you feel inclined to do so, of course. I'm not intending to force you :P )</p>
			</xsl:when>
			<xsl:otherwise>
				<p>Something weird happened. You shouldn't be here. You shouldn't have seen this. Please, take a look to this light... (FLASH) You have found a programming error in the application. Nothing else. Be happy.</p>
			</xsl:otherwise>
		</xsl:choose>
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
			</xsl:for-each>
		</div>
		<div id="login">
			<xsl:call-template name="userForm"/>
		</div>
	</xsl:template>


	<!--
		Creates the form to login.
		It includes a field to insert the password on it, a send button and a reset button.
	-->
	<xsl:template name="userForm">
		<div id="userForm">
			<xsl:element name="form">
				<xsl:attribute name="method">post</xsl:attribute>
				<xsl:attribute name="action">
					<xsl:value-of select="'Login.do'"/>
				</xsl:attribute>
				<p>Password:</p>
				<p>
					<input type="password" name="password"/>
					<xsl:element name="input">
						<xsl:attribute name="type">submit</xsl:attribute>
						<xsl:attribute name="value">
							<xsl:value-of select="'Login'"/>
						</xsl:attribute>
					</xsl:element>
					<input type="reset" value="Clear"/>
				</p>
			</xsl:element>
		</div>
	</xsl:template>	
</xsl:stylesheet>