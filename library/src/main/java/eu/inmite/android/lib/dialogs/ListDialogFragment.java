package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

/**
 * Dialog with a list of options. Implement {@link eu.inmite.android.lib.dialogs.IListDialogListener} to handle selection.
 */
public class ListDialogFragment extends BaseDialogFragment {

	public static SimpleListDialogBuilder createBuilder(Context context,
	                                                    FragmentManager fragmentManager) {
		return new SimpleListDialogBuilder(context, fragmentManager);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getArguments() == null) {
			throw new IllegalArgumentException(
				"use SimpleListDialogBuilder to construct this dialog");
		}
	}

	public static class SimpleListDialogBuilder extends BaseDialogBuilder<SimpleListDialogBuilder> {

		private String title;

		private String[] items;

		private String cancelButtonText;

		private boolean mShowDefaultButton = true;

		public SimpleListDialogBuilder(Context context, FragmentManager fragmentManager) {
			super(context, fragmentManager, ListDialogFragment.class);
		}

		@Override
		protected SimpleListDialogBuilder self() {
			return this;
		}

		private Resources getResources() {
			return mDialogParams.context.getResources();
		}

		public SimpleListDialogBuilder setItems(String[] items) {
			this.items = items;
			return this;
		}

		public SimpleListDialogBuilder setItems(int itemsArrayResID) {
			this.items = getResources().getStringArray(itemsArrayResID);
			return this;
		}

		public SimpleListDialogBuilder setCancelButtonText(String text) {
			this.cancelButtonText = text;
			return this;
		}

		public SimpleListDialogBuilder setCancelButtonText(int cancelBttTextResID) {
			this.cancelButtonText = getResources().getString(cancelBttTextResID);
			return this;
		}

		@Override
		public ListDialogFragment show() {
			return (ListDialogFragment)super.show();
		}

		@Override
		protected Bundle prepareArguments() {
			if (mShowDefaultButton && cancelButtonText == null) {
				cancelButtonText = mDialogParams.context.getString(R.string.dialog_close);
			}

			Bundle args = new Bundle();
			args.putString(BaseDialogFragment.ARG_TITLE, title);
			args.putString(BaseDialogFragment.ARG_POSITIVE_BUTTON, cancelButtonText);
			args.putStringArray(BaseDialogFragment.ARG_ITEMS, items);

			return args;
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		IListDialogListener onListItemSelectedListener = getDialogListener();
		if (onListItemSelectedListener != null) {
			onListItemSelectedListener.onCancelled();
		}
	}

	@Override
	protected Builder build(Builder builder) {
		final String title = getTitle();
		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		}

		if (!TextUtils.isEmpty(getPositiveButtonText())) {
			builder.setPositiveButton(getPositiveButtonText(), new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					IListDialogListener onListItemSelectedListener = getDialogListener();
					if (onListItemSelectedListener != null) {
						onListItemSelectedListener.onCancelled();
					}
					dismiss();
				}
			});
		}

		final String[] items = getItems();
		if (items != null && items.length > 0) {
			ListAdapter adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.dialog_list_item,
				R.id.list_item_text,
				items);

			builder.setItems(adapter, 0, new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					IListDialogListener onListItemSelectedListener = getDialogListener();
					if (onListItemSelectedListener != null) {
						onListItemSelectedListener
							.onListItemSelected(getItems()[position], position);
						dismiss();
					}
				}
			});
		}

		return builder;
	}

	private IListDialogListener getDialogListener() {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			if (targetFragment instanceof IListDialogListener) {
				return (IListDialogListener)targetFragment;
			}
		} else {
			if (getActivity() instanceof IListDialogListener) {
				return (IListDialogListener)getActivity();
			}
		}
		return null;
	}
}
