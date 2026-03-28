/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.CancelButton;
import editor.Logos;
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
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LogoImportDialog
extends JDialog {
    private JButton[] flagButton;
    private boolean trans = true;
    private OptionFile of;
    private OptionFile of2;
    JLabel fileLabel;
    boolean of2Open;
    int slot;
    int replacement;
    byte max;
    int adr;
    int size;

    public LogoImportDialog(Frame frame, OptionFile optionFile, OptionFile optionFile2) {
        super(frame, true);
        this.of = optionFile;
        this.of2 = optionFile2;
        this.fileLabel = new JLabel("From:");
        this.max = (byte)80;
        JPanel jPanel = new JPanel(new GridLayout(8, 10));
        this.flagButton = new JButton[this.max];
        PESUtils.javaUI();
        int n = 0;
        while (n < this.max) {
            this.flagButton[n] = new JButton(new ImageIcon(Logos.get(this.of, -1, false)));
            this.flagButton[n].setMargin(new Insets(0, 0, 0, 0));
            this.flagButton[n].setActionCommand(new Integer(n).toString());
            this.flagButton[n].addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    LogoImportDialog.this.replacement = new Integer(((JButton)actionEvent.getSource()).getActionCommand());
                    LogoImportDialog.this.importFlag();
                    LogoImportDialog.this.setVisible(false);
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
                LogoImportDialog.this.trans = !LogoImportDialog.this.trans;
                LogoImportDialog.this.updateFlags();
            }
        });
        CancelButton cancelButton = new CancelButton(this);
        JPanel jPanel2 = new JPanel(new GridLayout(0, 1));
        jPanel2.add(this.fileLabel);
        jPanel2.add(jButton);
        this.getContentPane().add((Component)jPanel2, "North");
        this.getContentPane().add((Component)cancelButton, "South");
        this.getContentPane().add((Component)jPanel, "Center");
        this.of2Open = false;
        this.slot = 0;
        this.replacement = 0;
        this.setResizable(false);
        this.pack();
    }

    private void updateFlags() {
        int n = 0;
        while (n < 80) {
            this.flagButton[n].setIcon(new ImageIcon(Logos.get(this.of2, n, !this.trans)));
            ++n;
        }
    }

    public void refresh() {
        this.updateFlags();
        this.of2Open = true;
        this.slot = 0;
        this.replacement = 0;
        this.fileLabel.setText("  From:  " + this.of2.fileName);
    }

    public void show(int n, String string) {
        this.setTitle(string);
        this.slot = n;
        this.setVisible(true);
    }

    private void importFlag() {
        Logos.importLogo(this.of2, this.replacement, this.of, this.slot);
    }
}

