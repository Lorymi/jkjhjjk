package org.acra.collector;

import android.util.SparseArray;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.acra.ACRAConstants;

public class MediaCodecListCollector {
    private static final String[] AAC_TYPES = {"aac", "AAC"};
    private static final String[] AVC_TYPES = {"avc", "h264", "AVC", "H264"};
    private static final String COLOR_FORMAT_PREFIX = "COLOR_";
    private static final String[] H263_TYPES = {"h263", "H263"};
    private static final String[] MPEG4_TYPES = {"mp4", "mpeg4", "MP4", "MPEG4"};
    private static Class codecCapabilitiesClass;
    private static Field colorFormatsField;
    private static Method getCapabilitiesForTypeMethod;
    private static Method getCodecInfoAtMethod;
    private static Method getNameMethod;
    private static Method getSupportedTypesMethod;
    private static Method isEncoderMethod;
    private static Field levelField;
    private static SparseArray mAACProfileValues = new SparseArray();
    private static SparseArray mAVCLevelValues = new SparseArray();
    private static SparseArray mAVCProfileValues = new SparseArray();
    private static SparseArray mColorFormatValues = new SparseArray();
    private static SparseArray mH263LevelValues = new SparseArray();
    private static SparseArray mH263ProfileValues = new SparseArray();
    private static SparseArray mMPEG4LevelValues = new SparseArray();
    private static SparseArray mMPEG4ProfileValues = new SparseArray();
    private static Class mediaCodecInfoClass;
    private static Class mediaCodecListClass;
    private static Field profileField;
    private static Field profileLevelsField;

