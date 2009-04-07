/**
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
 */
package org.atosorigin.clients.minefi.dpma;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Analyse d'un fichier XML.
 * 
 * @author Philippe Prados
 */
class XMLDictionnaryHandler extends DefaultHandler
{

	/** Nb moyen de domaines. */
	private static final int AVERAGE_MAX_NB_DOMAIN_FOR_ONE_MEANING = 5;
	/** Nb moyen de synonymes. */
	private static final int AVERAGE_MAX_NB_SYNONYMS_FOR_ONE_MEANING = 5;

	private static final String ID_ATTRIBUTE = "id";

	private static final String ANGLICISM_TAG = "anglicisme";

//	private static final String DOMAINS_TAG = "domaines";

	private static final String DOMAIN_TAG = "domaine";

//	private static final String SYNONYMES_TAG = "synonymes";

	private static final String SYNONYME_TAG = "synonyme";

	private String anglicismId = null;

	/** Le domaine à construire. */
	private Domaines.Domaine currentDomain = new Domaines.Domaine();

	/** Le dernier synonyme trouvé. */
	private String currentSynonyme;

	/* Flag à true si dans un synonyme. */
	private boolean insideSynonyme = false;

	/** La liste des synonymes. */
	private List<String> synonymes = new ArrayList<String>(AVERAGE_MAX_NB_SYNONYMS_FOR_ONE_MEANING);

	/** La liste des domaines. */
	private List<Domaines.Domaine> domaines = new ArrayList<Domaines.Domaine>(
			AVERAGE_MAX_NB_DOMAIN_FOR_ONE_MEANING);

	/** Derniers domaines générés. Permet la détection de doublons. */
	private List<Domaines> lastDomaines=new ArrayList<Domaines>();
	
	/** Le parseur à initialiser. */
	private WordsParser top;

	/** Constructeur. */
	XMLDictionnaryHandler(final WordsParser top)
	{
		this.top=top;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException
	{
		if (SYNONYME_TAG.equals(qName)) // Le plus souvent en premier.
		{
			insideSynonyme = true;
		}
		else if (DOMAIN_TAG.equals(qName))
		{
			currentDomain.domaine = attributes.getValue(ID_ATTRIBUTE);
		}
		else if (ANGLICISM_TAG.equals(qName))
		{
			insideSynonyme = false; // we never know...
			anglicismId = attributes.getValue(ID_ATTRIBUTE).toLowerCase();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void characters(char characters[], int start, int length) throws SAXException
	{
		if (insideSynonyme)
		{
			currentSynonyme = new String(characters, start, length);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if (SYNONYME_TAG.equals(qName)) // Le plus souvent en premier.
		{
			// We've done with the current synonym
			insideSynonyme = false;
			// Add the current domain synonyme list
			synonymes.add(currentSynonyme);
		}
		else if (DOMAIN_TAG.equals(qName))
		{
			currentDomain.synonymes=synonymes.toArray(new String[synonymes.size()]);
			domaines.add(currentDomain);
			// Prepare the next step
			synonymes.clear();
			currentDomain=new Domaines.Domaine();
		}
		else if (ANGLICISM_TAG.equals(qName))
		{
			Domaines newDomains=new Domaines();
			newDomains.domaines=domaines.toArray(new Domaines.Domaine[domaines.size()]);
			if (!lastDomaines.contains(newDomains)) // Detect double
			{
					top.add(newDomains,anglicismId);
					lastDomaines.add(newDomains);
			}
			// Prepare the next step
			domaines.clear();
			anglicismId=null;
		}
	}

}
