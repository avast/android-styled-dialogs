package com.avast.android.dialogs.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.avast.android.dialogs.R;
import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.IDateDialogCancelListener;
import com.avast.android.dialogs.iface.IDateDialogListener;
import com.avast.android.dialogs.iface.IListDialogListener;

/**
 * Dialog with a date picker. Implement {@link com.avast.android.dialogs.iface.IDateDialogListener} or {@link com.avast.android.dialogs.iface.IDateDialogCancelListener} to handle events.
 */
public class DatePickerDialogFragment extends BaseDialogFragment {

    protected static final String ARG_ZONE = "zone";
    protected static final String ARG_TITLE = "title";
    protected static final String ARG_POSITIVE_BUTTON = "positive_button";
    protected static final String ARG_NEGATIVE_BUTTON = "negative_button";
    protected static final String ARG_DATE = "date";
    protected static final String ARG_24H = "24h";

    DatePicker mDatePicker;
    Calendar mCalendar;

    private int mRequestCode;


    public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleDialogBuilder(context, fragmentManager, DatePickerDialogFragment.class);
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

    protected IDateDialogListener getDialogListener() {
        IDateDialogListener listener = getGeneralListener(IDateDialogListener.class);
        if (listener != null) {
            return listener;
        }
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof IDateDialogListener) {
                return (IDateDialogListener) targetFragment;
            }
        } else {
            if (getActivity() instanceof IDateDialogListener) {
                return (IDateDialogListener) getActivity();
            }
        }
        return null;
    }

    @SuppressWarnings({"unused"})
    protected IDateDialogCancelListener getCancelListener() {
        IDateDialogCancelListener listener = getGeneralListener(IListDialogListener.class);
        if (listener != null) {
            return listener;
        }
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof IDateDialogCancelListener) {
                return (IDateDialogCancelListener) targetFragment;
            }
        } else {
            if (getActivity() instanceof IDateDialogCancelListener) {
                return (IDateDialogCancelListener) getActivity();
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
                public void onClick(View view) {
                    IDateDialogListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onPositiveButtonClicked(mRequestCode, getDate());
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
                    IDateDialogListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onNegativeButtonClicked(mRequestCode, getDate());
                    }
                    dismiss();
                }
            });
        }
        mDatePicker = (DatePicker) LayoutInflater.from(getActivity()).inflate(R.layout.sdl_datepicker, null);
        builder.setView(mDatePicker);

        TimeZone zone = TimeZone.getTimeZone(getArguments().getString(ARG_ZONE));
        mCalendar = Calendar.getInstance(zone);
        mCalendar.setTimeInMillis(getArguments().getLong(ARG_DATE, System.currentTimeMillis()));
        mDatePicker.updateDate(mCalendar.get(Calendar.YEAR)
                , mCalendar.get(Calendar.MONTH)
                , mCalendar.get(Calendar.DAY_OF_MONTH));
        return builder;
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

    public Date getDate() {
        mCalendar.set(Calendar.YEAR, mDatePicker.getYear());
        mCalendar.set(Calendar.MONTH, mDatePicker.getMonth());
        mCalendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
        return mCalendar.getTime();
    }

    public static class SimpleDialogBuilder extends BaseDialogBuilder<SimpleDialogBuilder> {
        Date mDate = new Date();
        String mTimeZone = null;

        private String mTitle;
        private String mPositiveButtonText;
        private String mNegativeButtonText;

        private boolean mShowDefaultButton = true;
        private boolean m24h;

        protected SimpleDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends DatePickerDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
        }

        public SimpleDialogBuilder setTitle(int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }


        public SimpleDialogBuilder setTitle(String title) {
            mTitle = title;
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

        public SimpleDialogBuilder setDate(Date date) {
            mDate = date;
            return this;
        }

        public SimpleDialogBuilder setTimeZone(String zone) {
            mTimeZone = zone;
            return this;
        }

        public SimpleDialogBuilder set24hour(boolean state) {
            m24h = state;
            return this;
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle args = new Bundle();
            args.putString(SimpleDialogFragment.ARG_TITLE, mTitle);
            args.putString(SimpleDialogFragment.ARG_POSITIVE_BUTTON, mPositiveButtonText);
            args.putString(SimpleDialogFragment.ARG_NEGATIVE_BUTTON, mNegativeButtonText);

            args.putLong(ARG_DATE, mDate.getTime());
            args.putBoolean(ARG_24H, m24h);
            if (mTimeZone != null) {
                args.putString(ARG_ZONE, mTimeZone);
            } else {
                args.putString(ARG_ZONE, "GMT");
            }
            return args;
        }

        @Override
        protected SimpleDialogBuilder self() {
            return this;
        }
    }
}
