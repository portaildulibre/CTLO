package org.atosorigin.clients.minefi.dpma;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class WordsParser
{
	private static boolean log = false;

	// classical version, with more memory
	@SuppressWarnings("serial")
	private static class HashString extends HashMap<String, WordsParser> 
	{ 
		HashString()
		{
			super(8);
		}
	}
	// memory optimization with same little less speed 
//	private static class HashString extends MultiStringHashMap<WordsParser> { }
	// Version avec automate
//	private static class MultiStringHashMap<T> implements Map<String, T>
//	{
//		public MultiStringHashMap()
//		{
//		}
//
//		private char c = '\0';
//
//		private MultiStringHashMap<T> next;
//
//		private MultiStringHashMap<T> brother;
//
//		private T value;
//
//		@Override
//		public T put(String key, final T value)
//		{
//			// For each string...
//			MultiStringHashMap<T> cur = this;
//			final int l = key.length();
//			// For each char...
//			for (int j = 0; j < l; ++j)
//			{
//				final char c = key.charAt(j);
//				if (cur.next == null)
//				{
//					cur = cur.next = new MultiStringHashMap<T>();
//					cur.c = c;
//				}
//				else
//				{
//					MultiStringHashMap<T> f = cur.next;
//					while (f.c != c)
//					{
//						if (f.brother == null)
//						{
//							cur = f.brother = new MultiStringHashMap<T>();
//							cur.c = c;
//							break;
//						}
//						f = f.brother;
//					}
//					if (f.c == c)
//						cur = f;
//				}
//
//			}
//			cur.value = value;
//			return value;
//		}
//
//		@Override
//		public T get(final Object key)
//		{
//			String k=(String)key;
//			int end = 0;
//			MultiStringHashMap<T> cur = this;
//			final int max = k.length();
//			here: for (int i = 0; i < max; ++i)
//			{
//				final char c = k.charAt(i);
//				MultiStringHashMap<T> f = cur.next;
//				while (f != null)
//				{
//					if (f.c == c)
//					{
//						++end;
//						cur = f;
//						if (end == max)
//						{
//							return cur.value;
//						}
//						continue here;
//					}
//					f = f.brother;
//				}
//
//				if (cur != this)
//				{
//					cur = this;
//				}
//				return null;
//			}
//			return null;
//		}
//
//		private String dump()
//		{
//			final StringBuilder b = new StringBuilder();
//			b.append("--------\n");
//			new Runnable()
//			{
//				void dump(int i, MultiStringHashMap<T> n)
//				{
//					for (int j = 0; j < i; ++j)
//						b.append(" ");
//					b.append("{" + n.c + ":" + n.value + "}\n");
//					if (n.next != null)
//						dump(
//							i + 1, n.next);
//					if (n.brother != null)
//					{
//						dump(
//							i, n.brother);
//					}
//				}
//
//				public void run()
//				{
//					dump(
//						0, MultiStringHashMap.this);
//				}
//			}.run();
//			b.append("\n--------\n");
//			return b.toString();
//		}
//
//		@Override
//		public void clear()
//		{
//			c='\0';
//			next=null;
//			brother=null;
//		}
//
//		@Override
//		public boolean containsKey(Object paramObject)
//		{
//			return get(paramObject)!=null;
//		}
//
//		@Override
//		public boolean containsValue(Object paramObject)
//		{
//			throw new NotImplementedException();
//		}
//
//		@Override
//		public Set<java.util.Map.Entry<String, T>> entrySet()
//		{
//			throw new NotImplementedException();
//		}
//
//		@Override
//		public boolean isEmpty()
//		{
//			return c=='\0' && next==null && brother==null;
//		}
//
//		@Override
//		public Set<String> keySet()
//		{
//			throw new NotImplementedException();
//		}
//
//		@Override
//		public void putAll(Map<? extends String, ? extends T> paramMap)
//		{
//			throw new NotImplementedException();
//		}
//
//		@Override
//		public T remove(Object paramObject)
//		{
//			throw new NotImplementedException();
//		}
//
//		@Override
//		public int size()
//		{
//			throw new NotImplementedException();
//		}
//
//		@Override
//		public Collection<T> values()
//		{
//			throw new NotImplementedException();
//		}
//	};
//

	/**
	 * Un item de résultat.
	 * Avec le numéro de mot de départ, de fin, et les domaines associées.
	 */
	static class Result
	{
		public int start;

		public int stop;

		public Domaines domaines;

		Result(int start, int stop, Domaines domaines)
		{
			this.start = start;
			this.stop = stop;
			this.domaines = domaines;
		}
	}
	
	/**
	 * Un ensemble de résultats.
	 * @author pprados
	 *
	 */
	public static class Results extends ArrayList<Result>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public String toString()
		{
			StringBuilder builder=new StringBuilder();
			builder.append('{');
			for (Result r:this)
			{
				builder.append("[").append(r.start).append('-').append(r.stop).append(",").append(r.domaines).append("],");
			}
			if (builder.length()>1) builder.setLength(builder.length()-1);
			builder.append('}');
			return builder.toString();
		}
	}
	
