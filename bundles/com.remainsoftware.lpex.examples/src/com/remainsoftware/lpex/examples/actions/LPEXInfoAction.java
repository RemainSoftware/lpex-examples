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
package com.remainsoftware.lpex.examples.actions;

import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;

import com.ibm.lpex.core.LpexAction;
import com.ibm.lpex.core.LpexView;
import com.remainsoftware.lpex.examples.ui.LPEXInfoDialog;

public class LPEXInfoAction implements LpexAction {

	@Override
	public boolean available(LpexView pView) {
		return ! (pView.queryOn("readonly"));
	}

	@Override
	public void doAction(final LpexView pView) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				new LPEXInfoDialog(pView, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL).open();
			}
		});
	}
}
