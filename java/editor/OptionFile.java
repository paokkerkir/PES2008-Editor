/*
 * Decompiled with CFR 0.151.
 * 
 * MODIFIED TO ADD PES6 PC SUPPORT:
 * - Added import for PES6ToPES2008Converter
 * - Added PC encryption key (keyPC) for PES6 PC files
 * - Added isPES6File() detection method
 * - Added convertPCData() method for XOR decryption
 * - Added handling for files with no extension (PC format)
 * - Added automatic conversion in readXPS() method
 * 
 * Changes are clearly marked with "// ADDED FOR PES6 SUPPORT" comments
 */
package editor;

import editor.Lzari;
import editor.PESUtils;
import editor.PES6ToPES2008Converter;  // ADDED FOR PES6 SUPPORT
import java.io.File;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;

public class OptionFile {
    public static final int LENGTH = 0x111000;
    public byte[] data = new byte[0x111000];
    private byte[] headerData;
    public String fileName;
    public String gameID;
    private static final byte[] sharkport;
    private static final String magicMax = "Ps2PowerSave";
    String gameName;
    String saveName;
    String notes;
    static final int[] block;
    static final int[] blockSize;
    private static final int[] key;
    
    // ADDED FOR PES6 SUPPORT - PC encryption key
    private static final byte[] keyPC = { 115, 96, -31, -58, 31, 60, -83, 66,
            11, 88, -71, -2, 55, -76, 5, -6, -93, 80, -111, 54, 79, 44, 93,
            -78, 59, 72, 105, 110, 103, -92, -75, 106, -45, 64, 65, -90, 127,
            28, 13, 34, 107, 56, 25, -34, -105, -108, 101, -38, 3, 48, -15, 22,
            -81, 12, -67, -110, -101, 40, -55, 78, -57, -124, 21, 74, 51, 32,
            -95, -122, -33, -4, 109, 2, -53, 24, 121, -66, -9, 116, -59, -70,
            99, 16, 81, -10, 15, -20, 29, 114, -5, 8, 41, 46, 39, 100, 117, 42,
            -109, 0, 1, 102, 63, -36, -51, -30, 43, -8, -39, -98, 87, 84, 37,
            -102, -61, -16, -79, -42, 111, -52, 125, 82, 91, -24, -119, 14,
            -121, 68, -43, 10, -13, -32, 97, 70, -97, -68, 45, -62, -117, -40,
            57, 126, -73, 52, -123, 122, 35, -48, 17, -74, -49, -84, -35, 50,
            -69, -56, -23, -18, -25, 36, 53, -22, 83, -64, -63, 38, -1, -100,
            -115, -94, -21, -72, -103, 94, 23, 20, -27, 90, -125, -80, 113,
            -106, 47, -116, 61, 18, 27, -88, 73, -50, 71, 4, -107, -54, -77,
            -96, 33, 6, 95, 124, -19, -126, 75, -104, -7, 62, 119, -12, 69, 58,
            -29, -112, -47, 118, -113, 108, -99, -14, 123, -120, -87, -82, -89,
            -28, -11, -86, 19, -128, -127, -26, -65, 92, 77, 98, -85, 120, 89,
            30, -41, -44, -91, 26, 67, 112, 49, 86, -17, 76, -3, -46, -37, 104,
            9, -114, 7, -60, 85, -118 };
    
    byte format = (byte)-1;
    int fileCount;

