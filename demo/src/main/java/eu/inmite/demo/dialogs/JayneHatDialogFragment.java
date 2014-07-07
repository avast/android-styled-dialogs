/*
 * Copyright 2013 Inmite s.r.o. (www.inmite.eu).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.inmite.demo.dialogs;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import eu.inmite.android.lib.dialogs.BaseDialogFragment;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

/**
 * Sample implementation of eu.inmite.android.lib.dialogs.BaseDialogFragment - custom view by extending {@link SimpleDialogFragment}.
 *
 * @author David Vávra (david@inmite.eu)
 */
public class JayneHatDialogFragment extends SimpleDialogFragment {

	public static String TAG = "jayne";

	public static void show(FragmentActivity activity) {
		new JayneHatDialogFragment().show(activity.getSupportFragmentManager(), TAG);
	}

	@Override
	public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
		builder.setTitle("Jayne's hat");
		builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.item_jayne_hat, null));
		builder.setPositiveButton("I want one", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ISimpleDialogListener listener = getDialogListener();
				if (listener != null) {
					listener.onPositiveButtonClicked(0);
				}
				dismiss();
			}
		});
		return builder;
	}
}
