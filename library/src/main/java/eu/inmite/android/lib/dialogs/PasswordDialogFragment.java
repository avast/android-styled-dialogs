package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;


public class PasswordDialogFragment extends BaseDialogFragment {
	
	protected static String ARG_MESSAGE = "message";
	protected static String ARG_TITLE = "title";
	protected static String ARG_POSITIVE_BUTTON = "positive_button";
	protected static String ARG_NEGATIVE_BUTTON = "negative_button";
	
	protected int mRequestCode;
	
	private String mPassword;
	private EditText mPasswordText;
	
	public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
		return new SimpleDialogBuilder(context, fragmentManager, PasswordDialogFragment.class);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			mRequestCode = getTargetRequestCode();
		} else {
			Bundle args = getArguments();
			if (args != null) {
				mRequestCode = args.getInt(BaseDialogBuilder.ARG_REQUEST_CODE, 0);
			}
		}
	}
	

	/**
	 * Children can extend this to add more things to base builder.
	 */
	@Override
	protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
		View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_password,null);
		mPasswordText=((EditText)view.findViewById(R.id.password));
		
		builder.setView(view);
		
		final String title = getTitle();
		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		}

		final CharSequence message = getMessage();
		
		TextView messageTextView=(TextView) view.findViewById(R.id.message);
		if (TextUtils.isEmpty(message)) {
			messageTextView.setVisibility(View.GONE);
		} else {
			messageTextView.setVisibility(View.VISIBLE);
			messageTextView.setText(message);
		}

		final String positiveButtonText = getPositiveButtonText();
		if (!TextUtils.isEmpty(positiveButtonText)) {
			builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					populateUsernamePassword();
					IOneStringDialogListener listener = getDialogListener();
					if (listener != null) {
						listener.onPositiveButtonClicked(mRequestCode,mPassword);
					}
					dismiss();
				}
			});
		}

		final String negativeButtonText = getNegativeButtonText();
		if (!TextUtils.isEmpty(negativeButtonText)) {
			builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					populateUsernamePassword();
					IOneStringDialogListener listener = getDialogListener();
					if (listener != null) {
						listener.onNegativeButtonClicked(mRequestCode,mPassword);
					}
					dismiss();
				}
			});
		}
		return builder;
	}
	
	protected void populateUsernamePassword() {
		mPassword=mPasswordText.getText().toString();
		
	}

	protected CharSequence getMessage() {
		return getArguments().getCharSequence(ARG_MESSAGE);
	}

	protected String getTitle() {
		return getArguments().getString(ARG_TITLE);
	}

	protected String getPositiveButtonText() {
		return getArguments().getString(ARG_POSITIVE_BUTTON);
	}

	protected String getNegativeButtonText() {
		return getArguments().getString(ARG_NEGATIVE_BUTTON);
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		ISimpleDialogCancelListener listener = getCancelListener();
		if (listener != null) {
			listener.onCancelled(mRequestCode);
		}
	}

	protected IOneStringDialogListener getDialogListener() {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			if (targetFragment instanceof IOneStringDialogListener) {
				return (IOneStringDialogListener) targetFragment;
			}
		} else {
			if (getActivity() instanceof IOneStringDialogListener) {
				return (IOneStringDialogListener) getActivity();
			}
		}
		return null;
	}
	
	protected ISimpleDialogCancelListener getCancelListener() {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			if (targetFragment instanceof ISimpleDialogCancelListener) {
				return (ISimpleDialogCancelListener) targetFragment;
			}
		} else {
			if (getActivity() instanceof ISimpleDialogCancelListener) {
				return (ISimpleDialogCancelListener) getActivity();
			}
		}
		return null;
	}
	
	public static class SimpleDialogBuilder extends BaseDialogBuilder<SimpleDialogBuilder> {
		private CharSequence mMessage;
		private String mTitle;
		private String mNegativeButtonText;
		private String mPositiveButtonText;

		protected SimpleDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
			super(context, fragmentManager, clazz);
		}

		@Override
		protected SimpleDialogBuilder self() {
			return this;
		}
		
		public SimpleDialogBuilder setTitle(int titleResourceId) {
			mTitle = mContext.getString(titleResourceId);
			return this;
		}


		public SimpleDialogBuilder setTitle(String title) {
			mTitle = title;
			return this;
		}

		public SimpleDialogBuilder setMessage(int messageResourceId) {
			mMessage = mContext.getText(messageResourceId);
			return this;
		}

		/**
		 * Allow to set resource string with HTML formatting and bind %s,%i.
		 * This is workaround for https://code.google.com/p/android/issues/detail?id=2923
		 */
		public SimpleDialogBuilder setMessage(int resourceId, Object... formatArgs){
			mMessage = Html.fromHtml(String.format(Html.toHtml(new SpannedString(mContext.getText(resourceId))), formatArgs));
			return this;
		}

		public SimpleDialogBuilder setMessage(CharSequence message) {
			mMessage = message;
			return this;
		}
		
		public SimpleDialogBuilder setPositiveButtonText(int textResourceId) {
			mPositiveButtonText = mContext.getString(textResourceId);
			return this;
		}

		public SimpleDialogBuilder setPositiveButtonText(String text) {
			mPositiveButtonText = text;
			return this;
		}

		public SimpleDialogBuilder setNegativeButtonText(int textResourceId) {
			mNegativeButtonText = mContext.getString(textResourceId);
			return this;
		}

		public SimpleDialogBuilder setNegativeButtonText(String text) {
			mNegativeButtonText = text;
			return this;
		}
		
		@Override
		protected Bundle prepareArguments() {
			Bundle args=new Bundle();
			args.putCharSequence(SimpleDialogFragment.ARG_MESSAGE, mMessage);
			args.putString(SimpleDialogFragment.ARG_TITLE, mTitle);
			args.putString(SimpleDialogFragment.ARG_POSITIVE_BUTTON, mPositiveButtonText);
			args.putString(SimpleDialogFragment.ARG_NEGATIVE_BUTTON, mNegativeButtonText);
			return args;
		}
	}
}
