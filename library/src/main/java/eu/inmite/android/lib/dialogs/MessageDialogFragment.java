/*
 * Copyright (c) 2013. Inmite s.r.o. (www.inmite.eu).
 * All rights reserved. This source code can be used only for purposes specified
 * by the given license contract signed by the rightful deputy of Inmite s.r.o.
 * This source code can be used only by the owner of the license.
 * Any disputes arising in respect of this agreement (license) shall be brought
 * before the Municipal Court of Prague.
 */

package eu.inmite.android.lib.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import eu.inmite.apps.smsjizdenka.R;

/**
 * Dialog for displaying message from evil empire.
 *
 * @author David Vávra (david@inmite.eu)
 */
public class MessageDialogFragment extends BaseDialogFragment {

	public static String TAG = "message";

	public static MessageDialogFragment newInstance(String message) {
		MessageDialogFragment dialog = new MessageDialogFragment();
		Bundle args = new Bundle();
		args.putString("message", message);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Builder builder = new Builder(this, c, inflater, container);
		builder.setMessage(getMessage());
		builder.setPositiveButton(R.string.tickets_close, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		return builder.create();
	}

	private String getMessage() {
		return getArguments().getString("message");
	}
}
