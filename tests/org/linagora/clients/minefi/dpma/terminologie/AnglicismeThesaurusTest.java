/*
Correcteur terminologique - éradication des anglicismes.
Copyright (C) 2006 Linagora SA - Manuel Odesser modesser@linagora.com

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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.sun.star.beans.PropertyValue;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.Locale;
import com.sun.star.linguistic2.XMeaning;

public class AnglicismeThesaurusTest extends AnglicismeThesaurus {

	@Test
	public void testGetLocales() {

		Locale[] ls = super.getLocales();
		assertTrue(ls != null && ls.length > 0);
	}
	
	@Test
	public void testGetSynonyme1() throws IllegalArgumentException, RuntimeException {
		AnglicismeThesaurus aT = new AnglicismeThesaurus();
		Locale[] ls = aT.getLocales();
		PropertyValue[] emptyArgs = new PropertyValue[] {};
		XMeaning[] res = aT.queryMeanings("hit-and-run",ls[0],emptyArgs);
		assertTrue(res != null && res.length > 0);
	}
	
	@Test
	public void testGetSynonyme2() throws IllegalArgumentException, RuntimeException {
		AnglicismeThesaurus aT = new AnglicismeThesaurus();
		Locale[] ls = new AnglicismeThesaurus().getLocales();
		PropertyValue[] emptyArgs = new PropertyValue[] {};
		XMeaning[] res = aT.queryMeanings("spam",ls[0],emptyArgs);
		// No entry in dictionnary on 'spam'
		assertFalse(res != null && res.length > 0);
	}
	
	@Test
	public void testGetSynonyme3() throws IllegalArgumentException, RuntimeException {
		AnglicismeThesaurus aT = new AnglicismeThesaurus();
		Locale[] ls = new AnglicismeThesaurus().getLocales();
		PropertyValue[] emptyArgs = new PropertyValue[] {};
		XMeaning[] res = aT.queryMeanings("e-mail",ls[0],emptyArgs);
		assertTrue(res != null && res.length > 2);
	}
	
}
