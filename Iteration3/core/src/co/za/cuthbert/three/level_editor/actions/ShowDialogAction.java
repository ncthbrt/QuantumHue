package co.za.cuthbert.three.level_editor.actions;

import co.za.cuthbert.three.level_editor.Dialog;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class ShowDialogAction extends ButtonAction {
    private Dialog dialog;

    public ShowDialogAction(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void actionPerformed(String command) {
        dialog.show();
    }
}
