package com.snt.phoney.provider;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the state for one type of settings. It is responsible
 * for saving the state asynchronously to an XML file after a mutation and
 * loading the from an XML file on construction.
 * <p>
 * This class uses the same lock as the settings provider to ensure that
 * multiple changes made by the settings provider, e,g, upgrade, bulk insert,
 * etc, are atomically persisted since the asynchronous persistence is using
 * the same lock to grab the current state to write to disk.
 * </p>
 */
final class SettingsState {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_PERSISTENCE = false;

    private static final String LOG_TAG = "SettingsState";

    static final int SETTINGS_VERSOIN_NEW_ENCODING = 121;

    private static final long WRITE_SETTINGS_DELAY_MILLIS = 200;
    private static final long MAX_WRITE_SETTINGS_DELAY_MILLIS = 2000;

    public static final int MAX_BYTES_PER_APP_PACKAGE_UNLIMITED = -1;
    public static final int MAX_BYTES_PER_APP_PACKAGE_LIMITED = 20000;

    public static final String SYSTEM_PACKAGE_NAME = "android";

    public static final int VERSION_UNDEFINED = -1;

    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_SETTING = "setting";
    private static final String ATTR_PACKAGE = "package";

    private static final String ATTR_VERSION = "version";
    private static final String ATTR_ID = "id";
    private static final String ATTR_NAME = "name";

    /**
     * Non-binary value will be written in this attribute.
     */
    private static final String ATTR_VALUE = "value";

    /**
     * KXmlSerializer won't like some characters.  We encode such characters in base64 and
     * store in this attribute.
     * NOTE: A null value will have NEITHER ATTR_VALUE nor ATTR_VALUE_BASE64.
     */
    private static final String ATTR_VALUE_BASE64 = "valueBase64";

    // This was used in version 120 and before.
    private static final String NULL_VALUE_OLD_STYLE = "null";

    private final Object mLock;

    private final Handler mHandler = new MyHandler();

    private final ArrayMap<String, Setting> mSettings = new ArrayMap<>();

    private final ArrayMap<String, Integer> mPackageToMemoryUsage;

    private final int mMaxBytesPerAppPackage;

    private final File mStatePersistFile;

    private int mVersion = VERSION_UNDEFINED;

    private long mLastNotWrittenMutationTimeMillis;

    private boolean mDirty;

    private boolean mWriteScheduled;

    private long mNextId;

    public SettingsState(Object lock, File file, int maxBytesPerAppPackage) {
        // It is important that we use the same lock as the settings provider
        // to ensure multiple mutations on this state are atomicaly persisted
        // as the async persistence should be blocked while we make changes.
        mLock = lock;
        mStatePersistFile = file;
        if (maxBytesPerAppPackage == MAX_BYTES_PER_APP_PACKAGE_LIMITED) {
            mMaxBytesPerAppPackage = maxBytesPerAppPackage;
            mPackageToMemoryUsage = new ArrayMap<>();
        } else {
            mMaxBytesPerAppPackage = maxBytesPerAppPackage;
            mPackageToMemoryUsage = null;
        }
        synchronized (mLock) {
            readStateSyncLocked();
        }
    }

    // The settings provider must hold its lock when calling here.
    public int getVersionLocked() {
        return mVersion;
    }

    // The settings provider must hold its lock when calling here.
    public void setVersionLocked(int version) {
        if (version == mVersion) {
            return;
        }
        mVersion = version;

        scheduleWriteIfNeededLocked();
    }

    // The settings provider must hold its lock when calling here.
    public List<String> getSettingNamesLocked() {
        ArrayList<String> names = new ArrayList<>();
        final int settingsCount = mSettings.size();
        for (int i = 0; i < settingsCount; i++) {
            String name = mSettings.keyAt(i);
            names.add(name);
        }
        return names;
    }

    // The settings provider must hold its lock when calling here.
    public Setting getSettingLocked(String name) {
        if (DEBUG) {
            Log.i(LOG_TAG, "getSettingLocked, name = " + name);
        }

        if (TextUtils.isEmpty(name)) {
            return null;
        }
        return mSettings.get(name);
    }

    // The settings provider must hold its lock when calling here.
    public boolean updateSettingLocked(String name, String value, String packageName) {
        if (!hasSettingLocked(name)) {
            return false;
        }

        return insertSettingLocked(name, value, packageName);
    }

