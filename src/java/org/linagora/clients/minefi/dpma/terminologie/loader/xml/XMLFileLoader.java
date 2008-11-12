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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.linagora.clients.minefi.dpma.terminologie.loader.AbstractLoader;
import org.xml.sax.SAXException;

/**
 * @author Romain PELISSE, romain.pelisse@atosorigin.com
 *
 */
public class XMLFileLoader extends AbstractLoader {

	/**
	 * Basic constructor,
	 * @param filepath to the datafile inside the packaged jar
	 */
	public XMLFileLoader(String filepath) {
		super(filepath);
	}
	
	/*
	 * Parse the XML input file and load the data from it.
	 * XML parsing is preferred as XML abstraction layer provides ensure
	 * minimal encoding issue.
	 * Also, it allow to directly hierarchical data from the XML files 
	 * to build the proper object for OO. 
	 * Note that as we use SAX, rather than DOM, the loading process is
	 * not so memory consuming. 
	 *
	 */
	@Override
	protected void extractFromFile(InputStream dataInputStream,Map data) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		XMLDictionnaryHandler xmlHandler = new XMLDictionnaryHandler();
		xmlHandler.setResultingMap(data);
		try {
			factory.newSAXParser().parse(dataInputStream, xmlHandler);
		} catch (SAXException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
				
	}

}
