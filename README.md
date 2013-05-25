# StyledDialogs library

This library makes styling and using dialogs _a piece of cake_.

Features:

 - Compatible with Holo style and **Android Design Guidelines**
 - Changing style for all dialogs by only changing **few color resources**
 - Same look for **Android 2.2+**
 - **Same API** as native Android DialogFragments
 - `SimpleDialogFragment` class, which makes displaying simple dialogs **one line of code**

 Screenshots:

 ![Screenshot of the dialogs](https://github.com/inmite/android-styled-dialogs/blob/master/screenshot.png)

## How to include it in your project:

	<dependency>
		<groupId>eu.inmite.android.lib</groupId>
		<artifactId>android-styled-dialogs</artifactId>
		<version>(insert latest version)</version>
		<type>apklib</type>
	</dependency>

## How to style all dialogs:

Simply put colors you want to change into your colors.xml. Everything is optional, if you leave anything you get default Holo Dark design. Here are default values:

	<color name="dialog_message_text">#FFFFFF</color>
	<color name="dialog_title_text">#ff0099cc</color>
	<color name="dialog_title_separator">#ff0099cc</color>
	<color name="dialog_button_text">#FFFFFF</color>
	<color name="dialog_button_normal">@android:color/transparent</color>
	<color name="dialog_button_pressed">#990099cc</color> <!-- 60 % opacity -->
	<color name="dialog_button_focused">#4C0099cc</color> <!-- 30 % opacity -->
	<color name="dialog_button_separator">#12ffffff</color> <!-- 7% opacity -->

Only drawable resource is `dialog_bg.9.png` - put it in your project to override default background of the dialog.

## How to create simple dialogs:

Easy:

### Dialog with a simple message and Close button:

	SimpleDialogFragment.show(this, null, R.string.message_1);

### Dialog with a title, message and Close button:

	SimpleDialogFragment.show(this, null, R.string.message_2, R.string.title);

### Dialog with a title, message and two buttons:	
	SimpleDialogFragment.show(this, null, c.getString(R.string.message_3), this.getString(R.string.title), this.getString(R.string.positive_button), this.getString(R.string.negative_button));

### How to react on button press in your Activity/Fragment:

Simply implement interface `ISimpleDialogListener` in your Activity/Fragment. Similar pattern in your other DialogFragments is recommended.

### How to handle more callback in one Activity/Fragment:

Use listener's callbacks have `requestCode` parameter.

### How to react on cancelling the dialog:

Implement interface `ISimpleDialogCancelListener` in your Activity/Fragment.


## How create SimpleDialogFragment more easily using SimpleDialogFragmentBuilder:

    SimpleDialogFragmentBuilder builder = new SimpleDialogFragmentBuilder(MyActivity.this);
	builder.setTitle("Some title")
			.setMessage(R.string.message_3)
			.setPositiveButtonText(R.string.ok)
			.setNegativeButtonText("Cancel")
			.setCancelable(true)
			.setRequestCode(1)
			.build();


## How to create other (non-simple) DialogFragments:

Extend `BaseDialogFragment` and look at demo app implementation. It shows two types of those implementations:

 - `FavoriteCharacterDialogFragment` shows a list in the dialog using extending `BaseDialogFragment`
 - `JayneHatDialogFragment` shows a custom view in the dialog using extending `SimpleDialogFragment`

### What can you setup in the dialog builder:
 - title
 - message
 - three buttons (positive, negative, neutral)
 - list of items
 - custom view

### What is missing/TODO:
 - date picker (spinners or calendar)
 - time picker
 - edit field
 - password field
 - ...

		

