/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.CancelButton;
import editor.Emblems;
import editor.OptionFile;
import editor.PESUtils;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class EmblemChooserDialog
extends JDialog {
    private JButton[] flagButton;
    private boolean trans = true;
    private OptionFile of;
    int slot;
    byte max;
    int type;

    public EmblemChooserDialog(Frame frame, OptionFile optionFile) {
        super(frame, true);
        this.of = optionFile;
        this.max = (byte)60;
        JPanel jPanel = new JPanel(new GridLayout(6, 10));
        this.flagButton = new JButton[this.max];
        PESUtils.javaUI();
        int n = 0;
        while (n < this.max) {
            this.flagButton[n] = new JButton(new ImageIcon(Emblems.get16(this.of, -1, false, true)));
            this.flagButton[n].setMargin(new Insets(0, 0, 0, 0));
            this.flagButton[n].setActionCommand(new Integer(n).toString());
            this.flagButton[n].addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    EmblemChooserDialog.this.slot = new Integer(((JButton)actionEvent.getSource()).getActionCommand());
                    EmblemChooserDialog.this.slot = EmblemChooserDialog.this.slot >= Emblems.count16(EmblemChooserDialog.this.of) ? 59 - EmblemChooserDialog.this.slot : (EmblemChooserDialog.this.slot = EmblemChooserDialog.this.slot + 30);
                    EmblemChooserDialog.this.setVisible(false);
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
                EmblemChooserDialog.this.trans = !EmblemChooserDialog.this.trans;
                EmblemChooserDialog.this.refresh();
            }
        });
        CancelButton cancelButton = new CancelButton(this);
        this.getContentPane().add((Component)jButton, "North");
        this.getContentPane().add((Component)cancelButton, "South");
        this.getContentPane().add((Component)jPanel, "Center");
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
        this.refresh();
        this.setVisible(true);
        return this.slot;
    }
}

