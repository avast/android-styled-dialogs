/**
 * Copyright (activity) ${year}, Inmite s.r.o. (www.inmite.eu). All rights reserved.
 * <p/>
 * This source code can be used only for purposes specified by the given license contract
 * signed by the rightful deputy of Inmite s.r.o. This source code can be used only
 * by the owner of the license.
 * <p/>
 * Any disputes arising in respect of this agreement (license) shall be brought
 * before the Municipal Court of Prague.
 */
package main.java.eu.inmite.android.lib.dialogs;

/**
 * Implement this interface in Activity or Fragment to react to positive and negative buttons.
 * @author David Vávra (david@inmite.eu)
 */
public interface ISimpleDialogListener {
	public void onPositiveButtonClicked();
	public void onNegativeButtonClicked();
}
