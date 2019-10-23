package org.acra.collector;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.acra.ACRA;
import org.acra.ACRAConstants;

final class DisplayManagerCollector {
    static final SparseArray mDensities = new SparseArray();
    static final SparseArray mFlagsNames = new SparseArray();

    DisplayManagerCollector() {
    }

    private static String activeFlags(SparseArray sparseArray, int i) {
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= sparseArray.size()) {
                return sb.toString();
            }
            int keyAt = sparseArray.keyAt(i3) & i;
            if (keyAt > 0) {
                if (sb.length() > 0) {
                    sb.append('+');
                }
                sb.append((String) sparseArray.get(keyAt));
            }
            i2 = i3 + 1;
        }
    }

    private static String collectCurrentSizeRange(Display display) {
        StringBuilder sb = new StringBuilder();
        try {
            Method method = display.getClass().getMethod("getCurrentSizeRange", new Class[]{Point.class, Point.class});
            Point point = new Point();
            Point point2 = new Point();
            method.invoke(display, new Object[]{point, point2});
            sb.append(display.getDisplayId()).append(".currentSizeRange.smallest=[").append(point.x).append(',').append(point.y).append(']').append(10);
            sb.append(display.getDisplayId()).append(".currentSizeRange.largest=[").append(point2.x).append(',').append(point2.y).append(']').append(10);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
        return sb.toString();
    }

    private static Object collectDisplayData(Display display) {
        display.getMetrics(new DisplayMetrics());
        StringBuilder sb = new StringBuilder();
        sb.append(collectCurrentSizeRange(display));
        sb.append(collectFlags(display));
        sb.append(display.getDisplayId()).append(".height=").append(display.getHeight()).append(10);
        sb.append(collectMetrics(display, "getMetrics"));
        sb.append(collectName(display));
        sb.append(display.getDisplayId()).append(".orientation=").append(display.getOrientation()).append(10);
        sb.append(display.getDisplayId()).append(".pixelFormat=").append(display.getPixelFormat()).append(10);
        sb.append(collectMetrics(display, "getRealMetrics"));
        sb.append(collectSize(display, "getRealSize"));
        sb.append(collectRectSize(display));
        sb.append(display.getDisplayId()).append(".refreshRate=").append(display.getRefreshRate()).append(10);
        sb.append(collectRotation(display));
        sb.append(collectSize(display, "getSize"));
        sb.append(display.getDisplayId()).append(".width=").append(display.getWidth()).append(10);
        sb.append(collectIsValid(display));
        return sb.toString();
    }

    public static String collectDisplays(Context context) {
        Display[] displayArr;
        StringBuilder sb = new StringBuilder();
        if (Compatibility.getAPILevel() < 17) {
            displayArr = new Display[]{((WindowManager) context.getSystemService("window")).getDefaultDisplay()};
        } else {
            try {
                Object systemService = context.getSystemService((String) context.getClass().getField("DISPLAY_SERVICE").get(null));
                displayArr = (Display[]) systemService.getClass().getMethod("getDisplays", new Class[0]).invoke(systemService, new Object[0]);
            } catch (IllegalArgumentException e) {
                ACRA.log.mo2453w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e);
                displayArr = null;
            } catch (SecurityException e2) {
                ACRA.log.mo2453w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e2);
                displayArr = null;
            } catch (IllegalAccessException e3) {
                ACRA.log.mo2453w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e3);
                displayArr = null;
            } catch (NoSuchFieldException e4) {
                ACRA.log.mo2453w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e4);
                displayArr = null;
            } catch (NoSuchMethodException e5) {
                ACRA.log.mo2453w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e5);
                displayArr = null;
            } catch (InvocationTargetException e6) {
                ACRA.log.mo2453w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e6);
                displayArr = null;
            }
        }
        for (Display collectDisplayData : displayArr) {
            sb.append(collectDisplayData(collectDisplayData));
        }
        return sb.toString();
    }

    private static String collectFlags(Display display) {
        Field[] fields;
        StringBuilder sb = new StringBuilder();
        try {
            int intValue = ((Integer) display.getClass().getMethod("getFlags", new Class[0]).invoke(display, new Object[0])).intValue();
            for (Field field : display.getClass().getFields()) {
                if (field.getName().startsWith("FLAG_")) {
                    mFlagsNames.put(field.getInt(null), field.getName());
                }
            }
            sb.append(display.getDisplayId()).append(".flags=").append(activeFlags(mFlagsNames, intValue)).append(10);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
        return sb.toString();
    }

    private static Object collectIsValid(Display display) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(display.getDisplayId()).append(".isValid=").append((Boolean) display.getClass().getMethod("isValid", new Class[0]).invoke(display, new Object[0])).append(10);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
        return sb.toString();
    }

    private static Object collectMetrics(Display display, String str) {
        Field[] fields;
        StringBuilder sb = new StringBuilder();
        try {
            DisplayMetrics displayMetrics = (DisplayMetrics) display.getClass().getMethod(str, new Class[0]).invoke(display, new Object[0]);
            for (Field field : DisplayMetrics.class.getFields()) {
                if (field.getType().equals(Integer.class) && field.getName().startsWith("DENSITY_") && !field.getName().equals("DENSITY_DEFAULT")) {
                    mDensities.put(field.getInt(null), field.getName());
                }
            }
            sb.append(display.getDisplayId()).append('.').append(str).append(".density=").append(displayMetrics.density).append(10);
            sb.append(display.getDisplayId()).append('.').append(str).append(".densityDpi=").append(displayMetrics.getClass().getField("densityDpi")).append(10);
            sb.append(display.getDisplayId()).append('.').append(str).append("scaledDensity=x").append(displayMetrics.scaledDensity).append(10);
            sb.append(display.getDisplayId()).append('.').append(str).append(".widthPixels=").append(displayMetrics.widthPixels).append(10);
            sb.append(display.getDisplayId()).append('.').append(str).append(".heightPixels=").append(displayMetrics.heightPixels).append(10);
            sb.append(display.getDisplayId()).append('.').append(str).append(".xdpi=").append(displayMetrics.xdpi).append(10);
            sb.append(display.getDisplayId()).append('.').append(str).append(".ydpi=").append(displayMetrics.ydpi).append(10);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
        return sb.toString();
    }

    private static String collectName(Display display) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(display.getDisplayId()).append(".name=").append((String) display.getClass().getMethod("getName", new Class[0]).invoke(display, new Object[0])).append(10);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
        return sb.toString();
    }

    private static Object collectRectSize(Display display) {
        StringBuilder sb = new StringBuilder();
        try {
            Method method = display.getClass().getMethod("getRectSize", new Class[]{Rect.class});
            Rect rect = new Rect();
            method.invoke(display, new Object[]{rect});
            sb.append(display.getDisplayId()).append(".rectSize=[").append(rect.top).append(',').append(rect.left).append(',').append(rect.width()).append(',').append(rect.height()).append(']').append(10);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
        return sb.toString();
    }

    private static Object collectRotation(Display display) {
        StringBuilder sb = new StringBuilder();
        try {
            int intValue = ((Integer) display.getClass().getMethod("getRotation", new Class[0]).invoke(display, new Object[0])).intValue();
            sb.append(display.getDisplayId()).append(".rotation=");
            switch (intValue) {
                case 0:
                    sb.append("ROTATION_0");
                    break;
                case 1:
                    sb.append("ROTATION_90");
                    break;
                case 2:
                    sb.append("ROTATION_180");
                    break;
                case ACRAConstants.DEFAULT_MAX_NUMBER_OF_REQUEST_RETRIES /*3*/:
                    sb.append("ROTATION_270");
                    break;
                default:
                    sb.append(intValue);
                    break;
            }
            sb.append(10);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e2) {
        } catch (IllegalArgumentException e3) {
        } catch (IllegalAccessException | InvocationTargetException e4) {
        }
        return sb.toString();
    }

    private static Object collectSize(Display display, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            Method method = display.getClass().getMethod(str, new Class[]{Point.class});
            Point point = new Point();
            method.invoke(display, new Object[]{point});
            sb.append(display.getDisplayId()).append('.').append(str).append("=[").append(point.x).append(',').append(point.y).append(']').append(10);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
        return sb.toString();
    }
}
