package com.avast.android.dialogs.iface;

import com.avast.android.dialogs.core.BaseDialogFragment;

/**
 * Implement this interface in Activity or Fragment to react to neutral dialog buttons.
 *
 * @author Tomáš Kypta
 * @since 2.1.0
 */
public interface INeutralButtonDialogListener {

    void onNeutralButtonClicked(BaseDialogFragment dialog);
}