    /* renamed from: org.acra.collector.MediaCodecListCollector$1 */
    /* synthetic */ class C10701 {
        static final /* synthetic */ int[] $SwitchMap$org$acra$collector$MediaCodecListCollector$CodecType = new int[CodecType.values().length];

        static {
            try {
                $SwitchMap$org$acra$collector$MediaCodecListCollector$CodecType[CodecType.AVC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$acra$collector$MediaCodecListCollector$CodecType[CodecType.H263.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$acra$collector$MediaCodecListCollector$CodecType[CodecType.MPEG4.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$acra$collector$MediaCodecListCollector$CodecType[CodecType.AAC.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    enum CodecType {
        AVC,
        H263,
        MPEG4,
        AAC
    }

    static {
        Field[] fields;
        Field[] fields2;
        mediaCodecListClass = null;
        getCodecInfoAtMethod = null;
        mediaCodecInfoClass = null;
        getNameMethod = null;
        isEncoderMethod = null;
        getSupportedTypesMethod = null;
        getCapabilitiesForTypeMethod = null;
        codecCapabilitiesClass = null;
        colorFormatsField = null;
        profileLevelsField = null;
        profileField = null;
        levelField = null;
        try {
            mediaCodecListClass = Class.forName("android.media.MediaCodecList");
            getCodecInfoAtMethod = mediaCodecListClass.getMethod("getCodecInfoAt", new Class[]{Integer.TYPE});
            mediaCodecInfoClass = Class.forName("android.media.MediaCodecInfo");
            getNameMethod = mediaCodecInfoClass.getMethod("getName", new Class[0]);
            isEncoderMethod = mediaCodecInfoClass.getMethod("isEncoder", new Class[0]);
            getSupportedTypesMethod = mediaCodecInfoClass.getMethod("getSupportedTypes", new Class[0]);
            getCapabilitiesForTypeMethod = mediaCodecInfoClass.getMethod("getCapabilitiesForType", new Class[]{String.class});
            codecCapabilitiesClass = Class.forName("android.media.MediaCodecInfo$CodecCapabilities");
            colorFormatsField = codecCapabilitiesClass.getField("colorFormats");
            profileLevelsField = codecCapabilitiesClass.getField("profileLevels");
            for (Field field : codecCapabilitiesClass.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getName().startsWith(COLOR_FORMAT_PREFIX)) {
                    mColorFormatValues.put(field.getInt(null), field.getName());
                }
            }
            Class cls = Class.forName("android.media.MediaCodecInfo$CodecProfileLevel");
            for (Field field2 : cls.getFields()) {
                if (Modifier.isStatic(field2.getModifiers()) && Modifier.isFinal(field2.getModifiers())) {
                    if (field2.getName().startsWith("AVCLevel")) {
                        mAVCLevelValues.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("AVCProfile")) {
                        mAVCProfileValues.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("H263Level")) {
                        mH263LevelValues.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("H263Profile")) {
                        mH263ProfileValues.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("MPEG4Level")) {
                        mMPEG4LevelValues.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("MPEG4Profile")) {
                        mMPEG4ProfileValues.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("AAC")) {
                        mAACProfileValues.put(field2.getInt(null), field2.getName());
                    }
                }
            }
            profileField = cls.getField("profile");
            levelField = cls.getField("level");
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e2) {
        } catch (IllegalArgumentException e3) {
        } catch (IllegalAccessException e4) {
        } catch (SecurityException e5) {
        } catch (NoSuchFieldException e6) {
        }
    }

    public static String collecMediaCodecList() {
        StringBuilder sb = new StringBuilder();
        if (!(mediaCodecListClass == null || mediaCodecInfoClass == null)) {
            try {
                int intValue = ((Integer) mediaCodecListClass.getMethod("getCodecCount", new Class[0]).invoke(null, new Object[0])).intValue();
                for (int i = 0; i < intValue; i++) {
                    sb.append("\n");
                    Object invoke = getCodecInfoAtMethod.invoke(null, new Object[]{Integer.valueOf(i)});
                    sb.append(i).append(": ").append(getNameMethod.invoke(invoke, new Object[0])).append("\n");
                    sb.append("isEncoder: ").append(isEncoderMethod.invoke(invoke, new Object[0])).append("\n");
                    String[] strArr = (String[]) getSupportedTypesMethod.invoke(invoke, new Object[0]);
                    sb.append("Supported types: ").append(Arrays.toString(strArr)).append("\n");
                    for (String collectCapabilitiesForType : strArr) {
                        sb.append(collectCapabilitiesForType(invoke, collectCapabilitiesForType));
                    }
                    sb.append("\n");
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            }
        }
        return sb.toString();
    }

    private static String collectCapabilitiesForType(Object obj, String str) {
        StringBuilder sb = new StringBuilder();
        Object invoke = getCapabilitiesForTypeMethod.invoke(obj, new Object[]{str});
        int[] iArr = (int[]) colorFormatsField.get(invoke);
        if (iArr.length > 0) {
            sb.append(str).append(" color formats:");
            for (int i = 0; i < iArr.length; i++) {
                sb.append((String) mColorFormatValues.get(iArr[i]));
                if (i < iArr.length - 1) {
                    sb.append(',');
                }
            }
            sb.append("\n");
        }
        Object[] objArr = (Object[]) profileLevelsField.get(invoke);
        if (objArr.length > 0) {
            sb.append(str).append(" profile levels:");
            for (int i2 = 0; i2 < objArr.length; i2++) {
                CodecType identifyCodecType = identifyCodecType(obj);
                int i3 = profileField.getInt(objArr[i2]);
                int i4 = levelField.getInt(objArr[i2]);
                if (identifyCodecType == null) {
                    sb.append(i3).append('-').append(i4);
                }
                switch (C10701.$SwitchMap$org$acra$collector$MediaCodecListCollector$CodecType[identifyCodecType.ordinal()]) {
                    case 1:
                        sb.append(i3).append((String) mAVCProfileValues.get(i3)).append('-').append((String) mAVCLevelValues.get(i4));
                        break;
                    case 2:
                        sb.append((String) mH263ProfileValues.get(i3)).append('-').append((String) mH263LevelValues.get(i4));
                        break;
                    case ACRAConstants.DEFAULT_MAX_NUMBER_OF_REQUEST_RETRIES /*3*/:
                        sb.append((String) mMPEG4ProfileValues.get(i3)).append('-').append((String) mMPEG4LevelValues.get(i4));
                        break;
                    case 4:
                        sb.append((String) mAACProfileValues.get(i3));
                        break;
                }
                if (i2 < objArr.length - 1) {
                    sb.append(',');
                }
            }
            sb.append("\n");
        }
        return sb.append("\n").toString();
    }

    private static CodecType identifyCodecType(Object obj) {
        String str = (String) getNameMethod.invoke(obj, new Object[0]);
        for (String contains : AVC_TYPES) {
            if (str.contains(contains)) {
                return CodecType.AVC;
            }
        }
        for (String contains2 : H263_TYPES) {
            if (str.contains(contains2)) {
                return CodecType.H263;
            }
        }
        for (String contains3 : MPEG4_TYPES) {
            if (str.contains(contains3)) {
                return CodecType.MPEG4;
            }
        }
        for (String contains4 : AAC_TYPES) {
            if (str.contains(contains4)) {
                return CodecType.AAC;
            }
        }
        return null;
    }
}
