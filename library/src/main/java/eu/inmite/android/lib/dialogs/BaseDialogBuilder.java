package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * Internal base builder that holds common values for all dialog fragment builders.
 *
 * @author Tomas Vondracek (inmite.eu), Michal Reiter (avast.com)
 */
public abstract class BaseDialogBuilder<T extends BaseDialogBuilder<T>> {

	protected final FragmentManager mFragmentManager;
	protected final Class<? extends BaseDialogFragment> mClass;
	protected final DialogParams mDialogParams;

	private Fragment mTargetFragment;

	/**
	 * Public constructor.
	 *
	 * @param context         Context
	 * @param fragmentManager {@link android.support.v4.app.FragmentManager}
	 * @param clazz           calling class
	 */
	public BaseDialogBuilder(Context context, FragmentManager fragmentManager,
	                         Class<? extends BaseDialogFragment> clazz) {
		mFragmentManager = fragmentManager;
		mClass = clazz;
		mDialogParams = new DialogParams(context);
	}

	protected abstract T self();

	protected abstract Bundle prepareArguments();

	/**
	 * Sets whether dialog can be cancelable.
	 *
	 * @param cancelable true/false (default is true)
	 * @return builder
	 */
	public T setCancelable(boolean cancelable) {
		mDialogParams.cancelable = cancelable;
		return self();
	}

	/**
	 * Sets whether dialog can be canceled on touch outside.
	 *
	 * @param cancelable true/false (default is true)
	 * @return builder
	 */
	public T setCancelableOnTouchOutside(boolean cancelable) {
		mDialogParams.cancelableOnTouchOutside = cancelable;
		if (cancelable) {
			mDialogParams.cancelable = cancelable;
		}
		return self();
	}

	/**
	 * Sets target fragment.
	 *
	 * @param fragment    target fragment
	 * @param requestCode code to identify dialog
	 * @return builder
	 */
	public T setTargetFragment(Fragment fragment, int requestCode) {
		mTargetFragment = fragment;
		mDialogParams.requestCode = requestCode;
		return self();
	}

	/**
	 * Sets request code.
	 *
	 * @param requestCode code to identify dialog
	 * @return builder
	 */
	public T setRequestCode(int requestCode) {
		mDialogParams.requestCode = requestCode;
		return self();
	}

	/**
	 * Sets tag for {@link android.support.v4.app.FragmentTransaction}.
	 *
	 * @param tag fragment tag
	 * @return builder
	 */
	public T setTag(String tag) {
		mDialogParams.tag = tag;
		return self();
	}

	/**
	 * Sets message.
	 *
	 * @param messageResourceId dialog message
	 * @return builder
	 */
	public T setMessage(int messageResourceId) {
		mDialogParams.message = mDialogParams.context.getString(messageResourceId);
		return self();
	}

	/**
	 * Sets message.
	 * <p/>
	 * Allow to set resource string with HTML formatting and bind %s,%i. This is workaround for
	 * https://code.google.com/p/android/issues/detail?id=2923
	 *
	 * @param resourceId dialog message with binders %s, %i
	 * @param formatArgs formatting params for previous string
	 * @return builder
	 */
	public T setMessage(int resourceId, Object... formatArgs) {
		mDialogParams.message = Html.fromHtml(String.format(Html.toHtml(new SpannedString(
			mDialogParams.context.getString(resourceId))), formatArgs));
		return self();
	}

	/**
	 * Sets message.
	 *
	 * @param message dialog message
	 * @return builder
	 */
	public T setMessage(CharSequence message) {
		mDialogParams.message = message;
		return self();
	}

	/**
	 * Sets message.
	 *
	 * @param message dialog message
	 * @return builder
	 */
	public T setMessage(String message) {
		mDialogParams.message = message;
		return self();
	}

	/**
	 * Sets positive button text.
	 *
	 * @param textResourceId dialog label
	 * @return builder
	 */
	public T setPositiveButtonText(int textResourceId) {
		mDialogParams.positiveButtonText = mDialogParams.context.getString(textResourceId);
		return self();
	}

	/**
	 * Sets positive button text.
	 *
	 * @param text button label
	 * @return builder
	 */
	public T setPositiveButtonText(String text) {
		mDialogParams.positiveButtonText = text;
		return self();
	}

	/**
	 * Sets negative button text.
	 *
	 * @param textResourceId button label
	 * @return builder
	 */
	public T setNegativeButtonText(int textResourceId) {
		mDialogParams.negativeButtonText = mDialogParams.context.getString(textResourceId);
		return self();
	}

	/**
	 * Sets negative button text.
	 *
	 * @param text button label
	 * @return builder
	 */
	public T setNegativeButtonText(String text) {
		mDialogParams.negativeButtonText = text;
		return self();
	}

