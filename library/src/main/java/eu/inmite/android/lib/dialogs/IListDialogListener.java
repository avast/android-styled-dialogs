package eu.inmite.android.lib.dialogs;

/**
 * Interface for ListDialogFragment
 */
public interface IListDialogListener {

    public void onListItemSelected(String value, int number);
    public void onCancelled();
}
