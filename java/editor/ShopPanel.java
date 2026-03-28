/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShopPanel
extends JPanel {
    OptionFile of;
    JLabel status;
    JButton unlock;

    public ShopPanel(OptionFile optionFile) {
        this.of = optionFile;
        JPanel jPanel = new JPanel(new GridLayout(0, 1));
        jPanel.setBorder(BorderFactory.createTitledBorder("Shop Items"));
        JPanel jPanel2 = new JPanel();
        this.status = new JLabel("");
        this.unlock = new JButton("Unlock");
        this.unlock.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n = 0;
                while (n < 20) {
                    ShopPanel.this.of.data[5144 + n] = -1;
                    ++n;
                }
                ShopPanel.this.of.data[5164] = -8;
                ShopPanel.this.of.data[5165] = 11;
                ShopPanel.this.of.data[5166] = -2;
                ShopPanel.this.of.data[5167] = -1;
                ShopPanel.this.of.data[5168] = -49;
                ShopPanel.this.of.data[5169] = 127;
                ShopPanel.this.of.data[56] = 100;
                ShopPanel.this.status.setText("Unlocked");
            }
        });
        jPanel2.add(this.unlock);
        jPanel.add(jPanel2);
        jPanel.add(this.status);
        this.add(jPanel);
    }
}

