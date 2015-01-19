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

package eu.inmite.android.lib.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Base dialog fragment for all your dialogs, styleable and same design on Android 2.2+.
 *
 * @author David Vávra (david@inmite.eu)
 */
public abstract class BaseDialogFragment extends DialogFragment {

	/**
	 * Bundle argument tags to identify dialog window.
	 */
	public final static String ARG_REQUEST_CODE = "request_code";
	public final static int DEFAULT_REQUEST_CODE = -42;

	/**
	 * Bundle argument tags for buttons, dialog message and title.
	 */
	public final static String ARG_MESSAGE = "message";
	public final static String ARG_TITLE = "title";
	public final static String ARG_POSITIVE_BUTTON = "positive_button";
	public final static String ARG_NEGATIVE_BUTTON = "negative_button";
	public final static String ARG_NEUTRAL_BUTTON = "neutral_button";

	/**
	 * Bundle argument tag for cancellation param.
	 */
	public final static String ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_oto";
	public final static String ARG_CANCELABLE = "cancelable";

	/**
	 * Bundle argument for list items.
	 */
	public final static String ARG_ITEMS = "items";

	/**
	 * Bundle arguments for multi choice list items.
	 */
	public final static String ARG_ITEMS_MULTICHOICE = "items_multichoice";
	public final static String ARG_ITEMS_SELECTED_MULTICHOICE = "items_selected_multichoice";

	/**
	 * Default fragment tag.
	 */
	public final static String DEFAULT_TAG = "simple_dialog";

	/**
	 * States for list items
	 */
	private final static int[] pressedState = {android.R.attr.state_pressed};
	private final static int[] focusedState = {android.R.attr.state_focused};
	private final static int[] defaultState = {android.R.attr.state_enabled};
	/**
	 * Identification of caller
	 */
	protected int mRequestCode;

