package eu.inmite.demo.dialogs;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import main.java.eu.inmite.android.lib.dialogs.MessageDialogFragment;

public class MyActivity extends FragmentActivity {

	MyActivity c = this;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViewById(R.id.show_message_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MessageDialogFragment.show(c, "Title", "Message text, pretty long, long long long.");
			}
		});
	}
}
