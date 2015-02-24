package com.avast.android.dialogs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.avast.android.dialogs.R;
import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.IDateDialogListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Dialog with a date picker.
 * <p/>
 * Implement {@link com.avast.android.dialogs.iface.IDateDialogListener}
 * and/or {@link com.avast.android.dialogs.iface.ISimpleDialogCancelListener} to handle events.
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


    public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleDialogBuilder(context, fragmentManager, DatePickerDialogFragment.class);
    }

    /**
     * Get dialog date listeners.
     * There might be more than one date listener.
     *
     * @return Dialog date listeners
     * @since 2.1.0
     */
    protected List<IDateDialogListener> getDialogListeners() {
        return getDialogListeners(IDateDialogListener.class);
    }

    @Override
    protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        final CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        final CharSequence positiveButtonText = getPositiveButtonText();
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    for (IDateDialogListener listener : getDialogListeners()) {
                        listener.onPositiveButtonClicked(mRequestCode, getDate());
                    }
                    dismiss();
                }
            });
        }

        final CharSequence negativeButtonText = getNegativeButtonText();
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    for (IDateDialogListener listener : getDialogListeners()) {
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

    protected CharSequence getTitle() {
        return getArguments().getCharSequence(ARG_TITLE);
    }

    protected CharSequence getPositiveButtonText() {
        return getArguments().getCharSequence(ARG_POSITIVE_BUTTON);
    }

    protected CharSequence getNegativeButtonText() {
        return getArguments().getCharSequence(ARG_NEGATIVE_BUTTON);
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

        private CharSequence mTitle;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;

        private boolean mShowDefaultButton = true;
        private boolean m24h;

        protected SimpleDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends DatePickerDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
            m24h = DateFormat.is24HourFormat(context);
        }

        public SimpleDialogBuilder setTitle(int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }


        public SimpleDialogBuilder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public SimpleDialogBuilder setPositiveButtonText(int textResourceId) {
            mPositiveButtonText = mContext.getString(textResourceId);
            return this;
        }

        public SimpleDialogBuilder setPositiveButtonText(CharSequence text) {
            mPositiveButtonText = text;
            return this;
        }

        public SimpleDialogBuilder setNegativeButtonText(int textResourceId) {
            mNegativeButtonText = mContext.getString(textResourceId);
            return this;
        }

        public SimpleDialogBuilder setNegativeButtonText(CharSequence text) {
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
            args.putCharSequence(SimpleDialogFragment.ARG_TITLE, mTitle);
            args.putCharSequence(SimpleDialogFragment.ARG_POSITIVE_BUTTON, mPositiveButtonText);
            args.putCharSequence(SimpleDialogFragment.ARG_NEGATIVE_BUTTON, mNegativeButtonText);

            args.putLong(ARG_DATE, mDate.getTime());
            args.putBoolean(ARG_24H, m24h);
            if (mTimeZone != null) {
                args.putString(ARG_ZONE, mTimeZone);
            } else {
                args.putString(ARG_ZONE, TimeZone.getDefault().getID());
            }
            return args;
        }

        @Override
        protected SimpleDialogBuilder self() {
            return this;
        }
    }
}
