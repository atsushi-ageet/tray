/*
 * Copyright (C) 2015 grandcentrix GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.grandcentrix.tray.provider;

import net.grandcentrix.tray.R;
import net.grandcentrix.tray.core.TrayLog;
import net.grandcentrix.tray.core.TrayRuntimeException;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

/**
 * Contract defining the data in the {@link TrayContentProvider}. Use {@link TrayProviderHelper} to
 * access them.
 * <p>
 * Created by jannisveerkamp on 17.09.14.
 */
class TrayContract {

    /**
     * default preferences
     */
    public interface Preferences {

        interface Columns extends BaseColumns {

            String ID = BaseColumns._ID;

            String KEY = TrayDBHelper.KEY;

            String VALUE = TrayDBHelper.VALUE;

            String MODULE = TrayDBHelper.MODULE;

            String CREATED = TrayDBHelper.CREATED; // DATE

            String UPDATED = TrayDBHelper.UPDATED; // DATE

            String MIGRATED_KEY = TrayDBHelper.MIGRATED_KEY;
        }

        String BASE_PATH = "preferences";
    }

    /**
     * trays internal preferences to hold things like the version number
     */
    public interface InternalPreferences extends Preferences {

        String BASE_PATH = "internal_preferences";
    }

    @VisibleForTesting
    static String sAuthority;

    @NonNull
    public static Uri generateContentUri(@NonNull final Context context) {
        return generateContentUri(context, Preferences.BASE_PATH);
    }

    @NonNull
    /*package*/ static Uri generateInternalContentUri(@NonNull final Context context) {
        return generateContentUri(context, InternalPreferences.BASE_PATH);
    }

    @NonNull
    private static Uri generateContentUri(@NonNull final Context context, final String basepath) {

        final String authority = getAuthority(context);
        final Uri authorityUri = Uri.parse("content://" + authority);
        //noinspection UnnecessaryLocalVariable
        final Uri contentUri = Uri.withAppendedPath(authorityUri, basepath);
        return contentUri;
    }

    @NonNull
    private static synchronized String getAuthority(@NonNull final Context context) {
        if (sAuthority != null) {
            return sAuthority;
        }

        try {
            ProviderInfo providerInfo = context.getPackageManager().getProviderInfo(new ComponentName(context, TrayContentProvider.class), 0);
            sAuthority = providerInfo.authority;
            TrayLog.v("found authority: " + sAuthority);
            return sAuthority;
        } catch (PackageManager.NameNotFoundException e) {
            // Should never happen. Otherwise we implemented tray in a wrong way!
            throw new TrayRuntimeException("Internal tray error. "
                    + "Could not find the provider authority. "
                    + "Please fill an issue at https://github.com/grandcentrix/tray/issues");
        }
    }
}
