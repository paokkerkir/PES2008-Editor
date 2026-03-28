/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.Stadia;
import editor.TeamPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StadiumPanel
extends JPanel
implements ActionListener,
ListSelectionListener {
    private OptionFile of;
    private JTextField editor;
    private JList list;
    private TeamPanel teamPanel;

    public StadiumPanel(OptionFile optionFile, TeamPanel teamPanel) {
        this.of = optionFile;
        this.teamPanel = teamPanel;
        this.init();
    }

    public void init() {
        this.editor = new JTextField(15);
        this.editor.setToolTipText("Enter new name and press return");
        this.editor.addActionListener(this);
        this.list = new JList();
        this.list.addListSelectionListener(this);
        this.list.setVisibleRowCount(30);
        JScrollPane jScrollPane = new JScrollPane(20, 31);
        jScrollPane.setViewportView(this.list);
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder("Stadium Names"));
        jPanel.add(jScrollPane);
        jPanel.add(this.editor);
        this.add(jPanel);
    }

    public void refresh() {
        this.list.setListData(Stadia.get(this.of));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int n = this.list.getSelectedIndex();
        String string = this.editor.getText();
        if (n != -1 && string.length() < 61 && string.length() > 0) {
            if (!string.equals(Stadia.get(this.of, n))) {
                Stadia.set(this.of, n, string);
                this.teamPanel.refresh();
                this.refresh();
            }
            if (n < 34) {
                this.list.setSelectedIndex(n + 1);
                this.list.ensureIndexIsVisible(this.list.getSelectedIndex());
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
                this.editor.setText(Stadia.get(this.of, n));
                this.editor.selectAll();
            }
        }
    }
}

