/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.EmblemImportDialog;
import editor.Emblems;
import editor.GIFPNGFilter;
import editor.OptionFile;
import editor.PESUtils;
import editor.PNGFilter;
import editor.TeamPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EmblemPanel
extends JPanel
implements MouseListener {
    private JButton[] flagButton;
    private boolean trans = true;
    private OptionFile of;
    private JFileChooser chooser = new JFileChooser();
    private JFileChooser chooserPNG = new JFileChooser();
    private GIFPNGFilter filter128 = new GIFPNGFilter();
    private PNGFilter pngFilter = new PNGFilter();
    private EmblemImportDialog flagImpDia;
    private TeamPanel teamPanel;
    private JPanel flagPanel;
    private JButton addButton;
    private JButton add2Button;
    private JLabel free16Label;
    private JLabel free128Label;
    private JLabel largeFlag;

    public EmblemPanel(OptionFile optionFile, EmblemImportDialog emblemImportDialog, TeamPanel teamPanel) {
        this.of = optionFile;
        this.flagImpDia = emblemImportDialog;
        this.teamPanel = teamPanel;
        this.chooser.addChoosableFileFilter(this.filter128);
        this.chooser.setAcceptAllFileFilterUsed(false);
        this.chooser.setDialogTitle("Import Emblem");
        this.chooserPNG.addChoosableFileFilter(this.pngFilter);
        this.chooserPNG.setAcceptAllFileFilterUsed(false);
        this.chooserPNG.setDialogTitle("Export Emblem");
        this.flagButton = new JButton[60];
        this.flagPanel = new JPanel(new GridLayout(6, 10));
        PESUtils.javaUI();
        int n = 0;
        while (n < 60) {
            this.flagButton[n] = new JButton();
            this.flagButton[n].setBackground(new Color(204, 204, 204));
            this.flagButton[n].setMargin(new Insets(0, 0, 0, 0));
            this.flagButton[n].setActionCommand(new Integer(n).toString());
            this.flagButton[n].addMouseListener(this);
            this.flagButton[n].addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    int n;
                    int n2 = new Integer(((JButton)actionEvent.getSource()).getActionCommand());
                    ImageIcon imageIcon = null;
                    boolean bl = false;
                    if (n2 >= Emblems.count16(EmblemPanel.this.of)) {
                        bl = true;
                        n2 = 59 - n2;
                        imageIcon = new ImageIcon(Emblems.get128(EmblemPanel.this.of, n2, !EmblemPanel.this.trans, false));
                    } else {
                        imageIcon = new ImageIcon(Emblems.get16(EmblemPanel.this.of, n2, !EmblemPanel.this.trans, false));
                    }
                    Object[] objectArray = new Object[]{"Delete", "Import PNG / GIF", "Export as PNG", "Import (OF2)", "Cancel"};
                    Object[] objectArray2 = new Object[]{"Delete", "Import PNG / GIF", "Export as PNG", "Cancel"};
                    Object[] objectArray3 = ((EmblemPanel)EmblemPanel.this).flagImpDia.of2Open ? objectArray : objectArray2;
                    int n3 = JOptionPane.showOptionDialog(null, "Options:", "Emblem", 1, 3, imageIcon, objectArray3, objectArray3[0]);
                    if (n3 == 0) {
                        if (bl) {
                            Emblems.delete128(EmblemPanel.this.of, n2);
                        } else {
                            Emblems.delete16(EmblemPanel.this.of, n2);
                        }
                        EmblemPanel.this.teamPanel.refresh();
                        EmblemPanel.this.refresh();
                    }
                    if (n3 == 1 && (n = EmblemPanel.this.chooser.showOpenDialog(null)) == 0) {
                        File file = EmblemPanel.this.chooser.getSelectedFile();
                        try {
                            BufferedImage bufferedImage = ImageIO.read(file);
                            int n4 = EmblemPanel.this.checkImage(bufferedImage);
                            if (n4 != -1) {
                                if (bl) {
                                    if (n4 < 128) {
                                        if (n4 > 15) {
                                            Emblems.set128(EmblemPanel.this.of, n2, bufferedImage);
                                        } else {
                                            EmblemPanel.this.wasteMsg();
                                        }
                                    }
                                } else if (n4 < 16) {
                                    Emblems.set16(EmblemPanel.this.of, n2, bufferedImage);
                                } else {
                                    EmblemPanel.this.col16Msg();
                                }
                                EmblemPanel.this.teamPanel.refresh();
                                EmblemPanel.this.refresh();
                            }
                        }
                        catch (Exception exception) {
                            JOptionPane.showMessageDialog(null, "Could not open file", "Error", 0);
                        }
                    }
                    if (n3 == 2) {
                        EmblemPanel.this.savePNG(bl, n2);
                    }
                    if (((EmblemPanel)EmblemPanel.this).flagImpDia.of2Open && n3 == 3) {
                        n = -1;
                        if (bl) {
                            n = EmblemPanel.this.flagImpDia.getFlag("Import Emblem", 2);
                            if (n != -1) {
                                Emblems.import128(EmblemPanel.this.of, n2, ((EmblemPanel)EmblemPanel.this).flagImpDia.of, n);
                            }
                        } else {
                            n = EmblemPanel.this.flagImpDia.getFlag("Import Emblem", 1);
                            if (n != -1) {
                                Emblems.import16(EmblemPanel.this.of, n2, ((EmblemPanel)EmblemPanel.this).flagImpDia.of, n -= 30);
                            }
                        }
                        EmblemPanel.this.teamPanel.refresh();
                        EmblemPanel.this.refresh();
                    }
                }
            });
            this.flagPanel.add(this.flagButton[n]);
            ++n;
        }
        PESUtils.systemUI();
        JButton jButton = new JButton("Transparency");
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EmblemPanel.this.trans = !EmblemPanel.this.trans;
                EmblemPanel.this.refresh();
            }
        });
        this.free16Label = new JLabel();
        this.free128Label = new JLabel();
        this.addButton = new JButton("Add Emblem");
        this.addButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n;
                if ((Emblems.getFree128(EmblemPanel.this.of) > 0 || Emblems.getFree16(EmblemPanel.this.of) > 0) && (n = EmblemPanel.this.chooser.showOpenDialog(null)) == 0) {
                    File file = EmblemPanel.this.chooser.getSelectedFile();
                    try {
                        BufferedImage bufferedImage = ImageIO.read(file);
                        int n2 = EmblemPanel.this.checkImage(bufferedImage);
                        if (n2 != -1) {
                            if (n2 < 16) {
                                Emblems.set16(EmblemPanel.this.of, Emblems.count16(EmblemPanel.this.of), bufferedImage);
                            } else if (Emblems.getFree128(EmblemPanel.this.of) == 0) {
                                EmblemPanel.this.noSpaceMsg();
                            } else {
                                Emblems.set128(EmblemPanel.this.of, Emblems.count128(EmblemPanel.this.of), bufferedImage);
                            }
                            EmblemPanel.this.teamPanel.refresh();
                            EmblemPanel.this.refresh();
                        }
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Could not open file", "Error", 0);
                    }
                }
            }
        });
        this.add2Button = new JButton("Add Emblem (OF2)");
        this.add2Button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n = -1;
                if (Emblems.getFree128(EmblemPanel.this.of) > 0) {
                    n = EmblemPanel.this.flagImpDia.getFlag("Import Emblem", 0);
                } else if (Emblems.getFree16(EmblemPanel.this.of) > 0) {
                    n = EmblemPanel.this.flagImpDia.getFlag("Import Emblem", 1);
                }
                if (n != -1) {
                    if (n > 29) {
                        Emblems.import16(EmblemPanel.this.of, Emblems.count16(EmblemPanel.this.of), ((EmblemPanel)EmblemPanel.this).flagImpDia.of, n -= 30);
                    } else {
                        Emblems.import128(EmblemPanel.this.of, Emblems.count128(EmblemPanel.this.of), ((EmblemPanel)EmblemPanel.this).flagImpDia.of, n);
                    }
                    EmblemPanel.this.teamPanel.refresh();
                    EmblemPanel.this.refresh();
                }
            }
        });
        JPanel jPanel = new JPanel();
        JPanel jPanel2 = new JPanel(new GridLayout(0, 1));
        jPanel2.add(this.free16Label);
        jPanel2.add(this.free128Label);
        jPanel2.add(this.addButton);
        jPanel2.add(this.add2Button);
        jPanel.add(jPanel2);
        this.largeFlag = new JLabel();
        this.largeFlag.setIcon(new ImageIcon(Emblems.get16(this.of, -1, false, false)));
        JPanel jPanel3 = new JPanel(new BorderLayout());
        jPanel3.setBorder(BorderFactory.createLineBorder(Color.gray));
        jPanel3.add((Component)new JLabel("16 Colour Format"), "North");
        jPanel3.add((Component)this.flagPanel, "Center");
        jPanel3.add((Component)new JLabel("128 Colour Format", 4), "South");
        JPanel jPanel4 = new JPanel(new BorderLayout());
        jPanel4.add((Component)jPanel3, "Center");
        jPanel4.add((Component)jButton, "South");
        this.add(jPanel4);
        this.add(this.largeFlag);
        this.add(jPanel);
        this.refresh();
    }

    private void savePNG(boolean bl, int n) {
        boolean bl2 = false;
        int n2 = this.chooserPNG.showSaveDialog(null);
        if (n2 == 0) {
            File file = this.chooserPNG.getSelectedFile();
            if (PESUtils.getExtension(file) == null || !PESUtils.getExtension(file).equals("png")) {
                file = new File(String.valueOf(String.valueOf(file.getParent())) + File.separator + file.getName() + ".png");
            }
            if (file.exists()) {
                int n3 = JOptionPane.showConfirmDialog(null, String.valueOf(String.valueOf(file.getName())) + "\nAlready exists in:\n" + file.getParent() + "\nAre you sure you want to overwrite this file?", "Overwrite:  " + file.getName(), 0, 2, null);
                if (n3 == 0) {
                    boolean bl3 = file.delete();
                    if (!bl3) {
                        JOptionPane.showMessageDialog(null, "Could not access file", "Error", 0);
                        return;
                    }
                } else {
                    return;
                }
            }
            if (this.writeFile(file, bl, n)) {
                JOptionPane.showMessageDialog(null, String.valueOf(String.valueOf(file.getName())) + "\nSaved in:\n" + file.getParent(), "File Successfully Saved", 1);
            } else {
                bl2 = true;
            }
            if (bl2) {
                JOptionPane.showMessageDialog(null, "Could not access file", "Error", 0);
            }
        }
    }

    private boolean writeFile(File file, boolean bl, int n) {
        boolean bl2 = false;
        BufferedImage bufferedImage = bl ? (BufferedImage)Emblems.get128(this.of, n, false, false) : (BufferedImage)Emblems.get16(this.of, n, false, false);
        try {
            ImageIO.write((RenderedImage)bufferedImage, "png", file);
            bl2 = true;
        }
        catch (IOException iOException) {
            bl2 = false;
        }
        return bl2;
    }

    public void refresh() {
        int n = 0;
        while (n < Emblems.count16(this.of)) {
            this.flagButton[n].setIcon(new ImageIcon(Emblems.get16(this.of, n, !this.trans, true)));
            this.flagButton[n].setVisible(true);
            ++n;
        }
        n = 0;
        while (n < Emblems.count128(this.of)) {
            this.flagButton[59 - n].setIcon(new ImageIcon(Emblems.get128(this.of, n, !this.trans, true)));
            this.flagButton[59 - n].setVisible(true);
            ++n;
        }
        n = Emblems.count16(this.of);
        while (n < 60 - Emblems.count128(this.of)) {
            this.flagButton[n].setVisible(false);
            ++n;
        }
        this.free16Label.setText("16-colour, can stock: " + Emblems.getFree16(this.of));
        this.free128Label.setText("128-colour, can stock: " + Emblems.getFree128(this.of));
        if (this.flagImpDia.of2Open) {
            this.add2Button.setVisible(true);
        } else {
            this.add2Button.setVisible(false);
        }
        if (Emblems.getFree16(this.of) > 0) {
            this.addButton.setEnabled(true);
            this.add2Button.setEnabled(true);
        } else {
            this.addButton.setEnabled(false);
            this.add2Button.setEnabled(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        JButton jButton = (JButton)mouseEvent.getSource();
        int n = new Integer(jButton.getActionCommand());
        if (n >= Emblems.count16(this.of)) {
            n = 59 - n;
            this.largeFlag.setIcon(new ImageIcon(Emblems.get128(this.of, n, !this.trans, false)));
        } else {
            this.largeFlag.setIcon(new ImageIcon(Emblems.get16(this.of, n, !this.trans, false)));
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        this.largeFlag.setIcon(new ImageIcon(Emblems.get16(this.of, -1, false, false)));
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    private int checkImage(BufferedImage bufferedImage) {
        int n = -1;
        if (bufferedImage.getWidth() == 64 && bufferedImage.getHeight() == 64) {
            ColorModel colorModel = bufferedImage.getColorModel();
            if (colorModel instanceof IndexColorModel) {
                int[] nArray = new int[4096];
                Raster raster = bufferedImage.getData();
                raster.getPixels(0, 0, 64, 64, nArray);
                int n2 = 0;
                while (n2 < 4096) {
                    if (nArray[n2] > n) {
                        n = nArray[n2];
                    }
                    ++n2;
                }
                if (n > 127) {
                    this.colourMsg();
                    n = -1;
                }
            } else {
                this.notIndexMsg();
            }
        } else {
            this.sizeMsg();
        }
        return n;
    }

    private void notIndexMsg() {
        JOptionPane.showMessageDialog(null, "PNG files must be INDEXED format", "Error", 0);
    }

    private void noSpaceMsg() {
        JOptionPane.showMessageDialog(null, "Not enough space for a 128-colour emblem", "Error", 0);
    }

    private void colourMsg() {
        JOptionPane.showMessageDialog(null, "Too many colours, maximum is 128", "Error", 0);
    }

    private void sizeMsg() {
        JOptionPane.showMessageDialog(null, "Size must be 64x64 pixels", "Error", 0);
    }

    private void col16Msg() {
        JOptionPane.showMessageDialog(null, "Too many colours for a 16-colour slot", "Error", 0);
    }

    private void wasteMsg() {
        JOptionPane.showMessageDialog(null, "A 16 colour image in a 128-colour slot would waste space!", "Error", 0);
    }
}

