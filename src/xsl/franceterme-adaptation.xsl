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
	
	<terme id="anglicisme">
		<domaine name="nom du domaine">
			<equivalent>alternatif</equivalent>	
			<equivalent>alternatif</equivalent>	
		</domaine>
		<domaine name="nom du domaine">
			<equivalent>alternatif</equivalent>	
			<equivalent>alternatif</equivalent>	
		</domaine>
	</terme>
-->
<xsl:stylesheet version="1.0" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	

	<!-- Copy all nodes from here. 
    	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>	
		</xsl:copy>
    	</xsl:template>
-->
	<!-- We only look for 'anglicisme', therefore we only keep the following node.
Assembling the XML datafile is a bit tricky as we want to index every english term that are in fact not unique inside the FranceTerme file (basicly, there can two different <Article> that have the same term. (ID and ID_REF attribute in XML technology are designed to answer those kind of issue, however they have not been used for FranceTerme designed).

So, the process to create our database is recursive:

1. First term is encounter, we store it (with its metadata) as an xml structure, inside an xsl:variable.

2. We move to the second term, we check that this new term is not in our variable:
2.1 If not we add it, redefining a new variable
2.2 The term is already present, we add the (maybe) new metadata

3. Keep going on...
	-->

	<xsl:template match="/">
		<anglicismes>		
			<xsl:apply-templates/>
		</anglicismes>
		<xsl:message>
TOTAL Anglicismes:<xsl:value-of select="count(//Article/Equivalent[@langue = 'en'])"/>
		</xsl:message>
	</xsl:template>

	<xsl:template match="Article">
	
		<xsl:for-each select="Equivalent[(@langue = 'en')]">
			<!-- FIXME: Gérer les variantes !!! Trouver comment récupérer le text et non les fils !-->
			<xsl:variable name="anglicisme" select="string(normalize-space(text()))"/>
			<xsl:if test="count(variante) = 0">
				<xsl:element name="anglicisme">
					<xsl:attribute 	name="id"><xsl:value-of select="$anglicisme"/></xsl:attribute>
					<xsl:choose>	
						<xsl:when test="count(../Domaine) > 0">
							<xsl:element name="domaines">
								<xsl:for-each select="../Domaine/Dom">
									<xsl:element name="domaine">
										<xsl:attribute name="id">
											<xsl:value-of select="string(text())"/>
										</xsl:attribute>
										<xsl:element name="synonymes">
											<!--FIXME: Gérer les variantes ici aussi ! -->
											<xsl:if 	test="count(../../Terme_Vedette) > 0">
												<xsl:for-each 		select="../../Terme_Vedette">
													<xsl:element name="synonyme">
														<xsl:value-of select="string(normalize-space(text()))"/>
													</xsl:element>
												</xsl:for-each>
											</xsl:if>
											<xsl:if 	test="count(../../Synonyme) > 0">
												<xsl:for-each 	select="../../Synonyme">
													<xsl:element name="synonyme">
														<xsl:value-of select="string(normalize-space(text()))"/>
													</xsl:element>
												</xsl:for-each>
											</xsl:if>
							</xsl:element>
									</xsl:element>
								</xsl:for-each>
							</xsl:element>	

							
						</xsl:when>
						<xsl:otherwise>
							<xsl:message>
							Anglicism sans domaine: <xsl:value-of select="$anglicisme"/>
							</xsl:message>
						</xsl:otherwise>
					</xsl:choose> 
				</xsl:element> 
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
