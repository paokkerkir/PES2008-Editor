/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.Stats;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PositionPanel
extends JPanel
implements ActionListener {
    OptionFile of;
    JComboBox regBox;
    int player;
    JCheckBox[] checkBox;
    int regPos;

    public PositionPanel(OptionFile optionFile) {
        super(new BorderLayout());
        this.of = optionFile;
        this.setBorder(BorderFactory.createTitledBorder("Position"));
        JPanel jPanel = new JPanel(new GridLayout(4, 4));
        JLabel jLabel = new JLabel("Registered Position");
        JPanel jPanel2 = new JPanel();
        this.checkBox = new JCheckBox[Stats.roles.length];
        int n = 0;
        while (n < Stats.roles.length) {
            this.checkBox[n] = new JCheckBox(Stats.roles[n].name);
            if (n != 1) {
                this.checkBox[n].setActionCommand(String.valueOf(n));
                this.checkBox[n].addActionListener(this);
                jPanel.add(this.checkBox[n]);
            }
            if (n == 0) {
                jPanel.add(new JPanel());
                jPanel.add(new JPanel());
                jPanel.add(new JPanel());
            }
            ++n;
        }
        this.regBox = new JComboBox();
        this.regBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getActionCommand() == "y") {
                    String string = (String)PositionPanel.this.regBox.getSelectedItem();
                    int n = 0;
                    int n2 = 0;
                    while (n2 < Stats.roles.length) {
                        if (Stats.roles[n2].name.equals(string)) {
                            n = n2;
                        }
                        ++n2;
                    }
                    PositionPanel.this.regPos = n;
                }
            }
        });
        jPanel2.add(jLabel);
        jPanel2.add(this.regBox);
        this.add((Component)jPanel, "Center");
        this.add((Component)jPanel2, "South");
    }

    public void load(int n) {
        this.player = n;
        this.regPos = Stats.getValue(this.of, this.player, Stats.regPos);
        int n2 = 0;
        while (n2 < Stats.roles.length) {
            if (n2 != 1) {
                if (Stats.getValue(this.of, this.player, Stats.roles[n2]) == 1 || this.regPos == n2) {
                    this.checkBox[n2].setSelected(true);
                } else {
                    this.checkBox[n2].setSelected(false);
                }
            }
            ++n2;
        }
        this.updateRegBox();
    }

    public void updateRegBox() {
        this.regBox.setActionCommand("n");
        this.regBox.removeAllItems();
        int n = 0;
        while (n < Stats.roles.length) {
            if (this.checkBox[n].isSelected()) {
                this.regBox.addItem(Stats.roles[n].name);
            }
            ++n;
        }
        this.regBox.setSelectedItem(Stats.roles[this.regPos].name);
        this.regBox.setActionCommand("y");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int n = 0;
        try {
            n = new Integer(actionEvent.getActionCommand());
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        if (this.regPos == n) {
            this.checkBox[n].setSelected(true);
        }
        this.updateRegBox();
    }
}

