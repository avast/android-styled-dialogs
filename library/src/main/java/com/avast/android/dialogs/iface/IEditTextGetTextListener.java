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

package com.avast.android.dialogs.iface;

import android.widget.EditText;

/**
 * This is interface listener class called when positive button from EditTextDialogFragment is pressed.
 * To get the string from the edit text positive button must be included.
 * <p/>
 * Created by Anurag on 2/21/15.
 */
public interface IEditTextGetTextListener {

    /**
     * @param editText    This is the same EditText widget from the dialog fragment,via interface
     *                    you can use the same EditText to get its values
     * @param requestCode
     */
    public void onGetTextListener(EditText editText, int requestCode);
}
