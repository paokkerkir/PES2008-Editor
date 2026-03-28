/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Formations;
import editor.OptionFile;
import java.awt.Dimension;
import javax.swing.JList;

public class SquadNumList
extends JList {
    private OptionFile of;

    public SquadNumList(OptionFile optionFile) {
        this.of = optionFile;
        this.setSelectionMode(0);
        this.setLayoutOrientation(0);
        this.setVisibleRowCount(1);
        this.setPreferredSize(new Dimension(16, 576));
    }

    public void refresh(int n) {
        int n2;
        int n3;
        int n4;
        if (n < 71) {
            n4 = 23;
            n3 = 676624 + n * n4;
            n2 = n;
        } else if (n == 71) {
            n4 = 14;
            n3 = 676624 + n * 23;
            n2 = n;
        } else {
            n4 = 32;
            n3 = 678303 + (n - 73) * n4;
            n2 = n - 9;
        }
        String[] stringArray = new String[n4];
        int n5 = 0;
        while (n5 < n4) {
            int n6;
            int n7 = n3 + n5;
            if (n >= 0 && n < 64 || n >= 73 && n < 221) {
                n7 = n3 + Formations.getSlot(this.of, n2, n5);
            }
            stringArray[n5] = (n6 = this.of.toInt(this.of.data[n7]) + 1) == 256 ? "..." : String.valueOf(n6);
            ++n5;
        }
        this.setListData(stringArray);
    }
}

