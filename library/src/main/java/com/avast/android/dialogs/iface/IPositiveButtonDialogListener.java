package com.avast.android.dialogs.iface;

/** Implement this interface in Activity or Fragment to react to positive dialog buttons.
 *
 *  Created by Tomáš Kypta.
 */
public interface IPositiveButtonDialogListener {

    public void onPositiveButtonClicked(int requestCode);
}
