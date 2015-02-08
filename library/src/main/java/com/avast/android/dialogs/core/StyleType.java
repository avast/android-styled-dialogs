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

package com.avast.android.dialogs.core;

/**
 * Created by anurag on 8/2/15.
 *
 * this class responsible for enabling the ark theme for the dialog....
 *
 */
public class StyleType {

    /**
     * if true then use dark theme for dialog interface....
     */
    private static boolean drakThemeInUse;

    /**
     * enables the dark theme
     */
    public static void enableDarkTheme(){
        drakThemeInUse = true;
    }

    /**
     *
     * @return true if dark theme to be used....
     */
    public static boolean isDarkThemeEnabled(){
        return drakThemeInUse;
    }

    /**
     * disables the dark theme for the dialog box....
     */
    public static void disableDarkTheme(){
        drakThemeInUse = false;
    }
}