    // The settings provider must hold its lock when calling here.
    public boolean insertSettingLocked(String name, String value, String packageName) {
        if (DEBUG) {
            Log.i(LOG_TAG, "insertSettingLocked, name = " + name + ", value = " + value);
        }

        if (TextUtils.isEmpty(name)) {
            return false;
        }

        Setting oldState = mSettings.get(name);
        String oldValue = (oldState != null) ? oldState.value : null;

        if (oldState != null) {
            if (!oldState.update(value, packageName)) {
                return false;
            }
        } else {
            Setting state = new Setting(name, value, packageName);
            mSettings.put(name, state);
        }

        updateMemoryUsagePerPackageLocked(packageName, oldValue, value);

        scheduleWriteIfNeededLocked();

        return true;
    }

    // The settings provider must hold its lock when calling here.
    public void persistSyncLocked() {
        mHandler.removeMessages(MyHandler.MSG_PERSIST_SETTINGS);
        doWriteState();
    }

    // The settings provider must hold its lock when calling here.
    public boolean deleteSettingLocked(String name) {
        if (DEBUG) {
            Log.i(LOG_TAG, "deleteSettingLocked, name = " + name);
        }

        if (TextUtils.isEmpty(name) || !hasSettingLocked(name)) {
            return false;
        }

        Setting oldState = mSettings.remove(name);

        updateMemoryUsagePerPackageLocked(oldState.packageName, oldState.value, null);

        scheduleWriteIfNeededLocked();

        return true;
    }

    // The settings provider must hold its lock when calling here.
    public void destroyLocked(Runnable callback) {
        mHandler.removeMessages(MyHandler.MSG_PERSIST_SETTINGS);
        if (callback != null) {
            if (mDirty) {
                // Do it without a delay.
                mHandler.obtainMessage(MyHandler.MSG_PERSIST_SETTINGS,
                        callback).sendToTarget();
                return;
            }
            callback.run();
        }
    }

    private void updateMemoryUsagePerPackageLocked(String packageName, String oldValue, String newValue) {
        if (mMaxBytesPerAppPackage == MAX_BYTES_PER_APP_PACKAGE_UNLIMITED) {
            return;
        }

        if (SYSTEM_PACKAGE_NAME.equals(packageName)) {
            return;
        }

        final int oldValueSize = (oldValue != null) ? oldValue.length() : 0;
        final int newValueSize = (newValue != null) ? newValue.length() : 0;
        final int deltaSize = newValueSize - oldValueSize;

        Integer currentSize = mPackageToMemoryUsage.get(packageName);
        final int newSize = Math.max((currentSize != null) ? currentSize + deltaSize : deltaSize, 0);

        if (newSize > mMaxBytesPerAppPackage) {
            throw new IllegalStateException("You are adding too many system settings. "
                    + "You should stop using system settings for app specific data"
                    + " package: " + packageName);
        }

        if (DEBUG) {
            Log.i(LOG_TAG, "Settings for package: " + packageName
                    + " size: " + newSize + " bytes.");
        }

        mPackageToMemoryUsage.put(packageName, newSize);
    }

    private boolean hasSettingLocked(String name) {
        return mSettings.indexOfKey(name) >= 0;
    }

    private void scheduleWriteIfNeededLocked() {
        // If dirty then we have a write already scheduled.
        if (!mDirty) {
            mDirty = true;
            writeStateAsyncLocked();
        }
    }

    private void writeStateAsyncLocked() {
        final long currentTimeMillis = SystemClock.uptimeMillis();

        if (mWriteScheduled) {
            mHandler.removeMessages(MyHandler.MSG_PERSIST_SETTINGS);

            // If enough time passed, write without holding off anymore.
            final long timeSinceLastNotWrittenMutationMillis = currentTimeMillis
                    - mLastNotWrittenMutationTimeMillis;
            if (timeSinceLastNotWrittenMutationMillis >= MAX_WRITE_SETTINGS_DELAY_MILLIS) {
                mHandler.obtainMessage(MyHandler.MSG_PERSIST_SETTINGS).sendToTarget();
                return;
            }

            // Hold off a bit more as settings are frequently changing.
            final long maxDelayMillis = Math.max(mLastNotWrittenMutationTimeMillis
                    + MAX_WRITE_SETTINGS_DELAY_MILLIS - currentTimeMillis, 0);
            final long writeDelayMillis = Math.min(WRITE_SETTINGS_DELAY_MILLIS, maxDelayMillis);

            Message message = mHandler.obtainMessage(MyHandler.MSG_PERSIST_SETTINGS);
            mHandler.sendMessageDelayed(message, writeDelayMillis);
        } else {
            mLastNotWrittenMutationTimeMillis = currentTimeMillis;
            Message message = mHandler.obtainMessage(MyHandler.MSG_PERSIST_SETTINGS);
            mHandler.sendMessageDelayed(message, WRITE_SETTINGS_DELAY_MILLIS);
            mWriteScheduled = true;
        }
    }

