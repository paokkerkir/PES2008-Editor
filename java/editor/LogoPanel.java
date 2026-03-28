/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.GIFPNGFilter;
import editor.LogoImportDialog;
import editor.Logos;
import editor.OptionFile;
import editor.PESUtils;
import editor.PNGFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LogoPanel
extends JPanel {
    private JButton[] flagButton;
    private boolean trans = true;
    private OptionFile of;
    private JFileChooser chooser = new JFileChooser();
    private JFileChooser chooserPNG = new JFileChooser();
    private GIFPNGFilter filter = new GIFPNGFilter();
    private PNGFilter pngFilter = new PNGFilter();
    private LogoImportDialog imageImpDia;

    public LogoPanel(OptionFile optionFile, LogoImportDialog logoImportDialog) {
        this.of = optionFile;
        this.imageImpDia = logoImportDialog;
        this.chooser.addChoosableFileFilter(this.filter);
        this.chooser.setAcceptAllFileFilterUsed(false);
        this.chooser.setDialogTitle("Import Logo");
        this.chooserPNG.addChoosableFileFilter(this.pngFilter);
        this.chooserPNG.setAcceptAllFileFilterUsed(false);
        this.chooserPNG.setDialogTitle("Export Logo");
        this.flagButton = new JButton[80];
        JPanel jPanel = new JPanel(new GridLayout(8, 10));
        PESUtils.javaUI();
        int n = 0;
        while (n < 80) {
            this.flagButton[n] = new JButton();
            this.flagButton[n].setBackground(new Color(204, 204, 204));
            this.flagButton[n].setMargin(new Insets(0, 0, 0, 0));
            this.flagButton[n].setActionCommand(new Integer(n).toString());
            this.flagButton[n].addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    int n;
                    int n2 = new Integer(((JButton)actionEvent.getSource()).getActionCommand());
                    ImageIcon imageIcon = null;
                    imageIcon = new ImageIcon(Logos.get(LogoPanel.this.of, n2, !LogoPanel.this.trans));
                    Object[] objectArray = new Object[]{"Import PNG / GIF", "Export as PNG", "Import (OF2)", "Cancel"};
                    Object[] objectArray2 = new Object[]{"Import PNG / GIF", "Export as PNG", "Cancel"};
                    Object[] objectArray3 = ((LogoPanel)LogoPanel.this).imageImpDia.of2Open ? objectArray : objectArray2;
                    int n3 = JOptionPane.showOptionDialog(null, "Options:", "Logo", 1, 3, imageIcon, objectArray3, objectArray3[0]);
                    if (n3 == 0 && (n = LogoPanel.this.chooser.showOpenDialog(null)) == 0) {
                        File file = LogoPanel.this.chooser.getSelectedFile();
                        try {
                            BufferedImage bufferedImage = ImageIO.read(file);
                            int n4 = LogoPanel.this.checkImage(bufferedImage);
                            if (n4 != -1 && n4 < 16) {
                                Logos.set(LogoPanel.this.of, n2, bufferedImage);
                                LogoPanel.this.flagButton[n2].setIcon(new ImageIcon(Logos.get(LogoPanel.this.of, n2, !LogoPanel.this.trans)));
                            }
                        }
                        catch (Exception exception) {
                            JOptionPane.showMessageDialog(null, "Could not access file", "Error", 0);
                        }
                    }
                    if (n3 == 1 && Logos.isUsed(LogoPanel.this.of, n2)) {
                        LogoPanel.this.savePNG(n2);
                    }
                    if (((LogoPanel)LogoPanel.this).imageImpDia.of2Open && n3 == 2) {
                        LogoPanel.this.imageImpDia.show(n2, "Import Logo");
                        LogoPanel.this.flagButton[n2].setIcon(new ImageIcon(Logos.get(LogoPanel.this.of, n2, !LogoPanel.this.trans)));
                    }
                }
            });
            jPanel.add(this.flagButton[n]);
            ++n;
        }
        PESUtils.systemUI();
        JButton jButton = new JButton("Transparency");
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                LogoPanel.this.trans = !LogoPanel.this.trans;
                LogoPanel.this.refresh();
            }
        });
        JPanel jPanel2 = new JPanel(new BorderLayout());
        jPanel2.add((Component)jPanel, "Center");
        jPanel2.add((Component)jButton, "South");
        this.add(jPanel2);
        this.refresh();
    }

    private void savePNG(int n) {
        boolean bl = false;
        int n2 = this.chooserPNG.showSaveDialog(null);
        if (n2 == 0) {
            File file = this.chooserPNG.getSelectedFile();
            if (PESUtils.getExtension(file) == null || !PESUtils.getExtension(file).equals("png")) {
                file = new File(String.valueOf(String.valueOf(file.getParent())) + File.separator + file.getName() + ".png");
            }
            if (file.exists()) {
                int n3 = JOptionPane.showConfirmDialog(null, String.valueOf(String.valueOf(file.getName())) + "\nAlready exists in:\n" + file.getParent() + "\nAre you sure you want to overwrite this file?", "Overwrite:  " + file.getName(), 0, 2, null);
                if (n3 == 0) {
                    boolean bl2 = file.delete();
                    if (!bl2) {
                        JOptionPane.showMessageDialog(null, "Could not access file", "Error", 0);
                        return;
                    }
                } else {
                    return;
                }
            }
            if (this.writeFile(file, n)) {
                JOptionPane.showMessageDialog(null, String.valueOf(String.valueOf(file.getName())) + "\nSaved in:\n" + file.getParent(), "File Successfully Saved", 1);
            } else {
                bl = true;
            }
            if (bl) {
                JOptionPane.showMessageDialog(null, "Could not access file", "Error", 0);
            }
        }
    }

    private boolean writeFile(File file, int n) {
        boolean bl = false;
        BufferedImage bufferedImage = Logos.get(this.of, n, false);
        try {
            ImageIO.write((RenderedImage)bufferedImage, "png", file);
            bl = true;
        }
        catch (IOException iOException) {
            bl = false;
        }
        return bl;
    }

    public void refresh() {
        int n = 0;
        while (n < 80) {
            this.flagButton[n].setIcon(new ImageIcon(Logos.get(this.of, n, !this.trans)));
            ++n;
        }
    }

    private int checkImage(BufferedImage bufferedImage) {
        int n = -1;
        if (bufferedImage.getWidth() == 32 && bufferedImage.getHeight() == 32) {
            ColorModel colorModel = bufferedImage.getColorModel();
            if (colorModel instanceof IndexColorModel) {
                int[] nArray = new int[1024];
                Raster raster = bufferedImage.getData();
                raster.getPixels(0, 0, 32, 32, nArray);
                int n2 = 0;
                while (n2 < 1024) {
                    if (nArray[n2] > n) {
                        n = nArray[n2];
                    }
                    ++n2;
                }
                if (n > 15) {
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

    private void colourMsg() {
        JOptionPane.showMessageDialog(null, "Too many colours, maximum is 16", "Error", 0);
    }

    private void sizeMsg() {
        JOptionPane.showMessageDialog(null, "Size must be 32x32 pixels", "Error", 0);
    }
}

