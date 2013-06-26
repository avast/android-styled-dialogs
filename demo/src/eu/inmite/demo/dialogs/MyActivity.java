/*
 * Copyright 2013 Inmite s.r.o. (www.inmite.eu).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.inmite.demo.dialogs;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import eu.inmite.android.lib.dialogs.ISimpleDialogCancelListener;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

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
				new SimpleDialogFragment.Builder(c, getSupportFragmentManager()).setMessage(R.string.message_1).show();
			}
		});
		findViewById(R.id.message_title_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new SimpleDialogFragment.Builder(c, getSupportFragmentManager()).setTitle(R.string.title).setMessage(R.string.message_2).show();
			}
		});
		findViewById(R.id.message_title_buttons_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new SimpleDialogFragment.Builder(c, getSupportFragmentManager()).setTitle(R.string.title).setMessage(R.string.message_3).setPositiveButtonText(R.string.positive_button)
					.setNegativeButtonText(R.string.negative_button).show();
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
				new SimpleDialogFragment.Builder(c, getSupportFragmentManager()).setTitle("Some title")
					.setMessage(R.string.message_3)
					.setPositiveButtonText("OK")
					.setNegativeButtonText("Cancel")
					.setRequestCode(42)
					.show();
			}
		});
	}

	@Override
	public void onListItemSelected(String value, int number) {
		Toast.makeText(c, "Selected: " + value, Toast.LENGTH_SHORT).show();
	}

	// ISimpleDialogCancelListener

	@Override
	public void onCancelled(int requestCode) {
		if (requestCode == 42) {
			Toast.makeText(c, "Dialog cancelled", Toast.LENGTH_SHORT).show();
		}
	}

	// ISimpleDialogListener

	@Override
	public void onPositiveButtonClicked(int requestCode) {
		if (requestCode == 42) {
			Toast.makeText(c, "Positive button clicked", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onNegativeButtonClicked(int requestCode) {
		if (requestCode == 42) {
			Toast.makeText(c, "Negative button clicked", Toast.LENGTH_SHORT).show();
		}
	}
}
