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
package com.remainsoftware.lpex.rse.examples.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.ui.view.ISystemEditableRemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import com.ibm.etools.systems.editor.IRemoteResourceProperties;
import com.ibm.etools.systems.editor.RemoteResourcePropertiesFactoryManager;
import com.ibm.etools.systems.editor.SystemEditorUtilities;
import com.ibm.lpex.core.LpexView;

public class RSERemoteObjectInfoDialog extends Dialog {

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
	public RSERemoteObjectInfoDialog(LpexView pLPEXView, Shell parent, int style) {
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
		shell = new Shell(getParent(), getStyle() | SWT.RESIZE);
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));

		Label lblAuthor = new Label(shell, SWT.NONE);
		lblAuthor.setText("Author");

		txtAuthor = new Text(shell, SWT.BORDER);
		txtAuthor.setText("Remain Software");
		txtAuthor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Object remoteObject = getRemoteObject((ITextEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor());

		new Label(shell, SWT.NONE).setText("Class");
		if (remoteObject != null) {
			new Text(shell, SWT.BORDER).setText(remoteObject.getClass().getName());
		} else {
			new Text(shell, SWT.BORDER).setText("not a valid remote object");
		}
	}

	public static Object getRemoteObject(ITextEditor editor) {
		IFile file = SystemEditorUtilities.getIFile(editor);
		if (file == null) {
			return null;
		}

		IRemoteResourceProperties properties = RemoteResourcePropertiesFactoryManager.getInstance()
				.getRemoteResourceProperties(file);

		Object rmtEditable = properties.getRemoteFileObject();
		if ((rmtEditable != null) && (rmtEditable instanceof ISystemEditableRemoteObject)) {
			ISystemEditableRemoteObject editable = (ISystemEditableRemoteObject) rmtEditable;
			return editable.getRemoteObject();

		}

		String strRemoteFilePath = properties.getRemoteFilePath();
		if (strRemoteFilePath == null) {
			return null;
		}

		ISystemRegistry registry = RSECorePlugin.getDefault().getSystemRegistry();
		if (registry == null) {
			return null;
		}

		String strSubSystem = properties.getRemoteFileSubSystem();
		if (strSubSystem == null)
			return null;
		ISubSystem subsystem = registry.getSubSystem(strSubSystem);

		if ((subsystem != null) && (subsystem.isConnected()))

		{
			Object objSubsystem = null;
			try {
				objSubsystem = subsystem.getObjectWithAbsoluteName(strRemoteFilePath,
						new NullProgressMonitor());
			} catch (Exception localException1) {
				return null;
			}
			if (objSubsystem != null)
				return objSubsystem;
		} else if (subsystem != null)

		{
			boolean offline = subsystem.isOffline();
			subsystem.getHost().setOffline(true);
			try {
				Object objSubsystem = null;

				objSubsystem = subsystem.getObjectWithAbsoluteName(strRemoteFilePath,
						new NullProgressMonitor());
				subsystem.getHost().setOffline(true);
				if (objSubsystem != null) {
					return objSubsystem;

				}

			} catch (Exception localException2) {
				return null;
			} finally {
				subsystem.getHost().setOffline(offline);
			}
			subsystem.getHost().setOffline(offline);

		}

		return null;
	}
}
