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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.DatePickerDialogFragment;
import com.avast.android.dialogs.fragment.ListDialogFragment;
import com.avast.android.dialogs.fragment.ProgressDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.fragment.TimePickerDialogFragment;
import com.avast.android.dialogs.iface.IDateDialogListener;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.avast.android.dialogs.iface.IMultiChoiceListDialogListener;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.iface.ISimpleDialogListener;

public class DemoActivity extends ActionBarActivity implements
        ISimpleDialogListener,
        IDateDialogListener,
        ISimpleDialogCancelListener,
        IListDialogListener,
        IMultiChoiceListDialogListener {

    private static final int REQUEST_PROGRESS = 1;
    private static final int REQUEST_LIST_SIMPLE = 9;
    private static final int REQUEST_LIST_MULTIPLE = 10;
    private static final int REQUEST_LIST_SINGLE = 11;
    private static final int REQUEST_DATE_PICKER = 12;
    private static final int REQUEST_TIME_PICKER = 13;
    private static final int REQUEST_SIMPLE_DIALOG = 42;

    DemoActivity c = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.img_avast_logo_small);

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
                                .setNegativeButtonText("Hate")
                                .setNeutralButtonText("WTF?")
                                .setRequestCode(REQUEST_SIMPLE_DIALOG)
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
        findViewById(R.id.list_dialog_simple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogFragment
                        .createBuilder(c, getSupportFragmentManager())
                        .setTitle("Your favorite character:")
                        .setItems(new String[]{"Jayne", "Malcolm", "Kaylee",
                                "Wash", "Zoe", "River"})
                        .setRequestCode(REQUEST_LIST_SIMPLE)
                        .show();

            }
        });
        findViewById(R.id.list_dialog_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogFragment
                        .createBuilder(c, getSupportFragmentManager())
                        .setTitle("Your favorite character:")
                        .setItems(new String[]{"Jayne", "Malcolm", "Kaylee",
                                "Wash", "Zoe", "River"})
                        .setRequestCode(REQUEST_LIST_SINGLE)
                        .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
                        .show();

            }
        });
        findViewById(R.id.list_dialog_multiple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogFragment
                        .createBuilder(c, getSupportFragmentManager())
                        .setTitle("Your favorite character:")
                        .setItems(new String[]{"Jayne", "Malcolm", "Kaylee",
                                "Wash", "Zoe", "River"})
                        .setRequestCode(REQUEST_LIST_MULTIPLE)
                        .setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE)
                        .setCheckedItems(new int[]{1, 3})
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
                        .setPositiveButtonText(android.R.string.ok)
                        .setNegativeButtonText(android.R.string.cancel)
                        .setRequestCode(REQUEST_TIME_PICKER)
                        .show();
            }
        });
        findViewById(R.id.date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment
                        .createBuilder(DemoActivity.this, getSupportFragmentManager())
                        .setDate(new Date())
                        .setPositiveButtonText(android.R.string.ok)
                        .setNegativeButtonText(android.R.string.cancel)
                        .setRequestCode(REQUEST_DATE_PICKER)
                        .show();
            }
        });
    }

    // IListDialogListener

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        if (requestCode == REQUEST_LIST_SIMPLE || requestCode == REQUEST_LIST_SINGLE) {
            Toast.makeText(c, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int requestCode) {
        if (requestCode == REQUEST_LIST_MULTIPLE) {
            StringBuilder sb = new StringBuilder();
            for (CharSequence value : values) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(value);

            }
            Toast.makeText(c, "Selected: " + sb.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // ISimpleDialogCancelListener

    @Override
    public void onCancelled(int requestCode) {
        switch (requestCode) {
            case REQUEST_SIMPLE_DIALOG:
                Toast.makeText(c, "Dialog cancelled", Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_PROGRESS:
                Toast.makeText(c, "Progress dialog cancelled", Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_LIST_SIMPLE:
            case REQUEST_LIST_SINGLE:
            case REQUEST_LIST_MULTIPLE:
                Toast.makeText(c, "Nothing selected", Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_DATE_PICKER:
                Toast.makeText(c, "Date picker cancelled", Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_TIME_PICKER:
                Toast.makeText(c, "Time picker cancelled", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // ISimpleDialogListener

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode == REQUEST_SIMPLE_DIALOG) {
            Toast.makeText(c, "Positive button clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        if (requestCode == REQUEST_SIMPLE_DIALOG) {
            Toast.makeText(c, "Negative button clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {
        if (requestCode == REQUEST_SIMPLE_DIALOG) {
            Toast.makeText(c, "Neutral button clicked", Toast.LENGTH_SHORT).show();
        }
    }

    // IDateDialogListener

    @Override
    public void onNegativeButtonClicked(int resultCode, Date date) {
        String text = "";
        if (resultCode == REQUEST_DATE_PICKER) {
            text = "Date ";
        } else if (resultCode == REQUEST_TIME_PICKER) {
            text = "Time ";
        }

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
        Toast.makeText(this, text + "Cancelled " + dateFormat.format(date), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositiveButtonClicked(int resultCode, Date date) {
        String text = "";
        if (resultCode == REQUEST_DATE_PICKER) {
            text = "Date ";
        } else if (resultCode == REQUEST_TIME_PICKER) {
            text = "Time ";
        }

        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        Toast.makeText(this, text + "Success! " + dateFormat.format(date), Toast.LENGTH_SHORT).show();
    }

    // menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        if (isDarkTheme()) {
            menu.findItem(R.id.theme_change).setTitle("Use Light Theme");
        }
        else {
            menu.findItem(R.id.theme_change).setTitle("Use Dark Theme");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.theme_change:
                if (isDarkTheme()) {
                    setTheme(R.style.AppTheme);
                    Toast.makeText(DemoActivity.this, "Light theme set", Toast.LENGTH_SHORT).show();
                } else {
                    setTheme(R.style.AppThemeDark);
                    Toast.makeText(DemoActivity.this, "Dark theme set", Toast.LENGTH_SHORT).show();
                }
                invalidateOptionsMenu();
                return true;
            case R.id.about:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://github.com/avast/android-styled-dialogs"));
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isDarkTheme() {
        boolean darkTheme = false;
        //Try-catch block is used to overcome resource not found exception
        try {
            TypedValue val = new TypedValue();

            //Reading attr value from current theme
            getTheme().resolveAttribute(com.avast.android.dialogs.R.attr.isLightTheme, val, true);

            //Passing the resource ID to TypedArray to get the attribute value
            TypedArray arr = obtainStyledAttributes(val.data, new int[]{com.avast.android.dialogs.R.attr.isLightTheme});
            darkTheme = !arr.getBoolean(0, false);
            arr.recycle();
        } catch (RuntimeException e) {
            //Resource not found , so sticking to light theme
            darkTheme = false;
        }
        return darkTheme;
    }

}
