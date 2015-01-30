package com.avast.android.dialogs.iface;

/**
 * Interface for ListDialogFragment for modes: CHOICE_MODE_NONE, CHOICE_MODE_SINGLE
 * Implement it in Activity or Fragment to react to item selection.
 *
 */
public interface IListDialogListener {

    public void onListItemSelected(String value, int number, int requestCode);
}
