package eu.inmite.android.lib.dialogs;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.widget.TimePicker;

public class SimpleTimePickerDialogFragment extends SimpleDialogFragment {
	
	TimePicker mTimePicker;
	Calendar mCalendar;
	
	
	public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
		return new SimpleDialogBuilder(context, fragmentManager, SimpleTimePickerDialogFragment.class);
	}

	@Override
	protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
		builder=super.build(builder);
		mTimePicker=(TimePicker) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_part_timepicker,null);
		mTimePicker.setIs24HourView(getArguments().getBoolean("24h"));
		builder.setView(mTimePicker);
		
		TimeZone zone=TimeZone.getTimeZone(getArguments().getString("zone"));
		mCalendar=Calendar.getInstance(zone);
		mCalendar.setTimeInMillis(getArguments().getLong("date",System.currentTimeMillis()));

		mTimePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
		mTimePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
//		mTimePicker.setCurrentSecond(mCalendar.get(Calendar.SECOND));
		return builder;
	}
	
	public Date getDate() {
		mCalendar.set(Calendar.HOUR_OF_DAY,mTimePicker.getCurrentHour());
		mCalendar.set(Calendar.MINUTE,mTimePicker.getCurrentMinute());
		return mCalendar.getTime();
	}
	
	public static class SimpleDialogBuilder extends SimpleDialogFragment.SimpleDialogBuilder {

		Date mDate=new Date();
		String mTimeZone=null;
		boolean m24h=false;
		
		protected SimpleDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends SimpleDialogFragment> clazz) {
			super(context, fragmentManager, clazz);
		}

		//Remove message from consideration
		@Override
		public SimpleDialogBuilder setMessage(int messageResourceId) {
			return this;
		}

		//Remove Message from consideration
		@Override
		public SimpleDialogBuilder setMessage(int resourceId, Object... formatArgs){
			return this;
		}

		//Remove Message from consideration
		@Override
		public SimpleDialogBuilder setMessage(CharSequence message) {
			return this;
		}
		
		public SimpleDialogBuilder setDate(Date date) {
			mDate=date;
			return this;
		}
		
		public SimpleDialogBuilder set24hour(boolean state) {
			m24h=state;
			return this;
		}
		
		public SimpleDialogBuilder setTimeZone(String zone) {
			mTimeZone=zone;
			return this;
		}
		
		@Override
		protected Bundle prepareArguments() {
			Bundle args=super.prepareArguments();
			args.putLong("date",mDate.getTime());
			if (mTimeZone!=null) {
				args.putString("zone",mTimeZone);
			} else {
				args.putString("zone","GMT");
			}
			args.putBoolean("24h",m24h);
			return args;
		}
	}
}
