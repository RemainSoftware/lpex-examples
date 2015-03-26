/*******************************************************************************
 * Copyright (c) 2015 Remain BV and others All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this 
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Wim Jongman  - initial API and implementation - https://remainsoftware.com
 ******************************************************************************/
package com.remainsoftware.lpex.rse.examples;

import java.util.ArrayList;

import com.ibm.lpex.alef.LpexPreload;
import com.ibm.lpex.core.LpexView;
import com.remainsoftware.lpex.rse.examples.actions.RSERemoteObjectAction;

/**
 * See <a href=
 * "http://www-01.ibm.com/support/knowledgecenter/SSAE4W_9.1.1/com.ibm.lpex.doc.isv/api/com/ibm/lpex/alef/package-summary.html"
 * >http://www-01.ibm.com/support/knowledgecenter/SSAE4W_9.1.1/com.ibm.lpex.doc.
 * isv/api/com/ibm/lpex/alef/package-summary.html</a>
 * 
 * @author Wim Jongman
 * @creationdate 26 mrt. 2015
 * 
 */
public class Preload implements LpexPreload {
	@Override
	public void preload() {
		LpexView.doGlobalCommand("set default.popup "
				+ getLPEXPopupMenu(LpexView.globalQuery("current.popup")));
		LpexView.doGlobalCommand("set default.updateProfile.userActions "
				+ getLPEXActions(LpexView.globalQuery("current.updateProfile.userActions")));
	}

	/**
	 * Adds our actions to the LPEX actions.
	 * 
	 * @param existingActions
	 * @return the new user actions
	 */
	protected String getLPEXActions(String existingActions) {

		ArrayList<String> actions = new ArrayList<String>();

		actions.add(RSERemoteObjectAction.class.getSimpleName() + " " + RSERemoteObjectAction.class.getName());

		StringBuffer newUserActions = new StringBuffer();

		if ((existingActions == null) || (existingActions.equalsIgnoreCase("null"))) {
			for (String action : actions) {
				newUserActions.append(action + " ");
			}
		}

		else {
			newUserActions.append(existingActions + " ");
			for (String action : actions) {
				if (existingActions.indexOf(action) < 0) {
					newUserActions.append(action + " ");
				}
			}
		}

		return newUserActions.toString();
	}

	/**
	 * Fills the LPEX pop-up menu like so. The action added consists of the
	 * visible name and the command name. The command name is added in
	 * {@link #getLPEXActions(String)}.
	 * 
	 * @param popupMenu
	 * @return the new menu
	 */
	protected String getLPEXPopupMenu(String popupMenu) {

		String startMenu = "beginSubmenu \"Remain RSE Examples\"";
		String startSubMenu = "beginSubmenu \"Remain RSE Submenu\"";

		ArrayList<String> menuActions = new ArrayList<String>();

		menuActions.add("\"Info\" " /* Visible name */+ RSERemoteObjectAction.class.getSimpleName() /*
																							 * Command
																							 * name
																							 */);

		StringBuffer newMenu = new StringBuffer("");
		newMenu.append(startMenu);
		newMenu.append(" ");
		for (String action : menuActions) {
			newMenu.append(action + " ");
		}

		newMenu.append(startSubMenu);
		newMenu.append(" ");
		for (String action : menuActions) {
			newMenu.append(action + " ");
		}
		newMenu.append("endSubmenu separator endSubmenu separator ");

		if (popupMenu != null && popupMenu.contains(newMenu.toString())) {
			return popupMenu;
		}

		if (popupMenu != null) {
			return newMenu.toString() + " " + popupMenu;
		}

		return newMenu.toString();
	}
}
