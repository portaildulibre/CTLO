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
	This script adapts FranceTerme syntax to the simplified syntax used
	by the plugin:
	
<anglicismes>
	<anglicisme id="PCR method">
		<domaines>
			<domaine id="Biochimie et biologie moléculaire">
				<synonymes>
					<synonyme>amplification en chaîne par polymérase</synonyme>
				</synonymes>
			</domaine>
			<domaine id="Génétique">
				<synonymes>
					<synonyme>amplification en chaîne par polymérase</synonyme>
				</synonymes>
			</domaine>
		</domaines>
	</anglicisme>
	...
-->
<xsl:stylesheet version="1.0" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>

	<!-- Defining the root of the new XML document -->
	<xsl:template match="/">
		<xsl:variable name="total-angliscim-no-variation"	select="count(//Article/Equivalent[(@langue = 'en')])"/>
		<xsl:variable name="addons-variations"			select="count(//Article/Equivalent[@langue = 'en']/variante)"/>
		<xsl:variable name="sum-anglicism"			select="$total-angliscim-no-variation + $addons-variations"/>

		<anglicismes>
			<xsl:attribute name="nb"><xsl:value-of select="$sum-anglicism"/></xsl:attribute>		
			<xsl:apply-templates/>
		</anglicismes>
		<xsl:message>
Nombre total d'anglicisme (sans leurs variantes): <xsl:value-of select="$total-angliscim-no-variation"/>
Nombre total de variantes: <xsl:value-of select="$addons-variations"/>
TOTAL Anglicismes:<xsl:value-of select="$sum-anglicism"/>
		</xsl:message>
	</xsl:template>

	<!--
		For each article we select each Equivalent in english. Each one of those will be the top level entry 
		of our new XML document.
	-->
	<xsl:template match="Article">
		<xsl:for-each select="Equivalent[(@langue = 'en')]">
			<xsl:variable name="anglicisme">
				<xsl:call-template name="recursive-text-assembling">
					<xsl:with-param name="text-position"
							select="1"/>
					<xsl:with-param name="current-string"
							select="string('')"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:element name="anglicisme">
				<xsl:attribute 	name="id"><xsl:value-of select="$anglicisme"/></xsl:attribute>
				<xsl:choose>	
					<xsl:when test="count(../Domaine) > 0">
						<xsl:call-template name="make-domaines"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:message>
						Anglicisme sans domaine: <xsl:value-of select="$anglicisme"/>
						</xsl:message>
					</xsl:otherwise>
				</xsl:choose> 
			</xsl:element> 

			<xsl:for-each select="variante">
				<xsl:variable name="variante" select="."/>
				<xsl:message>Ajout de la variante <xsl:value-of select="string(normalize-space(text()))"/> pour <xsl:value-of select="$anglicisme"/></xsl:message>
				<xsl:element name="anglicisme">
					<xsl:attribute 	name="id"><xsl:value-of select="string(normalize-space(text()))"/></xsl:attribute>
					<xsl:element name="domaines">
						<xsl:for-each select="../../Domaine/Dom">
							<xsl:call-template name="make-domain"/>
						</xsl:for-each>
					</xsl:element>
				</xsl:element>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	
	<!--
		Functions area:
		- - - - - - - -

		Below this limit there is only template called (~ functions)
	-->
	
	<!-- No real need to have a template here, but it does help making
		this stylesheet more human-readable... -->
	<xsl:template name="make-domaines">
		<xsl:element name="domaines">
			<xsl:for-each select="../Domaine/Dom">
				<xsl:call-template name="make-domain"/>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>

	<xsl:template name="make-domain">
		<xsl:element name="domaine">
			<xsl:attribute name="id">
				<xsl:value-of select="string(normalize-space(text()))"/>
			</xsl:attribute>
			<xsl:element name="synonymes">
				<xsl:if 	test="count(../../Terme_Vedette) > 0">
					<xsl:for-each 		select="../../Terme_Vedette">
						<xsl:call-template name="make-synonyme"/>
					</xsl:for-each>
				</xsl:if>
				<xsl:if 	test="count(../../Synonyme) > 0">
					<xsl:for-each 	select="../../Synonyme">
						<xsl:call-template name="make-synonyme"/>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<!-- 
		Make an <synonyme> entry based on the current node data.
		Therefore, the following code works on both <Synonyme> and <Terme_Vedette>
		entry.
	-->
	<xsl:template name="make-synonyme">

		<!-- Regrouping all possible text node into a normalized string -->
		<xsl:variable name="assemble-text">
                        <xsl:call-template name="recursive-text-assembling">
                                <xsl:with-param name="text-position"
                                                select="1"/>
                                <xsl:with-param name="current-string"
                                                select="string('')"/>
                        </xsl:call-template>
                </xsl:variable>

		<!-- Let remove alternative (useless) data-->	
		<xsl:variable name="final-form">
			<xsl:choose>
				<xsl:when test="contains($assemble-text,',')">
					<!-- removing the alternative form (if any) -->
					<xsl:value-of select="substring-before($assemble-text,',')"/>	
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$assemble-text"/>
				</xsl:otherwise>
			</xsl:choose>		
		</xsl:variable>		

		<xsl:element name="synonyme">
			<xsl:value-of select="$final-form"/>
		</xsl:element>			
		
	</xsl:template>


	<!-- TODO: Maybe there is a more conventional way to do this ? -->
        <xsl:template name="recursive-text-assembling">
                <xsl:param name="text-position"/>
                <xsl:param name="current-string"/>

                <xsl:variable   name="text-content"
                                select="text()[position() = $text-position]"/>
                <xsl:choose>
                        <xsl:when test="string-length($text-content) > 0">
                                <xsl:variable   name="product"
                                                select="concat($current-string,normalize-space($text-content))"/>
                                <xsl:call-template name="recursive-text-assembling">
                                        <xsl:with-param name="text-position"
                                                        select="number($text-position) + 1"/>
                                        <xsl:with-param name="current-string"
                                                        select="$product"/>
                                </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                                <xsl:value-of select="$current-string"/>
                        </xsl:otherwise>
                </xsl:choose>
        </xsl:template>



</xsl:stylesheet>
