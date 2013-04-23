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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import eu.inmite.android.lib.dialogs.BaseDialogFragment;

/**
 * Sample implementation of eu.inmite.android.lib.dialogs.BaseDialogFragment - styled list of items.
 *
 * @author David Vávra (david@inmite.eu)
 */
public class FavoriteCharacterDialogFragment extends BaseDialogFragment {

	public static String TAG = "list";
	private static String ARG_TITLE = "title";
	private static String ARG_ITEMS = "items";
	IFavoriteCharacterDialogListener mListener;

	public static void show(FragmentActivity activity, String title, String[] items) {
		FavoriteCharacterDialogFragment dialog = new FavoriteCharacterDialogFragment();
		Bundle args = new Bundle();
		args.putString(ARG_TITLE, title);
		args.putStringArray(ARG_ITEMS, items);
		dialog.setArguments(args);
		dialog.show(activity.getSupportFragmentManager(), TAG);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null && targetFragment instanceof IFavoriteCharacterDialogListener) {
			mListener = (IFavoriteCharacterDialogListener) targetFragment;
		} else if (getActivity() instanceof IFavoriteCharacterDialogListener) {
			mListener = (IFavoriteCharacterDialogListener) getActivity();
		}
	}

	@Override
	public Builder build(Builder builder) {
		builder.setTitle(getTitle());
		ListAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_list,
				R.id.list_item_text,
				getItems());
		builder.setItems(adapter, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mListener != null) {
					mListener.onListItemSelected(getItems()[position], position);
					dismiss();
				}
			}
		});
		return builder;
	}

	private String getTitle() {
		return getArguments().getString(ARG_TITLE);
	}

	private String[] getItems() {
		return getArguments().getStringArray(ARG_ITEMS);
	}
}