    static {
        byte[] byArray = new byte[21];
        byArray[0] = 13;
        byArray[4] = 83;
        byArray[5] = 104;
        byArray[6] = 97;
        byArray[7] = 114;
        byArray[8] = 107;
        byArray[9] = 80;
        byArray[10] = 111;
        byArray[11] = 114;
        byArray[12] = 116;
        byArray[13] = 83;
        byArray[14] = 97;
        byArray[15] = 118;
        byArray[16] = 101;
        sharkport = byArray;
        block = new int[]{12, 5144, 7196, 11876, 34704, 676624, 773820, 786856, 938548, 1094184, 1096380};
        blockSize = new int[]{4852, 1884, 4668, 22816, 641080, 97181, 13024, 151680, 155624, 2184, 21740};
        key = new int[]{2058578050, 2058578078, 2058578109, 2058578079, 2058578084, 2058578115, 2058578073, 2058578105, 2058578068, 2058578101, 2058578095, 2058578045, 2058578100, 2058578111, 2058578096, 2058578068, 2058578101, 2058578117, 2058578115, 2058578071, 2058578064, 2058578045, 2058578078, 2058578085, 2058578062, 2058578116, 2058578109, 2058578045, 2058578115, 2058578076, 2058578049, 2058578093, 2058578066, 2058578051, 2058578082, 2058578114, 2058578045, 2058578093, 2058578052, 2058578112, 2058578073, 2058578063, 2058578100, 2058578102, 2058578103, 2058578053, 2058578085, 2058578078, 2058578077, 2058578115, 2058578076, 2058578086, 2058578116, 2058578111, 2058578083, 2058578109, 2058578072, 2058578047, 2058578081, 2058578049, 2058578074, 2058578048, 2058578086, 2058578110, 2058578098, 2058578102, 2058578105, 2058578050, 2058578046, 2058578086, 2058578095, 2058578083, 2058578065, 2058578062, 2058578047, 2058578116, 2058578109, 2058578100, 2058578068, 2058578100, 2058578109, 2058578104, 2058578079, 2058578084, 2058578084, 2058578083, 2058578084, 2058578098, 2058578096, 2058578070, 2058578068, 2058578110, 2058578094, 2058578045, 2058578114, 2058578082, 2058578116, 2058578068, 2058578114, 2058578097, 2058578085, 2058578115, 2058578072, 2058578068, 2058578047, 2058578099, 2058578076, 2058578101, 2058578086, 2058578117, 2058578052, 2058578109, 2058578070, 2058578050, 2058578118, 2058578046, 2058578109, 2058578098, 2058578099, 2058578064, 2058578048, 2058578103, 2058578069, 2058578075, 2058578068, 2058578085, 2058578110, 2058578111, 2058578114, 2058578110, 2058578081, 2058578084, 2058578077, 2058578073, 2058578084, 2058578100, 2058578104, 2058578063, 2058578083, 2058578049, 2058578065, 2058578109, 2058578105, 2058578099, 2058578105, 2058578062, 2058578069, 2058578070, 2058578065, 2058578066, 2058578047, 2058578100, 2058578107, 2058578077, 2058578062, 2058578050, 2058578113, 2058578080, 2058578065, 2058578083, 2058578095, 2058578111, 2058578096, 2058578044, 2058578116, 2058578053, 2058578084, 2058578077, 2058578118, 2058578100, 2058578072, 2058578044, 2058578073, 2058578104, 2058578117, 2058578074, 2058578069, 2058578110, 2058578050, 2058578045, 2058578045, 2058578047, 2058578047, 2058578106, 2058578064, 2058578099, 2058578095, 2058578063, 2058578067, 2058578068, 2058578049, 2058578108, 2058578098, 2058578115, 2058578099, 2058578097, 2058578106, 2058578097, 2058578116, 2058578116, 2058578110, 2058578118, 2058578099, 2058578111, 2058578106, 2058578109, 2058578101, 2058578093, 2058578077, 2058578053, 2058578061, 2058578098, 2058578050, 2058578086, 2058578104, 2058578098, 2058578113, 2058578102, 2058578065, 2058578077, 2058578082, 2058578044, 2058578050, 2058578085, 2058578117, 2058578045, 2058578117, 2058578113, 2058578082, 2058578051, 2058578110, 2058578103, 2058578096, 2058578069, 2058578052, 2058578114, 2058578046, 2058578044, 2058578047, 2058578108, 2058578083, 2058578075, 2058578077, 2058578069, 2058578050, 2058578101, 2058578063, 2058578082, 2058578052, 2058578108, 2058578106, 2058578109, 2058578112, 2058578062, 2058578071, 2058578051, 2058578047, 2058578097, 2058578062, 2058578100, 2058578048, 2058578080, 2058578080, 2058578077, 2058578047, 2058578048, 2058578096, 2058578100, 2058578118, 2058578105, 2058578096, 2058578072, 2058578085, 2058578084, 2058578061, 2058578114, 2058578044, 2058578049, 2058578053, 2058578093, 2058578064, 2058578049, 2058578083, 2058578069, 2058578073, 2058578104, 2058578080, 2058578098, 2058578103, 2058578093, 2058578049, 2058578044, 2058578099, 2058578094, 2058578070, 2058578103, 2058578070, 2058578062, 2058578078, 2058578102, 2058578104, 2058578109, 2058578068, 2058578067, 2058578108, 2058578108, 2058578076, 2058578086, 2058578053, 2058578104, 2058578093, 2058578070, 2058578105, 2058578110, 2058578094, 2058578112, 2058578086, 2058578049, 2058578101, 2058578086, 2058578108, 2058578071, 2058578095, 2058578079, 2058578097, 2058578116, 2058578111, 2058578046, 2058578103, 2058578071, 2058578067, 2058578063, 2058578096, 2058578048, 2058578079, 2058578103, 2058578068, 2058578114, 2058578079, 2058578072, 2058578102, 2058578115, 2058578053, 2058578047, 2058578084, 2058578046, 2058578110, 2058578044, 2058578108, 2058578101, 2058578078, 2058578073, 2058578086, 2058578049, 2058578107, 2058578069, 2058578077, 2058578086, 2058578079, 2058578110, 2058578048, 2058578116, 2058578101, 2058578108, 2058578081, 2058578093, 2058578113, 2058578065, 2058578045, 2058578080, 2058578109, 2058578075, 2058578097, 2058578071, 2058578049, 2058578053, 2058578078, 2058578050, 2058578075, 2058578067, 2058578083, 2058578061, 2058578116, 2058578116, 2058578075, 2058578093, 2058578116, 2058578100, 2058578093, 2058578052, 2058578085, 2058578047, 2058578095, 2058578081, 2058578045, 2058578044, 2058578101, 2058578097, 2058578110, 2058578115, 2058578096, 2058578069, 2058578053, 2058578050, 2058578112, 2058578085, 2058578104, 2058578082, 2058578073, 2058578099, 2058578081, 2058578045, 2058578079, 2058578071, 2058578080, 2058578047, 2058578113, 2058578076, 2058578082, 2058578117, 2058578086, 2058578046, 2058578099, 2058578068, 2058578074, 2058578108, 2058578064, 2058578077, 2058578115, 2058578066, 2058578074, 2058578104, 2058578082, 2058578115, 2058578117, 2058578082, 2058578117, 2058578048, 2058578053, 2058578107, 2058578079, 2058578116, 2058578081, 2058578086, 2058578064, 2058577996};
    }

