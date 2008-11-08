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
	
	<xsl:param name="ref-file"/>
	<xsl:variable name="index-file" select="document($ref-file)"/>

	<xsl:template match="anglicismes">
		<xsl:element name="anglicismes">
			<xsl:apply-templates select="@*|node()"/>	
		</xsl:element>
	</xsl:template>

    	<xsl:template match="anglicisme">

		<!-- FIXME:Warning: must not retreat other entry, need to keep a map of
			already dealt with entry... ask pps on this matter -->
		<xsl:variable name="value" select="@id"/>
		<xsl:choose>
			<!-- If there is more than one entry associated to this term, we
				have to merge the data -->
			<xsl:when test="count($index-file/anglicismes/anglicisme[@id = $value]) > 1">
				<!--
				<xsl:message>
					[<xsl:value-of select="$value"/>] a pour doublon:
					<xsl:for-each select="$index-file/anglicismes/anglicisme[@id = $value]">
						{<xsl:value-of select="@id"/>}
					</xsl:for-each>
				</xsl:message> -->
				<xsl:element name="anglicisme">
					<xsl:attribute name="id">
					</xsl:attribute>
					<xsl:for-each select="$index-file/anglicismes/anglicisme[@id = $value]">
						<!-- TODO: Agregate datas-->
					</xsl:for-each>
				</xsl:element>
				<!-- TODO: Add this term to dealt with entry -->
				
			</xsl:when>
			<!-- Otherwise, we simply copy the data ... -->
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="@*|node()"/>	
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
    	</xsl:template>


</xsl:stylesheet>