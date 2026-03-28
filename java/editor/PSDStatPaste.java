/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.PlayerDialog;
import editor.Stats;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

public class PSDStatPaste
extends JDialog {
    private static boolean debug = false;
    JLabel lbl = new JLabel("Paste PSD stat here:");
    JPanel pnl = new JPanel();
    JButton btn = new JButton("Import PSD Stat");
    JTextArea stat = new JTextArea("", 5, 20);
    JScrollPane sp = new JScrollPane(this.stat);
    PlayerDialog retForm;
    public static final String[] nationalities = new String[]{"Austrian", "Belgian", "Bulgarian", "Croatian", "Czech", "Danish", "English", "Finnish", "French", "German", "Greek", "Hungarian", "Irish", "Israeli", "Italian", "Dutch", "Northern Irish", "Norwegian", "Polish", "Portuguese", "Romanian", "Russian", "Scottish", "Serbian", "Slovakian", "Slovenian", "Spanish", "Swedish", "Swiss", "Turkish", "Ukrainian", "Welsh", "Angolan", "Cameroonian", "Ivorian", "Ghanaian", "Nigerian", "South African", "Togolese", "Tunisian", "Costa Rican", "Mexican", "Trinidadian", "American", "Argentinian", "Brazilian", "Chilean", "Colombian", "Ecuadorian", "Paraguayan", "Peruvian", "Uruguayan", "Australian", "Iranian", "Japanese", "Saudi Arabian", "South Korean", "Montenegrin", "Beninese", "Burkinabe", "Burundian", "Cape Verdean", "Congolese", "DR Congolese", "Equatorial Guinean", "Gabonese", "Gambian", "Guinean", "Kenyan", "Liberian", "Malian", "Rwandan", "Sierra Leonean", "Zambian", "Zimbabwean", "Canadian", "Grenadian", "Martinique", "Netherlands Antilles", "New Zealander", "Nation Free", "Solid Color", "My Team", "Albanian", "Andorran", "Armenian", "Azerbaijani", "Belarusian", "Bosnian", "Cypriot", "Estonian", "Faroan", "Georgian", "Icelandic", "Kazakh", "Latvian", "Liechtensteiner", "Lithuanian", "Luxembourger", "Macedonian", "Maltese", "Moldavian", "Sammarinese", "Algerian", "Egyptian", "Moroccan", "Senegalese", "Hondurian", "Jamaican", "Bolivian", "Venezuelan", "Bahrain", "Chinese", "Indonesian", "Iraqi", "Malaysian", "Oman", "Qatari", "Thai", "Emirati", "Uzbek", "Vietnamese"};
    public static final String[][] positionNames = new String[][]{{"GK", "?", "CWP", "CBT", "SB", "DM", "WB", "CM", "SM", "AM", "WG", "SS", "CF"}, {"GK", "?", "CWP", "CB", "SB", "DMF", "WB", "CMF", "SMF", "AMF", "WF", "SS", "CF"}};
    public static final String[][] statNames = new String[][]{{"Attack", "Defence", "Balance", "Stamina", "Speed", "Acceleration", "Response", "Agility", "Dribble Accuracy", "Dribble Speed", "Short Pass Accuracy", "Short Pass Speed", "Long Pass Accuracy", "Long Pass Speed", "Shot Accuracy", "Shot Power", "Shot Technique", "Free Kick Accuracy", "Swerve", "Heading", "Jump", "Technique", "Aggression", "Mentality", "GK Skills", "Team Work"}, {"Attack", "Defence", "Balance", "Stamina", "Top Speed", "Acceleration", "Response", "Agility", "Dribble Accuracy", "Dribble Speed", "Short Pass Accuracy", "Short Pass Speed", "Long Pass Accuracy", "Long Pass Speed", "Shot Accuracy", "Shot Power", "Shot Technique", "Free Kick Accuracy", "Curling", "Header", "Jump", "Technique", "Aggression", "Mentality", "Keeper Skills", "Teamwork"}, {"Attacking Prowess", "Defence Prowess", "Body Balance", "Stamina", "Speed", "Explosive Power", "Response", "Agility", "Dribbling", "Dribble Speed", "Low Pass", "Short Pass Speed", "Lofted Pass", "Long Pass Speed", "Finishing", "Kicking Power", "Shot Technique", "Place Kicking", "Controlled Spin", "Header", "Jump", "Ball Control", "Aggression", "Tenacity", "Goalkeeping", "Teamwork"}, {"Attack", "Defence", "Balance", "Stamina", "Top Speed", "Explosive Power", "Reflexes", "Agility", "Dribble Accuracy", "Dribble Speed", "Short Pass Accuracy", "Short Pass Speed", "Long Pass Accuracy", "Long Pass Speed", "Shot Accuracy", "Kicking Power", "Shot Technique", "Place Kicking", "Curling", "Header Accuracy", "Jump", "Ball Controll", "Aggression", "Tenacity", "Goal Keeping Skills", "Teamwork"}};
    public static String[] abilityNames = new String[]{"Dribbling", "Tactical dribble", "Positioning", "Reaction", "Playmaking", "Passing", "Scoring", "1-1 Scoring", "Post player", "Lines", "Middle shooting", "Side", "Centre", "Penalties", "1-Touch pass", "Outside", "Marking", "Sliding", "Covering", "D-Line control", "Penalty stopper", "1-On-1 stopper", "Long throw"};

    public static boolean strIn(String a, String b) {
        return a.toLowerCase().contains(b.toLowerCase());
    }

    private static void debugWrite(String s) {
        if (debug) {
            System.out.println(s);
        }
    }

    private void sendPSD(String s) {
        String[] lines = s.replaceAll("(?m)^[ \t]*\r?\n", "").split("\\r?\\n");
        String side = "";
        String foot = "";
        int z = 0;
        while (z < abilityNames.length) {
            this.retForm.spePanel.checkBox[z].setSelected(false);
            ++z;
        }
        z = 0;
        while (z < statNames[0].length) {
            this.retForm.abiPanel.field[z].setText("0");
            ++z;
        }
        int i = 0;
        while (i < Stats.roles.length) {
            this.retForm.posPanel.checkBox[i].setSelected(false);
            ++i;
        }
        i = 0;
        while (i < abilityNames.length) {
            this.retForm.spePanel.checkBox[i].setSelected(false);
            ++i;
        }
        this.retForm.posPanel.updateRegBox();
        this.retForm.genPanel.nationBox.setSelectedItem(nationalities.length - 1);
        this.retForm.genPanel.wfaBox.setSelectedIndex(0);
        this.retForm.genPanel.wffBox.setSelectedIndex(0);
        this.retForm.genPanel.condBox.setSelectedIndex(0);
        this.retForm.genPanel.consBox.setSelectedIndex(0);
        this.retForm.genPanel.injuryBox.setSelectedIndex(0);
        this.retForm.genPanel.dribBox.setSelectedIndex(0);
        this.retForm.genPanel.fkBox.setSelectedIndex(0);
        this.retForm.genPanel.pkBox.setSelectedIndex(0);
        this.retForm.genPanel.dkBox.setSelectedIndex(0);
        this.retForm.genPanel.footBox.setSelectedIndex(0);
        this.retForm.genPanel.ageField.setText("0");
        this.retForm.genPanel.heightField.setText("0");
        this.retForm.genPanel.weightField.setText("0");
        i = 0;
        while (i < lines.length) {
            block42: {
                try {
                    String[] parts = lines[i].split(":");
                    String f = parts[0];
                    if (parts.length > 1) {
                        if (PSDStatPaste.strIn("name", f)) {
                            PSDStatPaste.debugWrite("Found name >" + parts[1]);
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Shirt Name")) {
                            PSDStatPaste.debugWrite("Found shirt name >" + parts[1]);
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Positions")) {
                            PSDStatPaste.debugWrite("Found Positions! ");
                            String[] posList = parts[1].split(",");
                            int z2 = 0;
                            while (z2 < posList.length) {
                                String tmp = posList[z2].replace("\u2605", "").replace("*", "").trim().toUpperCase().replaceAll("[^a-zA-Z0-9]", "");
                                boolean getOut = false;
                                int k = 0;
                                while (k < positionNames.length && !getOut) {
                                    int k1 = 0;
                                    while (k1 < positionNames[k].length && !getOut) {
                                        if (tmp.toUpperCase().equals(positionNames[k][k1].toUpperCase())) {
                                            getOut = true;
                                            tmp = positionNames[0][k1];
                                            this.retForm.posPanel.checkBox[k1].setSelected(true);
                                        }
                                        ++k1;
                                    }
                                    ++k;
                                }
                                if (PSDStatPaste.strIn(posList[z2], "\u2605") || PSDStatPaste.strIn(posList[z2], "*")) {
                                    PSDStatPaste.debugWrite("Reg:" + tmp + " ");
                                    this.retForm.posPanel.updateRegBox();
                                    this.retForm.posPanel.regBox.setSelectedItem(tmp);
                                } else {
                                    PSDStatPaste.debugWrite(String.valueOf(tmp) + " ");
                                }
                                ++z2;
                            }
                            PSDStatPaste.debugWrite("");
                            break block42;
                        }
                        if (PSDStatPaste.strIn("Side", f)) {
                            side = parts[1].toUpperCase().trim();
                            PSDStatPaste.debugWrite("Found side >" + side);
                            break block42;
                        }
                        if (PSDStatPaste.strIn("Foot", f)) {
                            foot = parts[1].toUpperCase().trim();
                            PSDStatPaste.debugWrite("Found foot >" + foot);
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Nationality")) {
                            String nation = parts[1].trim().replace("\u00a0", "");
                            PSDStatPaste.debugWrite("Found Nationality >\"" + nation + "\"");
                            int index = nationalities.length - 1;
                            int k = 0;
                            while (k < nationalities.length) {
                                if (nation.toUpperCase().equals(nationalities[k].toUpperCase())) {
                                    index = k;
                                    break;
                                }
                                ++k;
                            }
                            String normalNation = Stats.nation[index];
                            this.retForm.genPanel.nationBox.setSelectedItem(normalNation);
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Age")) {
                            String tmp = parts[1].replace("(", " ").replace(" ", " ");
                            PSDStatPaste.debugWrite("Age string>" + tmp + "<");
                            tmp = tmp.trim().split(" ")[0];
                            tmp = tmp.trim().replace("\u00a0", "");
                            PSDStatPaste.debugWrite("Age stringint>" + tmp);
                            int age = Integer.parseInt(tmp);
                            PSDStatPaste.debugWrite("Found age > " + age);
                            this.retForm.genPanel.ageField.setText(Integer.toString(age));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Height")) {
                            int height = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Height > " + height);
                            this.retForm.genPanel.heightField.setText(Integer.toString(height));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Weight")) {
                            int weight = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Weight > " + weight);
                            this.retForm.genPanel.weightField.setText(Integer.toString(weight));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Weak Foot Accuracy")) {
                            int wfa = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Weak Foot Accuracy > " + wfa);
                            this.retForm.genPanel.wfaBox.setSelectedItem(Integer.toString(wfa));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Weak Foot Frequency")) {
                            int wff = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Weak Foot Frequency > " + wff);
                            this.retForm.genPanel.wffBox.setSelectedItem(Integer.toString(wff));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Condition") || PSDStatPaste.strIn(f, "Fitness")) {
                            int con = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Condition > " + con);
                            this.retForm.genPanel.condBox.setSelectedItem(Integer.toString(con));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Consistency")) {
                            int con = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Consistency > " + con);
                            this.retForm.genPanel.consBox.setSelectedItem(Integer.toString(con));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Injury")) {
                            String inj = parts[1].toUpperCase().trim().substring(0, 1);
                            PSDStatPaste.debugWrite("Found Injury Tolerance >" + inj);
                            this.retForm.genPanel.injuryBox.setSelectedItem(inj);
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Dribble Style")) {
                            int ds = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Dribble Style >" + ds);
                            this.retForm.genPanel.dribBox.setSelectedItem(Integer.toString(ds));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Free Kick Style")) {
                            int fks = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Dribble Style >" + fks);
                            this.retForm.genPanel.fkBox.setSelectedItem(Integer.toString(fks));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Penalty Style")) {
                            int ps = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Penalty Style >" + ps);
                            this.retForm.genPanel.pkBox.setSelectedItem(Integer.toString(ps));
                            break block42;
                        }
                        if (PSDStatPaste.strIn(f, "Drop Kick Style")) {
                            int dk = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found Drop Kick Style >" + dk);
                            this.retForm.genPanel.dkBox.setSelectedItem(Integer.toString(dk));
                            break block42;
                        }
                        boolean foundStat = false;
                        int statIndex = -1;
                        int j = 0;
                        while (!foundStat && j < statNames.length) {
                            int z3 = 0;
                            while (!foundStat && z3 < statNames[j].length) {
                                if (PSDStatPaste.strIn(statNames[j][z3], f) && PSDStatPaste.strIn(f, statNames[j][z3])) {
                                    foundStat = true;
                                    statIndex = z3;
                                }
                                ++z3;
                            }
                            ++j;
                        }
                        if (foundStat) {
                            int statt = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
                            PSDStatPaste.debugWrite("Found stat: " + statNames[0][statIndex] + "=" + f + " value -> " + statt);
                            this.retForm.abiPanel.field[statIndex].setText(Integer.toString(statt));
                        } else {
                            PSDStatPaste.debugWrite("Nope. " + f);
                        }
                    } else {
                        f = f.replace("\u2605", "").replace("*", "").trim();
                        int z4 = 0;
                        while (z4 < abilityNames.length) {
                            if (PSDStatPaste.strIn(abilityNames[z4], f) && PSDStatPaste.strIn(f, abilityNames[z4])) {
                                PSDStatPaste.debugWrite("Found special " + abilityNames[z4]);
                                this.retForm.spePanel.checkBox[z4].setSelected(true);
                                break;
                            }
                            ++z4;
                        }
                    }
                    PSDStatPaste.debugWrite("");
                }
                catch (Exception e) {
                    PSDStatPaste.debugWrite("Error! " + e);
                }
            }
            ++i;
        }
        String fs = String.valueOf(foot) + " foot / " + side + " side";
        PSDStatPaste.debugWrite("Foot/side combo:" + fs);
        this.retForm.genPanel.footBox.setSelectedItem(fs);
    }

    public PSDStatPaste(Frame owner, PlayerDialog ret) {
        super(owner, "Paste PSD Stat", true);
        this.retForm = ret;
        this.pnl.setLayout(null);
        JPopupMenu menu = new JPopupMenu();
        DefaultEditorKit.CutAction cut = new DefaultEditorKit.CutAction();
        cut.putValue("Name", "Cut");
        cut.putValue("AcceleratorKey", KeyStroke.getKeyStroke("control X"));
        menu.add(cut);
        DefaultEditorKit.CopyAction copy = new DefaultEditorKit.CopyAction();
        copy.putValue("Name", "Copy");
        copy.putValue("AcceleratorKey", KeyStroke.getKeyStroke("control C"));
        menu.add(copy);
        DefaultEditorKit.PasteAction paste = new DefaultEditorKit.PasteAction();
        paste.putValue("Name", "Paste");
        paste.putValue("AcceleratorKey", KeyStroke.getKeyStroke("control V"));
        menu.add(paste);
        SelectAll selectAll = new SelectAll();
        menu.add(selectAll);
        this.stat.setComponentPopupMenu(menu);
        this.lbl.setBounds(0, 0, 400, 30);
        this.pnl.add(this.lbl);
        this.sp.setBounds(0, 30, 400, 340);
        this.pnl.add(this.sp);
        this.btn.setBounds(0, 370, 400, 30);
        this.btn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                PSDStatPaste.this.sendPSD(PSDStatPaste.this.stat.getText());
            }
        });
        this.pnl.setBounds(0, 0, 400, 400);
        this.pnl.add(this.btn);
        this.add(this.pnl);
        this.setSize(400, 430);
        this.setResizable(false);
        this.setBackground(Color.BLACK);
        this.setTitle("PSD Stats Paste");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        this.setVisible(true);
    }

    static class SelectAll
    extends TextAction {
        public SelectAll() {
            super("Select All");
            this.putValue("AcceleratorKey", KeyStroke.getKeyStroke("control S"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextComponent component = this.getFocusedComponent();
            component.selectAll();
            component.requestFocusInWindow();
        }
    }
}