// ----------------------------------------
	/**
	 * Un processeur de mots dans une phrase.
	 */
	public  WordsParser()
	{
		this.word="";
	}
	
	/**
	 * Creation d'un noeud de l'automate.
	 * @param word
	 */
	private WordsParser(String word)
	{
		this.word = word;
	}

	/**
	 * Le mot du noeud.
	 */
	String word;

	/**
	 * Les mots suivants candidats.
	 */
	HashString candidates = new HashString();

	/**
	 * Le domaine associé ou null.
	 */
	Domaines domaines;

	/**
	 * Une vision string de l'arbre.
	 */
	@Override
	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		buf.append(
			"word=").append(
			word).append(
			"\n");
		buf.append(
			"domaines=").append(
			domaines).append(
			"\n");
		buf.append(
			"candidates={").append(
			candidates).append(
			"}\n");
		buf.append("]");
		return buf.toString();
	}

	// ------------------------------

	/**
	 * Ajoute une phrase à l'automate.
	 * @param domaines Le domaine à associer à la phrase.
	 * @param sentence La phrase. Les mots sont séparé par des espaces ou des tirets.
	 */
	public void add( Domaines domaines, String sentence)
	{
		if (log)
			System.out.println("add \"" + sentence + "\"");
		add(domaines,parseSentence(sentence));
	}
	
	/**
	 * Ajoute une liste de mots à l'automate pour un domaine.
	 * 
	 * @param domaines Le domaine.
	 * @param words La liste de mots.
	 */
	public void add( Domaines domaines, String... words)
	{
		// For each string...
		WordsParser cur = this;
		for (final String word : words)
		{
			// if (log) System.out.println("ADD \""+word+"\"");
			WordsParser f = cur.candidates.get(word);
			if (f == null)
			{
				cur.candidates.put(
					word, f = new WordsParser(word));
			}
			cur = f;
		}
		cur.domaines = domaines;

		// new Runnable()
		// {
		// void dump(int i, NodeWord n)
		// {
		// for (int j = 0; j < i; ++j)
		// System.out.print(" ");
		// System.out.println("{" + n.word + ":"+n.domaines+"}");
		// System.out.println(n.candidates);
		// if (n.next != null)
		// dump(i + 1, n.next);
		// dumpNodeChar(n.candidates);
		// }
		//
		// public void run()
		// {
		// dump(0, top);
		// }
		// }.run();

	}

	/**
	 * Recherche les mots dans la phrase.
	 * 
	 * @param sentence La phrase où chercher les mots.
	 * @return Un ensemble de résultats.
	 */
	public Results find(final String sentence)
	{
		if (log)
			System.out.println("find \"" + sentence + "\"");
		return find(parseSentence(sentence));
	}

	/**
	 * Recherche dans la pharse composée de mots
	 * @param words Les mots de la phrase.
	 * @return Un ensemble de résultats.
	 */
	private Results find(final String[] words)
	{
		Results results = new Results();
		int start = 0;
		WordsParser cur = this;
		final int max = words.length;
		here: for (int i = 0; i < max; ++i)
		{
			String word = words[i];
			// System.out.println(cur.candidates.dump());
			WordsParser find = cur.candidates.get(word);
			if (find != null)
			{
				if (log)
					System.out.println("FIND \"" + find.word + "\"");
				if (find.domaines != null)
				{
					if (log)
						System.out.println("ADD \"" + find.domaines + "\" de " + start + " à " + i);
					results.add(new Result(start, i, find.domaines));
				}
				cur = find;
				continue here;
			}

			if (cur != this)
			{
				cur = this;
				start = i;
				--i;
				continue here;
			}
			else
				start = i + 1;
		}
		return results;
	}

	// --------------
	/**
	 * Initialisation de l'automate à partir d'un flux XML.
	 * 
	 * @param dataInputStream Le flux.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static WordsParser createFromXML(InputStream dataInputStream) 
		throws SAXException, IOException, ParserConfigurationException
	{
		final WordsParser parser=new WordsParser();
		parser.parseXML(dataInputStream);
		return parser;
	}
	
	/**
	 * Ajout à l'automate à partir d'un flux XML.
	 * 
	 * @param dataInputStream Le flux.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private void parseXML(InputStream dataInputStream) 
		throws SAXException, IOException,ParserConfigurationException
	{
		SAXParserFactory.newInstance().newSAXParser().parse(dataInputStream, new XMLDictionnaryHandler(this));

	}

	/**
	 * Découpe une phrase en mots.
	 * @param sentence La phrase.
	 * @return Le tableau de mots.
	 */
	private static String[] parseSentence(String sentence)
	{
		return sentence.split("[ -]");
	}

}
