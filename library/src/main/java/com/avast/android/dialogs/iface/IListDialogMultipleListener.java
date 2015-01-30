package com.avast.android.dialogs.iface;

/**
 * Interface for ListDialogFragment
 */
public interface IListDialogMultipleListener extends ISimpleDialogCancelListener {

    public void onListItemsSelected(String[] values, int[] selectedPositions, int requestCode);
}
