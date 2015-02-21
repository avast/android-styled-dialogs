/**
 * Copyright(c) 2015 DRAWNZER.ORG PROJECTS -> ANURAG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *                             anuraxsharma1512@gmail.com
 *
 */

package com.avast.android.dialogs.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;

import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.IEditTextGetTextListener;

import java.util.List;

/**
 * This class shows the DialogFragment having a editText Widget in dialog box.
 * <p/>
 * One must call setHint() method,other wise it will be simple dialog box.If you don't want
 * any hint then simple pass empty string to setHint() method.
 * <p/>
 * Created by Anurag on 20/2/15.
 */
public class EditTextDialogFragment extends BaseDialogFragment {


    protected final static String ARG_TITLE = "title";
    protected final static String ARG_POSITIVE_BUTTON = "positive_button";

    //hint in the EditText widget in dialog fragment
    protected final static String ARG_HINT = "hint";
    protected final static String ARG_MESSAGE = "message";

    public static SimpleEditTextDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleEditTextDialogBuilder(context, fragmentManager);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException(
                    "use EditTextDialogFragment to construct this dialog");
        }
    }

    @Override
    protected Builder build(final Builder builder) {
        String title = getTitle();
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
                    for (IEditTextGetTextListener listener : getEditTextDialogListeners()) {
                        listener.onGetTextListener(builder.getEditText(), mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        final String hint = getHint();
        if (!TextUtils.isEmpty(hint)) {
            builder.setHint(hint);
        }

        return builder;
    }


    protected CharSequence getMessage() {
        return getArguments().getCharSequence(ARG_MESSAGE);
    }

    protected String getTitle() {
        return getArguments().getString(ARG_TITLE);
    }

    protected String getHint() {
        return getArguments().getString(ARG_HINT);
    }

    protected String getPositiveButtonText() {
        return getArguments().getString(ARG_POSITIVE_BUTTON);
    }

    /**
     * Get positive button dialog listeners.
     * There might be more than one listener.
     *
     * @return Dialog listeners
     * @since 2.1.0
     */
    protected List<IEditTextGetTextListener> getEditTextDialogListeners() {
        return getDialogListeners(IEditTextGetTextListener.class);
    }

    public static class SimpleEditTextDialogBuilder extends BaseDialogBuilder<SimpleEditTextDialogBuilder> {

        private String title;
        private String message;
        private String hint;
        private String positiveButtonText;

        public SimpleEditTextDialogBuilder(Context context, FragmentManager manager) {
            super(context, manager, EditTextDialogFragment.class);
        }

        @Override
        protected SimpleEditTextDialogBuilder self() {
            return this;
        }

        private Resources getResources() {
            return mContext.getResources();
        }

        public SimpleEditTextDialogBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public SimpleEditTextDialogBuilder setTitle(int titleResId) {
            this.title = getResources().getString(titleResId);
            return this;
        }

        public SimpleEditTextDialogBuilder setMessage(String msg) {
            this.message = msg;
            return this;
        }

        public SimpleEditTextDialogBuilder setMessage(int msgResId) {
            this.message = getResources().getString(msgResId);
            return this;
        }

        public SimpleEditTextDialogBuilder setHint(String editHint) {
            this.hint = editHint;
            return this;
        }

        public SimpleEditTextDialogBuilder setHint(int hintResId) {
            this.hint = getResources().getString(hintResId);
            return this;
        }

        public SimpleEditTextDialogBuilder setPositiveButtonText(String text) {
            this.positiveButtonText = text;
            return this;
        }

        public SimpleEditTextDialogBuilder setPositiveButtonText(int resId) {
            this.positiveButtonText = getResources().getString(resId);
            return this;
        }

        public EditTextDialogFragment show() {
            return (EditTextDialogFragment) super.show();
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle args = new Bundle();
            args.putCharSequence(EditTextDialogFragment.ARG_MESSAGE, message);
            args.putString(EditTextDialogFragment.ARG_TITLE, title);
            args.putString(EditTextDialogFragment.ARG_POSITIVE_BUTTON, positiveButtonText);
            args.putString(EditTextDialogFragment.ARG_HINT, hint);
            return args;
        }
    }
}
