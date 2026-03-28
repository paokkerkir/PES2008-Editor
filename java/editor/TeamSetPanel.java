/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Formations;
import editor.OptionFile;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TeamSetPanel
extends JPanel {
    private OptionFile of;
    int alt = 0;
    int squad = 0;
    private JComboBox[] box = new JComboBox[4];
    private boolean ok = false;

    public TeamSetPanel(OptionFile optionFile) {
        super(new GridBagLayout());
        this.setBorder(BorderFactory.createTitledBorder("Team Settings"));
        this.of = optionFile;
        ActionListener actionListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (TeamSetPanel.this.ok) {
                    int n = new Integer(actionEvent.getActionCommand());
                    Formations.setTeam(TeamSetPanel.this.of, TeamSetPanel.this.squad, TeamSetPanel.this.alt, n, TeamSetPanel.this.box[n].getSelectedIndex());
                }
            }
        };
        String[] stringArray = new String[]{"A", "B", "C"};
        int n = 0;
        while (n < 4) {
            this.box[n] = new JComboBox<String>(stringArray);
            this.box[n].setActionCommand(String.valueOf(n));
            this.box[n].addActionListener(actionListener);
            ++n;
        }
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = 13;
        gridBagConstraints.insets = new Insets(0, 10, 0, 1);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        this.add((Component)new JLabel("Back line"), gridBagConstraints);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        this.add((Component)new JLabel("Pressure"), gridBagConstraints);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        this.add((Component)new JLabel("Offside Trap"), gridBagConstraints);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        this.add((Component)new JLabel("Counter Attack"), gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 1, 0, 10);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        this.add((Component)this.box[0], gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        this.add((Component)this.box[1], gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        this.add((Component)this.box[2], gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        this.add((Component)this.box[3], gridBagConstraints);
    }

    public void refresh(int n) {
        this.squad = n;
        this.ok = false;
        int n2 = 0;
        while (n2 < 4) {
            this.box[n2].setSelectedIndex(Formations.getTeam(this.of, this.squad, this.alt, n2));
            ++n2;
        }
        this.ok = true;
    }
}

