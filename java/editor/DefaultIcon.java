/*
 * Decompiled with CFR 0.151.
 */
package editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.SwingConstants;

public class DefaultIcon
implements Icon,
SwingConstants {
    private int width = 64;
    private int height = 64;

    @Override
    public int getIconHeight() {
        return this.height;
    }

    @Override
    public int getIconWidth() {
        return this.width;
    }

    @Override
    public void paintIcon(Component component, Graphics graphics, int n, int n2) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.translate(n, n2);
        graphics2D.setFont(new Font("Dialog", 1, 18));
        graphics2D.setPaint(Color.black);
        graphics2D.drawString("Default", 0, 38);
        graphics2D.translate(-n, -n2);
    }
}

