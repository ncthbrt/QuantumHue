package com.deepwallgames.quantumhue.ui.actions;

import com.deepwallgames.quantumhue.ui.Dialog;

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
