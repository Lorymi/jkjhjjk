package org.acra.collector;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.util.SparseArray;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.acra.ACRA;

public final class ConfigurationCollector {
    private static final String FIELD_MCC = "mcc";
    private static final String FIELD_MNC = "mnc";
    private static final String FIELD_SCREENLAYOUT = "screenLayout";
    private static final String FIELD_UIMODE = "uiMode";
    private static final String PREFIX_HARDKEYBOARDHIDDEN = "HARDKEYBOARDHIDDEN_";
    private static final String PREFIX_KEYBOARD = "KEYBOARD_";
    private static final String PREFIX_KEYBOARDHIDDEN = "KEYBOARDHIDDEN_";
    private static final String PREFIX_NAVIGATION = "NAVIGATION_";
    private static final String PREFIX_NAVIGATIONHIDDEN = "NAVIGATIONHIDDEN_";
    private static final String PREFIX_ORIENTATION = "ORIENTATION_";
    private static final String PREFIX_SCREENLAYOUT = "SCREENLAYOUT_";
    private static final String PREFIX_TOUCHSCREEN = "TOUCHSCREEN_";
    private static final String PREFIX_UI_MODE = "UI_MODE_";
    private static final String SUFFIX_MASK = "_MASK";
    private static SparseArray mHardKeyboardHiddenValues = new SparseArray();
    private static SparseArray mKeyboardHiddenValues = new SparseArray();
    private static SparseArray mKeyboardValues = new SparseArray();
    private static SparseArray mNavigationHiddenValues = new SparseArray();
    private static SparseArray mNavigationValues = new SparseArray();
    private static SparseArray mOrientationValues = new SparseArray();
    private static SparseArray mScreenLayoutValues = new SparseArray();
    private static SparseArray mTouchScreenValues = new SparseArray();
    private static SparseArray mUiModeValues = new SparseArray();
    private static final HashMap mValueArrays = new HashMap();

    static {
        Field[] fields;
        mValueArrays.put(PREFIX_HARDKEYBOARDHIDDEN, mHardKeyboardHiddenValues);
        mValueArrays.put(PREFIX_KEYBOARD, mKeyboardValues);
        mValueArrays.put(PREFIX_KEYBOARDHIDDEN, mKeyboardHiddenValues);
        mValueArrays.put(PREFIX_NAVIGATION, mNavigationValues);
        mValueArrays.put(PREFIX_NAVIGATIONHIDDEN, mNavigationHiddenValues);
        mValueArrays.put(PREFIX_ORIENTATION, mOrientationValues);
        mValueArrays.put(PREFIX_SCREENLAYOUT, mScreenLayoutValues);
        mValueArrays.put(PREFIX_TOUCHSCREEN, mTouchScreenValues);
        mValueArrays.put(PREFIX_UI_MODE, mUiModeValues);
        for (Field field : Configuration.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                String name = field.getName();
                try {
                    if (name.startsWith(PREFIX_HARDKEYBOARDHIDDEN)) {
                        mHardKeyboardHiddenValues.put(field.getInt(null), name);
                    } else if (name.startsWith(PREFIX_KEYBOARD)) {
                        mKeyboardValues.put(field.getInt(null), name);
                    } else if (name.startsWith(PREFIX_KEYBOARDHIDDEN)) {
                        mKeyboardHiddenValues.put(field.getInt(null), name);
                    } else if (name.startsWith(PREFIX_NAVIGATION)) {
                        mNavigationValues.put(field.getInt(null), name);
                    } else if (name.startsWith(PREFIX_NAVIGATIONHIDDEN)) {
                        mNavigationHiddenValues.put(field.getInt(null), name);
                    } else if (name.startsWith(PREFIX_ORIENTATION)) {
                        mOrientationValues.put(field.getInt(null), name);
                    } else if (name.startsWith(PREFIX_SCREENLAYOUT)) {
                        mScreenLayoutValues.put(field.getInt(null), name);
                    } else if (name.startsWith(PREFIX_TOUCHSCREEN)) {
                        mTouchScreenValues.put(field.getInt(null), name);
                    } else if (name.startsWith(PREFIX_UI_MODE)) {
                        mUiModeValues.put(field.getInt(null), name);
                    }
                } catch (IllegalArgumentException e) {
                    Log.w(ACRA.LOG_TAG, "Error while inspecting device configuration: ", e);
                } catch (IllegalAccessException e2) {
                    Log.w(ACRA.LOG_TAG, "Error while inspecting device configuration: ", e2);
                }
            }
        }
    }

    private static String activeFlags(SparseArray sparseArray, int i) {
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= sparseArray.size()) {
                return sb.toString();
            }
            int keyAt = sparseArray.keyAt(i3);
            if (((String) sparseArray.get(keyAt)).endsWith(SUFFIX_MASK)) {
                int i4 = i & keyAt;
                if (i4 > 0) {
                    if (sb.length() > 0) {
                        sb.append('+');
                    }
                    sb.append((String) sparseArray.get(i4));
                }
            }
            i2 = i3 + 1;
        }
    }

    public static String collectConfiguration(Context context) {
        try {
            return toString(context.getResources().getConfiguration());
        } catch (RuntimeException e) {
            Log.w(ACRA.LOG_TAG, "Couldn't retrieve CrashConfiguration for : " + context.getPackageName(), e);
            return "Couldn't retrieve crash config";
        }
    }

    private static String getFieldValueName(Configuration configuration, Field field) {
        String name = field.getName();
        if (name.equals(FIELD_MCC) || name.equals(FIELD_MNC)) {
            return Integer.toString(field.getInt(configuration));
        }
        if (name.equals(FIELD_UIMODE)) {
            return activeFlags((SparseArray) mValueArrays.get(PREFIX_UI_MODE), field.getInt(configuration));
        }
        if (name.equals(FIELD_SCREENLAYOUT)) {
            return activeFlags((SparseArray) mValueArrays.get(PREFIX_SCREENLAYOUT), field.getInt(configuration));
        }
        SparseArray sparseArray = (SparseArray) mValueArrays.get(name.toUpperCase() + '_');
        if (sparseArray == null) {
            return Integer.toString(field.getInt(configuration));
        }
        String str = (String) sparseArray.get(field.getInt(configuration));
        return str == null ? Integer.toString(field.getInt(configuration)) : str;
    }

    public static String toString(Configuration configuration) {
        Field[] fields;
        StringBuilder sb = new StringBuilder();
        for (Field field : configuration.getClass().getFields()) {
            try {
                if (!Modifier.isStatic(field.getModifiers())) {
                    sb.append(field.getName()).append('=');
                    if (field.getType().equals(Integer.TYPE)) {
                        sb.append(getFieldValueName(configuration, field));
                    } else if (field.get(configuration) != null) {
                        sb.append(field.get(configuration).toString());
                    }
                    sb.append(10);
                }
            } catch (IllegalArgumentException e) {
                Log.e(ACRA.LOG_TAG, "Error while inspecting device configuration: ", e);
            } catch (IllegalAccessException e2) {
                Log.e(ACRA.LOG_TAG, "Error while inspecting device configuration: ", e2);
            }
        }
        return sb.toString();
    }
}
