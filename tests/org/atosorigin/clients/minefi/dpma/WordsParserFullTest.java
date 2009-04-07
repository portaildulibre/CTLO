package org.atosorigin.clients.minefi.dpma;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

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
			long start=System.currentTimeMillis();
			InputStream in=new BufferedInputStream(new FileInputStream("/home/pprados/workspace/ctOO2/target/agregate-database.xml"));
			parser=WordsParser.createFromXML(in);
			long time=System.currentTimeMillis()-start;
			in.close();
			Assert.assertNotNull(parser);
			Assert.assertTrue(time<3000); // < 3s.
		}
		catch (Throwable x)
		{
			Assert.assertFalse(true); // Burk
		}
		
	}
	
	@Test
	public void realWord()
	{
		Assert.assertEquals(2,parser.find("pcr method").size());
		Assert.assertEquals(2,parser.find("je suis un pcr method avec batterie.").size());
	}
	
}
