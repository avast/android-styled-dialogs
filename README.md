# StyledDialogs for Android

 ![Screenshot of the dialogs](graphics/screenshot-small.png)

Features:

 - Compatible with **Material Design Guidelines**
 - Same look for **Android 2.2+**
 - Built on top of standard **DialogFragment**
 - Supports stacked buttons, neutral button, callbacks even after rotation
 - Contains even more specialized dialogs: List, Progress, Time&Date Picker, Custom, ...

## How to include it in your project:

```groovy
dependencies {
	compile 'com.avast:android-styled-dialogs:2.0.1'
}
```    
(from [jcenter](https://bintray.com/avast/android/styled-dialogs/))

[Looking for Holo dialogs?](https://github.com/inmite/android-styled-dialogs/tree/holo)

## How to style all dialogs:

It uses standard Material colors, for example like this:

```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">@color/indigo</item>
        <item name="colorPrimaryDark">@color/indigo_dark</item>
        <item name="colorAccent">@color/pink</item>
</style>
```

## How to create simple dialogs:

Easy:

### Dialog with a simple message only:
```java
SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setMessage(R.string.message).show();
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
