package com.avast.android.dialogs.iface;

/**
 * Implement this interface in Activity or Fragment to react to neutral dialog buttons.
 *
 * @author Tomáš Kypta
 * @since 2.1.0
 */
public interface INeutralButtonDialogListener {

    public void onNeutralButtonClicked(int requestCode);
}
