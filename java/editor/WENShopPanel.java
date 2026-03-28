/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.OptionFile;
import editor.ShopPanel;
import editor.WENPanel;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class WENShopPanel
extends JPanel {
    WENPanel wenPanel;
    ShopPanel shopPanel;

    public WENShopPanel(OptionFile optionFile) {
        JPanel jPanel = new JPanel(new GridLayout(0, 1));
        this.wenPanel = new WENPanel(optionFile);
        this.shopPanel = new ShopPanel(optionFile);
        jPanel.add(this.wenPanel);
        jPanel.add(this.shopPanel);
        this.add(jPanel);
    }
}

