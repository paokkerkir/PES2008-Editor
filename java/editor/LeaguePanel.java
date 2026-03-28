/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Leagues;
import editor.OptionFile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LeaguePanel
extends JPanel
implements ActionListener,
ListSelectionListener {
    private OptionFile of;
    private JTextField editor;
    private JList list;

    public LeaguePanel(OptionFile optionFile) {
        this.of = optionFile;
        this.init();
    }

    public void init() {
        this.editor = new JTextField(15);
        this.editor.setToolTipText("Enter new name and press return");
        this.editor.addActionListener(this);
        this.list = new JList();
        this.list.addListSelectionListener(this);
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder("League Names"));
        jPanel.add(this.list);
        jPanel.add(this.editor);
        this.add(jPanel);
    }

    public void refresh() {
        this.list.setListData(Leagues.get(this.of));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int n = this.list.getSelectedIndex();
        String string = this.editor.getText();
        if (n != -1 && string.length() <= 61 && string.length() > 0) {
            if (!string.equals(Leagues.get(this.of, n))) {
                Leagues.set(this.of, n, string);
                this.refresh();
            }
            if (n < 27) {
                this.list.setSelectedIndex(n + 1);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
            int n = this.list.getSelectedIndex();
            if (n == -1) {
                this.editor.setText("");
            } else {
                this.editor.setText(Leagues.get(this.of, n));
                this.editor.selectAll();
            }
        }
    }
}

