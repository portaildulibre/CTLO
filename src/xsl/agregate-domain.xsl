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
	<xsl:param name="ref-filename"/>
	<xsl:variable name="ref-file"	select="document($ref-filename)"/>

	<!-- Copy all nodes from here.  -->
    	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>	
		</xsl:copy>
    	</xsl:template>

	<xsl:template match="anglicisme">
		<xsl:variable name="id" select="@id"/> 
			<xsl:choose>
				<xsl:when test="count($ref-file/anglicismes/anglicisme[@id = $id]) > 1"> 
<!--
<xsl:message>Anglicisme en doublon <xsl:value-of select="$id"/> (<xsl:value-of select="count($ref-file/anglicismes/anglicisme[@id = $id])"/>).</xsl:message>
-->
					<xsl:element name="anglicisme">
						<xsl:attribute name="id">
							<xsl:value-of select="$id"/>
						</xsl:attribute>
						<xsl:element name="domaines">
							<xsl:for-each select="$ref-file/anglicismes/anglicisme[@id = $id]">
								<xsl:for-each select="domaines/domaine">
<!--
<xsl:message>Adding domain :<xsl:value-of select="@id"/>, for anglicism  <xsl:value-of select="$id"/>.</xsl:message>
-->
									<xsl:copy>
										<xsl:apply-templates select="@*|node()"/>	
									</xsl:copy>
								</xsl:for-each>  
							</xsl:for-each>
						</xsl:element>
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
<!--					<xsl:message>Pas de doublons pour <xsl:value-of select="$id"/>.</xsl:message>	-->
					<xsl:copy>
						<xsl:apply-templates select="@*|node()"/>	
					</xsl:copy>
				</xsl:otherwise>
			</xsl:choose><!--
		</xsl:element> -->
	</xsl:template>
</xsl:stylesheet>
