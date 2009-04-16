<?xml version="1.0" encoding="UTF-8"?>
	<!--
		This script removes all excluded terms from the term databases. Note
		that it use a Java extension.
	-->
<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:str="http://exslt.org/strings"
	xsl:exclude-result-prefixes="#all"
	>
   <import href="functions/tokenize/str.tokenize.xsl"/>
	
	

	<xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes" />
	<xsl:preserve-space elements="*"/>

	<!-- Defining the root of the new XML document -->
	<xsl:template match="/">
		<rules lang="fr">
			<category name="TestTerminologie">
				<xsl:apply-templates select="@*|node()" />
			</category>
		</rules>
	</xsl:template>

	<xsl:template match="anglicisme">
		<xsl:variable name="term" select="@id" />
		<xsl:variable name="node" select="child::node()" />
		<rule id="{@id}" name="Terminologie «&#160;{@id}&#160;»">
			<pattern>
				<xsl:for-each select="str:tokenize(@id,' ')">
					<token><xsl:value-of select="."/></token>
				</xsl:for-each>
			</pattern>
			<message>
				<xsl:text>«&#160;</xsl:text><xsl:value-of select="@id"/><xsl:text>&#160;» est une terminologie.&#13;</xsl:text> 
				<xsl:text>Employez</xsl:text>
				<xsl:for-each select="domaines/domaine">
					<xsl:text>&#13;pour le domaine «&#160;</xsl:text><xsl:value-of select="@id"/><xsl:text>&#160;»&#160;:&#13;</xsl:text>
					<xsl:for-each select="synonymes/synonyme">
						<xsl:text>&#x2022;</xsl:text><suggestion><xsl:value-of select="."/></suggestion><xsl:text>&#13;</xsl:text>
					</xsl:for-each>
				</xsl:for-each>
			</message>
			<!-- 
		      <example type="incorrect"><marker>comme par exemple</marker></example>
		      <example type="correct"><marker>par exemple</marker></example>
		    -->
    </rule>		
	</xsl:template>
</xsl:stylesheet>
