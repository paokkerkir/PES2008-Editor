/*
 * Decompiled with CFR 0.151.
 * 
 * COMPLETELY REWRITTEN TO SUPPORT PES6 PC FILES:
 * - Now accepts files with NO EXTENSION (PES6 PC format)
 * - Checks file size to identify PES6 (1,191,936 bytes) vs PES2008 (1,118,208 bytes)
 * - Improved error handling and debugging
 * - Fixed resource leaks
 */
package editor;

import editor.OptionFile;
import editor.PESUtils;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.filechooser.FileFilter;

public class OptionFilter
extends FileFilter {
    
    // File sizes
    private static final long PES2008_SIZE = 0x111000; // 1,118,208 bytes
    private static final long PES6_SIZE = 1191936;      // 1,191,936 bytes
    
    @Override
    public boolean accept(File file) {
        if (file == null) {
            return false;
        }
        if (file.isDirectory()) {
            return true;
        }
        
        String extension = PESUtils.getExtension(file);
        
        try {
            // Check files with known extensions (PES2008 PS2 formats)
            if (extension != null) {
                if (extension.equals("xps") && this.fileIsXPSOption(file)) {
                    System.out.println("Accepted XPS file: " + file.getName());
                    return true;
                }
                if (extension.equals("psu") && this.fileIsPSUOption(file)) {
                    System.out.println("Accepted PSU file: " + file.getName());
                    return true;
                }
                if (extension.equals("max") && this.fileIsMAXOption(file)) {
                    System.out.println("Accepted MAX file: " + file.getName());
                    return true;
                }
            } else {
                // ADDED: Accept files with NO EXTENSION (PES6 PC format)
                if (this.fileIsPES6PC(file)) {
                    System.out.println("Accepted PES6 PC file (no extension): " + file.getName());
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking file " + file.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Rejected file: " + file.getName() + " (extension: " + extension + ", size: " + file.length() + ")");
        return false;
    }

    // ADDED: Check if file is a PES6 PC option file (no extension, specific size)
    private boolean fileIsPES6PC(File file) {
        RandomAccessFile raf = null;
        try {
            if (!file.canRead()) {
                System.out.println("Cannot read file: " + file.getName());
                return false;
            }
            
            long fileSize = file.length();
            System.out.println("Checking file: " + file.getName() + ", size: " + fileSize);
            
            // PES6 PC files are exactly 1,191,936 bytes
            // Allow small variance for different formats
            if (fileSize >= PES6_SIZE - 1000 && fileSize <= PES6_SIZE + 1000) {
                System.out.println("File size matches PES6 PC range");
                
                // Additional validation: check if file has reasonable data
                raf = new RandomAccessFile(file, "r");
                
                // PES6 PC files should have player data around offset 37116
                if (fileSize > 37116 + 100) {
                    raf.seek(37116);
                    byte[] sample = new byte[100];
                    int bytesRead = raf.read(sample);
                    
                    if (bytesRead < 100) {
                        System.out.println("Could not read enough bytes at PES6 offset");
                        raf.close();
                        return false;
                    }
                    
                    // Count non-zero bytes
                    int nonZero = 0;
                    for (byte b : sample) {
                        if (b != 0) nonZero++;
                    }
                    
                    raf.close();
                    
                    // If there's some data there, it's likely a valid PES6 file
                    if (nonZero > 10) {
                        System.out.println("Found data at PES6 offset (" + nonZero + " non-zero bytes) - accepting as PES6 PC");
                        return true;
                    } else {
                        System.out.println("Not enough data at PES6 offset (" + nonZero + " non-zero bytes)");
                    }
                } else {
                    System.out.println("File too small for PES6 validation");
                    if (raf != null) raf.close();
                }
            }
            
            // Also check for PES2008 size (in case OF2 is PES2008 without extension)
            if (fileSize >= PES2008_SIZE - 1000 && fileSize <= PES2008_SIZE + 1000) {
                System.out.println("File size matches PES2008 range");
                
                if (raf == null) {
                    raf = new RandomAccessFile(file, "r");
                }
                
                if (fileSize > 34704 + 100) {
                    raf.seek(34704);
                    byte[] sample = new byte[100];
                    int bytesRead = raf.read(sample);
                    
                    if (bytesRead < 100) {
                        System.out.println("Could not read enough bytes at PES2008 offset");
                        raf.close();
                        return false;
                    }
                    
                    int nonZero = 0;
                    for (byte b : sample) {
                        if (b != 0) nonZero++;
                    }
                    
                    raf.close();
                    
                    if (nonZero > 10) {
                        System.out.println("Found data at PES2008 offset (" + nonZero + " non-zero bytes) - accepting as PES2008");
                        return true;
                    } else {
                        System.out.println("Not enough data at PES2008 offset (" + nonZero + " non-zero bytes)");
                    }
                } else {
                    System.out.println("File too small for PES2008 validation");
                    if (raf != null) raf.close();
                }
            }
            
            System.out.println("File does not match any known format");
            
        } catch (IOException e) {
            System.out.println("IOException in fileIsPES6PC: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure file is closed
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        
        return false;
    }

    private boolean isPES6File(String gameID) {
        if (gameID == null) {
            return false;
        }
        if (gameID.equals("PC_WE")) {
            return true;
        }
        if (gameID.startsWith("BESLES") && gameID.contains("PES6")) {
            return true;
        }
        if (gameID.contains("PC_WE") || gameID.contains("WEPC")) {
            return true;
        }
        return false;
    }

    private boolean fileIsPSUOption(File file) {
        boolean bl = false;
        RandomAccessFile randomAccessFile = null;
        try {
            if (file.canRead()) {
                byte[] byArray = new byte[19];
                String string = "";
                randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(64L);
                randomAccessFile.read(byArray);
                string = new String(byArray);
                randomAccessFile.close();
                bl = OptionFile.isPS2pes(string) || this.isPES6File(string);
            }
        }
        catch (IOException iOException) {
            bl = false;
        } finally {
            if (randomAccessFile != null) {
                try { randomAccessFile.close(); } catch (IOException e) {}
            }
        }
        return bl;
    }

    private boolean fileIsXPSOption(File file) {
        boolean bl = false;
        RandomAccessFile randomAccessFile = null;
        try {
            if (file.canRead()) {
                byte[] byArray = new byte[19];
                String string = "";
                randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(21L);
                int n = PESUtils.swabInt(randomAccessFile.readInt());
                if (randomAccessFile.skipBytes(n) == n && randomAccessFile.skipBytes(n = PESUtils.swabInt(randomAccessFile.readInt())) == n && randomAccessFile.skipBytes(n = PESUtils.swabInt(randomAccessFile.readInt()) + 6) == n) {
                    randomAccessFile.read(byArray);
                    string = new String(byArray);
                }
                randomAccessFile.close();
                bl = OptionFile.isPS2pes(string) || this.isPES6File(string);
            }
        }
        catch (IOException iOException) {
            bl = false;
        } finally {
            if (randomAccessFile != null) {
                try { randomAccessFile.close(); } catch (IOException e) {}
            }
        }
        return bl;
    }

    private boolean fileIsMAXOption(File file) {
        boolean bl = false;
        RandomAccessFile randomAccessFile = null;
        try {
            if (file.canRead()) {
                byte[] byArray = new byte[19];
                String string = "";
                randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(16L);
                randomAccessFile.read(byArray);
                string = new String(byArray);
                randomAccessFile.close();
                bl = OptionFile.isPS2pes(string) || this.isPES6File(string);
            }
        }
        catch (IOException iOException) {
            bl = false;
        } finally {
            if (randomAccessFile != null) {
                try { randomAccessFile.close(); } catch (IOException e) {}
            }
        }
        return bl;
    }

    @Override
    public String getDescription() {
        return "PES2008/PES6 Option File";
    }
}
