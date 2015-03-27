# StyledDialogs for Android 
[![Build Status](https://travis-ci.org/avast/android-styled-dialogs.svg?branch=master)](https://travis-ci.org/avast/android-styled-dialogs) [![License](https://img.shields.io/badge/license-Apache%202-green.svg?style=flat)](https://github.com/avast/android-styled-dialogs/blob/master/LICENSE.txt) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-StyledDialogs-green.svg?style=flat)](https://android-arsenal.com/details/1/143) 

 ![Screenshot of the dialogs](graphics/screenshot-small.png)
 
Demo app:

<a href="https://play.google.com/store/apps/details?id=com.avast.dialogs"><img src="http://www.android.com/images/brand/get_it_on_play_logo_small.png" alt="Get it on Google Play" /></a>

Features:

 - Compatible with **Material Design Guidelines**
 - Same look for **Android 2.2+**
 - Built on top of standard **DialogFragment**
 - Supports stacked buttons, neutral button, callbacks even after rotation
 - Light and dark theme
 - Contains even more specialized dialogs: List, Progress, Time&Date Picker, Custom, ...

## How to include it in your project:

```groovy
dependencies {
	compile 'com.avast:android-styled-dialogs:2.2.0'
}
```    
Hosted in [jcenter](https://bintray.com/avast/android/styled-dialogs/): [ ![Download](https://api.bintray.com/packages/avast/android/styled-dialogs/images/download.svg) ](https://bintray.com/avast/android/styled-dialogs/_latestVersion)

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

For dark theme, inherit from `Theme.AppCompat`. Or you can force dark theme per individual dialog using `useDarkTheme()` builder method.
You can also force light theme per individual dialog using `useLightTheme()` builder method.

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

For Fragments use `setTargetFragment()` method in the builder.

It's not possible to use normal Java callbacks, because they are lost after device rotation.

### How to react on cancelling the dialog:

Implement interface `ISimpleDialogCancelListener` in your Activity/Fragment.

## How to create custom DialogFragments:

Extend `BaseDialogFragment`. 

Have a look at [JayneHatDialogFragment](https://github.com/avast/android-styled-dialogs/blob/master/demo/src/main/java/com/avast/dialogs/JayneHatDialogFragment.java) for a practical example.

## Contributing

Pull requests are welcomed!

Please set your Android Studio formatting to [our code style](https://github.com/avast/android-styled-dialogs/blob/master/code-formatting-config.xml).

## Why 'Dialogs'?

<img src="http://img.radio.cz/pictures/osobnosti/cimrman_jarax.jpg" width="70"  align="right"/>

Theodor Dialogs was a famous Czech painter, uncle of [Jára Cimrman](http://en.wikipedia.org/wiki/J%C3%A1ra_Cimrman). Jára Cimrman was a playwright, philosopher, inventor, teacher, poet, mathematician and more but he lacked painting skills. That's why Theodor was helping him with many projects. Just like Android and this library do.

See [**our other Czech personalities**](http://inmite.github.io) who help with [#AndroidDev](https://plus.google.com/s/%23AndroidDev).
