/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.Player;
import editor.Stats;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JList;

public class NationalityList
extends JList {
    private OptionFile of;

    public NationalityList(OptionFile optionFile) {
        this.of = optionFile;
        this.setSelectionMode(0);
        this.setLayoutOrientation(0);
        this.setVisibleRowCount(32);
    }

    public void refresh(int n, boolean bl) {
        Vector<Player> vector = new Vector<Player>();
        if (n == Stats.nation.length + 4) {
            int n2 = 1;
            while (n2 < 5165) {
                vector.addElement(new Player(this.of, n2, 0));
                ++n2;
            }
            n2 = 32768;
            while (n2 < 32952) {
                vector.addElement(new Player(this.of, n2, 0));
                ++n2;
            }
        } else if (n == Stats.nation.length + 3) {
            int n3;
            int n4;
            boolean bl2;
            int n5 = 1;
            while (n5 < 1312) {
                bl2 = true;
                n4 = 686652;
                do {
                    if ((n3 = this.of.toInt(this.of.data[(n4 += 2) + 1]) << 8 | this.of.toInt(this.of.data[n4])) != n5) continue;
                    bl2 = false;
                } while (n4 < 696124 && n3 != n5);
                if (bl2) {
                    vector.addElement(new Player(this.of, n5, 0));
                }
                ++n5;
            }
            n5 = 1473;
            while (n5 < 4790) {
                bl2 = true;
                n4 = 686652;
                do {
                    if ((n3 = this.of.toInt(this.of.data[(n4 += 2) + 1]) << 8 | this.of.toInt(this.of.data[n4])) != n5) continue;
                    bl2 = false;
                } while (n4 < 696124 && n3 != n5);
                if (bl2) {
                    vector.addElement(new Player(this.of, n5, 0));
                }
                ++n5;
            }
        } else if (n == Stats.nation.length + 2) {
            int n6 = 1473;
            while (n6 < 4330) {
                int n7 = this.getDupe(n6);
                if (n7 != -1) {
                    vector.addElement(new Player(this.of, n6, 0));
                    vector.addElement(new Player(this.of, n7, 0));
                }
                ++n6;
            }
        } else if (n == Stats.nation.length + 1) {
            int n8 = 4978;
            while (n8 < 5155) {
                vector.addElement(new Player(this.of, n8, 0));
                ++n8;
            }
        } else if (n == Stats.nation.length) {
            int n9 = 5155;
            while (n9 < 5165) {
                vector.addElement(new Player(this.of, n9, 0));
                ++n9;
            }
        } else {
            int n10 = 1;
            while (n10 < 5165) {
                if (Stats.getValue(this.of, n10, Stats.nationality) == n) {
                    vector.addElement(new Player(this.of, n10, 0));
                }
                ++n10;
            }
            n10 = 32768;
            while (n10 < 32952) {
                if (Stats.getValue(this.of, n10, Stats.nationality) == n) {
                    vector.addElement(new Player(this.of, n10, 0));
                }
                ++n10;
            }
        }
        if (n != Stats.nation.length + 2 && bl) {
            Collections.sort(vector);
        }
        vector.trimToSize();
        this.setListData(vector);
    }

    private int getDupe(int n) {
        int n2 = 1;
        while (n2 < 1312) {
            boolean bl = true;
            if (Stats.getValue(this.of, n, Stats.nationality) != Stats.getValue(this.of, n2, Stats.nationality)) {
                bl = false;
            } else {
                int n3 = 0;
                if (Stats.getValue(this.of, n, Stats.age) == Stats.getValue(this.of, n2, Stats.age)) {
                    ++n3;
                }
                if (Stats.getValue(this.of, n, Stats.height) == Stats.getValue(this.of, n2, Stats.height)) {
                    ++n3;
                }
                if (Stats.getValue(this.of, n, Stats.weight) == Stats.getValue(this.of, n2, Stats.weight)) {
                    ++n3;
                }
                if (Stats.getValue(this.of, n, Stats.foot) == Stats.getValue(this.of, n2, Stats.foot)) {
                    ++n3;
                }
                if (Stats.getValue(this.of, n, Stats.favSide) == Stats.getValue(this.of, n2, Stats.favSide)) {
                    ++n3;
                }
                if (Stats.getValue(this.of, n, Stats.regPos) == Stats.getValue(this.of, n2, Stats.regPos)) {
                    ++n3;
                }
                if (Stats.getValue(this.of, n, Stats.attack) == Stats.getValue(this.of, n2, Stats.attack)) {
                    ++n3;
                }
                if (Stats.getValue(this.of, n, Stats.accel) == Stats.getValue(this.of, n2, Stats.accel)) {
                    ++n3;
                }
                if (n3 < 7) {
                    bl = false;
                }
            }
            if (bl) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }
}

