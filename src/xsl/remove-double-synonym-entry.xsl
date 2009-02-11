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
<xsl:stylesheet version="1.0" 
		xmlns:exsl="http://exslt.org/common"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>	
	<!-- Copy all nodes from here.  -->
    	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>	
		</xsl:copy>
    	</xsl:template>

	<xsl:template match="synonymes">
		<xsl:variable name="synonymes">
			<synomymes>
				<xsl:for-each select="synonyme">
				<xsl:sort select="text()"/>
					<synonyme><xsl:value-of select="text()"/></synonyme>
				</xsl:for-each>
			</synomymes>
		</xsl:variable>

		<xsl:element name="synonymes">
		<xsl:for-each select="exsl:node-set($synonymes)/synomymes/synonyme">
			<xsl:variable name="current-synonyme">
				<xsl:value-of select="text()"/>
			</xsl:variable>
			<xsl:variable name="previous-synonyme">
				<xsl:value-of select="./preceding-sibling::synonyme"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="string-length($previous-synonyme) &lt; 1">
					<!-- pas de précédent, on garde ce terme, c'est sa première occurence -->
					<xsl:call-template name="make-synonym">
						<xsl:with-param name="current-synonyme" select="$current-synonyme"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="($current-synonyme != $previous-synonyme)">
						<!-- le terme est précédé d'un terme différent, on le recopie aussi -->
						<xsl:call-template name="make-synonym">
							<xsl:with-param name="current-synonyme" select="$current-synonyme"/>
						</xsl:call-template>
					</xsl:if>
					<!-- sinon, c'est un doublon, on le "drop" -->
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		</xsl:element>
	</xsl:template>

	<xsl:template name="make-synonym">
		<xsl:param name="current-synonyme"/>
		<xsl:element name="synonyme">
			<xsl:value-of select="$current-synonyme"/>
		</xsl:element>
	</xsl:template>
  
</xsl:stylesheet>
