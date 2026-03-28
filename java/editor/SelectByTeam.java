/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Clubs;
import editor.OptionFile;
import editor.PESUtils;
import editor.PositionList;
import editor.SquadList;
import editor.SquadNumList;
import editor.Stats;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SelectByTeam
extends JPanel {
    public SquadList squadList;
    JComboBox teamBox;
    OptionFile of;
    SquadNumList numList;
    PositionList posList;
    boolean normal;

    public SelectByTeam(OptionFile optionFile, boolean bl) {
        super(new BorderLayout());
        this.of = optionFile;
        this.normal = bl;
        this.teamBox = new JComboBox();
        this.teamBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (SelectByTeam.this.teamBox.getSelectedIndex() != -1) {
                    SelectByTeam.this.squadList.refresh(SelectByTeam.this.teamBox.getSelectedIndex(), true);
                    if (SelectByTeam.this.normal) {
                        SelectByTeam.this.posList.refresh(SelectByTeam.this.teamBox.getSelectedIndex());
                        SelectByTeam.this.numList.refresh(SelectByTeam.this.teamBox.getSelectedIndex());
                    }
                }
            }
        });
        this.teamBox.setMaximumRowCount(32);
        this.squadList = new SquadList(this.of, bl);
        this.add((Component)this.teamBox, "North");
        if (this.normal) {
            this.numList = new SquadNumList(this.of);
            this.posList = new PositionList(this.of, true);
            this.add((Component)this.posList, "West");
            this.add((Component)this.squadList, "Center");
            this.add((Component)this.numList, "East");
        } else {
            JScrollPane jScrollPane = new JScrollPane(22, 31);
            this.setPreferredSize(null);
            jScrollPane.setViewportView(this.squadList);
            this.add((Component)jScrollPane, "Center");
        }
        if (this.normal) {
            this.setPreferredSize(new Dimension(164, 601));
        }
    }

    public void refresh() {
        String[] stringArray = this.normal ? new String[57 + PESUtils.extraSquad.length + 148] : new String[57 + PESUtils.extraSquad.length + 148 + 1];
        System.arraycopy(Stats.nation, 0, stringArray, 0, 57);
        System.arraycopy(PESUtils.extraSquad, 0, stringArray, 57, 16);
        System.arraycopy(Clubs.getNames(this.of), 0, stringArray, 73, 148);
        System.arraycopy(PESUtils.extraSquad, 16, stringArray, 221, 6);
        if (!this.normal) {
            stringArray[57 + PESUtils.extraSquad.length + 148] = "All Players";
        }
        this.teamBox.setModel(new DefaultComboBoxModel<String>(stringArray));
        if (this.normal) {
            this.teamBox.setSelectedIndex(73);
            this.numList.refresh(this.teamBox.getSelectedIndex());
            this.posList.refresh(this.teamBox.getSelectedIndex());
        } else {
            this.teamBox.setSelectedIndex(57 + PESUtils.extraSquad.length + 148);
        }
        this.squadList.refresh(this.teamBox.getSelectedIndex(), true);
    }
}

