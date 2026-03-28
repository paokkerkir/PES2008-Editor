/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Formations;
import editor.OptionFile;
import java.awt.Color;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JobList
extends JList
implements ListSelectionListener {
    private OptionFile of;
    private int offSet;
    private String job;
    int team;
    private boolean ok = false;

    public JobList(OptionFile optionFile, int n, String string, Color color) {
        this.of = optionFile;
        this.offSet = n;
        this.job = string;
        this.refresh(-1);
        this.setSelectionMode(0);
        this.setLayoutOrientation(0);
        this.setVisibleRowCount(32);
        this.setSelectionBackground(color);
        this.addListSelectionListener(this);
    }

    public void refresh(int n) {
        this.ok = false;
        this.team = n;
        String[] stringArray = new String[11];
        byte by = 0;
        int n2 = 0;
        while (n2 < 11) {
            stringArray[n2] = " ";
            ++n2;
        }
        if (n == -1) {
            stringArray[0] = this.job;
        } else {
            by = Formations.getJob(this.of, n, this.offSet);
            if (by >= 0 && by < 11) {
                stringArray[by] = this.job;
            }
        }
        this.setListData(stringArray);
        this.setSelectedIndex(by);
        this.ok = true;
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting() && !this.isSelectionEmpty() && this.ok) {
            Formations.setJob(this.of, this.team, this.offSet, this.of.toByte(this.getSelectedIndex()));
            this.refresh(this.team);
        }
    }
}

