package org.linagora.clients.minefi.dpma.terminologie;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AnglicismeThesaurusTest {

	private XMeaningThesaurus meaning;
	
	@Before
	public void setUp() throws Exception {
		String[] synonyms = new String[2];
		synonyms[0] = "restaurer";
		synonyms[1] = "réinitialiser";
		this.meaning = new XMeaningThesaurus("Informatique",synonyms);
	}
	
	@Test
	public void testEquals() {
		
		String[] synonyms = new String[2];
		synonyms[0] = "restaurer";
		synonyms[1] = "réinitialiser";
		XMeaningThesaurus other = new XMeaningThesaurus("Informatique",synonyms);
		assertTrue(this.meaning.equals(other));
	}

	@Test
	public void testBugEquals() {
		String[] synonyms = new String[1];
		synonyms[0] = "âme";
		XMeaningThesaurus first = new XMeaningThesaurus("Génie civil et construction",synonyms);
		String[] secondSynonyms = new String[1];
		secondSynonyms[0] = "âme";
		XMeaningThesaurus second = new XMeaningThesaurus("Génie civil et construction",secondSynonyms);
		assertTrue(first.equals(second));
	}
	
}
