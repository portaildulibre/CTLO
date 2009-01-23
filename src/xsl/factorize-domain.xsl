<?xml version="1.0" encoding="UTF-8"?>
<!--
 Correcteur terminologique - éradication des anglicismes.
 Copyright (C) 2006 Romain PELISSE, belaran@gmail.com
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

	<xsl:param name="ref-filename"/>
	<xsl:variable name="ref-file"	select="document($ref-filename)"/>

	<!-- Copy all nodes from here.  -->
    	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>	
		</xsl:copy>
    	</xsl:template>
<!--

Disable, work still in progress.
	<xsl:template match="domaine">
		<xsl:variable name="entry-id" select="../../@id"/> // anglicisme id 
		<xsl:variable name="domain-id" select="@id"/> 

		<xsl:variable name="count"
			      select="count($ref-file/anglicismes/anglicisme[@id = $entry-id]/domaines/domaine[@id = $domain-id])"/>
<xsl:message><xsl:value-of select="$count"/></xsl:message>
		<xsl:if test="count($ref-file/anglicismes/anglicisme[@id = $entry-id]/domaines/domain[@id = $domain-id]) > 1">
			<xsl:message>
Suspect is :<xsl:value-of select="$entry-id"/> with domain: <xsl:value-of select="$domain-id"/>, that appears <xsl:value-of select="$count"/>
			</xsl:message>
		</xsl:if>
	</xsl:template>
	-->
</xsl:stylesheet>
