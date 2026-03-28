/*
 * Decompiled with CFR 0.151.
 */
package editor;

import java.io.File;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class PESUtils {
    public static final String xps = "xps";
    public static final String psu = "psu";
    public static final String max = "max";
    public static final String png = "png";
    public static final String gif = "gif";
    public static final String csv = "csv";
    public static final String[] extraSquad = new String[]{"Classic England", "Classic France", "Classic Germany", "Classic Italy", "Classic Netherlands", "Classic Argentina", "Classic Brazil", "<Japan 1>", "<Edited> National 1", "<Edited> National 2", "<Edited> National 3", "<Edited> National 4", "<Edited> National 5", "<Edited> National 6", "<Edited> National 7", "<Edited>", "<ML Default>", "<Shop 1>", "<Shop 2>", "<Shop 3>", "<Shop 4>", "<Shop 5>"};

    public static String getExtension(File file) {
        String string = null;
        String string2 = file.getName();
        int n = string2.lastIndexOf(46);
        if (n > 0 && n < string2.length() - 1) {
            string = string2.substring(n + 1).toLowerCase();
        }
        return string;
    }

    public static int swabInt(int n) {
        return n >>> 24 | n << 24 | n << 8 & 0xFF0000 | n >> 8 & 0xFF00;
    }

    public static void javaUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        catch (InstantiationException instantiationException) {
            instantiationException.printStackTrace();
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
            unsupportedLookAndFeelException.printStackTrace();
        }
    }

    public static void systemUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        catch (InstantiationException instantiationException) {
            instantiationException.printStackTrace();
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
            unsupportedLookAndFeelException.printStackTrace();
        }
    }
}

