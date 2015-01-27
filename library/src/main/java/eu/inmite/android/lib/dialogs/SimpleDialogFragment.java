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

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;

/**
 * Dialog for displaying simple message, message with title or message with title and two buttons. Implement {@link
 * ISimpleDialogListener} in your Fragment or Activity to rect on positive and negative button clicks. This class can
 * be extended and more parameters can be added in overridden build() method.
 *
 * @author David Vávra (david@inmite.eu)
 */
public class SimpleDialogFragment extends BaseDialogFragment {

    public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleDialogBuilder(context, fragmentManager, SimpleDialogFragment.class);
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
                mRequestCode = args.getInt(BaseDialogFragment.ARG_REQUEST_CODE, 0);
            }
        }
    }

    /**
     * Children can extend this to add more things to base builder.
     */
    @Override
    protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        final String title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        final CharSequence message = getMessage();
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        final String positiveButtonText = getPositiveButtonText();
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ISimpleDialogListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onPositiveButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        final String negativeButtonText = getNegativeButtonText();
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ISimpleDialogListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onNegativeButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        final String neutralButtonText = getNeutralButtonText();
        if (!TextUtils.isEmpty(neutralButtonText)) {
            builder.setNeutralButton(neutralButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ISimpleDialogListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onNeutralButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        return builder;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ISimpleDialogCancelListener listener = getCancelListener();
        if (listener != null) {
            listener.onCancelled(mRequestCode);
        }
    }

    protected ISimpleDialogListener getDialogListener() {
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof ISimpleDialogListener) {
                return (ISimpleDialogListener)targetFragment;
            }
        } else {
            if (getActivity() instanceof ISimpleDialogListener) {
                return (ISimpleDialogListener)getActivity();
            }
        }
        return null;
    }

    protected ISimpleDialogCancelListener getCancelListener() {
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof ISimpleDialogCancelListener) {
                return (ISimpleDialogCancelListener)targetFragment;
            }
        } else {
            if (getActivity() instanceof ISimpleDialogCancelListener) {
                return (ISimpleDialogCancelListener)getActivity();
            }
        }
        return null;
    }

    public static class SimpleDialogBuilder extends BaseDialogBuilder<SimpleDialogBuilder> {

        protected SimpleDialogBuilder(Context context, FragmentManager fragmentManager,
                                      Class<? extends SimpleDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
        }

        @Override
        protected SimpleDialogBuilder self() {
            return this;
        }

        @Override
        protected Bundle prepareArguments() {
            if (mDialogParams.showDefaultButton && mDialogParams.positiveButtonText == null &&
                mDialogParams.negativeButtonText == null) {
                mDialogParams.positiveButtonText = mDialogParams.context.getString(R.string.dialog_close);
            }

            Bundle args = new Bundle();
            args.putCharSequence(BaseDialogFragment.ARG_MESSAGE, mDialogParams.message);
            if (!TextUtils.isEmpty(mDialogParams.title)) {
                args.putString(BaseDialogFragment.ARG_TITLE, mDialogParams.title.toString());
            }
            if (mDialogParams.positiveButtonText != null) {
                args.putString(BaseDialogFragment.ARG_POSITIVE_BUTTON, mDialogParams.positiveButtonText
                    .toString());
            }
            if (mDialogParams.negativeButtonText != null) {
                args.putString(BaseDialogFragment.ARG_NEGATIVE_BUTTON,
                    mDialogParams.negativeButtonText.toString());
            }
            if (mDialogParams.neutralButtonText != null) {
                args.putString(BaseDialogFragment.ARG_NEUTRAL_BUTTON,
                    mDialogParams.neutralButtonText.toString());
            }
            args.putBoolean(BaseDialogFragment.ARG_CANCELABLE_ON_TOUCH_OUTSIDE,
                mDialogParams.cancelableOnTouchOutside);
            return args;
        }
    }
}
