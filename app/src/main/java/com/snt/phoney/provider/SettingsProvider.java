package com.snt.phoney.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.snt.phoney.provider.SettingsState.Setting;

/**
 *
 */
public class SettingsProvider extends ContentProvider {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_LOG = false;

    private static final String LOG_TAG = "SettingsProvider";

    private static final int MUTATION_OPERATION_INSERT = 1;
    private static final int MUTATION_OPERATION_DELETE = 2;
    private static final int MUTATION_OPERATION_UPDATE = 3;

    private static final String[] ALL_COLUMNS = new String[]{
            AppSettings._ID,
            AppSettings.NAME,
            AppSettings.VALUE
    };

    private static Bundle forPair(String key, String value) {
        Bundle b = new Bundle(1);
        b.putString(key, value);
        return b;
    }

    private static final Bundle NULL_SETTING = forPair(AppSettings.VALUE, null);

    private final Object mLock = new Object();

    private SettingsRegistry mSettingsRegistry;

    @Override
    public boolean onCreate() {
        synchronized (mLock) {
            mSettingsRegistry = new SettingsRegistry();
        }
        return true;
    }

    @Override
    public Bundle call(String method, String name, Bundle args) {
        switch (method) {
            case AppSettings.CALL_METHOD_GET: {
                Setting setting = getSetting(name);
                return packageValueForCallResult(setting);
            }
            case AppSettings.CALL_METHOD_PUT: {
                String value = getSettingValue(args);
                insertSetting(name, value);
                break;
            }
            default: {
                Log.w(LOG_TAG, "call() with invalid method: " + method);
            }
            break;
        }

        return null;
    }

