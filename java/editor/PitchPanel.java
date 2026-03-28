/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.AttDefPanel;
import editor.FormPanel;
import editor.Formations;
import editor.OptionFile;
import editor.SquadList;
import editor.SquadNumList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class PitchPanel
extends JPanel
implements MouseListener,
MouseMotionListener {
    OptionFile of;
    SquadList list;
    AttDefPanel adPanel;
    int squad = 0;
    int selected = -1;
    boolean attack = true;
    boolean defence = true;
    boolean numbers = true;
    boolean roleOn = true;
    int adj = 14;
    Color[] colour = new Color[]{new Color(0, 0, 0), new Color(255, 255, 255), new Color(255, 255, 0), new Color(0, 255, 255), new Color(0, 255, 0), new Color(255, 0, 0), new Color(0, 0, 255), Color.gray};
    JComboBox altBox;
    SquadNumList numList;
    int xadj = 0;
    int yadj = 0;

    public PitchPanel(OptionFile optionFile, SquadList squadList, AttDefPanel attDefPanel, JComboBox jComboBox, SquadNumList squadNumList) {
        this.of = optionFile;
        this.list = squadList;
        this.adPanel = attDefPanel;
        this.altBox = jComboBox;
        this.numList = squadNumList;
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(329 + this.adj * 2, 200 + this.adj * 2));
        this.setBackground(Color.black);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setPaint(Color.black);
        graphics2D.fill(new Rectangle2D.Double(0.0, 0.0, 329 + this.adj * 2, 200 + this.adj * 2));
        graphics2D.setPaint(Color.gray);
        graphics2D.draw(new Rectangle2D.Double(13.0, 13.0, 331.0, 202.0));
        graphics2D.draw(new Line2D.Double(178.0, 13.0, 178.0, 215.0));
        graphics2D.draw(new Ellipse2D.Double(145.0, 81.0, 66.0, 66.0));
        graphics2D.draw(new Rectangle2D.Double(13.0, 62.0, 46.0, 104.0));
        graphics2D.draw(new Rectangle2D.Double(298.0, 62.0, 46.0, 104.0));
        graphics2D.draw(new Rectangle2D.Double(13.0, 85.0, 17.0, 58.0));
        graphics2D.draw(new Rectangle2D.Double(327.0, 85.0, 17.0, 58.0));
        graphics2D.draw(new Arc2D.Double(40.0, 89.0, 38.0, 49.0, 270.0, 180.0, 0));
        graphics2D.draw(new Arc2D.Double(279.0, 89.0, 38.0, 49.0, 90.0, 180.0, 0));
        int n = 0;
        while (n < 11) {
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            int n8;
            byte by = Formations.getPos(this.of, this.squad, this.altBox.getSelectedIndex(), n);
            if (n == 0) {
                n8 = 0 + this.adj;
                n7 = 90 + this.adj;
            } else {
                n8 = (Formations.getX(this.of, this.squad, this.altBox.getSelectedIndex(), n) - 2) * 7 + this.adj;
                n7 = (Formations.getY(this.of, this.squad, this.altBox.getSelectedIndex(), n) - 6) * 2 + this.adj;
            }
            if (n == this.selected) {
                graphics2D.setPaint(Color.white);
            } else if (by == 0) {
                graphics2D.setPaint(Color.yellow);
            } else if (by > 0 && by < 10) {
                graphics2D.setPaint(Color.cyan);
            } else if (by > 9 && by < 29) {
                graphics2D.setPaint(Color.green);
            } else if (by > 28 && by < 41) {
                graphics2D.setPaint(Color.red);
            }
            graphics2D.fill(new Ellipse2D.Double(n8, n7, 14.0, 14.0));
            if (this.roleOn) {
                graphics2D.setFont(new Font("Dialog", 1, 10));
                n6 = 0;
                if (by == 30 || by == 16 || by == 4) {
                    n6 = -1;
                }
                n5 = Formations.getAtk(this.of, this.squad, this.altBox.getSelectedIndex(), n, 2) ? 1 : 0;
                int n9 = n4 = Formations.getAtk(this.of, this.squad, this.altBox.getSelectedIndex(), n, 6) ? 1 : 0;
                if (n5 != 0 && n4 != 0) {
                    graphics2D.drawString(this.getPosLabel(by).substring(0, 1), n8 + 15, n7 + 6);
                    graphics2D.drawString(this.getPosLabel(by).substring(1, 2), n8 + 15, n7 + 16);
                } else if (by == 9 || by == 16 || by == 23 || by == 30) {
                    if (n4 == 0) {
                        graphics2D.drawString(this.getPosLabel(by), n8 + n6, n7 + 24);
                    } else {
                        graphics2D.drawString(this.getPosLabel(by), n8 + n6, n7 - 2);
                    }
                } else if (n5 != 0) {
                    graphics2D.drawString(this.getPosLabel(by), n8 + n6, n7 + 24);
                } else {
                    graphics2D.drawString(this.getPosLabel(by), n8 + n6, n7 - 2);
                }
            }
            if (this.attack) {
                n6 = n8 + 7;
                n5 = n7 + 7;
                n4 = n6;
                n3 = n5;
                n2 = 0;
                while (n2 < 8) {
                    if (Formations.getAtk(this.of, this.squad, this.altBox.getSelectedIndex(), n, n2)) {
                        switch (n2) {
                            case 0: {
                                n4 = n6 - 21;
                                n3 = n5;
                                break;
                            }
                            case 1: {
                                n4 = n6 - 15;
                                n3 = n5 - 15;
                                break;
                            }
                            case 2: {
                                n4 = n6;
                                n3 = n5 - 21;
                                break;
                            }
                            case 3: {
                                n4 = n6 + 15;
                                n3 = n5 - 15;
                                break;
                            }
                            case 4: {
                                n4 = n6 + 21;
                                n3 = n5;
                                break;
                            }
                            case 5: {
                                n4 = n6 + 15;
                                n3 = n5 + 15;
                                break;
                            }
                            case 6: {
                                n4 = n6;
                                n3 = n5 + 21;
                                break;
                            }
                            case 7: {
                                n4 = n6 - 15;
                                n3 = n5 + 15;
                            }
                        }
                        graphics2D.draw(new Line2D.Double(n6, n5, n4, n3));
                    }
                    ++n2;
                }
            }
            if (this.numbers) {
                graphics2D.setFont(new Font("Dialog", 1, 10));
                graphics2D.setPaint(Color.black);
                String string = (String)this.numList.getModel().getElementAt(n);
                n5 = 0;
                if (string.length() == 1) {
                    n5 = 3;
                }
                if (string.startsWith("1")) {
                    --n5;
                }
                graphics2D.drawString(string, n8 + 2 + n5, n7 + 11);
            }
            if (this.defence) {
                graphics2D.setPaint(Color.blue);
                int n9 = 6;
                n5 = n8 + 7 - 13 - n9 / 2;
                n4 = n7 + 7 - 5 - n9 / 2;
                n3 = n8 + 7 - 13 - n9 / 2;
                n2 = n7 + 7 + 5 - n9 / 2;
                if (Formations.getDef(this.of, this.squad, this.altBox.getSelectedIndex(), n) == 1) {
                    graphics2D.fill(new Ellipse2D.Double(n3, n2, n9, n9));
                } else if (Formations.getDef(this.of, this.squad, this.altBox.getSelectedIndex(), n) == 0) {
                    graphics2D.fill(new Ellipse2D.Double(n5, n4, n9, n9));
                    graphics2D.fill(new Ellipse2D.Double(n3, n2, n9, n9));
                }
            }
            ++n;
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Ellipse2D.Double double_;
        this.selected = -1;
        int n = 1;
        while (this.selected == -1 && n < 11) {
            int n2;
            int n3 = (Formations.getX(this.of, this.squad, this.altBox.getSelectedIndex(), n) - 2) * 7 + this.adj;
            double_ = new Ellipse2D.Double(n3, n2 = (Formations.getY(this.of, this.squad, this.altBox.getSelectedIndex(), n) - 6) * 2 + this.adj, 14.0, 14.0);
            if (double_.contains(mouseEvent.getX(), mouseEvent.getY())) {
                this.selected = n;
                this.xadj = mouseEvent.getX() - n3;
                this.yadj = mouseEvent.getY() - n2;
            }
            ++n;
        }
        FormPanel.fromPitch = true;
        if (this.selected != -1) {
            this.list.setSelectedIndex(this.selected);
            this.adPanel.selected = this.selected;
        } else {
            double_ = new Ellipse2D.Double(0 + this.adj, 90 + this.adj, 14.0, 14.0);
            if (double_.contains(mouseEvent.getX(), mouseEvent.getY())) {
                this.selected = 0;
                this.list.setSelectedIndex(this.selected);
                this.adPanel.selected = this.selected;
            } else {
                this.list.clearSelection();
                this.adPanel.selected = -1;
            }
        }
        this.repaint();
        this.adPanel.repaint();
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

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.selected > 0) {
            int n = mouseEvent.getX() - this.xadj;
            int n2 = mouseEvent.getY() - this.yadj;
            byte by = Formations.getPos(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected);
            if (n < 0 + this.adj) {
                n = 0 + this.adj;
            }
            if (n2 < 0 + this.adj) {
                n2 = 0 + this.adj;
            }
            if (n > 315 + this.adj) {
                n = 315 + this.adj;
            }
            if (n2 > 186 + this.adj) {
                n2 = 186 + this.adj;
            }
            n = (n - this.adj) / 7 + 2;
            n2 = (n2 - this.adj) / 2 + 6;
            if (by > 0 && by < 10) {
                if (n > 15) {
                    n = 15;
                }
            } else if (by > 9 && by < 29) {
                if (n < 16) {
                    n = 16;
                } else if (n > 34) {
                    n = 34;
                }
            } else if (by > 28 && by < 41 && n < 35) {
                n = 35;
            }
            if ((by == 8 || by == 15 || by == 22 || by == 29) && n2 > 50) {
                n2 = 50;
            }
            if ((by == 9 || by == 16 || by == 23 || by == 30) && n2 < 54) {
                n2 = 54;
            }
            Formations.setX(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, n);
            Formations.setY(this.of, this.squad, this.altBox.getSelectedIndex(), this.selected, n2);
            this.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    private String getPosLabel(int n) {
        String string = "";
        if (n == 0) {
            string = "GK";
        }
        if (n > 0 && n < 4 || n > 4 && n < 8) {
            string = "CB";
        }
        if (n == 4) {
            string = "CW";
        }
        if (n == 8) {
            string = "LB";
        }
        if (n == 9) {
            string = "RB";
        }
        if (n > 9 && n < 15) {
            string = "DM";
        }
        if (n == 15) {
            string = "LW";
        }
        if (n == 16) {
            string = "RW";
        }
        if (n > 16 && n < 22) {
            string = "CM";
        }
        if (n == 22) {
            string = "LM";
        }
        if (n == 23) {
            string = "RM";
        }
        if (n > 23 && n < 29) {
            string = "AM";
        }
        if (n == 29) {
            string = "LW";
        }
        if (n == 30) {
            string = "RW";
        }
        if (n > 30 && n < 36) {
            string = "SS";
        }
        if (n > 35 && n < 41) {
            string = "CF";
        }
        if (n > 40) {
            string = String.valueOf(n);
        }
        return string;
    }
}

