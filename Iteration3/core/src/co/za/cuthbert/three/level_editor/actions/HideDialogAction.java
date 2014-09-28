package co.za.cuthbert.three.level_editor.actions;

import co.za.cuthbert.three.level_editor.Dialog;

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
