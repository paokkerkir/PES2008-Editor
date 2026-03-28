/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.CancelButton;
import editor.Logos;
import editor.OptionFile;
import editor.PESUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LogoChooserDialog
extends JDialog {
    private JButton[] flagButton;
    private boolean trans = true;
    private OptionFile of;
    byte slot;
    private JLabel repLabel;

    public LogoChooserDialog(Frame frame, OptionFile optionFile) {
        super(frame, true);
        this.of = optionFile;
        JPanel jPanel = new JPanel(new GridLayout(8, 10));
        this.flagButton = new JButton[80];
        PESUtils.javaUI();
        int n = 0;
        while (n < 80) {
            this.flagButton[n] = new JButton(new ImageIcon(Logos.get(this.of, -1, false)));
            this.flagButton[n].setMargin(new Insets(0, 0, 0, 0));
            this.flagButton[n].setActionCommand(new Integer(n).toString());
            this.flagButton[n].addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    LogoChooserDialog.this.slot = (byte)new Integer(((JButton)actionEvent.getSource()).getActionCommand()).intValue();
                    LogoChooserDialog.this.setVisible(false);
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
                LogoChooserDialog.this.trans = !LogoChooserDialog.this.trans;
                LogoChooserDialog.this.refresh();
            }
        });
        CancelButton cancelButton = new CancelButton(this);
        this.repLabel = new JLabel(new ImageIcon(Logos.get(this.of, -1, false)));
        JPanel jPanel2 = new JPanel(new BorderLayout());
        jPanel2.add((Component)this.repLabel, "North");
        jPanel2.add((Component)jPanel, "Center");
        this.getContentPane().add((Component)jButton, "North");
        this.getContentPane().add((Component)cancelButton, "South");
        this.getContentPane().add((Component)jPanel2, "Center");
        this.slot = (byte)88;
        this.setResizable(false);
        this.pack();
    }

    public void refresh() {
        int n = 0;
        while (n < 80) {
            this.flagButton[n].setIcon(new ImageIcon(Logos.get(this.of, n, !this.trans)));
            ++n;
        }
    }

    public byte getFlag(String string, Image image) {
        this.slot = (byte)88;
        this.setTitle(string);
        this.repLabel.setIcon(new ImageIcon(image));
        this.refresh();
        this.setVisible(true);
        return this.slot;
    }
}

