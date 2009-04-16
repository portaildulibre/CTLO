package org.atosorigin.ctOO.comp;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.atosorigin.ctOO.Result;
import org.atosorigin.ctOO.WordsParser;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.uno.XComponentContext;


public final class CtOO3Impl extends WeakBase
   implements com.sun.star.lang.XServiceInfo,
              org.atosorigin.ctOO.XCtOO3
{
    private final XComponentContext m_xContext;
    private static final String m_implementationName = CtOO3Impl.class.getName();
    private static final String[] m_serviceNames = {
        "org.atosorigin.ctOO.CtOO3" };

    private URL foreignTerms;
    {
    	try
    	{
    		foreignTerms=new URL("file:///home/pprados/workspace/ctOO2/target/terminologie.xml");
    	}
    	catch (MalformedURLException e)
    	{
    		// Ignore
    	}
    }
    private WordsParser parser;
    
    public CtOO3Impl( XComponentContext context )
    {
        m_xContext = context;
		try
		{
			InputStream in=new BufferedInputStream(foreignTerms.openStream());
			parser=WordsParser.createFromXML(in);
			in.close();
		}
		catch (Throwable x)
		{
			// TODO
		}
        
    };

    public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        XSingleComponentFactory xFactory = null;

        if ( sImplementationName.equals( m_implementationName ) )
            xFactory = Factory.createComponentFactory(CtOO3Impl.class, m_serviceNames);
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        return Factory.writeRegistryServiceInfo(m_implementationName,
                                                m_serviceNames,
                                                xRegistryKey);
    }

    // com.sun.star.lang.XServiceInfo:
    public String getImplementationName() {
         return m_implementationName;
    }

    public boolean supportsService( String sService ) {
        int len = m_serviceNames.length;

        for( int i=0; i < len; i++) {
            if (sService.equals(m_serviceNames[i]))
                return true;
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

    // org.atosorigin.ctOO.XCtOO3:
    public Result[] search(String sentence)
    {
    	return parser.find(sentence);
    }

//	@Override // FIXME
	public String[] getdomaines()
	{
		return parser.getDomains();
	}

//	@Override
	public int getmaxwords()
	{
		return parser.getMaxWords();
	}

}
