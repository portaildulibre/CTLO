/*
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

package org.linagora.clients.minefi.dpma.terminologie;

import com.sun.star.linguistic2.XMeaning;

/**
 * TODO
 * 
 * @author Romain PELISSE, romain.pelisse@atosorigin.com
 *
 */
public final class XMeaningThesaurus implements XMeaning
{
	String aMeaning;
	String[] aSynonyms;
	
	/**
	 *  Default constructor
	 *  
	 * @param aMeaning
	 * @param aSynonyms
	 */
	public XMeaningThesaurus ( String aMeaning, String[] aSynonyms )
	{
		this.aMeaning = aMeaning;
		this.aSynonyms = aSynonyms;
		
		//!! Aucun de ces deux tests ne devrait arriver. 
		//!! Les valeurs données sont là pour sécurité.
		if (this.aMeaning == null)
			this.aMeaning = new String();
		
		// Un sens sans synonymes devrait être valide.
		// Cependant, on place un tableau vide par sécurité.
		if (this.aSynonyms == null)
			this.aSynonyms = new String[]{};
	}
	
	/*
	 * @see com.sun.star.linguistic2.XMeaning#getMeaning()
	 */
	public String getMeaning() throws com.sun.star.uno.RuntimeException
	{
		return aMeaning;
	}
	
	/*
	 * @see com.sun.star.linguistic2.XMeaning#querySynonyms()
	 */
	public String[] querySynonyms() throws com.sun.star.uno.RuntimeException
	{
		return aSynonyms;
	}
	
	/**
	 * Overriding default implementation of toString to provide clearer output
	 * while debugging. 
	 * Note that this method should NOT be used to realize any business
	 * action !
	 * 
	 * @return a "string" representation of this component (a list of synonyms)
	 */
	@Override
	public String toString() {
		String synonyms = ", synomyms:";
		for (int idSynonym = 0; idSynonym < aSynonyms.length ; idSynonym++ )
			synonyms += aSynonyms[idSynonym] + "|";
		return this.aMeaning + synonyms;
	}
};