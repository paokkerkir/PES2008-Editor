/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.Player;
import editor.SelectByTeam;
import editor.Stat;
import editor.Stats;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

public class InfoPanel
extends JScrollPane {
    OptionFile of;
    JEditorPane ta;
    SelectByTeam selector;
    boolean listSquads;
    SimpleAttributeSet attr;
    Document doc;

    public InfoPanel(SelectByTeam selectByTeam, OptionFile optionFile) {
        super(22, 31);
        this.selector = selectByTeam;
        this.of = optionFile;
        this.listSquads = true;
        this.init();
    }

    public InfoPanel(OptionFile optionFile) {
        super(22, 31);
        this.of = optionFile;
        this.listSquads = false;
        this.init();
    }

    private void init() {
        this.ta = new JEditorPane();
        this.ta.setEditable(false);
        this.setViewportView(this.ta);
        StyledEditorKit styledEditorKit = new StyledEditorKit();
        this.ta.setEditorKit(styledEditorKit);
        this.ta.setBackground(Color.black);
        this.attr = new SimpleAttributeSet(styledEditorKit.getInputAttributes());
        this.doc = this.ta.getDocument();
        if (this.listSquads) {
            this.setPreferredSize(new Dimension(290, 493));
        } else {
            this.setPreferredSize(new Dimension(145, 493));
        }
        StyleConstants.setFontFamily(this.attr, "SansSerif");
    }

    public void refresh(int n, int n2) {
        this.ta.setText("");
        if (n > 0 || n2 > 0) {
            try {
                if (n2 > 0) {
                    this.insertId(n, n2);
                    StyleConstants.setFontSize(this.attr, 10);
                    this.insertName(n, n2);
                    this.doc.insertString(this.doc.getLength(), "\n", this.attr);
                    this.insertRole(n, n2);
                    this.doc.insertString(this.doc.getLength(), "\n", this.attr);
                    this.insertPhys(n, n2);
                    this.insertStats(n, n2);
                    this.doc.insertString(this.doc.getLength(), "\n", this.attr);
                    this.insertAbilities(n, n2);
                } else {
                    this.insertId(n, n2);
                    StyleConstants.setFontSize(this.attr, 12);
                    this.insertName(n, n2);
                    this.doc.insertString(this.doc.getLength(), "\n\n", this.attr);
                    this.insertAgeNat(n, n2);
                    this.doc.insertString(this.doc.getLength(), "\n", this.attr);
                    this.insertPhys(n, n2);
                    this.doc.insertString(this.doc.getLength(), "\n", this.attr);
                    this.insertRole(n, n2);
                    this.doc.insertString(this.doc.getLength(), "\n\n", this.attr);
                    this.insertSquads(n);
                }
            }
            catch (BadLocationException badLocationException) {
                // empty catch block
            }
            this.ta.setCaretPosition(0);
        }
    }

    private void insertStats(int n, int n2) throws BadLocationException {
        StyleConstants.setForeground(this.attr, Color.white);
        this.insertStat(Stats.wfa, n, n2);
        this.insertStat(Stats.wff, n, n2);
        int n3 = 0;
        while (n3 < Stats.ability99.length) {
            this.insertStat(Stats.ability99[n3], n, n2);
            ++n3;
        }
        this.insertStat(Stats.consistency, n, n2);
        this.insertStat(Stats.condition, n, n2);
    }

    private void insertRole(int n, int n2) throws BadLocationException {
        String string = "";
        if (n > 0 && Stats.getValue(this.of, n, Stats.gk) == 1) {
            string = "GK";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.gk) == 1) {
            string = String.valueOf(String.valueOf(string)) + "\t\tGK";
        }
        string = String.valueOf(String.valueOf(string)) + "\n";
        if (n > 0 && Stats.getValue(this.of, n, Stats.cwp) == 1) {
            string = String.valueOf(String.valueOf(string)) + "CWP  ";
        }
        if (n > 0 && Stats.getValue(this.of, n, Stats.cbt) == 1) {
            string = String.valueOf(String.valueOf(string)) + "CB  ";
        }
        if (n2 > 0) {
            string = String.valueOf(String.valueOf(string)) + "\t\t";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.cwp) == 1) {
            string = String.valueOf(String.valueOf(string)) + "CWP  ";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.cbt) == 1) {
            string = String.valueOf(String.valueOf(string)) + "CB  ";
        }
        string = String.valueOf(String.valueOf(string)) + "\n";
        if (n > 0 && Stats.getValue(this.of, n, Stats.sb) == 1) {
            string = String.valueOf(String.valueOf(string)) + "SB  ";
        }
        if (n > 0 && Stats.getValue(this.of, n, Stats.wb) == 1) {
            string = String.valueOf(String.valueOf(string)) + "WB  ";
        }
        if (n > 0 && Stats.getValue(this.of, n, Stats.dm) == 1) {
            string = String.valueOf(String.valueOf(string)) + "DM  ";
        }
        if (n2 > 0) {
            string = String.valueOf(String.valueOf(string)) + "\t\t";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.sb) == 1) {
            string = String.valueOf(String.valueOf(string)) + "SB  ";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.wb) == 1) {
            string = String.valueOf(String.valueOf(string)) + "WB  ";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.dm) == 1) {
            string = String.valueOf(String.valueOf(string)) + "DM  ";
        }
        string = String.valueOf(String.valueOf(string)) + "\n";
        if (n > 0 && Stats.getValue(this.of, n, Stats.cm) == 1) {
            string = String.valueOf(String.valueOf(string)) + "CM  ";
        }
        if (n > 0 && Stats.getValue(this.of, n, Stats.sm) == 1) {
            string = String.valueOf(String.valueOf(string)) + "SM  ";
        }
        if (n > 0 && Stats.getValue(this.of, n, Stats.am) == 1) {
            string = String.valueOf(String.valueOf(string)) + "AM  ";
        }
        if (n2 > 0) {
            string = String.valueOf(String.valueOf(string)) + "\t\t";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.cm) == 1) {
            string = String.valueOf(String.valueOf(string)) + "CM  ";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.sm) == 1) {
            string = String.valueOf(String.valueOf(string)) + "SM  ";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.am) == 1) {
            string = String.valueOf(String.valueOf(string)) + "AM  ";
        }
        string = String.valueOf(String.valueOf(string)) + "\n";
        if (n > 0 && Stats.getValue(this.of, n, Stats.ss) == 1) {
            string = String.valueOf(String.valueOf(string)) + "SS  ";
        }
        if (n > 0 && Stats.getValue(this.of, n, Stats.cf) == 1) {
            string = String.valueOf(String.valueOf(string)) + "CF  ";
        }
        if (n > 0 && Stats.getValue(this.of, n, Stats.wg) == 1) {
            string = String.valueOf(String.valueOf(string)) + "WG  ";
        }
        if (n2 > 0) {
            string = String.valueOf(String.valueOf(string)) + "\t\t";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.ss) == 1) {
            string = String.valueOf(String.valueOf(string)) + "SS  ";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.cf) == 1) {
            string = String.valueOf(String.valueOf(string)) + "CF  ";
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, Stats.wg) == 1) {
            string = String.valueOf(String.valueOf(string)) + "WG  ";
        }
        if (n2 > 0) {
            StyleConstants.setFontSize(this.attr, 9);
        }
        this.doc.insertString(this.doc.getLength(), string, this.attr);
        if (n2 > 0) {
            StyleConstants.setFontSize(this.attr, 10);
        }
    }

    private void insertAbilities(int n, int n2) throws BadLocationException {
        int n3 = 0;
        while (n3 < Stats.abilitySpecial.length) {
            if ((n3 & 1) == 1) {
                this.insertAbility(Stats.abilitySpecial[n3], n, n2, Color.red);
            } else {
                this.insertAbility(Stats.abilitySpecial[n3], n, n2, Color.green);
            }
            ++n3;
        }
        StyleConstants.setForeground(this.attr, Color.white);
    }

    private void insertSquads(int n) throws BadLocationException {
        if (this.listSquads) {
            StyleConstants.setForeground(this.attr, Color.white);
            this.doc.insertString(this.doc.getLength(), "Squads:", this.attr);
            int n2 = 683294;
            do {
                int n3;
                if ((n3 = this.of.toInt(this.of.data[(n2 += 2) + 1]) << 8 | this.of.toInt(this.of.data[n2])) != n) continue;
                int n4 = n2 < 686654 ? (n2 >= 686590 ? 72 : (n2 - 683296) / 46) : (n2 - 686654) / 64 + 73;
                this.doc.insertString(this.doc.getLength(), "\n" + this.selector.teamBox.getModel().getElementAt(n4), this.attr);
            } while (n2 < 696508);
        }
    }

    private void insertName(int n, int n2) throws BadLocationException {
        StyleConstants.setForeground(this.attr, Color.white);
        if (n > 0) {
            this.doc.insertString(this.doc.getLength(), new Player((OptionFile)this.of, (int)n, (int)0).name, this.attr);
        }
        if (n2 > 0) {
            this.doc.insertString(this.doc.getLength(), "\t" + new Player((OptionFile)this.of, (int)n2, (int)0).name, this.attr);
        }
    }

    private void insertId(int index1, int index2) throws BadLocationException {
        StyleConstants.setForeground(this.attr, Color.white);
        if (index1 > 0) {
            this.doc.insertString(this.doc.getLength(), "ID: " + Integer.toString(index1) + "\n", this.attr);
        }
        if (index2 > 0) {
            this.doc.insertString(this.doc.getLength(), "\tID: " + Integer.toString(index2) + "\n", this.attr);
        }
    }

    private void insertAgeNat(int n, int n2) throws BadLocationException {
        StyleConstants.setForeground(this.attr, Color.white);
        if (n > 0) {
            this.doc.insertString(this.doc.getLength(), Stats.getString(this.of, n, Stats.nationality), this.attr);
        }
        if (n2 > 0) {
            this.doc.insertString(this.doc.getLength(), "\t" + Stats.getString(this.of, n2, Stats.nationality), this.attr);
        }
        if (n > 0) {
            this.doc.insertString(this.doc.getLength(), "\nAge: " + Stats.getString(this.of, n, Stats.age), this.attr);
        }
        if (n2 > 0) {
            this.doc.insertString(this.doc.getLength(), "\tAge: " + Stats.getString(this.of, n2, Stats.age), this.attr);
        }
    }

    private void insertPhys(int n, int n2) throws BadLocationException {
        String string;
        StyleConstants.setForeground(this.attr, Color.white);
        if (n > 0) {
            string = "LF/";
            if (Stats.getValue(this.of, n, Stats.foot) == 1) {
                switch (Stats.getValue(this.of, n, Stats.favSide)) {
                    case 0: {
                        string = String.valueOf(String.valueOf(string)) + "LS";
                        break;
                    }
                    case 1: {
                        string = String.valueOf(String.valueOf(string)) + "RS";
                        break;
                    }
                    case 2: {
                        string = String.valueOf(String.valueOf(string)) + "BS";
                    }
                }
            } else {
                string = "RF/";
                switch (Stats.getValue(this.of, n, Stats.favSide)) {
                    case 0: {
                        string = String.valueOf(String.valueOf(string)) + "RS";
                        break;
                    }
                    case 1: {
                        string = String.valueOf(String.valueOf(string)) + "LS";
                        break;
                    }
                    case 2: {
                        string = String.valueOf(String.valueOf(string)) + "BS";
                    }
                }
            }
            string = String.valueOf(String.valueOf(string)) + ", ";
            this.doc.insertString(this.doc.getLength(), String.valueOf(String.valueOf(string)) + Stats.getString(this.of, n, Stats.height) + "cm, " + Stats.getString(this.of, n, Stats.weight) + "Kg", this.attr);
        }
        if (n2 > 0) {
            if (n > 0) {
                this.doc.insertString(this.doc.getLength(), "\t", this.attr);
            } else {
                this.doc.insertString(this.doc.getLength(), "\t\t", this.attr);
            }
            string = "LF/";
            if (Stats.getValue(this.of, n2, Stats.foot) == 1) {
                switch (Stats.getValue(this.of, n2, Stats.favSide)) {
                    case 0: {
                        string = String.valueOf(String.valueOf(string)) + "LS";
                        break;
                    }
                    case 1: {
                        string = String.valueOf(String.valueOf(string)) + "RS";
                        break;
                    }
                    case 2: {
                        string = String.valueOf(String.valueOf(string)) + "BS";
                    }
                }
            } else {
                string = "RF/";
                switch (Stats.getValue(this.of, n2, Stats.favSide)) {
                    case 0: {
                        string = String.valueOf(String.valueOf(string)) + "RS";
                        break;
                    }
                    case 1: {
                        string = String.valueOf(String.valueOf(string)) + "LS";
                        break;
                    }
                    case 2: {
                        string = String.valueOf(String.valueOf(string)) + "BS";
                    }
                }
            }
            string = String.valueOf(String.valueOf(string)) + ", ";
            this.doc.insertString(this.doc.getLength(), String.valueOf(String.valueOf(string)) + Stats.getString(this.of, n2, Stats.height) + "cm, " + Stats.getString(this.of, n2, Stats.weight) + "Kg", this.attr);
        }
    }

    private void insertStat(Stat stat, int n, int n2) throws BadLocationException {
        int n3 = Stats.getValue(this.of, n, stat);
        int n4 = Stats.getValue(this.of, n2, stat);
        String string = Stats.getString(this.of, n, stat);
        String string2 = Stats.getString(this.of, n2, stat);
        this.doc.insertString(this.doc.getLength(), "\n" + stat.name + "\t", this.attr);
        if (n > 0) {
            this.setStatColour(stat, n3);
            this.doc.insertString(this.doc.getLength(), string, this.attr);
        }
        if (n2 > 0) {
            if (n > 0) {
                int n5;
                int n6 = n3 - n4;
                String string3 = " ";
                int n7 = 3;
                int n8 = 1;
                if (stat == Stats.wfa || stat == Stats.wff || stat == Stats.consistency || stat == Stats.condition) {
                    n7 = 1;
                    n8 = 0;
                }
                if (n6 > 0) {
                    n6 = n6 / n7 + n8;
                    n5 = 0;
                    while (n5 < n6 && n5 < 10) {
                        string3 = String.valueOf(String.valueOf(string3)) + "*";
                        ++n5;
                    }
                    StyleConstants.setForeground(this.attr, Color.green);
                    this.doc.insertString(this.doc.getLength(), string3, this.attr);
                }
                if (n6 < 0) {
                    n6 = n6 / -n7 + n8;
                    n5 = 0;
                    while (n5 < n6 && n5 < 10) {
                        string3 = String.valueOf(String.valueOf(string3)) + "*";
                        ++n5;
                    }
                    StyleConstants.setForeground(this.attr, Color.red);
                    this.doc.insertString(this.doc.getLength(), string3, this.attr);
                }
            }
            StyleConstants.setForeground(this.attr, Color.white);
            this.setStatColour(stat, n4);
            this.doc.insertString(this.doc.getLength(), "\t" + string2, this.attr);
        }
        StyleConstants.setForeground(this.attr, Color.white);
    }

    private void insertAbility(Stat stat, int n, int n2, Color color) throws BadLocationException {
        StyleConstants.setForeground(this.attr, color);
        this.doc.insertString(this.doc.getLength(), "\n" + stat.name + "\t", this.attr);
        if (n > 0 && Stats.getValue(this.of, n, stat) == 1) {
            this.doc.insertString(this.doc.getLength(), "O", this.attr);
        }
        if (n2 > 0 && Stats.getValue(this.of, n2, stat) == 1) {
            this.doc.insertString(this.doc.getLength(), "\tO", this.attr);
        }
    }

    private void setStatColour(Stat stat, int n) {
        if (stat == Stats.wfa || stat == Stats.wff || stat == Stats.consistency || stat == Stats.condition) {
            if (n == 7) {
                StyleConstants.setForeground(this.attr, Color.red);
            } else if (n == 6) {
                StyleConstants.setForeground(this.attr, Color.orange);
            }
        } else if (n > 94) {
            StyleConstants.setForeground(this.attr, Color.red);
        } else if (n > 89) {
            StyleConstants.setForeground(this.attr, Color.orange);
        } else if (n > 79) {
            StyleConstants.setForeground(this.attr, Color.yellow);
        } else if (n > 74) {
            StyleConstants.setForeground(this.attr, new Color(183, 255, 0));
        }
    }
}

