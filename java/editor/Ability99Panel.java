/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.Stats;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class Ability99Panel
extends JPanel
implements ActionListener,
CaretListener,
KeyListener {
    OptionFile of;
    int player;
    JTextField[] field;
    String[] ability99 = new String[]{"Attack", "Defence", "Balance", "Stamina", "Speed", "Acceleration", "Response", "Agility", "Dribble Accuracy", "Dribble Speed", "Short Pass Accuracy", "Short Pass Speed", "Long Pass Accuracy", "Long Pass Speed", "Shot Accuracy", "Shot Power", "Shot Technique", "Free Kick Accuracy", "Swerve", "Heading", "Jump", "Technique", "Aggression", "Mentality", "GK Skills", "Team Work"};
    String[] initVal;
    int f;

    public Ability99Panel(OptionFile optionFile) {
        super(new GridBagLayout());
        this.of = optionFile;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.ipadx = 2;
        this.setBorder(BorderFactory.createTitledBorder("1-99 Ability"));
        this.field = new JTextField[this.ability99.length];
        this.initVal = new String[this.ability99.length];
        Verifier99 verifier99 = new Verifier99();
        int n = 0;
        while (n < this.ability99.length) {
            this.field[n] = new JTextField(2);
            gridBagConstraints.anchor = 13;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = n;
            this.add((Component)new JLabel(this.ability99[n]), gridBagConstraints);
            gridBagConstraints.anchor = 10;
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = n;
            this.add((Component)this.field[n], gridBagConstraints);
            this.field[n].setActionCommand(String.valueOf(n));
            this.field[n].addActionListener(this);
            this.field[n].setInputVerifier(verifier99);
            this.field[n].addCaretListener(this);
            this.field[n].addKeyListener(this);
            ++n;
        }
    }

    public void load(int n) {
        this.player = n;
        int n2 = 0;
        while (n2 < Stats.ability99.length) {
            this.initVal[n2] = Stats.getString(this.of, this.player, Stats.ability99[n2]);
            this.field[n2].setText(this.initVal[n2]);
            ++n2;
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.f = new Integer(actionEvent.getActionCommand());
        try {
            int n = new Integer(((JTextField)actionEvent.getSource()).getText());
            if (n > 0 && n < 100) {
                if (this.f < this.ability99.length - 1) {
                    this.field[this.f + 1].requestFocus();
                    this.field[this.f + 1].selectAll();
                } else {
                    this.field[0].requestFocus();
                    this.field[0].selectAll();
                }
            } else {
                this.field[this.f].setText(this.initVal[this.f]);
                this.field[this.f].selectAll();
            }
        }
        catch (NumberFormatException numberFormatException) {
            this.field[this.f].setText(this.initVal[this.f]);
            this.field[this.f].selectAll();
        }
    }

    @Override
    public void caretUpdate(CaretEvent caretEvent) {
        block8: {
            JTextField jTextField = (JTextField)caretEvent.getSource();
            String string = jTextField.getText();
            if (string != "") {
                try {
                    int n = new Integer(((JTextField)caretEvent.getSource()).getText());
                    if (n >= 75 && n < 80) {
                        jTextField.setBackground(new Color(183, 255, 0));
                        break block8;
                    }
                    if (n >= 80 && n < 90) {
                        jTextField.setBackground(Color.yellow);
                        break block8;
                    }
                    if (n >= 90 && n < 95) {
                        jTextField.setBackground(Color.orange);
                        break block8;
                    }
                    if (n >= 80 && n < 100) {
                        jTextField.setBackground(Color.red);
                        break block8;
                    }
                    jTextField.setBackground(Color.white);
                }
                catch (NumberFormatException numberFormatException) {
                    jTextField.setBackground(Color.white);
                }
            } else {
                jTextField.setBackground(Color.white);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField)keyEvent.getSource();
        int n = new Integer(jTextField.getText());
        int n2 = keyEvent.getKeyCode();
        if (n2 == 38 && n < 99) {
            jTextField.setText(String.valueOf(++n));
        }
        if (n2 == 40 && n > 1) {
            jTextField.setText(String.valueOf(--n));
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    class Verifier99
    extends InputVerifier {
        Verifier99() {
        }

        @Override
        public boolean verify(JComponent jComponent) {
            boolean bl = false;
            JTextField jTextField = (JTextField)jComponent;
            try {
                int n = new Integer(jTextField.getText());
                if (n > 0 && n < 100) {
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

