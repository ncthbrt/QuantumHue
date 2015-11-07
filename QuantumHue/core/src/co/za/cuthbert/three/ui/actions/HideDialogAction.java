package co.za.cuthbert.three.ui.actions;

import co.za.cuthbert.three.ui.Dialog;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class HideDialogAction extends ButtonAction {
    private Dialog dialog;

    public HideDialogAction(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void actionPerformed(String command) {
        dialog.hide();
    }

}
