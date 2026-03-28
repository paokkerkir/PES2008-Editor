/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Formations;
import editor.OptionFile;
import editor.Player;
import editor.Stat;
import editor.Stats;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JList;

public class PositionList
extends JList {
    private OptionFile of;
    boolean tran;
    int[] posNum;
    int alt = 0;

    public PositionList(OptionFile optionFile, boolean bl) {
        this.tran = bl;
        this.of = optionFile;
        this.setSelectionMode(2);
        this.setLayoutOrientation(0);
        this.setVisibleRowCount(32);
        this.setBackground(new Color(255, 255, 224));
        this.setPreferredSize(new Dimension(30, 576));
    }

    public void refresh(int n) {
        String[] stringArray = new String[32];
        this.posNum = new int[32];
        if (this.tran && (n > 63 && n < 73 || n > 220)) {
            this.setListData(stringArray);
        } else {
            if (this.tran && n > 72) {
                n -= 9;
            }
            stringArray[0] = "GK   ";
            this.posNum[0] = 0;
            int n2 = 0;
            while (n2 < 10) {
                byte by = Formations.getPos(this.of, n, this.alt, n2 + 1);
                this.posNum[n2 + 1] = by;
                if (by == 0) {
                    stringArray[n2 + 1] = "GK";
                }
                if (by > 0 && by < 4 || by > 5 && by < 8) {
                    stringArray[n2 + 1] = "CBT";
                }
                if (by == 4) {
                    stringArray[n2 + 1] = "CWP";
                }
                if (by == 5) {
                    stringArray[n2 + 1] = "ASW";
                }
                if (by == 8) {
                    stringArray[n2 + 1] = "LB";
                }
                if (by == 9) {
                    stringArray[n2 + 1] = "RB";
                }
                if (by > 9 && by < 15) {
                    stringArray[n2 + 1] = "DM";
                }
                if (by == 15) {
                    stringArray[n2 + 1] = "LWB";
                }
                if (by == 16) {
                    stringArray[n2 + 1] = "RWB";
                }
                if (by > 16 && by < 22) {
                    stringArray[n2 + 1] = "CM";
                }
                if (by == 22) {
                    stringArray[n2 + 1] = "LM";
                }
                if (by == 23) {
                    stringArray[n2 + 1] = "RM";
                }
                if (by > 23 && by < 29) {
                    stringArray[n2 + 1] = "AM";
                }
                if (by == 29) {
                    stringArray[n2 + 1] = "LW";
                }
                if (by == 30) {
                    stringArray[n2 + 1] = "RW";
                }
                if (by > 30 && by < 36) {
                    stringArray[n2 + 1] = "SS";
                }
                if (by > 35 && by < 41) {
                    stringArray[n2 + 1] = "CF";
                }
                if (by > 40) {
                    stringArray[n2 + 1] = String.valueOf(by);
                }
                ++n2;
            }
            n2 = 11;
            while (n2 < 32) {
                stringArray[n2] = " ";
                ++n2;
            }
            this.setListData(stringArray);
        }
    }

    public void selectPos(JList jList, int n) {
        this.clearSelection();
        if (n >= 0 && n < 11) {
            int n2 = jList.getModel().getSize();
            int n3 = this.posNum[n];
            int[] nArray = new int[32];
            int n4 = 0;
            Stat stat = Stats.gk;
            if (n3 > 0 && n3 < 4 || n3 > 5 && n3 < 8) {
                stat = Stats.cbt;
            }
            if (n3 == 4) {
                stat = Stats.cwp;
            }
            if (n3 == 5) {
                stat = Stats.cwp;
            }
            if (n3 == 8) {
                stat = Stats.sb;
            }
            if (n3 == 9) {
                stat = Stats.sb;
            }
            if (n3 > 9 && n3 < 15) {
                stat = Stats.dm;
            }
            if (n3 == 15) {
                stat = Stats.wb;
            }
            if (n3 == 16) {
                stat = Stats.wb;
            }
            if (n3 > 16 && n3 < 22) {
                stat = Stats.cm;
            }
            if (n3 == 22) {
                stat = Stats.sm;
            }
            if (n3 == 23) {
                stat = Stats.sm;
            }
            if (n3 > 23 && n3 < 29) {
                stat = Stats.am;
            }
            if (n3 == 29) {
                stat = Stats.wg;
            }
            if (n3 == 30) {
                stat = Stats.wg;
            }
            if (n3 > 30 && n3 < 36) {
                stat = Stats.ss;
            }
            if (n3 > 35 && n3 < 41) {
                stat = Stats.cf;
            }
            n4 = 0;
            int n5 = 0;
            while (n5 < n2) {
                int n6 = ((Player)jList.getModel().getElementAt((int)n5)).index;
                if (n6 != 0 && Stats.getValue(this.of, n6, stat) == 1) {
                    nArray[n4] = n5;
                    ++n4;
                }
                ++n5;
            }
            int[] nArray2 = new int[n4];
            System.arraycopy(nArray, 0, nArray2, 0, n4);
            this.setSelectedIndices(nArray2);
        } else {
            this.clearSelection();
        }
    }
}

