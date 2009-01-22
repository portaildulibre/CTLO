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
package org.linagora.clients.minefi.dpma.terminologie.loader.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.linagora.clients.minefi.dpma.terminologie.XMeaningThesaurus;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.star.linguistic2.XMeaning;

/**
 * @author Romain PELISSE, romain.pelisse@atosorigin.com
 *
 */
public class XMLDictionnaryHandler extends DefaultHandler {

	private static final int AVERAGE_MAX_NB_DOMAIN_FOR_ONE_MEANING = 5;
	private static final int AVERAGE_MAX_NB_SYNONYMS_FOR_ONE_MEANING = 5;

	private static final String ID_ATTRIBUTE = "id";
	private static final String ANGLICISM_TAG = "anglicisme";
	private static final String DOMAINS_TAG = "domaines";
	private static final String DOMAIN_TAG = "domaine";
	private static final String SYNONYMES_TAG = "synonymes";
	private static final String SYNONYME_TAG = "synonyme";
	
	private String currentAnglicism = null;
	private String currentDomainId = null;
	private String currentSynonyme;
	private boolean insideSynonyme = false;

	private List<String> currentSynonymesList = new ArrayList<String>(AVERAGE_MAX_NB_SYNONYMS_FOR_ONE_MEANING);
	private List<XMeaning> currentDomainList = new ArrayList<XMeaning>(AVERAGE_MAX_NB_DOMAIN_FOR_ONE_MEANING);
	
	private Map<String,XMeaning[]> resultingMap;
	
	/**
	 * @return the resultingMap
	 */
	public Map getResultingMap() {
		return resultingMap;
	}

	/**
	 * @param resultingMap the resultingMap to set
	 */
	public void setResultingMap(Map resultingMap) {
		this.resultingMap = resultingMap;
	}
	
    /**
     * Receive notification of the start of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the start of
     * each element (such as allocating a new tree node or writing
     * output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
	@Override
    public void startElement (String uri, String localName,String qName, Attributes attributes) throws SAXException {
		if ( ANGLICISM_TAG.equals(qName) ) {
			insideSynonyme = false; // we never know...
			currentAnglicism = attributes.getValue(ID_ATTRIBUTE).toLowerCase();
		} else if ( DOMAINS_TAG.equals(qName) ) {
			currentDomainList.clear();							// Most likely to be useless
		} else if ( DOMAIN_TAG.equals(qName) ) {
			currentDomainId = attributes.getValue(ID_ATTRIBUTE);
		} else if ( SYNONYMES_TAG.equals(qName) ) {		
			currentSynonymesList.clear();						// Most likely to be useless
		} else if ( SYNONYME_TAG.equals(qName) ) {
			insideSynonyme = true;
		}
    }
	
	
    /**
     * Receive notification of character data inside an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method to take specific actions for each chunk of character data
     * (such as adding the data to a node or buffer, or printing it to
     * a file).</p>
     *
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the
     *               character array.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#characters
     */
	@Override
    public void characters (char characters[], int start, int length) throws SAXException
    {
    	if ( insideSynonyme ) {
    		currentSynonyme = new String(characters,start,length);
    	}
    }
	
    /**
     * Receive notification of the end of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the end of
     * each element (such as finalising a tree node or writing
     * output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
	@Override
    public void endElement (String uri, String localName, String qName) throws SAXException {
		if ( ANGLICISM_TAG.equals(qName) ) {
			// Add to map 
			this.resultingMap.put(currentAnglicism,(XMeaning[]) currentDomainList.toArray(new XMeaningThesaurus[currentDomainList.size()])); 
			// Reset to current 'anglicism' to null
			currentAnglicism = "";
			currentDomainList.clear();
//		} else if ( DOMAINS_TAG.equals(qName) ) { // nothing to do here, commented out for performance 
//			// We've done with the domains associated with the anglicism's synonyms
		} else if ( DOMAIN_TAG.equals(qName) ) {
			// We've done with the current domain
			currentDomainId = "";
		} else if ( SYNONYMES_TAG.equals(qName) ) {
			// Ok, we gathered all synonyms for one domain, let's add them to the current domains list
			currentDomainList.add(new XMeaningThesaurus(currentDomainId,(String[]) currentSynonymesList.toArray(new String[currentSynonymesList.size()])));
			currentSynonymesList.clear();
		} else if ( SYNONYME_TAG.equals(qName) ) {
			// We've done with the current synonym
			insideSynonyme = false;
			// Add the current domain synonyme list
			currentSynonymesList.add(currentSynonyme);
		} 
    }
	
}
