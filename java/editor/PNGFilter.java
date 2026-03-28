/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.PESUtils;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class PNGFilter
extends FileFilter {
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String string = PESUtils.getExtension(file);
        if (string != null) {
            return string.equals("png");
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "PNG";
    }
}

