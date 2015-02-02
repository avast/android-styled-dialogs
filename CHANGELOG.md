# Changelog

## 2.1.0

February 2, 2015

 - new multi-choice and single-choice modes for ListDialogFragment
 - listeners called for both activity and fragment
 - cancel listener made more general
 - broke ISimpleDialogListener into separated button listeners
 - TimePickerDialog fragment has correct default timezone and 24-hour view
 - Custom view is no longer wrapped in ScrollView

## 2.0.1

January 8, 2015

- complete rewrite for Material design

## 1.1.2

January 2, 2014

 - Gradle support! (AAR dependency)
 - support for icon next to title
 - support for HTML in the message
 - support for controlling cancelable behaviour
 - bugfixes

## 1.0.1

July 26, 2013

 - Gradle support (#3)
 - fixed paddings to better match native style
 - fixed horizontal separator for neutral button (#7)
 - posibility to remove Close button in simple dialogs

## 1.0

June 27, 2013

 - BREAKING CHANGES!
 - styling dialogs using attributes
 - light theme
 - show methods for SimpleDialogFragment removed in favor of Builder
 - change button order before ICS
 - SimpleDialogFragment.Builder requires Context and FragmentManager, not Activity
 - fixed vertical align for buttons
 - fix for #1 and #2

## 0.5.7.

June 5, 2013

 - initial public release
