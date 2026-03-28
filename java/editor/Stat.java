/*
 * Decompiled with CFR 0.151.
 */
package editor;

public class Stat {
    public int type;
    public int offSet;
    public int shift;
    public int mask;
    public String name;

    public Stat(int n, int n2, int n3, int n4, String string) {
        this.type = n;
        this.offSet = n2;
        this.shift = n3;
        this.mask = n4;
        this.name = string;
    }
}

