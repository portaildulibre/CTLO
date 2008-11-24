/**
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
package org.linagora.clients.minefi.dpma.terminologie.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Array;

/**
 * @author Romain PELISSE, romain.pelisse@atosorigin.com
 *
 */
public class Utils {

	// Should not be able to instantiate an utilities class such as this one
	private Utils() {}
	
	/**
	 * 
	 * Concat two arrays
	 * 
	 * @param term
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] concat(T[] a, T[] b) {
	    final int alen = a.length;
	    final int blen = b.length;
	    final T[] result = (T[]) Array.newInstance(a.getClass().getComponentType(), alen + blen);
	    System.arraycopy(a, 0, result, 0, alen);
	    System.arraycopy(b, 0, result, alen, blen);
	    return result;
	}
}
