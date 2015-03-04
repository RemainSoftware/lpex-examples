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
package com.remainsoftware.lpex.examples;

import java.util.ArrayList;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.ibm.lpex.core.LpexView;
import com.remainsoftware.lpex.examples.actions.LPEXHelloWorldAction;
import com.remainsoftware.lpex.examples.actions.LPEXInfoAction;

public class Activator extends AbstractUIPlugin {
	private static Activator instance;

	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
		LpexView.doGlobalCommand("set default.popup "
				+ getLPEXPopupMenu(LpexView.globalQuery("current.popup")));
		LpexView.doGlobalCommand("set default.updateProfile.userActions "
				+ getLPEXActions(LpexView.globalQuery("current.updateProfile.userActions")));
	}

	public void stop(BundleContext context) throws Exception {
		instance = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return instance;
	}

	/**
	 * Adds our actions to the LPEX actions.
	 * 
	 * @param existingActions
	 * @return the new user actions
	 */
	protected String getLPEXActions(String existingActions) {

		ArrayList<String> actions = new ArrayList<String>();

		actions.add(LPEXInfoAction.class.getSimpleName() + " " + LPEXInfoAction.class.getName());
		actions.add(LPEXHelloWorldAction.class.getSimpleName() + " " + LPEXHelloWorldAction.class.getName());

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
		
		String startMenu = "beginSubmenu \"Remain Examples\"";
		String startSubMenu = "beginSubmenu \"Remain Submenu\"";

		ArrayList<String> menuActions = new ArrayList<String>();

		menuActions.add("\"Info\" " /* Visible name */+ LPEXInfoAction.class.getSimpleName() /*
																							 * Command
																							 * name
																							 */);
		menuActions.add("\"Hello\" " + LPEXHelloWorldAction.class.getSimpleName());

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