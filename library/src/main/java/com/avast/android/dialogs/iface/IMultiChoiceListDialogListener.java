package com.avast.android.dialogs.iface;

import com.avast.android.dialogs.core.BaseDialogFragment;

/**
 * Interface for ListDialogFragment in modes: CHOICE_MODE_MULTIPLE
 * Implement it in Activity or Fragment to react to item selection.
 *
 * @since 2.1.0
 */
public interface IMultiChoiceListDialogListener {

    void onListItemsSelected(CharSequence[] values, int[] selectedPositions, BaseDialogFragment dialog);
}
