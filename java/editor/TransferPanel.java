/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.FormationDialog;
import editor.Formations;
import editor.InfoPanel;
import editor.OptionFile;
import editor.Player;
import editor.PlayerDialog;
import editor.SelectByNation;
import editor.SelectByTeam;
import editor.SquadList;
import editor.Squads;
import editor.Stats;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TransferPanel
extends JPanel
implements MouseListener,
DropTargetListener,
DragSourceListener,
DragGestureListener {
    private SelectByTeam selectorL;
    private SelectByTeam selectorR;
    private SelectByNation freeList;
    private OptionFile of;
    private NameEditor nameEditor;
    private NumEditor numEditor;
    private InfoPanel infoPanel;
    private ShirtNameEditor shirtEditor;
    private PlayerDialog playerDia;
    private FormationDialog teamDia;
    private JCheckBox autoRel = new JCheckBox("Auto Release");
    private JCheckBox autoRep = new JCheckBox("Auto Sub");
    private JCheckBox safeMode = new JCheckBox("Safe Mode");
    private JButton compare;
    private JButton exportTeam;
    private JButton importTeam;
    private int releasedIndex = 0;
    private DragSource sourceF = null;
    private DragSource sourceL = null;
    private DragSource sourceR = null;
    private Component sourceComp = null;
    private int sourceIndex = -1;
    private DataFlavor localPlayerFlavor;
    private int compIndex = 0;
    private int lastIndex = 0;

    public TransferPanel(PlayerDialog playerDialog, OptionFile optionFile, FormationDialog formationDialog) {
        this.of = optionFile;
        this.teamDia = formationDialog;
        this.playerDia = playerDialog;
        this.autoRel.setToolTipText("When a player is transfered to a club squad he will be automatically released from his old squad");
        this.autoRel.setSelected(true);
        this.autoRep.setToolTipText("Gaps made in a team's first 11 will be automatically filled with the most appropriate sub");
        this.autoRep.setSelected(true);
        this.safeMode.setToolTipText("Only transfers that are possible in-game will be allowed");
        this.safeMode.setSelected(true);
        this.compare = new JButton("Compare Stats");
        this.compare.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (TransferPanel.this.compIndex == 0) {
                    int n;
                    TransferPanel.this.compIndex = TransferPanel.this.lastIndex;
                    if (((TransferPanel)TransferPanel.this).nameEditor.source == 2) {
                        int n2 = ((TransferPanel)TransferPanel.this).selectorL.teamBox.getSelectedIndex();
                        if (n2 < 64 || n2 > 72 && n2 < 221) {
                            ((TransferPanel)TransferPanel.this).selectorL.posList.selectPos(((TransferPanel)TransferPanel.this).selectorL.squadList, ((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedIndex());
                        }
                    } else if (((TransferPanel)TransferPanel.this).nameEditor.source == 3 && ((n = ((TransferPanel)TransferPanel.this).selectorR.teamBox.getSelectedIndex()) < 64 || n > 72 && n < 221)) {
                        ((TransferPanel)TransferPanel.this).selectorR.posList.selectPos(((TransferPanel)TransferPanel.this).selectorR.squadList, ((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedIndex());
                    }
                } else {
                    TransferPanel.this.compIndex = 0;
                    ((TransferPanel)TransferPanel.this).selectorL.posList.clearSelection();
                    ((TransferPanel)TransferPanel.this).selectorR.posList.clearSelection();
                }
                TransferPanel.this.infoPanel.refresh(TransferPanel.this.lastIndex, TransferPanel.this.compIndex);
            }
        });
        
        // Export Team button
        this.exportTeam = new JButton("Export Team");
        this.exportTeam.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get current team from left selector
                int teamIndex = TransferPanel.this.selectorL.squadList.team;
                String teamName = (String)TransferPanel.this.selectorL.teamBox.getSelectedItem();
                
                // Determine squad size based on team type
                int squadSize;
                if (teamIndex < 73) {
                    squadSize = 23;  // National teams
                } else {
                    squadSize = 32;  // Club teams (including team 73)
                }
                
                // Count actual players (non-empty)
                int playerCount = 0;
                for (int i = 0; i < squadSize; i++) {
                    Player p = (Player)TransferPanel.this.selectorL.squadList.getModel().getElementAt(i);
                    if (p != null && p.index != 0) {  // Not empty
                        playerCount++;
                    }
                }
                
                if (playerCount == 0) {
                    JOptionPane.showMessageDialog(TransferPanel.this,
                        "No players to export in this team!",
                        "Export Team",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Create byte array for all players
                byte[] teamData = new byte[playerCount * 124];
                int currentPlayer = 0;
                
                for (int i = 0; i < squadSize; i++) {
                    Player p = (Player)TransferPanel.this.selectorL.squadList.getModel().getElementAt(i);
                    if (p != null && p.index != 0) {
                        // Calculate player offset (PES2008 offsets)
                        int offset;
                        if (p.index >= 32768) {
                            offset = 11876 + (p.index - 32768) * 124;
                        } else {
                            offset = 34704 + p.index * 124;
                        }
                        // Copy 124 bytes
                        System.arraycopy(TransferPanel.this.of.data, offset, teamData, currentPlayer * 124, 124);
                        currentPlayer++;
                    }
                }
                
                // File chooser
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Export Team");
                fc.setCurrentDirectory(new File(System.getProperty("user.home")));
                fc.setFileFilter(new FileNameExtensionFilter("TMRV files (*.tmrv)", "tmrv"));
                if (teamName != null && !teamName.isEmpty()) {
                    fc.setSelectedFile(new File(teamName + ".tmrv"));
                }
                
                int result = fc.showSaveDialog(TransferPanel.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    // Ensure .tmrv extension
                    if (!file.getName().toLowerCase().endsWith(".tmrv")) {
                        file = new File(file.getAbsolutePath() + ".tmrv");
                    }
                    
                    try {
                        RandomAccessFile raf = new RandomAccessFile(file, "rw");
                        raf.write(teamData);
                        raf.close();
                        JOptionPane.showMessageDialog(TransferPanel.this,
                            "Team exported successfully!\n" +
                            "File: " + file.getName() + "\n" +
                            "Players: " + playerCount,
                            "Export Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(TransferPanel.this,
                            "Failed to export team:\n" + ex.getMessage(),
                            "Export Failed",
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        // Import Team button
        this.importTeam = new JButton("Import Team");
        this.importTeam.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Import Team");
                fc.setCurrentDirectory(new File(System.getProperty("user.home")));
                fc.setFileFilter(new FileNameExtensionFilter("TMRV files (*.tmrv)", "tmrv"));
                
                int result = fc.showOpenDialog(TransferPanel.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (!file.isFile()) {
                        JOptionPane.showMessageDialog(TransferPanel.this,
                            "File not found or invalid.",
                            "Invalid File",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Get current team info
                    int teamIndex = TransferPanel.this.selectorL.squadList.team;
                    String teamName = (String)TransferPanel.this.selectorL.teamBox.getSelectedItem();
                    
                    // Determine squad size
                    int squadSize;
                    if (teamIndex < 73) {
                        squadSize = 23;  // National teams
                    } else {
                        squadSize = 32;  // Club teams (including team 73)
                    }
                    
                    // Calculate player count from file size
                    int filePlayerCount = (int)(file.length() / 124);
                    
                    if (file.length() % 124 != 0) {
                        JOptionPane.showMessageDialog(TransferPanel.this,
                            "Invalid team file. File size must be a multiple of 124 bytes.",
                            "Invalid File",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (filePlayerCount > squadSize) {
                        int choice = JOptionPane.showConfirmDialog(TransferPanel.this,
                            "File contains " + filePlayerCount + " players but team can only hold " + squadSize + ".\n" +
                            "Only the first " + squadSize + " players will be imported. Continue?",
                            "Warning",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                        if (choice != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                    
                    try {
                        RandomAccessFile raf = new RandomAccessFile(file, "r");
                        byte[] teamData = new byte[(int)file.length()];
                        raf.read(teamData);
                        raf.close();
                        
                        int playersToImport = Math.min(filePlayerCount, squadSize);
                        
                        for (int i = 0; i < playersToImport; i++) {
                            Player p = (Player)TransferPanel.this.selectorL.squadList.getModel().getElementAt(i);
                            if (p == null) continue;
                            
                            // Calculate player offset (PES2008 offsets)
                            int offset;
                            if (p.index >= 32768) {
                                offset = 11876 + (p.index - 32768) * 124;
                            } else {
                                offset = 34704 + p.index * 124;
                            }
                            
                            // Copy 124 bytes from file to option file
                            System.arraycopy(teamData, i * 124, TransferPanel.this.of.data, offset, 124);
                        }
                        
                        // Refresh the squad list to show imported players
                        TransferPanel.this.selectorL.squadList.refresh(TransferPanel.this.selectorL.teamBox.getSelectedIndex(), true);
                        
                        JOptionPane.showMessageDialog(TransferPanel.this,
                            "Team imported successfully!\n" +
                            "File: " + file.getName() + "\n" +
                            "Players imported: " + playersToImport + " into " + teamName,
                            "Import Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(TransferPanel.this,
                            "Failed to import team:\n" + ex.getMessage(),
                            "Import Failed",
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        this.freeList = new SelectByNation(this.of);
        this.selectorL = new SelectByTeam(this.of, true);
        this.nameEditor = new NameEditor();
        this.numEditor = new NumEditor();
        this.shirtEditor = new ShirtNameEditor();
        JPanel jPanel = new JPanel(new GridLayout(0, 1));
        JPanel jPanel2 = new JPanel(new GridLayout(0, 1));
        JPanel jPanel3 = new JPanel(new BorderLayout());
        JPanel jPanel4 = new JPanel(new BorderLayout());
        this.selectorR = new SelectByTeam(this.of, true);
        this.addListen();
        this.freeList.freeList.addMouseListener(this);
        this.selectorL.squadList.addMouseListener(this);
        this.selectorR.squadList.addMouseListener(this);
        String string = "application/x-java-jvm-local-objectref;class=editor.Player";
        try {
            this.localPlayerFlavor = new DataFlavor(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            System.out.println("FormTransferHandler: unable to create data flavor");
        }
        new DropTarget(this.freeList.freeList, this);
        new DropTarget(this.selectorL.squadList, this);
        new DropTarget(this.selectorR.squadList, this);
        this.sourceF = new DragSource();
        this.sourceF.createDefaultDragGestureRecognizer(this.freeList.freeList, 2, this);
        this.sourceL = new DragSource();
        this.sourceL.createDefaultDragGestureRecognizer(this.selectorL.squadList, 2, this);
        this.sourceR = new DragSource();
        this.sourceR.createDefaultDragGestureRecognizer(this.selectorR.squadList, 2, this);
        this.infoPanel = new InfoPanel(this.selectorL, this.of);
        this.selectorL.squadList.setToolTipText("Double click to edit player, right click to edit formation");
        this.selectorR.squadList.setToolTipText("Double click to edit player, right click to edit formation");
        this.freeList.freeList.setToolTipText("Double click to edit player");
        jPanel.add(this.nameEditor);
        jPanel.add(this.shirtEditor);
        jPanel2.add(this.autoRel);
        jPanel2.add(this.autoRep);
        jPanel2.add(this.safeMode);
        JPanel jPanel5 = new JPanel();
        JPanel jPanel6 = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));  // 1 row, 3 columns for buttons
        buttonPanel.add(this.compare);
        buttonPanel.add(this.exportTeam);
        buttonPanel.add(this.importTeam);
        jPanel5.add(this.numEditor);
        jPanel5.add(jPanel);
        jPanel5.add(jPanel2);
        jPanel6.add((Component)jPanel5, "North");
        jPanel6.add((Component)this.infoPanel, "Center");
        jPanel6.add((Component)buttonPanel, "South");
        jPanel3.add((Component)this.selectorL, "Center");
        jPanel4.add((Component)this.selectorR, "Center");
        JPanel jPanel7 = new JPanel(new GridLayout(0, 3));
        jPanel7.add(this.freeList);
        jPanel7.add(jPanel3);
        jPanel7.add(jPanel4);
        this.add(jPanel7);
        this.add(jPanel6);
    }

    private int getNumAdr(int n) {
        return 676624 + (n - 683296) / 2;
    }

    public void refresh() {
        this.freeList.refresh();
        this.selectorL.refresh();
        this.selectorR.refresh();
        this.nameEditor.setText("");
        this.numEditor.setText("");
        this.shirtEditor.setText("");
        this.compIndex = 0;
        this.lastIndex = 0;
        this.infoPanel.refresh(this.lastIndex, this.compIndex);
    }

    public void refreshLists() {
        this.freeList.freeList.refresh(this.freeList.nationBox.getSelectedIndex(), this.freeList.alpha);
        this.selectorL.squadList.refresh(this.selectorL.teamBox.getSelectedIndex(), true);
        this.selectorR.squadList.refresh(this.selectorR.teamBox.getSelectedIndex(), true);
        this.selectorL.numList.refresh(this.selectorL.teamBox.getSelectedIndex());
        this.selectorR.numList.refresh(this.selectorR.teamBox.getSelectedIndex());
        this.selectorL.posList.refresh(this.selectorL.teamBox.getSelectedIndex());
        this.selectorR.posList.refresh(this.selectorR.teamBox.getSelectedIndex());
        this.nameEditor.setText("");
        this.numEditor.setText("");
        this.shirtEditor.setText("");
        this.compIndex = 0;
        this.lastIndex = 0;
        this.infoPanel.refresh(this.lastIndex, this.compIndex);
    }

    public int getShirt(int n, int n2) {
        int n3 = n == 2 ? ((Player)this.selectorL.squadList.getModel().getElementAt((int)n2)).adr : ((Player)this.selectorR.squadList.getModel().getElementAt((int)n2)).adr;
        int n4 = this.of.toInt(this.of.data[n3 = this.getNumAdr(n3)]) + 1;
        if (n4 == 256) {
            n4 = 0;
        }
        return n4;
    }

    public void setShirt(int n, int n2, int n3) {
        int n4 = n == 2 ? ((Player)this.selectorL.squadList.getModel().getElementAt((int)n2)).adr : ((Player)this.selectorR.squadList.getModel().getElementAt((int)n2)).adr;
        int n5 = this.of.toInt(this.of.data[n4 = this.getNumAdr(n4)]) + 1;
        if (n5 != 256) {
            this.of.data[n4] = this.of.toByte(n3 - 1);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 1 && mouseEvent.isControlDown() && mouseEvent.getSource() != this.freeList.freeList) {
            mouseEvent.consume();
            SquadList squadList = (SquadList)mouseEvent.getSource();
            int n = squadList.team;
            if (n >= 0 && n < 64) {
                this.teamDia.show(n, (String)this.selectorL.teamBox.getItemAt(n));
                Squads.fixForm(this.of, n, false);
                this.refreshLists();
            }
            if (n >= 73 && n < 221) {
                this.teamDia.show(n - 9, (String)this.selectorL.teamBox.getItemAt(n));
                Squads.fixForm(this.of, n, false);
                this.refreshLists();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int n = mouseEvent.getClickCount();
        if (mouseEvent.getButton() == 1 && n == 2) {
            JList jList = (JList)mouseEvent.getSource();
            Player player = (Player)jList.getSelectedValue();
            int n2 = player.index;
            if (n2 != 0) {
                if (this.safeMode.isSelected()) {
                    if (this.inNatSquad(n2)) {
                        this.playerDia.genPanel.nationBox.setEnabled(false);
                    } else {
                        this.playerDia.genPanel.nationBox.setEnabled(true);
                    }
                } else {
                    this.playerDia.genPanel.nationBox.setEnabled(true);
                }
                this.playerDia.show(player);
                this.refreshLists();
            }
        } else if (n == 1 && mouseEvent.getButton() == 3 && mouseEvent.getSource() != this.freeList.freeList) {
            SquadList squadList = (SquadList)mouseEvent.getSource();
            int n3 = squadList.team;
            if (n3 >= 0 && n3 < 64) {
                this.teamDia.show(n3, (String)this.selectorL.teamBox.getItemAt(n3));
                Squads.fixForm(this.of, n3, false);
                this.refreshLists();
            }
            if (n3 >= 73 && n3 < 221) {
                this.teamDia.show(n3 - 9, (String)this.selectorL.teamBox.getItemAt(n3));
                Squads.fixForm(this.of, n3, false);
                this.refreshLists();
            }
        }
    }

    private int clubRelease(int n, boolean bl) {
        int n2 = 686652;
        int n3 = -1;
        do {
            int n4;
            if ((n4 = this.of.toInt(this.of.data[(n2 += 2) + 1]) << 8 | this.of.toInt(this.of.data[n2])) != n) continue;
            int n5 = (n2 - 686654) / 64 + 73;
            int n6 = (n2 - 686654) % 64;
            if (!(n3 != -1 || bl && n6 >= 22)) {
                n3 = n5;
                if (bl) {
                    this.releasedIndex = n6 / 2;
                }
            }
            if (!bl) continue;
            this.of.data[n2] = 0;
            this.of.data[n2 + 1] = 0;
            this.of.data[this.getNumAdr((int)n2)] = -1;
            if (n6 >= 22) {
                Squads.tidy(this.of, n5);
                continue;
            }
            if (!this.autoRep.isSelected()) continue;
            int n7 = n5;
            if (n7 > 72) {
                n7 -= 9;
            }
            Squads.tidy11(this.of, n5, n6 / 2, Formations.getPos(this.of, n7, 0, n6 / 2));
        } while (n2 < 696124);
        return n3;
    }

    private byte getNextNum(int n) {
        int n2;
        int n3;
        byte by = -1;
        if (n < 73) {
            n3 = 23;
            n2 = 676624 + n * n3;
        } else {
            n3 = 32;
            n2 = 678303 + (n - 73) * n3;
        }
        byte by2 = 0;
        while (by == -1 && by2 < 99) {
            boolean bl = true;
            int n4 = 0;
            while (bl && n4 < n3) {
                int n5 = n2 + n4;
                byte by3 = this.of.data[n5];
                if (by3 == by2) {
                    bl = false;
                }
                ++n4;
            }
            if (bl) {
                by = by2;
            }
            by2 = (byte)(by2 + 1);
        }
        if (by == -1) {
            by = 0;
        }
        return by;
    }

    private int countPlayers(int n) {
        int n2;
        int n3;
        int n4 = 0;
        if (n < 73) {
            n3 = 23;
            n2 = 683296 + n * n3 * 2;
        } else {
            n3 = 32;
            n2 = 686654 + (n - 73) * n3 * 2;
        }
        int n5 = 0;
        while (n5 < n3) {
            int n6 = n2 + n5 * 2;
            int n7 = this.of.toInt(this.of.data[n6 + 1]) << 8 | this.of.toInt(this.of.data[n6]);
            if (n7 != 0) {
                ++n4;
            }
            ++n5;
        }
        return n4;
    }

    private boolean inNatSquad(int n) {
        boolean bl = false;
        int n2 = 0;
        while (!bl && n2 < 64) {
            if (this.inSquad(n2, n)) {
                bl = true;
            }
            ++n2;
        }
        return bl;
    }

    private boolean inSquad(int n, int n2) {
        boolean bl = false;
        if (n2 != 0) {
            int n3;
            int n4;
            if (n < 73) {
                n4 = 23;
                n3 = 683296 + n * n4 * 2;
            } else {
                n4 = 32;
                n3 = 686654 + (n - 73) * n4 * 2;
            }
            int n5 = 0;
            while (!bl && n5 < n4) {
                int n6 = n3 + n5 * 2;
                int n7 = this.of.toInt(this.of.data[n6 + 1]) << 8 | this.of.toInt(this.of.data[n6]);
                if (n7 == n2) {
                    bl = true;
                }
                ++n5;
            }
        }
        return bl;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
    }

    @Override
    public void dragExit(DropTargetEvent dropTargetEvent) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
        JList jList = (JList)dropTargetDragEvent.getDropTargetContext().getComponent();
        int n = jList.locationToIndex(dropTargetDragEvent.getLocation());
        Player player = n != -1 ? (Player)jList.getModel().getElementAt(n) : new Player(this.of, 0, 0);
        boolean bl = this.checkSafeDrag(this.safeMode.isSelected(), jList, player);
        jList.setSelectedIndex(n);
        if (bl) {
            dropTargetDragEvent.acceptDrag(2);
        } else {
            dropTargetDragEvent.rejectDrag();
        }
    }

    @Override
    public void drop(DropTargetDropEvent dropTargetDropEvent) {
        Transferable transferable = dropTargetDropEvent.getTransferable();
        if (transferable.isDataFlavorSupported(this.localPlayerFlavor)) {
            int n;
            Player player;
            JList jList = (JList)this.sourceComp;
            JList jList2 = (JList)dropTargetDropEvent.getDropTargetContext().getComponent();
            Player player2 = (Player)jList.getModel().getElementAt(this.sourceIndex);
            int n2 = player2.index;
            if (jList2.getSelectedIndex() != -1) {
                player = (Player)jList2.getSelectedValue();
                n = player.index;
            } else {
                player = new Player(this.of, 0, 0);
                n = 0;
            }
            if (jList != this.freeList.freeList && jList2 != this.freeList.freeList) {
                int n3 = ((SelectByTeam)jList.getParent()).teamBox.getSelectedIndex();
                int n4 = ((SelectByTeam)jList2.getParent()).teamBox.getSelectedIndex();
                if (jList == jList2) {
                    if ((n3 < 64 || n3 > 72 && n3 < 221) && n2 != n) {
                        dropTargetDropEvent.acceptDrop(2);
                        this.transferS(player2, player, n3, n4, jList, jList2);
                    }
                } else if (jList == this.selectorL.squadList && jList2 == this.selectorR.squadList) {
                    dropTargetDropEvent.acceptDrop(2);
                    this.transferLR(player2);
                } else if (jList == this.selectorR.squadList && jList2 == this.selectorL.squadList) {
                    dropTargetDropEvent.acceptDrop(2);
                    this.transferRL(player2);
                }
            } else if (jList == this.freeList.freeList && jList2 == this.selectorL.squadList) {
                dropTargetDropEvent.acceptDrop(2);
                this.transferFL(n2);
            } else if (jList == this.freeList.freeList && jList2 == this.selectorR.squadList) {
                dropTargetDropEvent.acceptDrop(2);
                this.transferFR(n2);
            } else if (jList == this.selectorL.squadList && jList2 == this.freeList.freeList) {
                dropTargetDropEvent.acceptDrop(2);
                this.tranRelL(player2, this.sourceIndex);
            } else if (jList == this.selectorR.squadList && jList2 == this.freeList.freeList) {
                dropTargetDropEvent.acceptDrop(2);
                this.tranRelR(player2, this.sourceIndex);
            } else {
                dropTargetDropEvent.rejectDrop();
            }
            dropTargetDropEvent.getDropTargetContext().dropComplete(true);
        } else {
            dropTargetDropEvent.rejectDrop();
        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
        this.sourceComp = dragGestureEvent.getComponent();
        if (this.sourceComp instanceof JList) {
            JList jList = (JList)this.sourceComp;
            this.sourceIndex = jList.getSelectedIndex();
            Player player = (Player)jList.getSelectedValue();
            if (this.sourceIndex != -1 && player.index != 0) {
                int n;
                this.removeListen();
                this.lastIndex = 0;
                this.compIndex = 0;
                this.infoPanel.refresh(this.lastIndex, this.compIndex);
                this.nameEditor.setText("");
                this.shirtEditor.setText("");
                this.nameEditor.source = 0;
                this.shirtEditor.source = 0;
                PlayerTransferable playerTransferable = new PlayerTransferable(player);
                if (jList != this.freeList.freeList && ((n = ((SelectByTeam)jList.getParent()).teamBox.getSelectedIndex()) < 64 || n > 72 && n < 221)) {
                    if (jList == this.selectorL.squadList) {
                        this.selectorL.posList.selectPos(this.selectorL.squadList, this.selectorL.squadList.getSelectedIndex());
                    } else if (jList == this.selectorR.squadList) {
                        this.selectorR.posList.selectPos(this.selectorR.squadList, this.selectorR.squadList.getSelectedIndex());
                    }
                }
                dragGestureEvent.getDragSource().startDrag(dragGestureEvent, null, playerTransferable, this);
            }
        }
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
        if (!dragSourceDropEvent.getDropSuccess()) {
            this.refreshLists();
        }
        this.addListen();
    }

    @Override
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
    }

    @Override
    public void dragExit(DragSourceEvent dragSourceEvent) {
    }

    @Override
    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
    }

    private boolean checkSafeDrag(boolean bl, JList jList, Player player) {
        int n;
        boolean bl2 = true;
        boolean bl3 = true;
        boolean bl4 = true;
        boolean bl5 = true;
        boolean bl6 = true;
        boolean bl7 = true;
        boolean bl8 = true;
        boolean bl9 = true;
        boolean bl10 = true;
        int n2 = -1;
        int n3 = -1;
        JList jList2 = (JList)this.sourceComp;
        int n4 = ((Player)jList2.getModel().getElementAt((int)this.sourceIndex)).index;
        int n5 = player.index;
        int n6 = -1;
        int n7 = 0;
        if (jList2 == this.freeList.freeList) {
            n7 = n4;
            bl8 = false;
        } else if (jList == this.freeList.freeList) {
            n7 = n5;
            bl8 = false;
        }
        int n8 = 0;
        if (jList2 == this.selectorL.squadList) {
            n8 = n4;
            bl9 = false;
            n6 = ((SelectByTeam)jList2.getParent()).teamBox.getSelectedIndex();
        } else if (jList == this.selectorL.squadList) {
            n8 = n5;
            bl9 = false;
        }
        int n9 = 0;
        if (jList2 == this.selectorR.squadList) {
            n9 = n4;
            bl10 = false;
            n6 = ((SelectByTeam)jList2.getParent()).teamBox.getSelectedIndex();
        } else if (jList == this.selectorR.squadList) {
            n9 = n5;
            bl10 = false;
        }
        n2 = this.selectorL.teamBox.getSelectedIndex();
        n3 = this.selectorR.teamBox.getSelectedIndex();
        if (bl) {
            int n10;
            int n11;
            n = 16;
            int n12 = 16;
            if (n2 < 73) {
                n = 23;
            }
            if (n3 < 73) {
                n12 = 23;
            }
            if (n7 >= 4978 && n7 < 32768) {
                bl2 = false;
                bl3 = false;
            }
            if (n7 >= 4790 && n7 < 4818) {
                bl2 = false;
                bl3 = false;
            }
            if (n7 >= 32768 && n7 < 32920 && n2 > 56) {
                bl2 = false;
            }
            if (n7 >= 32768 && n7 < 32920 && n3 > 56) {
                bl3 = false;
            }
            if (n2 > 72 && n2 < 221) {
                n11 = this.clubRelease(n7, false);
                if (this.autoRel.isSelected()) {
                    if (n11 != -1 && (n10 = this.countPlayers(n11)) <= 16) {
                        bl2 = false;
                    }
                } else if (n11 != -1) {
                    bl2 = false;
                }
            }
            if (n3 > 72 && n3 < 221) {
                n11 = this.clubRelease(n7, false);
                if (this.autoRel.isSelected()) {
                    if (n11 != -1 && (n10 = this.countPlayers(n11)) <= 16) {
                        bl3 = false;
                    }
                } else if (n11 != -1) {
                    bl3 = false;
                }
            }
            if (n2 > 56 && n2 < 73 || n2 > 220) {
                bl2 = false;
                if (n2 == 64 || n2 == 221) {
                    bl4 = false;
                } else if (n2 > 64 && n2 < 72 && n3 > 63) {
                    bl4 = false;
                }
                bl5 = false;
                bl6 = false;
            } else {
                n11 = this.countPlayers(n2);
                if (n11 <= n) {
                    bl6 = false;
                    if (this.autoRel.isSelected() && n2 > 72) {
                        bl4 = false;
                    }
                }
                if (this.inSquad(n2, n9)) {
                    bl5 = false;
                }
                if (this.inSquad(n2, n7)) {
                    bl2 = false;
                }
                if (!this.autoRel.isSelected() && n2 > 72 && n2 < 221 && (n10 = this.clubRelease(n9, false)) != -1) {
                    bl5 = false;
                }
            }
            if (n3 > 56 && n3 < 73 || n3 > 220) {
                bl4 = false;
                bl3 = false;
                if (n3 == 64 || n3 == 221) {
                    bl5 = false;
                } else if (n3 > 64 && n3 < 72 && n2 > 63) {
                    bl5 = false;
                }
                bl7 = false;
            } else {
                n11 = this.countPlayers(n3);
                if (n11 <= n12) {
                    bl7 = false;
                    if (this.autoRel.isSelected() && n3 > 72) {
                        bl5 = false;
                    }
                }
                if (this.inSquad(n3, n8)) {
                    bl4 = false;
                }
                if (this.inSquad(n3, n7)) {
                    bl3 = false;
                }
                if (!this.autoRel.isSelected() && n3 > 72 && n3 < 221 && (n10 = this.clubRelease(n8, false)) != -1) {
                    bl4 = false;
                }
            }
            if (n3 == n2) {
                bl4 = false;
                bl5 = false;
            }
            if (n2 < 64) {
                n11 = n2;
                switch (n11) {
                    case 57: {
                        n11 = 6;
                        break;
                    }
                    case 58: {
                        n11 = 8;
                        break;
                    }
                    case 59: {
                        n11 = 9;
                        break;
                    }
                    case 60: {
                        n11 = 13;
                        break;
                    }
                    case 61: {
                        n11 = 15;
                        break;
                    }
                    case 62: {
                        n11 = 44;
                        break;
                    }
                    case 63: {
                        n11 = 45;
                    }
                }
                if (!bl8 && (n10 = Stats.getValue(this.of, n7, Stats.nationality)) != Stats.nation.length - 1 && n10 != n11) {
                    bl2 = false;
                }
                if (!bl10 && (n10 = Stats.getValue(this.of, n9, Stats.nationality)) != Stats.nation.length - 1 && n10 != n11) {
                    bl5 = false;
                }
            }
            if (n3 < 64) {
                n11 = n3;
                switch (n11) {
                    case 57: {
                        n11 = 6;
                        break;
                    }
                    case 58: {
                        n11 = 8;
                        break;
                    }
                    case 59: {
                        n11 = 9;
                        break;
                    }
                    case 60: {
                        n11 = 13;
                        break;
                    }
                    case 61: {
                        n11 = 15;
                        break;
                    }
                    case 62: {
                        n11 = 44;
                        break;
                    }
                    case 63: {
                        n11 = 45;
                    }
                }
                if (!bl8 && (n10 = Stats.getValue(this.of, n7, Stats.nationality)) != Stats.nation.length - 1 && n10 != n11) {
                    bl3 = false;
                }
                if (!bl9 && (n10 = Stats.getValue(this.of, n8, Stats.nationality)) != Stats.nation.length - 1 && n10 != n11) {
                    bl4 = false;
                }
            }
        }
        n = 0;
        if (jList2 != this.freeList.freeList && jList != this.freeList.freeList) {
            if (jList2 == jList) {
                if ((n6 < 64 || n6 > 72 && n6 < 221) && n4 != n5) {
                    n = 1;
                }
            } else if (jList2 == this.selectorL.squadList && jList == this.selectorR.squadList && bl4 && n4 != 0) {
                n = 1;
            } else if (jList2 == this.selectorR.squadList && jList == this.selectorL.squadList && bl5 && n4 != 0) {
                n = 1;
            }
        } else if (jList2 == this.freeList.freeList && jList == this.selectorL.squadList && bl2) {
            n = 1;
        } else if (jList2 == this.freeList.freeList && jList == this.selectorR.squadList && bl3) {
            n = 1;
        } else if (jList2 == this.selectorL.squadList && jList == this.freeList.freeList && bl6) {
            n = 1;
        } else if (jList2 == this.selectorR.squadList && jList == this.freeList.freeList && bl7) {
            n = 1;
        }
        return n != 0;
    }

    private void transferFL(int n) {
        int n2 = ((Player)this.selectorL.squadList.getSelectedValue()).adr;
        int n3 = this.selectorL.teamBox.getSelectedIndex();
        int n4 = -1;
        if (n3 >= 73 && n3 < 221 && this.autoRel.isSelected()) {
            n4 = this.clubRelease(n, true);
        }
        this.of.data[n2] = this.of.toByte(n & 0xFF);
        this.of.data[n2 + 1] = this.of.toByte((n & 0xFF00) >>> 8);
        if (this.of.data[this.getNumAdr(n2)] == -1) {
            this.of.data[this.getNumAdr((int)n2)] = this.getNextNum(n3);
        }
        if (this.selectorL.squadList.getSelectedIndex() > 10) {
            Squads.tidy(this.of, n3);
        }
        this.refreshLists();
        if (n4 != -1) {
            this.selectorR.teamBox.setSelectedIndex(n4);
            this.selectorR.posList.clearSelection();
            this.selectorR.posList.setSelectedIndex(this.releasedIndex);
        }
    }

    private void transferFR(int n) {
        int n2 = ((Player)this.selectorR.squadList.getSelectedValue()).adr;
        int n3 = this.selectorR.teamBox.getSelectedIndex();
        int n4 = -1;
        if (n3 >= 73 && n3 < 221 && this.autoRel.isSelected()) {
            n4 = this.clubRelease(n, true);
        }
        this.of.data[n2] = this.of.toByte(n & 0xFF);
        this.of.data[n2 + 1] = this.of.toByte((n & 0xFF00) >>> 8);
        if (this.of.data[this.getNumAdr(n2)] == -1) {
            this.of.data[this.getNumAdr((int)n2)] = this.getNextNum(n3);
        }
        if (this.selectorR.squadList.getSelectedIndex() > 10) {
            Squads.tidy(this.of, n3);
        }
        this.refreshLists();
        if (n4 != -1) {
            this.selectorL.teamBox.setSelectedIndex(n4);
            this.selectorL.posList.clearSelection();
            this.selectorL.posList.setSelectedIndex(this.releasedIndex);
        }
    }

    private void transferLR(Player player) {
        int n = ((Player)this.selectorR.squadList.getSelectedValue()).adr;
        int n2 = player.index;
        if (n2 != 0) {
            int n3 = this.selectorR.teamBox.getSelectedIndex();
            int n4 = this.selectorL.teamBox.getSelectedIndex();
            int n5 = -1;
            if (n3 >= 73 && n3 < 221 && this.autoRel.isSelected()) {
                n5 = this.clubRelease(n2, true);
            }
            this.of.data[n] = this.of.toByte(n2 & 0xFF);
            this.of.data[n + 1] = this.of.toByte((n2 & 0xFF00) >>> 8);
            if (this.of.data[this.getNumAdr(n)] == -1) {
                this.of.data[this.getNumAdr((int)n)] = this.getNextNum(n3);
            }
            if (this.selectorR.squadList.getSelectedIndex() > 10) {
                Squads.tidy(this.of, this.selectorR.teamBox.getSelectedIndex());
            }
            this.refreshLists();
            if (n5 != -1 && (n4 < 73 || n4 > 220)) {
                this.selectorL.teamBox.setSelectedIndex(n5);
                this.selectorL.posList.clearSelection();
                this.selectorL.posList.setSelectedIndex(this.releasedIndex);
            }
        }
    }

    private void transferRL(Player player) {
        int n = ((Player)this.selectorL.squadList.getSelectedValue()).adr;
        int n2 = player.index;
        if (n2 != 0) {
            int n3 = this.selectorL.teamBox.getSelectedIndex();
            int n4 = this.selectorR.teamBox.getSelectedIndex();
            int n5 = -1;
            if (n3 >= 73 && n3 < 221 && this.autoRel.isSelected()) {
                n5 = this.clubRelease(n2, true);
            }
            this.of.data[n] = this.of.toByte(n2 & 0xFF);
            this.of.data[n + 1] = this.of.toByte((n2 & 0xFF00) >>> 8);
            if (this.of.data[this.getNumAdr(n)] == -1) {
                this.of.data[this.getNumAdr((int)n)] = this.getNextNum(n3);
            }
            if (this.selectorL.squadList.getSelectedIndex() > 10) {
                Squads.tidy(this.of, this.selectorL.teamBox.getSelectedIndex());
            }
            this.refreshLists();
            if (n5 != -1 && (n4 < 73 || n4 > 220)) {
                this.selectorR.teamBox.setSelectedIndex(n5);
                this.selectorR.posList.clearSelection();
                this.selectorR.posList.setSelectedIndex(this.releasedIndex);
            }
        }
    }

    private void transferS(Player player, Player player2, int n, int n2, JList jList, JList jList2) {
        int n3 = player.adr;
        int n4 = player.index;
        int n5 = player2.adr;
        int n6 = player2.index;
        this.of.data[n3] = this.of.toByte(n6 & 0xFF);
        this.of.data[n3 + 1] = this.of.toByte((n6 & 0xFF00) >>> 8);
        this.of.data[n5] = this.of.toByte(n4 & 0xFF);
        this.of.data[n5 + 1] = this.of.toByte((n4 & 0xFF00) >>> 8);
        if (n == n2) {
            byte by = this.of.data[this.getNumAdr(n5)];
            this.of.data[this.getNumAdr((int)n5)] = this.of.data[this.getNumAdr(n3)];
            this.of.data[this.getNumAdr((int)n3)] = by;
        }
        if (n4 == 0 || n6 == 0) {
            if (this.sourceIndex > 10) {
                Squads.tidy(this.of, n);
            } else if (this.autoRep.isSelected()) {
                Squads.tidy11(this.of, n, this.sourceIndex, ((SelectByTeam)jList.getParent()).posList.posNum[this.sourceIndex]);
            }
            if (jList2.getSelectedIndex() > 10) {
                Squads.tidy(this.of, n2);
            } else if (this.autoRep.isSelected() && jList != jList2) {
                Squads.tidy11(this.of, n2, jList2.getSelectedIndex(), ((SelectByTeam)jList2.getParent()).posList.posNum[jList2.getSelectedIndex()]);
            }
        }
        this.refreshLists();
    }

    private void tranRelL(Player player, int n) {
        int n2 = player.adr;
        this.of.data[n2] = 0;
        this.of.data[n2 + 1] = 0;
        this.of.data[this.getNumAdr((int)n2)] = -1;
        if (n > 10) {
            Squads.tidy(this.of, this.selectorL.teamBox.getSelectedIndex());
        } else if (this.autoRep.isSelected()) {
            Squads.tidy11(this.of, this.selectorL.teamBox.getSelectedIndex(), n, this.selectorL.posList.posNum[n]);
        }
        this.refreshLists();
    }

    private void tranRelR(Player player, int n) {
        int n2 = player.adr;
        this.of.data[n2] = 0;
        this.of.data[n2 + 1] = 0;
        this.of.data[this.getNumAdr((int)n2)] = -1;
        if (n > 10) {
            Squads.tidy(this.of, this.selectorR.teamBox.getSelectedIndex());
        } else if (this.autoRep.isSelected()) {
            Squads.tidy11(this.of, this.selectorR.teamBox.getSelectedIndex(), n, this.selectorR.posList.posNum[n]);
        }
        this.refreshLists();
    }

    private void addListen() {
        this.selectorL.squadList.addListSelectionListener(this.nameEditor);
        this.selectorR.squadList.addListSelectionListener(this.nameEditor);
        this.freeList.freeList.addListSelectionListener(this.nameEditor);
        this.selectorL.squadList.addListSelectionListener(this.shirtEditor);
        this.selectorR.squadList.addListSelectionListener(this.shirtEditor);
        this.freeList.freeList.addListSelectionListener(this.shirtEditor);
        this.selectorL.numList.addListSelectionListener(this.numEditor);
        this.selectorR.numList.addListSelectionListener(this.numEditor);
    }

    private void removeListen() {
        this.selectorL.squadList.removeListSelectionListener(this.nameEditor);
        this.selectorR.squadList.removeListSelectionListener(this.nameEditor);
        this.freeList.freeList.removeListSelectionListener(this.nameEditor);
        this.selectorL.squadList.removeListSelectionListener(this.shirtEditor);
        this.selectorR.squadList.removeListSelectionListener(this.shirtEditor);
        this.freeList.freeList.removeListSelectionListener(this.shirtEditor);
        this.selectorL.numList.removeListSelectionListener(this.numEditor);
        this.selectorR.numList.removeListSelectionListener(this.numEditor);
    }

    private class NameEditor
    extends JTextField
    implements ListSelectionListener,
    ActionListener {
        int source;

        public NameEditor() {
            super(13);
            this.source = 0;
            this.addActionListener(this);
            this.setToolTipText("Enter new name and press return");
        }

        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                if (listSelectionEvent.getSource() == ((TransferPanel)TransferPanel.this).freeList.freeList) {
                    if (((TransferPanel)TransferPanel.this).freeList.freeList.isSelectionEmpty()) {
                        this.setText("");
                        TransferPanel.this.lastIndex = 0;
                    } else {
                        this.setText(((Player)((TransferPanel)TransferPanel.this).freeList.freeList.getSelectedValue()).name);
                        this.source = 1;
                        this.selectAll();
                        TransferPanel.this.lastIndex = ((Player)((TransferPanel)TransferPanel.this).freeList.freeList.getSelectedValue()).index;
                    }
                }
                if (listSelectionEvent.getSource() == ((TransferPanel)TransferPanel.this).selectorL.squadList) {
                    if (((TransferPanel)TransferPanel.this).selectorL.squadList.isSelectionEmpty()) {
                        this.setText("");
                        TransferPanel.this.lastIndex = 0;
                    } else {
                        if (((Player)((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedValue()).index != 0) {
                            this.setText(((Player)((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedValue()).name);
                        } else {
                            this.setText("");
                        }
                        this.source = 2;
                        this.selectAll();
                        TransferPanel.this.lastIndex = ((Player)((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedValue()).index;
                    }
                }
                if (listSelectionEvent.getSource() == ((TransferPanel)TransferPanel.this).selectorR.squadList) {
                    if (((TransferPanel)TransferPanel.this).selectorR.squadList.isSelectionEmpty()) {
                        this.setText("");
                        TransferPanel.this.lastIndex = 0;
                    } else {
                        if (((Player)((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedValue()).index != 0) {
                            this.setText(((Player)((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedValue()).name);
                        } else {
                            this.setText("");
                        }
                        this.source = 3;
                        this.selectAll();
                        TransferPanel.this.lastIndex = ((Player)((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedValue()).index;
                    }
                }
                TransferPanel.this.infoPanel.refresh(TransferPanel.this.lastIndex, TransferPanel.this.compIndex);
            }
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int n;
            if (this.source == 1 && !((TransferPanel)TransferPanel.this).freeList.freeList.isSelectionEmpty() && this.getText().length() < 16 && this.getText().length() != 0) {
                n = ((TransferPanel)TransferPanel.this).freeList.freeList.getSelectedIndex();
                if (!((Player)((TransferPanel)TransferPanel.this).freeList.freeList.getSelectedValue()).name.equals(this.getText())) {
                    ((Player)((TransferPanel)TransferPanel.this).freeList.freeList.getSelectedValue()).setName(this.getText());
                    ((Player)((TransferPanel)TransferPanel.this).freeList.freeList.getSelectedValue()).makeShirt(this.getText());
                    TransferPanel.this.refreshLists();
                }
                if (!((TransferPanel)TransferPanel.this).freeList.alpha && n < ((TransferPanel)TransferPanel.this).freeList.freeList.getModel().getSize() - 1) {
                    ((TransferPanel)TransferPanel.this).freeList.freeList.setSelectedIndex(n + 1);
                    ((TransferPanel)TransferPanel.this).freeList.freeList.ensureIndexIsVisible(n + 1);
                }
            }
            if (this.source == 2 && !((TransferPanel)TransferPanel.this).selectorL.squadList.isSelectionEmpty() && this.getText().length() < 16 && this.getText().length() != 0) {
                n = ((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedIndex();
                if (!((Player)((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedValue()).name.equals(this.getText())) {
                    ((Player)((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedValue()).setName(this.getText());
                    ((Player)((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedValue()).makeShirt(this.getText());
                    TransferPanel.this.refreshLists();
                }
                if (n < ((TransferPanel)TransferPanel.this).selectorL.squadList.getModel().getSize() - 1) {
                    ((TransferPanel)TransferPanel.this).selectorL.squadList.setSelectedIndex(n + 1);
                }
            }
            if (this.source == 3 && !((TransferPanel)TransferPanel.this).selectorR.squadList.isSelectionEmpty() && this.getText().length() < 16 && this.getText().length() != 0) {
                n = ((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedIndex();
                if (!((Player)((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedValue()).name.equals(this.getText())) {
                    ((Player)((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedValue()).setName(this.getText());
                    ((Player)((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedValue()).makeShirt(this.getText());
                    TransferPanel.this.refreshLists();
                }
                if (n < ((TransferPanel)TransferPanel.this).selectorR.squadList.getModel().getSize() - 1) {
                    ((TransferPanel)TransferPanel.this).selectorR.squadList.setSelectedIndex(n + 1);
                }
            }
        }
    }

    private class NumEditor
    extends JTextField
    implements ListSelectionListener,
    ActionListener {
        int source;

        public NumEditor() {
            super(2);
            this.source = 0;
            this.addActionListener(this);
            this.setToolTipText("Enter new squad number and press return");
        }

        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if (listSelectionEvent.getSource() == ((TransferPanel)TransferPanel.this).selectorL.numList) {
                if (((TransferPanel)TransferPanel.this).selectorL.numList.isSelectionEmpty()) {
                    this.setText("");
                } else {
                    this.source = 2;
                    this.setText(String.valueOf(TransferPanel.this.getShirt(this.source, ((TransferPanel)TransferPanel.this).selectorL.numList.getSelectedIndex())));
                    ((TransferPanel)TransferPanel.this).selectorR.numList.clearSelection();
                    this.selectAll();
                }
            }
            if (listSelectionEvent.getSource() == ((TransferPanel)TransferPanel.this).selectorR.numList) {
                if (((TransferPanel)TransferPanel.this).selectorR.numList.isSelectionEmpty()) {
                    this.setText("");
                } else {
                    this.source = 3;
                    this.setText(String.valueOf(TransferPanel.this.getShirt(this.source, ((TransferPanel)TransferPanel.this).selectorR.numList.getSelectedIndex())));
                    ((TransferPanel)TransferPanel.this).selectorL.numList.clearSelection();
                    this.selectAll();
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int n;
            int n2;
            if (this.source == 2 && !((TransferPanel)TransferPanel.this).selectorL.numList.isSelectionEmpty()) {
                n2 = ((TransferPanel)TransferPanel.this).selectorL.numList.getSelectedIndex();
                try {
                    n = new Integer(this.getText());
                    if (n != 0 && n <= 99) {
                        TransferPanel.this.setShirt(this.source, n2, n);
                    }
                    ((TransferPanel)TransferPanel.this).selectorR.numList.refresh(((TransferPanel)TransferPanel.this).selectorR.teamBox.getSelectedIndex());
                    ((TransferPanel)TransferPanel.this).selectorL.numList.refresh(((TransferPanel)TransferPanel.this).selectorL.teamBox.getSelectedIndex());
                    if (n2 < ((TransferPanel)TransferPanel.this).selectorL.squadList.getModel().getSize() - 1) {
                        ((TransferPanel)TransferPanel.this).selectorL.numList.setSelectedIndex(n2 + 1);
                    }
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
            if (this.source == 3 && !((TransferPanel)TransferPanel.this).selectorR.numList.isSelectionEmpty()) {
                n2 = ((TransferPanel)TransferPanel.this).selectorR.numList.getSelectedIndex();
                try {
                    n = new Integer(this.getText());
                    if (n != 0 && n <= 99) {
                        TransferPanel.this.setShirt(this.source, n2, n);
                    }
                    ((TransferPanel)TransferPanel.this).selectorR.numList.refresh(((TransferPanel)TransferPanel.this).selectorR.teamBox.getSelectedIndex());
                    ((TransferPanel)TransferPanel.this).selectorL.numList.refresh(((TransferPanel)TransferPanel.this).selectorL.teamBox.getSelectedIndex());
                    if (n2 < ((TransferPanel)TransferPanel.this).selectorR.squadList.getModel().getSize() - 1) {
                        ((TransferPanel)TransferPanel.this).selectorR.numList.setSelectedIndex(n2 + 1);
                    }
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
        }
    }

    public class PlayerTransferable
    implements Transferable {
        Player data;

        public PlayerTransferable(Player player) {
            this.data = player;
        }

        @Override
        public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException {
            if (!this.isDataFlavorSupported(dataFlavor)) {
                throw new UnsupportedFlavorException(dataFlavor);
            }
            return this.data;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{TransferPanel.this.localPlayerFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
            return TransferPanel.this.localPlayerFlavor.equals(dataFlavor);
        }
    }

    private class ShirtNameEditor
    extends JTextField
    implements ListSelectionListener,
    ActionListener {
        int source;

        public ShirtNameEditor() {
            super(13);
            this.source = 0;
            this.addActionListener(this);
            this.setToolTipText("Enter new shirt name and press return");
        }

        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                if (listSelectionEvent.getSource() == ((TransferPanel)TransferPanel.this).freeList.freeList) {
                    if (((TransferPanel)TransferPanel.this).freeList.freeList.isSelectionEmpty()) {
                        this.setText("");
                    } else {
                        this.setText(((Player)((TransferPanel)TransferPanel.this).freeList.freeList.getSelectedValue()).getShirtName());
                        this.source = 1;
                        this.selectAll();
                    }
                }
                if (listSelectionEvent.getSource() == ((TransferPanel)TransferPanel.this).selectorL.squadList) {
                    if (((TransferPanel)TransferPanel.this).selectorL.squadList.isSelectionEmpty()) {
                        this.setText("");
                    } else {
                        this.setText(((Player)((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedValue()).getShirtName());
                        this.source = 2;
                        this.selectAll();
                    }
                }
                if (listSelectionEvent.getSource() == ((TransferPanel)TransferPanel.this).selectorR.squadList) {
                    if (((TransferPanel)TransferPanel.this).selectorR.squadList.isSelectionEmpty()) {
                        this.setText("");
                    } else {
                        this.setText(((Player)((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedValue()).getShirtName());
                        this.source = 3;
                        this.selectAll();
                    }
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int n;
            if (this.source == 1 && !((TransferPanel)TransferPanel.this).freeList.freeList.isSelectionEmpty() && this.getText().length() < 16) {
                ((Player)((TransferPanel)TransferPanel.this).freeList.freeList.getSelectedValue()).setShirtName(this.getText());
                TransferPanel.this.refreshLists();
            }
            if (this.source == 2 && !((TransferPanel)TransferPanel.this).selectorL.squadList.isSelectionEmpty() && this.getText().length() < 16) {
                n = ((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedIndex();
                ((Player)((TransferPanel)TransferPanel.this).selectorL.squadList.getSelectedValue()).setShirtName(this.getText());
                TransferPanel.this.refreshLists();
                if (n < ((TransferPanel)TransferPanel.this).selectorL.squadList.getModel().getSize() - 1) {
                    ((TransferPanel)TransferPanel.this).selectorL.squadList.setSelectedIndex(n + 1);
                }
            }
            if (this.source == 3 && !((TransferPanel)TransferPanel.this).selectorR.squadList.isSelectionEmpty() && this.getText().length() < 16) {
                n = ((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedIndex();
                ((Player)((TransferPanel)TransferPanel.this).selectorR.squadList.getSelectedValue()).setShirtName(this.getText());
                TransferPanel.this.refreshLists();
                if (n < ((TransferPanel)TransferPanel.this).selectorR.squadList.getModel().getSize() - 1) {
                    ((TransferPanel)TransferPanel.this).selectorR.squadList.setSelectedIndex(n + 1);
                }
            }
        }
    }
}