	/**
	 * Children should extend this to add more things to base builder.
	 * <p/>
	 * Typically {@code BaseDialogBuilder.setTitle} and {@code BaseDialogBuilder.setMessage} and
	 * {@code BaseDialogBuilder.setPositiveButton} are called here.
	 */
	protected abstract Builder build(Builder initialBuilder);

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.SDL_Dialog);
		// custom dialog background
		final TypedArray a = getActivity().getTheme().obtainStyledAttributes(null,
			R.styleable.DialogStyle, R.attr.sdlDialogStyle, 0);
		Drawable dialogBackground = a.getDrawable(R.styleable.DialogStyle_dialogBackground);
		a.recycle();
		dialog.getWindow().setBackgroundDrawable(dialogBackground);
		Bundle args = getArguments();
		if (args != null) {
			boolean cancelable = args.getBoolean(ARG_CANCELABLE);
			dialog.setCancelable(cancelable);
			dialog.setCanceledOnTouchOutside(args.getBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE));
		}
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		Builder builder = new Builder(this, getActivity(), inflater, container);
		return build(builder).create();
	}

	@Override
	public void onDestroyView() {
		// bug in the compatibility library
		if (getDialog() != null && getRetainInstance()) {
			getDialog().setDismissMessage(null);
		}
		super.onDestroyView();
	}

	public void showAllowingStateLoss(FragmentManager manager, String tag) {
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(this, tag);
		ft.commitAllowingStateLoss();
	}

	/**
	 * @return the positive button if specified and the view is created, null otherwise
	 */
	protected Button getPositiveButton() {
		if (getView() != null) {
			return (Button)getView().findViewById(R.id.sdl__positive_button);
		} else {
			return null;
		}
	}

	/**
	 * @return the negative button if specified and the view is created, null otherwise
	 */
	protected Button getNegativeButton() {
		if (getView() != null) {
			return (Button)getView().findViewById(R.id.sdl__negative_button);
		} else {
			return null;
		}
	}

	/**
	 * @return the neutral button if specified and the view is created, null otherwise
	 */
	protected Button getNeutralButton() {
		if (getView() != null) {
			return (Button)getView().findViewById(R.id.sdl__neutral_button);
		} else {
			return null;
		}
	}

	/**
	 * Returns the dialog message.
	 *
	 * @return message
	 */
	protected CharSequence getMessage() {
		return getArguments().getCharSequence(ARG_MESSAGE);
	}

	/**
	 * Returns the title.
	 *
	 * @return title
	 */
	protected String getTitle() {
		return getArguments().getString(ARG_TITLE);
	}

	/**
	 * Returns positive button text.
	 *
	 * @return text for the button.
	 */
	protected String getPositiveButtonText() {
		return getArguments().getString(ARG_POSITIVE_BUTTON);
	}

	/**
	 * Returns negative button text.
	 *
	 * @return text for the button.
	 */
	protected String getNegativeButtonText() {
		return getArguments().getString(ARG_NEGATIVE_BUTTON);
	}

	/**
	 * Returns neutral button text.
	 *
	 * @return text for the button.
	 */
	protected String getNeutralButtonText() {
		return getArguments().getString(ARG_NEUTRAL_BUTTON);
	}

	/**
	 * Gets arguments for list items.
	 *
	 * @return StringArray
	 */
	protected String[] getItems() {
		return getArguments().getStringArray(ARG_ITEMS);
	}

	/**
	 * Gets text array of multi choice list.
	 *
	 * @return array containing texts from multi choice list.
	 */
	protected String[] getMultiChoiceItems() {
		return getArguments().getStringArray(ARG_ITEMS_MULTICHOICE);
	}

	/**
	 * Gets array with checked state of multi choice list.
	 *
	 * @return boolean array with checked states.
	 */
	protected boolean[] getMultiChoiceSelectedItems() {
		return getArguments().getBooleanArray(ARG_ITEMS_SELECTED_MULTICHOICE);
	}


	/**
	 * Custom dialog builder
	 */
	protected static class Builder {
		protected BaseDialogBuilder.DialogParams mDialogParams;


		/**
		 * Public constructor.
		 *
		 * @param dialogFragment calling fragment
		 * @param context        context
		 * @param inflater       inflater
		 * @param container      view group container
		 */
		public Builder(DialogFragment dialogFragment, Context context, LayoutInflater inflater,
		               ViewGroup container) {
			mDialogParams = new BaseDialogBuilder.DialogParams(context);
			mDialogParams.dialogFragment = dialogFragment;
			mDialogParams.container = container;
			mDialogParams.layoutInflater = inflater;
		}

		/**
		 * Gets LayoutInflater.
		 *
		 * @return LayoutInflater
		 */
		public LayoutInflater getLayoutInflater() {
			return mDialogParams.layoutInflater;
		}

		/**
		 * Sets the dialog title.
		 *
		 * @param titleId resource id
		 * @return builder
		 */
		public Builder setTitle(int titleId) {
			mDialogParams.title = mDialogParams.context.getString(titleId);
			return this;
		}

		/**
		 * Sets the dialog title.
		 *
		 * @param title dialog title
		 * @return builder
		 */
		public Builder setTitle(CharSequence title) {
			mDialogParams.title = title;
			return this;
		}

		/**
		 * Sets dialog cancelable on touch outside.
		 *
		 * @param cancelableOnTouchOutside true/false (default is true)
		 * @return builder
		 */
		public Builder setCancelableOnTouchOutside(boolean cancelableOnTouchOutside) {
			mDialogParams.cancelableOnTouchOutside = cancelableOnTouchOutside;
			return this;
		}

		/**
		 * Sets whether dialog can be cancelable.
		 *
		 * @param cancelable true/false (default is true)
		 * @return builder
		 */
		public Builder setCancelable(boolean cancelable) {
			mDialogParams.cancelable = cancelable;
			return this;
		}

		/**
		 * Sets the positive button.
		 *
		 * @param textId   button label
		 * @param listener button click listener
		 * @return builder
		 */
		public Builder setPositiveButton(int textId, final View.OnClickListener listener) {
			mDialogParams.positiveButtonText = mDialogParams.context.getString(textId);
			mDialogParams.positiveButtonListener = listener;
			return this;
		}

		/**
		 * Sets the positive button.
		 *
		 * @param text     button label
		 * @param listener button click listener
		 * @return builder
		 */
		public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
			mDialogParams.positiveButtonText = text;
			mDialogParams.positiveButtonListener = listener;
			return this;
		}

		/**
		 * Sets the negative button.
		 *
		 * @param textId   button label
		 * @param listener button click listener
		 * @return builder
		 */
		public Builder setNegativeButton(int textId, final View.OnClickListener listener) {
			mDialogParams.negativeButtonText = mDialogParams.context.getString(textId);
			mDialogParams.negativeButtonListener = listener;
			return this;
		}

		/**
		 * Sets the negative button.
		 *
		 * @param text     button label
		 * @param listener button click listener
		 * @return builder
		 */
		public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
			mDialogParams.negativeButtonText = text;
			mDialogParams.negativeButtonListener = listener;
			return this;
		}

		/**
		 * Sets neutral button.
		 *
		 * @param textId   button label
		 * @param listener button click listener
		 * @return builder
		 */
		public Builder setNeutralButton(int textId, final View.OnClickListener listener) {
			mDialogParams.neutralButtonText = mDialogParams.context.getString(textId);
			mDialogParams.neutralButtonListener = listener;
			return this;
		}

		/**
		 * Sets neutral button.
		 *
		 * @param text     button label
		 * @param listener button click listener
		 * @return builder
		 */
		public Builder setNeutralButton(CharSequence text, final View.OnClickListener listener) {
			mDialogParams.neutralButtonText = text;
			mDialogParams.neutralButtonListener = listener;
			return this;
		}

		/**
		 * Sets the message for dialog.
		 *
		 * @param messageId main text in dialog
		 * @return builder
		 */
		public Builder setMessage(int messageId) {
			mDialogParams.message = mDialogParams.context.getString(messageId);
			return this;
		}

		/**
		 * Sets the message for dialog.
		 *
		 * @param message main text in dialog
		 * @return builder
		 */
		public Builder setMessage(CharSequence message) {
			mDialogParams.message = message;
			return this;
		}

		/**
		 * Sets list.
		 *
		 * @param listAdapter    list adapter
		 * @param checkedItemIdx Item check by default, -1 if no item should be checked
		 * @param listener       item click listener
		 * @return builder
		 */
		public Builder setItems(ListAdapter listAdapter, int checkedItemIdx,
		                        final AdapterView.OnItemClickListener listener) {
			mDialogParams.listAdapter = listAdapter;
			mDialogParams.onItemClickListener = listener;
			mDialogParams.listCheckedItemIdx = checkedItemIdx;
			return this;
		}

		/**
		 * Sets the list for multi choice.
		 *
		 * @param listAdapter The list adapter.
		 * @return builder
		 */
		public Builder setMultiChoiceItems(ListAdapter listAdapter) {
			mDialogParams.listAdapter = listAdapter;
			return this;
		}

		/**
		 * Sets view for dialog.
		 *
		 * @param view main view
		 * @return builder
		 */
		public Builder setView(View view) {
			mDialogParams.view = view;
			mDialogParams.viewSpacingSpecified = false;
			return this;
		}

		/**
		 * Sets view for dialog.
		 *
		 * @param view              main view
		 * @param viewSpacingLeft   left padding
		 * @param viewSpacingTop    top padding
		 * @param viewSpacingRight  right padding
		 * @param viewSpacingBottom bottom padding
		 * @return builder
		 */
		public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
		                       int viewSpacingBottom) {
			mDialogParams.view = view;
			mDialogParams.viewSpacingSpecified = true;
			mDialogParams.viewSpacingLeft = viewSpacingLeft;
			mDialogParams.viewSpacingTop = viewSpacingTop;
			mDialogParams.viewSpacingRight = viewSpacingRight;
			mDialogParams.viewSpacingBottom = viewSpacingBottom;
			return this;
		}

		/**
		 * Sets dialog icon.
		 *
		 * @param resourceId resource id of icon drawable
		 * @return builder
		 */
		public Builder setIcon(int resourceId) {
			mDialogParams.icon = mDialogParams.context.getResources().getDrawable(resourceId);
			return this;
		}

		/**
		 * Sets dialog icon.
		 *
		 * @param drawable drawable
		 * @return builder
		 */
		public Builder setIcon(Drawable drawable) {
			mDialogParams.icon = drawable;
			return this;
		}

		/**
		 * Creates dialog view.
		 *
		 * @return view
		 */
		public View create() {
			final Resources res = mDialogParams.context.getResources();
			final int defaultTitleTextColor = res.getColor(R.color.sdl_title_text_dark);
			final int defaultTitleSeparatorColor = res.getColor(R.color.sdl_title_separator_dark);
			final int defaultMessageTextColor = res.getColor(R.color.sdl_message_text_dark);
			final ColorStateList defaultButtonTextColor = res.getColorStateList(
				R.color.sdl_button_text_dark);
			final int defaultButtonSeparatorColor = res.getColor(R.color.sdl_button_separator_dark);
			final int defaultButtonBackgroundColorNormal = res.getColor(R.color.sdl_button_normal_dark);
			final int defaultButtonBackgroundColorPressed = res.getColor(R.color.sdl_button_pressed_dark);
			final int defaultButtonBackgroundColorFocused = res.getColor(R.color.sdl_button_focused_dark);

			final TypedArray a = mDialogParams.context.getTheme().obtainStyledAttributes(null,
				R.styleable.DialogStyle, R.attr.sdlDialogStyle, 0);
			mDialogParams.titleTextColor = a.getColor(R.styleable.DialogStyle_titleTextColor,
				defaultTitleTextColor);
			mDialogParams.titleSeparatorColor = a.getColor(R.styleable.DialogStyle_titleSeparatorColor,
				defaultTitleSeparatorColor);
			mDialogParams.messageTextColor = a.getColor(R.styleable.DialogStyle_messageTextColor,
				defaultMessageTextColor);
			mDialogParams.buttonTextColor = a.getColorStateList(R.styleable.DialogStyle_buttonTextColor);
			if (mDialogParams.buttonTextColor == null) {
				mDialogParams.buttonTextColor = defaultButtonTextColor;
			}
			mDialogParams.buttonSeparatorColor = a.getColor(R.styleable.DialogStyle_buttonSeparatorColor,
				defaultButtonSeparatorColor);
			mDialogParams.buttonBackgroundColorNormal = a.getColor(
				R.styleable.DialogStyle_buttonBackgroundColorNormal, defaultButtonBackgroundColorNormal);
			mDialogParams.buttonBackgroundColorPressed = a.getColor(
				R.styleable.DialogStyle_buttonBackgroundColorPressed,
				defaultButtonBackgroundColorPressed);
			mDialogParams.buttonBackgroundColorFocused = a.getColor(
				R.styleable.DialogStyle_buttonBackgroundColorFocused,
				defaultButtonBackgroundColorFocused);

			if (mDialogParams.listAdapter != null) {
				final int defaultListItemSeparatorColor = res.getColor(
					R.color.sdl_list_item_separator_dark);
				final int defaultListItemBackgroundColorNormal = res.getColor(
					R.color.sdl_button_normal_dark);
				final int defaultListItemBackgroundColorFocused = res.getColor(
					R.color.sdl_button_focused_dark);
				final int defaultListItemBackgroundColorPressed = res.getColor(
					R.color.sdl_button_pressed_dark);
				mDialogParams.listItemSeparatorColor = a.getColor(
					R.styleable.DialogStyle_listItemSeparatorColor, defaultListItemSeparatorColor);
				mDialogParams.listItemBackgroundColorNormal = a.getColor(
					R.styleable.DialogStyle_listItemColorNormal, defaultListItemBackgroundColorNormal);
				mDialogParams.listItemBackgroundColorFocused = a.getColor(
					R.styleable.DialogStyle_listItemColorFocused, defaultListItemBackgroundColorFocused);
				mDialogParams.listItemBackgroundColorPressed = a.getColor(
					R.styleable.DialogStyle_listItemColorPressed, defaultListItemBackgroundColorPressed);
			}
			a.recycle();

			View view = getDialogLayoutAndInitTitle();

			LinearLayout content = (LinearLayout)view.findViewById(R.id.sdl__content);

			if (mDialogParams.message != null) {
				View viewMessage = mDialogParams.layoutInflater.inflate(R.layout.dialog_part_message,
					content, false);
				TextView tvMessage = (TextView)viewMessage.findViewById(R.id.sdl__message);
				tvMessage.setTextColor(mDialogParams.messageTextColor);
				tvMessage.setText(mDialogParams.message);
				content.addView(viewMessage);
			}

			if (mDialogParams.view != null) {
				FrameLayout customPanel = (FrameLayout)mDialogParams.layoutInflater.inflate(
					R.layout.dialog_part_custom, content, false);
				FrameLayout custom = (FrameLayout)customPanel.findViewById(R.id.sdl__custom);
				custom.addView(mDialogParams.view, new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				if (mDialogParams.viewSpacingSpecified) {
					custom.setPadding(mDialogParams.viewSpacingLeft, mDialogParams.viewSpacingTop,
						mDialogParams.viewSpacingRight, mDialogParams.viewSpacingBottom);
				}
				content.addView(customPanel);
			}

			if (mDialogParams.listAdapter != null) {
				ListView listView = (ListView)mDialogParams.layoutInflater.inflate(
					R.layout.dialog_part_list, content, false);
				listView.setAdapter(mDialogParams.listAdapter);
				listView.setDivider(getColoredListItemsDivider());
				listView.setDividerHeight(1);
				listView.setSelector(getListItemSelector());
				listView.setOnItemClickListener(mDialogParams.onItemClickListener);
				if (mDialogParams.listCheckedItemIdx != -1) {
					listView.setSelection(mDialogParams.listCheckedItemIdx);
				}
				content.addView(listView);
			}

			addButtons(content);

			return view;
		}

		/**
		 * Gets dialog layout and inits title.
		 *
		 * @return view DialogLayout
		 */
		@SuppressWarnings("deprecation")
		private View getDialogLayoutAndInitTitle() {
			View view = mDialogParams.layoutInflater.inflate(R.layout.dialog_part_title,
				mDialogParams.container, false);
			TextView tvTitle = (TextView)view.findViewById(R.id.sdl__title);
			View viewTitleDivider = view.findViewById(R.id.sdl__titleDivider);

			if (!TextUtils.isEmpty(mDialogParams.title)) {
				tvTitle.setVisibility(View.VISIBLE);
				viewTitleDivider.setVisibility(View.VISIBLE);
				tvTitle.setText(mDialogParams.title);
				tvTitle.setTextColor(mDialogParams.titleTextColor);
				if (mDialogParams.icon != null) {
					tvTitle.setCompoundDrawablesWithIntrinsicBounds(mDialogParams.icon, null, null, null);
					tvTitle.setCompoundDrawablePadding(
						mDialogParams.context.getResources().getDimensionPixelSize(R.dimen.grid_2));
				}
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					viewTitleDivider.setBackgroundDrawable(new ColorDrawable(
						mDialogParams.titleSeparatorColor));
				} else {
					viewTitleDivider.setBackground(new ColorDrawable(mDialogParams.titleSeparatorColor));
				}

			} else {
				tvTitle.setVisibility(View.GONE);
				viewTitleDivider.setVisibility(View.GONE);
			}
			return view;
		}

		/**
		 * Adds buttons to given layout.
		 *
		 * @param listDialog layout.
		 */
		@SuppressWarnings("deprecation")
		private void addButtons(LinearLayout listDialog) {
			if (mDialogParams.negativeButtonText != null || mDialogParams.neutralButtonText != null ||
				mDialogParams.positiveButtonText != null) {
				View viewButtonPanel = mDialogParams.layoutInflater.inflate(
					R.layout.dialog_part_button_panel, listDialog, false);
				LinearLayout llButtonPanel = (LinearLayout)viewButtonPanel.findViewById(
					R.id.dialog_button_panel);
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					viewButtonPanel.findViewById(R.id.dialog_horizontal_separator).setBackgroundDrawable(
						new ColorDrawable(mDialogParams.buttonSeparatorColor));
				} else {
					viewButtonPanel.findViewById(R.id.dialog_horizontal_separator).setBackground(
						new ColorDrawable(mDialogParams.buttonSeparatorColor));
				}

				boolean addDivider = false;

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					addDivider = addPositiveButton(llButtonPanel, addDivider);
				} else {
					addDivider = addNegativeButton(llButtonPanel, addDivider);
				}
				addDivider = addNeutralButton(llButtonPanel, addDivider);

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					addNegativeButton(llButtonPanel, addDivider);
				} else {
					addPositiveButton(llButtonPanel, addDivider);
				}

				listDialog.addView(viewButtonPanel);
			}
		}

		/**
		 * Adds negative button.
		 *
		 * @param parent     parent view group
		 * @param addDivider true/false whether parent needs divider
		 * @return divider defined in parent or true if negative button was added
		 */
		@SuppressWarnings("deprecation")
		private boolean addNegativeButton(ViewGroup parent, boolean addDivider) {
			if (mDialogParams.negativeButtonText != null) {
				if (addDivider) {
					addDivider(parent);
				}
				Button btn = (Button)mDialogParams.layoutInflater.inflate(R.layout.dialog_part_button,
					parent, false);
				btn.setId(R.id.sdl__negative_button);
				btn.setText(mDialogParams.negativeButtonText);
				btn.setTextColor(mDialogParams.buttonTextColor);
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					btn.setBackgroundDrawable(getButtonBackground());
				} else {
					btn.setBackground(getButtonBackground());
				}

				btn.setOnClickListener(mDialogParams.negativeButtonListener);
				parent.addView(btn);
				return true;
			}
			return addDivider;
		}

		/**
		 * Adds positive button.
		 *
		 * @param parent     parent view group
		 * @param addDivider true/false whether parent needs divider
		 * @return divider defined in parent or true if positive button was added
		 */
		@SuppressWarnings("deprecation")
		private boolean addPositiveButton(ViewGroup parent, boolean addDivider) {
			if (mDialogParams.positiveButtonText != null) {
				if (addDivider) {
					addDivider(parent);
				}
				Button btn = (Button)mDialogParams.layoutInflater.inflate(R.layout.dialog_part_button,
					parent, false);
				btn.setId(R.id.sdl__positive_button);
				btn.setText(mDialogParams.positiveButtonText);
				btn.setTextColor(mDialogParams.buttonTextColor);
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					btn.setBackgroundDrawable(getButtonBackground());
				} else {
					btn.setBackground(getButtonBackground());
				}
				btn.setOnClickListener(mDialogParams.positiveButtonListener);
				parent.addView(btn);
				return true;
			}
			return addDivider;
		}

		/**
		 * Adds neutral button.
		 *
		 * @param parent     parent view group
		 * @param addDivider true/false whether parent needs divider
		 * @return divider defined in parent or true if neutral  button was added
		 */
		@SuppressWarnings("deprecation")
		private boolean addNeutralButton(ViewGroup parent, boolean addDivider) {
			if (mDialogParams.neutralButtonText != null) {
				if (addDivider) {
					addDivider(parent);
				}
				Button btn = (Button)mDialogParams.layoutInflater.inflate(R.layout.dialog_part_button,
					parent, false);
				btn.setId(R.id.sdl__neutral_button);
				btn.setText(mDialogParams.neutralButtonText);
				btn.setTextColor(mDialogParams.buttonTextColor);
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					btn.setBackgroundDrawable(getButtonBackground());
				} else {
					btn.setBackground(getButtonBackground());
				}
				btn.setOnClickListener(mDialogParams.neutralButtonListener);
				parent.addView(btn);
				return true;
			}
			return addDivider;
		}

		/**
		 * Adds divider.
		 *
		 * @param parent parent view group
		 */
		@SuppressWarnings("deprecation")
		private void addDivider(ViewGroup parent) {
			View view = mDialogParams.layoutInflater.inflate(R.layout.dialog_part_button_separator,
				parent, false);
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
				view.findViewById(R.id.dialog_button_separator).setBackgroundDrawable(new ColorDrawable(
					mDialogParams.buttonSeparatorColor));
			} else {
				view.findViewById(R.id.dialog_button_separator).setBackground(new ColorDrawable(
					mDialogParams.buttonSeparatorColor));
			}
			parent.addView(view);
		}

		/**
		 * Defines button background with all states.
		 *
		 * @return background for button.
		 */
		private StateListDrawable getButtonBackground() {
			ColorDrawable colorDefault = new ColorDrawable(mDialogParams.buttonBackgroundColorNormal);
			ColorDrawable colorPressed = new ColorDrawable(mDialogParams.buttonBackgroundColorPressed);
			ColorDrawable colorFocused = new ColorDrawable(mDialogParams.buttonBackgroundColorFocused);
			StateListDrawable background = new StateListDrawable();
			background.addState(pressedState, colorPressed);
			background.addState(focusedState, colorFocused);
			background.addState(defaultState, colorDefault);
			return background;
		}

		/**
		 * Defines list item background with all states.
		 *
		 * @return background for list.
		 */
		private StateListDrawable getListItemSelector() {
			ColorDrawable colorDefault = new ColorDrawable(mDialogParams.listItemBackgroundColorNormal);
			ColorDrawable colorPressed = new ColorDrawable(mDialogParams.listItemBackgroundColorPressed);
			ColorDrawable colorFocused = new ColorDrawable(mDialogParams.listItemBackgroundColorFocused);
			StateListDrawable background = new StateListDrawable();
			background.addState(pressedState, colorPressed);
			background.addState(focusedState, colorFocused);
			background.addState(defaultState, colorDefault);
			return background;
		}

		/**
		 * Defines list item divider.
		 *
		 * @return divider
		 */
		private ColorDrawable getColoredListItemsDivider() {
			ColorDrawable colorDividerDrawable = new ColorDrawable(mDialogParams.listItemSeparatorColor);
			return colorDividerDrawable;
		}
	}
}
