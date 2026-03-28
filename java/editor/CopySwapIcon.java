/*
 * Decompiled with CFR 0.151.
 */
package editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import javax.swing.Icon;
import javax.swing.SwingConstants;

public class CopySwapIcon
implements Icon,
SwingConstants {
    private boolean swap = false;
    private int width = 10;
    private int height = 20;

    public CopySwapIcon(boolean bl) {
        this.swap = bl;
    }

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
        graphics2D.setPaint(Color.black);
        graphics2D.draw(new Line2D.Double(5.0, 0.0, 5.0, 20.0));
        graphics2D.draw(new Line2D.Double(5.0, 20.0, 0.0, 15.0));
        graphics2D.draw(new Line2D.Double(5.0, 20.0, 10.0, 15.0));
        if (this.swap) {
            graphics2D.draw(new Line2D.Double(5.0, 0.0, 0.0, 5.0));
            graphics2D.draw(new Line2D.Double(5.0, 0.0, 10.0, 5.0));
        }
        graphics2D.translate(-n, -n2);
    }
}

