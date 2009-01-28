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
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>

	<xsl:template match="/">
<xsl:message>
Les termes suivants ont été supprimés pour cause d'homographie :
</xsl:message>
		<xsl:apply-templates select="@*|node()"/>	
	</xsl:template>
	
	<!-- Copy all nodes from here.  -->
    	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>	
		</xsl:copy>
    	</xsl:template>

    	<xsl:template match="anglicisme">
		<xsl:variable name="label" select="@id"/>
			<!-- 	if there is at least one synonym that differs from french translation, 
				we remove the entry, otherwise we keep it -->
			<xsl:choose>
				<xsl:when test="count(domaines/domaine/synonymes/synonyme[text() != $label]) > 1">
					<xsl:copy>
						<xsl:apply-templates select="@*|node()"/>	
					</xsl:copy>
				</xsl:when>
				<xsl:otherwise>
					<xsl:message><xsl:value-of select="$label"/></xsl:message>
				</xsl:otherwise>
			</xsl:choose> <!--
				<xsl:copy>
					<xsl:apply-templates select="@*|node()"/>	
				</xsl:copy> -->
    	</xsl:template>


</xsl:stylesheet>
