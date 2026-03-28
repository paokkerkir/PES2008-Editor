/*
 * Decompiled with CFR 0.151.
 */
package editor;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

public class PSDConnPanel
extends JDialog {
    JTextField teamSearch;
    JLabel teamSearchLbl;
    JComboBox teamSelection;
    JButton teamSearchButton;
    JList<String> playerList;
    JLabel playerSearchCaption;
    JTextField playerSearch;
    JButton playerSearchButton;
    JTextArea statData;
    JLabel statLbl;
    JScrollPane sp1;
    JScrollPane sp2;
    DefaultListModel<String> playerListModel;
    String domain = "https://pesstatsdatabasedapi.herokuapp.com";

    public static String getHTML(String urlToRead) {
        try {
            String line;
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = rd.readLine()) != null) {
                result.append(String.valueOf(line) + "\n");
            }
            rd.close();
            return result.toString();
        }
        catch (Exception e) {
            System.out.println("Internet error: " + e);
            return "";
        }
    }

    public PSDConnPanel() {
        this.sp1 = new JScrollPane(this.statData);
        this.sp2 = new JScrollPane(this.playerList);
        this.domain = "https://pesstatsdatabasedapi.herokuapp.com";
        this.setTitle("PesStatsDatabase");
        this.setLayout(null);
        this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        this.setModalityType(Dialog.ModalityType.MODELESS);
        this.setupGUI();
    }

    void setupGUI() {
        this.teamSearch = new JTextField();
        this.teamSearch.setLocation(9, 35);
        this.teamSearch.setSize(158, 28);
        this.teamSearch.setBackground(new Color(-1));
        this.teamSearch.setText("");
        this.teamSearch.setColumns(10);
        this.add(this.teamSearch);
        this.teamSearchLbl = new JLabel();
        this.teamSearchLbl.setLocation(6, 6);
        this.teamSearchLbl.setSize(153, 25);
        this.teamSearchLbl.setText("Team name search:");
        this.add(this.teamSearchLbl);
        this.teamSelection = new JComboBox();
        this.teamSelection.setLocation(9, 78);
        this.teamSelection.setSize(253, 30);
        this.teamSelection.setEditable(false);
        this.add(this.teamSelection);
        this.teamSelection.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                PSDConnPanel.this.playerListModel.clear();
                String teamDesc = (String)PSDConnPanel.this.teamSelection.getSelectedItem();
                String teamNo = teamDesc.split(";")[0];
                if (teamNo == "") {
                    return;
                }
                String res = PSDConnPanel.getHTML("https://pesstatsdatabasedapi.herokuapp.com/index.php?t=" + teamNo.trim());
                String[] lines = res.replaceAll("(?m)^[ \t]*\r?\n", "").split("\\r?\\n");
                int i = 0;
                while (i < lines.length) {
                    PSDConnPanel.this.playerListModel.addElement(lines[i]);
                    ++i;
                }
            }
        });
        this.teamSearchButton = new JButton();
        this.teamSearchButton.setLocation(176, 33);
        this.teamSearchButton.setSize(85, 33);
        this.teamSearchButton.setText("Search");
        this.add(this.teamSearchButton);
        this.teamSearchButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                PSDConnPanel.this.teamSelection.removeAllItems();
                String teamName = PSDConnPanel.this.teamSearch.getText();
                if (teamName == "") {
                    return;
                }
                String res = PSDConnPanel.getHTML("https://pesstatsdatabasedapi.herokuapp.com/index.php?n=" + teamName.trim());
                String[] lines = res.replaceAll("(?m)^[ \t]*\r?\n", "").split("\\r?\\n");
                int i = 0;
                while (i < lines.length) {
                    PSDConnPanel.this.teamSelection.addItem(lines[i]);
                    ++i;
                }
            }
        });
        this.playerListModel = new DefaultListModel();
        this.playerList = new JList<String>(this.playerListModel);
        this.playerList.setLocation(287, 77);
        this.playerList.setSize(308, 345);
        this.playerList.setBackground(new Color(-1));
        this.add(this.playerList);
        MouseAdapter playerListMouseListener = new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String playerDesc = PSDConnPanel.this.playerList.getSelectedValue();
                    if (playerDesc == "") {
                        return;
                    }
                    String[] tmp = playerDesc.split(";");
                    if (tmp.length < 2) {
                        return;
                    }
                    String playerId = tmp[1];
                    String res = PSDConnPanel.getHTML("https://pesstatsdatabasedapi.herokuapp.com/index.php?v=6&p=" + playerId.trim());
                    PSDConnPanel.this.statData.setText(res);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        };
        this.playerList.addMouseListener(playerListMouseListener);
        this.playerSearchCaption = new JLabel();
        this.playerSearchCaption.setLocation(286, 5);
        this.playerSearchCaption.setSize(116, 26);
        this.playerSearchCaption.setText("Player search:");
        this.add(this.playerSearchCaption);
        this.playerSearch = new JTextField();
        this.playerSearch.setLocation(286, 36);
        this.playerSearch.setSize(227, 31);
        this.playerSearch.setBackground(new Color(-1));
        this.playerSearch.setText("");
        this.playerSearch.setColumns(10);
        this.add(this.playerSearch);
        this.playerSearchButton = new JButton();
        this.playerSearchButton.setLocation(520, 36);
        this.playerSearchButton.setSize(85, 33);
        this.playerSearchButton.setText("Search");
        this.add(this.playerSearchButton);
        this.playerSearchButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                PSDConnPanel.this.playerListModel.clear();
                String playerName = PSDConnPanel.this.playerSearch.getText();
                if (playerName == "") {
                    return;
                }
                String res = PSDConnPanel.getHTML("https://pesstatsdatabasedapi.herokuapp.com/index.php?s=" + playerName.trim());
                String[] lines = res.replaceAll("(?m)^[ \t]*\r?\n", "").split("\\r?\\n");
                int i = 0;
                while (i < lines.length) {
                    PSDConnPanel.this.playerListModel.addElement(lines[i]);
                    ++i;
                }
            }
        });
        this.statData = new JTextArea();
        this.statData.setLocation(11, 160);
        this.statData.setSize(257, 259);
        this.statData.setText("");
        this.add(this.statData);
        this.statLbl = new JLabel();
        this.statLbl.setLocation(9, 123);
        this.statLbl.setSize(225, 25);
        this.statLbl.setText("Stat:");
        this.add(this.statLbl);
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
        this.statData.setComponentPopupMenu(menu);
        this.playerSearch.setComponentPopupMenu(menu);
        this.teamSearch.setComponentPopupMenu(menu);
        this.add(this.sp1);
        this.add(this.sp2);
        this.setSize(618, 476);
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

