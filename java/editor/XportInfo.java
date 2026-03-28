/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.PESUtils;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class XportInfo {
    public String gameName = new String("");
    public String saveName = new String("");
    public String notes = new String("");
    public String game = new String("");

    public boolean getInfo(File file) {
        if (file.isFile()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                this.gameName = "";
                this.saveName = "";
                this.notes = "";
                this.game = "";
                String string = PESUtils.getExtension(file);
                if (string.equals("xps")) {
                    randomAccessFile.seek(21L);
                    int n = PESUtils.swabInt(randomAccessFile.readInt());
                    byte[] byArray = new byte[n];
                    randomAccessFile.read(byArray);
                    this.gameName = new String(byArray);
                    n = PESUtils.swabInt(randomAccessFile.readInt());
                    byArray = new byte[n];
                    randomAccessFile.read(byArray);
                    this.saveName = new String(byArray);
                    n = PESUtils.swabInt(randomAccessFile.readInt());
                    byArray = new byte[n];
                    randomAccessFile.read(byArray);
                    this.notes = new String(byArray);
                    randomAccessFile.skipBytes(6);
                    byArray = new byte[19];
                    randomAccessFile.read(byArray);
                    this.game = new String(byArray);
                } else if (string.equals("max")) {
                    randomAccessFile.seek(16L);
                    byte[] byArray = new byte[19];
                    randomAccessFile.read(byArray);
                    this.game = new String(byArray);
                    randomAccessFile.seek(48L);
                    byArray = new byte[32];
                    randomAccessFile.read(byArray);
                    this.gameName = new String(byArray);
                    this.gameName = this.gameName.replaceAll("\u0000", "");
                } else {
                    randomAccessFile.seek(64L);
                    byte[] byArray = new byte[19];
                    randomAccessFile.read(byArray);
                    this.game = new String(byArray);
                }
                randomAccessFile.close();
                return true;
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return false;
    }
}

