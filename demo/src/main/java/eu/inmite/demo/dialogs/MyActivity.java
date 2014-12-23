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

import java.text.DateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import eu.inmite.android.lib.dialogs.*;

public class MyActivity extends ActionBarActivity implements
    ISimpleDialogListener,
    IDateDialogListener,
    ISimpleDialogCancelListener,
    IListDialogListener {

    public static final String EXTRA_THEME = "theme";

    private static final int REQUEST_PROGRESS = 1;

    MyActivity c = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.message_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialogFragment.createBuilder(c, getSupportFragmentManager())
                    .setMessage(R.string.message_1).show();
            }
        });
        findViewById(R.id.message_title_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialogFragment.createBuilder(c, getSupportFragmentManager())
                    .setTitle(R.string.title).setMessage(R.string.message_2).show();
            }
        });
        findViewById(R.id.message_title_buttons_dialog)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDialogFragment.createBuilder(c, getSupportFragmentManager())
                        .setTitle(R.string.title)
                        .setMessage(R.string.message_3)
                        .setPositiveButtonText(R.string.positive_button)
                        .setNegativeButtonText(R.string.negative_button).setNeutralButtonText("WTF?").setRequestCode(42)
                        .setTag("custom-tag")
                        .show();
                }
            });
        findViewById(R.id.progress_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogFragment.createBuilder(c, getSupportFragmentManager())
                    .setMessage(R.string.message_4)
                    .setRequestCode(REQUEST_PROGRESS)
                    .setTitle(R.string.app_name)
                    .show();
            }
        });
        findViewById(R.id.list_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogFragment
                    .createBuilder(c, getSupportFragmentManager())
                    .setTitle("Your favorite character:")
                    .setItems(new String[]{"Jayne", "Malcolm", "Kaylee",
                        "Wash", "Zoe", "River"})
                    .show();

            }
        });
        findViewById(R.id.custom_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JayneHatDialogFragment.show(c);
            }
        });
        findViewById(R.id.time_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment
                    .createBuilder(MyActivity.this, getSupportFragmentManager())
                    .setDate(new Date())
                    .set24hour(true)
                    .setPositiveButtonText(android.R.string.ok)
                    .setNegativeButtonText(android.R.string.cancel)
                    .setRequestCode(13)
                    .show();
            }
        });
        findViewById(R.id.date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment
                    .createBuilder(MyActivity.this, getSupportFragmentManager())
                    .setDate(new Date())
                    .set24hour(true)
                    .setPositiveButtonText(android.R.string.ok)
                    .setNegativeButtonText(android.R.string.cancel)
                    .setRequestCode(12)
                    .show();
            }
        });
    }

    // IListDialogListener

    @Override
    public void onListItemSelected(String value, int number) {
        Toast.makeText(c, "Selected: " + value, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled() {
        Toast.makeText(c, "Nothing selected", Toast.LENGTH_SHORT).show();
    }

    // ISimpleDialogCancelListener

    @Override
    public void onCancelled(int requestCode) {
        if (requestCode == 42) {
            Toast.makeText(c, "Dialog cancelled", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_PROGRESS) {
            Toast.makeText(c, "Progress dialog cancelled", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onNeutralButtonClicked(int requestCode) {
        if (requestCode == 42) {
            Toast.makeText(c, "Neutral button clicked", Toast.LENGTH_SHORT).show();
        }
    }

    // IDateDialogListener

    @Override
    public void onNegativeButtonClicked(int resultCode, Date date) {
        String text = "";
        if (resultCode == 12) {
            text = "Date ";
        } else if (resultCode == 13) {
            text = "Time ";
        }

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
        Toast.makeText(this, text + "Cancelled " + dateFormat.format(date), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositiveButtonClicked(int resultCode, Date date) {
        String text = "";
        if (resultCode == 12) {
            text = "Date ";
        } else if (resultCode == 13) {
            text = "Time ";
        }

        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        Toast.makeText(this, text + "Success! " + dateFormat.format(date), Toast.LENGTH_SHORT).show();
    }

    private void setCurrentTheme(int theme) {
        Intent i = new Intent(c, MyActivity.class);
        i.putExtra(EXTRA_THEME, theme);
        startActivity(i);
        finish();
        overridePendingTransition(0, 0);
    }
}
