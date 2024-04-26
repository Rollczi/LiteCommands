package dev.rollczi.litecommands.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public final class ObjectsUtil {

    private static final Object[] EMPTY_GENERIC_ARRAY = new Object[0];

    private ObjectsUtil() {}

    public static boolean equals(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;

        if (a.getClass().isArray() && b.getClass().isArray()) {
            if (a instanceof Object[] && b instanceof Object[]) {
                return Arrays.equals((Object[]) a, (Object[]) b);
            }
            else if (a instanceof boolean[] && b instanceof boolean[]) {
                return Arrays.equals((boolean[]) a, (boolean[]) b);
            }
            else if (a instanceof byte[] && b instanceof byte[]) {
                return Arrays.equals((byte[]) a, (byte[]) b);
            }
            else if (a instanceof char[] && b instanceof char[]) {
                return Arrays.equals((char[]) a, (char[]) b);
            }
            else if (a instanceof double[] && b instanceof double[]) {
                return Arrays.equals((double[]) a, (double[]) b);
            }
            else if (a instanceof float[] && b instanceof float[]) {
                return Arrays.equals((float[]) a, (float[]) b);
            }
            else if (a instanceof int[] && b instanceof int[]) {
                return Arrays.equals((int[]) a, (int[]) b);
            }
            else if (a instanceof long[] && b instanceof long[]) {
                return Arrays.equals((long[]) a, (long[]) b);
            }
            else if (a instanceof short[] && b instanceof short[]) {
                return Arrays.equals((short[]) a, (short[]) b);
            }
        }

        return a.equals(b);
    }

    public static Object[] getGenericCopyOfPrimitiveArray(Object result) {
        if (result == null || !result.getClass().isArray()) {
            return EMPTY_GENERIC_ARRAY;
        }

        int length = Array.getLength(result);

        Object[] output = new Object[length];
        for (int i = 0; i < length; i++) {
            output[i] = Array.get(result, i);
        }

        return output;
    }

}
