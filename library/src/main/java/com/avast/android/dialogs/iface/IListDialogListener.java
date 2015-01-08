package com.avast.android.dialogs.iface;

/**
 * Interface for ListDialogFragment
 */
public interface IListDialogListener {

    public void onListItemSelected(String value, int number);
    public void onCancelled();
}
