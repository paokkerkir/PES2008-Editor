/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.Stats;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class SpecialAbilityPanel
extends JPanel {
    OptionFile of;
    int player;
    JCheckBox[] checkBox;
    String[] ability = new String[]{"Dribbling", "Tactical dribble", "Positioning", "Reaction", "Playmaking", "Passing", "Scoring", "1-1 Scoring", "Post player", "Lines", "Middle shooting", "Side", "Centre", "Penalties", "1-Touch pass", "Outside", "Marking", "Sliding", "Covering", "D-Line control", "Penalty stopper", "1-On-1 stopper", "Long throw"};

    public SpecialAbilityPanel(OptionFile optionFile) {
        super(new GridLayout(0, 1));
        this.of = optionFile;
        this.setBorder(BorderFactory.createTitledBorder("Special Ability"));
        this.checkBox = new JCheckBox[this.ability.length];
        int n = 0;
        while (n < this.ability.length) {
            this.checkBox[n] = new JCheckBox(this.ability[n]);
            this.add(this.checkBox[n]);
            ++n;
        }
    }

    public void load(int n) {
        this.player = n;
        int n2 = 0;
        while (n2 < Stats.abilitySpecial.length) {
            if (Stats.getValue(this.of, this.player, Stats.abilitySpecial[n2]) == 1) {
                this.checkBox[n2].setSelected(true);
            } else {
                this.checkBox[n2].setSelected(false);
            }
            ++n2;
        }
    }
}