    private void doWriteState() {
        if (DEBUG_PERSISTENCE) {
            Log.i(LOG_TAG, "[PERSIST START]");
        }

        AtomicFile destination = new AtomicFile(mStatePersistFile);

        final int version;
        final ArrayMap<String, Setting> settings;

        synchronized (mLock) {
            version = mVersion;
            settings = new ArrayMap<>(mSettings);
            mDirty = false;
            mWriteScheduled = false;
        }

        FileOutputStream out = null;
        try {
            out = destination.startWrite();

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(out, StandardCharsets.UTF_8.name());
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startDocument(null, true);
            serializer.startTag(null, TAG_SETTINGS);
            serializer.attribute(null, ATTR_VERSION, String.valueOf(version));

            final int settingCount = settings.size();
            for (int i = 0; i < settingCount; i++) {
                Setting setting = settings.valueAt(i);

                writeSingleSetting(mVersion, serializer, setting.getId(), setting.getName(),
                        setting.getValue(), setting.getPackageName());

                if (DEBUG_PERSISTENCE) {
                    Log.i(LOG_TAG, "[PERSISTED]" + setting.getName() + "=" + setting.getValue());
                }
            }

            serializer.endTag(null, TAG_SETTINGS);
            serializer.endDocument();
            destination.finishWrite(out);

            if (DEBUG_PERSISTENCE) {
                Log.i(LOG_TAG, "[PERSIST END]");
            }
        } catch (Throwable t) {
            Log.wtf(LOG_TAG, "Failed to write settings, restoring backup", t);
            destination.failWrite(out);
        } finally {
            closeQuietly(out);
        }
    }

    /**
     * Closes 'closeable', ignoring any checked exceptions. Does nothing if 'closeable' is null.
     */
    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    static void writeSingleSetting(int version, XmlSerializer serializer, String id,
                                   String name, String value, String packageName) throws IOException {
        if (DEBUG) {
            Log.i(LOG_TAG, "writeSingleSetting, version = " + version + ", name = " + name
                    + ", value = " + value + ", packageName = " + packageName);
        }

        if (id == null || isBinary(id) || name == null || isBinary(name)
                || packageName == null || isBinary(packageName)) {
            // This shouldn't happen.
            return;
        }
        serializer.startTag(null, TAG_SETTING);
        serializer.attribute(null, ATTR_ID, id);
        serializer.attribute(null, ATTR_NAME, name);
        setValueAttribute(version, serializer, value);
        serializer.attribute(null, ATTR_PACKAGE, packageName);
        serializer.endTag(null, TAG_SETTING);
    }

    static void setValueAttribute(int version, XmlSerializer serializer, String value)
            throws IOException {
        if (version >= SETTINGS_VERSOIN_NEW_ENCODING) {
            if (value == null) {
                // Null value -> No ATTR_VALUE nor ATTR_VALUE_BASE64.
            } else if (isBinary(value)) {
                serializer.attribute(null, ATTR_VALUE_BASE64, base64Encode(value));
            } else {
                serializer.attribute(null, ATTR_VALUE, value);
            }
        } else {
            // Old encoding.
            if (value == null) {
                serializer.attribute(null, ATTR_VALUE, NULL_VALUE_OLD_STYLE);
            } else {
                serializer.attribute(null, ATTR_VALUE, value);
            }
        }
    }

    private String getValueAttribute(XmlPullParser parser) {
        if (mVersion >= SETTINGS_VERSOIN_NEW_ENCODING) {
            final String value = parser.getAttributeValue(null, ATTR_VALUE);
            if (value != null) {
                return value;
            }
            final String base64 = parser.getAttributeValue(null, ATTR_VALUE_BASE64);
            if (base64 != null) {
                return base64Decode(base64);
            }
            // null has neither ATTR_VALUE nor ATTR_VALUE_BASE64.
            return null;
        } else {
            // Old encoding.
            final String stored = parser.getAttributeValue(null, ATTR_VALUE);
            if (NULL_VALUE_OLD_STYLE.equals(stored)) {
                return null;
            } else {
                return stored;
            }
        }
    }

