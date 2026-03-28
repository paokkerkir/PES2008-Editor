/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import java.io.UnsupportedEncodingException;

public class Stadia {
    static final byte total = 35;
    static final int nameAdr = 7196;
    static final byte maxLen = 61;
    static final int switchAdr = 9331;

    static String get(OptionFile optionFile, int n) {
        String string;
        int n2 = 7196 + n * 61;
        int n3 = 0;
        if (optionFile.data[n2] != 0) {
            int n4 = 0;
            while (n3 == 0 && n4 < 61) {
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
        } else {
            string = "<" + String.valueOf(n) + ">";
        }
        return string;
    }

    static String[] get(OptionFile optionFile) {
        String[] stringArray = new String[35];
        int n = 0;
        while (n < 35) {
            stringArray[n] = Stadia.get(optionFile, n);
            n = (byte)(n + 1);
        }
        return stringArray;
    }

    static void set(OptionFile optionFile, int n, String string) {
        if (string.length() < 61 && string.length() > 0) {
            byte[] byArray;
            int n2 = 7196 + n * 61;
            int n3 = 9331 + n;
            byte[] byArray2 = new byte[61];
            try {
                byArray = string.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                byArray = new byte[60];
            }
            if (byArray.length <= 60) {
                System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
            } else {
                System.arraycopy(byArray, 0, byArray2, 0, 60);
            }
            System.arraycopy(byArray2, 0, optionFile.data, n2, 61);
            optionFile.data[n3] = 1;
        }
    }

    static void importData(OptionFile optionFile, OptionFile optionFile2) {
        System.arraycopy(optionFile.data, 7196, optionFile2.data, 7196, 2253);
    }
}