    @Override
    public String getType(Uri uri) {
        Arguments args = new Arguments(uri, null, null, true);
        if (TextUtils.isEmpty(args.name)) {
            return "vnd.android.cursor.dir/settings";
        } else {
            return "vnd.android.cursor.item/settings";
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String where, String[] whereArgs, String order) {
        Arguments args = new Arguments(uri, where, whereArgs, true);
        String[] normalizedProjection = normalizeProjection(projection);
        Setting setting = getSetting(args.name);
        return packageSettingForQuery(setting, normalizedProjection);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String name = values.getAsString(AppSettings.NAME);
        if (!isKeyValid(name)) {
            return null;
        }
        String value = values.getAsString(AppSettings.VALUE);
        if (insertSetting(name, value)) {
            return Uri.withAppendedPath(AppSettings.CONTENT_URI, name);
        }
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] allValues) {
        int insertionCount = 0;
        final int valuesCount = allValues.length;
        for (int i = 0; i < valuesCount; i++) {
            ContentValues values = allValues[i];
            if (insert(uri, values) != null) {
                insertionCount++;
            }
        }
        return insertionCount;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        Arguments args = new Arguments(uri, where, whereArgs, false);
        if (!isKeyValid(args.name)) {
            return 0;
        }
        return deleteSetting(args.name) ? 1 : 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        Arguments args = new Arguments(uri, where, whereArgs, false);
        String name = values.getAsString(AppSettings.NAME);
        if (!isKeyValid(name)) {
            return 0;
        }
        String value = values.getAsString(AppSettings.VALUE);
        return updateSetting(args.name, value) ? 1 : 0;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        throw new FileNotFoundException("Direct file access no longer supported; "
                + "ringtone playback is available through android.media.Ringtone");
    }

    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        synchronized (mLock) {
            try {
                dump(pw);
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception:" + e.getMessage());
            }
        }
    }

    private void dump(PrintWriter pw) {
        Cursor globalCursor = getAllSettings(ALL_COLUMNS);
        dumpSettings(globalCursor, pw);
        pw.println();
    }

    private void dumpSettings(Cursor cursor, PrintWriter pw) {
        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }

        final int idColumnIdx = cursor.getColumnIndex(AppSettings._ID);
        final int nameColumnIdx = cursor.getColumnIndex(AppSettings.NAME);
        final int valueColumnIdx = cursor.getColumnIndex(AppSettings.VALUE);

        do {
            pw.append("_id:").append(toDumpString(cursor.getString(idColumnIdx)));
            pw.append(" name:").append(toDumpString(cursor.getString(nameColumnIdx)));
            pw.append(" value:").append(toDumpString(cursor.getString(valueColumnIdx)));
            pw.println();
        } while (cursor.moveToNext());
    }

    private static String toDumpString(String s) {
        if (s != null) {
            return s;
        }
        return "{null}";
    }

    private Cursor getAllSettings(String[] projection) {
        if (DEBUG) {
            Log.v(LOG_TAG, "getAllSettings()");
        }

        synchronized (mLock) {
            // Get the settings.
            SettingsState settingsState = mSettingsRegistry.getSettingsLocked();

            List<String> names = settingsState.getSettingNamesLocked();
            final int nameCount = names.size();
            String[] normalizedProjection = normalizeProjection(projection);
            MatrixCursor result = new MatrixCursor(normalizedProjection, nameCount);
            // Anyone can get the global settings, so no security checks.
            for (int i = 0; i < nameCount; i++) {
                String name = names.get(i);
                Setting setting = settingsState.getSettingLocked(name);
                appendSettingToCursor(result, setting);
            }
            return result;
        }
    }

    private Setting getSetting(String name) {
        if (DEBUG) {
            Log.v(LOG_TAG, "getSetting(" + name + ")");
        }
        // Get the value.
        synchronized (mLock) {
            return mSettingsRegistry.getSettingLocked(name);
        }
    }

    private boolean updateSetting(String name, String value) {
        if (DEBUG) {
            Log.v(LOG_TAG, "updateSetting(" + name + ", " + value + ")");
        }
        return mutateSetting(name, value, MUTATION_OPERATION_UPDATE);
    }

    private boolean insertSetting(String name, String value) {
        if (DEBUG) {
            Log.v(LOG_TAG, "insertSetting(" + name + ", " + value + ")");
        }
        return mutateSetting(name, value, MUTATION_OPERATION_INSERT);
    }

    private boolean deleteSetting(String name) {
        if (DEBUG) {
            Log.v(LOG_TAG, "deleteGlobalSettingLocked(" + name + ")");
        }
        return mutateSetting(name, null, MUTATION_OPERATION_DELETE);
    }

    private boolean mutateSetting(String name, String value, int operation) {
        // Make sure the caller can change the settings - treated as secure.
//        enforceWritePermission(Manifest.permission.WRITE_SECURE_SETTINGS);
        // Perform the mutation.
        synchronized (mLock) {
            switch (operation) {
                case MUTATION_OPERATION_INSERT: {
                    return mSettingsRegistry.insertSettingLocked(name, value, getCallingPackage());
                }
                case MUTATION_OPERATION_DELETE: {
                    return mSettingsRegistry.deleteSettingLocked(name);
                }
                case MUTATION_OPERATION_UPDATE: {
                    return mSettingsRegistry.updateSettingLocked(name, value, getCallingPackage());
                }
            }
        }
        return false;
    }

    private void enforceWritePermission(String permission) {
        if (getContext().checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Permission denial: writing to settings requires:" + permission);
        }
    }

    private static Bundle packageValueForCallResult(Setting setting) {
        if (setting == null) {
            return NULL_SETTING;
        }
        if (DEBUG_LOG) {
            Log.v(LOG_TAG, "packageValueForCallResult, name = " + setting.getName()
                    + ", value : " + setting.getValue());
        }
        return forPair(AppSettings.VALUE, setting.getValue());
    }

    private static String getSettingValue(Bundle args) {
        return (args != null) ? args.getString(AppSettings.VALUE) : null;
    }

    private static MatrixCursor packageSettingForQuery(Setting setting, String[] projection) {
        if (setting == null) {
            return new MatrixCursor(projection, 0);
        }
        MatrixCursor cursor = new MatrixCursor(projection, 1);
        appendSettingToCursor(cursor, setting);
        return cursor;
    }

    private static String[] normalizeProjection(String[] projection) {
        if (projection == null) {
            return ALL_COLUMNS;
        }
        final int columnCount = projection.length;
        for (int i = 0; i < columnCount; i++) {
            String column = projection[i];
            if (!ArrayUtils.contains(ALL_COLUMNS, column)) {
                throw new IllegalArgumentException("Invalid column: " + column);
            }
        }
        return projection;
    }

    private static void appendSettingToCursor(MatrixCursor cursor, Setting setting) {
        final int columnCount = cursor.getColumnCount();
        String[] values = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            String column = cursor.getColumnName(i);
            switch (column) {
                case AppSettings._ID: {
                    values[i] = setting.getId();
                }
                break;
                case AppSettings.NAME: {
                    values[i] = setting.getName();
                }
                break;
                case AppSettings.VALUE: {
                    values[i] = setting.getValue();
                }
                break;
            }
        }
        cursor.addRow(values);
    }

    private static boolean isKeyValid(String key) {
        return !(TextUtils.isEmpty(key) || SettingsState.isBinary(key));
    }

    private static final class Arguments {
        private static final Pattern WHERE_PATTERN_WITH_PARAM_NO_BRACKETS = Pattern.compile("[\\s]*name[\\s]*=[\\s]*\\?[\\s]*");

        private static final Pattern WHERE_PATTERN_WITH_PARAM_IN_BRACKETS = Pattern.compile("[\\s]*\\([\\s]*name[\\s]*=[\\s]*\\?[\\s]*\\)[\\s]*");

        private static final Pattern WHERE_PATTERN_NO_PARAM_IN_BRACKETS = Pattern.compile("[\\s]*\\([\\s]*name[\\s]*=[\\s]*['\"].*['\"][\\s]*\\)[\\s]*");

        private static final Pattern WHERE_PATTERN_NO_PARAM_NO_BRACKETS = Pattern.compile("[\\s]*name[\\s]*=[\\s]*['\"].*['\"][\\s]*");

        public final String name;

        public Arguments(Uri uri, String where, String[] whereArgs, boolean supportAll) {
            final int segmentSize = uri.getPathSegments().size();
            switch (segmentSize) {
                case 1: {
                    if (where != null
                            && (WHERE_PATTERN_WITH_PARAM_NO_BRACKETS.matcher(where).matches()
                            || WHERE_PATTERN_WITH_PARAM_IN_BRACKETS.matcher(where).matches())
                            && whereArgs.length == 1) {
                        name = whereArgs[0];
                        return;
                    } else if (where != null
                            && (WHERE_PATTERN_NO_PARAM_NO_BRACKETS.matcher(where).matches()
                            || WHERE_PATTERN_NO_PARAM_IN_BRACKETS.matcher(where).matches())) {
                        final int startIndex = Math.max(where.indexOf("'"), where.indexOf("\"")) + 1;
                        final int endIndex = Math.max(where.lastIndexOf("'"), where.lastIndexOf("\""));
                        name = where.substring(startIndex, endIndex);
                        return;
                    } else if (supportAll && where == null && whereArgs == null) {
                        name = null;
                        return;
                    }
                }
                break;
                case 2: {
                    if (where == null && whereArgs == null) {
                        name = uri.getPathSegments().get(1);
                        return;
                    }
                }
                break;
            }

            String message = String.format("Supported SQL:\n"
                            + "  uri content://some_table/some_property with null where and where args\n"
                            + "  uri content://some_table with query name=? and single name as arg\n"
                            + "  uri content://some_table with query name=some_name and null args\n"
                            + "  but got - uri:%1s, where:%2s whereArgs:%3s", uri, where,
                    Arrays.toString(whereArgs));
            throw new IllegalArgumentException(message);
        }
    }

    final class SettingsRegistry {

        private static final String TAG = "SettingsRegistry";

        private static final String SETTINGS_FILE = "settings.xml";

        private SettingsState mSettingsState;

        private final Handler mHandler;

        public SettingsRegistry() {
            mHandler = new MyHandler(getContext().getMainLooper());
        }

        public List<String> getSettingsNamesLocked() {
            SettingsState settingsState = peekSettingsStateLocked();
            return settingsState.getSettingNamesLocked();
        }

        public SettingsState getSettingsLocked() {
            return peekSettingsStateLocked();
        }

        public void ensureSettingsLocked() {
            // Ensure global settings loaded if owner.
            ensureSettingsStateLocked();
            // Upgrade the settings to the latest version.
            UpgradeController upgrader = new UpgradeController();
            upgrader.upgradeIfNeededLocked();
        }

        private void ensureSettingsStateLocked() {
            if (mSettingsState == null) {
                final int maxBytesPerPackage = getMaxBytes();
                mSettingsState = new SettingsState(mLock, getSettingsFile(), maxBytesPerPackage);
            }
        }

        public boolean insertSettingLocked(String name, String value, String packageName) {
            SettingsState settingsState = peekSettingsStateLocked();
            final boolean success = settingsState.insertSettingLocked(name, value, packageName);
            if (success) {
                notifyForSettingsChange(name);
            }
            return success;
        }

        public boolean deleteSettingLocked(String name) {
            SettingsState settingsState = peekSettingsStateLocked();
            final boolean success = settingsState.deleteSettingLocked(name);
            if (success) {
                notifyForSettingsChange(name);
            }
            return success;
        }

        public Setting getSettingLocked(String name) {
            SettingsState settingsState = peekSettingsStateLocked();
            return settingsState.getSettingLocked(name);
        }

        public boolean updateSettingLocked(String name, String value, String packageName) {
            SettingsState settingsState = peekSettingsStateLocked();
            final boolean success = settingsState.updateSettingLocked(name, value, packageName);
            if (success) {
                notifyForSettingsChange(name);
            }
            return success;
        }

        private SettingsState peekSettingsStateLocked() {
            if (mSettingsState != null) {
                return mSettingsState;
            }
            ensureSettingsLocked();
            return mSettingsState;
        }

        private void notifyForSettingsChange(String name) {
            // Now send the notification through the content framework.
            Uri uri = getNotificationUriFor(name);
            Log.d(TAG, "notifyForSettingsChange uri=" + uri);
            mHandler.obtainMessage(MyHandler.MSG_NOTIFY_URI_CHANGED, uri).sendToTarget();
        }

        private Uri getNotificationUriFor(String name) {
            return (name != null) ? Uri.withAppendedPath(AppSettings.CONTENT_URI, name) : AppSettings.CONTENT_URI;
        }

        private File getSettingsFile() {
            return new File(getContext().getFilesDir(), SETTINGS_FILE);
        }


        private int getMaxBytes() {
            return SettingsState.MAX_BYTES_PER_APP_PACKAGE_UNLIMITED;
        }

        private final class MyHandler extends Handler {
            private static final int MSG_NOTIFY_URI_CHANGED = 1;

            public MyHandler(Looper looper) {
                super(looper);
            }

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_NOTIFY_URI_CHANGED: {
                        final int userId = msg.arg1;
                        Uri uri = (Uri) msg.obj;
                        getContext().getContentResolver().notifyChange(uri, null, true);
                        if (DEBUG) {
                            Log.v(LOG_TAG, "Notifying for " + userId + ": " + uri);
                        }
                    }
                    break;
                }
            }
        }

        private final class UpgradeController {
            private static final int SETTINGS_VERSION = 123;

            public UpgradeController() {
            }

            public void upgradeIfNeededLocked() {
                // The version of all settings for a user is the same (all users have secure).
                SettingsState secureSettings = getSettingsLocked();

                // Try an update from the current state.
                final int oldVersion = secureSettings.getVersionLocked();
                final int newVersion = SETTINGS_VERSION;

                // If up do date - done.
                if (oldVersion == newVersion) {
                    return;
                }

                // Try to upgrade.
                final int curVersion = onUpgradeLocked(oldVersion, newVersion);

                // Set the global settings version if owner.
                SettingsState globalSettings = getSettingsLocked();
                globalSettings.setVersionLocked(newVersion);
            }

            /**
             * You must perform all necessary mutations to bring the settings
             * for this user from the old to the new version. When you add a new
             * upgrade step you *must* update SETTINGS_VERSION.
             * <p>
             * This is an example of moving a setting from secure to global.
             * <p>
             * // v119: Example settings changes.
             * if (currentVersion == 118) {
             * if (userId == UserHandle.USER_OWNER) {
             * // Remove from the secure settings.
             * SettingsState secureSettings = getSecureSettingsLocked(userId);
             * String name = "example_setting_to_move";
             * String value = secureSettings.getSetting(name);
             * secureSettings.deleteSetting(name);
             * <p>
             * // Add to the global settings.
             * SettingsState globalSettings = getGlobalSettingsLocked();
             * globalSettings.insertSetting(name, value, SettingsState.SYSTEM_PACKAGE_NAME);
             * }
             * <p>
             * // Update the current version.
             * currentVersion = 119;
             * }
             */
            private int onUpgradeLocked(int oldVersion, int newVersion) {
                // Return the current version.
                return oldVersion;
            }
        }
    }
}
