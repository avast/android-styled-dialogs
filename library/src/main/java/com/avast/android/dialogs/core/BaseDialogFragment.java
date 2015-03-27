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

package com.avast.android.dialogs.core;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avast.android.dialogs.R;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.util.TypefaceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base dialog fragment for all your dialogs, styleable and same design on Android 2.2+.
 *
 * @author David Vávra (david@inmite.eu)
 */
public abstract class BaseDialogFragment extends DialogFragment implements DialogInterface.OnShowListener {

    //True then use dark theme , else by default make use of light theme
    private static boolean darkTheme;
    protected int mRequestCode;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        if (args != null) {

            if (args.getBoolean(BaseDialogBuilder.ARG_USE_DARK_THEME)) {
                //Developer is explicitly using the dark theme
                darkTheme = true;
            } else if (args.getBoolean(BaseDialogBuilder.ARG_USE_LIGHT_THEME)) {
                //Developer is explicitly using the light theme
                darkTheme = false;
            } else {
                //Dynamically detecting the theme declared in manifest
                resolveTheme();
            }

        } else {

            //Dynamically detecting the theme declared in manifest
            resolveTheme();
        }

        Dialog dialog = new Dialog(getActivity(), darkTheme ? R.style.SDL_Dialog_Dark : R.style.SDL_Dialog);

