/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;

public class Kits {
    static final int totalN = 64;
    static final int startAdrN = 786856;
    static final int sizeN = 352;
    static final int startAdrC = 809384;
    static final int sizeC = 544;

    static boolean logoUsed(OptionFile optionFile, int n, int n2) {
        int n3 = 809640 + 544 * n + n2 * 24 + 2;
        if (n >= 148) {
            n3 = 787112 + 352 * (n -= 148) + n2 * 24 + 2;
        }
        return optionFile.data[n3] == 1;
    }

    static byte getLogo(OptionFile optionFile, int n, int n2) {
        int n3 = 809640 + 544 * n + n2 * 24 + 3;
        if (n >= 148) {
            n3 = 787112 + 352 * (n -= 148) + n2 * 24 + 3;
        }
        return optionFile.data[n3];
    }

    static void setLogo(OptionFile optionFile, int n, int n2, byte by) {
        int n3 = 809640 + 544 * n + n2 * 24 + 3;
        if (n >= 148) {
            n3 = 787112 + 352 * (n -= 148) + n2 * 24 + 3;
        }
        optionFile.data[n3] = by;
    }

    static void setLogoUnused(OptionFile optionFile, int n, int n2) {
        int n3 = 809640 + 544 * n + n2 * 24 + 2;
        if (n >= 148) {
            n3 = 787112 + 352 * (n -= 148) + n2 * 24 + 2;
        }
        optionFile.data[n3] = 0;
        optionFile.data[n3 + 1] = 88;
    }

    static void importKit(OptionFile optionFile, int n, OptionFile optionFile2, int n2) {
        int n3 = 809384 + 544 * n;
        int n4 = 809384 + 544 * n2;
        int n5 = 544;
        if (n >= 148) {
            n3 = 786856 + 352 * (n -= 148);
            n4 = 786856 + 352 * (n2 -= 148);
            n5 = 352;
        }
        System.arraycopy(optionFile2.data, n4, optionFile.data, n3, n5);
    }

    static boolean isLic(OptionFile optionFile, int n) {
        int n2 = 809442 + 544 * n;
        if (n >= 148) {
            n2 = 786914 + 352 * (n -= 148);
        }
        return optionFile.data[n2] == 1;
    }

    static byte getKitModel(OptionFile optionFile, int n, int n2) {
        int n3 = 809444 + 544 * n + n2 * 62;
        if (n >= 148) {
            n3 = 786916 + 352 * (n -= 148) + n2 * 62;
        }
        return optionFile.data[n3];
    }

    static void setKitModel(OptionFile optionFile, int n, int n2, byte by) {
        int n3 = 809444 + 544 * n + n2 * 62;
        if (n >= 148) {
            n3 = 786916 + 352 * (n -= 148) + n2 * 62;
        }
        optionFile.data[n3] = by;
    }
}

