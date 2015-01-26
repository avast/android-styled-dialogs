package com.avast.android.dialogs.iface;

/** Implement this interface in Activity or Fragment to react to negative dialog buttons.
 *
 *  Created by Tomáš Kypta.
 */
public interface INegativeButtonDialogListener {

    public void onNegativeButtonClicked(int requestCode);
}
