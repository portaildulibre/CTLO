package org.atosorigin.clients.minefi.dpma;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class WordsParserTest
{
	private WordsParser parser;
	
	@Before
	public void initTinyParser() throws SAXException, IOException, ParserConfigurationException
	{
		StringBuilder xml=new StringBuilder();
		xml.append(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
			"<anglicismes xmlns:exsl=\"http://exslt.org/common\">");
		for (String s:new String[]{"a b c","a d","a","d e"})
		{
			xml.append(
				"<anglicisme id=\"").append(s).append("\">"+
					"<domaines>"+
						"<domaine id=\"\">"+
							"<synonymes>"+
								"<synonyme>").append(s).append("</synonyme>"+
							"</synonymes>"+
						"</domaine>"+
					"</domaines>"+
				"</anglicisme>");
		}
		xml.append("</anglicismes>");
		System.out.println(xml.toString());
		parser = WordsParser.createFromXML(new ByteArrayInputStream(xml.toString().getBytes()));
	}
	
	@Test
	public void find()
	{
		Assert.assertEquals(0,parser.find("ZZZ XXX").size());
		Assert.assertEquals(2,parser.find("ZZZ a XXX d e").size());
		Assert.assertEquals(2,parser.find("a b c").size());
		Assert.assertEquals(2,parser.find("a b c ZZZ").size());
		Assert.assertEquals(2,parser.find("ZZZ a b c").size());
		Assert.assertEquals(2,parser.find("ZZZ a b c ZZZ").size());
		Assert.assertEquals(2,parser.find("a b c ZZZ").size());
		Assert.assertEquals(3,parser.find("a a b c ZZZ").size());
		Assert.assertEquals(3,parser.find("a b a b c ZZZ").size());
		Assert.assertEquals(4,parser.find("a b c a b c ZZZ").size());
		Assert.assertEquals(2,parser.find("ZZZ a d e").size());
		Assert.assertEquals(3,parser.find("ZZZ a d d e").size());
	}
	
}
