package com.avast.android.dialogs.iface;

/**
 * Implement this interface in Activity or Fragment to react to positive dialog buttons.
 *
 * @author Tomáš Kypta
 * @since 2.1.0
 */
public interface IPositiveButtonDialogListener {

    public void onPositiveButtonClicked(int requestCode);
}
