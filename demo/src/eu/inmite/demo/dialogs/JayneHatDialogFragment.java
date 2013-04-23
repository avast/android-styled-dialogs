/**
 * Copyright (c) ${year}, Inmite s.r.o. (www.inmite.eu). All rights reserved.
 * <p/>
 * This source code can be used only for purposes specified by the given license contract
 * signed by the rightful deputy of Inmite s.r.o. This source code can be used only
 * by the owner of the license.
 * <p/>
 * Any disputes arising in respect of this agreement (license) shall be brought
 * before the Municipal Court of Prague.
 */
package eu.inmite.demo.dialogs;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import main.java.eu.inmite.android.lib.dialogs.SimpleDialogFragment;

/**
 * Sample implementation of BaseDialogFragment - custom view by extending {@link SimpleDialogFragment}.
 *
 * @author David Vávra (david@inmite.eu)
 */
public class JayneHatDialogFragment extends SimpleDialogFragment {

	public static String TAG = "jayne";

	public static void show(FragmentActivity activity) {
		new JayneHatDialogFragment().show(activity.getSupportFragmentManager(), TAG);
	}

	@Override
	public Builder build(Builder builder) {
		builder.setTitle("Jayne's hat");
		builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.item_jayne_hat, null));
		builder.setPositiveButton("I want one", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onPositiveButtonClicked();
				}
				dismiss();
			}
		});
		return builder;
	}
}
