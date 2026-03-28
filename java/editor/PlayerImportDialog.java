/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.CancelButton;
import editor.InfoPanel;
import editor.OptionFile;
import editor.Player;
import editor.SelectByTeam;
import editor.Stats;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PlayerImportDialog
extends JDialog
implements ListSelectionListener,
MouseListener {
    OptionFile of;
    OptionFile of2;
    JLabel fileLabel;
    SelectByTeam plList;
    InfoPanel infoPanel;
    boolean of2Open;
    int index;
    int replacement;
    JRadioButton allButton;
    JRadioButton statsButton;

    public PlayerImportDialog(Frame frame, OptionFile optionFile, OptionFile optionFile2) {
        super(frame, "Import Player", true);
        this.of = optionFile;
        this.of2 = optionFile2;
        this.fileLabel = new JLabel("From:");
        this.plList = new SelectByTeam(this.of2, false);
        this.infoPanel = new InfoPanel(this.of2);
        this.plList.squadList.addListSelectionListener(this);
        this.plList.squadList.addMouseListener(this);
        CancelButton cancelButton = new CancelButton(this);
        this.allButton = new JRadioButton("Import everything (name, appearance, stats, etc.)");
        this.statsButton = new JRadioButton("Import only the stats editable on the 'Edit Player' dialog");
        JRadioButton jRadioButton = new JRadioButton("Import everything except stats (name, appearance, etc.)");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.allButton);
        buttonGroup.add(this.statsButton);
        buttonGroup.add(jRadioButton);
        this.allButton.setSelected(true);
        JPanel jPanel = new JPanel(new GridLayout(4, 1));
        jPanel.add(this.fileLabel);
        jPanel.add(this.allButton);
        jPanel.add(this.statsButton);
        jPanel.add(jRadioButton);
        this.getContentPane().add((Component)this.plList, "West");
        this.getContentPane().add((Component)this.infoPanel, "Center");
        this.getContentPane().add((Component)cancelButton, "South");
        this.getContentPane().add((Component)jPanel, "North");
        this.of2Open = false;
        this.index = 0;
        this.replacement = 0;
        this.pack();
        this.setResizable(false);
    }

    public void show(int n) {
        this.index = n;
        this.setVisible(true);
    }

    public void refresh() {
        this.plList.refresh();
        this.of2Open = true;
        this.fileLabel.setText("  From:  " + this.of2.fileName);
        this.index = 0;
        this.replacement = 0;
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting() && !this.plList.squadList.isSelectionEmpty()) {
            this.infoPanel.refresh(((Player)this.plList.squadList.getSelectedValue()).index, 0);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int n = mouseEvent.getClickCount();
        JList jList = (JList)mouseEvent.getSource();
        int n2 = ((Player)jList.getSelectedValue()).index;
        if (n == 2 && n2 != 0) {
            this.replacement = n2;
            this.importPlayer();
            this.setVisible(false);
        }
    }

    private void importPlayer() {
        int n = 34704 + this.index * 124;
        if (this.index >= 32768) {
            n = 11876 + (this.index - 32768) * 124;
        }
        int n2 = 34704 + this.replacement * 124;
        if (this.replacement >= 32768) {
            n2 = 11876 + (this.replacement - 32768) * 124;
        }
        if (this.allButton.isSelected()) {
            System.arraycopy(this.of2.data, n2, this.of.data, n, 124);
            Stats.setValue(this.of, this.index, Stats.nameEdited, 1);
            Stats.setValue(this.of, this.index, Stats.callEdited, 1);
            Stats.setValue(this.of, this.index, Stats.shirtEdited, 1);
            Stats.setValue(this.of, this.index, Stats.abilityEdited, 1);
        } else if (this.statsButton.isSelected()) {
            Stats.setValue(this.of, this.index, Stats.nationality, Stats.getValue(this.of2, this.replacement, Stats.nationality));
            Stats.setValue(this.of, this.index, Stats.age, Stats.getValue(this.of2, this.replacement, Stats.age));
            Stats.setValue(this.of, this.index, Stats.height, Stats.getValue(this.of2, this.replacement, Stats.height));
            Stats.setValue(this.of, this.index, Stats.weight, Stats.getValue(this.of2, this.replacement, Stats.weight));
            Stats.setValue(this.of, this.index, Stats.foot, Stats.getValue(this.of2, this.replacement, Stats.foot));
            Stats.setValue(this.of, this.index, Stats.favSide, Stats.getValue(this.of2, this.replacement, Stats.favSide));
            Stats.setValue(this.of, this.index, Stats.wfa, Stats.getValue(this.of2, this.replacement, Stats.wfa));
            Stats.setValue(this.of, this.index, Stats.wff, Stats.getValue(this.of2, this.replacement, Stats.wff));
            Stats.setValue(this.of, this.index, Stats.condition, Stats.getValue(this.of2, this.replacement, Stats.condition));
            Stats.setValue(this.of, this.index, Stats.consistency, Stats.getValue(this.of2, this.replacement, Stats.consistency));
            Stats.setValue(this.of, this.index, Stats.injury, Stats.getValue(this.of2, this.replacement, Stats.injury));
            Stats.setValue(this.of, this.index, Stats.dribSty, Stats.getValue(this.of2, this.replacement, Stats.dribSty));
            Stats.setValue(this.of, this.index, Stats.pkStyle, Stats.getValue(this.of2, this.replacement, Stats.pkStyle));
            Stats.setValue(this.of, this.index, Stats.freekick, Stats.getValue(this.of2, this.replacement, Stats.freekick));
            Stats.setValue(this.of, this.index, Stats.dkSty, Stats.getValue(this.of2, this.replacement, Stats.dkSty));
            Stats.setValue(this.of, this.index, Stats.regPos, Stats.getValue(this.of2, this.replacement, Stats.regPos));
            int n3 = 0;
            while (n3 < Stats.roles.length) {
                Stats.setValue(this.of, this.index, Stats.roles[n3], Stats.getValue(this.of2, this.replacement, Stats.roles[n3]));
                ++n3;
            }
            n3 = 0;
            while (n3 < Stats.ability99.length) {
                Stats.setValue(this.of, this.index, Stats.ability99[n3], Stats.getValue(this.of2, this.replacement, Stats.ability99[n3]));
                ++n3;
            }
            n3 = 0;
            while (n3 < Stats.abilitySpecial.length) {
                Stats.setValue(this.of, this.index, Stats.abilitySpecial[n3], Stats.getValue(this.of2, this.replacement, Stats.abilitySpecial[n3]));
                ++n3;
            }
            Stats.setValue(this.of, this.index, Stats.abilityEdited, 1);
        } else {
            byte[] byArray = new byte[124];
            System.arraycopy(this.of2.data, n2, byArray, 0, 124);
            Stats.setValue(this.of2, this.replacement, Stats.nationality, Stats.getValue(this.of, this.index, Stats.nationality));
            Stats.setValue(this.of2, this.replacement, Stats.age, Stats.getValue(this.of, this.index, Stats.age));
            Stats.setValue(this.of2, this.replacement, Stats.height, Stats.getValue(this.of, this.index, Stats.height));
            Stats.setValue(this.of2, this.replacement, Stats.weight, Stats.getValue(this.of, this.index, Stats.weight));
            Stats.setValue(this.of2, this.replacement, Stats.foot, Stats.getValue(this.of, this.index, Stats.foot));
            Stats.setValue(this.of2, this.replacement, Stats.favSide, Stats.getValue(this.of, this.index, Stats.favSide));
            Stats.setValue(this.of2, this.replacement, Stats.wfa, Stats.getValue(this.of, this.index, Stats.wfa));
            Stats.setValue(this.of2, this.replacement, Stats.wff, Stats.getValue(this.of, this.index, Stats.wff));
            Stats.setValue(this.of2, this.replacement, Stats.condition, Stats.getValue(this.of, this.index, Stats.condition));
            Stats.setValue(this.of2, this.replacement, Stats.consistency, Stats.getValue(this.of, this.index, Stats.consistency));
            Stats.setValue(this.of2, this.replacement, Stats.injury, Stats.getValue(this.of, this.index, Stats.injury));
            Stats.setValue(this.of2, this.replacement, Stats.dribSty, Stats.getValue(this.of, this.index, Stats.dribSty));
            Stats.setValue(this.of2, this.replacement, Stats.pkStyle, Stats.getValue(this.of, this.index, Stats.pkStyle));
            Stats.setValue(this.of2, this.replacement, Stats.freekick, Stats.getValue(this.of, this.index, Stats.freekick));
            Stats.setValue(this.of2, this.replacement, Stats.dkSty, Stats.getValue(this.of, this.index, Stats.dkSty));
            Stats.setValue(this.of2, this.replacement, Stats.regPos, Stats.getValue(this.of, this.index, Stats.regPos));
            int n4 = 0;
            while (n4 < Stats.roles.length) {
                Stats.setValue(this.of2, this.replacement, Stats.roles[n4], Stats.getValue(this.of, this.index, Stats.roles[n4]));
                ++n4;
            }
            n4 = 0;
            while (n4 < Stats.ability99.length) {
                Stats.setValue(this.of2, this.replacement, Stats.ability99[n4], Stats.getValue(this.of, this.index, Stats.ability99[n4]));
                ++n4;
            }
            n4 = 0;
            while (n4 < Stats.abilitySpecial.length) {
                Stats.setValue(this.of2, this.replacement, Stats.abilitySpecial[n4], Stats.getValue(this.of, this.index, Stats.abilitySpecial[n4]));
                ++n4;
            }
            System.arraycopy(this.of2.data, n2, this.of.data, n, 124);
            Stats.setValue(this.of, this.index, Stats.nameEdited, 1);
            Stats.setValue(this.of, this.index, Stats.callEdited, 1);
            Stats.setValue(this.of, this.index, Stats.shirtEdited, 1);
            System.arraycopy(byArray, 0, this.of2.data, n2, 124);
        }
    }
}