    // ADDED FOR PES6 SUPPORT - START
    /**
     * Detect if this is a PES6 PC option file.
     * PES6 PC files have player data at offset 37116 instead of 34704.
     */
    private boolean isPES6File() {
        // Check gameID first
        if (gameID != null) {
            if (gameID.equals("PC_WE") || gameID.equals("PC_PES")) {
                System.out.println("PES6 detected: gameID = " + gameID);
                return true;
            }
            if (gameID.startsWith("BESLES") && gameID.contains("PES6")) {
                System.out.println("PES6 detected: gameID contains PES6");
                return true;
            }
        }
        
        // Check for player data at PES6 location (offset 37116)
        if (data.length > 37116 + 32) {
            int nonZeroBytes = 0;
            for (int i = 37116; i < 37116 + 32; i++) {
                if (data[i] != 0) {
                    nonZeroBytes++;
                }
            }
            
            int pes2008NonZero = 0;
            for (int i = 34704; i < 34704 + 32; i++) {
                if (data[i] != 0) {
                    pes2008NonZero++;
                }
            }
            
            if (nonZeroBytes > 10 && pes2008NonZero < 5) {
                System.out.println("PES6 detected: Player data at PES6 offset (37116)");
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * XOR decrypt PES6 PC data
     */
    private void convertPCData() {
        int k = 0;
        // PES6 files are larger, but we only decrypt the PES2008 size
        for (int a = 0; a < LENGTH; a++) {
            data[a] = (byte) (data[a] ^ keyPC[k]);
            if (k < 255) {
                k++;
            } else {
                k = 0;
            }
        }
    }
    
    /**
     * XOR decrypt PES6 PC data in a larger array
     */
    private void convertPCDataLarge(byte[] data) {
        int k = 0;
        // Decrypt the full array
        for (int a = 0; a < data.length; a++) {
            data[a] = (byte) (data[a] ^ keyPC[k]);
            if (k < 255) {
                k++;
            } else {
                k = 0;
            }
        }
    }
    // ADDED FOR PES6 SUPPORT - END

    public boolean readXPS(File file) {
        this.format = (byte)-1;
        boolean bl = true;
        String string = PESUtils.getExtension(file);
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            if (string != null && string.equals("xps")) {
                long l = randomAccessFile.length() - 0x111000L - 4L;
                randomAccessFile.seek(21L);
                int n = PESUtils.swabInt(randomAccessFile.readInt());
                byte[] byArray = new byte[n];
                randomAccessFile.read(byArray);
                this.gameName = new String(byArray, "ISO-8859-1");
                n = PESUtils.swabInt(randomAccessFile.readInt());
                byArray = new byte[n];
                randomAccessFile.read(byArray);
                this.saveName = new String(byArray, "ISO-8859-1");
                n = PESUtils.swabInt(randomAccessFile.readInt());
                byArray = new byte[n];
                randomAccessFile.read(byArray);
                this.notes = new String(byArray, "ISO-8859-1");
                this.headerData = new byte[(int)l - 33 - this.gameName.length() - this.saveName.length() - this.notes.length()];
                randomAccessFile.read(this.headerData);
                this.gameID = new String(this.headerData, 6, 19);
                this.format = 0;
            } else if (string != null && string.equals("psu")) {
                this.headerData = new byte[(int)(randomAccessFile.length() - 0x111000L)];
                randomAccessFile.read(this.headerData);
                this.gameID = new String(this.headerData, 64, 19);
                this.format = (byte)2;
            } else if (string != null && string.equals("max")) {
                byte[] byArray = new byte[(int)randomAccessFile.length()];
                randomAccessFile.read(byArray);
                String string2 = new String(byArray, 0, 12, "ISO-8859-1");
                if (string2.equals(magicMax)) {
                    int n = this.byteToInt(byArray, 12);
                    byArray[12] = 0;
                    byArray[13] = 0;
                    byArray[14] = 0;
                    byArray[15] = 0;
                    CRC32 cRC32 = new CRC32();
                    cRC32.update(byArray);
                    if ((int)cRC32.getValue() == n) {
                        byArray = new byte[32];
                        randomAccessFile.seek(16L);
                        randomAccessFile.read(byArray);
                        this.gameID = new String(byArray, "ISO-8859-1");
                        this.gameID = this.gameID.replace("\u0000", "");
                        randomAccessFile.read(byArray);
                        this.gameName = new String(byArray, "ISO-8859-1");
                        this.gameName = this.gameName.replace("\u0000", "");
                        int n2 = PESUtils.swabInt(randomAccessFile.readInt());
                        this.fileCount = PESUtils.swabInt(randomAccessFile.readInt());
                        byArray = new byte[n2];
                        randomAccessFile.read(byArray);
                        Lzari lzari = new Lzari();
                        byArray = lzari.decodeLzari(byArray);
                        int n3 = 0;
                        int n4 = 0;
                        while (n4 < this.fileCount && this.format != 3) {
                            int n5 = this.byteToInt(byArray, n3);
                            String string3 = new String(byArray, n3 + 4, 19, "ISO-8859-1");
                            if (n5 == 0x111000 && string3.equals(this.gameID)) {
                                this.headerData = new byte[n3 += 36];
                                System.arraycopy(byArray, 0, this.headerData, 0, n3);
                                System.arraycopy(byArray, n3, this.data, 0, 0x111000);
                                this.format = (byte)3;
                            } else {
                                n3 = n3 + 36 + n5;
                                n3 = (n3 + 23) / 16 * 16 - 8;
                            }
                            ++n4;
                        }
                        if (this.format != 3) {
                            bl = false;
                        }
                    } else {
                        bl = false;
                    }
                } else {
                    bl = false;
                }
            } else {
                // ADDED FOR PES6 SUPPORT - Handle files with no extension (PC format)
                // Check filename for PC indicators
                if (file.getName().equals("KONAMI-WIN32WEXUOPT") || file.getName().contains("KONAMI")) {
                    this.gameID = "PC_WE";
                    this.format = 1;  // PC format
                    System.out.println("Detected PC format file: " + file.getName());
                } else {
                    // Default to PC format for files without extension
                    this.gameID = "PC_PES";
                    this.format = 1;
                    System.out.println("Assuming PC format for file: " + file.getName());
                }
            }
            if (bl && this.format != -1) {
                // MODIFIED: For PC format files, check file size and use appropriate array
                if (this.format == 1) {
                    long fileSize = randomAccessFile.length();
                    System.out.println("PC format file size: " + fileSize + " bytes");
                    
                    // PES6 PC files are 1,191,936 bytes, PES 2008 PS2 files are 1,118,208 bytes
                    if (fileSize > LENGTH) {
                        // This is likely a PES6 PC file - read into temporary larger array
                        byte[] tempData = new byte[(int)fileSize];
                        randomAccessFile.read(tempData);
                        
                        // Decrypt the full PES6 data WITH XOR
                        System.out.println("Decrypting PES6 PC format data (XOR)...");
                        this.convertPCDataLarge(tempData);
                        
                        // Apply PS2 decryption with PES6 block structure
                        System.out.println("Decrypting PES6 with PS2 algorithm (using PES6 blocks)...");
                        this.decryptPES6Data(tempData);
                        
                        // Now convert from decrypted PES6 to decrypted PES 2008 using block-based approach
                        System.out.println("Converting decrypted PES6 to PES 2008 format...");
                        try {
                            PES6ToPES2008Converter converter = new PES6ToPES2008Converter();
                            byte[] convertedData = converter.convert(tempData);
                            
                            if (converter.validate(convertedData)) {
                                // Copy converted data into this.data
                                // Data is now DECRYPTED and at PES 2008 offsets
                                System.arraycopy(convertedData, 0, this.data, 0, LENGTH);
                                
                                System.out.println("PES6 conversion successful!");
                            } else {
                                System.out.println("PES6 conversion validation failed!");
                                bl = false;
                            }
                        } catch (Exception e) {
                            System.out.println("PES6 conversion error: " + e.getMessage());
                            e.printStackTrace();
                            bl = false;
                        }
                    } else {
                        // Normal PES 2008 size PC format file
                        randomAccessFile.read(this.data);
                        System.out.println("Decrypting PC format data...");
                        this.convertPCData();
                    }
                } else if (this.format != 3) {
                    randomAccessFile.read(this.data);
                }
                
                // Decrypt PS2 format (skip if already handled PC format above)
                if (this.format != 1) {
                    this.decrypt();
                }
            }
            randomAccessFile.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            bl = false;
        }
        this.fileName = bl ? file.getName() : null;
        return bl;
    }

    public boolean saveXPS(File file) {
        this.data[49] = 1;
        this.data[50] = 1;
        this.data[5938] = 1;
        this.data[5939] = 1;
        boolean bl = true;
        this.encrypt();
        this.checkSums();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            if (this.format == 0) {
                this.saveName = file.getName();
                this.saveName = this.saveName.substring(0, this.saveName.length() - 4);
                randomAccessFile.write(sharkport);
                randomAccessFile.writeInt(PESUtils.swabInt(this.gameName.length()));
                randomAccessFile.writeBytes(this.gameName);
                randomAccessFile.writeInt(PESUtils.swabInt(this.saveName.length()));
                randomAccessFile.writeBytes(this.saveName);
                randomAccessFile.writeInt(PESUtils.swabInt(this.notes.length()));
                randomAccessFile.writeBytes(this.notes);
                randomAccessFile.write(this.headerData);
            }
            if (this.format == 2) {
                randomAccessFile.write(this.headerData);
            }
            if (this.format == 3) {
                int n = this.headerData.length + 0x111000;
                n = (n + 23) / 16 * 16 - 8;
                byte[] byArray = new byte[n];
                System.arraycopy(this.headerData, 0, byArray, 0, this.headerData.length);
                System.arraycopy(this.data, 0, byArray, this.headerData.length, this.data.length);
                Lzari lzari = new Lzari();
                byArray = lzari.encodeLzari(byArray);
                int n2 = byArray.length;
                byte[] byArray2 = new byte[88];
                System.arraycopy(magicMax.getBytes("ISO-8859-1"), 0, byArray2, 0, magicMax.length());
                System.arraycopy(this.gameID.getBytes("ISO-8859-1"), 0, byArray2, 16, 19);
                System.arraycopy(this.gameName.getBytes("ISO-8859-1"), 0, byArray2, 48, this.gameName.length());
                System.arraycopy(this.getBytesInt(n2), 0, byArray2, 80, 4);
                System.arraycopy(this.getBytesInt(this.fileCount), 0, byArray2, 84, 4);
                CRC32 cRC32 = new CRC32();
                cRC32.update(byArray2);
                cRC32.update(byArray);
                System.arraycopy(this.getBytesInt((int)cRC32.getValue()), 0, byArray2, 12, 4);
                randomAccessFile.write(byArray2);
                randomAccessFile.write(byArray);
            } else {
                randomAccessFile.write(this.data);
            }
            if (this.format == 0) {
                randomAccessFile.write(0);
                randomAccessFile.write(0);
                randomAccessFile.write(0);
                randomAccessFile.write(0);
            }
            randomAccessFile.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            bl = false;
        }
        this.decrypt();
        return bl;
    }

    public byte toByte(int n) {
        byte by = n > 127 ? (byte)(n - 256) : (byte)n;
        return by;
    }

    public int toInt(byte n) {
        int n2 = n;
        if (n2 < 0) {
            n2 += 256;
        }
        return n2;
    }

    /**
     * Fix unaligned squad slot data that gets scrambled during block decryption
     * Squad slot32 starts at 667730, which is unaligned (667730 % 4 = 2)
     */
    private void fixUnalignedSlotData(byte[] data) {
        int PES6_SLOT32_START = 667730;
        int PES6_SLOT32_SIZE = 9472;  // 148 teams * 64 bytes
        
        // The slot data was decrypted as part of overlapping 4-byte chunks
        // We need to re-decrypt it properly by treating it as starting at the aligned boundary
        // and then extracting the correct bytes
        
        // Actually, simpler approach: the decrypt already happened on the aligned 4-byte chunks
        // The issue is that 2-byte slot values span across chunks
        // Let's just re-read and re-decrypt the specific slot section
        
        // Find which 4-byte chunk we're in
        int blockStart = 667728; // Aligned start before slot data (667730 - 2)
        int keyOffset = (blockStart - 657956) / 4; // Offset within block 5
        
        // Re-decrypt the slot region with correct key alignment
        for (int offset = 0; offset < PES6_SLOT32_SIZE; offset += 4) {
            int addr = PES6_SLOT32_START + offset;
            if (addr + 4 <= data.length && addr >= PES6_SLOT32_START) {
                // Read the 4-byte chunk
                int c = this.byteToInt(data, addr);
                // Decrypt with correct key
                int k = (keyOffset + (offset / 4)) % 446;
                int p = ((c - key[k]) + 0x7ab3684c) ^ 0x7ab3684c;
                // Write back
                data[addr] = this.toByte(p & 0x000000FF);
                data[addr + 1] = this.toByte((p >>> 8) & 0x000000FF);
                data[addr + 2] = this.toByte((p >>> 16) & 0x000000FF);
                data[addr + 3] = this.toByte((p >>> 24) & 0x000000FF);
            }
        }
        
        System.out.println("  - Fixed unaligned slot data");
    }

    /**
     * Decrypt using PES6 block structure for PES6 files
     * This is the EXACT algorithm from PES6 editor OptionFile.java
     */
    private void decryptPES6Data(byte[] data) {
        // PES6 blocks - EXACT from PES6 editor source
        int[] blockPES6 = { 12, 5144, 9544, 14288, 37116, 657956, 751472, 763804, 911144, 1170520 };
        int[] blockSizePES6 = { 4844, 1268, 4730, 22816, 620000, 93501, 12320, 147328, 259364, 21032 };
        
        System.out.println("DEBUG: PES6 decrypt - data length = " + data.length);
        
        // Loop through blocks 1-9 (same as PES6 editor)
        for (int i = 1; i < 10; i++) {
            int blockStart = blockPES6[i];
            int blockEnd = blockPES6[i] + blockSizePES6[i];
            System.out.println("DEBUG: Block " + i + ": " + blockStart + " to " + blockEnd + " (size " + blockSizePES6[i] + ")");
            
            int k = 0;
            for (int a = blockStart; a + 4 <= blockEnd; a = a + 4) {
                int c = this.byteToInt(data, a);
                int p = ((c - key[k]) + 0x7ab3684c) ^ 0x7ab3684c;
                data[a] = this.toByte(p & 0x000000FF);
                data[a + 1] = this.toByte((p >>> 8) & 0x000000FF);
                data[a + 2] = this.toByte((p >>> 16) & 0x000000FF);
                data[a + 3] = this.toByte((p >>> 24) & 0x000000FF);
                k++;
                if (k == 446) {
                    k = 0;
                }
            }
        }
        
        System.out.println("DEBUG: Decrypt complete");
    }

    private void decrypt() {
        int n = 1;
        while (n < 11) {
            int n2 = 0;
            int n3 = block[n];
            while (n3 + 4 <= block[n] + blockSize[n]) {
                int n4 = this.byteToInt(this.data, n3);
                int n5 = n4 - key[n2] + 2058577996 ^ 0x7AB3684C;
                this.data[n3] = this.toByte(n5 & 0xFF);
                this.data[n3 + 1] = this.toByte(n5 >>> 8 & 0xFF);
                this.data[n3 + 2] = this.toByte(n5 >>> 16 & 0xFF);
                this.data[n3 + 3] = this.toByte(n5 >>> 24 & 0xFF);
                if (++n2 == 446) {
                    n2 = 0;
                }
                n3 += 4;
            }
            ++n;
        }
    }

    private void encrypt() {
        int n = 1;
        while (n < 11) {
            int n2 = 0;
            int n3 = block[n];
            while (n3 + 4 <= block[n] + blockSize[n]) {
                int n4 = this.byteToInt(this.data, n3);
                int n5 = key[n2] + ((n4 ^ 0x7AB3684C) - 2058577996);
                this.data[n3] = this.toByte(n5 & 0xFF);
                this.data[n3 + 1] = this.toByte(n5 >>> 8 & 0xFF);
                this.data[n3 + 2] = this.toByte(n5 >>> 16 & 0xFF);
                this.data[n3 + 3] = this.toByte(n5 >>> 24 & 0xFF);
                if (++n2 == 446) {
                    n2 = 0;
                }
                n3 += 4;
            }
            ++n;
        }
    }

    private int byteToInt(byte[] byArray, int n) {
        int[] nArray = new int[]{this.toInt(byArray[n]), this.toInt(byArray[n + 1]), this.toInt(byArray[n + 2]), this.toInt(byArray[n + 3])};
        int n2 = nArray[0] | nArray[1] << 8 | nArray[2] << 16 | nArray[3] << 24;
        return n2;
    }

    public void checkSums() {
        int n = 0;
        while (n < 11) {
            int n2 = 0;
            int n3 = block[n];
            while (n3 + 4 <= block[n] + blockSize[n]) {
                n2 += this.byteToInt(this.data, n3);
                n3 += 4;
            }
            this.data[OptionFile.block[n] - 8] = this.toByte(n2 & 0xFF);
            this.data[OptionFile.block[n] - 7] = this.toByte(n2 >>> 8 & 0xFF);
            this.data[OptionFile.block[n] - 6] = this.toByte(n2 >>> 16 & 0xFF);
            this.data[OptionFile.block[n] - 5] = this.toByte(n2 >>> 24 & 0xFF);
            ++n;
        }
    }

    public byte[] getBytes(int n) {
        byte[] byArray = new byte[]{this.toByte(n & 0xFF), this.toByte(n >>> 8 & 0xFF)};
        return byArray;
    }

    public byte[] getBytesInt(int n) {
        byte[] byArray = new byte[]{this.toByte(n & 0xFF), this.toByte(n >>> 8 & 0xFF), this.toByte(n >>> 16 & 0xFF), this.toByte(n >>> 24 & 0xFF)};
        return byArray;
    }

    public static boolean isPS2pes(String string) {
        return string.startsWith("BESLES-") && string.endsWith("P2K8OPT");
    }
    
    // ADDED: Export DB for PES2008
    // Exports unnamed_73.bin_000: 640,460 bytes (5165 players x 124 bytes)
    // Player 0 is cleared to default (empty name, default stats) to match original game DB
    private static final byte[] DEFAULT_PLAYER0_STATS = {
        (byte)0xCD, (byte)0xCD, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xC0,
        (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32,
        (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32, (byte)0xB2,
        (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32,
        (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32, (byte)0x32,
        (byte)0x32, (byte)0x32, (byte)0x9B, (byte)0x1B, (byte)0x00, (byte)0x00,
        (byte)0x00, (byte)0x08, (byte)0x01, (byte)0x00, (byte)0x1B, (byte)0x46,
        (byte)0x72, (byte)0x77, (byte)0x95, (byte)0x08, (byte)0x09, (byte)0x00,
        (byte)0x3E, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0F, (byte)0x00,
        (byte)0x58, (byte)0x13, (byte)0x12, (byte)0x77, (byte)0x77, (byte)0x77,
        (byte)0x77, (byte)0x07, (byte)0x00, (byte)0x00, (byte)0x50, (byte)0x04,
        (byte)0x00, (byte)0x40, (byte)0x73, (byte)0xD4, (byte)0x36, (byte)0x49,
        (byte)0x85, (byte)0xC2, (byte)0x06, (byte)0x6E
    };

    public boolean savePara(File file, String directory) {
        boolean success = true;

        try {
            // Player data: 5165 players x 124 bytes = 640,460 bytes
            byte[] players = new byte[640460];
            System.arraycopy(this.data, block[4], players, 0, players.length);

            // Clear player 0 (first 124 bytes): zero the name area, set default stats
            java.util.Arrays.fill(players, 0, 48, (byte)0);
            System.arraycopy(DEFAULT_PLAYER0_STATS, 0, players, 48, DEFAULT_PLAYER0_STATS.length);

            // Write single file
            RandomAccessFile file0 = new RandomAccessFile(directory + File.separator + "unnamed_73.bin_000", "rw");
            file0.write(players);
            file0.close();

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }
}

