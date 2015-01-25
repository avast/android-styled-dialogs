package com.avast.android.dialogs.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.avast.android.dialogs.R;
import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.IEditTextDialogCancelListener;
import com.avast.android.dialogs.iface.IEditTextDialogListener;


public class EditTextDialogFragment extends BaseDialogFragment {

    protected final static String ARG_INIT_TEXT = "init_text";
    protected final static String ARG_HINT = "hint";

    protected final static String ARG_MESSAGE = "message";
    protected final static String ARG_SINGLE_LINE = "singleLine";
    protected final static String ARG_SELECT_ALL = "selectAll";


    protected final static String ARG_TITLE = "title";
    protected final static String ARG_POSITIVE_BUTTON = "positive_button";
    protected final static String ARG_NEUTRAL_BUTTON = "neutral_button";
    protected final static String ARG_NEGATIVE_BUTTON = "negative_button";
    public static final String INIT_FINISHED = "initFinished";
    private int mRequestCode;

    private EditText mEditText;


    public static EditTextDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new EditTextDialogBuilder(context, fragmentManager, EditTextDialogFragment.class);
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

    private boolean mSoftKeyShown;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        if (d != null && !mSoftKeyShown) {
            mSoftKeyShown = true;
            d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        return d;
    }


    @Override
    public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.sdl_dialog_edittext, null);
        mEditText = (EditText) view.findViewById(R.id.sdl_edit);
        if (isSingleLine()) {
            mEditText.setSingleLine();
            String imeAction = getPositiveButtonText();
            if (TextUtils.isEmpty(imeAction)) {
                imeAction = getString(android.R.string.ok);
            }
            mEditText.setImeActionLabel(imeAction, EditorInfo.IME_ACTION_DONE);
            mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE && event == null || event.getAction() == KeyEvent.ACTION_DOWN) {
                        onTextSubmitted(mEditText.getText().toString());
                        return true;
                    }

                    return false;
                }
            });
        }
        builder.setView(view);
        final String title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        final CharSequence message = getMessage();
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        final String hint = getHint();
        if (!TextUtils.isEmpty(hint)) {
            mEditText.setHint(hint);
        }

        final String initText = getInitText();
        if (!TextUtils.isEmpty(initText) && !getArguments().getBoolean(INIT_FINISHED)) {
            mEditText.setText(initText);
            getArguments().putBoolean(INIT_FINISHED, true);
            if (isTextSelected()) {
                mEditText.setSelectAllOnFocus(true);
            } else {
                mEditText.setSelection(initText.length());
            }
        }


        final String neutralButtonText = getNeutralButtonText();
        if (!TextUtils.isEmpty(neutralButtonText)) {
            builder.setNeutralButton(neutralButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IEditTextDialogListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onNeutralButtonClicked(mRequestCode, mEditText.getText().toString());
                    }
                    dismiss();
                }
            });
        }

        final String positiveButtonText = getPositiveButtonText();
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTextSubmitted(mEditText.getText().toString());
                }
            });
        }

        final String negativeButtonText = getNegativeButtonText();
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IEditTextDialogListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onNegativeButtonClicked(mRequestCode, mEditText.getText().toString());
                    }
                    dismiss();
                }
            });
        }


        return builder;
    }

    protected void onTextSubmitted(String text) {
        IEditTextDialogListener listener = getDialogListener();
        if (listener != null) {
            listener.onPositiveButtonClicked(mRequestCode, text);
        }
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        IEditTextDialogCancelListener listener = getCancelListener();
        if (listener != null) {
            listener.onCancelled(mRequestCode);
        }
    }

    protected CharSequence getMessage() {
        return getArguments().getCharSequence(ARG_MESSAGE);
    }

    protected boolean isSingleLine() {
        return getArguments().getBoolean(ARG_SINGLE_LINE);
    }

    protected boolean isTextSelected() {
        return getArguments().getBoolean(ARG_SELECT_ALL);
    }


    protected String getTitle() {
        return getArguments().getString(ARG_TITLE);
    }

    protected String getPositiveButtonText() {
        return getArguments().getString(ARG_POSITIVE_BUTTON);
    }

    protected String getNeutralButtonText() {
        return getArguments().getString(ARG_NEUTRAL_BUTTON);
    }


    protected String getNegativeButtonText() {
        return getArguments().getString(ARG_NEGATIVE_BUTTON);
    }

    protected String getHint() {
        return getArguments().getString(ARG_HINT);
    }

    protected String getInitText() {
        return getArguments().getString(ARG_INIT_TEXT);
    }


    protected IEditTextDialogListener getDialogListener() {
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof IEditTextDialogListener) {
                return (IEditTextDialogListener) targetFragment;
            }
        } else {
            if (getActivity() instanceof IEditTextDialogListener) {
                return (IEditTextDialogListener) getActivity();
            }
        }
        return null;
    }

    protected IEditTextDialogCancelListener getCancelListener() {
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof IEditTextDialogCancelListener) {
                return (IEditTextDialogCancelListener) targetFragment;
            }
        } else {
            if (getActivity() instanceof IEditTextDialogCancelListener) {
                return (IEditTextDialogCancelListener) getActivity();
            }
        }
        return null;
    }

    public static class EditTextDialogBuilder extends BaseDialogBuilder<EditTextDialogBuilder> {

        private String mTitle;
        private CharSequence mMessage;
        private String mHint;
        private String mInitText;
        private String mPositiveButtonText;
        private String mNeutralButtonText;
        private boolean mSingleLine;
        private boolean mSelectAll;

        private String mNegativeButtonText;

        protected EditTextDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends EditTextDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
        }

        @Override
        protected EditTextDialogBuilder self() {
            return this;
        }

        /**
         * @param initText text to prefill in the edittext
         * @param selected whether the text should be preselected when dialog shows up
         * @return
         */
        public EditTextDialogBuilder setInitText(@StringRes int initTextResourceId, boolean selected) {
            mInitText = mContext.getString(initTextResourceId);
            mSelectAll = selected;
            return this;
        }


        /**
         * @param initText text to prefill in the edittext
         * @param selected whether the text should be preselected when dialog shows up
         * @return
         */
        public EditTextDialogBuilder setInitText(String initText, boolean selected) {
            mInitText = initText;
            mSelectAll = selected;
            return this;
        }


        public EditTextDialogBuilder setHint(@StringRes int hintResourceId) {
            mHint = mContext.getString(hintResourceId);
            return this;
        }


        public EditTextDialogBuilder setHint(String hint) {
            mHint = hint;
            return this;
        }


        public EditTextDialogBuilder setSingleLine(boolean singleLine) {
            mSingleLine = singleLine;
            return this;
        }

        public EditTextDialogBuilder setTitle(@StringRes int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }


        public EditTextDialogBuilder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public EditTextDialogBuilder setMessage(@StringRes int messageResourceId) {
            mMessage = mContext.getText(messageResourceId);
            return this;
        }

        /**
         * Allow to set resource string with HTML formatting and bind %s,%i.
         * This is workaround for https://code.google.com/p/android/issues/detail?id=2923
         */
        public EditTextDialogBuilder setMessage(@StringRes int resourceId, Object... formatArgs) {
            mMessage = Html.fromHtml(String.format(Html.toHtml(new SpannedString(mContext.getText(resourceId))), formatArgs));
            return this;
        }

        public EditTextDialogBuilder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }

        public EditTextDialogBuilder setNeutralButtonText(@StringRes int textResourceId) {
            mNeutralButtonText = mContext.getString(textResourceId);
            return this;
        }

        public EditTextDialogBuilder setNeutralButtonText(String text) {
            mNeutralButtonText = text;
            return this;
        }

        public EditTextDialogBuilder setPositiveButtonText(@StringRes int textResourceId) {
            mPositiveButtonText = mContext.getString(textResourceId);
            return this;
        }

        public EditTextDialogBuilder setPositiveButtonText(String text) {
            mPositiveButtonText = text;
            return this;
        }

        public EditTextDialogBuilder setNegativeButtonText(@StringRes int textResourceId) {
            mNegativeButtonText = mContext.getString(textResourceId);
            return this;
        }

        public EditTextDialogBuilder setNegativeButtonText(String text) {
            mNegativeButtonText = text;
            return this;
        }


        @Override
        protected Bundle prepareArguments() {
            Bundle args = new Bundle();
            args.putCharSequence(ARG_HINT, mHint);
            args.putCharSequence(ARG_INIT_TEXT, mInitText);
            args.putCharSequence(ARG_MESSAGE, mMessage);
            args.putString(ARG_TITLE, mTitle);
            args.putString(ARG_POSITIVE_BUTTON, mPositiveButtonText);
            args.putString(ARG_NEGATIVE_BUTTON, mNegativeButtonText);
            args.putString(ARG_NEUTRAL_BUTTON, mNeutralButtonText);
            args.putBoolean(ARG_SINGLE_LINE, mSingleLine);
            args.putBoolean(ARG_SELECT_ALL, mSelectAll);


            return args;
        }
    }
}
