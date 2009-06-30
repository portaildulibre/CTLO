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

  <!-- Copy all nodes from here.  -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <!--  -->
  <xsl:template match="/">
    <html>
      <head>
<title>Rapport de génération de la table de correspondance</title>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
<meta content="text/css" http-equiv="Content-Style-Type" /><title>FranceTerme</title><link type="text/css" rel="stylesheet" href="http://franceterme.culture.fr/FranceTerme/style-accueil.css" />
      </head>
      <body>
        <h2>Information générales</h2>
        <p>
        La table de correspondance contient :
        <ul>
          <li><xsl:value-of select="count(//anglicismes/anglicisme)"/> termes ;</li>
          <li><xsl:value-of select="count(//synonyme)"/> synonymes.</li> 
        </ul>
        </p>
        <table>
          <tr><!--
            <td valign="top">
        <h2>Bilan des termes exclus:</h2>
          <p><xsl:value-of select="count(//log-entry[@type='exclusions']/log-item)"/> terme(s) exclu(s).</p>
          <ol>
        <xsl:for-each select="//log-entry[@type='exclusions']">
            <xsl:for-each select="log-item">
              <li><xsl:value-of select="current()"/></li>
            </xsl:for-each>
        </xsl:for-each>
          </ol>
          </td>
-->
            <xsl:call-template name="log-section">
              <xsl:with-param name="log-type" select="'homographie'"/>
              <xsl:with-param name="title-message" select="'Bilan des termes exclus'"/>
              <xsl:with-param name="count-message" select="'terme(s) exclu(s)'"/>
            </xsl:call-template>


<!--
          <td valign="top">
        <h2>Homographies retirées:</h2>
          <p><xsl:value-of select="count(//log-entry[@type='homographie']/log-item)"/> homographie(s) retirée(s)</p>
          <ol>
            <xsl:for-each select="//log-entry[@type='homographie']">
              <xsl:for-each select="log-item">
                <li><xsl:value-of select="current()"/></li>
              </xsl:for-each>
            </xsl:for-each>
          </ol>
          </td> -->  
            <xsl:call-template name="log-section">
              <xsl:with-param name="log-type" select="'homographie'"/>
              <xsl:with-param name="title-message" select="'Homographies retirées'"/>
              <xsl:with-param name="count-message" select="'homographie(s) retirée(s)'"/>
            </xsl:call-template>


            <xsl:call-template name="log-section">
              <xsl:with-param name="log-type" select="'inclusions'"/>
              <xsl:with-param name="title-message" select="'Bilan des termes ajoutés'"/>
              <xsl:with-param name="count-message" select="'terme(s) ajouté(s)'"/>
            </xsl:call-template>

          </tr>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template name="log-section">
    <xsl:param name="log-type"/>
    <xsl:param name="title-message"/>
    <xsl:param name="count-message"/>
    <td valign="top">
    <h2><xsl:value-of select="$title-message"/> :</h2>
      <p>
        <xsl:value-of select="count(//log-entry[@type=$log-type]/log-item)"/><xsl:value-of select="concat(' ',$count-message)"/>.
      </p>
      <ol>
        <xsl:for-each select="//log-entry[@type=$log-type]">
          <xsl:for-each select="log-item">
            <li><xsl:value-of select="current()"/></li>
          </xsl:for-each>
        </xsl:for-each>
      </ol>
    </td>
  </xsl:template>

  <xsl:template name="anglicisme">
  </xsl:template>

  <xsl:template name="domaines">
  </xsl:template>

  <xsl:template name="domaine">
  </xsl:template>

  <xsl:template name="synonymes">
  </xsl:template>

  <xsl:template name="synonyme">
  </xsl:template>

</xsl:stylesheet>
