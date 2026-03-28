/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.Stats;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Player
implements Comparable,
Serializable {
    public String name;
    public int index;
    public int adr;
    private OptionFile of;
    public byte[] bytes = new byte[124];
    static final int startAdr = 34704;
    static final int startAdrE = 11876;
    static final int firstML = 4790;
    static final int firstShop = 4818;
    static final int firstYoung = 4978;
    static final int firstOld = 5155;
    static final int firstUnused = 5165;
    static final int firstEdit = 32768;
    static final int total = 5165;
    static final int totalEdit = 184;
    static final int firstClassic = 1312;
    static final int firstClub = 1473;
    static final int firstPESUnited = 4330;

    public Player(OptionFile optionFile, int n, int n2) {
        this.of = optionFile;
        if (this.of == null) {
            throw new NullPointerException();
        }
        this.index = n;
        this.adr = n2;
        if (n == 0) {
            this.name = "<empty>";
        } else if (n < 0 || n >= 5165 && n < 32768 || n > 32951) {
            this.name = "<ERROR>";
            this.index = 0;
        } else {
            int n3 = 34704;
            int n4 = n * 124;
            if (n >= 32768) {
                n3 = 11876;
                n4 = (n - 32768) * 124;
            }
            byte[] byArray = new byte[32];
            System.arraycopy(this.of.data, n3 + n4, byArray, 0, 32);
            boolean bl = false;
            int n5 = 0;
            int n6 = 0;
            while (!bl && n6 < byArray.length - 1) {
                if (byArray[n6] == 0 && byArray[n6 + 1] == 0) {
                    bl = true;
                    n5 = n6;
                }
                n6 += 2;
            }
            try {
                this.name = new String(byArray, 0, n5, "UTF-16LE");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                this.name = "<Error " + String.valueOf(this.index) + ">";
            }
            
            // Copy player's 124 bytes to the bytes field
            System.arraycopy(this.of.data, n3 + n4, this.bytes, 0, 124);
            
            if (this.name.equals("") && this.index >= 32768) {
                this.name = "<Edited " + String.valueOf(this.index - 32768) + ">";
            } else if (this.name.equals("")) {
                this.name = this.index >= 5165 ? "<Unused " + String.valueOf(this.index) + ">" : "<L " + String.valueOf(this.index) + ">";
            }
        }
    }

    public String toString() {
        return this.name;
    }

    public int compareTo(Object object) {
        Player player = (Player)object;
        int n = this.name.compareTo(player.name);
        if (n == 0) {
            n = new Integer(Stats.getValue(this.of, this.index, Stats.age)).compareTo(new Integer(Stats.getValue(this.of, player.index, Stats.age)));
        }
        return n;
    }

    public void setName(String string) {
        int n = string.length();
        if (this.index != 0 && n <= 15) {
            byte[] byArray;
            byte[] byArray2 = new byte[32];
            try {
                byArray = string.getBytes("UTF-16LE");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                byArray = new byte[30];
            }
            if (byArray.length <= 30) {
                System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
            } else {
                System.arraycopy(byArray, 0, byArray2, 0, 30);
            }
            int n2 = 34704;
            int n3 = this.index * 124;
            if (this.index >= 32768) {
                n2 = 11876;
                n3 = (this.index - 32768) * 124;
            }
            System.arraycopy(byArray2, 0, this.of.data, n2 + n3, 32);
            Stats.setValue(this.of, this.index, Stats.callName, 52685);
            Stats.setValue(this.of, this.index, Stats.nameEdited, 1);
            Stats.setValue(this.of, this.index, Stats.callEdited, 1);
            this.name = string;
        }
    }

    public String getShirtName() {
        String string = "";
        int n = 34736 + this.index * 124;
        if (this.index >= 32768) {
            n = 11908 + (this.index - 32768) * 124;
        }
        if (this.of.data[n] != 0) {
            byte[] byArray = new byte[16];
            System.arraycopy(this.of.data, n, byArray, 0, 16);
            int n2 = 0;
            while (n2 < 16) {
                if (byArray[n2] == 0) {
                    byArray[n2] = 33;
                }
                ++n2;
            }
            string = new String(byArray);
            string = string.replaceAll("!", "");
        }
        return string;
    }

    public void setShirtName(String string) {
        if (string.length() < 16 && this.index != 0) {
            int n = 34736 + this.index * 124;
            if (this.index >= 32768) {
                n = 11908 + (this.index - 32768) * 124;
            }
            byte[] byArray = new byte[16];
            string = string.toUpperCase();
            byte[] byArray2 = string.getBytes();
            int n2 = 0;
            while (n2 < byArray2.length) {
                if ((byArray2[n2] < 65 || byArray2[n2] > 90) && byArray2[n2] != 46 && byArray2[n2] != 32 && byArray2[n2] != 95) {
                    byArray2[n2] = 32;
                }
                ++n2;
            }
            System.arraycopy(byArray2, 0, byArray, 0, byArray2.length);
            System.arraycopy(byArray, 0, this.of.data, n, 16);
            Stats.setValue(this.of, this.index, Stats.shirtEdited, 1);
        }
    }

    public void makeShirt(String string) {
        String string2 = "";
        String string3 = "";
        int n = string.length();
        if (n < 9 && n > 5) {
            string3 = " ";
        } else if (n < 6 && n > 3) {
            string3 = "  ";
        } else if (n == 3) {
            string3 = "    ";
        } else if (n == 2) {
            string3 = "        ";
        }
        string = string.toUpperCase();
        byte[] byArray = string.getBytes();
        int n2 = 0;
        while (n2 < byArray.length) {
            if ((byArray[n2] < 65 || byArray[n2] > 90) && byArray[n2] != 46 && byArray[n2] != 32 && byArray[n2] != 95) {
                byArray[n2] = 32;
            }
            ++n2;
        }
        string = new String(byArray);
        n2 = 0;
        while (n2 < string.length() - 1) {
            string2 = String.valueOf(String.valueOf(string2)) + string.substring(n2, n2 + 1) + string3;
            ++n2;
        }
        string2 = String.valueOf(String.valueOf(string2)) + string.substring(string.length() - 1, string.length());
        this.setShirtName(string2);
    }
}

