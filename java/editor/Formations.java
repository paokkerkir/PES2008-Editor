/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;

public class Formations {
    static final int startAdr = 696638;
    static final int size = 364;
    static final int abcSize = 82;

    static byte getPos(OptionFile optionFile, int n, int n2, int n3) {
        return optionFile.data[696776 + 364 * n + n2 * 82 + n3];
    }

    static void setPos(OptionFile optionFile, int n, int n2, int n3, int n4) {
        optionFile.data[696776 + 364 * n + n2 * 82 + n3] = (byte)n4;
    }

    static byte getSlot(OptionFile optionFile, int n, int n2) {
        return optionFile.data[696644 + 364 * n + n2];
    }

    static void setSlot(OptionFile optionFile, int n, int n2, byte by) {
        optionFile.data[696644 + 364 * n + n2] = by;
    }

    static byte getJob(OptionFile optionFile, int n, int n2) {
        return optionFile.data[696749 + 364 * n + n2];
    }

    static void setJob(OptionFile optionFile, int n, int n2, byte by) {
        optionFile.data[696749 + 364 * n + n2] = by;
    }

    static byte getX(OptionFile optionFile, int n, int n2, int n3) {
        return optionFile.data[696756 + 364 * n + n2 * 82 + 2 * (n3 - 1)];
    }

    static byte getY(OptionFile optionFile, int n, int n2, int n3) {
        return optionFile.data[696757 + 364 * n + n2 * 82 + 2 * (n3 - 1)];
    }

    static void setX(OptionFile optionFile, int n, int n2, int n3, int n4) {
        optionFile.data[696756 + 364 * n + n2 * 82 + 2 * (n3 - 1)] = (byte)n4;
    }

    static void setY(OptionFile optionFile, int n, int n2, int n3, int n4) {
        optionFile.data[696757 + 364 * n + n2 * 82 + 2 * (n3 - 1)] = (byte)n4;
    }

    static boolean getAtk(OptionFile optionFile, int n, int n2, int n3, int n4) {
        boolean bl = false;
        byte by = optionFile.data[696787 + 364 * n + n2 * 82 + n3];
        if ((by >>> n4 & 1) == 1) {
            bl = true;
        }
        return bl;
    }

    static void setAtk(OptionFile optionFile, int n, int n2, int n3, int n4) {
        if (n4 < 0) {
            optionFile.data[696787 + 364 * n + n2 * 82 + n3] = 0;
        } else {
            int n5 = optionFile.data[696787 + 364 * n + n2 * 82 + n3];
            optionFile.data[696787 + 364 * n + n2 * 82 + n3] = (byte)(n5 ^= 1 << n4);
        }
    }

    static byte getDef(OptionFile optionFile, int n, int n2, int n3) {
        return optionFile.data[696798 + 364 * n + n2 * 82 + n3];
    }

    static void setDef(OptionFile optionFile, int n, int n2, int n3, int n4) {
        optionFile.data[696798 + 364 * n + n2 * 82 + n3] = (byte)n4;
    }

    static byte getStrat(OptionFile optionFile, int n, int n2) {
        return optionFile.data[696740 + 364 * n + n2];
    }

    static void setStrat(OptionFile optionFile, int n, int n2, int n3) {
        optionFile.data[696740 + 364 * n + n2] = (byte)n3;
    }

    static byte getStratOlCB(OptionFile optionFile, int n) {
        return optionFile.data[696744 + 364 * n];
    }

    static void setStratOlCB(OptionFile optionFile, int n, int n2) {
        optionFile.data[696744 + 364 * n] = (byte)n2;
    }

    static boolean getStratAuto(OptionFile optionFile, int n) {
        boolean bl = false;
        if (optionFile.data[696745 + 364 * n] == 1) {
            bl = true;
        }
        return bl;
    }

    static void setStratAuto(OptionFile optionFile, int n, boolean bl) {
        byte by = 0;
        if (bl) {
            by = 1;
        }
        optionFile.data[696745 + 364 * n] = by;
    }

    static byte getTeam(OptionFile optionFile, int n, int n2, int n3) {
        return optionFile.data[696832 + 364 * n + n2 * 82 + n3];
    }

    static void setTeam(OptionFile optionFile, int n, int n2, int n3, int n4) {
        optionFile.data[696832 + 364 * n + n2 * 82 + n3] = (byte)n4;
    }
}

