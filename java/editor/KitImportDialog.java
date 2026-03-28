/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.CancelButton;
import editor.Clubs;
import editor.Kits;
import editor.OptionFile;
import editor.PESUtils;
import editor.Stats;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class KitImportDialog
extends JDialog
implements MouseListener {
    OptionFile of2;
    JLabel fileLabel;
    JList list;
    int index;

    public KitImportDialog(Frame frame, OptionFile optionFile) {
        super(frame, "Import Kit", true);
        this.of2 = optionFile;
        JPanel jPanel = new JPanel(new BorderLayout());
        this.fileLabel = new JLabel("From:");
        this.list = new JList();
        this.list.addMouseListener(this);
        this.list.setSelectionMode(0);
        this.list.setLayoutOrientation(0);
        this.list.setVisibleRowCount(20);
        JScrollPane jScrollPane = new JScrollPane(22, 31);
        jScrollPane.setViewportView(this.list);
        CancelButton cancelButton = new CancelButton(this);
        jPanel.add((Component)this.fileLabel, "North");
        jPanel.add((Component)jScrollPane, "Center");
        jPanel.add((Component)cancelButton, "South");
        this.getContentPane().add(jPanel);
        this.index = 0;
    }

    public int show(int n) {
        this.index = -1;
        this.refresh(n);
        this.setVisible(true);
        return this.index;
    }

    public void refresh(int n) {
        DefaultListModel<KitItem> defaultListModel = new DefaultListModel<KitItem>();
        defaultListModel.removeAllElements();
        if (n < 148) {
            defaultListModel.addElement(new KitItem(Clubs.getName(this.of2, n), n));
            int n2 = 0;
            while (n2 < 148) {
                if (n2 != n && !Kits.isLic(this.of2, n2)) {
                    defaultListModel.addElement(new KitItem(Clubs.getName(this.of2, n2), n2));
                }
                ++n2;
            }
        } else {
            if (n - 148 > 56) {
                defaultListModel.addElement(new KitItem(PESUtils.extraSquad[n - 148 - 57], n));
            } else {
                defaultListModel.addElement(new KitItem(Stats.nation[n - 148], n));
            }
            int n3 = 148;
            while (n3 < 212) {
                if (n3 != n && !Kits.isLic(this.of2, n3)) {
                    if (n3 - 148 > 56) {
                        defaultListModel.addElement(new KitItem(PESUtils.extraSquad[n3 - 148 - 57], n3));
                    } else {
                        defaultListModel.addElement(new KitItem(Stats.nation[n3 - 148], n3));
                    }
                }
                ++n3;
            }
        }
        defaultListModel.trimToSize();
        this.list.setModel(defaultListModel);
        this.fileLabel.setText("  From:  " + this.of2.fileName);
        this.pack();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int n = mouseEvent.getClickCount();
        JList jList = (JList)mouseEvent.getSource();
        KitItem kitItem = (KitItem)jList.getSelectedValue();
        if (n == 2 && kitItem != null) {
            this.index = kitItem.num;
            this.setVisible(false);
        }
    }

    private class KitItem {
        String team;
        int num;

        public KitItem(String string, int n) {
            this.team = string;
            this.num = n;
        }

        public String toString() {
            return this.team;
        }
    }
}