        if (args != null) {
            dialog.setCanceledOnTouchOutside(
                    args.getBoolean(BaseDialogBuilder.ARG_CANCELABLE_ON_TOUCH_OUTSIDE));
        }
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity(), inflater, container);
        return build(builder).create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            mRequestCode = getTargetRequestCode();
        } else {
            Bundle args = getArguments();
            if (args != null) {
                mRequestCode = args.getInt(BaseDialogBuilder.ARG_REQUEST_CODE, 0);
            }
        }
    }

    /**
     * Key method for using {@link com.avast.android.dialogs.core.BaseDialogFragment}.
     * Customized dialogs need to be set up via provided builder.
     *
     * @param initialBuilder Provided builder for setting up customized dialog
     * @return Updated builder
     */
    protected abstract Builder build(Builder initialBuilder);

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

    @Override
    public void onShow(DialogInterface dialog) {
        if (getView() != null) {
            ScrollView vMessageScrollView = (ScrollView) getView().findViewById(R.id.sdl_message_scrollview);
            ListView vListView = (ListView) getView().findViewById(R.id.sdl_list);
            FrameLayout vCustomViewNoScrollView = (FrameLayout) getView().findViewById(R.id.sdl_custom);
            boolean customViewNoScrollViewScrollable = false;
            if (vCustomViewNoScrollView.getChildCount() > 0) {
                View firstChild = vCustomViewNoScrollView.getChildAt(0);
                if (firstChild instanceof ViewGroup) {
                    customViewNoScrollViewScrollable = isScrollable((ViewGroup) firstChild);
                }
            }
            boolean listViewScrollable = isScrollable(vListView);
            boolean messageScrollable = isScrollable(vMessageScrollView);
            boolean scrollable = listViewScrollable || messageScrollable || customViewNoScrollViewScrollable;
            modifyButtonsBasedOnScrollableContent(scrollable);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        for (ISimpleDialogCancelListener listener : getCancelListeners()) {
            listener.onCancelled(mRequestCode);
        }
    }

    /**
     * Get dialog cancel listeners.
     * There might be more than one cancel listener.
     *
     * @return Dialog cancel listeners
     * @since 2.1.0
     */
    protected List<ISimpleDialogCancelListener> getCancelListeners() {
        return getDialogListeners(ISimpleDialogCancelListener.class);
    }

    /**
     * Utility method for acquiring all listeners of some type for current instance of DialogFragment
     *
     * @param listenerInterface Interface of the desired listeners
     * @return Unmodifiable list of listeners
     * @since 2.1.0
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> getDialogListeners(Class<T> listenerInterface) {
        final Fragment targetFragment = getTargetFragment();
        List<T> listeners = new ArrayList<T>(2);
        if (targetFragment != null && listenerInterface.isAssignableFrom(targetFragment.getClass())) {
            listeners.add((T) targetFragment);
        }
        if (getActivity() != null && listenerInterface.isAssignableFrom(getActivity().getClass())) {
            listeners.add((T) getActivity());
        }
        return Collections.unmodifiableList(listeners);
    }

    /**
     * Button divider should be shown only if the content is scrollable.
     */
    private void modifyButtonsBasedOnScrollableContent(boolean scrollable) {
        if (getView() == null) {
            return;
        }
        View vButtonDivider = getView().findViewById(R.id.sdl_button_divider);
        View vButtonsBottomSpace = getView().findViewById(R.id.sdl_buttons_bottom_space);
        View vDefaultButtons = getView().findViewById(R.id.sdl_buttons_default);
        View vStackedButtons = getView().findViewById(R.id.sdl_buttons_stacked);
        if (vDefaultButtons.getVisibility() == View.GONE && vStackedButtons.getVisibility() == View.GONE) {
            // no buttons
            vButtonDivider.setVisibility(View.GONE);
            vButtonsBottomSpace.setVisibility(View.GONE);
        } else if (scrollable) {
            vButtonDivider.setVisibility(View.VISIBLE);
            vButtonsBottomSpace.setVisibility(View.GONE);
        } else {
            vButtonDivider.setVisibility(View.GONE);
            vButtonsBottomSpace.setVisibility(View.VISIBLE);
        }
    }

    private boolean isScrollable(ViewGroup listView) {
        int totalHeight = 0;
        for (int i = 0; i < listView.getChildCount(); i++) {
            totalHeight += listView.getChildAt(i).getMeasuredHeight();
        }
        return listView.getMeasuredHeight() < totalHeight;
    }

    /**
     * This method resolves the current theme declared in the manifest
     */
    private void resolveTheme() {
        try {
            TypedValue val = new TypedValue();

            //Reading attr value from current theme
            getActivity().getTheme().resolveAttribute(R.attr.isLightTheme, val, true);

            //Passing the resource ID to TypedArray to get the attribute value
            TypedArray arr = getActivity().obtainStyledAttributes(val.data, new int[]{R.attr.isLightTheme});
            darkTheme = !arr.getBoolean(0, false);
            arr.recycle();
        } catch (RuntimeException e) {
            //Resource not found , so sticking to light theme
            darkTheme = false;
        }
    }

    /**
     * Custom dialog builder
     */
    protected static class Builder {

        private final Context mContext;

        private final ViewGroup mContainer;

        private final LayoutInflater mInflater;

        private CharSequence mTitle = null;

        private CharSequence mPositiveButtonText;

        private View.OnClickListener mPositiveButtonListener;

        private CharSequence mNegativeButtonText;

        private View.OnClickListener mNegativeButtonListener;

        private CharSequence mNeutralButtonText;

        private View.OnClickListener mNeutralButtonListener;

        private CharSequence mMessage;

        private View mCustomView;

        private ListAdapter mListAdapter;

        private int mListCheckedItemIdx;

        private int mChoiceMode;

        private int[] mListCheckedItemMultipleIds;

        private AdapterView.OnItemClickListener mOnItemClickListener;

        public Builder(Context context, LayoutInflater inflater, ViewGroup container) {
            this.mContext = context;
            this.mContainer = container;
            this.mInflater = inflater;
        }

        public LayoutInflater getLayoutInflater() {
            return mInflater;
        }

        public Builder setTitle(int titleId) {
            this.mTitle = mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setPositiveButton(int textId, final View.OnClickListener listener) {
            mPositiveButtonText = mContext.getText(textId);
            mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, final View.OnClickListener listener) {
            mNegativeButtonText = mContext.getText(textId);
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(int textId, final View.OnClickListener listener) {
            mNeutralButtonText = mContext.getText(textId);
            mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, final View.OnClickListener listener) {
            mNeutralButtonText = text;
            mNeutralButtonListener = listener;
            return this;
        }

        public Builder setMessage(int messageId) {
            mMessage = mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder setItems(ListAdapter listAdapter, int[] checkedItemIds, int choiceMode, final AdapterView.OnItemClickListener listener) {
            mListAdapter = listAdapter;
            mListCheckedItemMultipleIds = checkedItemIds;
            mOnItemClickListener = listener;
            mChoiceMode = choiceMode;
            mListCheckedItemIdx = -1;
            return this;
        }

        /**
         * Set list
         *
         * @param checkedItemIdx Item check by default, -1 if no item should be checked
         */
        public Builder setItems(ListAdapter listAdapter, int checkedItemIdx,
                                final AdapterView.OnItemClickListener listener) {
            mListAdapter = listAdapter;
            mOnItemClickListener = listener;
            mListCheckedItemIdx = checkedItemIdx;
            mChoiceMode = AbsListView.CHOICE_MODE_NONE;
            return this;
        }

        public Builder setView(View view) {
            mCustomView = view;
            return this;
        }

        public View create() {

            LinearLayout content = (LinearLayout) mInflater.inflate(R.layout.sdl_dialog, mContainer, false);
            TextView vTitle = (TextView) content.findViewById(R.id.sdl_title);
            TextView vMessage = (TextView) content.findViewById(R.id.sdl_message);
            FrameLayout vCustomView = (FrameLayout) content.findViewById(R.id.sdl_custom);
            Button vPositiveButton = (Button) content.findViewById(R.id.sdl_button_positive);
            Button vNegativeButton = (Button) content.findViewById(R.id.sdl_button_negative);
            Button vNeutralButton = (Button) content.findViewById(R.id.sdl_button_neutral);
            Button vPositiveButtonStacked = (Button) content.findViewById(R.id.sdl_button_positive_stacked);
            Button vNegativeButtonStacked = (Button) content.findViewById(R.id.sdl_button_negative_stacked);
            Button vNeutralButtonStacked = (Button) content.findViewById(R.id.sdl_button_neutral_stacked);
            View vButtonsDefault = content.findViewById(R.id.sdl_buttons_default);
            View vButtonsStacked = content.findViewById(R.id.sdl_buttons_stacked);
            ListView vList = (ListView) content.findViewById(R.id.sdl_list);

            Typeface regularFont = TypefaceHelper.get(mContext, "Roboto-Regular");
            Typeface mediumFont = TypefaceHelper.get(mContext, "Roboto-Medium");

            set(vTitle, mTitle, mediumFont);
            set(vMessage, mMessage, regularFont);
            setPaddingOfTitleAndMessage(vTitle, vMessage);

            if (mCustomView != null) {
                vCustomView.addView(mCustomView);
            }
            if (mListAdapter != null) {
                vList.setAdapter(mListAdapter);
                vList.setOnItemClickListener(mOnItemClickListener);
                if (mListCheckedItemIdx != -1) {
                    vList.setSelection(mListCheckedItemIdx);
                }
                if (mListCheckedItemMultipleIds != null) {
                    vList.setChoiceMode(mChoiceMode);
                    for (int i : mListCheckedItemMultipleIds) {
                        vList.setItemChecked(i, true);
                    }
                }
            }

            if (shouldStackButtons()) {
                set(vPositiveButtonStacked, mPositiveButtonText, mediumFont, mPositiveButtonListener);
                set(vNegativeButtonStacked, mNegativeButtonText, mediumFont, mNegativeButtonListener);
                set(vNeutralButtonStacked, mNeutralButtonText, mediumFont, mNeutralButtonListener);
                vButtonsDefault.setVisibility(View.GONE);
                vButtonsStacked.setVisibility(View.VISIBLE);
            } else {
                set(vPositiveButton, mPositiveButtonText, mediumFont, mPositiveButtonListener);
                set(vNegativeButton, mNegativeButtonText, mediumFont, mNegativeButtonListener);
                set(vNeutralButton, mNeutralButtonText, mediumFont, mNeutralButtonListener);
                vButtonsDefault.setVisibility(View.VISIBLE);
                vButtonsStacked.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(mPositiveButtonText) && TextUtils.isEmpty(mNegativeButtonText) && TextUtils.isEmpty
                    (mNeutralButtonText)) {
                vButtonsDefault.setVisibility(View.GONE);
            }

            return content;
        }

        /**
         * Padding is different if there is only title, only message or both.
         */
        private void setPaddingOfTitleAndMessage(TextView vTitle, TextView vMessage) {
            int grid6 = mContext.getResources().getDimensionPixelSize(R.dimen.grid_6);
            int grid4 = mContext.getResources().getDimensionPixelSize(R.dimen.grid_4);
            if (!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mMessage)) {
                vTitle.setPadding(grid6, grid6, grid6, grid4);
                vMessage.setPadding(grid6, 0, grid6, grid4);
            } else if (TextUtils.isEmpty(mTitle)) {
                vMessage.setPadding(grid6, grid4, grid6, grid4);
            } else if (TextUtils.isEmpty(mMessage)) {
                vTitle.setPadding(grid6, grid6, grid6, grid4);
            }
        }

        private boolean shouldStackButtons() {
            return shouldStackButton(mPositiveButtonText) || shouldStackButton(mNegativeButtonText)
                    || shouldStackButton(mNeutralButtonText);
        }

        private boolean shouldStackButton(CharSequence text) {
            final int MAX_BUTTON_CHARS = 12; // based on observation, could be done better with measuring widths
            return text != null && text.length() > MAX_BUTTON_CHARS;
        }

        private void set(Button button, CharSequence text, Typeface font, View.OnClickListener listener) {
            set(button, text, font);
            if (listener != null) {
                button.setOnClickListener(listener);
            }
        }

        private void set(TextView textView, CharSequence text, Typeface font) {
            if (text != null) {
                textView.setText(text);
                textView.setTypeface(font);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }
}