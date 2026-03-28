/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Formations;
import editor.OptionFile;
import editor.PS2ButtonIcon;
import editor.Player;
import editor.PositionList;
import editor.SquadList;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StrategyPanel
extends JPanel {
    private OptionFile of;
    private SquadList list;
    int squad = 0;
    private JComboBox[] butBox = new JComboBox[4];
    private JComboBox overBox;
    private boolean ok = false;
    private JLabel[] label = new JLabel[4];

    public StrategyPanel(OptionFile optionFile, SquadList squadList, PositionList positionList) {
        super(new GridBagLayout());
        this.setBorder(BorderFactory.createTitledBorder("Strategy"));
        this.of = optionFile;
        this.list = squadList;
        ActionListener actionListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (StrategyPanel.this.ok) {
                    int n = new Integer(actionEvent.getActionCommand());
                    byte by = (byte)StrategyPanel.this.butBox[n].getSelectedIndex();
                    Formations.setStrat(StrategyPanel.this.of, StrategyPanel.this.squad, n, by);
                    if (by == 6 && Formations.getStratOlCB(StrategyPanel.this.of, StrategyPanel.this.squad) == 0) {
                        int n2 = 1;
                        while (Formations.getStratOlCB(StrategyPanel.this.of, StrategyPanel.this.squad) == 0 && n2 < 11) {
                            byte by2 = Formations.getPos(StrategyPanel.this.of, StrategyPanel.this.squad, 0, n2);
                            if (by2 > 0 && by2 < 8) {
                                Formations.setStratOlCB(StrategyPanel.this.of, StrategyPanel.this.squad, n2);
                            }
                            ++n2;
                        }
                    }
                    StrategyPanel.this.refresh(StrategyPanel.this.squad);
                }
            }
        };
        String[] stringArray = new String[]{"No Strategy", "Centre Att.", "R. Side Att.", "L. Side Att.", "Opp. Side Att.", "Change Sides", "CB Overlap", "Pressure", "Counter Attack", "Offside Trap", "Strategy Plan A", "Strategy Plan B"};
        int n = 0;
        while (n < 4) {
            this.label[n] = new JLabel();
            this.label[n].setPreferredSize(new Dimension(42, 17));
            this.label[n].setText(null);
            this.label[n].setIcon(new PS2ButtonIcon(n));
            this.butBox[n] = new JComboBox<String>(stringArray);
            this.butBox[n].setActionCommand(String.valueOf(n));
            this.butBox[n].addActionListener(actionListener);
            ++n;
        }
        this.overBox = new JComboBox();
        this.overBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SweItem sweItem;
                if (StrategyPanel.this.ok && (sweItem = (SweItem)StrategyPanel.this.overBox.getSelectedItem()) != null) {
                    Formations.setStratOlCB(StrategyPanel.this.of, StrategyPanel.this.squad, sweItem.index);
                }
            }
        });
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        while (n4 < 4) {
            if (n4 < 2) {
                n2 = n4 + 1;
                n3 = 0;
            } else {
                n2 = n4 - 1;
                n3 = 2;
            }
            gridBagConstraints.gridx = n2;
            gridBagConstraints.gridy = n3;
            this.add((Component)this.label[n4], gridBagConstraints);
            gridBagConstraints.gridx = n2;
            gridBagConstraints.gridy = n3 + 1;
            this.add((Component)this.butBox[n4], gridBagConstraints);
            ++n4;
        }
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        this.add((Component)new JLabel("Overlap CB:"), gridBagConstraints);
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        this.add((Component)this.overBox, gridBagConstraints);
    }

    public void refresh(int n) {
        int n2;
        this.squad = n;
        this.ok = false;
        boolean bl = false;
        int n3 = 0;
        while (n3 < 4) {
            n2 = Formations.getStrat(this.of, this.squad, n3);
            this.butBox[n3].setSelectedIndex(n2);
            if (n2 == 6) {
                bl = true;
            }
            ++n3;
        }
        this.overBox.removeAllItems();
        n3 = 0;
        n2 = -1;
        byte by = 1;
        while (by < 11) {
            byte by2 = Formations.getPos(this.of, this.squad, 0, by);
            if (by2 > 0 && by2 < 8) {
                if (by == Formations.getStratOlCB(this.of, this.squad)) {
                    n2 = n3;
                }
                this.overBox.addItem(new SweItem(by));
                n3 = (byte)(n3 + 1);
            }
            by = (byte)(by + 1);
        }
        this.overBox.setSelectedIndex(n2);
        if (bl && this.overBox.getItemCount() != 0) {
            this.overBox.setEnabled(true);
        } else {
            this.overBox.setEnabled(false);
        }
        this.ok = true;
    }

    private class SweItem {
        String name;
        byte index;

        public SweItem(byte by) {
            this.index = by;
            this.name = ((Player)((StrategyPanel)StrategyPanel.this).list.getModel().getElementAt((int)this.index)).name;
        }

        public String toString() {
            return this.name;
        }
    }
}

