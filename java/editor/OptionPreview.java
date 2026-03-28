/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.PESUtils;
import editor.XportInfo;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

public class OptionPreview
extends JPanel
implements PropertyChangeListener {
    private File file = null;
    private XportInfo xpInfo = new XportInfo();
    private JTextArea previewText = new JTextArea(20, 19);
    private FileFilter filter;

    public OptionPreview(JFileChooser jFileChooser) {
        this.filter = jFileChooser.getFileFilter();
        this.setBorder(BorderFactory.createTitledBorder("Details"));
        this.previewText.setFont(new Font("SansSerif", 0, 12));
        this.previewText.setEditable(false);
        this.previewText.setLineWrap(true);
        this.previewText.setWrapStyleWord(true);
        jFileChooser.addPropertyChangeListener(this);
        this.add(this.previewText);
    }

    public void loadImage() {
        if (this.file == null || this.file.isDirectory() || !this.filter.accept(this.file)) {
            this.previewText.setText("");
            return;
        }
        String string = PESUtils.getExtension(this.file);
        if (string != null) {
            if (this.xpInfo.getInfo(this.file)) {
                if (string.equals("xps")) {
                    this.previewText.setText(String.valueOf(String.valueOf(this.xpInfo.game)) + "\n\nX-Port game name:\n" + this.xpInfo.gameName + "\n\nX-Port save name:\n" + this.xpInfo.saveName + "\n\nX-Port notes:\n" + this.xpInfo.notes);
                } else if (string.equals("max")) {
                    this.previewText.setText(String.valueOf(String.valueOf(this.xpInfo.game)) + "\n\n" + this.xpInfo.gameName);
                } else {
                    this.previewText.setText(this.xpInfo.game);
                }
            }
        } else {
            this.previewText.setText("PC");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        boolean bl = false;
        String string = propertyChangeEvent.getPropertyName();
        if ("directoryChanged".equals(string)) {
            this.file = null;
            bl = true;
        } else if ("SelectedFileChangedProperty".equals(string)) {
            this.file = (File)propertyChangeEvent.getNewValue();
            bl = true;
        }
        if (bl) {
            this.previewText.setText("");
            if (this.isShowing()) {
                this.loadImage();
                this.repaint();
            }
        }
    }
}

