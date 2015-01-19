package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Dialog with a date picker. Implement {@link eu.inmite.android.lib.dialogs.IDateDialogListener} or {@link eu.inmite.android.lib.dialogs.IDateDialogCancelListener} to handle events.
 */
public class DatePickerDialogFragment extends BaseDialogFragment {

	protected static final String ARG_ZONE = "zone";
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
				mRequestCode = args.getInt(BaseDialogFragment.ARG_REQUEST_CODE, 0);
			}
		}
	}

	protected IDateDialogListener getDialogListener() {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			if (targetFragment instanceof IDateDialogListener) {
				return (IDateDialogListener)targetFragment;
			}
		} else {
			if (getActivity() instanceof IDateDialogListener) {
				return (IDateDialogListener)getActivity();
			}
		}
		return null;
	}

	protected IDateDialogCancelListener getCancelListener() {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			if (targetFragment instanceof IDateDialogCancelListener) {
				return (IDateDialogCancelListener)targetFragment;
			}
		} else {
			if (getActivity() instanceof IDateDialogCancelListener) {
				return (IDateDialogCancelListener)getActivity();
			}
		}
		return null;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		IDateDialogCancelListener onDateDialogCanceled = getCancelListener();
		if (onDateDialogCanceled != null) {
			onDateDialogCanceled.onCancelled(mRequestCode, getDate());
		}
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
		mDatePicker = (DatePicker)LayoutInflater.from(getActivity()).inflate(R.layout.dialog_part_datepicker, null);
		builder.setView(mDatePicker);

		TimeZone zone = TimeZone.getTimeZone(getArguments().getString(ARG_ZONE));
		mCalendar = Calendar.getInstance(zone);
		mCalendar.setTimeInMillis(getArguments().getLong(ARG_DATE, System.currentTimeMillis()));
		mDatePicker.updateDate(mCalendar.get(Calendar.YEAR)
			, mCalendar.get(Calendar.MONTH)
			, mCalendar.get(Calendar.DAY_OF_MONTH));
		return builder;
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

		private boolean mShowDefaultButton = true;
		private boolean m24h;

		protected SimpleDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends DatePickerDialogFragment> clazz) {
			super(context, fragmentManager, clazz);
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
			if (mShowDefaultButton && mDialogParams.positiveButtonText == null && mDialogParams.negativeButtonText
				== null) {
				mDialogParams.positiveButtonText = mDialogParams.context.getString(R.string.dialog_close);
			}

			Bundle args = new Bundle();
			if (!TextUtils.isEmpty(mDialogParams.title)) {
				args.putString(BaseDialogFragment.ARG_TITLE, mDialogParams.title.toString());
			}
			if (mDialogParams.positiveButtonText != null) {
				args.putString(BaseDialogFragment.ARG_POSITIVE_BUTTON, mDialogParams.positiveButtonText
					.toString());
			}
			if (mDialogParams.negativeButtonText != null) {
				args.putString(BaseDialogFragment.ARG_NEGATIVE_BUTTON,
					mDialogParams.negativeButtonText.toString());
			}

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
