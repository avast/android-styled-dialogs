package com.avast.android.dialogs.iface;

import com.avast.android.dialogs.core.BaseDialogFragment;

/**
 * Interface for ListDialogFragment in modes: CHOICE_MODE_NONE, CHOICE_MODE_SINGLE
 * Implement it in Activity or Fragment to react to item selection.
 */
public interface IListDialogListener {

    void onListItemSelected(CharSequence value, int number, BaseDialogFragment dialog);
}
