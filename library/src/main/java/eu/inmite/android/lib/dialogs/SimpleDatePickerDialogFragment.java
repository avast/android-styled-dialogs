package eu.inmite.android.lib.dialogs;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.widget.DatePicker;

public class SimpleDatePickerDialogFragment extends SimpleDialogFragment {
	
	DatePicker mDatePicker;
	Calendar mCalendar;
	
	
	public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
		return new SimpleDialogBuilder(context, fragmentManager, SimpleDatePickerDialogFragment.class);
	}

	@Override
	protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
		builder=super.build(builder);
		mDatePicker=(DatePicker) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_part_datepicker,null);
		builder.setView(mDatePicker);
		
		TimeZone zone=TimeZone.getTimeZone(getArguments().getString("zone"));
		mCalendar=Calendar.getInstance(zone);
		mCalendar.setTimeInMillis(getArguments().getLong("date",System.currentTimeMillis()));
		mDatePicker.updateDate(mCalendar.get(Calendar.YEAR)
				,mCalendar.get(Calendar.MONTH)
				,mCalendar.get(Calendar.DAY_OF_MONTH));
		return builder;
	}
	
	public Date getDate() {
		mCalendar.set(Calendar.YEAR,mDatePicker.getYear());
		mCalendar.set(Calendar.MONTH,mDatePicker.getMonth());
		mCalendar.set(Calendar.DAY_OF_MONTH,mDatePicker.getDayOfMonth());
		return mCalendar.getTime();
	}
	
	public static class SimpleDialogBuilder extends SimpleDialogFragment.SimpleDialogBuilder {

		Date mDate=new Date();
		String mTimeZone=null;
		
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
			return args;
		}
	}
}
