package com.avast.android.dialogs.iface;

/**
 * Implement this interface in Activity or Fragment to react to positive dialog buttons.
 *
 * @author Kirill Volkov
 * @since 2.1.0
 */
public interface ICheckBoxDialogListener {

    public void onCheckBoxChanged(int requestCode, boolean isChecked);
}
