package eu.inmite.demo.dialogs;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;
import eu.inmite.android.lib.dialogs.ISimpleDialogCancelListener;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import eu.inmite.android.lib.dialogs.SimpleDialogFragmentBuilder;

public class MyActivity extends FragmentActivity implements 
		ISimpleDialogListener, 
		IFavoriteCharacterDialogListener,
		ISimpleDialogCancelListener {

	MyActivity c = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViewById(R.id.message_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialogFragment.show(c, null, R.string.message_1);
			}
		});
		findViewById(R.id.message_title_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialogFragment.show(c, null, R.string.message_2, R.string.title);
			}
		});
		findViewById(R.id.message_title_buttons_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialogFragment.show(c, null, c.getString(R.string.message_3),
						c.getString(R.string.title), c.getString(R.string.positive_button), c.getString(R.string.negative_button));
			}
		});
		findViewById(R.id.list_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FavoriteCharacterDialogFragment.show(c, "Your favorite character (some text added to make it longer):", new String[]{"Jayne", "Malcolm", "Kaylee",
						"Wash", "Zoe", "River"});
			}
		});
		findViewById(R.id.custom_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JayneHatDialogFragment.show(c);
			}
		});
		findViewById(R.id.with_callbacks_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialogFragmentBuilder builder = new SimpleDialogFragmentBuilder(MyActivity.this);
				builder.setTitle("Some title")
						.setMessage(R.string.message_3)
						.setPositiveButtonText("OK")
						.setNegativeButtonText("Cancel")
						.setCancelable(true)
						.setRequestCode(1)
						.build();
			}
		});
	}

	@Override
	public void onListItemSelected(String value, int number) {
		Toast.makeText(c, "Selected: "+value, Toast.LENGTH_SHORT).show();
	}

	// ISimpleDialogCancelListener

	@Override
	public void onCancelled(int requestCode) {
		Toast.makeText(c, "Dialog cancelled", Toast.LENGTH_SHORT).show();
	}

	// ISimpleDialogListener

	@Override
	public void onPositiveButtonClicked(int requestCode) {
		Toast.makeText(c, "Positive button clicked", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNegativeButtonClicked(int requestCode) {
		Toast.makeText(c, "Negative button clicked", Toast.LENGTH_SHORT).show();
	}
}
