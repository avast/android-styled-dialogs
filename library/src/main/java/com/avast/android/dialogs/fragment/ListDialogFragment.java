package com.avast.android.dialogs.fragment;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.avast.android.dialogs.R;
import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.avast.android.dialogs.iface.IMultiChoiceListDialogListener;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.util.SparseBooleanArrayParcelable;

/**
 * Dialog with a list of options.
 * <p/>
 * Implement {@link com.avast.android.dialogs.iface.IListDialogListener} to handle selection of single and no choice
 * modes. Implement {@link com.avast.android.dialogs.iface.IMultiChoiceListDialogListener} to handle selection of
 * multi choice.
 */
public class ListDialogFragment extends BaseDialogFragment {


    protected static final String ARG_ITEMS = "items";
    protected static final String ARG_CHECKED_ITEMS = "checkedItems";
    protected static final String ARG_MODE = "choiceMode";
    protected final static String ARG_TITLE = "title";
    protected final static String ARG_POSITIVE_BUTTON = "positive_button";
    protected final static String ARG_NEGATIVE_BUTTON = "negative_button";

    public static SimpleListDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleListDialogBuilder(context, fragmentManager);
    }

    private static int[] asIntArray(SparseBooleanArray checkedItems) {
        int checked = 0;
        // compute number of items
        for (int i = 0; i < checkedItems.size(); i++) {
            int key = checkedItems.keyAt(i);
            if (checkedItems.get(key)) {
                ++checked;
            }
        }

        int[] array = new int[checked];
        //add indexes that are checked
        for (int i = 0, j = 0; i < checkedItems.size(); i++) {
            int key = checkedItems.keyAt(i);
            if (checkedItems.get(key)) {
                array[j++] = key;
            }
        }
        Arrays.sort(array);
        return array;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException(
                "use SimpleListDialogBuilder to construct this dialog");
        }
    }

    private ListAdapter prepareAdapter(final int itemLayoutId) {
        return new ArrayAdapter<Object>(getActivity(),
            itemLayoutId,
            R.id.sdl_text,
            getItems()) {

            /**
             * Overriding default implementation because it ignores current light/dark theme.
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
                }
                TextView t = (TextView)convertView.findViewById(R.id.sdl_text);
                if (t != null) {
                    t.setText((CharSequence)getItem(position));
                }
                return convertView;
            }
        };
    }

    private void buildMultiChoice(Builder builder) {
        builder.setItems(
            prepareAdapter(R.layout.sdl_list_item_multichoice),
            asIntArray(getCheckedItems()), AbsListView.CHOICE_MODE_MULTIPLE,
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SparseBooleanArray checkedPositions = ((ListView)parent).getCheckedItemPositions();
                    setCheckedItems(new SparseBooleanArrayParcelable(checkedPositions));
                }
            });
    }

    private void buildSingleChoice(Builder builder) {
        builder.setItems(
            prepareAdapter(R.layout.sdl_list_item_singlechoice),
            asIntArray(getCheckedItems()),
            AbsListView.CHOICE_MODE_SINGLE, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SparseBooleanArray checkedPositions = ((ListView)parent).getCheckedItemPositions();
                    setCheckedItems(new SparseBooleanArrayParcelable(checkedPositions));
                }
            });
    }

    private void buildNormalChoice(Builder builder) {
        builder.setItems(
            prepareAdapter(R.layout.sdl_list_item), -1,
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (IListDialogListener listener : getSingleDialogListeners()) {
                        listener.onListItemSelected(getItems()[position], position, mRequestCode);
                    }
                    dismiss();
                }
            });
    }

    @Override
    protected Builder build(Builder builder) {
        final CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (!TextUtils.isEmpty(getNegativeButtonText())) {
            builder.setNegativeButton(getNegativeButtonText(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (ISimpleDialogCancelListener listener : getCancelListeners()) {
                        listener.onCancelled(mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        //confirm button makes no sense when CHOICE_MODE_NONE
        if (getMode() != AbsListView.CHOICE_MODE_NONE) {
            View.OnClickListener positiveButtonClickListener = null;
            switch (getMode()) {
                case AbsListView.CHOICE_MODE_MULTIPLE:
                    positiveButtonClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // prepare multiple results
                            final int[] checkedPositions = asIntArray(getCheckedItems());
                            final CharSequence[] items = getItems();
                            final CharSequence[] checkedValues = new CharSequence[checkedPositions.length];
                            int i = 0;
                            for (int checkedPosition : checkedPositions) {
                                if (checkedPosition >= 0 && checkedPosition < items.length) {
                                    checkedValues[i++] = items[checkedPosition];
                                }
                            }

                            for (IMultiChoiceListDialogListener listener : getMutlipleDialogListeners()) {
                                listener.onListItemsSelected(checkedValues, checkedPositions, mRequestCode);
                            }
                            dismiss();
                        }
                    };
                    break;
                case AbsListView.CHOICE_MODE_SINGLE:
                    positiveButtonClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // prepare single result
                            int selectedPosition = -1;
                            final int[] checkedPositions = asIntArray(getCheckedItems());
                            final CharSequence[] items = getItems();
                            for (int i : checkedPositions) {
                                if (i >= 0 && i < items.length) {
                                    //1st valid value
                                    selectedPosition = i;
                                    break;
                                }
                            }

                            // either item is selected or dialog is cancelled
                            if (selectedPosition != -1) {
                                for (IListDialogListener listener : getSingleDialogListeners()) {
                                    listener.onListItemSelected(items[selectedPosition], selectedPosition, mRequestCode);
                                }
                            } else {
                                for (ISimpleDialogCancelListener listener : getCancelListeners()) {
                                    listener.onCancelled(mRequestCode);
                                }
                            }
                            dismiss();
                        }
                    };
                    break;
            }

            CharSequence positiveButton = getPositiveButtonText();
            if (TextUtils.isEmpty(getPositiveButtonText())) {
                //we always need confirm button when CHOICE_MODE_SINGLE or CHOICE_MODE_MULTIPLE
                positiveButton = getString(android.R.string.ok);
            }
            builder.setPositiveButton(positiveButton, positiveButtonClickListener);
        }

        // prepare list and its item click listener
        final CharSequence[] items = getItems();
        if (items != null && items.length > 0) {
            @ChoiceMode
            final int mode = getMode();
            switch (mode) {
                case AbsListView.CHOICE_MODE_MULTIPLE:
                    buildMultiChoice(builder);
                    break;
                case AbsListView.CHOICE_MODE_SINGLE:
                    buildSingleChoice(builder);
                    break;
                case AbsListView.CHOICE_MODE_NONE:
                    buildNormalChoice(builder);
                    break;
            }
        }

        return builder;
    }

    /**
     * Get dialog listeners.
     * There might be more than one listener.
     *
     * @return Dialog listeners
     * @since 2.1.0
     */
    private List<IListDialogListener> getSingleDialogListeners() {
        return getDialogListeners(IListDialogListener.class);
    }

    /**
     * Get dialog listeners.
     * There might be more than one listener.
     *
     * @return Dialog listeners
     * @since 2.1.0
     */
    private List<IMultiChoiceListDialogListener> getMutlipleDialogListeners() {
        return getDialogListeners(IMultiChoiceListDialogListener.class);
    }

    private CharSequence getTitle() {
        return getArguments().getCharSequence(ARG_TITLE);
    }

    @SuppressWarnings("ResourceType")
    @ChoiceMode
    private int getMode() {
        return getArguments().getInt(ARG_MODE);
    }

    private CharSequence[] getItems() {
        return getArguments().getCharSequenceArray(ARG_ITEMS);
    }

    @NonNull
    private SparseBooleanArrayParcelable getCheckedItems() {
        SparseBooleanArrayParcelable items = getArguments().getParcelable(ARG_CHECKED_ITEMS);
        if (items == null) {
            items = new SparseBooleanArrayParcelable();
        }
        return items;
    }

    private void setCheckedItems(SparseBooleanArrayParcelable checkedItems) {
        getArguments().putParcelable(ARG_CHECKED_ITEMS, checkedItems);
    }

    private CharSequence getPositiveButtonText() {
        return getArguments().getCharSequence(ARG_POSITIVE_BUTTON);
    }

    private CharSequence getNegativeButtonText() {
        return getArguments().getCharSequence(ARG_NEGATIVE_BUTTON);
    }

    @IntDef({AbsListView.CHOICE_MODE_MULTIPLE, AbsListView.CHOICE_MODE_SINGLE, AbsListView.CHOICE_MODE_NONE})
    public @interface ChoiceMode {
    }

    public static class SimpleListDialogBuilder extends BaseDialogBuilder<SimpleListDialogBuilder> {

        private CharSequence title;

        private CharSequence[] items;

        @ChoiceMode
        private int mode;
        private int[] checkedItems;

        private CharSequence cancelButtonText;
        private CharSequence confirmButtonText;


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

        public SimpleListDialogBuilder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public SimpleListDialogBuilder setTitle(int titleResID) {
            this.title = getResources().getString(titleResID);
            return this;
        }


        /**
         * Positions of item that should be pre-selected
         * Valid for setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE)
         *
         * @param positions list of item positions to mark as checked
         * @return builder
         */
        public SimpleListDialogBuilder setCheckedItems(int[] positions) {
            this.checkedItems = positions;
            return this;
        }

        /**
         * Position of item that should be pre-selected
         * Valid for setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
         *
         * @param position item position to mark as selected
         * @return builder
         */
        public SimpleListDialogBuilder setSelectedItem(int position) {
            this.checkedItems = new int[]{position};
            return this;
        }

        public SimpleListDialogBuilder setChoiceMode(@ChoiceMode int choiceMode) {
            this.mode = choiceMode;
            return this;
        }

        public SimpleListDialogBuilder setItems(CharSequence[] items) {
            this.items = items;
            return this;
        }

        public SimpleListDialogBuilder setItems(int itemsArrayResID) {
            this.items = getResources().getStringArray(itemsArrayResID);
            return this;
        }

        public SimpleListDialogBuilder setConfirmButtonText(CharSequence text) {
            this.confirmButtonText = text;
            return this;
        }

        public SimpleListDialogBuilder setConfirmButtonText(int confirmBttTextResID) {
            this.confirmButtonText = getResources().getString(confirmBttTextResID);
            return this;
        }

        public SimpleListDialogBuilder setCancelButtonText(CharSequence text) {
            this.cancelButtonText = text;
            return this;
        }

        public SimpleListDialogBuilder setCancelButtonText(int cancelBttTextResID) {
            this.cancelButtonText = getResources().getString(cancelBttTextResID);
            return this;
        }

        @Override
        public ListDialogFragment show() {
            return (ListDialogFragment)super.show();
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle args = new Bundle();
            args.putCharSequence(ARG_TITLE, title);
            args.putCharSequence(ARG_POSITIVE_BUTTON, confirmButtonText);
            args.putCharSequence(ARG_NEGATIVE_BUTTON, cancelButtonText);

            args.putCharSequenceArray(ARG_ITEMS, items);

            SparseBooleanArrayParcelable sparseArray = new SparseBooleanArrayParcelable();
            for (int index = 0; checkedItems != null && index < checkedItems.length; index++) {
                sparseArray.put(checkedItems[index], true);
            }
            args.putParcelable(ARG_CHECKED_ITEMS, sparseArray);
            args.putInt(ARG_MODE, mode);


            return args;
        }
    }

}
