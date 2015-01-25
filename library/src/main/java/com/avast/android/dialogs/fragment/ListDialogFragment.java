package com.avast.android.dialogs.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.*;
import com.avast.android.dialogs.R;
import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.avast.android.dialogs.iface.IListDialogMultipleListener;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.util.SparseBooleanArrayParcelable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Dialog with a list of options. Implement {@link com.avast.android.dialogs.iface.IListDialogListener} to handle selection.
 */
public class ListDialogFragment extends BaseDialogFragment {


    @IntDef({AbsListView.CHOICE_MODE_MULTIPLE, AbsListView.CHOICE_MODE_SINGLE, AbsListView.CHOICE_MODE_NONE})
    public @interface ChoiceMode {
    }

    private static final String ARG_ITEMS = "items";
    private static final String ARG_CHECKED_ITEMS = "checkedItems";
    private static final String ARG_MODE = "choiceMode";

    protected int mRequestCode;

    public static SimpleListDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
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

        @ChoiceMode
        private int mode;
        private int[] checkedItems;

        private String cancelButtonText;
        private String confirmButtonText;


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

        public SimpleListDialogBuilder setItems(String[] items) {
            this.items = items;
            return this;
        }

        public SimpleListDialogBuilder setItems(int itemsArrayResID) {
            this.items = getResources().getStringArray(itemsArrayResID);
            return this;
        }

        public SimpleListDialogBuilder setConfirmButtonText(String text) {
            this.confirmButtonText = text;
            return this;
        }

        public SimpleListDialogBuilder setConfirmButtonText(int confirmBttTextResID) {
            this.confirmButtonText = getResources().getString(confirmBttTextResID);
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
            args.putString(SimpleDialogFragment.ARG_POSITIVE_BUTTON, confirmButtonText);
            args.putString(SimpleDialogFragment.ARG_NEGATIVE_BUTTON, cancelButtonText);

            args.putStringArray(ARG_ITEMS, items);

            SparseBooleanArrayParcelable sparseArray = new SparseBooleanArrayParcelable();
            for (int index = 0; checkedItems != null && index < checkedItems.length; index++) {
                sparseArray.put(checkedItems[index], true);
            }
            args.putParcelable(ARG_CHECKED_ITEMS, sparseArray);
            args.putInt(ARG_MODE, mode);


            return args;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ISimpleDialogCancelListener onListItemSelectedListener = getDialogListener();
        if (onListItemSelectedListener != null) {
            onListItemSelectedListener.onCancelled(mRequestCode);
        }
    }


