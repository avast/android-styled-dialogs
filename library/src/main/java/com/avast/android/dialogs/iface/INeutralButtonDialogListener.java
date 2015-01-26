package com.avast.android.dialogs.iface;

/** Implement this interface in Activity or Fragment to react to neutral dialog buttons.
 *
 *  Created by Tomáš Kypta.
 */
public interface INeutralButtonDialogListener {

    public void onNeutralButtonClicked(int requestCode);
}
