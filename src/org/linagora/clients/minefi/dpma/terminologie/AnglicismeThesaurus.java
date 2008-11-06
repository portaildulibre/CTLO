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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

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
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;

public class AnglicismeThesaurus extends ComponentBase implements XThesaurus,
		XInitialization, XServiceDisplayName, XServiceInfo {
	/**
	 * @var int Le nombre d'entrée dans la liste terminologique.
	 */
	private static final int ANGLICISME_PRELOAD_COUNT = 4000;
	/**
	 * @var String Nom du service UNO
	 */
	final static String __serviceName = "org.linagora.clients.minefi.dpma.AnglicismeThesaurus";
	/**
	 * @var String Chemin vers le fichier de données à l'intérieur du jar
	 */
	final static String dataPath = "/terminologie_fr_FR.dat";
	PropChgHelper aPropChgHelper;
	/**
	 * @var Locale La locale supportée
	 */
	Locale oLocale;
	private String encoding;
	private static HashMap data;
	/**
	 * Constructeur
	 */
	public AnglicismeThesaurus() {
		// Nom de paramètres utiles à utiliser.
		String[] aProps = new String[] { "IsIgnoreControlCharacters",
				"IsUseDictionaryList", };
		aPropChgHelper = new PropChgHelper((XThesaurus) this, aProps);
		oLocale = new Locale("fr", "FR", "");

		// charge les anglicismes
		try {
			if(data == null) {
				data = new HashMap(ANGLICISME_PRELOAD_COUNT);
				initDataStruct();
			}
		} catch (IOException e) {
			// impossible d'ouvrir le fichier de données
			e.printStackTrace();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
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

	// __________ low level stuff

	/**
	 * Open index and dat files and load list array
	 * 
	 * @param String
	 *            initDataStruct
	 * @throws IOException
	 * @throws Exception
	 * @return boolean
	 */
	private boolean initDataStruct() throws IOException, Exception {
		String line;
		String index;
		String[] tmp;
		int count;

		InputStream dataInputStream = AnglicismeThesaurus.class.getResourceAsStream(dataPath);
		// open the data file
		if(dataInputStream == null) {
			throw new Exception("Could not open data file '"+dataPath+"' included in jar archive.");
		}
		BufferedReader buffer = new BufferedReader(new InputStreamReader(dataInputStream, "UTF-8"));
		// Read first line (encoding)
		setEncoding(buffer.readLine());
		// now parse the remaining lines of the index
		// Read through file one line at time.
		while ((line = buffer.readLine()) != null) {
			if (-1 != line.indexOf("|")) {
				tmp = line.split("\\|");
				index = tmp[0];
				count = Integer.parseInt(tmp[1]);
				XMeaning[] list = new XMeaningThesaurus[count];
				// on prend les n sens différents du mot en cours
				for (int i = 0; i < count; i++) {
					line = buffer.readLine();
					String[] tmp2 = new String[] {};
					tmp2 = line.split("\\|");
					line = tmp2[0];
					String[] tmp3 = new String[tmp2.length - 1];
					System.arraycopy(tmp2, 1, tmp3, 0, tmp2.length - 1);
					list[i] = new XMeaningThesaurus(line, tmp3);
				}
				data.put(index, list);
			} else {
				buffer.close();
				throw new Exception("Bad data file format at line '" + line + "' in file '"+dataPath+"' included in jar archive.");
			}
		}
		buffer.close();
		return true;
	}

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
			// en cas de probl�me. On se contente de renvoyer une valeur vide
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