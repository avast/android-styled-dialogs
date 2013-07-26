package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Internal base builder that holds common values for all dialog fragment builders.
 *
 * @author Tomas Vondracek
 */
abstract class BaseDialogBuilder<T extends BaseDialogBuilder<T>> {

	public static String ARG_REQUEST_CODE = "request_code";
	public static String DEFAULT_TAG = "simple_dialog";
	public static int DEFAULT_REQUEST_CODE = -42;

	protected final Context mContext;
	protected final FragmentManager mFragmentManager;
	protected final Class<? extends BaseDialogFragment> mClass;

	private Fragment mTargetFragment;
	private boolean mCancelable = true;

	private String mTag = DEFAULT_TAG;
	private int mRequestCode = DEFAULT_REQUEST_CODE;

	public BaseDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
		mFragmentManager = fragmentManager;
		mContext = context.getApplicationContext();
		mClass = clazz;
	}

	protected abstract T self();

	protected abstract Bundle prepareArguments();

	public T setCancelable(boolean cancelable) {
		mCancelable = cancelable;
		return self();
	}

	public T setTargetFragment(Fragment fragment, int requestCode) {
		mTargetFragment = fragment;
		mRequestCode = requestCode;
		return self();
	}

	public T setRequestCode(int requestCode) {
		mRequestCode = requestCode;
		return self();
	}

	public T setTag(String tag) {
		mTag = tag;
		return self();
	}


	public DialogFragment show() {
		final Bundle args = prepareArguments();

		final BaseDialogFragment fragment = (BaseDialogFragment) Fragment.instantiate(mContext, mClass.getName(), args);
		if (mTargetFragment != null) {
			fragment.setTargetFragment(mTargetFragment, mRequestCode);
		} else {
			args.putInt(ARG_REQUEST_CODE, mRequestCode);
		}
		fragment.setCancelable(mCancelable);
		fragment.show(mFragmentManager, mTag);

		return fragment;
	}
}
