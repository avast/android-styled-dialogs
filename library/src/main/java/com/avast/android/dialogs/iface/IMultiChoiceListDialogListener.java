package com.avast.android.dialogs.iface;

/**
 * Interface for ListDialogFragment for modes: CHOICE_MODE_MULTIPLE
 * Implement it in Activity or Fragment to react to item selection.
 *
 * @since 2.1.0
 */
public interface IMultiChoiceListDialogListener {

    public void onListItemsSelected(String[] values, int[] selectedPositions, int requestCode);
}
