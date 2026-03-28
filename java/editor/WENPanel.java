/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WENPanel
extends JPanel
implements ActionListener {
    OptionFile of;
    JLabel current;
    JTextField field;

    public WENPanel(OptionFile optionFile) {
        this.of = optionFile;
        JPanel jPanel = new JPanel(new GridLayout(0, 1));
        jPanel.setBorder(BorderFactory.createTitledBorder("PES Points"));
        this.current = new JLabel("");
        this.field = new JTextField(8);
        this.field.setToolTipText("Enter an amount (0-99999) and press return");
        this.field.addActionListener(this);
        jPanel.add(this.field);
        jPanel.add(this.current);
        this.add(jPanel);
        this.refresh();
    }

    public void refresh() {
        int n = (this.of.toInt(this.of.data[54]) << 16) + (this.of.toInt(this.of.data[53]) << 8) + this.of.toInt(this.of.data[52]);
        this.current.setText("Current:  " + String.valueOf(n));
        this.field.setText("");
    }

    public void setWEN(int n) {
        if (n >= 0 && n <= 99999) {
            this.of.data[52] = this.of.toByte(n & 0xFF);
            this.of.data[53] = this.of.toByte((n & 0xFF00) >>> 8);
            this.of.data[54] = this.of.toByte((n & 0xFF0000) >>> 16);
            this.of.data[5208] = this.of.toByte(n & 0xFF);
            this.of.data[5209] = this.of.toByte((n & 0xFF00) >>> 8);
            this.of.data[5210] = this.of.toByte((n & 0xFF0000) >>> 16);
            this.refresh();
        } else {
            this.field.setText("");
            JOptionPane.showMessageDialog(null, "Amount must be in the range 0-99999", "Error", 0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            this.setWEN(new Integer(this.field.getText()));
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }
}

