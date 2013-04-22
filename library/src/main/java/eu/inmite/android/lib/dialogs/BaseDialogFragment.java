package eu.inmite.android.lib.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockDialogFragment;
import eu.inmite.android.fw.activity.BaseActivity;
import eu.inmite.apps.smsjizdenka.R;

import java.util.List;

/**
 * Custom dialog, same design on Android 2.2+.
 *
 * @author David Vávra (david@inmite.eu)
 */
public abstract class BaseDialogFragment extends SherlockDialogFragment {

	protected BaseActivity c;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.c = (BaseActivity) activity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new Dialog(c, R.style.dialog);
	}

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
		private List<String> mItemsList;
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

		public Builder setItems(List<String> itemsList, final AdapterView.OnItemClickListener listener) {
			mItemsList = itemsList;
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

			LinearLayout content = (LinearLayout) v.findViewById(R.id.content);

			if (mMessage != null) {
				View viewMessage = mInflater.inflate(R.layout.dialog_part_message, content, false);
				TextView tvMessage = (TextView) viewMessage.findViewById(R.id.message);
				tvMessage.setText(mMessage);
				content.addView(viewMessage);
			}

			if (mView != null) {
				if (mView != null) {
					FrameLayout customPanel = (FrameLayout) mInflater.inflate(R.layout.dialog_part_custom, content, false);
					FrameLayout custom = (FrameLayout) customPanel.findViewById(R.id.custom);
					custom.addView(mView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
					if (mViewSpacingSpecified) {
						custom.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
					}
					content.addView(customPanel);
				}
			}

			if (mItemsList != null) {
				ListAdapter adapter = new ArrayAdapter<String>(mContext, R.layout.item_city, R.id.list_item_text,
						mItemsList);

				ListView list = (ListView) mInflater.inflate(R.layout.dialog_part_list, content, false);
				list.setAdapter(adapter);
				list.setOnItemClickListener(mOnItemClickListener);
				content.addView(list);
			}
			addButtons(content);

			return v;
		}

		private View getDialogLayoutAndInitTitle() {
			View v = mInflater.inflate(R.layout.dialog_part_title, mContainer, false);
			TextView tvTitle = (TextView) v.findViewById(R.id.title);
			View viewTitleDivider = v.findViewById(R.id.titleDivider);
			if (mTitle != null) {
				tvTitle.setText(mTitle);
			} else {
				tvTitle.setVisibility(View.GONE);
				viewTitleDivider.setVisibility(View.GONE);
			}
			return v;
		}

		/**
		 * Allow enable/disable possitive button.
		 * This methos is available after call {@link #create()}.
		 *
		 * @param enabled
		 */
		public void setPossitiveButtonEnabled(boolean enabled) {
			if (vPossitiveButton != null) {
				vPossitiveButton.setEnabled(enabled);
			}
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
					vPossitiveButton = (Button) mInflater.inflate(R.layout.dialog_part_button_important, llButtonPanel, false);
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
