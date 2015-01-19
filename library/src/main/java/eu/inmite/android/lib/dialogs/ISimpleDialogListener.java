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

/**
 * Implement this interface in Activity or Fragment to react to positive, negative and neutral
 * buttons.
 *
 * @author David Vávra (david@inmite.eu)
 */
public interface ISimpleDialogListener {
	/**
	 * Callback for click on positive button.
	 *
	 * @param requestCode enables to identify dialog
	 */
	public void onPositiveButtonClicked(int requestCode);

	/**
	 * Callback for click on negative button.
	 *
	 * @param requestCode enables to identify dialog
	 */
	public void onNegativeButtonClicked(int requestCode);

	/**
	 * Callback for click on neutral button.
	 *
	 * @param requestCode enables to identify dialog
	 */
	public void onNeutralButtonClicked(int requestCode);
}
