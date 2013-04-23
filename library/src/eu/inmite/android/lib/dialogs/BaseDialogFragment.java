package eu.inmite.android.lib.dialogs;
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

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 * Base dialog fragment for all your dialogs, stylable and same design on Android 2.2+.
 *
 * @author David Vávra (david@inmite.eu)
 */
public abstract class BaseDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new Dialog(getActivity(), R.style.SDL_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Builder builder = new Builder(this, getActivity(), inflater, container);
		return build(builder).create();
	}

	public abstract Builder build(Builder initialBuilder);

	@Override
	public void onDestroyView() {
		// bug in the compatibility library
		if (getDialog() != null && getRetainInstance()) {
			getDialog().setDismissMessage(null);
		}
		super.onDestroyView();
	}

	/**
	 * Custom dialog builder
	 */
	public static class Builder {

		private DialogFragment mDialogFragment;
		private Context mContext;
		private ViewGroup mContainer;
		private LayoutInflater mInflater;
		private CharSequence mTitle = null;
		private CharSequence mPositiveButtonText;
		private View.OnClickListener mPositiveButtonListener;
		private CharSequence mNegativeButtonText;
		private View.OnClickListener mNegativeButtonListener;
		private CharSequence mNeutralButtonText;
		private View.OnClickListener mNeutralButtonListener;
		private CharSequence mMessage;
		private View mView;
		private boolean mViewSpacingSpecified;
		private int mViewSpacingLeft;
		private int mViewSpacingTop;
		private int mViewSpacingRight;
		private int mViewSpacingBottom;
		private Button vPossitiveButton;
		private ListAdapter mListAdapter;
		private AdapterView.OnItemClickListener mOnItemClickListener;

		public Builder(DialogFragment dialogFragment, Context context, LayoutInflater inflater, ViewGroup container) {
			this.mDialogFragment = dialogFragment;
			this.mContext = context;
			this.mContainer = container;
			this.mInflater = inflater;
		}

		public Builder setTitle(int titleId) {
			this.mTitle = mContext.getText(titleId);
			return this;
		}

		public Builder setTitle(CharSequence title) {
			this.mTitle = title;
			return this;
		}

		public Builder setPositiveButton(int textId, final View.OnClickListener listener) {
			mPositiveButtonText = mContext.getText(textId);
			mPositiveButtonListener = listener;
			return this;
		}

		public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
			mPositiveButtonText = text;
			mPositiveButtonListener = listener;
			return this;
		}

		public Builder setNegativeButton(int textId, final View.OnClickListener listener) {
			mNegativeButtonText = mContext.getText(textId);
			mNegativeButtonListener = listener;
			return this;
		}

		public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
			mNegativeButtonText = text;
			mNegativeButtonListener = listener;
			return this;
		}

		public Builder setNeutralButton(int textId, final View.OnClickListener listener) {
			mNeutralButtonText = mContext.getText(textId);
			mNeutralButtonListener = listener;
			return this;
		}

		public Builder setNeutralButton(CharSequence text, final View.OnClickListener listener) {
			mNeutralButtonText = text;
			mNeutralButtonListener = listener;
			return this;
		}

		public Builder setMessage(int messageId) {
			mMessage = mContext.getText(messageId);
			return this;
		}

		public Builder setMessage(CharSequence message) {
			mMessage = message;
			return this;
		}

		public Builder setItems(ListAdapter listAdapter, final AdapterView.OnItemClickListener listener) {
			mListAdapter = listAdapter;
			mOnItemClickListener = listener;
			return this;
		}

		public Builder setView(View view) {
			mView = view;
			mViewSpacingSpecified = false;
			return this;
		}

		public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop,
		                       int viewSpacingRight, int viewSpacingBottom) {
			mView = view;
			mViewSpacingSpecified = true;
			mViewSpacingLeft = viewSpacingLeft;
			mViewSpacingTop = viewSpacingTop;
			mViewSpacingRight = viewSpacingRight;
			mViewSpacingBottom = viewSpacingBottom;
			return this;
		}

		public View create() {
			View v = getDialogLayoutAndInitTitle();

			LinearLayout content = (LinearLayout) v.findViewById(R.id.sdl__content);

			if (mMessage != null) {
				View viewMessage = mInflater.inflate(R.layout.dialog_part_message, content, false);
				TextView tvMessage = (TextView) viewMessage.findViewById(R.id.sdl__message);
				tvMessage.setText(mMessage);
				content.addView(viewMessage);
			}

			if (mView != null) {
				FrameLayout customPanel = (FrameLayout) mInflater.inflate(R.layout.dialog_part_custom, content, false);
				FrameLayout custom = (FrameLayout) customPanel.findViewById(R.id.sdl__custom);
				custom.addView(mView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				if (mViewSpacingSpecified) {
					custom.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
				}
				content.addView(customPanel);
			}

			if (mListAdapter != null) {
				ListView list = (ListView) mInflater.inflate(R.layout.dialog_part_list, content, false);
				list.setAdapter(mListAdapter);
				list.setOnItemClickListener(mOnItemClickListener);
				content.addView(list);
			}
			addButtons(content);

			return v;
		}

		private View getDialogLayoutAndInitTitle() {
			View v = mInflater.inflate(R.layout.dialog_part_title, mContainer, false);
			TextView tvTitle = (TextView) v.findViewById(R.id.sdl__title);
			View viewTitleDivider = v.findViewById(R.id.sdl__titleDivider);
			if (mTitle != null) {
				tvTitle.setText(mTitle);
			} else {
				tvTitle.setVisibility(View.GONE);
				viewTitleDivider.setVisibility(View.GONE);
			}
			return v;
		}

		private void addButtons(LinearLayout llListDialog) {
			if (mNegativeButtonText != null || mNeutralButtonText != null || mPositiveButtonText != null) {
				View viewButtonPanel = mInflater.inflate(R.layout.dialog_part_button_panel, llListDialog, false);
				LinearLayout llButtonPanel = (LinearLayout) viewButtonPanel.findViewById(R.id.dialog_button_panel);

				if (mNegativeButtonText != null) {
					Button btn = (Button) mInflater.inflate(R.layout.dialog_part_button, llButtonPanel, false);
					btn.setText(mNegativeButtonText);
					btn.setOnClickListener(mNegativeButtonListener);
					llButtonPanel.addView(btn);
				}
				if (mNeutralButtonText != null) {
					if (mNegativeButtonText != null) {
						addDivider(llButtonPanel);
					}
					Button btn = (Button) mInflater.inflate(R.layout.dialog_part_button, llButtonPanel, false);
					btn.setText(mNeutralButtonText);
					btn.setOnClickListener(mNeutralButtonListener);
					llButtonPanel.addView(btn);
				}
				if (mPositiveButtonText != null) {
					if (mNegativeButtonText != null || mNeutralButtonText != null) {
						addDivider(llButtonPanel);
					}
					vPossitiveButton = (Button) mInflater.inflate(R.layout.dialog_part_button, llButtonPanel, false);
					vPossitiveButton.setText(mPositiveButtonText);
					vPossitiveButton.setOnClickListener(mPositiveButtonListener);
					llButtonPanel.addView(vPossitiveButton);
				}

				llListDialog.addView(viewButtonPanel);
			}
		}

		private void addDivider(ViewGroup parent) {
			mInflater.inflate(R.layout.dialog_part_button_separator, parent, true);
		}
	}
}
