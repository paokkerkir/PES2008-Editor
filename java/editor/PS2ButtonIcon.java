/*
 * Decompiled with CFR 0.151.
 */
package editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;
import javax.swing.SwingConstants;

public class PS2ButtonIcon
implements Icon,
SwingConstants {
    private int type = 0;
    private int width = 17;
    private int height = 17;

    public PS2ButtonIcon(int n) {
        this.type = n;
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
        graphics2D.fill(new Ellipse2D.Double(1.0, 1.0, 15.0, 15.0));
        if (this.type == 3) {
            graphics2D.setPaint(Color.pink);
            graphics2D.draw(new Rectangle2D.Double(4.0, 4.0, 8.0, 8.0));
        }
        if (this.type == 2) {
            graphics2D.setPaint(Color.green);
            graphics2D.draw(new Line2D.Double(8.0, 4.0, 12.0, 12.0));
            graphics2D.draw(new Line2D.Double(4.0, 12.0, 12.0, 12.0));
            graphics2D.draw(new Line2D.Double(8.0, 4.0, 4.0, 12.0));
        }
        if (this.type == 0) {
            graphics2D.setPaint(Color.red);
            graphics2D.draw(new Ellipse2D.Double(4.0, 4.0, 8.0, 8.0));
        }
        if (this.type == 1) {
            graphics2D.setPaint(Color.cyan);
            graphics2D.draw(new Line2D.Double(4.0, 4.0, 12.0, 12.0));
            graphics2D.draw(new Line2D.Double(4.0, 12.0, 12.0, 4.0));
        }
        graphics2D.translate(-n, -n2);
    }
}

