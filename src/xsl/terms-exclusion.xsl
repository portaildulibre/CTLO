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
	
	<xsl:include href="common.xsl"/>

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
		<xsl:apply-templates select="@*|node()"/>
	</xsl:template>

	<xsl:template name="anglicismes">
          <xsl:variable name="nb-exclude-terms"   select="count($exclusions/excludes/exclude)"/>
	  <log-entry type="exclusions">
	    <h1>Termes exclues</h1>
	    <p>Nombre de termes à exclure: <xsl:value-of select="$nb-exclude-terms"/>.</p>
	    <p>Les termes suivants ont été retirés de la table de correspondance:</p>
	  </log-entry>
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
<log-entry type="exclusions">
	<log-item><xsl:value-of select="$term"/></log-item>
</log-entry>
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
		<xsl:variable name="term-as-lower-case">
			<xsl:call-template name="toLowerCase">
				<xsl:with-param name="string" select="$term"/>
			</xsl:call-template>
		</xsl:variable>
	
		<xsl:for-each select="$exclusions/excludes/exclude">
			<xsl:variable name="text">
				<xsl:call-template name="toLowerCase">
					<xsl:with-param name="string" select="text()"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$term-as-lower-case = $text">
					<xsl:value-of select="'excluded'"/>
				</xsl:when>
			</xsl:choose>
		</xsl:for-each> 
	</xsl:template>

</xsl:stylesheet>
