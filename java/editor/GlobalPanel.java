/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.Stats;
import editor.TransferPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GlobalPanel extends JPanel {
    private JTextField numField;
    private JComboBox statBox;
    private JComboBox opBox;
    private JButton doButton;
    private JComboBox scopeBox;

    // NEW BUTTONS
    private JButton lowercase;
    private JButton uppercase;
    private JButton titlecase;
    private JButton autofix;   // <-- NEW BUTTON

    private String[] statNames = new String[]{
        "1-99 Stats", "ATTACK", "DEFENSE", "BALANCE", "STAMINA", "TOP SPEED",
        "ACCELERATION", "RESPONSE", "AGILITY", "DRIBBLE ACCURACY",
        "DRIBBLE SPEED", "SHORT PASS ACCURACY", "SHORT PASS SPEED",
        "LONG PASS ACCURACY", "LONG PASS SPEED", "SHOT ACCURACY",
        "SHOT POWER", "SHOT TECHNIQUE", "FREE KICK ACCURACY", "CURLING",
        "HEADING", "JUMP", "TECHNIQUE", "AGGRESSION", "MENTALITY",
        "GOAL KEEPING", "TEAM WORK", "1-8 Stats", "WEAK FOOT ACCURACY",
        "WEAK FOOT FREQUENCY", "CONSISTENCY", "CONDITION / FITNESS", "AGE",
        "SHIRT UNTUCKED",  // 33
        "SLEEVES"  // 34 - ADDED
    };

    private String[] ops = new String[]{"+", "-", "=", "+ %", "- %"};
    private String[] scopes = new String[]{
        "All Players", "GK", "CWP", "CBT", "SB", "DM", "WB", "CM", "SM", "AM", "WG", "SS", "CF"
    };

    private JComboBox teamBox;
    private JComboBox forTeamBox;  // NEW: "For Team Only" filter
    private OptionFile of;
    private JCheckBox checkBox;
    private TransferPanel tranPanel;

    public GlobalPanel(OptionFile optionFile, TransferPanel transferPanel) {
        this.of = optionFile;
        this.tranPanel = transferPanel;

        this.statBox = new JComboBox<String>(this.statNames);
        this.numField = new JTextField(2);
        this.opBox = new JComboBox<String>(this.ops);

        JPanel adjustPanel = new JPanel();
        JPanel scopePanel = new JPanel(new GridLayout(2, 4));
        JPanel mainPanel = new JPanel(new GridLayout(0, 1));

        scopePanel.setBorder(BorderFactory.createTitledBorder("Scope"));
        adjustPanel.setBorder(BorderFactory.createTitledBorder("Adjustment"));

        this.scopeBox = new JComboBox<String>(this.scopes);
        this.teamBox = new JComboBox();
        this.forTeamBox = new JComboBox();
        this.checkBox = new JCheckBox();

        this.doButton = new JButton("Adjust");
        this.doButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n = GlobalPanel.this.getNum();
                int n2 = GlobalPanel.this.statBox.getSelectedIndex();
                int n3 = n;
                int n4 = GlobalPanel.this.opBox.getSelectedIndex();
                int n5 = 99;
                int n6 = 1;

                // ADDED: Special handling for SHIRT UNTUCKED (index 33)
                if (n2 == 33) {
                    // Shirt untucked is binary (0 or 1), only allow "=" operation
                    if (n4 != 2) {
                        JOptionPane.showMessageDialog(null, "Shirt Untucked only supports '=' operation", "Error", 0);
                        return;
                    }
                    if (n != 0 && n != 1) {
                        JOptionPane.showMessageDialog(null, "Enter 0 (tucked) or 1 (untucked)", "Error", 0);
                        return;
                    }
                    // Apply to all players matching scope
                    for (int p = 1; p < 5165; p++) {
                        if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                            Stats.setValue(GlobalPanel.this.of, p, Stats.shirtUntucked, n);
                        }
                    }
                    if (GlobalPanel.this.checkBox.isSelected()) {
                        for (int p = 32768; p < 32952; p++) {
                            if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                                Stats.setValue(GlobalPanel.this.of, p, Stats.shirtUntucked, n);
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Shirt Untucked has been adjusted", "Stats Adjusted", 1);
                    GlobalPanel.this.tranPanel.refresh();
                    return;
                }
                
                // ADDED: Special handling for SLEEVES (index 34)
                if (n2 == 34) {
                    // Sleeves: 0=Auto, 1=Always Short, 2=Always Long
                    if (n4 != 2) {
                        JOptionPane.showMessageDialog(null, "Sleeves only supports '=' operation", "Error", 0);
                        return;
                    }
                    if (n < 0 || n > 2) {
                        JOptionPane.showMessageDialog(null, "Enter 0 (Auto), 1 (Always Short), or 2 (Always Long)", "Error", 0);
                        return;
                    }
                    // Apply to all players matching scope
                    for (int p = 1; p < 5165; p++) {
                        if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                            Stats.setValue(GlobalPanel.this.of, p, Stats.sleeves, n);
                        }
                    }
                    if (GlobalPanel.this.checkBox.isSelected()) {
                        for (int p = 32768; p < 32952; p++) {
                            if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                                Stats.setValue(GlobalPanel.this.of, p, Stats.sleeves, n);
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Sleeves has been adjusted", "Stats Adjusted", 1);
                    GlobalPanel.this.tranPanel.refresh();
                    return;
                }

                if (n2 == 32) {
                    n5 = 46;
                    n6 = 15;
                } else if (n2 > 26) {
                    n5 = 8;
                }

                if (n == 0) {
                    int min = (n4 == 2 ? n6 : 1);
                    int max = (n4 == 2 ? n5 : 99);
                    JOptionPane.showMessageDialog(null, "Enter a number: " + min + "-" + max, "Error", 0);
                    return;
                }

                int p, s;

                if (n2 == 0) {
                    for (p = 1; p < 5165; p++) {
                        if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                            for (s = 1; s < 27; s++) {
                                n3 = GlobalPanel.this.applyOperation(s, p, n, n4);
                                GlobalPanel.this.setStat(s, p, n3);
                            }
                        }
                    }
                    if (GlobalPanel.this.checkBox.isSelected()) {
                        for (p = 32768; p < 32952; p++) {
                            if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                                for (s = 1; s < 27; s++) {
                                    n3 = GlobalPanel.this.applyOperation(s, p, n, n4);
                                    GlobalPanel.this.setStat(s, p, n3);
                                }
                            }
                        }
                    }
                }

                else if (n2 == 27) {
                    for (p = 1; p < 5165; p++) {
                        if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                            for (s = 28; s < 32; s++) {
                                n3 = GlobalPanel.this.applyOperation(s, p, n, n4);
                                if (n3 > 8) n3 = 8;
                                if (n3 < 1) n3 = 1;
                                GlobalPanel.this.setStat(s, p, n3);
                            }
                        }
                    }
                    if (GlobalPanel.this.checkBox.isSelected()) {
                        for (p = 32768; p < 32952; p++) {
                            if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                                for (s = 28; s < 32; s++) {
                                    n3 = GlobalPanel.this.applyOperation(s, p, n, n4);
                                    if (n3 > 8) n3 = 8;
                                    if (n3 < 1) n3 = 1;
                                    GlobalPanel.this.setStat(s, p, n3);
                                }
                            }
                        }
                    }
                }

                else {
                    for (p = 1; p < 5165; p++) {
                        if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                            n3 = GlobalPanel.this.applyOperation(n2, p, n, n4);
                            if (n3 > n5) n3 = n5;
                            if (n3 < n6) n3 = n6;
                            GlobalPanel.this.setStat(n2, p, n3);
                        }
                    }
                    if (GlobalPanel.this.checkBox.isSelected()) {
                        for (p = 32768; p < 32952; p++) {
                            if (GlobalPanel.this.adj(p) && GlobalPanel.this.adjTeam(p) && GlobalPanel.this.adjForTeam(p)) {
                                n3 = GlobalPanel.this.applyOperation(n2, p, n, n4);
                                if (n3 > n5) n3 = n5;
                                if (n3 < 1) n3 = 1;
                                GlobalPanel.this.setStat(n2, p, n3);
                            }
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, "Stats have been adjusted", "Stats Adjusted", 1);
                GlobalPanel.this.tranPanel.refresh();
            }
        });

        scopePanel.add(new JLabel("Registered Position:"));
        scopePanel.add(new JLabel("Exclude Team:"));
        scopePanel.add(new JLabel("For Team Only:"));
        scopePanel.add(new JLabel("Created Players:"));
        scopePanel.add(this.scopeBox);
        scopePanel.add(this.teamBox);
        scopePanel.add(this.forTeamBox);
        scopePanel.add(this.checkBox);

        adjustPanel.add(this.statBox);
        adjustPanel.add(this.opBox);
        adjustPanel.add(this.numField);
        adjustPanel.add(this.doButton);

        JPanel namePanel = new JPanel();
        namePanel.setBorder(BorderFactory.createTitledBorder("Names"));

        this.lowercase = new JButton("lowercase");
        this.lowercase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 1; i < 5165; i++) {
                    Player p = new Player(GlobalPanel.this.of, i, 0);
                    p.setName(GlobalPanel.capitalizeFully(p.toString()));
                }
                for (int i = 32768; i < 32952; i++) {
                    Player p = new Player(GlobalPanel.this.of, i, 0);
                    p.setName(GlobalPanel.capitalizeFully(p.toString()));
                }
                GlobalPanel.this.tranPanel.refresh();
            }
        });

        this.uppercase = new JButton("UPPERCASE");
        this.uppercase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 1; i < 5165; i++) {
                    Player p = new Player(GlobalPanel.this.of, i, 0);
                    p.setName(p.toString().toUpperCase());
                }
                for (int i = 32768; i < 32952; i++) {
                    Player p = new Player(GlobalPanel.this.of, i, 0);
                    p.setName(p.toString().toUpperCase());
                }
                GlobalPanel.this.tranPanel.refresh();
            }
        });

        this.titlecase = new JButton("Title Case");
        this.titlecase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 1; i < 5165; i++) {
                    Player p = new Player(GlobalPanel.this.of, i, 0);
                    p.setName(GlobalPanel.toTitleCase(p.toString()));
                }
                for (int i = 32768; i < 32952; i++) {
                    Player p = new Player(GlobalPanel.this.of, i, 0);
                    p.setName(GlobalPanel.toTitleCase(p.toString()));
                }
                GlobalPanel.this.tranPanel.refresh();
            }
        });

        // --- NEW AUTO-FIX SPACING BUTTON ---
        this.autofix = new JButton("Auto-Fix Spacing");
        this.autofix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 1; i < 5165; i++) {
                    Player p = new Player(GlobalPanel.this.of, i, 0);
                    p.setName(GlobalPanel.autoFixSpacing(p.toString()));
                }
                for (int i = 32768; i < 32952; i++) {
                    Player p = new Player(GlobalPanel.this.of, i, 0);
                    p.setName(GlobalPanel.autoFixSpacing(p.toString()));
                }
                GlobalPanel.this.tranPanel.refresh();
            }
        });

        namePanel.add(this.lowercase);
        namePanel.add(this.uppercase);
        namePanel.add(this.titlecase);
        namePanel.add(this.autofix);   // <-- ADDED

        mainPanel.add(scopePanel);
        mainPanel.add(adjustPanel);
        mainPanel.add(namePanel);

        this.add(mainPanel);
    }

    private int applyOperation(int stat, int player, int n, int op) {
        int base = this.getStat(stat, player);
        switch (op) {
            case 0: return base + n;
            case 1: return base - n;
            case 2: return n;
            case 3: return base + base * n / 100;
            case 4: return base - base * n / 100;
        }
        return base;
    }

    private int getNum() {
        int max = 99;
        int min = 1;
        int stat = this.statBox.getSelectedIndex();

        if (this.opBox.getSelectedIndex() == 2) {
            if (stat == 32) {
                max = 46;
                min = 15;
            } else if (stat > 26) {
                max = 8;
            }
        }

        try {
            int n = Integer.parseInt(this.numField.getText());
            if (n < min || n > max) return 0;
            return n;
        } catch (Exception e) {
            return 0;
        }
    }

    private void setStat(int stat, int player, int value) {
        if (stat == 32) value -= 15;
        else if (stat > 26) value--;

        if (stat > 0 && stat <= 26) {
            Stats.setValue(this.of, player, Stats.ability99[stat - 1], value);
        }

        switch (stat) {
            case 28: Stats.setValue(this.of, player, Stats.wfa, value); break;
            case 29: Stats.setValue(this.of, player, Stats.wff, value); break;
            case 30: Stats.setValue(this.of, player, Stats.consistency, value); break;
            case 31: Stats.setValue(this.of, player, Stats.condition, value); break;
            case 32: Stats.setValue(this.of, player, Stats.age, value); break;
        }

        Stats.setValue(this.of, player, Stats.abilityEdited, 1);
    }

    private int getStat(int stat, int player) {
        int v = 0;

        if (stat > 0 && stat <= 26) {
            v = Stats.getValue(this.of, player, Stats.ability99[stat - 1]);
        }

        switch (stat) {
            case 28: v = Stats.getValue(this.of, player, Stats.wfa); break;
            case 29: v = Stats.getValue(this.of, player, Stats.wff); break;
            case 30: v = Stats.getValue(this.of, player, Stats.consistency); break;
            case 31: v = Stats.getValue(this.of, player, Stats.condition); break;
            case 32: v = Stats.getValue(this.of, player, Stats.age); break;
        }

        if (stat == 32) v += 15;
        else if (stat > 26) v++;

        return v;
    }

    private boolean adj(int player) {
        int scope = this.scopeBox.getSelectedIndex();
        if (scope == 0) return true;

        int pos = Stats.getValue(this.of, player, Stats.regPos);
        if (scope == 1) scope--;

        return pos == scope;
    }

    public void updateTeamBox(String[] arr) {
        String[] arr2 = new String[149];
        arr2[0] = "None";
        System.arraycopy(arr, 0, arr2, 1, 148);
        this.teamBox.setModel(new DefaultComboBoxModel<String>(arr2));
        // Also populate the "For Team Only" box with the same list
        String[] arr3 = new String[149];
        arr3[0] = "None";
        System.arraycopy(arr, 0, arr3, 1, 148);
        this.forTeamBox.setModel(new DefaultComboBoxModel<String>(arr3));
    }

    private boolean adjTeam(int player) {
        int t = this.teamBox.getSelectedIndex();
        if (t == 0) return true;

        int base = 686654 + (t - 1) * 64;

        for (int i = 0; i < 32; i++) {
            int addr = base + i * 2;
            int id = (this.of.toInt(this.of.data[addr + 1]) << 8) | this.of.toInt(this.of.data[addr]);
            if (id == player) return false;
        }

        return true;
    }

    // NEW: "For Team Only" filter - opposite of adjTeam
    // Returns true only if player IS on the selected team (or if "None" selected, allow all)
    private boolean adjForTeam(int player) {
        int t = this.forTeamBox.getSelectedIndex();
        if (t == 0) return true;  // "None" = no filter, allow all

        int base = 686654 + (t - 1) * 64;

        for (int i = 0; i < 32; i++) {
            int addr = base + i * 2;
            int id = (this.of.toInt(this.of.data[addr + 1]) << 8) | this.of.toInt(this.of.data[addr]);
            if (id == player) return true;  // Player IS on this team - allow
        }

        return false;  // Player NOT on this team - skip
    }

    public static String capitalizeFully(String s) {
        if (s == null) return "";
        return s.toLowerCase();
    }

    public static String toTitleCase(String s) {
        if (s == null || s.isEmpty()) return s;

        StringBuilder sb = new StringBuilder();
        boolean cap = true;

        for (char c : s.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                if (cap) {
                    sb.append(Character.toUpperCase(c));
                    cap = false;
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
                cap = true;
            }
        }

        return sb.toString();
    }

    // --- NEW AUTO-FIX SPACING HELPER ---
    public static String autoFixSpacing(String s) {
        if (s == null) return "";

        // Trim
        s = s.trim();

        // Collapse multiple spaces
        s = s.replaceAll("\\s{2,}", " ");

        // Ensure space after punctuation
        s = s.replaceAll("([.,!?;:])(\\S)", "$1 $2");

        // Fix spacing around apostrophes
        s = s.replaceAll("\\s*'\\s*", "'");

        // Fix spacing around hyphens
        s = s.replaceAll("\\s*-\\s*", "-");

        // Remove space before punctuation
        s = s.replaceAll("\\s+([.,!?;:])", "$1");

        return s;
    }
}
