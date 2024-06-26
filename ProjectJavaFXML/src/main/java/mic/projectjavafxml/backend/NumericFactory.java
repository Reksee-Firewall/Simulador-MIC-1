package mic.projectjavafxml.backend;

public class NumericFactory {
    private static int radix = 10;

    public static short getShortValue(String value) {
        if (radix == 10)
            return getShortValue10(value);
        return getShortValue2(value);
    }

    private static short getShortValue2(String value) {
        return (short) Integer.parseUnsignedInt(value, 2);
    }

    private static short getShortValue10(String value) {
        return (short) Integer.parseInt(value);
    }

    public static String getStringValue16(short value) {
        return getStringValue(value, 16);
    }

    public static String getStringValue32(int value) {
        return getStringValue(value, 32);
    }

    public static String getStringValue8(short value) {
        // here we use short bcs we need unsigned byte
        return getStringValue(value, 8);
    }

    public static String getStringValue(int value, int length) {
        if (radix == 10)
            return String.valueOf(value);

        String res = Integer.toBinaryString(value);
        if (res.length() > length)
            return res.substring(res.length()-length);
        return "0".repeat(length - res.length()) + res;
    }

    public static short getShortValue(String value, int radix) {
        try {
            return (short) Integer.parseInt(value, radix);
        } catch (NumberFormatException e) {
            System.out.println("Error in NumericFactory getShortValue");
        }
        return 0;
    }

    public static int getRadix() {
        return radix;
    }

    public static void setRadix(int radix) {
        NumericFactory.radix = radix;
    }
}
