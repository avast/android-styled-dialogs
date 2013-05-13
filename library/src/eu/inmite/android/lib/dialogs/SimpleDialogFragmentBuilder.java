package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/** Builder to ease the construction of SimpleDialogFragment
 *
 * @author Tomáš Kypta
 * @since 13/05/2013
 */
public class SimpleDialogFragmentBuilder {

	private FragmentActivity mActivity;

	private String mTitle;
	private String mMessage;
	private String mPositiveButtonText;
	private String mNegativeButtonText;
	private boolean mCancelable = true;
	private Fragment mTargetFragment;
	private int mRequestCode;

	public SimpleDialogFragmentBuilder(FragmentActivity activity) {
		mActivity = activity;
	}


	public SimpleDialogFragmentBuilder setTitle(int titleResourceId) {
		mTitle = mActivity.getString(titleResourceId);
		return this;
	}

	public SimpleDialogFragmentBuilder setTitle(String title) {
		mTitle = title;
		return this;
	}

	public SimpleDialogFragmentBuilder setMessage(int messageResourceId) {
		mMessage = mActivity.getString(messageResourceId);
		return this;
	}

	public SimpleDialogFragmentBuilder setMessage(String message) {
		mMessage = message;
		return this;
	}

	public SimpleDialogFragmentBuilder setPositiveButtonText(int textResourceId) {
		mPositiveButtonText = mActivity.getString(textResourceId);
		return this;
	}

	public SimpleDialogFragmentBuilder setPositiveButtonText(String text) {
		mPositiveButtonText = text;
		return this;
	}

	public SimpleDialogFragmentBuilder setNegativeButtonText(int textResourceId) {
		mNegativeButtonText = mActivity.getString(textResourceId);
		return this;
	}

	public SimpleDialogFragmentBuilder setNegativeButtonText(String text) {
		mNegativeButtonText = text;
		return this;
	}

	public SimpleDialogFragmentBuilder setCancelable(boolean cancelable) {
		mCancelable = cancelable;
		return this;
	}

	public SimpleDialogFragmentBuilder setTargetFragment(Fragment fragment) {
		mTargetFragment = fragment;
		return this;
	}

	public SimpleDialogFragmentBuilder setRequestCode(int requestCode) {
		mRequestCode = requestCode;
		return this;
	}


	public void build() {
		SimpleDialogFragment.show(mActivity, mTargetFragment, mRequestCode,
				mMessage, mTitle, mPositiveButtonText, mNegativeButtonText,
				mCancelable);
	}
}
