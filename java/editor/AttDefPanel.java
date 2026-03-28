/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Formations;
import editor.OptionFile;
import editor.PitchPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class AttDefPanel
extends JPanel
implements MouseListener {
    OptionFile of;
    int squad = 0;
    int selected = -1;
    PitchPanel pitch;
    Rectangle2D[] attSqu = new Rectangle2D[8];
    JComboBox altBox;

    public AttDefPanel(OptionFile optionFile, JComboBox jComboBox) {
        this.of = optionFile;
        this.altBox = jComboBox;
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(98, 98));
        this.setBackground(Color.black);
        this.addMouseListener(this);
        this.attSqu[0] = new Rectangle2D.Double(0.0, 42.0, 14.0, 14.0);
        this.attSqu[1] = new Rectangle2D.Double(0.0, 0.0, 14.0, 14.0);
        this.attSqu[2] = new Rectangle2D.Double(42.0, 0.0, 14.0, 14.0);
        this.attSqu[3] = new Rectangle2D.Double(84.0, 0.0, 14.0, 14.0);
        this.attSqu[4] = new Rectangle2D.Double(84.0, 42.0, 14.0, 14.0);
        this.attSqu[5] = new Rectangle2D.Double(84.0, 84.0, 14.0, 14.0);
        this.attSqu[6] = new Rectangle2D.Double(42.0, 84.0, 14.0, 14.0);
        this.attSqu[7] = new Rectangle2D.Double(0.0, 84.0, 14.0, 14.0);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        if (this.selected == -1) {
            graphics2D.setPaint(Color.gray);
            graphics2D.fill(new Rectangle2D.Double(0.0, 0.0, 98.0, 98.0));
        } else {
            graphics2D.setPaint(Color.black);
            graphics2D.fill(new Rectangle2D.Double(0.0, 0.0, 98.0, 98.0));
            byte by = Formations.getPos(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected);
            if (by == 0) {
                graphics2D.setPaint(Color.yellow);
            } else if (by > 0 && by < 10) {
                graphics2D.setPaint(Color.cyan);
            } else if (by > 9 && by < 29) {
                graphics2D.setPaint(Color.green);
            } else if (by > 28 && by < 41) {
                graphics2D.setPaint(Color.red);
            }
            graphics2D.fill(new Ellipse2D.Double(42.0, 42.0, 14.0, 14.0));
            int n = 0;
            while (n < 8) {
                if (Formations.getAtk(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, n)) {
                    if (by == 0) {
                        graphics2D.setPaint(Color.yellow);
                    } else if (by > 0 && by < 10) {
                        graphics2D.setPaint(Color.cyan);
                    } else if (by > 9 && by < 29) {
                        graphics2D.setPaint(Color.green);
                    } else if (by > 28 && by < 41) {
                        graphics2D.setPaint(Color.red);
                    }
                    graphics2D.fill(this.attSqu[n]);
                } else {
                    graphics2D.setPaint(Color.gray);
                    if (this.selected != 0) {
                        graphics2D.draw(this.attSqu[n]);
                    } else if (n == 0 || n == 4) {
                        graphics2D.draw(this.attSqu[n]);
                    }
                }
                ++n;
            }
            if (Formations.getDef(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected) == 1) {
                graphics2D.setPaint(Color.gray);
                graphics2D.draw(new Ellipse2D.Double(21.0, 21.0, 14.0, 14.0));
                graphics2D.setPaint(Color.blue);
                graphics2D.fill(new Ellipse2D.Double(21.0, 63.0, 14.0, 14.0));
            } else if (Formations.getDef(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected) == 0) {
                graphics2D.setPaint(Color.blue);
                graphics2D.fill(new Ellipse2D.Double(21.0, 21.0, 14.0, 14.0));
                graphics2D.fill(new Ellipse2D.Double(21.0, 63.0, 14.0, 14.0));
            } else {
                graphics2D.setPaint(Color.gray);
                graphics2D.draw(new Ellipse2D.Double(21.0, 21.0, 14.0, 14.0));
                graphics2D.draw(new Ellipse2D.Double(21.0, 63.0, 14.0, 14.0));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        int n = 0;
        boolean bl = true;
        byte by = Formations.getDef(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected);
        if (new Ellipse2D.Double(42.0, 42.0, 14.0, 14.0).contains(mouseEvent.getX(), mouseEvent.getY())) {
            Formations.setAtk(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, -1);
            Formations.setDef(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, 2);
        } else if (new Ellipse2D.Double(21.0, 21.0, 14.0, 14.0).contains(mouseEvent.getX(), mouseEvent.getY())) {
            if (by == 0) {
                Formations.setDef(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, 1);
            } else {
                Formations.setDef(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, 0);
            }
        } else if (new Ellipse2D.Double(21.0, 63.0, 14.0, 14.0).contains(mouseEvent.getX(), mouseEvent.getY())) {
            if (by == 2) {
                Formations.setDef(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, 1);
            } else {
                Formations.setDef(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, 2);
            }
        } else {
            int n2 = 0;
            while (n2 < 8) {
                if (Formations.getAtk(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, n2)) {
                    ++n;
                }
                ++n2;
            }
            n2 = 0;
            while (bl & n2 < 8) {
                if (this.attSqu[n2].contains(mouseEvent.getX(), mouseEvent.getY())) {
                    bl = false;
                    if (Formations.getAtk(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, n2)) {
                        Formations.setAtk(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, n2);
                    } else if (n < 2) {
                        if (this.selected != 0) {
                            Formations.setAtk(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, n2);
                        } else if (n2 == 0 || n2 == 4) {
                            Formations.setAtk(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, n2);
                        }
                    }
                }
                ++n2;
            }
        }
        this.repaint();
        this.pitch.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }
}

