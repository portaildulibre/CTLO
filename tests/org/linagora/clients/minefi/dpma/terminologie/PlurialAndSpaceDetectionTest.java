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
package org.linagora.clients.minefi.dpma.terminologie;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.linguistic2.XMeaning;

/**
 * @author Romain PELISSE, romain.pelisse@atosorigin.com
 *
 */
public class PlurialAndSpaceDetectionTest extends AbstractAnglicismeThesaurusTest {

	@Test
	public void pluralForm() throws IllegalArgumentException, RuntimeException {
		XMeaning[] results = super.getSynonyme("binary digits");
		assertNotNull(results);
		assertTrue(results.length > 0);
	}
	
	@Test
	public void alternativesForms() throws IllegalArgumentException, RuntimeException {
		XMeaning[] results = super.getSynonyme("wild-card");
		assertNotNull(results);
		assertTrue(results.length > 0);
		results = super.getSynonyme("data-bank");
		assertNotNull(results);
		assertTrue(results.length > 0);
	}

	@Test
	public void alternativesFormAndPluralForm() throws IllegalArgumentException, RuntimeException {
		XMeaning[] results = super.getSynonyme("binary-digits");
		assertNotNull(results);
		assertTrue(results.length > 0);	
	}
}
