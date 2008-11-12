/*
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

import java.util.HashMap;
import java.util.Map;

import org.linagora.clients.minefi.dpma.terminologie.loader.Loader;
import org.linagora.clients.minefi.dpma.terminologie.loader.text.TextFileLoader;
import org.linagora.clients.minefi.dpma.terminologie.loader.xml.XMLFileLoader;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.loader.FactoryHelper;
import com.sun.star.lang.Locale;
import com.sun.star.lang.XInitialization;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XServiceDisplayName;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.lang.XSingleServiceFactory;
import com.sun.star.lib.uno.helper.ComponentBase;
import com.sun.star.linguistic2.XMeaning;
import com.sun.star.linguistic2.XThesaurus;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.uno.UnoRuntime;

public class AnglicismeThesaurus extends ComponentBase implements XThesaurus, XInitialization, XServiceDisplayName, XServiceInfo {
	
	/**
	 * @var int Le nombre d'entrée dans la liste terminologique.
	 */
	private static final int ANGLICISME_PRELOAD_COUNT = 4000; // TODO: Set this from build file
	/**
	 * @var String Nom du service UNO
	 */
	public final static String __serviceName = "org.linagora.clients.minefi.dpma.AnglicismeThesaurus";

	/**
	 * @var String Chemin vers le fichier de données à l'intérieur du jar
	 */
	public final static String dataPath = "/terminologie_fr_FR.dat";
	public final static String xmlDataPath = "/terminologie.xml";
	
	public PropChgHelper aPropChgHelper; //FIXME: Should this be public ?
	
	/**
	 * @var Locale La locale supportée
	 */
	public Locale oLocale;				//FIXME: Should this be public ?
	
	private String encoding;
	private static Map data;
	
	private boolean loadAsXml = true; 	// Set to false to fallback to the old text file loading process
	/**
	 * Constructeur
	 */
	public AnglicismeThesaurus() {
		init();
	}
	
	/**
	 * <p>This constructor is merely for test, it should not be used while the plugin
	 * is running inside OOo</p>
	 * 
	 * @param loadAsXML
	 */
	public AnglicismeThesaurus(boolean loadAsXml) {
		this.loadAsXml = loadAsXml;
		init();
	}
	
	private void init() {
		// Nom de paramètres utiles à utiliser.
		String[] aProps = new String[] { "IsIgnoreControlCharacters","IsUseDictionaryList", };
		aPropChgHelper = new PropChgHelper((XThesaurus) this, aProps);
		oLocale = new Locale("fr", "FR", "");

		// charge les anglicismes
		if(data == null) {
			data = new HashMap(ANGLICISME_PRELOAD_COUNT);
			
			Loader loader = null;
			if (loadAsXml) {
				loader = new XMLFileLoader(xmlDataPath);
			}
			else {
				loader = new TextFileLoader(dataPath);
			}
			
			try {
				loader.loadDictionnary(data);
			}
			catch (AnglicismeThesaurusException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Vérifie si deux locales sont égales.
	 * 
	 * @param Locale
	 *            aLoc1
	 * @param Locale
	 *            aLoc2
	 * @return boolean
	 */
	private boolean IsEqual(Locale aLoc1, Locale aLoc2) {
		return aLoc1.Language.equals(aLoc2.Language)
				&& aLoc1.Country.equals(aLoc2.Country)
				&& aLoc1.Variant.equals(aLoc2.Variant);
	}
	
	//FIXME: Are thos actually used by OOo ? 
	/**
	 * @return String
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param String
	 *            encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	// __________ interface methods __________

	/**
	 * @return Locale[]
	 * @throws RuntimeException
	 * @see com.sun.star.linguistic2.XSupportedLocales
	 */
	public Locale[] getLocales() throws RuntimeException {
		Locale aLocales[] = { oLocale };
		return aLocales;
	}

	/**
	 * @return boolean
	 * @throws RuntimeException
	 * @see com.sun.star.linguistic2.XSupportedLocales
	 */
	public boolean hasLocale(Locale aLocale) throws RuntimeException {
		boolean bRes = false;
		if (IsEqual(aLocale, oLocale))
			bRes = true;
		return bRes;
	}

	/**
	 * @param String
	 *            aTerm
	 * @param Locale
	 *            aLocale
	 * @param PropertyValue[]
	 *            aProperties
	 * @return XMeaning[]
	 * @throws RuntimeException
	 * @see com.sun.star.linguistic2.XThesaurus
	 */
	public XMeaning[] queryMeanings(String aTerm, Locale aLocale,
			PropertyValue[] aProperties)
			throws com.sun.star.lang.IllegalArgumentException, RuntimeException {
		XMeaning[] aRes = new XMeaning[] {};
		try {
			if (IsEqual(aLocale, new Locale()) || aTerm.length() == 0) {
				return aRes;
			}

			// Le module linguistic2 n'a pas le droit pas d'envoyer une
			// exception
			// en cas de problème. On se contente de renvoyer une valeur vide
			// (un XMeaning[]) qui veut dire "on n'a pas pu trouver le mot".
			if (!hasLocale(aLocale)) {
				return aRes;
			}
			if (data.containsKey(aTerm)) {
				aRes = (XMeaning[])data.get(aTerm);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return aRes;
	}

	/**
	 * @param Locale
	 *            aLocale
	 * @return String
	 * @throws RuntimeException
	 * @see com.sun.star.lang.XServiceDisplayName
	 */
	public String getServiceDisplayName(Locale aLocale) throws RuntimeException {
		return "Correcteur terminologique";
	}

	/**
	 * @param Object[]
	 *            aArguments
	 * @throws com.sun.star.uno.Exception
	 * @throws RuntimeException
	 * @see com.sun.star.lang.XInitialization
	 */
	public void initialize(Object[] aArguments)
			throws com.sun.star.uno.Exception, RuntimeException {
		int nLen = aArguments.length;
		if (2 == nLen) {
			XPropertySet xPropSet = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, aArguments[0]);
			// Commence l'écoute des événements sur les propriétés
			aPropChgHelper.AddAsListenerTo(xPropSet);
		}
	}

	/**
	 * @param String
	 *            aServiceName
	 * @return boolean
	 * @throws RuntimeException
	 * @see com.sun.star.lang.XServiceInfo
	 */
	public boolean supportsService(String aServiceName) throws RuntimeException {
		String[] aServices = getSupportedServiceNames_Static();
		int i, nLength = aServices.length;
		boolean bResult = false;

		for (i = 0; !bResult && i < nLength; ++i)
			bResult = aServiceName.equals(aServices[i]);

		return bResult;
	}

	/**
	 * @return String
	 * @throws RuntimeException
	 * @see com.sun.star.lang.XServiceInfo
	 */
	public String getImplementationName() throws RuntimeException {
		return __serviceName;
	}

	/**
	 * @return String[]
	 * @throws RuntimeException
	 * @see com.sun.star.lang.XServiceInfo
	 */
	public String[] getSupportedServiceNames() throws RuntimeException {
		return getSupportedServiceNames_Static();
	}

	// __________ static things __________

	/**
	 * Renvoie une usine pour créer le service. Cette méthode est appelée par le
	 * <code>JavaLoader</code>.
	 * <p>
	 * 
	 * @return Renvoie un <code>XSingleServiceFactory</code> pour créer le
	 *         composant.
	 * @param implName
	 *            Le nom de l'implémentation pour laquelle le service est voulu.
	 * @param multiFactory
	 *            Le gestionnaire de services à utiliser si besoin.
	 * @param regKey
	 *            Le registre.
	 * @see com.sun.star.comp.loader.JavaLoader
	 */
	public static XSingleServiceFactory __getServiceFactory(String implName,
			XMultiServiceFactory multiFactory, XRegistryKey regKey) {
		XSingleServiceFactory xSingleServiceFactory = null;
		if (implName.equals(AnglicismeThesaurus.class.getName())) {
			xSingleServiceFactory = FactoryHelper.getServiceFactory(
					AnglicismeThesaurus.class, __serviceName, multiFactory,
					regKey);
		}
		return xSingleServiceFactory;
	}

	/**
	 * Inscrit les informations sur le service donné dans le registre donné.
	 * Cette méthode est appelée par le <code>JavaLoader</code>.
	 * <p>
	 * 
	 * @return Renvoie true si l'opération est un succès.
	 * @param xRegKey
	 *            Le registre.
	 * @see com.sun.star.comp.loader.JavaLoader
	 */
	public static boolean __writeRegistryServiceInfo(XRegistryKey regKey) {
		boolean b = FactoryHelper.writeRegistryServiceInfo(
				AnglicismeThesaurus.class.getName(), __serviceName, regKey);
		return b;
	}

	/**
	 * @return String[]
	 * @throws RuntimeException
	 */
	public static String[] getSupportedServiceNames_Static() {
		String[] aResult = { "com.sun.star.linguistic2.Thesaurus" };
		return aResult;
	}
}