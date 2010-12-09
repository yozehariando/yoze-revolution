package com.github.twitterapi.ui;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.container.MainScreen;

public class ActionScreen extends MainScreen {

	protected Vector actionListeners = new Vector();

	public void addActionListener(ActionListener actionListener) {
		if (actionListener != null) {
			actionListeners.addElement(actionListener);
		}

	}

	protected void fireaction(String action) {
		fireaction(action, null);
	}

	protected void fireaction(String action, Object data) {
		Enumeration listenerEnumeration = actionListeners.elements();
		while (listenerEnumeration.hasMoreElements()) {
			((ActionListener) listenerEnumeration.nextElement())
					.onAction(new Action(this, action, data));
		}
	}

}
