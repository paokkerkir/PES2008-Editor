/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.CancelButton;
import editor.Emblems;
import editor.OptionFile;
import editor.PESUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EmblemImportDialog
extends JDialog {
    private JButton[] flagButton;
    private boolean trans = true;
    OptionFile of;
    int slot;
    byte max;
    int type;
    boolean of2Open = false;
    JLabel fileLabel;

    public EmblemImportDialog(Frame frame, OptionFile optionFile) {
        super(frame, true);
        this.of = optionFile;
        this.max = (byte)60;
        JPanel jPanel = new JPanel(new GridLayout(6, 10));
        this.flagButton = new JButton[this.max];
        this.fileLabel = new JLabel("From:");
        PESUtils.javaUI();
        int n = 0;
        while (n < this.max) {
            this.flagButton[n] = new JButton(new ImageIcon(Emblems.get16(this.of, -1, false, true)));
            this.flagButton[n].setMargin(new Insets(0, 0, 0, 0));
            this.flagButton[n].setActionCommand(new Integer(n).toString());
            this.flagButton[n].addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    EmblemImportDialog.this.slot = new Integer(((JButton)actionEvent.getSource()).getActionCommand());
                    EmblemImportDialog.this.slot = EmblemImportDialog.this.slot >= Emblems.count16(EmblemImportDialog.this.of) ? 59 - EmblemImportDialog.this.slot : (EmblemImportDialog.this.slot = EmblemImportDialog.this.slot + 30);
                    EmblemImportDialog.this.setVisible(false);
                }
            });
            jPanel.add(this.flagButton[n]);
            ++n;
        }
        PESUtils.systemUI();
        JButton jButton = new JButton("Transparency");
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EmblemImportDialog.this.trans = !EmblemImportDialog.this.trans;
                EmblemImportDialog.this.refresh();
            }
        });
        CancelButton cancelButton = new CancelButton(this);
        JPanel jPanel2 = new JPanel(new BorderLayout());
        jPanel2.add((Component)jButton, "North");
        jPanel2.add((Component)cancelButton, "South");
        jPanel2.add((Component)jPanel, "Center");
        this.getContentPane().add((Component)this.fileLabel, "North");
        this.getContentPane().add((Component)jPanel2, "Center");
        this.slot = -1;
        this.setResizable(false);
        this.pack();
    }

    public void refresh() {
        int n;
        if (this.type == 0 || this.type == 1) {
            n = 0;
            while (n < Emblems.count16(this.of)) {
                this.flagButton[n].setIcon(new ImageIcon(Emblems.get16(this.of, n, !this.trans, true)));
                this.flagButton[n].setVisible(true);
                ++n;
            }
        }
        if (this.type == 0 || this.type == 2) {
            n = 0;
            while (n < Emblems.count128(this.of)) {
                this.flagButton[59 - n].setIcon(new ImageIcon(Emblems.get128(this.of, n, !this.trans, true)));
                this.flagButton[59 - n].setVisible(true);
                ++n;
            }
        }
        n = Emblems.count16(this.of);
        int n2 = 60 - Emblems.count128(this.of);
        if (this.type == 1) {
            n2 = 60;
        }
        if (this.type == 2) {
            n = 0;
        }
        int n3 = n;
        while (n3 < n2) {
            this.flagButton[n3].setVisible(false);
            ++n3;
        }
    }

    public int getFlag(String string, int n) {
        this.type = n;
        this.slot = -1;
        this.setTitle(string);
        this.fileLabel.setText("  From:  " + this.of.fileName);
        this.refresh();
        this.setVisible(true);
        return this.slot;
    }
}

