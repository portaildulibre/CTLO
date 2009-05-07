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
	public void testOrder()
	{
		Result[] r;

		// Vérifie le classement des résultats
		r=parser.find("cell library");
		Assert.assertEquals(2,r.length);
		Assert.assertTrue(r[0].stop>r[1].stop);
	}
	
	@org.junit.Test
	public void testPlurial()
	{
		Result[] r;
		
		// Vérifie la gestion des pluriels
		r=parser.find("databases");
		Assert.assertEquals(1,r.length);
		Assert.assertEquals(0,r[0].start);
		Assert.assertEquals(9,r[0].stop);
	}
	@org.junit.Test
	public void testTiret()
	{
		Result[] r;

		// Vérifie les tirets
		r=parser.find("back-up");
		Assert.assertEquals(1, r.length);
		Assert.assertEquals(0,r[0].start);
		Assert.assertEquals(7,r[0].stop);
	}
	@org.junit.Test
	public void testDouble()
	{
		Result[] r;

		// Vérifie les doublons
		r=parser.find("database database");
		Assert.assertEquals(2,r.length);
		Assert.assertEquals(0,r[0].start);
		Assert.assertEquals(9,r[1].start);
		Assert.assertEquals(2,parser.find("bit et bit").length);
	}

}
