/*
 * Decompiled with CFR 0.151.
 */
package editor;

import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class CSVSwitch
extends JPanel {
    JCheckBox head = new JCheckBox("Column Headings");
    JCheckBox create = new JCheckBox("Created Players");

    public CSVSwitch() {
        super(new GridLayout(0, 1));
        this.add(this.head);
        this.add(this.create);
    }
}

