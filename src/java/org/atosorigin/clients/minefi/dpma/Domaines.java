package org.atosorigin.clients.minefi.dpma;

/**
 * Un ensemble de domaines.
 */
public class Domaines
{
	/**
	 * Un seul domaine.
	 */
	static class Domaine
	{
		/** Le domaine. */
		public String domaine;
		/** La liste des synonymes pour le domaine. */
		public String[] synonymes; // Tableau pour optimiser la mémoire et l'accès.
		
		/**
		 * Comparaison de domaine.
		 * Les synonymes doivent être dans le même ordre.
		 */
		@Override
		public boolean equals(Object dd)
		{
			Domaine d=(Domaine)dd;
			if (!domaine.equals(d.domaine)) return false;
			if (synonymes.length!=d.synonymes.length) return false;
			boolean find=false;
			// Version same order
			for (int i=synonymes.length-1;i>=0;--i)
			{
				if (!synonymes[i].equals(d.synonymes[i]))
					return false;
			}
			// Version order free
//			for (String s:synonymes)
//			{
//				for (String s2:d.synonymes)
//				{
//					if (s.equals(s2))
//					{
//						find=true;
//						break;
//					}
//				}
//				if (find) break;
//			}
			return find;
		}
	}

	/**
	 * La liste des domaines.
	 */
	Domaine[] domaines; // Tableau pour les performances et l'économie de mémoire.
	
	/**
	 * Le constructeur.
	 */
	Domaines()
	{
	}

	/**
	 * Compare la liste de domaines.
	 * Les domaines doivent être dans le même ordre.
	 */
	@Override
	public boolean equals(Object dd)
	{
		Domaines dom=(Domaines)dd;
		boolean find=false;
		if (domaines.length!=dom.domaines.length) return false;
		// Version same order
		for (int i=domaines.length-1;i>=0;--i)
		{
			if (!domaines[i].equals(dom.domaines[i]))
				return false;
		}
		// Version order free
//		for (Domaine d:domaines)
//		{
//			for (Domaine d2:dom.domaines)
//			{
//				if (d.equals(d2))
//				{
//					find=true;
//					break;
//				}
//			}
//			if (find) break;
//		}
		return find;
	}
	
	/**
	 * Vue string de la liste des domaines.
	 */
	@Override
	public String toString()
	{
		StringBuilder builder=new StringBuilder();
		for (Domaine d:domaines)
		{
			builder.append("{").append(d.domaine).append(",[");
			for (String s:d.synonymes)
			{
				builder.append(s).append(',');
			}
			builder.setLength(builder.length()-1);
			builder.append("]},");
		}
		if (builder.length()>0)
			builder.setLength(builder.length()-1);
		return builder.toString();
	}
}


