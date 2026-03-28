/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import java.awt.Color;
import java.io.UnsupportedEncodingException;

public class Clubs {
    static final int total = 148;
    static final int startAdr = 773820;
    static final int size = 88;
    static final int firstFlag = 307;
    static final int firstDefEmblem = 122;

    static int getEmblem(OptionFile optionFile, int n) {
        int n2 = 773880 + 88 * n;
        byte[] byArray = new byte[2];
        System.arraycopy(optionFile.data, n2, byArray, 0, 2);
        return optionFile.toInt(byArray[1]) << 8 | optionFile.toInt(byArray[0]) & 0xFF;
    }

    static void importClub(OptionFile optionFile, int n, OptionFile optionFile2, int n2) {
        int n3 = 773820 + 88 * n;
        int n4 = 773820 + 88 * n2;
        byte by = optionFile.data[n3 + 54];
        System.arraycopy(optionFile2.data, n4, optionFile.data, n3, 88);
        optionFile.data[n3 + 54] = by;
        Clubs.setNameEdited(optionFile, n);
        Clubs.setEmblemEdited(optionFile, n, true);
        Clubs.setStadiumEdited(optionFile, n);
    }

    static void setEmblem(OptionFile optionFile, int n, byte[] byArray) {
        boolean bl = true;
        if (byArray == null) {
            byArray = optionFile.getBytes(n + 122);
            bl = false;
        }
        int n2 = 773880 + 88 * n;
        System.arraycopy(byArray, 0, optionFile.data, n2, 2);
        System.arraycopy(byArray, 0, optionFile.data, n2 + 4, 2);
        Clubs.setEmblemEdited(optionFile, n, bl);
    }

    static void unAssEmblem(OptionFile optionFile, int n) {
        int n2 = 0;
        while (n2 < 148) {
            if (n == Clubs.getEmblem(optionFile, n2) - 307) {
                Clubs.setEmblem(optionFile, n2, null);
            }
            ++n2;
        }
    }

    static String[] getNames(OptionFile optionFile) {
        String[] stringArray = new String[148];
        int n = 0;
        while (n < stringArray.length) {
            stringArray[n] = Clubs.getName(optionFile, n);
            ++n;
        }
        return stringArray;
    }

    static String getName(OptionFile optionFile, int n) {
        String string = "";
        int n2 = 0;
        int n3 = 773820 + n * 88;
        if (optionFile.data[n3] != 0) {
            int n4 = 0;
            while (n4 < 49) {
                if (n2 == 0 && optionFile.data[n3 + n4] == 0) {
                    n2 = n4;
                }
                ++n4;
            }
            try {
                string = new String(optionFile.data, n3, n2, "UTF-8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                string = "<" + String.valueOf(n) + ">";
            }
        } else {
            string = "<" + String.valueOf(n) + ">";
        }
        return string;
    }

    static String getAbv(OptionFile optionFile, int n) {
        String string = "";
        int n2 = 773869 + n * 88;
        string = new String(optionFile.data, n2, 3);
        return string;
    }

    static void setAbv(OptionFile optionFile, int n, String string) {
        int n2 = 773869 + n * 88;
        byte[] byArray = new byte[3];
        byte[] byArray2 = string.getBytes();
        System.arraycopy(byArray2, 0, byArray, 0, 3);
        System.arraycopy(byArray, 0, optionFile.data, n2, 3);
        Clubs.setNameEdited(optionFile, n);
    }

    static void setName(OptionFile optionFile, int n, String string) {
        byte[] byArray;
        int n2 = 773820 + n * 88;
        byte[] byArray2 = new byte[49];
        try {
            byArray = string.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            byArray = new byte[48];
        }
        if (byArray.length <= 48) {
            System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        } else {
            System.arraycopy(byArray, 0, byArray2, 0, 48);
        }
        System.arraycopy(byArray2, 0, optionFile.data, n2, 49);
        Clubs.setNameEdited(optionFile, n);
    }

    static int getStadium(OptionFile optionFile, int n) {
        int n2 = 773901 + n * 88;
        return optionFile.data[n2];
    }

    static void setStadium(OptionFile optionFile, int n, int n2) {
        int n3 = 773901 + n * 88;
        optionFile.data[n3] = optionFile.toByte(n2);
        Clubs.setStadiumEdited(optionFile, n);
    }

    static byte getBack(OptionFile optionFile, int n) {
        int n2 = 773890 + n * 88;
        return optionFile.data[n2];
    }

    static void setBack(OptionFile optionFile, int n, int n2) {
        int n3 = 773890 + n * 88;
        optionFile.data[n3] = optionFile.toByte(n2);
    }

    static byte[] getRed(OptionFile optionFile, int n) {
        int n2 = 773892 + n * 88;
        byte[] byArray = new byte[]{optionFile.data[n2], optionFile.data[n2 + 4]};
        return byArray;
    }

    static byte[] getGreen(OptionFile optionFile, int n) {
        int n2 = 773893 + n * 88;
        byte[] byArray = new byte[]{optionFile.data[n2], optionFile.data[n2 + 4]};
        return byArray;
    }

    static byte[] getBlue(OptionFile optionFile, int n) {
        int n2 = 773894 + n * 88;
        byte[] byArray = new byte[]{optionFile.data[n2], optionFile.data[n2 + 4]};
        return byArray;
    }

    static Color getColor(OptionFile optionFile, int n, boolean bl) {
        int n2 = 773892 + n * 88;
        if (bl) {
            n2 += 4;
        }
        int n3 = optionFile.toInt(optionFile.data[n2]);
        int n4 = optionFile.toInt(optionFile.data[n2 + 1]);
        int n5 = optionFile.toInt(optionFile.data[n2 + 2]);
        return new Color(n3, n4, n5);
    }

    static void setColor(OptionFile optionFile, int n, boolean bl, Color color) {
        int n2 = 773892 + n * 88;
        if (bl) {
            n2 += 4;
        }
        byte by = (byte)color.getRed();
        byte by2 = (byte)color.getGreen();
        byte by3 = (byte)color.getBlue();
        optionFile.data[n2] = by;
        optionFile.data[n2 + 1] = by2;
        optionFile.data[n2 + 2] = by3;
    }

    static void importNames(OptionFile optionFile, OptionFile optionFile2) {
        int n = 0;
        while (n < 148) {
            System.arraycopy(optionFile2.data, 773820 + n * 88, optionFile.data, 773820 + n * 88, 57);
            ++n;
        }
    }

    static void importData(OptionFile optionFile, OptionFile optionFile2) {
        int n = 0;
        while (n < 148) {
            System.arraycopy(optionFile2.data, 773820 + n * 88 + 57, optionFile.data, 773820 + n * 88 + 57, 31);
            ++n;
        }
    }

    static void setNameEdited(OptionFile optionFile, int n) {
        int n2 = 773820 + n * 88 + 56;
        optionFile.data[n2] = 1;
    }

    static void setEmblemEdited(OptionFile optionFile, int n, boolean bl) {
        byte by = 0;
        if (bl) {
            by = 1;
        }
        int n2 = 773888 + 88 * n;
        optionFile.data[n2] = by;
        optionFile.data[n2 + 1] = by;
    }

    static void setStadiumEdited(OptionFile optionFile, int n) {
        int n2 = 773903 + n * 88;
        optionFile.data[n2] = 1;
    }
}

