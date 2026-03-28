/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Ability99Panel;
import editor.CancelButton;
import editor.ExtraPanel;
import editor.GeneralAbilityPanel;
import editor.OptionFile;
import editor.PSDStatPaste;
import editor.Player;
import editor.PlayerImportDialog;
import editor.PositionPanel;
import editor.SpecialAbilityPanel;
import editor.Stats;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PlayerDialog
extends JDialog {
    OptionFile of;
    int index;
    Player player;
    GeneralAbilityPanel genPanel;
    ExtraPanel exPanel;
    PositionPanel posPanel;
    Ability99Panel abiPanel;
    SpecialAbilityPanel spePanel;
    JButton psdButton;
    JButton copyButton;
    JButton acceptButton;
    JButton cancelButton;
    JButton importButton;
    JButton exportPlayerButton;
    JButton importPlayerButton;
    PlayerImportDialog plImpDia;
    PlayerDialog thisForm = this;

    public PlayerDialog(final Frame owner, OptionFile opf, PlayerImportDialog pid) {
        super(owner, "Edit Player", true);
        JPanel panel = new JPanel();
        JPanel lPanel = new JPanel(new BorderLayout());
        JPanel bPanel = new JPanel(new java.awt.GridLayout(2, 4, 5, 5));  // 2 rows, 4 columns, 5px gaps
        this.acceptButton = new JButton("Accept");
        this.acceptButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent a) {
                if (PlayerDialog.this.check()) {
                    PlayerDialog.this.updateStats();
                    PlayerDialog.this.setVisible(false);
                }
            }
        });
        this.copyButton = new JButton("Copy Stats");
        this.copyButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
                String position = "";
                String pos = "";
                String[] posStr = new String[]{"GK", "?", "CWP", "CBT", "SB", "DM", "WB", "CM", "SM", "AM", "WG", "SS", "CF"};
                String test = (String)PlayerDialog.this.posPanel.regBox.getSelectedItem();
                int j = 0;
                while (j < posStr.length) {
                    if (PlayerDialog.this.posPanel.checkBox[j].isSelected()) {
                        pos = String.valueOf(pos) + ", " + posStr[j];
                    }
                    ++j;
                }
                String posComa = pos.replace(", " + test, "");
                position = "Positions: " + test + "\u2605" + posComa;
                String[] nationOrg = new String[]{"Austria", "Belgium", "Bulgaria", "Croatia", "Czech Republic", "Denmark", "England", "Finland", "France", "Germany", "Greece", "Hungary", "Ireland", "Israel", "Italy", "Netherlands", "Northern Ireland", "Norway", "Poland", "Portugal", "Romania", "Russia", "Scotland", "Serbia and Montenegro", "Slovakia", "Slovenia", "Spain", "Sweden", "Switzerland", "Turkey", "Ukraine", "Wales", "Angola", "Cameroon", "Cote d'Ivoire", "Ghana", "Nigeria", "South Africa", "Togo", "Tunisia", "Costa Rica", "Mexico", "Trinidad and Tobago", "USA", "Argentina", "Brazil", "Chile", "Colombia", "Ecuador", "Paraguay", "Peru", "Uruguay", "Australia", "Iran", "Japan", "Saudi Arabia", "South Korea", "Montenegro", "Benin", "Burkina Faso", "Burundi", "Cape Verde", "Congo", "DR Congo", "Equatorial Guinea", "Gabon", "Gambia", "Guinea", "Kenya", "Liberia", "Mali", "Rwanda", "Sierra Leone", "Zambia", "Zimbabwe", "Canada", "Grenada", "Martinique", "Netherlands Antilles", "New Zealand", "Albania", "Andorra", "Armenia", "Azerbaijan", "Belarus", "Bosnia and Herzegovina", "Cyprus", "Estonia", "Faroe Islands", "Georgia", "Iceland", "Kazakhstan", "Latvia", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Malta", "Moldova", "San Marino", "Algeria", "Egypt", "Morocco", "Senegal", "Honduras", "Jamaica", "Bolivia", "Venezuela", "Bahrain", "China", "Indonesia", "Iraq", "Malaysia", "Oman", "Qatar", "Thailand", "United Arab Emirates", "Uzbekistan", "Vietnam", "No Nationality"};
                String nationSel = (String)PlayerDialog.this.genPanel.nationBox.getSelectedItem();
                String[] nationalities = new String[]{"Austrian", "Belgian", "Bulgarian", "Croatian", "Czech", "Danish", "English", "Finnish", "French", "German", "Greek", "Hungarian", "Irish", "Israeli", "Italian", "Dutch", "Northern Irish", "Norwegian", "Polish", "Portuguese", "Romanian", "Russian", "Scottish", "Serbian", "Slovakian", "Slovenian", "Spanish", "Swedish", "Swiss", "Turkish", "Ukrainian", "Welsh", "Angolan", "Cameroonian", "Ivorian", "Ghanaian", "Nigerian", "South African", "Togolese", "Tunisian", "Costa Rican", "Mexican", "Trinidadian", "American", "Argentinian", "Brazilian", "Chilean", "Colombian", "Ecuadorian", "Paraguayan", "Peruvian", "Uruguayan", "Australian", "Iranian", "Japanese", "Saudi Arabian", "South Korean", "Montenegrin", "Beninese", "Burkinabe", "Burundian", "Cape Verdean", "Congolese", "DR Congolese", "Equatorial Guinean", "Gabonese", "Gambian", "Guinean", "Kenyan", "Liberian", "Malian", "Rwandan", "Sierra Leonean", "Zambian", "Zimbabwean", "Canadian", "Grenadian", "Martinique", "Netherlands Antilles", "New Zealander", "Albanian", "Andorran", "Armenian", "Azerbaijani", "Belarusian", "Bosnian", "Cypriot", "Estonian", "Faroan", "Georgian", "Icelandic", "Kazakh", "Latvian", "Liechtensteiner", "Lithuanian", "Luxembourger", "Macedonian", "Maltese", "Moldavian", "Sammarinese", "Algerian", "Egyptian", "Moroccan", "Senegalese", "Hondurian", "Jamaican", "Bolivian", "Venezuelan", "Bahrain", "Chinese", "Indonesian", "Iraqi", "Malaysian", "Oman", "Qatari", "Thai", "Emirati", "Uzbek", "Vietnamese", "No Nationality"};
                String nation = "";
                int ind = 0;
                int i = 0;
                while (i < nationOrg.length) {
                    if (nationOrg[i] == nationSel) {
                        ind = i;
                    }
                    ++i;
                }
                nation = "Nationality: " + nationalities[ind];
                String age = "Age: " + PlayerDialog.this.genPanel.ageField.getText();
                String height = "Height: " + PlayerDialog.this.genPanel.heightField.getText();
                String weight = "Weight: " + PlayerDialog.this.genPanel.weightField.getText();
                String injury = "Injury Tolerance: " + (String)PlayerDialog.this.genPanel.injuryBox.getSelectedItem();
                String footSide = (String)PlayerDialog.this.genPanel.footBox.getSelectedItem();
                String foot = "Foot: " + footSide.charAt(0);
                String side = "Side: " + footSide.charAt(9);
                String attack = "Attack: " + PlayerDialog.this.abiPanel.field[0].getText();
                String defence = "Defence: " + PlayerDialog.this.abiPanel.field[1].getText();
                String balance = "Balance: " + PlayerDialog.this.abiPanel.field[2].getText();
                String stamina = "Stamina: " + PlayerDialog.this.abiPanel.field[3].getText();
                String speed = "Top Speed: " + PlayerDialog.this.abiPanel.field[4].getText();
                String acceleration = "Acceleration: " + PlayerDialog.this.abiPanel.field[5].getText();
                String response = "Response: " + PlayerDialog.this.abiPanel.field[6].getText();
                String agility = "Agility: " + PlayerDialog.this.abiPanel.field[7].getText();
                String dribbleAcc = "Dribble Accuracy: " + PlayerDialog.this.abiPanel.field[8].getText();
                String dribbleSpeed = "Dribble Speed: " + PlayerDialog.this.abiPanel.field[9].getText();
                String sPassAcc = "Short Pass Accuracy: " + PlayerDialog.this.abiPanel.field[10].getText();
                String sPassSpeed = "Short Pass Speed: " + PlayerDialog.this.abiPanel.field[11].getText();
                String lPassAcc = "Long Pass Accuracy: " + PlayerDialog.this.abiPanel.field[12].getText();
                String lPassSpeed = "Long Pass Speed: " + PlayerDialog.this.abiPanel.field[13].getText();
                String shotAcc = "Shot Accuracy: " + PlayerDialog.this.abiPanel.field[14].getText();
                String shotPower = "Shot Power: " + PlayerDialog.this.abiPanel.field[15].getText();
                String shotTech = "Shot Technique: " + PlayerDialog.this.abiPanel.field[16].getText();
                String fk = "Free Kick Accuracy: " + PlayerDialog.this.abiPanel.field[17].getText();
                String curling = "Curling: " + PlayerDialog.this.abiPanel.field[18].getText();
                String heading = "Header: " + PlayerDialog.this.abiPanel.field[19].getText();
                String jump = "Jump: " + PlayerDialog.this.abiPanel.field[20].getText();
                String tech = "Technique: " + PlayerDialog.this.abiPanel.field[21].getText();
                String aggression = "Aggression: " + PlayerDialog.this.abiPanel.field[22].getText();
                String mentality = "Mentality: " + PlayerDialog.this.abiPanel.field[23].getText();
                String gkAbility = "Keeper Skills: " + PlayerDialog.this.abiPanel.field[24].getText();
                String teamWork = "Teamwork: " + PlayerDialog.this.abiPanel.field[25].getText();
                String stats = String.valueOf(attack) + "\n" + defence + "\n" + balance + "\n" + stamina + "\n" + speed + "\n" + acceleration + "\n" + response + "\n" + agility + "\n" + dribbleAcc + "\n" + dribbleSpeed + "\n" + sPassAcc + "\n" + sPassSpeed + "\n" + lPassAcc + "\n" + lPassSpeed + "\n" + shotAcc + "\n" + shotPower + "\n" + shotTech + "\n" + fk + "\n" + curling + "\n" + heading + "\n" + jump + "\n" + tech + "\n" + aggression + "\n" + mentality + "\n" + gkAbility + "\n" + teamWork;
                String consistency = "Consistency: " + (String)PlayerDialog.this.genPanel.consBox.getSelectedItem();
                String condition = "Condition: " + (String)PlayerDialog.this.genPanel.condBox.getSelectedItem();
                String weakFootAcc = "Weak Foot Accuracy: " + (String)PlayerDialog.this.genPanel.wfaBox.getSelectedItem();
                String weakFootFreq = "Weak Foot Frequency: " + (String)PlayerDialog.this.genPanel.wffBox.getSelectedItem();
                String specAbb = "";
                int i2 = 0;
                while (i2 < PlayerDialog.this.spePanel.ability.length) {
                    if (PlayerDialog.this.spePanel.checkBox[i2].isSelected()) {
                        specAbb = String.valueOf(specAbb) + "\u2605 " + PlayerDialog.this.spePanel.ability[i2] + "\n";
                    }
                    ++i2;
                }
                String dribbleStyle = "Dribble Style: " + (String)PlayerDialog.this.genPanel.dribBox.getSelectedItem();
                String freeKickStyle = "Free Kick Style: " + (String)PlayerDialog.this.genPanel.fkBox.getSelectedItem();
                String penaltyKickStyle = "Penalty Kick Style: " + (String)PlayerDialog.this.genPanel.pkBox.getSelectedItem();
                String dropKick = "Drop Kick Style: " + (String)PlayerDialog.this.genPanel.dkBox.getSelectedItem();
                String finalString = String.valueOf(position) + "\n\n" + nation + "\n" + age + "\n\n" + height + "\n" + weight + "\n\n" + injury + "\n" + foot + "\n" + side + "\n\nSTATS:\n" + stats + "\n\n" + consistency + "\n" + condition + "\n" + weakFootAcc + "\n" + weakFootFreq + "\n\nSPECIAL ABILITIES:\n" + specAbb + "\n" + dribbleStyle + "\n" + freeKickStyle + "\n" + penaltyKickStyle + "\n" + dropKick;
                Clipboard Cboard = PlayerDialog.this.getToolkit().getSystemClipboard();
                StringSelection clipString = new StringSelection(finalString);
                Cboard.setContents(clipString, clipString);
                JOptionPane.showMessageDialog(null, "Text Copied", "InfoBox: Success", 1);
            }
        });
        CancelButton cancelButton = new CancelButton(this.thisForm);
        this.importButton = new JButton("Import (OF2)");
        this.importButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent i) {
                PlayerDialog.this.plImpDia.show(PlayerDialog.this.index);
                PlayerDialog.this.setVisible(false);
            }
        });
        this.psdButton = new JButton("Paste PSD Stat");
        this.psdButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent a) {
                new PSDStatPaste(owner, PlayerDialog.this.thisForm);
            }
        });
        
        // Export Player to .plrv file
        this.exportPlayerButton = new JButton("Export .plrv");
        this.exportPlayerButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent a) {
                if (PlayerDialog.this.check()) {
                    PlayerDialog.this.updateStats();
                    Player p = new Player(PlayerDialog.this.of, PlayerDialog.this.index, 0);
                    
                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Export Player");
                    fc.setCurrentDirectory(new File(System.getProperty("user.home")));
                    fc.setFileFilter(new FileNameExtensionFilter("PLRV files (*.plrv)", "plrv"));
                    fc.setSelectedFile(new File(p.name + ".plrv"));
                    int result = fc.showSaveDialog(PlayerDialog.this.getContentPane());
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        // Ensure .plrv extension
                        if (!file.getName().toLowerCase().endsWith(".plrv")) {
                            file = new File(file.getAbsolutePath() + ".plrv");
                        }
                        try {
                            RandomAccessFile raf = new RandomAccessFile(file, "rw");
                            raf.write(p.bytes);
                            raf.close();
                            JOptionPane.showMessageDialog(PlayerDialog.this.getContentPane(), 
                                "Player exported successfully to:\n" + file.getName(), 
                                "Export Successful", 
                                JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(PlayerDialog.this.getContentPane(), 
                                "Failed to export player:\n" + e.getMessage(), 
                                "Export Failed", 
                                JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        
        // Import Player from .plrv file
        this.importPlayerButton = new JButton("Import .plrv");
        this.importPlayerButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent a) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Import Player");
                fc.setCurrentDirectory(new File(System.getProperty("user.home")));
                fc.setFileFilter(new FileNameExtensionFilter("PLRV files (*.plrv)", "plrv"));
                int result = fc.showOpenDialog(PlayerDialog.this.getRootPane());
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (file.isFile()) {
                        Player p = new Player(PlayerDialog.this.of, PlayerDialog.this.index, 0);
                        try {
                            RandomAccessFile raf = new RandomAccessFile(file, "r");
                            byte[] playerData = new byte[124];
                            raf.read(playerData);
                            raf.close();
                            
                            // Copy player data to correct offset (PES2008 offsets)
                            if (p.index >= 32768) {
                                System.arraycopy(playerData, 0, PlayerDialog.this.of.data, 11876 + (p.index - 32768) * 124, 124);
                            } else {
                                System.arraycopy(playerData, 0, PlayerDialog.this.of.data, 34704 + p.index * 124, 124);
                            }
                            
                            JOptionPane.showMessageDialog(PlayerDialog.this.getContentPane(), 
                                "Player imported successfully from:\n" + file.getName() + "\n\nReopen the Edit Player dialog to see the changes.", 
                                "Import Successful", 
                                JOptionPane.INFORMATION_MESSAGE);
                            fc.setSelectedFile(null);
                            PlayerDialog.this.setVisible(false);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(PlayerDialog.this.getContentPane(), 
                                "Failed to import player:\n" + e.getMessage(), 
                                "Import Failed", 
                                JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(PlayerDialog.this.getContentPane(), 
                            "File not found or invalid.", 
                            "Invalid File", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        this.of = opf;
        this.plImpDia = pid;
        this.genPanel = new GeneralAbilityPanel(this.of);
        this.exPanel = new ExtraPanel(owner, this.of);
        this.posPanel = new PositionPanel(this.of);
        this.abiPanel = new Ability99Panel(this.of);
        this.spePanel = new SpecialAbilityPanel(this.of);
        bPanel.add(this.acceptButton);
        bPanel.add(cancelButton);
        bPanel.add(this.copyButton);
        bPanel.add(this.psdButton);
        bPanel.add(this.importButton);
        bPanel.add(this.exportPlayerButton);
        bPanel.add(this.importPlayerButton);
        lPanel.add((Component)this.genPanel, "North");
        lPanel.add((Component)this.exPanel, "Center");
        lPanel.add((Component)bPanel, "South");
        panel.add(lPanel);
        panel.add(this.posPanel);
        panel.add(this.abiPanel);
        panel.add(this.spePanel);
        this.getContentPane().add(panel);
        this.pack();
        this.setResizable(false);
    }

    public void show(Player p) {
        this.index = p.index;
        this.player = p;
        this.setTitle("Edit Player - " + String.valueOf(this.index) + " - " + p.name);
        if (this.plImpDia.of2Open) {
            this.importButton.setVisible(true);
        } else {
            this.importButton.setVisible(false);
        }
        this.genPanel.load(this.index);
        this.exPanel.load(this.index);
        this.posPanel.load(this.index);
        this.abiPanel.load(this.index);
        this.spePanel.load(this.index);
        this.setVisible(true);
    }

    private void error(String s) {
        JOptionPane.showMessageDialog(null, s, "Error", 0);
    }

    private boolean check() {
        int v;
        int i = 0;
        while (i < this.abiPanel.ability99.length) {
            try {
                v = new Integer(this.abiPanel.field[i].getText());
                if (v < 1 || v > 99) {
                    this.error("Stat " + Stats.ability99[i].name + " out of range!");
                    return false;
                }
            }
            catch (NumberFormatException nfe) {
                this.error("Stat " + Stats.ability99[i].name + " is not a number!");
                return false;
            }
            ++i;
        }
        try {
            v = new Integer(this.genPanel.heightField.getText());
            if (v < 148 || v > 211) {
                this.error("Height out of range!");
                return false;
            }
        }
        catch (NumberFormatException nfe) {
            this.error("Height is not a number!");
            return false;
        }
        try {
            v = new Integer(this.genPanel.weightField.getText());
            if (v < 1 || v > 127) {
                this.error("Weight is out of range!");
                return false;
            }
        }
        catch (NumberFormatException nfe) {
            this.error("Weight is not a number!");
            return false;
        }
        try {
            v = new Integer(this.genPanel.ageField.getText());
            if (v < 15 || v > 46) {
                this.error("Age is out of range!");
                return false;
            }
        }
        catch (NumberFormatException nfe) {
            this.error("Age is not a number");
            return false;
        }
        try {
            String test = (String)this.posPanel.regBox.getSelectedItem();
            int count = 0;
            int i2 = 0;
            while (i2 < Stats.roles.length) {
                if (i2 != 1) {
                    count += this.posPanel.checkBox[i2].isSelected() ? 1 : 0;
                }
                ++i2;
            }
            if (count == 0) {
                throw new Exception("Position");
            }
        }
        catch (Exception ex) {
            this.error("Position is not selected!");
            return false;
        }
        
        // Validate ExtraPanel fields
        try {
            v = new Integer(this.exPanel.callField.getText());
            if (v < 0 || v > 65535) {
                this.error("Callname out of range!");
                return false;
            }
        }
        catch (NumberFormatException nfe) {
            this.error("Callname is not a number!");
            return false;
        }
        try {
            v = new Integer(this.exPanel.faceField.getText());
            if (v < 0 || v > 511) {  // PES2008 face range is 0-511
                this.error("Face ID out of range!");
                return false;
            }
        }
        catch (NumberFormatException nfe) {
            this.error("Face ID is not a number!");
            return false;
        }
        try {
            v = new Integer(this.exPanel.hairField.getText());
            if (v < 0 || v > 2047) {  // PES2008 hair range is 0-2047
                this.error("Hair ID out of range!");
                return false;
            }
        }
        catch (NumberFormatException nfe) {
            this.error("Hair ID is not a number!");
            return false;
        }
        
        return true;
    }

    private void updateStats() {
        int i = 0;
        while (i < Stats.roles.length) {
            if (i != 1) {
                Stats.setValue(this.of, this.index, Stats.roles[i], this.boToInt(this.posPanel.checkBox[i].isSelected()));
            }
            ++i;
        }
        int v = 0;
        int i2 = 0;
        while (i2 < Stats.roles.length) {
            if (((String)this.posPanel.regBox.getSelectedItem()).equals(Stats.roles[i2].name)) {
                v = i2;
            }
            ++i2;
        }
        Stats.setValue(this.of, this.index, Stats.regPos, v);
        Stats.setValue(this.of, this.index, Stats.height, this.genPanel.heightField.getText());
        int item = this.genPanel.footBox.getSelectedIndex();
        int foot = item / 3;
        int side = item - foot * 3;
        Stats.setValue(this.of, this.index, Stats.foot, foot);
        Stats.setValue(this.of, this.index, Stats.favSide, side);
        Stats.setValue(this.of, this.index, Stats.wfa, (String)this.genPanel.wfaBox.getSelectedItem());
        Stats.setValue(this.of, this.index, Stats.wff, (String)this.genPanel.wffBox.getSelectedItem());
        int i3 = 0;
        while (i3 < Stats.ability99.length) {
            Stats.setValue(this.of, this.index, Stats.ability99[i3], this.abiPanel.field[i3].getText());
            ++i3;
        }
        Stats.setValue(this.of, this.index, Stats.consistency, (String)this.genPanel.consBox.getSelectedItem());
        Stats.setValue(this.of, this.index, Stats.condition, (String)this.genPanel.condBox.getSelectedItem());
        i3 = 0;
        while (i3 < Stats.abilitySpecial.length) {
            Stats.setValue(this.of, this.index, Stats.abilitySpecial[i3], this.boToInt(this.spePanel.checkBox[i3].isSelected()));
            ++i3;
        }
        Stats.setValue(this.of, this.index, Stats.injury, (String)this.genPanel.injuryBox.getSelectedItem());
        Stats.setValue(this.of, this.index, Stats.freekick, (String)this.genPanel.fkBox.getSelectedItem());
        Stats.setValue(this.of, this.index, Stats.pkStyle, (String)this.genPanel.pkBox.getSelectedItem());
        Stats.setValue(this.of, this.index, Stats.age, this.genPanel.ageField.getText());
        Stats.setValue(this.of, this.index, Stats.weight, this.genPanel.weightField.getText());
        Stats.setValue(this.of, this.index, Stats.nationality, (String)this.genPanel.nationBox.getSelectedItem());
        Stats.setValue(this.of, this.index, Stats.dribSty, (String)this.genPanel.dribBox.getSelectedItem());
        Stats.setValue(this.of, this.index, Stats.dkSty, (String)this.genPanel.dkBox.getSelectedItem());
        
        // ExtraPanel fields (appearance data) - using PES2008 Stats constants
        Stats.setValue(this.of, this.index, Stats.callName, this.exPanel.callField.getText());
        Stats.setValue(this.of, this.index, Stats.face, this.exPanel.faceField.getText());
        Stats.setValue(this.of, this.index, Stats.hair, this.exPanel.hairField.getText());
        
        // ADDED: Save shirt untucked value
        Stats.setValue(this.of, this.index, Stats.shirtUntucked, this.exPanel.shirtUntuckedCheck.isSelected() ? 1 : 0);
        
        // ADDED: Save sleeves value
        Stats.setValue(this.of, this.index, Stats.sleeves, this.exPanel.sleevesBox.getSelectedIndex());

        // ADDED: Save skin color value
        Stats.setValue(this.of, this.index, Stats.skin, this.exPanel.skinBox.getSelectedIndex());

        // ADDED: Save face type value
        Stats.setValue(this.of, this.index, Stats.faceType, this.exPanel.faceTypeBox.getSelectedIndex());

        Stats.setValue(this.of, this.index, Stats.abilityEdited, 1);
    }

    private int boToInt(boolean b) {
        return b ? 1 : 0;
    }
}