	/**
	 * Sets neutral button text.
	 *
	 * @param textResourceId button label
	 * @return builder
	 */
	public T setNeutralButtonText(int textResourceId) {
		mDialogParams.neutralButtonText = mDialogParams.context.getString(textResourceId);
		return self();
	}

	/**
	 * Sets neutral button text.
	 *
	 * @param text button label
	 * @return builder
	 */
	public T setNeutralButtonText(String text) {
		mDialogParams.neutralButtonText = text;
		return self();
	}

	/**
	 * Sets title.
	 *
	 * @param titleResourceId dialog label
	 * @return builder
	 */
	public T setTitle(int titleResourceId) {
		mDialogParams.title = mDialogParams.context.getString(titleResourceId);
		return self();
	}

	/**
	 * Sets title.
	 *
	 * @param title dialog label
	 * @return builder
	 */
	public T setTitle(String title) {
		mDialogParams.title = title;
		return self();
	}

	/**
	 * Sets title.
	 *
	 * @param title dialog label
	 * @return builder
	 */
	public T setTitle(CharSequence title) {
		mDialogParams.title = title.toString();
		return self();
	}

	/**
	 * When there is neither positive nor negative button, default "close" button is created if it was
	 * enabled.<br/> Default is true.
	 */
	public T hideDefaultButton(boolean hide) {
		mDialogParams.showDefaultButton = !hide;
		return self();
	}

	private BaseDialogFragment create() {
		final Bundle args = prepareArguments();

		final BaseDialogFragment fragment = (BaseDialogFragment)Fragment.instantiate(mDialogParams.context, mClass.getName
			(), args);

		args.putBoolean(BaseDialogFragment.ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mDialogParams.cancelableOnTouchOutside);

		if (mTargetFragment != null) {
			fragment.setTargetFragment(mTargetFragment, mDialogParams.requestCode);
		} else {
			args.putInt(BaseDialogFragment.ARG_REQUEST_CODE, mDialogParams.requestCode);
		}
		fragment.setCancelable(mDialogParams.cancelable);
		return fragment;
	}

	public DialogFragment show() {
		BaseDialogFragment fragment = create();
		fragment.show(mFragmentManager, mDialogParams.tag);
		return fragment;
	}

	/**
	 * Like show() but allows the commit to be executed after an activity's state is saved. This
	 * is dangerous because the commit can be lost if the activity needs to later be restored from
	 * its state, so this should only be used for cases where it is okay for the UI state to change
	 * unexpectedly on the user.
	 */
	public DialogFragment showAllowingStateLoss() {
		BaseDialogFragment fragment = create();
		fragment.showAllowingStateLoss(mFragmentManager, mDialogParams.tag);
		return fragment;
	}

	/**
	 * Holds params for dialog similarly as in {@link AlertController.AlertParams}
	 */
	public static class DialogParams {
		public final Context context;

		public CharSequence title = null;
		public CharSequence message;
		public CharSequence positiveButtonText;
		public CharSequence negativeButtonText;
		public CharSequence neutralButtonText;
		public String[] stringItems;
		public String[] multiChoiceItems;
		public boolean[] multiChoiceSelectedItems;
		public String tag = BaseDialogFragment.DEFAULT_TAG;

		public View.OnClickListener positiveButtonListener;
		public View.OnClickListener negativeButtonListener;
		public View.OnClickListener neutralButtonListener;

		public DialogFragment dialogFragment;
		public ViewGroup container;
		public LayoutInflater layoutInflater;
		public View view;

		public boolean cancelable = true;
		public boolean cancelableOnTouchOutside = true;
		public boolean viewSpacingSpecified;
		public boolean showDefaultButton = true;

		public int viewSpacingLeft;
		public int viewSpacingTop;
		public int requestCode = BaseDialogFragment.DEFAULT_REQUEST_CODE;
		public int viewSpacingRight;
		public int viewSpacingBottom;
		public ListAdapter listAdapter;
		public int listCheckedItemIdx;
		public AdapterView.OnItemClickListener onItemClickListener;
		public Drawable icon;

		public ColorStateList buttonTextColor;

		public int titleTextColor;
		public int titleSeparatorColor;
		public int messageTextColor;

		public int buttonSeparatorColor;
		public int buttonBackgroundColorNormal;
		public int buttonBackgroundColorPressed;
		public int buttonBackgroundColorFocused;

		public int listItemSeparatorColor;
		public int listItemBackgroundColorNormal;
		public int listItemBackgroundColorPressed;
		public int listItemBackgroundColorFocused;

		private DialogParams() {
			context = null;
		}

		public DialogParams(Context context) {
			this.context = context;
		}

	}
}
