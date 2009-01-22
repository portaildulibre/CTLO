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

<!-- 
	This script removes all excluded terms from the term databases.
	Note that it use a Java extension.
-->
<xsl:stylesheet version="1.0" 
		xmlns:java="java"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>

	<xsl:param name="exclusion-file"/>

	<xsl:variable name="exclusions"		select="document($exclusion-file)"/>

	<!-- Copy all nodes from here.  -->
    	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>	
		</xsl:copy>
    	</xsl:template>

	<!-- Defining the root of the new XML document -->
	<xsl:template match="/">
		<xsl:variable name="nb-exclude-terms"	select="count($exclusions/excludes/exclude)"/>
		<!-- //FUTURE: Find way to log missing exclude term -->
		<xsl:message>
Nombre de termes à exclure: <xsl:value-of select="$nb-exclude-terms"/>
		</xsl:message>

		<xsl:apply-templates select="@*|node()"/>

	</xsl:template>

	<xsl:template match="anglicisme">
		<xsl:variable name="term" 	select="@id"/>
		<xsl:variable name="node"	select="child::node()"/>
		
		<xsl:variable name="is-exclude">
			  <xsl:call-template name="is-an-exclude-term">
			    <xsl:with-param name="term" select="$term"/>
			  </xsl:call-template>
		</xsl:variable>
<!--
		<xsl:message>anglicisme:<xsl:value-of select="$term"/> (<xsl:value-of select="$is-exclude"/>)</xsl:message>
-->
		<xsl:choose>
			<xsl:when test="contains($is-exclude,'excluded')">
				<xsl:message>Le terme <xsl:value-of select="$term"/> a été retiré.</xsl:message>
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="anglicisme">
					<xsl:attribute name="id">
						<xsl:value-of select="$term"/>
					</xsl:attribute>
					<xsl:apply-templates select="$node"/>	
				</xsl:element>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!--
		Functions area:
		- - - - - - - -

		Below this limit there is only template called (~ functions)
	-->
	<xsl:template name="is-an-exclude-term">
		<xsl:param name="term"/>
	
		<xsl:for-each select="$exclusions/excludes/exclude">
			<xsl:choose>
				<xsl:when test="$term = text()">
					<xsl:value-of select="'excluded'"/>
				</xsl:when>
				<!-- FIXME: Find a away to set term to lower case !!! --> 
				<xsl:when test="$term = 'Net'">
					<xsl:value-of select="'excluded'"/>
				</xsl:when> -->
			</xsl:choose>
		</xsl:for-each> 
	</xsl:template>

</xsl:stylesheet>
