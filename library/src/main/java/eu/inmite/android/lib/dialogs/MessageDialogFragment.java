/*
 * Copyright (c) 2013. Inmite s.r.o. (www.inmite.eu).
 * All rights reserved. This source code can be used only for purposes specified
 * by the given license contract signed by the rightful deputy of Inmite s.r.o.
 * This source code can be used only by the owner of the license.
 * Any disputes arising in respect of this agreement (license) shall be brought
 * before the Municipal Court of Prague.
 */

package main.java.eu.inmite.android.lib.dialogs;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import eu.inmite.android.lib.dialogs.R;

/**
 * Dialog for displaying simple message.
 *
 * @author David Vávra (david@inmite.eu)
 */
public class MessageDialogFragment extends BaseDialogFragment {

	public static String TAG = "message";
	private static String ARG_MESSAGE = "message";
	private static String ARG_TITLE = "title";

	public static void show(FragmentActivity c, String message) {
		show(c, null, message);
	}

	public static void show(FragmentActivity c, String title, String message) {
		MessageDialogFragment dialog = new MessageDialogFragment();
		Bundle args = new Bundle();
		args.putString(ARG_MESSAGE, message);
		args.putString(ARG_TITLE, title);
		dialog.setArguments(args);
		dialog.show(c.getSupportFragmentManager(), TAG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Builder builder = new Builder(this, c, inflater, container);
		if (!TextUtils.isEmpty(getTitle())) {
			builder.setTitle(getTitle());
		}
		builder.setMessage(getMessage());
		builder.setPositiveButton(R.string.close, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		return builder.create();
	}

	private String getMessage() {
		return getArguments().getString(ARG_MESSAGE);
	}

	private String getTitle() {
		return getArguments().getString(ARG_TITLE);
	}
}
