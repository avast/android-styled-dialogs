package com.avast.android.dialogs.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;

/**
 * Dialog with a list of options. Implement {@link com.avast.android.dialogs.iface.IListDialogListener} to handle selection.
 */
public class ListDialogFragment extends BaseDialogFragment {

    protected final static String ARG_ITEMS = "items";
    protected final static String ARG_TITLE = "title";
    protected final static String ARG_CANCEL_BUTTON = "cancel_button";

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
            args.putString(ARG_TITLE, title);
            args.putString(ARG_CANCEL_BUTTON, cancelButtonText);
            args.putStringArray(ARG_ITEMS, items);

            return args;
        }
    }

    @Override
    protected Builder build(Builder builder) {
        final String title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (!TextUtils.isEmpty(getCancelButtonText())) {
            builder.setPositiveButton(getCancelButtonText(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (ISimpleDialogCancelListener listener : getCancelListeners()) {
                        listener.onCancelled(mRequestCode);
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
                    for (IListDialogListener listener : getDialogListeners()) {
                        listener.onListItemSelected(getItems()[position], position, mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        return builder;
    }

    /** Get dialog listeners.
     *  There might be more than one listener.
     *
     * @return Dialog listeners
     * @since 2.1.0
     */
    private IListDialogListener[] getDialogListeners() {
        final Fragment targetFragment = getTargetFragment();
        List<IListDialogListener> listeners = new ArrayList<IListDialogListener>();
        if (targetFragment != null && targetFragment instanceof IListDialogListener) {
            listeners.add((IListDialogListener) targetFragment);
        }
        if (getActivity() instanceof IListDialogListener) {
            listeners.add((IListDialogListener) getActivity());
        }
        return listeners.toArray(new IListDialogListener[listeners.size()]);
    }

    private String getTitle() {
        return getArguments().getString(ARG_TITLE);
    }

    private String[] getItems() {
        return getArguments().getStringArray(ARG_ITEMS);
    }

    private String getCancelButtonText() {
        return getArguments().getString(ARG_CANCEL_BUTTON);
    }

}
