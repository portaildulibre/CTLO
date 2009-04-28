package org.atosorigin.ctOO;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

import org.atosorigin.ctOO.comp.CtOO3Impl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;

//@org.junit.TestSuite
public class UnitTest
{
    private WordsParser parser;
	
    private URL foreignTerms;
	{
		foreignTerms = UnitTest.class.getResource("/terminologie.xml");
	}
	
	@Before
	public void before()
	{
		try
		{
			InputStream in=new BufferedInputStream(foreignTerms.openStream());
			parser=WordsParser.createFromXML(in);
			in.close();
		}
		catch (Throwable x)
		{
			x.printStackTrace();
		}
		
	}
	@org.junit.Test
	public void testCase()
	{
		Assert.assertEquals(2,parser.find("bit et bit").length);
	}

}
