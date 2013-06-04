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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/** Builder to ease the construction of SimpleDialogFragment
 *
 * @author Tomáš Kypta
 * @since 13/05/2013
 */
public class SimpleDialogFragmentBuilder {

	private FragmentActivity mActivity;

	private String mTitle;
	private String mMessage;
	private String mPositiveButtonText;
	private String mNegativeButtonText;
	private boolean mCancelable = true;
	private Fragment mTargetFragment;
	private int mRequestCode;

	public static SimpleDialogFragmentBuilder from(FragmentActivity activity) {
		return new SimpleDialogFragmentBuilder(activity);
	}

	public SimpleDialogFragmentBuilder(FragmentActivity activity) {
		mActivity = activity;
	}


	public SimpleDialogFragmentBuilder setTitle(int titleResourceId) {
		mTitle = mActivity.getString(titleResourceId);
		return this;
	}

	public SimpleDialogFragmentBuilder setTitle(String title) {
		mTitle = title;
		return this;
	}

	public SimpleDialogFragmentBuilder setMessage(int messageResourceId) {
		mMessage = mActivity.getString(messageResourceId);
		return this;
	}

	public SimpleDialogFragmentBuilder setMessage(String message) {
		mMessage = message;
		return this;
	}

	public SimpleDialogFragmentBuilder setPositiveButtonText(int textResourceId) {
		mPositiveButtonText = mActivity.getString(textResourceId);
		return this;
	}

	public SimpleDialogFragmentBuilder setPositiveButtonText(String text) {
		mPositiveButtonText = text;
		return this;
	}

	public SimpleDialogFragmentBuilder setNegativeButtonText(int textResourceId) {
		mNegativeButtonText = mActivity.getString(textResourceId);
		return this;
	}

	public SimpleDialogFragmentBuilder setNegativeButtonText(String text) {
		mNegativeButtonText = text;
		return this;
	}

	public SimpleDialogFragmentBuilder setCancelable(boolean cancelable) {
		mCancelable = cancelable;
		return this;
	}

	public SimpleDialogFragmentBuilder setTargetFragment(Fragment fragment) {
		mTargetFragment = fragment;
		return this;
	}

	public SimpleDialogFragmentBuilder setRequestCode(int requestCode) {
		mRequestCode = requestCode;
		return this;
	}

	@Deprecated
	public void build() {
		SimpleDialogFragment.show(mActivity, mTargetFragment, mRequestCode,
				mMessage, mTitle, mPositiveButtonText, mNegativeButtonText,
				mCancelable);
	}

	public void buildAndShow() {
		SimpleDialogFragment.show(mActivity, mTargetFragment, mRequestCode,
				mMessage, mTitle, mPositiveButtonText, mNegativeButtonText,
				mCancelable);
	}
}
