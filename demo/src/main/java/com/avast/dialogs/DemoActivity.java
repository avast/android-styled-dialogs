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

package com.avast.dialogs;

import java.text.DateFormat;
import java.util.Date;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.*;
import com.avast.android.dialogs.iface.IDateDialogListener;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.iface.ISimpleDialogListener;

public class DemoActivity extends ActionBarActivity implements
    ISimpleDialogListener,
    IDateDialogListener,
    ISimpleDialogCancelListener,
    IListDialogListener {

    private static final int REQUEST_PROGRESS = 1;

    DemoActivity c = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.message_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialogFragment.createBuilder(c, getSupportFragmentManager())
                    .setMessage("Love. Can know all the math in the \'verse but take a boat in the air that you don\'t " +
                        "love? She\'ll shake you off just as sure as a turn in the worlds. Love keeps her in the air when " +
                        "she oughtta fall down...tell you she\'s hurtin\' \'fore she keens...makes her a home.").show();
            }
        });
        findViewById(R.id.message_title_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialogFragment.createBuilder(c, getSupportFragmentManager())
                    .setTitle("More Firefly quotes:").setMessage
                    ("Wash: \"Psychic, though? That sounds like something out of science fiction.\"\n\nZoe: \"We live" +
                        " " +
                        "in a space ship, dear.\"\nWash: \"Here lies my beloved Zoe, " +
                        ("my autumn flower ... somewhat less attractive now that she's all corpsified and gross" +
                            ".\"\n\nRiver Tam: \"Also? I can kill you with my brain.\"\n\nKayle: \"Going on a year now, nothins twixed my neathers not run on batteries.\" \n" +
                            "Mal: \"I can't know that.\" \n" +
                            "Jayne: \"I can stand to hear a little more.\"\n\nWash: \"I've been under fire before. " +
                            "Well ... I've been in a fire. Actually, I was fired. I can handle myself.\""))
                    .setNegativeButtonText("Close")
                    .show();
            }
        });
        findViewById(R.id.message_title_buttons_dialog)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDialogFragment.createBuilder(c, getSupportFragmentManager())
                        .setTitle("Do you like this quote?")
                        .setMessage("Jayne: \"Shiny. Let's be bad guys.\"")
                        .setPositiveButtonText("Love")
                        .setNegativeButtonText("Hate").setNeutralButtonText("WTF?").setRequestCode(42)
                        .show();
                }
            });
        findViewById(R.id.long_buttons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialogFragment.createBuilder(c, getSupportFragmentManager()).setMessage("How will you decide?")
                    .setPositiveButtonText("Time for some thrillin' heroics!").setNegativeButtonText("Misbehave")
                    .setNeutralButtonText("Keep flying").show();
            }
        });
        findViewById(R.id.progress_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogFragment.createBuilder(c, getSupportFragmentManager())
                    .setMessage("Mal: I\'m just waiting to see if I pass out. Long story.")
                    .setRequestCode(REQUEST_PROGRESS)
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
                    .createBuilder(DemoActivity.this, getSupportFragmentManager())
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
                    .createBuilder(DemoActivity.this, getSupportFragmentManager())
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
}
