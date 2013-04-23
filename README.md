# StyledDialogs library

This library makes styling dialogs easy. They are compatible with Holo design and Android Design Guidelines, but allow custom colors and same look from Android 2.2+, which is normally hard to do. Dialogs have same API as native Android DialogFragments.

The library also contains `SimpleDialogFragment` class, which makes creating custom dialogs one-line of code. It also contains demo app which shows all posibilities of the library. 

## How to include it in your project:

	<dependency>
		<groupId>eu.inmite.android.lib</groupId>
		<artifactId>android-styled-dialogs</artifactId>
		<version>0.5</version>
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

	SimpleDialogFragment.show(this, R.string.message_1);

### Dialog with a title, message and Close button:

	SimpleDialogFragment.show(this, R.string.message_2, R.string.title); 

### Dialog with a title, message and two buttons:	
	SimpleDialogFragment.show(this, c.getString(R.string.message_3), this.getString(R.string.title), this.getString(R.string.positive_button), this.getString(R.string.negative_button));

### How to react on button press in your Activity/Fragment:

Simply implement interface `ISimpleDialogFragmentListener` in your Activity/Fragment. I recommend similar pattern in your other (non-simple) DialogFragments.

## How to create other (non-simple) DialogFragments:

Extend BaseDialogFragment class and look at demo app implementation. It shows two types of those implementations:

 - `FavoriteCharacterDialogFragment`: showing list in the dialog using extending `BaseDialogFragment`
 - `JayneHatDialogFragment`: showing a custom view in the dialog using extending `SimpleDialogFragment`

### What can you set in a DialogFragment
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

		

