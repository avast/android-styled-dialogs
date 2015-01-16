package com.avast.android.dialogs.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.avast.android.dialogs.R;
import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;

/**
 * Dialog with a list of options. Implement {@link com.avast.android.dialogs.iface.IListDialogListener} to handle selection.
 */
public class ListDialogFragment extends BaseDialogFragment {

    private static String ARG_ITEMS = "items";
    protected int mRequestCode;

    public static SimpleListDialogBuilder createBuilder(Context context,
                                                        FragmentManager fragmentManager) {
        return new SimpleListDialogBuilder(context, fragmentManager);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException(
                    "use SimpleListDialogBuilder to construct this dialog");
        }
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

    public static class SimpleListDialogBuilder extends BaseDialogBuilder<SimpleListDialogBuilder> {

        private String title;

        private String[] items;

        private String cancelButtonText;

        public SimpleListDialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, ListDialogFragment.class);
        }

        @Override
        protected SimpleListDialogBuilder self() {
            return this;
        }

        private Resources getResources() {
            return mContext.getResources();
        }

        public SimpleListDialogBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public SimpleListDialogBuilder setTitle(int titleResID) {
            this.title = getResources().getString(titleResID);
            return this;
        }

        public SimpleListDialogBuilder setItems(String[] items) {
            this.items = items;
            return this;
        }

        public SimpleListDialogBuilder setItems(int itemsArrayResID) {
            this.items = getResources().getStringArray(itemsArrayResID);
            return this;
        }

        public SimpleListDialogBuilder setCancelButtonText(String text) {
            this.cancelButtonText = text;
            return this;
        }

        public SimpleListDialogBuilder setCancelButtonText(int cancelBttTextResID) {
            this.cancelButtonText = getResources().getString(cancelBttTextResID);
            return this;
        }

        @Override
        public ListDialogFragment show() {
            return (ListDialogFragment) super.show();
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle args = new Bundle();
            args.putString(SimpleDialogFragment.ARG_TITLE, title);
            args.putString(SimpleDialogFragment.ARG_POSITIVE_BUTTON, cancelButtonText);
            args.putStringArray(ARG_ITEMS, items);

            return args;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        IListDialogListener onListItemSelectedListener = getDialogListener();
        if (onListItemSelectedListener != null) {
            onListItemSelectedListener.onCancelled(mRequestCode);
        }
    }

    @Override
    protected Builder build(Builder builder) {
        final String title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (!TextUtils.isEmpty(getPositiveButtonText())) {
            builder.setPositiveButton(getPositiveButtonText(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IListDialogListener onListItemSelectedListener = getDialogListener();
                    if (onListItemSelectedListener != null) {
                        onListItemSelectedListener.onCancelled(mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        final String[] items = getItems();
        if (items != null && items.length > 0) {
            ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.sdl_list_item,
                    R.id.sdl_text,
                    items);

            builder.setItems(adapter, 0, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    IListDialogListener onListItemSelectedListener = getDialogListener();
                    if (onListItemSelectedListener != null) {
                        onListItemSelectedListener
                                .onListItemSelected(getItems()[position], position, mRequestCode);
                        dismiss();
                    }
                }
            });
        }

        return builder;
    }

    private IListDialogListener getDialogListener() {
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof IListDialogListener) {
                return (IListDialogListener) targetFragment;
            }
        } else {
            if (getActivity() instanceof IListDialogListener) {
                return (IListDialogListener) getActivity();
            }
        }
        return null;
    }

    private String getTitle() {
        return getArguments().getString(SimpleDialogFragment.ARG_TITLE);
    }

    private String[] getItems() {
        return getArguments().getStringArray(ARG_ITEMS);
    }

    private String getPositiveButtonText() {
        return getArguments().getString(SimpleDialogFragment.ARG_POSITIVE_BUTTON);
    }

}
