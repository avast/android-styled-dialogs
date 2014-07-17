# StyledDialogs for Android

This library makes styling and using dialogs _a piece of cake_.

 ![Screenshot of the dialogs](graphics/screenshot-small.png)

Features:

 - Compatible with Holo style and **Android Design Guidelines**
 - Change style for all dialogs only by changing a **few color resources**
 - Same look for **Android 2.2+**
 - **Same API** as native Android DialogFragments
 - `SimpleDialogFragment` class, which makes displaying simple dialogs a **one line of code**
 - `ListDialogFragment`, `DatePickerDialogFragment` and `TimePickerDialogFragment`

## How to include it in your project:

With Gradle:
```groovy
compile 'eu.inmite.android.lib:android-styled-dialogs:1.2.0'
```    

With Maven:
```xml
<dependency>
	<groupId>eu.inmite.android.lib</groupId>
	<artifactId>android-styled-dialogs</artifactId>
	<version>1.2.0</version>
	<type>aar</type>
</dependency>
```

Manually:

 - clone the project
 - add it as library project in your IDE
 - include latest support library

## How to style all dialogs:

Add following into your application theme:
```xml
<item name="sdlDialogStyle">@style/DialogStyleLight.Custom</item>
```
or
```xml
<item name="sdlDialogStyle">@style/DialogStyleDark.Custom</item>
```
Define your dialog style, example for light theme:
```xml
<style name="DialogStyleLight.Custom">
	<!-- anything can be left out: -->
	<item name="titleTextColor">@color/dialog_title_text</item>
	<item name="titleSeparatorColor">@color/dialog_title_separator</item>
	<item name="messageTextColor">@color/dialog_message_text</item>
	<item name="buttonTextColor">@color/dialog_button_text</item>
	<item name="buttonSeparatorColor">@color/dialog_button_separator</item>
	<item name="buttonBackgroundColorNormal">@color/dialog_button_normal</item>
	<item name="buttonBackgroundColorPressed">@color/dialog_button_pressed</item>
	<item name="buttonBackgroundColorFocused">@color/dialog_button_focused</item>
	<item name="dialogBackground">@drawable/dialog_background</item>
</style>
```

## How to create simple dialogs:

Easy:

### Dialog with a simple message and Close button:
```java
SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setMessage(R.string.message).show();
```
### Dialog with a title, message and Close button:
```java
SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle(R.string.title).setMessage(R.string.message).show();
```
### Dialog with a title, message and two buttons:	
```java
SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle(R.string.title).setMessage(R.string.message).setPositiveButtonText(R.string.positive_button).setNegativeButtonText(R.string.negative_button).show();
```
### How to react on button press in your Activity/Fragment:

Simply implement interface `ISimpleDialogListener` in your Activity/Fragment. Listener's callbacks have `requestCode` parameter - you can use it if you have more dialogs in one Activity/Fragment.
For Fragments use setTargetFragment() method in the builder.

### How to react on cancelling the dialog:

Implement interface `ISimpleDialogCancelListener` in your Activity/Fragment.

## How to create all other DialogFragments:

Extend `BaseDialogFragment`. 

Demo app contains a `JayneHatDialogFragment` which shows how to add custom view to a dialog.

## Why 'Dialogs'?

<img src="http://img.radio.cz/pictures/osobnosti/cimrman_jarax.jpg" width="70"  align="right"/>

Theodor Dialogs was a famous Czech painter, uncle of [Jára Cimrman](http://en.wikipedia.org/wiki/J%C3%A1ra_Cimrman). Jára Cimrman was a playwright, philosopher, inventor, teacher, poet, mathematician and more but he lacked painting skills. That's why Theodor was helping him with many projects. Just like Android and this library do.

See [**our other Czech personalities**](http://inmite.github.io) who help with [#AndroidDev](https://plus.google.com/s/%23AndroidDev).