    private void buildMultiChoice(Builder builder) {
        final String[] items = getItems();
        ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.select_dialog_multichoice,
                android.R.id.text1,
                items);
        builder.setItems(adapter, asIntArray(getCheckedItems()), AbsListView.CHOICE_MODE_MULTIPLE, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checkedPositions = ((ListView) parent).getCheckedItemPositions();
                setCheckedItems(new SparseBooleanArrayParcelable(checkedPositions));
            }
        });
    }


    private void buildSingleChoice(Builder builder) {
        final String[] items = getItems();
        ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.select_dialog_singlechoice,
                android.R.id.text1,
                items);
        builder.setItems(adapter, asIntArray(getCheckedItems()), AbsListView.CHOICE_MODE_SINGLE, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checkedPositions = ((ListView) parent).getCheckedItemPositions();
                setCheckedItems(new SparseBooleanArrayParcelable(checkedPositions));
            }
        });
    }


    private void buildNormalChoice(Builder builder) {
        final String[] items = getItems();
        ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                R.layout.sdl_list_item,
                R.id.sdl_text,
                items);

        builder.setItems(adapter, -1, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ISimpleDialogCancelListener listener = getDialogListener();
                if (listener != null && listener instanceof IListDialogListener) {
                    ((IListDialogListener) listener).onListItemSelected(getItems()[position], position, mRequestCode);
                }
                dismiss();
            }
        });
    }

    @Override
    protected Builder build(Builder builder) {
        final String title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }


        if (!TextUtils.isEmpty(getNegativeButtonText())) {
            builder.setNegativeButton(getNegativeButtonText(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ISimpleDialogCancelListener listener = getDialogListener();
                    if (listener != null) {
                        listener.onCancelled(mRequestCode);
                    }
                    dismiss();
                }
            });
        }


        //confirm button makes no sense when CHOICE_MODE_NONE
        if (getMode() != AbsListView.CHOICE_MODE_NONE) {
            String positiveButton = getPositiveButtonText();
            if (TextUtils.isEmpty(getPositiveButtonText())) {
                //we always need confirm button when CHOICE_MODE_SINGLE or CHOICE_MODE_MULTIPLE
                positiveButton = getString(android.R.string.ok);
            }
            builder.setPositiveButton(positiveButton, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int[] checkedPositions = asIntArray(getCheckedItems());
                    String[] items = getItems();
                    ISimpleDialogCancelListener listener = getDialogListener();
                    if (getMode() == AbsListView.CHOICE_MODE_MULTIPLE && listener != null && listener instanceof IListDialogMultipleListener) {
                        String[] checkedValues = new String[checkedPositions.length];
                        int i = 0;
                        for (int checkedPosition : checkedPositions) {
                            if (checkedPosition >= 0 && checkedPosition < items.length) {
                                checkedValues[i++] = items[checkedPosition];
                            }
                        }
                        ((IListDialogMultipleListener) listener).onListItemsSelected(checkedValues, checkedPositions, mRequestCode);
                    } else if (getMode() == AbsListView.CHOICE_MODE_SINGLE && listener != null && listener instanceof IListDialogListener) {
                        boolean success = false;
                        for (int i : checkedPositions) {
                            if (i >= 0 && i < items.length) {
                                //1st valid value
                                ((IListDialogListener) listener).onListItemSelected(items[i], i, mRequestCode);
                                success = true;
                                break;
                            }
                        }
                        if (!success) {//no item selected
                            listener.onCancelled(mRequestCode);
                        }
                    }
                    dismiss();
                }
            });
        }


        final String[] items = getItems();
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

    private ISimpleDialogCancelListener getDialogListener() {
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof IListDialogListener || targetFragment instanceof IListDialogMultipleListener) {
                return (ISimpleDialogCancelListener) targetFragment;
            }
        } else {
            if (getActivity() instanceof IListDialogListener || getActivity() instanceof IListDialogMultipleListener) {
                return (ISimpleDialogCancelListener) getActivity();
            }
        }
        return null;
    }

    private String getTitle() {
        return getArguments().getString(SimpleDialogFragment.ARG_TITLE);
    }


    @SuppressWarnings("ResourceType")
    @ChoiceMode
    private int getMode() {
        return getArguments().getInt(ARG_MODE);
    }

    private String[] getItems() {
        return getArguments().getStringArray(ARG_ITEMS);
    }

    private void setCheckedItems(SparseBooleanArrayParcelable checkedItems) {
        getArguments().putParcelable(ARG_CHECKED_ITEMS, checkedItems);
    }

    @NonNull
    private SparseBooleanArrayParcelable getCheckedItems() {
        SparseBooleanArrayParcelable items = getArguments().getParcelable(ARG_CHECKED_ITEMS);
        if (items == null) {
            items = new SparseBooleanArrayParcelable();
        }
        return items;
    }

    private String getPositiveButtonText() {
        return getArguments().getString(SimpleDialogFragment.ARG_POSITIVE_BUTTON);
    }

    private String getNegativeButtonText() {
        return getArguments().getString(SimpleDialogFragment.ARG_NEGATIVE_BUTTON);
    }


    private static int[] asIntArray(SparseBooleanArray checkedItems) {
        ArrayList<Integer> list = new ArrayList<>();
        //add indexes that are checked to a list
        for (int i = 0; i < checkedItems.size(); i++) {
            int key = checkedItems.keyAt(i);
            if (checkedItems.get(key)) {
                list.add(key);
            }
        }
        Collections.sort(list);
        //convert to int array
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

}
