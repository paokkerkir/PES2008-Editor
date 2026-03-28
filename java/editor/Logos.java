/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Logos {
    static final byte total = 80;
    static final int startAdr = 889896;
    static final int size = 608;
    static final int paletteSize = 16;
    static final int raster = 512;

    static boolean isUsed(OptionFile optionFile, int n) {
        boolean bl = false;
        if (optionFile.data[889896 + n * 608] == 1) {
            bl = true;
        }
        return bl;
    }

    static BufferedImage get(OptionFile optionFile, int n, boolean bl) {
        byte[] byArray = new byte[16];
        byte[] byArray2 = new byte[16];
        byte[] byArray3 = new byte[16];
        byte[] byArray4 = new byte[16];
        byte[] byArray5 = new byte[512];
        if (n >= 0 && n < 80 && Logos.isUsed(optionFile, n)) {
            int n2;
            int n3 = 0;
            while (n3 < 16) {
                n2 = 889928 + n * 608 + n3 * 4;
                byArray[n3] = optionFile.data[n2];
                byArray2[n3] = optionFile.data[n2 + 1];
                byArray3[n3] = optionFile.data[n2 + 2];
                byArray4[n3] = optionFile.data[n2 + 3];
                ++n3;
            }
            n2 = 889992 + n * 608;
            System.arraycopy(optionFile.data, n2, byArray5, 0, 512);
            if (bl) {
                n3 = 0;
                while (n3 < 16) {
                    byArray4[n3] = -1;
                    ++n3;
                }
            }
        }
        IndexColorModel indexColorModel = new IndexColorModel(4, 16, byArray, byArray2, byArray3, byArray4);
        DataBufferByte dataBufferByte = new DataBufferByte(byArray5, 512, 0);
        MultiPixelPackedSampleModel multiPixelPackedSampleModel = new MultiPixelPackedSampleModel(0, 32, 32, 4);
        WritableRaster writableRaster = Raster.createWritableRaster(multiPixelPackedSampleModel, dataBufferByte, null);
        BufferedImage bufferedImage = new BufferedImage(indexColorModel, writableRaster, false, null);
        return bufferedImage;
    }

    static boolean set(OptionFile optionFile, int n, BufferedImage bufferedImage) {
        boolean bl = false;
        try {
            byte[] byArray = new byte[256];
            byte[] byArray2 = new byte[256];
            byte[] byArray3 = new byte[256];
            byte[] byArray4 = new byte[256];
            int[] nArray = new int[1024];
            Raster raster = bufferedImage.getData();
            ColorModel colorModel = bufferedImage.getColorModel();
            if (colorModel instanceof IndexColorModel) {
                IndexColorModel indexColorModel = (IndexColorModel)colorModel;
                if (bufferedImage.getWidth() == 32 && bufferedImage.getHeight() == 32) {
                    int n2;
                    int n3;
                    raster.getPixels(0, 0, 32, 32, nArray);
                    indexColorModel.getReds(byArray);
                    indexColorModel.getGreens(byArray2);
                    indexColorModel.getBlues(byArray3);
                    indexColorModel.getAlphas(byArray4);
                    if (byArray4[0] != 0) {
                        n3 = 0;
                        int n4 = 1;
                        while (n4 < 16) {
                            if (n3 == 0 && byArray4[n4] == 0) {
                                n3 = n4;
                            }
                            ++n4;
                        }
                        if (n3 != 0) {
                            byte by = byArray[0];
                            byte by2 = byArray2[0];
                            byte by3 = byArray3[0];
                            byte by4 = byArray4[0];
                            byArray[0] = byArray[n3];
                            byArray2[0] = byArray2[n3];
                            byArray3[0] = byArray3[n3];
                            byArray4[0] = 0;
                            byArray[n3] = by;
                            byArray2[n3] = by2;
                            byArray3[n3] = by3;
                            byArray4[n3] = by4;
                            n4 = 0;
                            while (n4 < 1024) {
                                if (nArray[n4] == 0) {
                                    nArray[n4] = n3;
                                } else if (nArray[n4] == n3) {
                                    nArray[n4] = 0;
                                }
                                ++n4;
                            }
                        }
                    }
                    n3 = 0;
                    while (n3 < 16) {
                        n2 = 889928 + n * 608 + n3 * 4;
                        optionFile.data[n2] = byArray[n3];
                        optionFile.data[n2 + 1] = byArray2[n3];
                        optionFile.data[n2 + 2] = byArray3[n3];
                        optionFile.data[n2 + 3] = byArray4[n3];
                        ++n3;
                    }
                    n3 = 0;
                    while (n3 < 1024) {
                        n2 = 889992 + n * 608 + n3 / 2;
                        optionFile.data[n2] = optionFile.toByte(nArray[n3] << 4 | nArray[n3 + 1]);
                        n3 += 2;
                    }
                    optionFile.data[889896 + n * 608] = 1;
                    bl = true;
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return bl;
    }

    static void importLogo(OptionFile optionFile, int n, OptionFile optionFile2, int n2) {
        int n3 = 889896 + n * 608;
        int n4 = 889896 + n2 * 608;
        System.arraycopy(optionFile.data, n3, optionFile2.data, n4, 608);
    }

    static void delete(OptionFile optionFile, byte by) {
        int n = 889896 + by * 608;
        int n2 = 0;
        while (n2 < 608) {
            optionFile.data[n + n2] = 0;
            ++n2;
        }
    }
}

