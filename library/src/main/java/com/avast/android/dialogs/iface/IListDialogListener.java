package com.avast.android.dialogs.iface;

/** Interface for ListDialogFragment, implement it in Activity or Fragment to react to item selection
 *
 */
public interface IListDialogListener  extends ISimpleDialogCancelListener{

    public void onListItemSelected(String value, int number, int requestCode);
}
