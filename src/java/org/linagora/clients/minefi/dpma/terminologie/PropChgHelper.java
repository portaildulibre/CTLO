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

import java.util.ArrayList;
import com.sun.star.beans.PropertyChangeEvent;
import com.sun.star.beans.XPropertyChangeListener;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.EventObject;
import com.sun.star.linguistic2.LinguServiceEvent;
import com.sun.star.linguistic2.XLinguServiceEventBroadcaster;
import com.sun.star.linguistic2.XLinguServiceEventListener;
import com.sun.star.uno.XInterface;

public class PropChgHelper implements XPropertyChangeListener,XLinguServiceEventBroadcaster {
	
	XInterface xEvtSource;
	String[] aPropNames;
	XPropertySet xPropSet;
	ArrayList aLngSvcEvtListeners;
	
	public PropChgHelper(
			XInterface xEvtSource,
			String[] aPropNames )
	{
		this.xEvtSource = xEvtSource;
		this.aPropNames = aPropNames;
		xPropSet		= null;
		aLngSvcEvtListeners = new ArrayList();
	}
	
	public XInterface GetEvtSource()
	{
		return xEvtSource;
	}
	
	public XPropertySet GetPropSet()
	{
		return xPropSet;
	}
	
	public String[] GetPropNames()
	{
		return aPropNames;
	}
	
	public void LaunchEvent( LinguServiceEvent aEvt )
	{
		int nCnt = aLngSvcEvtListeners.size();
		for (int i = 0; i < nCnt; ++i)
		{
			XLinguServiceEventListener xLstnr = 
				(XLinguServiceEventListener) aLngSvcEvtListeners.get(i);
			if (xLstnr != null)
				xLstnr.processLinguServiceEvent( aEvt );
		}
	}
	
	public void AddAsListenerTo( XPropertySet xPropertySet )
	{
		// Ne plus surveiller les anciens paramètres (s'il y en a).
		RemoveAsListener();
		
		// Définir le nouveau groupe de propriétés à utiliser et
		// démarrer la surveillance dessus.
		xPropSet = xPropertySet;
		if (xPropSet != null)
		{
			int nLen = aPropNames.length;
			for (int i = 0; i < nLen; ++i)
			{
				if (aPropNames[i].length() != 0)
				{
					try {
						xPropSet.addPropertyChangeListener( 
								aPropNames[i], (XPropertyChangeListener) this );
					}
					catch( Exception e ) {
					}
				}
			}
		}
	}
	
	public void RemoveAsListener()
	{
		if (xPropSet != null)
		{
			int nLen = aPropNames.length;
			for (int i = 0; i < nLen; ++i)
			{
				if (aPropNames[i].length() != 0)
				{
					try {
						xPropSet.removePropertyChangeListener( 
								aPropNames[i], (XPropertyChangeListener) this );
					}
					catch( Exception e ) {
					}
				}
			}
			
			xPropSet = null;
		}
	}
	
	// __________ méthode des interfaces __________
	
	//***************
	// XEventListener
	//***************
	public void disposing( EventObject aSource )
	throws com.sun.star.uno.RuntimeException
	{
		if (aSource.Source == xPropSet)
		{
			RemoveAsListener();
		}
	}
	
	//************************
	// XPropertyChangeListener
	//************************
	public void propertyChange( PropertyChangeEvent aEvt )
	throws com.sun.star.uno.RuntimeException
	{
		// sera surchargé dans les classes dérivées
		
	}
	
	//******************************
	// XLinguServiceEventBroadcaster
	//******************************
	public boolean addLinguServiceEventListener(
			XLinguServiceEventListener xListener )
	throws com.sun.star.uno.RuntimeException
	{
		boolean bRes = false;
		if (xListener != null)
		{
			bRes = aLngSvcEvtListeners.add( xListener );
		}
		return bRes;
	}
	
	public boolean removeLinguServiceEventListener(
			XLinguServiceEventListener xListener )
	throws com.sun.star.uno.RuntimeException
	{
		boolean bRes = false;
		if (xListener != null)
		{
			int nIdx = aLngSvcEvtListeners.indexOf( xListener );
			if (nIdx != -1)
			{
				aLngSvcEvtListeners.remove( nIdx );
				bRes = true;
			}
		}
		return bRes;
	}
};