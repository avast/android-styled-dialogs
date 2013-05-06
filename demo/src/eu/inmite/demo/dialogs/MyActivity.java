package eu.inmite.demo.dialogs;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class MyActivity extends FragmentActivity implements ISimpleDialogListener, IFavoriteCharacterDialogListener {

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
				FavoriteCharacterDialogFragment.show(c, "Your favorite character:", new String[]{"Jayne", "Malcolm", "Kaylee",
						"Wash", "Zoe", "River"});
			}
		});
		findViewById(R.id.custom_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JayneHatDialogFragment.show(c);
			}
		});
	}

	@Override
	public void onPositiveButtonClicked() {
		Toast.makeText(c, "Positive button clicked", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNegativeButtonClicked() {
		Toast.makeText(c, "Negative button clicked", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onListItemSelected(String value, int number) {
		Toast.makeText(c, "Selected: "+value, Toast.LENGTH_SHORT).show();
	}
}
