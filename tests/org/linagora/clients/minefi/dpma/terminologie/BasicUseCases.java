package org.linagora.clients.minefi.dpma.terminologie;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.Locale;
import com.sun.star.linguistic2.XMeaning;

public class BasicUseCases extends AbstractAnglicismeThesaurusTest {


	@Test
	public void testGetLocales() {

		Locale[] ls = super.getLocales();
		assertTrue(ls != null && ls.length > 0);
	}
	
	@Test
	public void invalideArguments() {
		try {
			XMeaning[] results = getSynonyme(null);
			assertNotNull(results);
			assertTrue(results.length == 0);
			results = getSynonyme("");
			assertNotNull(results);
			assertTrue(results.length == 0);
		} catch (IllegalArgumentException e) {
			fail();
		} catch (RuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void testGetSynonyme1() throws IllegalArgumentException, RuntimeException {
		XMeaning[] res = getSynonyme("spam");
		// No entry in dictionnary on 'spam'
		assertTrue(res != null && res.length == 0);
	}
	
	@Test
	public void testGetSynonyme2() throws IllegalArgumentException, RuntimeException {
		XMeaning[] results = getSynonyme("hit-and-run");
		assertTrue(results != null && results.length > 0);
	}
	
	@Test
	public void testGetSynonyme3() throws IllegalArgumentException, RuntimeException {
		XMeaning[] results = getSynonyme("e-mail");
		assertTrue(results != null && results.length >= 2);
	}
}
