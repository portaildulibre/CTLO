<?xml version="1.0" encoding="UTF-8"?>
<!--
 Correcteur terminologique - éradication des anglicismes.

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
    
    <xsl:param name="inclusion-file"/>
    <xsl:param name="ref-filename"/>
    
    <xsl:variable name="inclusions"      select="document($inclusion-file)"/>
    <xsl:variable name="ref-file"        select="document($ref-filename)"/>

    <xsl:variable name="NOTHING-TO-ADD"    select="'nothing-to-add'"/> 

    <!-- Copy all nodes from here.  -->
        <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>    
        </xsl:copy>
        </xsl:template>

    <!-- Defining the root of the new XML document -->
    <xsl:template match="/anglicismes">
        <xsl:variable name="nb-include-terms"    select="count($inclusions//include)"/>
        <xsl:element name="anglicismes">
<log-entry type="inclusions">
  <h2>Termes ajoutés à la table de correspondance</h2>
  <p>Nombre de termes à inclure: <xsl:value-of select="count($inclusions/includes/include)"/>.</p>
  <p>Nombre de termes existants: <xsl:value-of select="count($ref-file/anglicismes/anglicisme)"/>.</p>
</log-entry>
            <xsl:apply-templates select="@*|node()"/>

        <xsl:for-each select="$inclusions/includes/include">
            <xsl:variable name="anchor" select="@anchor"/>
            <xsl:variable name="term-to-include" select="text()"/>
            <xsl:choose>
                <xsl:when test="count($ref-file/anglicismes/anglicisme/domaines/domaine/synonymes/synonyme[text() = $anchor]) > 0">
                <!-- one item 'for each' -->
                <xsl:for-each select="$ref-file/anglicismes/anglicisme/domaines/domaine/synonymes/synonyme[text() = $anchor]">
                    <!-- Ok we duplicate this entry -->
		    <xsl:if test="position() = 1">
                         <xsl:element name="anglicisme">
                             <xsl:attribute name="id"><xsl:value-of select="$term-to-include"/></xsl:attribute>
                             <xsl:message>
			     	<xsl:value-of select="name(../../..)"/>
			     </xsl:message>
			     <xsl:copy-of select="../../.."/>
                         </xsl:element>
<log-entry type="inclusions"><log-item><b><xsl:value-of select="$term-to-include"/></b> (variante du terme <i><xsl:value-of select="$anchor"/></i>)</log-item></log-entry>
		    </xsl:if>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                 <log-entry type="inclusions"><div style="color:red;"><p>Impossible d'include le terme '<xsl:value-of select="$term-to-include"/>' car la forme terminologique qui lui est associée, '<xsl:value-of select="$anchor"/>' n'existe pas !</p></div></log-entry> 
            </xsl:otherwise>
        </xsl:choose>
        </xsl:for-each>
      </xsl:element>
    </xsl:template>
</xsl:stylesheet>
