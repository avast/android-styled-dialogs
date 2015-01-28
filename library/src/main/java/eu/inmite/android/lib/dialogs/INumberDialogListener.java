package eu.inmite.android.lib.dialogs;

/**
 * Implement this interface in Activity or Fragment to react to positive and negative buttons.
 *
 * @author Lukas Prokop (prokop@avast.com)
 */
public interface INumberDialogListener {

    /**
     * Callback fired on positive button clicked
     *
     * @param requestCode identifier of caller
     * @param number from number picker
     */
    public void onPositiveButtonClicked(int requestCode, int number);

    /**
     * Callback fired on positive button clicked
     *
     * @param requestCode identifier of caller
     * @param number from number picker
     */
    public void onNegativeButtonClicked(int requestCode, int number);
}