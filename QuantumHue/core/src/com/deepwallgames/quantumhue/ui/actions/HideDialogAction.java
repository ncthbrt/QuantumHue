package com.deepwallgames.quantumhue.ui.actions;

import com.deepwallgames.quantumhue.ui.Dialog;

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
