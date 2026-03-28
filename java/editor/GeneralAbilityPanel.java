/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.Stats;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GeneralAbilityPanel
extends JPanel {
    OptionFile of;
    int player;
    JComboBox nationBox;
    JTextField ageField;
    JTextField heightField;
    JTextField weightField;
    JComboBox footBox;
    JComboBox wfaBox;
    JComboBox wffBox;
    JComboBox consBox;
    JComboBox condBox;
    JComboBox injuryBox;
    JComboBox fkBox;
    JComboBox pkBox;
    JComboBox dribBox;
    JComboBox dkBox;

    public GeneralAbilityPanel(OptionFile optionFile) {
        super(new GridLayout(0, 2));
        this.of = optionFile;
        this.setBorder(BorderFactory.createTitledBorder("General"));
        this.nationBox = new JComboBox<String>(Stats.nation);
        this.ageField = new JTextField(2);
        this.ageField.setInputVerifier(new VerifierAge());
        this.heightField = new JTextField(2);
        this.heightField.setInputVerifier(new VerifierHeight());
        this.weightField = new JTextField(2);
        this.weightField.setInputVerifier(new VerifierWeight());
        String[] stringArray = new String[]{"R foot / R side", "R foot / L side", "R foot / B side", "L foot / L side", "L foot / R side", "L foot / B side"};
        this.footBox = new JComboBox<String>(stringArray);
        this.wfaBox = new JComboBox<String>(Stats.mod18);
        this.wffBox = new JComboBox<String>(Stats.mod18);
        this.consBox = new JComboBox<String>(Stats.mod18);
        this.condBox = new JComboBox<String>(Stats.mod18);
        this.injuryBox = new JComboBox<String>(Stats.modInjury);
        String[] stringArray2 = new String[]{"1", "2", "3", "4"};
        this.dribBox = new JComboBox<String>(stringArray2);
        this.dkBox = new JComboBox<String>(stringArray2);
        String[] stringArray3 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        this.fkBox = new JComboBox<String>(stringArray3);
        String[] stringArray4 = new String[]{"1", "2", "3", "4", "5"};
        this.pkBox = new JComboBox<String>(stringArray4);
        this.add(new JLabel("Nationality"));
        this.add(this.nationBox);
        this.add(new JLabel("Age"));
        this.add(this.ageField);
        this.add(new JLabel("Height"));
        this.add(this.heightField);
        this.add(new JLabel("Weight"));
        this.add(this.weightField);
        this.add(new JLabel("Foot / Side"));
        this.add(this.footBox);
        this.add(new JLabel("Weak Foot Accuracy"));
        this.add(this.wfaBox);
        this.add(new JLabel("Weak Foot Frequency"));
        this.add(this.wffBox);
        this.add(new JLabel("Consistency"));
        this.add(this.consBox);
        this.add(new JLabel("Condition"));
        this.add(this.condBox);
        this.add(new JLabel("Injury Tolerancy"));
        this.add(this.injuryBox);
        this.add(new JLabel("Dribble Style"));
        this.add(this.dribBox);
        this.add(new JLabel("Free Kick Style"));
        this.add(this.fkBox);
        this.add(new JLabel("Penalty Style"));
        this.add(this.pkBox);
        this.add(new JLabel("Drop Kick Style"));
        this.add(this.dkBox);
    }

    public void load(int n) {
        this.player = n;
        this.nationBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.nationality));
        this.ageField.setText(Stats.getString(this.of, this.player, Stats.age));
        this.heightField.setText(Stats.getString(this.of, this.player, Stats.height));
        this.weightField.setText(Stats.getString(this.of, this.player, Stats.weight));
        this.wfaBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.wfa));
        this.wffBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.wff));
        this.consBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.consistency));
        this.condBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.condition));
        this.injuryBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.injury));
        this.fkBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.freekick));
        this.pkBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.pkStyle));
        this.dribBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.dribSty));
        this.dkBox.setSelectedItem(Stats.getString(this.of, this.player, Stats.dkSty));
        int n2 = Stats.getValue(this.of, this.player, Stats.foot);
        int n3 = Stats.getValue(this.of, this.player, Stats.favSide);
        int n4 = n2 * 3 + n3;
        this.footBox.setSelectedIndex(n4);
    }

    class VerifierAge
    extends InputVerifier {
        VerifierAge() {
        }

        @Override
        public boolean verify(JComponent jComponent) {
            boolean bl = false;
            JTextField jTextField = (JTextField)jComponent;
            try {
                int n = new Integer(jTextField.getText());
                if (n >= 15 && n <= 46) {
                    bl = true;
                }
            }
            catch (NumberFormatException numberFormatException) {
                bl = false;
            }
            return bl;
        }
    }

    class VerifierHeight
    extends InputVerifier {
        VerifierHeight() {
        }

        @Override
        public boolean verify(JComponent jComponent) {
            boolean bl = false;
            JTextField jTextField = (JTextField)jComponent;
            try {
                int n = new Integer(jTextField.getText());
                if (n >= 148 && n <= 211) {
                    bl = true;
                }
            }
            catch (NumberFormatException numberFormatException) {
                bl = false;
            }
            return bl;
        }
    }

    class VerifierWeight
    extends InputVerifier {
        VerifierWeight() {
        }

        @Override
        public boolean verify(JComponent jComponent) {
            boolean bl = false;
            JTextField jTextField = (JTextField)jComponent;
            try {
                int n = new Integer(jTextField.getText());
                if (n >= 1 && n < 128) {
                    bl = true;
                }
            }
            catch (NumberFormatException numberFormatException) {
                bl = false;
            }
            return bl;
        }
    }
}

