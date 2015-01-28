package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.numberpicker.backport.NumberPicker;

/**
 /**
 * Simple implementation of styled number picker dialog.
 *
 * Implement {@link eu.inmite.android.lib.dialogs.INumberDialogListener} in your Fragment or Activity to react on positive,
 * negative and/or neutral button clicks. This class can be extended and more parameters can be
 * added in overridden build() method.
 *
 * @author kd7uiy (https://github.com/kd7uiy/android-styled-dialogs)
 * @author Lukas Prokop (prokop@avast.com)
 */
public class NumberPickerDialogFragment extends BaseDialogFragment {

    protected static final String ARG_NUMBER = "number";
    protected static final String ARG_MIN = "minValue";
    protected static final String ARG_MAX = "maxValue";

    private NumberPicker mNumberPicker;
    private int mNumber;
    private int mMinValue;
    private int mMaxValue;

    public static NumberPickerDialogBuilder createBuilder(Context context,
                                                          FragmentManager fragmentManager) {
        return new NumberPickerDialogBuilder(context, fragmentManager, NumberPickerDialogFragment.class);
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
                mRequestCode = args.getInt(BaseDialogFragment.ARG_REQUEST_CODE, 0);
            }
        }
    }

    protected INumberDialogListener getDialogListener() {
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof INumberDialogListener) {
                return (INumberDialogListener) targetFragment;
            }
        } else {
            if (getActivity() instanceof INumberDialogListener) {
                return (INumberDialogListener) getActivity();
            }
        }
        return null;
    }

    @Override
    protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        final String title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        final String positiveButtonText = getPositiveButtonText();
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    INumberDialogListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onPositiveButtonClicked(mRequestCode, getNumber());
                    }
                    dismiss();
                }
            });
        }

        final String negativeButtonText = getNegativeButtonText();
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    INumberDialogListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onNegativeButtonClicked(mRequestCode, getNumber());
                    }
                    dismiss();
                }
            });
        }

        mNumberPicker = (NumberPicker) LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_part_numberpicker, null);
        builder.setView(mNumberPicker);

        mMinValue = getArguments().getInt(ARG_MIN, Integer.MIN_VALUE);
        mMaxValue = getArguments().getInt(ARG_MAX, Integer.MAX_VALUE);
        mNumber = getArguments().getInt(ARG_NUMBER, mMinValue);

        mNumberPicker.setMinValue(mMinValue);
        mNumberPicker.setMaxValue(mMaxValue);
        mNumberPicker.setValue(mNumber);

        return builder;
    }

    public int getNumber() {
        return mNumberPicker.getValue();
    }

    /**
     * Custom builder
     */
    public static class NumberPickerDialogBuilder
            extends BaseDialogBuilder<NumberPickerDialogBuilder> {
        int mNumber = 0;
        private int mMin = Integer.MIN_VALUE;
        private int mMax = Integer.MAX_VALUE;

        protected NumberPickerDialogBuilder(Context context, FragmentManager fragmentManager,
                                            Class<? extends NumberPickerDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
        }

        public NumberPickerDialogBuilder setNumber(int number) {
            mNumber = number;
            return this;
        }

        public NumberPickerDialogBuilder setMinValue(int minValue) {
            mMin = minValue;
            return this;
        }

        public NumberPickerDialogBuilder setMaxValue(int maxValue) {
            mMax = maxValue;
            return this;
        }

        @Override
        protected Bundle prepareArguments() {
            if (mDialogParams.showDefaultButton && mDialogParams.positiveButtonText == null &&
                    mDialogParams.negativeButtonText == null) {
                mDialogParams.positiveButtonText = mDialogParams.context.getString(R.string.dialog_close);
            }

            Bundle args = new Bundle();
            if (!TextUtils.isEmpty(mDialogParams.title)) {
                args.putString(BaseDialogFragment.ARG_TITLE, mDialogParams.title.toString());
            }
            args.putString(BaseDialogFragment.ARG_POSITIVE_BUTTON,
                    mDialogParams.positiveButtonText.toString());
            if (!TextUtils.isEmpty(mDialogParams.negativeButtonText)) {
                args.putString(BaseDialogFragment.ARG_NEGATIVE_BUTTON,
                        mDialogParams.negativeButtonText.toString());
            }
            args.putInt(ARG_MIN, mMin);
            args.putInt(ARG_MAX, mMax);
            args.putInt(ARG_NUMBER, mNumber);

            return args;
        }

        @Override
        protected NumberPickerDialogBuilder self() {
            return this;
        }
    }
}
