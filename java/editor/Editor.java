/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.CSVFilter;
import editor.CSVMaker;
import editor.CSVSwitch;
import editor.EmblemChooserDialog;
import editor.EmblemImportDialog;
import editor.EmblemPanel;
import editor.FormationDialog;
import editor.GlobalPanel;
import editor.HelpDialog;
import editor.ImportPanel;
import editor.KitImportDialog;
import editor.LeaguePanel;
import editor.LogoChooserDialog;
import editor.LogoImportDialog;
import editor.LogoPanel;
import editor.OptionFile;
import editor.OptionFilter;
import editor.OptionPreview;
import editor.PESUtils;
import editor.PlayerDialog;
import editor.PlayerImportDialog;
import editor.Squads;
import editor.StadiumPanel;
import editor.TeamPanel;
import editor.TransferPanel;
import editor.WENShopPanel;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class Editor
extends JFrame {
    private JFileChooser chooser;
    private OptionFile of;
    private OptionFile of2;
    private File currentFile = null;
    private OptionFilter filter;
    protected EmblemPanel flagPanel;
    protected LogoPanel imagePanel;
    protected TransferPanel tranPanel;
    protected WENShopPanel wenShop;
    protected StadiumPanel stadPan;
    protected TeamPanel teamPan;
    protected LeaguePanel leaguePan;
    JTabbedPane tabbedPane;
    PlayerImportDialog plImpDia;
    KitImportDialog kitImpDia;
    EmblemImportDialog flagImpDia;
    LogoImportDialog imageImpDia;
    PlayerDialog playerDia;
    EmblemChooserDialog flagChooser;
    FormationDialog teamDia;
    ImportPanel importPanel;
    LogoChooserDialog logoChooser;
    private CSVMaker csvMaker;
    private JMenuItem csvItem;
    private JMenuItem saveParaItem;  // ADDED: Export DB PES2008
    private JMenuItem open2Item;
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
    private JFileChooser csvChooser;
    private CSVSwitch csvSwitch;
    private GlobalPanel globalPanel;
    private HelpDialog helpDia;
    private File settingsFile;
    private JMenuItem convertItem;

    public Editor() {
        super("PES2008 Editor v9.5");
        this.setIcon();
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.filter = new OptionFilter();
        this.tabbedPane = new JTabbedPane();
        this.csvMaker = new CSVMaker();
        this.csvChooser = new JFileChooser();
        this.csvSwitch = new CSVSwitch();
        this.of = new OptionFile();
        this.of2 = new OptionFile();
        this.csvChooser.addChoosableFileFilter(new CSVFilter());
        this.csvChooser.setAcceptAllFileFilterUsed(false);
        this.csvChooser.setAccessory(this.csvSwitch);
        this.flagChooser = new EmblemChooserDialog((Frame)this, this.of);
        this.logoChooser = new LogoChooserDialog((Frame)this, this.of);
        this.plImpDia = new PlayerImportDialog((Frame)this, this.of, this.of2);
        this.kitImpDia = new KitImportDialog((Frame)this, this.of2);
        this.flagImpDia = new EmblemImportDialog((Frame)this, this.of2);
        this.imageImpDia = new LogoImportDialog((Frame)this, this.of, this.of2);
        this.playerDia = new PlayerDialog((Frame)this, this.of, this.plImpDia);
        this.teamDia = new FormationDialog((Frame)this, this.of);
        this.tranPanel = new TransferPanel(this.playerDia, this.of, this.teamDia);
        this.imagePanel = new LogoPanel(this.of, this.imageImpDia);
        this.globalPanel = new GlobalPanel(this.of, this.tranPanel);
        this.teamPan = new TeamPanel(this.of, this.tranPanel, this.flagChooser, this.of2, this.imagePanel, this.globalPanel, this.kitImpDia, this.logoChooser);
        this.teamPan.flagPan = this.flagPanel = new EmblemPanel(this.of, this.flagImpDia, this.teamPan);
        this.wenShop = new WENShopPanel(this.of);
        this.stadPan = new StadiumPanel(this.of, this.teamPan);
        this.leaguePan = new LeaguePanel(this.of);
        this.importPanel = new ImportPanel(this.of, this.of2, this.wenShop, this.stadPan, this.leaguePan, this.teamPan, this.flagPanel, this.imagePanel, this.tranPanel);
        this.helpDia = new HelpDialog(this);
        this.tabbedPane.addTab("Transfer", null, this.tranPanel, null);
        this.tabbedPane.addTab("Team", null, this.teamPan, null);
        this.tabbedPane.addTab("Emblem", null, this.flagPanel, null);
        this.tabbedPane.addTab("Logo", null, this.imagePanel, null);
        this.tabbedPane.addTab("Stadium", null, this.stadPan, null);
        this.tabbedPane.addTab("League", null, this.leaguePan, null);
        this.tabbedPane.addTab("PES / Shop", null, this.wenShop, null);
        this.tabbedPane.addTab("Stat Adjust", null, this.globalPanel, null);
        this.tabbedPane.addTab("OF2 Import", null, this.importPanel, null);
        this.settingsFile = new File("PFE_settings.dat");
        this.chooser = new JFileChooser(this.loadSet());
        this.chooser.setAcceptAllFileFilterUsed(false);
        this.chooser.addChoosableFileFilter(this.filter);
        this.chooser.setAccessory(new OptionPreview(this.chooser));
        this.buildMenu();
        this.getContentPane().add(this.tabbedPane);
        this.pack();
        this.tabbedPane.setVisible(false);
        this.setVisible(true);
        this.openFile();
    }

    private void buildMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        JMenu jMenu2 = new JMenu("Help");
        JMenu jMenu3 = new JMenu("Tools");
        JMenuItem jMenuItem = new JMenuItem("Open...");
        this.open2Item = new JMenuItem("Open OF2...");
        this.saveItem = new JMenuItem("Save");
        this.saveAsItem = new JMenuItem("Save As...");
        JMenuItem jMenuItem2 = new JMenuItem("Exit");
        JMenuItem jMenuItem3 = new JMenuItem("PES2008 Editor v9.5 Help...");
        JMenuItem jMenuItem4 = new JMenuItem("About...");
        this.convertItem = new JMenuItem("Convert OF2 > OF1");
        this.csvItem = new JMenuItem("Make csv stats file...");
        this.saveParaItem = new JMenuItem("Export DB PES2008");  // ADDED
        jMenuItem2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        jMenuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Editor.this.openFile();
            }
        });
        this.open2Item.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n = Editor.this.chooser.showOpenDialog(Editor.this.getContentPane());
                if (n == 0 && Editor.this.filter.accept(Editor.this.chooser.getSelectedFile())) {
                   if (Editor.this.chooser.getSelectedFile().isFile() && Editor.this.of2.readXPS(Editor.this.chooser.getSelectedFile())) {
                        // Wrap Squads.fixAll in try-catch to handle PES6 converted files
                        // PES6 formations may have compatibility issues, but players/emblems will still work
                        try {
                            Squads.fixAll(Editor.this.of2);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("========================================");
                            System.out.println("Warning: Formation processing failed");
                            System.out.println("This is expected for PES6 files");
                            System.out.println("Player and emblem importing will work normally!");
                            System.out.println("========================================");
                        }
                        Editor.this.plImpDia.refresh();
                        Editor.this.flagImpDia.of2Open = true;
                        Editor.this.imageImpDia.refresh();
                        Editor.this.importPanel.refresh();
                        Editor.this.flagPanel.refresh();
                        Editor.this.teamPan.list.setToolTipText("Double click to import kit from OF2");
                        if (((Editor)Editor.this).of.fileName != null) {
                            Editor.this.convertItem.setEnabled(true);
                        } else {
                            Editor.this.convertItem.setEnabled(false);
                        }
                    } else {
                        Editor.this.teamPan.list.setToolTipText(null);
                        Editor.this.plImpDia.of2Open = false;
                        Editor.this.flagImpDia.of2Open = false;
                        Editor.this.imageImpDia.of2Open = false;
                        Editor.this.flagPanel.refresh();
                        Editor.this.importPanel.refresh();
                        Editor.this.convertItem.setEnabled(false);
                        Editor.this.openFailMsg();
                    }
                }
            }
        });
        this.saveItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Editor.this.currentFile != null) {
                    if (Editor.this.currentFile.delete() && Editor.this.of.saveXPS(Editor.this.currentFile)) {
                        Editor.this.saveOkMsg(Editor.this.currentFile);
                        Editor.this.chooser.setSelectedFile(null);
                    } else {
                        Editor.this.saveFailMsg();
                    }
                }
            }
        });
        
        // ADDED: Export DB PES2008 action listener
        this.saveParaItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Editor.this.currentFile != null) {
                    String directory = Editor.this.currentFile.getParent();
                    if (Editor.this.of.savePara(Editor.this.currentFile, directory)) {
                        JOptionPane.showMessageDialog(Editor.this.getContentPane(),
                            "PES2008 DB exported successfully!\n\nunnamed_73.bin_000 created in:\n" + directory,
                            "Export DB Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        Editor.this.chooser.setSelectedFile(null);
                    } else {
                        JOptionPane.showMessageDialog(Editor.this.getContentPane(), 
                            "Export DB failed!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        this.saveAsItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Editor.this.currentFile != null) {
                    int n = Editor.this.chooser.showSaveDialog(Editor.this.getContentPane());
                    Editor.this.saveSet();
                    if (n == 0) {
                        File file = Editor.this.chooser.getSelectedFile();
                        if (!(((Editor)Editor.this).of.format != 0 || PESUtils.getExtension(file) != null && PESUtils.getExtension(file).equals("xps"))) {
                            file = new File(String.valueOf(String.valueOf(file.getParent())) + File.separator + file.getName() + ".xps");
                        }
                        if (!(((Editor)Editor.this).of.format != 2 || PESUtils.getExtension(file) != null && PESUtils.getExtension(file).equals("psu"))) {
                            file = new File(String.valueOf(String.valueOf(file.getParent())) + File.separator + file.getName() + ".psu");
                        }
                        if (!(((Editor)Editor.this).of.format != 3 || PESUtils.getExtension(file) != null && PESUtils.getExtension(file).equals("max"))) {
                            file = new File(String.valueOf(String.valueOf(file.getParent())) + File.separator + file.getName() + ".max");
                        }
                        if (Editor.this.fileNameLegal(file.getName())) {
                            if (file.exists()) {
                                int n2 = JOptionPane.showConfirmDialog(Editor.this.getContentPane(), String.valueOf(String.valueOf(file.getName())) + "\nAlready exists in:\n" + file.getParent() + "\nAre you sure you want to replace this file?", "Replace:  " + file.getName(), 0, 2, null);
                                if (n2 == 0) {
                                    if (file.delete() && Editor.this.of.saveXPS(file)) {
                                        Editor.this.currentFile = file;
                                        Editor.this.setTitle("PES2008 Editor v9.5 - " + Editor.this.currentFile.getName());
                                        Editor.this.saveOkMsg(file);
                                        Editor.this.chooser.setSelectedFile(null);
                                    } else {
                                        Editor.this.saveFailMsg();
                                    }
                                }
                            } else if (Editor.this.of.saveXPS(file)) {
                                Editor.this.currentFile = file;
                                Editor.this.setTitle("PES2008 Editor v9.5 - " + Editor.this.currentFile.getName());
                                Editor.this.saveOkMsg(file);
                                Editor.this.chooser.setSelectedFile(null);
                            } else {
                                Editor.this.saveFailMsg();
                            }
                        } else {
                            Editor.this.illNameMsg();
                        }
                    }
                }
            }
        });
        jMenuItem3.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Editor.this.helpDia.setVisible(true);
            }
        });
        jMenuItem4.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(Editor.this.getContentPane(), "PES2008 Editor\nversion 9.5\n\n\u00a9 Copyright 2026 paokkerkir - 2007 Compulsion\n\nWebsite:\nhttps://evoweb.uk/threads/pes2008-editor-import-function-from-pes6-pc-of-released.104459/\n\nContact:\npes_compulsion@yahoo.co.uk\n\nThanks to:\nAbhishek, Arsenal666, Big Boss, djsaunders, dragonskin, Flipper, gothi,\nJayz123, JeffT, PLF, SFCMike, TheBoss, timo the owl, Tricky, \nadams06, FABIO VITOR, \nMarkella for her patience <3, Kimi for the purrs ", "About PES2008 Editor", -1, Editor.this.getIcon());
            }
        });
        this.csvItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n;
                if (Editor.this.currentFile != null && (n = Editor.this.csvChooser.showSaveDialog(Editor.this.getContentPane())) == 0) {
                    File file = Editor.this.csvChooser.getSelectedFile();
                    boolean bl = ((Editor)Editor.this).csvSwitch.head.isSelected();
                    boolean bl2 = ((Editor)Editor.this).csvSwitch.create.isSelected();
                    if (PESUtils.getExtension(file) == null || !PESUtils.getExtension(file).equals("csv")) {
                        file = new File(String.valueOf(String.valueOf(file.getParent())) + File.separator + file.getName() + ".csv");
                    }
                    if (Editor.this.fileNameLegal(file.getName())) {
                        if (file.exists()) {
                            int n2 = JOptionPane.showConfirmDialog(Editor.this.getContentPane(), String.valueOf(String.valueOf(file.getName())) + "\nAlready exists in:\n" + file.getParent() + "\nAre you sure you want to replace this file?", "Replace:  " + file.getName(), 0, 2, null);
                            if (n2 == 0) {
                                if (file.delete() && Editor.this.csvMaker.makeFile(Editor.this.of, file, bl, false, bl2)) {
                                    Editor.this.saveOkMsg(file);
                                } else {
                                    Editor.this.saveFailMsg();
                                }
                            }
                        } else if (Editor.this.csvMaker.makeFile(Editor.this.of, file, bl, false, bl2)) {
                            Editor.this.saveOkMsg(file);
                        } else {
                            Editor.this.saveFailMsg();
                        }
                    } else {
                        Editor.this.illNameMsg();
                    }
                }
            }
        });
        this.convertItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.arraycopy(((Editor)Editor.this).of2.data, OptionFile.block[2], ((Editor)Editor.this).of.data, OptionFile.block[2], OptionFile.blockSize[2]);
                System.arraycopy(((Editor)Editor.this).of2.data, OptionFile.block[3], ((Editor)Editor.this).of.data, OptionFile.block[3], OptionFile.blockSize[3]);
                System.arraycopy(((Editor)Editor.this).of2.data, OptionFile.block[4], ((Editor)Editor.this).of.data, OptionFile.block[4], OptionFile.blockSize[4]);
                System.arraycopy(((Editor)Editor.this).of2.data, OptionFile.block[5], ((Editor)Editor.this).of.data, OptionFile.block[5], OptionFile.blockSize[5]);
                System.arraycopy(((Editor)Editor.this).of2.data, OptionFile.block[6], ((Editor)Editor.this).of.data, OptionFile.block[6], OptionFile.blockSize[6]);
                System.arraycopy(((Editor)Editor.this).of2.data, OptionFile.block[7], ((Editor)Editor.this).of.data, OptionFile.block[7], OptionFile.blockSize[7]);
                System.arraycopy(((Editor)Editor.this).of2.data, OptionFile.block[8], ((Editor)Editor.this).of.data, OptionFile.block[8], OptionFile.blockSize[8]);
                System.arraycopy(((Editor)Editor.this).of2.data, 675784, ((Editor)Editor.this).of.data, 675784, 828);
                Editor.this.flagPanel.refresh();
                Editor.this.imagePanel.refresh();
                Editor.this.tranPanel.refresh();
                Editor.this.stadPan.refresh();
                Editor.this.teamPan.refresh();
                Editor.this.leaguePan.refresh();
                Editor.this.importPanel.disableAll();
                Editor.this.convertItem.setEnabled(false);
            }
        });
        jMenu.add(jMenuItem);
        jMenu.add(this.open2Item);
        jMenu.add(this.saveItem);
        jMenu.add(this.saveAsItem);
        jMenu.add(this.saveParaItem);  // ADDED: Export DB PES2008
        jMenu.add(jMenuItem2);
        jMenu2.add(jMenuItem3);
        jMenu2.add(jMenuItem4);
        jMenu3.add(this.csvItem);
        jMenu3.add(this.convertItem);
        jMenuBar.add(jMenu);
        jMenuBar.add(jMenu3);
        jMenuBar.add(jMenu2);
        this.setJMenuBar(jMenuBar);
        this.csvItem.setEnabled(false);
        this.open2Item.setEnabled(false);
        this.saveItem.setEnabled(false);
        this.saveAsItem.setEnabled(false);
        this.convertItem.setEnabled(false);
    }

    private boolean fileNameLegal(String string) {
        boolean bl = true;
        if (string.indexOf("\\") != -1) {
            bl = false;
        }
        if (string.indexOf("/") != -1) {
            bl = false;
        }
        if (string.indexOf(":") != -1) {
            bl = false;
        }
        if (string.indexOf("*") != -1) {
            bl = false;
        }
        if (string.indexOf("?") != -1) {
            bl = false;
        }
        if (string.indexOf("\"") != -1) {
            bl = false;
        }
        if (string.indexOf("<") != -1) {
            bl = false;
        }
        if (string.indexOf(">") != -1) {
            bl = false;
        }
        if (string.indexOf("|") != -1) {
            bl = false;
        }
        return bl;
    }

    private void saveFailMsg() {
        JOptionPane.showMessageDialog(this.getContentPane(), "Save failed", "Error", 0);
    }

    private void openFailMsg() {
        JOptionPane.showMessageDialog(this.getContentPane(), "Could not open file", "Error", 0);
    }

    private void saveOkMsg(File file) {
        JOptionPane.showMessageDialog(this.getContentPane(), String.valueOf(String.valueOf(file.getName())) + "\nSaved in:\n" + file.getParent(), "File Successfully Saved", 1);
    }

    private void illNameMsg() {
        JOptionPane.showMessageDialog(this.getContentPane(), "File name cannot contain the following characters:\n\\ / : * ? \" < > |", "Error", 0);
    }

    private void setIcon() {
        URL uRL = this.getClass().getResource("data/icon.png");
        if (uRL != null) {
            ImageIcon imageIcon = new ImageIcon(uRL);
            this.setIconImage(imageIcon.getImage());
        }
    }

    private ImageIcon getIcon() {
        ImageIcon imageIcon = null;
        URL uRL = this.getClass().getResource("data/icon.png");
        if (uRL != null) {
            imageIcon = new ImageIcon(uRL);
        }
        return imageIcon;
    }

    private void openFile() {
        int n = this.chooser.showOpenDialog(this.getContentPane());
        this.saveSet();
        if (n == 0 && this.filter.accept(this.chooser.getSelectedFile())) {
            if (this.chooser.getSelectedFile().isFile() && this.of.readXPS(this.chooser.getSelectedFile())) {
                this.currentFile = this.chooser.getSelectedFile();
                this.setTitle("PES2008 Editor v9.5 - " + this.currentFile.getName());
                Squads.fixAll(this.of);
                this.flagPanel.refresh();
                this.imagePanel.refresh();
                this.tranPanel.refresh();
                this.wenShop.wenPanel.refresh();
                this.wenShop.shopPanel.status.setText("");
                this.stadPan.refresh();
                this.teamPan.refresh();
                this.leaguePan.refresh();
                this.tabbedPane.setVisible(true);
                this.importPanel.refresh();
                this.csvItem.setEnabled(true);
                this.open2Item.setEnabled(true);
                this.saveItem.setEnabled(true);
                this.saveAsItem.setEnabled(true);
                if (this.of2.fileName != null) {
                    this.convertItem.setEnabled(true);
                } else {
                    this.convertItem.setEnabled(false);
                }
            } else {
                this.csvItem.setEnabled(false);
                this.open2Item.setEnabled(false);
                this.saveItem.setEnabled(false);
                this.saveAsItem.setEnabled(false);
                this.tabbedPane.setVisible(false);
                this.convertItem.setEnabled(false);
                this.setTitle("PES2008 Editor v9.5");
                this.openFailMsg();
            }
        }
    }

    private boolean saveSet() {
        boolean bl = true;
        if (this.chooser.getCurrentDirectory() != null) {
            try {
                if (this.settingsFile.exists()) {
                    this.settingsFile.delete();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(this.settingsFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(this.chooser.getCurrentDirectory());
                objectOutputStream.flush();
                objectOutputStream.close();
                fileOutputStream.close();
            }
            catch (IOException iOException) {
                bl = false;
            }
        }
        return bl;
    }

    private File loadSet() {
        File file = null;
        if (this.settingsFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(this.settingsFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                file = (File)objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
                if (file != null && !file.exists()) {
                    file = null;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return file;
    }

    public static void main(String[] stringArray) throws IOException {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                PESUtils.systemUI();
                System.setProperty("swing.metalTheme", "steel");
                new Editor();
            }
        });
    }
}

