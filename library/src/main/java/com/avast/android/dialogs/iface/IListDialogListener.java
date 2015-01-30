package com.avast.android.dialogs.iface;

/**
 * Interface for ListDialogFragment
 */
public interface IListDialogListener  extends ISimpleDialogCancelListener{

    public void onListItemSelected(String value, int number, int requestCode);
}
