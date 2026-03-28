/*
 * Decompiled with CFR 0.151.
 */
package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;

public class CancelButton
extends JButton {
    public CancelButton(final JDialog jDialog) {
        super("Cancel");
        this.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jDialog.setVisible(false);
            }
        });
    }
}

