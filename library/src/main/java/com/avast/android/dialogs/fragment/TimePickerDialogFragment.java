package com.avast.android.dialogs.fragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import com.avast.android.dialogs.R;
import com.avast.android.dialogs.core.BaseDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Dialog with a time picker.
 * <p/>
 * Implement {@link com.avast.android.dialogs.iface.IDateDialogListener}
 * and/or {@link com.avast.android.dialogs.iface.ISimpleDialogCancelListener} to handle events.
 */
public class TimePickerDialogFragment extends DatePickerDialogFragment {

    TimePicker mTimePicker;
    Calendar mCalendar;


    public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleDialogBuilder(context, fragmentManager, TimePickerDialogFragment.class);
    }

    @Override
    protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        builder = super.build(builder);
        mTimePicker = (TimePicker) LayoutInflater.from(getActivity()).inflate(R.layout.sdl_timepicker, null);
        mTimePicker.setIs24HourView(getArguments().getBoolean(ARG_24H));
        builder.setView(mTimePicker);

        TimeZone zone = TimeZone.getTimeZone(getArguments().getString(ARG_ZONE));
        mCalendar = Calendar.getInstance(zone);
        mCalendar.setTimeInMillis(getArguments().getLong(ARG_DATE, System.currentTimeMillis()));

        mTimePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        return builder;
    }

    public Date getDate() {
        mCalendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
        mCalendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
        return mCalendar.getTime();
    }
}
