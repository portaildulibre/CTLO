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
package org.linagora.clients.minefi.dpma.terminologie.loader;

import java.io.InputStream;
import java.util.Map;

import org.linagora.clients.minefi.dpma.terminologie.AnglicismeThesaurus;
import org.linagora.clients.minefi.dpma.terminologie.AnglicismeThesaurusException;

/**
 * <p>An abstract class to regroup methods and property in common 
 * to all parser (text based or XML based).</p>
 * 
 * 
 * @author Romain PELISSE, romain.pelisse@atosorigin.com
 *
 */
public abstract class AbstractLoader implements Loader  {

	protected String filepath;
	private String encoding;
	
	public AbstractLoader(String filepath) {
		this.filepath = filepath;
	}
	
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Map loadDictionnary(Map data) throws AnglicismeThesaurusException {
		InputStream dataInputStream = AnglicismeThesaurus.class.getResourceAsStream(filepath);
		// open the data file
		if(dataInputStream == null) {
			throw new AnglicismeThesaurusException("Could not open data file '"+ filepath +"'. The file was not found in the application classpath.");
		}
		extractFromFile(dataInputStream,data);
		return null; // FIXME:shouldn't return data here ?
	}
	
	protected abstract void extractFromFile(InputStream dataInputStream, Map data) throws AnglicismeThesaurusException;

}
