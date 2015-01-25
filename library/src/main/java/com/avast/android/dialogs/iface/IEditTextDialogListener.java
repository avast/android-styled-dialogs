package com.avast.android.dialogs.iface;

/**
 * IEditTextDialogListener.java
 *
 * @author eMan s.r.o.
 * @project android-styled-dialogs
 * @package com.avast.android.dialogs.iface
 * @since 1/25/2015
 */
public interface IEditTextDialogListener {
    public void onPositiveButtonClicked(int requestCode, String result);

    public void onNegativeButtonClicked(int requestCode, String result);

    public void onNeutralButtonClicked(int requestCode, String result);
}
