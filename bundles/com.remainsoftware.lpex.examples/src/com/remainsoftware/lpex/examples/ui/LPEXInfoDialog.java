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
package com.remainsoftware.lpex.examples.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.lpex.core.LpexView;

public class LPEXInfoDialog extends Dialog {

	protected Object result;

	protected Shell shell;

	protected LpexView fLPEXView;

	private Text txtAuthor;



	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public LPEXInfoDialog(LpexView pLPEXView, Shell parent, int style) {
		super(parent, style);
		fLPEXView = pLPEXView;
		setText("LPEX Info Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));

		Label lblAuthor = new Label(shell, SWT.NONE);
		lblAuthor.setText("Author");

		txtAuthor = new Text(shell, SWT.BORDER);
		txtAuthor.setText("Remain Software");
		txtAuthor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		createWidgets(new String[] { "sourceName", "cursorRow", "position", "readonly", "block.text", "line",
				"lines", "dirty" });

	}

	private void createWidgets(String[] pQueryCommands) {
		for (String command : pQueryCommands) {
			Label label = new Label(shell, SWT.NONE);
			label.setText(command);
			Text text = new Text(shell, SWT.BORDER);
			text.setText(fLPEXView.query(command));
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
	}
}
