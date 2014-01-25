package eu.inmite.android.lib.dialogs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.widget.DatePicker;

public class SimpleDatePickerDialogFragment extends SimpleDialogFragment {
	
	DatePicker mDatePicker;
	
	public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
		return new SimpleDialogBuilder(context, fragmentManager, SimpleDatePickerDialogFragment.class);
	}

	@Override
	protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
		builder=super.build(builder);
		mDatePicker=(DatePicker) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_part_datepicker,null);
		builder.setView(mDatePicker);
		
		return builder;
	}
	
	public Date getDate() {
		return getDate(TimeZone.getTimeZone("GMT") );
	}
	
	public Date getDate(TimeZone zone) {
		int year=mDatePicker.getYear();
		int month=mDatePicker.getMonth();
		int day=mDatePicker.getDayOfMonth();
		Calendar cal=new GregorianCalendar(zone);
		cal.set(year,month,day,0,0,0);
		return cal.getTime();
	}
	
}
