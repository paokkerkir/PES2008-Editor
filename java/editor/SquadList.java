/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Formations;
import editor.OptionFile;
import editor.Player;
import java.awt.Dimension;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JList;

public class SquadList
extends JList {
    private OptionFile of;
    public int team;

    public SquadList(OptionFile optionFile, boolean bl) {
        this.of = optionFile;
        this.setSelectionMode(0);
        this.setLayoutOrientation(0);
        this.setVisibleRowCount(32);
        if (bl) {
            this.setPreferredSize(new Dimension(118, 576));
        }
    }

    public void refresh(int n, boolean bl) {
        this.team = n;
        if (!bl && this.team > 63) {
            this.team += 9;
        }
        if (this.team == 227) {
            Vector<Player> vector = new Vector<Player>();
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
            Collections.sort(vector);
            vector.trimToSize();
            this.setListData(vector);
        } else {
            int n3;
            int n4;
            int n5;
            if (this.team < 71) {
                n5 = 23;
                n4 = 683296 + this.team * n5 * 2;
                n3 = this.team;
            } else if (this.team == 71) {
                n5 = 14;
                n4 = 683296 + this.team * 23 * 2;
                n3 = this.team;
            } else {
                n5 = 32;
                n4 = 686654 + (this.team - 73) * n5 * 2;
                n3 = this.team - 9;
            }
            Player[] playerArray = new Player[n5];
            int n6 = 0;
            while (n6 < n5) {
                int n7 = n4 + n6 * 2;
                if (this.team >= 0 && this.team < 64 || this.team >= 73 && this.team < 221) {
                    n7 = n4 + Formations.getSlot(this.of, n3, n6) * 2;
                }
                playerArray[n6] = new Player(this.of, this.of.toInt(this.of.data[n7 + 1]) << 8 | this.of.toInt(this.of.data[n7]), n7);
                ++n6;
            }
            this.setListData(playerArray);
        }
    }
}

