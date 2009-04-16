package org.atosorigin.ctOO;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


public class WordsParserFullTest
{
	private WordsParser parser;
	
	@Before 
	public void initParser()
	{
		try
		{
		    final URL foreignTerms=new URL("file:///home/pprados/workspace/ctOO2/target/terminologie.xml");
			long start=System.currentTimeMillis();
			InputStream in=new BufferedInputStream(foreignTerms.openStream());
			parser=WordsParser.createFromXML(in);
			long time=System.currentTimeMillis()-start;
			in.close();
			Assert.assertNotNull(parser);
			Assert.assertTrue(time<3000); // < 3s.
		}
		catch (Throwable x)
		{
			x.printStackTrace();
			Assert.assertFalse(true); // Burk
		}
		
	}
	
	@Test
	public void realWord()
	{
		Assert.assertEquals(2,parser.find("pcr method").length);
		Assert.assertEquals(2,parser.find("je suis un pcr method avec batterie.").length);
		Assert.assertEquals(1,parser.find("un business development director performant").length);
	}
	
}
