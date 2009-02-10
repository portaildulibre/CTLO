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

	<xsl:template match="domaines">
		<xsl:element name="domaines">
			<xsl:for-each select="domaine">
				<xsl:variable name="id" select="@id"/>
				<xsl:if test="(count(./following-sibling::domaine[@id = $id]) = 0) and (count(./preceding-sibling::domaine[@id = $id]) = 0) ">
					<xsl:copy>
						<xsl:apply-templates select="@*|node()"/>	
					</xsl:copy>
				</xsl:if>
			</xsl:for-each>
				
			<xsl:for-each select="domaine">
				<xsl:variable name="id" select="@id"/>
				<xsl:if test="(count(./following-sibling::domaine[@id = $id]) > 0) and (count(preceding-sibling::domaine[@id = $id]) = 0)">
					<xsl:element name="domaine">
						<xsl:attribute name="id">
							<xsl:value-of select="$id"/>
						</xsl:attribute>
						<xsl:element name="synonymes">
							<xsl:for-each select="synonymes/synonyme">
								<xsl:copy>
									<xsl:apply-templates select="@*|node()"/>	
								</xsl:copy>
							</xsl:for-each>
							<xsl:for-each select="./following-sibling::domaine[@id = $id]/synonymes/synonyme">
								<xsl:copy>
									<xsl:apply-templates select="@*|node()"/>	
								</xsl:copy>
							</xsl:for-each>
						</xsl:element>
					</xsl:element>
				</xsl:if>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>