    private void readStateSyncLocked() {
        FileInputStream in;
        if (!mStatePersistFile.exists()) {
            return;
        }
        try {
            in = new AtomicFile(mStatePersistFile).openRead();
        } catch (FileNotFoundException fnfe) {
            Log.i(LOG_TAG, "No settings state");
            return;
        }
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, StandardCharsets.UTF_8.name());
            parseStateLocked(parser);

        } catch (XmlPullParserException | IOException e) {
            throw new IllegalStateException("Failed parsing settings file: "
                    + mStatePersistFile, e);
        } finally {
            closeQuietly(in);
        }
    }

    private void parseStateLocked(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        final int outerDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
            if (tagName.equals(TAG_SETTINGS)) {
                parseSettingsLocked(parser);
            }
        }
    }

    private void parseSettingsLocked(XmlPullParser parser)
            throws IOException, XmlPullParserException {

        mVersion = Integer.parseInt(parser.getAttributeValue(null, ATTR_VERSION));

        final int outerDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
            if (tagName.equals(TAG_SETTING)) {
                String id = parser.getAttributeValue(null, ATTR_ID);
                String name = parser.getAttributeValue(null, ATTR_NAME);
                String value = getValueAttribute(parser);
                String packageName = parser.getAttributeValue(null, ATTR_PACKAGE);
                mSettings.put(name, new Setting(name, value, packageName, id));

                if (DEBUG_PERSISTENCE) {
                    Log.i(LOG_TAG, "[RESTORED] " + name + "=" + value);
                }
            }
        }
    }

    public static final class BackgroundThread extends HandlerThread {
        private static BackgroundThread sInstance;
        private static Handler sHandler;

        private BackgroundThread() {
            super("settings", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        }

        private static void ensureThreadLocked() {
            if (sInstance == null) {
                sInstance = new BackgroundThread();
                sInstance.start();
                sHandler = new Handler(sInstance.getLooper());
            }
        }

        public static BackgroundThread get() {
            synchronized (BackgroundThread.class) {
                ensureThreadLocked();
                return sInstance;
            }
        }

        public static Handler getHandler() {
            synchronized (BackgroundThread.class) {
                ensureThreadLocked();
                return sHandler;
            }
        }
    }

    private final class MyHandler extends Handler {
        public static final int MSG_PERSIST_SETTINGS = 1;

        public MyHandler() {
            super(BackgroundThread.getHandler().getLooper());
        }

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_PERSIST_SETTINGS: {
                    Runnable callback = (Runnable) message.obj;
                    doWriteState();
                    if (callback != null) {
                        callback.run();
                    }
                }
                break;
            }
        }
    }

    public final class Setting {
        private String name;
        private String value;
        private String packageName;
        private String id;

        public Setting(String name, String value, String packageName) {
            init(name, value, packageName, String.valueOf(mNextId++));
        }

        public Setting(String name, String value, String packageName, String id) {
            mNextId = Math.max(mNextId, Long.valueOf(id) + 1);
            init(name, value, packageName, id);
        }

        private void init(String name, String value, String packageName, String id) {
            this.name = name;
            this.value = value;
            this.packageName = packageName;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getId() {
            return id;
        }

        public boolean update(String value, String packageName) {
            if (value.equals(this.value)) {
                return false;
            }
            this.value = value;
            this.packageName = packageName;
            this.id = String.valueOf(mNextId++);
            return true;
        }
    }

    /**
     * @return TRUE if a string is considered "binary" from KXML's point of view.  NOTE DO NOT
     * pass null.
     */
    public static boolean isBinary(String s) {
        if (s == null) {
            throw new NullPointerException();
        }
        // See KXmlSerializer.writeEscaped
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean allowedInXml = (c >= 0x20 && c <= 0xd7ff) || (c >= 0xe000 && c <= 0xfffd);
            if (!allowedInXml) {
                return true;
            }
        }
        return false;
    }

    private static String base64Encode(String s) {
        return Base64.encodeToString(toBytes(s), Base64.NO_WRAP);
    }

    private static String base64Decode(String s) {
        return fromBytes(Base64.decode(s, Base64.DEFAULT));
    }

    // Note the followings are basically just UTF-16 encode/decode.  But we want to preserve
    // contents as-is, even if it contains broken surrogate pairs, we do it by ourselves,
    // since I don't know how Charset would treat them.

    private static byte[] toBytes(String s) {
        final byte[] result = new byte[s.length() * 2];
        int resultIndex = 0;
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            result[resultIndex++] = (byte) (ch >> 8);
            result[resultIndex++] = (byte) ch;
        }
        return result;
    }

    private static String fromBytes(byte[] bytes) {
        final StringBuffer sb = new StringBuffer(bytes.length / 2);

        final int last = bytes.length - 1;

        for (int i = 0; i < last; i += 2) {
            final char ch = (char) ((bytes[i] & 0xff) << 8 | (bytes[i + 1] & 0xff));
            sb.append(ch);
        }
        return sb.toString();
    }
}
