/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.CancelButton;
import editor.Editor;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class BackChooserDialog
extends JDialog
implements ActionListener {
    JButton[] flagButton = new JButton[12];
    byte slot;
    Raster[] raster = new Raster[12];

    public BackChooserDialog(Frame frame) {
        super(frame, "Choose Background", true);
        Object object;
        Object object2;
        JPanel jPanel = new JPanel(new GridLayout(3, 4));
        int n = 0;
        while (n < 12) {
            this.flagButton[n] = new JButton();
            object2 = Editor.class.getResource("data/backflag" + String.valueOf(n) + ".png");
            object = null;
            if (object2 != null) {
                try {
                    object = ImageIO.read((URL)object2);
                    this.raster[n] = ((BufferedImage)object).getData();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
            this.flagButton[n].setMargin(new Insets(0, 0, 0, 0));
            this.flagButton[n].setActionCommand(new Integer(n).toString());
            this.flagButton[n].addActionListener(this);
            jPanel.add(this.flagButton[n]);
            ++n;
        }
        byte[] byArray = new byte[2];
        byArray[1] = -1;
        byte[] byArray2 = byArray;
        byte[] byArray3 = new byte[2];
        byArray3[1] = -1;
        object2 = byArray3;
        byte[] byArray4 = new byte[2];
        byArray4[1] = -1;
        object = byArray4;
        this.refresh(null, byArray2, (byte[])object, (byte[])object2);
        CancelButton cancelButton = new CancelButton(this);
        this.getContentPane().add((Component)cancelButton, "South");
        this.getContentPane().add((Component)jPanel, "Center");
        this.slot = (byte)99;
        this.pack();
        this.setResizable(false);
    }

    public byte getBack(Image image, byte[] byArray, byte[] byArray2, byte[] byArray3) {
        this.slot = (byte)99;
        this.refresh(image, byArray, byArray2, byArray3);
        this.setVisible(true);
        return this.slot;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.slot = new Byte(((JButton)actionEvent.getSource()).getActionCommand());
        this.setVisible(false);
    }

    private void refresh(Image image, byte[] byArray, byte[] byArray2, byte[] byArray3) {
        byte by = 0;
        while (by < 12) {
            this.flagButton[by].setIcon(this.getFlagBG(image, by, byArray, byArray2, byArray3));
            by = (byte)(by + 1);
        }
    }

    public ImageIcon getFlagBG(Image image, byte by, byte[] byArray, byte[] byArray2, byte[] byArray3) {
        IndexColorModel indexColorModel = new IndexColorModel(1, 2, byArray, byArray2, byArray3);
        BufferedImage bufferedImage = new BufferedImage(indexColorModel, (WritableRaster)this.raster[by], false, null);
        BufferedImage bufferedImage2 = new BufferedImage(85, 64, 6);
        Graphics2D graphics2D = (Graphics2D)bufferedImage2.getGraphics();
        graphics2D.drawImage((Image)bufferedImage, 0, 0, null);
        if (image != null) {
            graphics2D.drawImage(image, 11, 0, null);
        }
        ImageIcon imageIcon = new ImageIcon(bufferedImage2);
        return imageIcon;
    }
}

