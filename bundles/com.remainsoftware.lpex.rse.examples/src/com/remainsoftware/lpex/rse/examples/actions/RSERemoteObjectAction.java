package com.remainsoftware.lpex.rse.examples.actions;

import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;

import com.ibm.lpex.core.LpexAction;
import com.ibm.lpex.core.LpexView;
import com.remainsoftware.lpex.rse.examples.ui.RSERemoteObjectInfoDialog;

public class RSERemoteObjectAction implements LpexAction {

	@Override
	public boolean available(LpexView arg0) {
		return true;
	}

	@Override
	public void doAction(final LpexView pView) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				new RSERemoteObjectInfoDialog(pView, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL).open();
			}
		});
	}
}

