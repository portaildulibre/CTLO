<?xml version="1.0" encoding="UTF-8"?>
<!--
 Correcteur terminologique - éradication des anglicismes.
 Copyright (C) 2006 Linagora SA - Manuel Odesser modesser@linagora.com
 Copyright (c) 2003 by Sun Microsystems, Inc.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA

	Romain PELISSE, romain.pelisse@atosorigin.com
-->

<!-- As there is no string function provided by XPath to handle case issue, and
as i do not wish to use either java: extension point or even exslt, i wrote this
little handy xslt script...
-->
<xsl:stylesheet version="1.0" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<!-- Unit test -->
	<xsl:template match="/">
		<xsl:message>
			<xsl:call-template name="toLowerCase">
				<xsl:with-param name="string" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
			</xsl:call-template>

			<xsl:call-template name="toLowerCase">
				<xsl:with-param name="string" select="'UnExempleParmiTANTdAUtres'"/>
			</xsl:call-template>
		</xsl:message>
	</xsl:template>

	<xsl:template name="toLowerCase">
		<xsl:param name="string"/>
		<xsl:value-of select="translate($string,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
	</xsl:template>
	


</xsl:stylesheet>