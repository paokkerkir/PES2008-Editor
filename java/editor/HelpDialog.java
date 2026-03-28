/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Editor;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpDialog
extends JDialog {
    private JEditorPane editpane;
    private URL helpURL;

    public HelpDialog(Frame frame) {
        super(frame, "PES Editor 2008 Help", false);
        JScrollPane jScrollPane = new JScrollPane(20, 31);
        this.helpURL = Editor.class.getResource("help/index.html");
        JButton jButton = new JButton("Close Help");
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                HelpDialog.this.setVisible(false);
            }
        });
        this.editpane = new JEditorPane();
        this.editpane.setEditable(false);
        this.editpane.addHyperlinkListener(new MyHyperlinkListener());
        jScrollPane.setViewportView(this.editpane);
        this.showPage();
        jScrollPane.setPreferredSize(new Dimension(430, 550));
        this.getContentPane().add((Component)jScrollPane, "Center");
        this.getContentPane().add((Component)jButton, "South");
        this.pack();
    }

    public void showPage() {
        if (this.helpURL != null) {
            try {
                this.editpane.setPage(this.helpURL);
            }
            catch (IOException iOException) {
                System.out.println("IOExecption while loading help page.");
            }
        } else {
            System.out.println("null.");
        }
    }

    private class MyHyperlinkListener
    implements HyperlinkListener {
        private MyHyperlinkListener() {
        }

        @Override
        public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
            if (hyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    HelpDialog.this.editpane.setPage(hyperlinkEvent.getURL());
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }
}

