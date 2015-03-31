package com.avast.android.dialogs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.INegativeButtonDialogListener;
import com.avast.android.dialogs.iface.INeutralButtonDialogListener;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;

import java.util.List;

/**
 * Dialog with a title and editText.
 *
 * @author Wang
 * @data 2015/3/27
 */
public class InputDialogFragment extends BaseDialogFragment {

    protected final static String ARG_HINT = "hint";
    protected final static String ARG_TITLE = "title";
    protected final static String ARG_INPUT_TEXT = "input_text";
    protected final static String ARG_POSITIVE_BUTTON = "positive_button";
    protected final static String ARG_NEGATIVE_BUTTON = "negative_button";
    protected final static String ARG_NEUTRAL_BUTTON = "neutral_button";

    private EditText vEditText;
    private static StringBuffer inputText = new StringBuffer("");

    public static InputDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new InputDialogBuilder(context, fragmentManager, InputDialogFragment.class);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected BaseDialogFragment.Builder build(final BaseDialogFragment.Builder builder) {
        final CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        final CharSequence hint = getHint();
        if (!TextUtils.isEmpty(hint)) {
            builder.setEditTextHint(hint);
        }

        final CharSequence positiveButtonText = getPositiveButtonText();
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputText = builder.getInput();
                    Log.d("input", "input2 " + inputText);
                    for (IPositiveButtonDialogListener listener : getPositiveButtonDialogListeners()) {
                        listener.onPositiveButtonClicked(mRequestCode);
                    }
                    //if input is empty, click is invalid
                    if (!TextUtils.isEmpty(inputText)) {
                        dismiss();
                        inputText.setLength(0);
                    }
                }
            });
        }

        final CharSequence negativeButtonText = getNegativeButtonText();
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (INegativeButtonDialogListener listener : getNegativeButtonDialogListeners()) {
                        listener.onNegativeButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        final CharSequence neutralButtonText = getNeutralButtonText();
        if (!TextUtils.isEmpty(neutralButtonText)) {
            builder.setNeutralButton(neutralButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (INeutralButtonDialogListener listener : getNeutralButtonDialogListeners()) {
                        listener.onNeutralButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        /*vEditText = builder.getEditText();
        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputText = editable.toString();
                Log.d("input", "input3 " + inputText);
            }
        };
        vEditText.addTextChangedListener(mTextWatcher);*/
        return builder;
    }

    protected CharSequence getHint() {
        return getArguments().getCharSequence(ARG_HINT);
    }

    protected CharSequence getTitle() {
        return getArguments().getCharSequence(ARG_TITLE);
    }

    protected CharSequence getPositiveButtonText() {
        return getArguments().getCharSequence(ARG_POSITIVE_BUTTON);
    }

    protected CharSequence getNegativeButtonText() {
        return getArguments().getCharSequence(ARG_NEGATIVE_BUTTON);
    }

    protected CharSequence getNeutralButtonText() {
        return getArguments().getCharSequence(ARG_NEUTRAL_BUTTON);
    }

    /**
     * Get positive button dialog listeners.
     * There might be more than one listener.
     *
     * @return Dialog listeners
     * @since 2.1.0
     */
    protected List<IPositiveButtonDialogListener> getPositiveButtonDialogListeners() {
        return getDialogListeners(IPositiveButtonDialogListener.class);
    }

    /**
     * Get negative button dialog listeners.
     * There might be more than one listener.
     *
     * @return Dialog listeners
     * @since 2.1.0
     */
    protected List<INegativeButtonDialogListener> getNegativeButtonDialogListeners() {
        return getDialogListeners(INegativeButtonDialogListener.class);
    }

    /**
     * Get neutral button dialog listeners.
     * There might be more than one listener.
     *
     * @return Dialog listeners
     * @since 2.1.0
     */
    protected List<INeutralButtonDialogListener> getNeutralButtonDialogListeners() {
        return getDialogListeners(INeutralButtonDialogListener.class);
    }


    public static class InputDialogBuilder extends BaseDialogBuilder<InputDialogBuilder> {

        private CharSequence mTitle;
        private CharSequence mHint;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        private CharSequence mNeutralButtonText;

        protected InputDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
        }

        @Override
        protected InputDialogBuilder self() {
            return this;
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle args = new Bundle();
            args.putCharSequence(InputDialogFragment.ARG_HINT, mHint);
            args.putCharSequence(InputDialogFragment.ARG_TITLE, mTitle);
            args.putCharSequence(InputDialogFragment.ARG_POSITIVE_BUTTON, mPositiveButtonText);
            args.putCharSequence(InputDialogFragment.ARG_NEGATIVE_BUTTON, mNegativeButtonText);
            args.putCharSequence(InputDialogFragment.ARG_NEUTRAL_BUTTON, mNeutralButtonText);

            return args;
        }

        public InputDialogBuilder setTitle(int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }


        public InputDialogBuilder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public InputDialogBuilder setHint(int hintResourceId) {
            mHint = mContext.getText(hintResourceId);
            return this;
        }

        public InputDialogBuilder setHint(CharSequence hint) {
            mHint = hint;
            return this;
        }

        public InputDialogBuilder setPositiveButtonText(int textResourceId) {
            mPositiveButtonText = mContext.getString(textResourceId);
            return this;
        }

        public InputDialogBuilder setPositiveButtonText(CharSequence text) {
            mPositiveButtonText = text;
            return this;
        }

        public InputDialogBuilder setNegativeButtonText(int textResourceId) {
            mNegativeButtonText = mContext.getString(textResourceId);
            return this;
        }

        public InputDialogBuilder setNegativeButtonText(CharSequence text) {
            mNegativeButtonText = text;
            return this;
        }

        public InputDialogBuilder setNeutralButtonText(int textResourceId) {
            mNeutralButtonText = mContext.getString(textResourceId);
            return this;
        }

        public InputDialogBuilder setNeutralButtonText(CharSequence text) {
            mNeutralButtonText = text;
            return this;
        }

    }

    public StringBuffer getInput() {
        Log.d("input", "333 " + inputText);
        return inputText;
    }

}
