/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.FormPanel;
import editor.OptionFile;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class FormationDialog
extends JDialog
implements WindowListener {
    OptionFile of;
    FormPanel formPan;
    byte[] original = new byte[364];
    int squadIndex;

    public FormationDialog(Frame frame, OptionFile optionFile) {
        super(frame, "Edit Formation", true);
        this.addWindowListener(this);
        this.of = optionFile;
        this.formPan = new FormPanel(this.of);
        JButton jButton = new JButton("Accept");
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FormationDialog.this.setVisible(false);
            }
        });
        JButton jButton2 = new JButton("Cancel");
        jButton2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n = 696638 + FormationDialog.this.squadIndex * 364;
                System.arraycopy(FormationDialog.this.original, 0, FormationDialog.this.of.data, n, 364);
                FormationDialog.this.setVisible(false);
            }
        });
        JPanel jPanel = new JPanel();
        JPanel jPanel2 = new JPanel(new BorderLayout());
        jPanel.add(jButton);
        jPanel.add(jButton2);
        jPanel2.add((Component)this.formPan, "Center");
        jPanel2.add((Component)jPanel, "South");
        this.getContentPane().add(jPanel2);
        this.pack();
        this.setResizable(false);
    }

    public void show(int n, String string) {
        this.setTitle("Edit Formation - " + string);
        this.squadIndex = n;
        int n2 = 696638 + n * 364;
        System.arraycopy(this.of.data, n2, this.original, 0, 364);
        this.formPan.refresh(n);
        this.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        int n = 696638 + this.squadIndex * 364;
        System.arraycopy(this.original, 0, this.of.data, n, 364);
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
    }
}

