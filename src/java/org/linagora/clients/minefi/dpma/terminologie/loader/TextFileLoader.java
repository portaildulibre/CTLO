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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.linagora.clients.minefi.dpma.terminologie.AnglicismeThesaurusException;
import org.linagora.clients.minefi.dpma.terminologie.XMeaningThesaurus;

import com.sun.star.linguistic2.XMeaning;

/**
 * @author Romain PELISSE, romain.pelisse@atosorigin.com
 *
 */
public class TextFileLoader extends AbstractLoader {

	/**
	 * Basic constructor
	 * @param filepath to the datafile inside the packaged jar
	 */
	public TextFileLoader(String filepath) {
		super(filepath);
	}
		
	/*
	 * Parse text file and load data from it. 
	 */
	@Override
	protected void extractFromFile(InputStream dataInputStream, Map data) throws AnglicismeThesaurusException {
		String line;
		String index;
		String[] tmp;
		int count;
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(dataInputStream, "UTF-8"));
			// Read first line (encoding)
			super.setEncoding(buffer.readLine());
			// now parse the remaining lines of the index
			// Read through file one line at time.
			while ((line = buffer.readLine()) != null) {
				if (-1 != line.indexOf("|")) {
					tmp = line.split("\\|");
					index = tmp[0];
					count = Integer.parseInt(tmp[1]);
					XMeaning[] list = new XMeaningThesaurus[count];
					// on prend les n sens différents du mot en cours
					for (int i = 0; i < count; i++) {
						line = buffer.readLine();
						String[] tmp2 = new String[] {};
						tmp2 = line.split("\\|");
						line = tmp2[0];
						String[] tmp3 = new String[tmp2.length - 1];
						System.arraycopy(tmp2, 1, tmp3, 0, tmp2.length - 1);
						list[i] = new XMeaningThesaurus(line, tmp3);
					}
					data.put(index, list);
				} else {
					buffer.close();
					throw new AnglicismeThesaurusException("Bad data file format at line '" + line + "' in file '"+ filepath +"' included in jar archive.");
				}
			}
			buffer.close();
		}
		catch (java.io.IOException e) {
			throw new AnglicismeThesaurusException("Can't access file" + filepath,e);
		}
	}
	
}
