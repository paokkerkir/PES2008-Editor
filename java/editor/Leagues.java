/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import java.io.UnsupportedEncodingException;

public class Leagues {
    static final byte total = 28;
    static final int nameAdr = 9449;
    static final byte maxLen = 61;
    static final byte fieldLen = 84;
    static final String[] def = new String[]{"ISS", "England", "French", "German", "Italian", "Netherlands", "Spanish", "International", "European", "African", "American", "Asia-Oceanian", "Konami", "D2", "D1", "European Masters", "European Championship"};

    static String get(OptionFile optionFile, int n) {
        String string;
        int n2 = 9449 + n * 84;
        int n3 = 0;
        if (optionFile.data[n2] != 0) {
            int n4 = 0;
            while (n3 == 0 && n4 < 62) {
                if (n3 == 0 && optionFile.data[n2 + n4] == 0) {
                    n3 = n4;
                }
                n4 = (byte)(n4 + 1);
            }
            try {
                string = new String(optionFile.data, n2, n3, "UTF-8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                string = "<" + String.valueOf(n) + ">";
            }
        } else if (n > 10) {
            string = def[n - 11];
            if (n < 27) {
                string = String.valueOf(String.valueOf(string)) + " Cup";
            }
        } else {
            string = "<" + String.valueOf(n) + ">";
        }
        return string;
    }

    static String[] get(OptionFile optionFile) {
        String[] stringArray = new String[28];
        int n = 0;
        while (n < 28) {
            stringArray[n] = Leagues.get(optionFile, n);
            n = (byte)(n + 1);
        }
        return stringArray;
    }

    static void set(OptionFile optionFile, int n, String string) {
        if (string.length() <= 61 && string.length() > 0) {
            byte[] byArray;
            int n2 = 9449 + n * 84;
            byte[] byArray2 = new byte[63];
            try {
                byArray = string.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                byArray = new byte[61];
            }
            if (byArray.length <= 61) {
                System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
            } else {
                System.arraycopy(byArray, 0, byArray2, 0, 61);
            }
            byArray2[62] = 1;
            System.arraycopy(byArray2, 0, optionFile.data, n2, 63);
        }
    }

    static void importData(OptionFile optionFile, OptionFile optionFile2) {
        System.arraycopy(optionFile.data, 9449, optionFile2.data, 9449, 2352);
    }
}

