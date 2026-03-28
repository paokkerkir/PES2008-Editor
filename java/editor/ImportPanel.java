/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Clubs;
import editor.EmblemPanel;
import editor.LeaguePanel;
import editor.Leagues;
import editor.LogoPanel;
import editor.OptionFile;
import editor.Stadia;
import editor.StadiumPanel;
import editor.TeamPanel;
import editor.TransferPanel;
import editor.WENShopPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImportPanel
extends JPanel {
    JLabel importFile;
    JPanel panel;
    private OptionFile of;
    private OptionFile of2;
    private JButton optionsButton;
    private JButton stadiumButton;
    private JButton leagueButton;
    private JButton bootsButton;
    private JButton clubNameButton;
    private JButton clubNameButtonNoPES2008OTC;  // NEW: Don't overwrite PES2008 Other Teams C
    private JButton clubNameButtonNoPES6OTC;     // NEW: No Other Teams C from PES6
    private JButton playerButton;
    private JButton playerButtonNoPES2008OTC;  // NEW: Don't overwrite PES2008 Other Teams C
    private JButton playerButtonNoPES6OTC;     // NEW: No Other Teams C from PES6
    private JButton allKitButton;
    private JButton allKitButtonNoPES2008OTC;    // NEW: Kits - Don't overwrite PES2008 OTC
    private JButton allKitButtonNoPES6OTC;       // NEW: Kits - No PES6 OTC
    private JButton mlYoungButton;               // NEW: Import ML Young players (PES2008 only)
    private WENShopPanel wenShop;
    private StadiumPanel stadPan;
    private LeaguePanel leaguePan;
    private TeamPanel teamPan;
    EmblemPanel flagPan;
    LogoPanel imagePan;
    private TransferPanel tranPan;

    public ImportPanel(OptionFile optionFile, OptionFile optionFile2, WENShopPanel wENShopPanel, StadiumPanel stadiumPanel, LeaguePanel leaguePanel, TeamPanel teamPanel, EmblemPanel emblemPanel, LogoPanel logoPanel, TransferPanel transferPanel) {
        super(new BorderLayout());
        this.of = optionFile;
        this.of2 = optionFile2;
        this.wenShop = wENShopPanel;
        this.stadPan = stadiumPanel;
        this.leaguePan = leaguePanel;
        this.teamPan = teamPanel;
        this.flagPan = emblemPanel;
        this.imagePan = logoPanel;
        this.tranPan = transferPanel;
        JPanel jPanel = new JPanel(new GridLayout(0, 1));
        this.importFile = new JLabel("To use the import options you must first use File > Open OF2...");
        this.panel = new JPanel();
        this.optionsButton = new JButton("Options / PES Points / Shop Items / Cup Gallery / Track Record / Playlist");
        this.optionsButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.arraycopy(((ImportPanel)ImportPanel.this).of2.data, OptionFile.block[0], ((ImportPanel)ImportPanel.this).of.data, OptionFile.block[0], OptionFile.blockSize[0]);
                System.arraycopy(((ImportPanel)ImportPanel.this).of2.data, OptionFile.block[1], ((ImportPanel)ImportPanel.this).of.data, OptionFile.block[1], OptionFile.blockSize[1]);
                System.arraycopy(((ImportPanel)ImportPanel.this).of2.data, OptionFile.block[9], ((ImportPanel)ImportPanel.this).of.data, OptionFile.block[9], OptionFile.blockSize[9]);
                System.arraycopy(((ImportPanel)ImportPanel.this).of2.data, OptionFile.block[10], ((ImportPanel)ImportPanel.this).of.data, OptionFile.block[10], OptionFile.blockSize[10]);
                ((ImportPanel)ImportPanel.this).wenShop.wenPanel.refresh();
                ((ImportPanel)ImportPanel.this).wenShop.shopPanel.status.setText("");
                ImportPanel.this.optionsButton.setEnabled(false);
            }
        });
        this.stadiumButton = new JButton("Stadium names");
        this.stadiumButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Stadia.importData(ImportPanel.this.of2, ImportPanel.this.of);
                ImportPanel.this.stadPan.refresh();
                ImportPanel.this.teamPan.refresh();
                ImportPanel.this.stadiumButton.setEnabled(false);
            }
        });
        this.leagueButton = new JButton("League names");
        this.leagueButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Leagues.importData(ImportPanel.this.of2, ImportPanel.this.of);
                ImportPanel.this.leaguePan.refresh();
                ImportPanel.this.leagueButton.setEnabled(false);
            }
        });
        this.bootsButton = new JButton("Boots");
        this.bootsButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.arraycopy(((ImportPanel)ImportPanel.this).of2.data, 675784, ((ImportPanel)ImportPanel.this).of.data, 675784, 828);
                ImportPanel.this.bootsButton.setEnabled(false);
            }
        });
        this.playerButton = new JButton("Players / Squads / Formations");
        this.playerButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Check if OF2 is PES6
                boolean isPES6 = false;
                if (ImportPanel.this.of2.gameID != null) {
                    isPES6 = ImportPanel.this.of2.gameID.equals("PC_WE") || 
                             ImportPanel.this.of2.gameID.equals("PC_PES") || 
                             (ImportPanel.this.of2.gameID.startsWith("BESLES") && ImportPanel.this.of2.gameID.contains("PES6"));
                }
                
                if (isPES6) {
                    // PES6 to PES2008 - selective import
                    // NOTE: OF2 has already been converted to PES2008 format by PES6ToPES2008Converter
                    // So data is at PES2008 offsets, we just need to limit what we copy
                    System.out.println("Importing from PES6 - preserving PES2008 special teams and extra clubs...");
                    
                    // Copy Block 3 (Edited players 32768+) - safe to copy whole block
                    System.arraycopy(ImportPanel.this.of2.data, OptionFile.block[3], 
                                   ImportPanel.this.of.data, OptionFile.block[3], OptionFile.blockSize[3]);
                    
                    // Copy Block 4 (Players) - ONLY players 1-4413
                    int PLAYER_START_OFFSET = OptionFile.block[4];
                    int PLAYERS_TO_COPY = 4413;
                    int PLAYER_SIZE = 124;
                    int playerDataSize = PLAYERS_TO_COPY * PLAYER_SIZE;
                    
                    System.out.println("  Copying players 1-" + PLAYERS_TO_COPY + " (" + playerDataSize + " bytes)");
                    System.arraycopy(ImportPanel.this.of2.data, PLAYER_START_OFFSET, 
                                   ImportPanel.this.of.data, PLAYER_START_OFFSET, playerDataSize);
                    System.out.println("  PES2008 special teams (IDs 4414+) preserved");
                    
                    // Copy Block 5 (Squads/Formations)
                    // PES6 has: 67 national teams (0-66) + 140 clubs (67-206) = 207 teams total
                    // PES2008 has: 67 national teams (0-66) + 148 clubs (67-214) = 215 teams total
                    // We preserve the last 8 PES2008 clubs (207-214)
                    
                    int SQUAD_NUM_23 = 676624;  // National teams (0-66)
                    int SQUAD_NUM_32 = 678303;  // Club teams (67-214)
                    int SQUAD_SLOT_23 = 683296;
                    int SQUAD_SLOT_32 = 686654;
                    int FORMATION_START = 696638;
                    
                    // Copy squad numbers for 23-man teams (67 teams × 23 bytes = 1541 bytes)
                    System.arraycopy(ImportPanel.this.of2.data, SQUAD_NUM_23, 
                                   ImportPanel.this.of.data, SQUAD_NUM_23, 67 * 23);
                    
                    // Copy squad numbers for 32-man teams - 140 clubs (teams 67-206)
                    // 140 teams × 32 bytes = 4480 bytes
                    System.arraycopy(ImportPanel.this.of2.data, SQUAD_NUM_32, 
                                   ImportPanel.this.of.data, SQUAD_NUM_32, 140 * 32);
                    
                    // Copy squad slots for 23-man teams (67 teams × 46 bytes = 3082 bytes)
                    System.arraycopy(ImportPanel.this.of2.data, SQUAD_SLOT_23, 
                                   ImportPanel.this.of.data, SQUAD_SLOT_23, 67 * 46);
                    
                    // Copy squad slots for 32-man teams - 140 clubs (teams 67-206)
                    // 140 teams × 64 bytes = 8960 bytes
                    System.arraycopy(ImportPanel.this.of2.data, SQUAD_SLOT_32, 
                                   ImportPanel.this.of.data, SQUAD_SLOT_32, 140 * 64);
                    
                    // Copy formations for 204 teams (0-203)
                    // Note: Formations are stored separately from squads
                    System.arraycopy(ImportPanel.this.of2.data, FORMATION_START, 
                                   ImportPanel.this.of.data, FORMATION_START, 204 * 364);
                    
                    System.out.println("  Squads for teams 0-206 (67 national + 140 clubs) copied");
                    System.out.println("  Formations for teams 0-203 (204 teams) copied");
                    System.out.println("  Extra 8 PES2008 teams (207-214) preserved");
                    
                } else {
                    // PES2008 to PES2008 - copy entire blocks
                    System.arraycopy(ImportPanel.this.of2.data, OptionFile.block[3], 
                                   ImportPanel.this.of.data, OptionFile.block[3], OptionFile.blockSize[3]);
                    System.arraycopy(ImportPanel.this.of2.data, OptionFile.block[4], 
                                   ImportPanel.this.of.data, OptionFile.block[4], OptionFile.blockSize[4]);
                    System.arraycopy(ImportPanel.this.of2.data, OptionFile.block[5], 
                                   ImportPanel.this.of.data, OptionFile.block[5], OptionFile.blockSize[5]);
                }
                
                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.playerButton.setEnabled(false);
            }
        });
        
        // OPTION 2: Don't overwrite PES2008 Other Teams C (PES6 only)
        this.playerButtonNoPES2008OTC = new JButton("Players / Squads / Formations (Don't overwrite PES2008 Other Teams C)");
        this.playerButtonNoPES2008OTC.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // This option only appears for PES6 files
                System.out.println("Importing from PES6 - preserving PES2008 Other Teams C (players 4376+ and clubs 197+)...");
                
                // Copy Block 3 (Edited players 32768+)
                System.arraycopy(ImportPanel.this.of2.data, OptionFile.block[3], 
                               ImportPanel.this.of.data, OptionFile.block[3], OptionFile.blockSize[3]);
                
                // Copy Block 4 (Players) - ONLY 1-4375 (stops before PES2008 Other Teams C at 4376)
                int PLAYER_START_OFFSET = OptionFile.block[4];
                int PLAYERS_TO_COPY = 4375;
                int PLAYER_SIZE = 124;
                int playerDataSize = PLAYERS_TO_COPY * PLAYER_SIZE;
                
                System.out.println("  Copying players 1-" + PLAYERS_TO_COPY + " (" + playerDataSize + " bytes)");
                System.arraycopy(ImportPanel.this.of2.data, PLAYER_START_OFFSET, 
                               ImportPanel.this.of.data, PLAYER_START_OFFSET, playerDataSize);
                System.out.println("  PES6 Other Teams C players 4000-4375 imported");
                System.out.println("  PES2008 Other Teams C players 4376+ preserved");
                
                // Copy Block 5 (Squads/Formations) - Only up to club 196
                int SQUAD_NUM_23 = 676624;
                int SQUAD_NUM_32 = 678303;
                int SQUAD_SLOT_23 = 683296;
                int SQUAD_SLOT_32 = 686654;
                int FORMATION_START = 696638;
                
                // National teams (0-66): 67 teams
                System.arraycopy(ImportPanel.this.of2.data, SQUAD_NUM_23, 
                               ImportPanel.this.of.data, SQUAD_NUM_23, 67 * 23);
                
                // Club teams 67-196: 130 clubs (197-206 are PES2008 Other Teams C)
                System.arraycopy(ImportPanel.this.of2.data, SQUAD_NUM_32, 
                               ImportPanel.this.of.data, SQUAD_NUM_32, 130 * 32);
                
                System.arraycopy(ImportPanel.this.of2.data, SQUAD_SLOT_23, 
                               ImportPanel.this.of.data, SQUAD_SLOT_23, 67 * 46);
                
                System.arraycopy(ImportPanel.this.of2.data, SQUAD_SLOT_32, 
                               ImportPanel.this.of.data, SQUAD_SLOT_32, 130 * 64);
                
                // Formations 0-196: 197 teams
                System.arraycopy(ImportPanel.this.of2.data, FORMATION_START, 
                               ImportPanel.this.of.data, FORMATION_START, 197 * 364);
                
                System.out.println("  Squads/formations for teams 0-196 copied");
                System.out.println("  PES2008 Other Teams C clubs 197-214 preserved");
                
                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.playerButtonNoPES2008OTC.setEnabled(false);
            }
        });
        
        // OPTION 3: No Other Teams C from PES6 (PES6 only)
        this.playerButtonNoPES6OTC = new JButton("Players / Squads / Formations (No Other Teams C from PES6)");
        this.playerButtonNoPES6OTC.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // This option only appears for PES6 files
                System.out.println("Importing from PES6 - excluding PES6 Other Teams C completely...");
                
                // Copy Block 3 (Edited players 32768+)
                System.arraycopy(ImportPanel.this.of2.data, OptionFile.block[3], 
                               ImportPanel.this.of.data, OptionFile.block[3], OptionFile.blockSize[3]);
                
                // Copy Block 4 (Players) - ONLY 1-3999 (stops before PES6 Other Teams C at 4000)
                int PLAYER_START_OFFSET = OptionFile.block[4];
                int PLAYERS_TO_COPY = 3999;
                int PLAYER_SIZE = 124;
                int playerDataSize = PLAYERS_TO_COPY * PLAYER_SIZE;
                
                System.out.println("  Copying players 1-" + PLAYERS_TO_COPY + " (" + playerDataSize + " bytes)");
                System.arraycopy(ImportPanel.this.of2.data, PLAYER_START_OFFSET, 
                               ImportPanel.this.of.data, PLAYER_START_OFFSET, playerDataSize);
                System.out.println("  PES6 Other Teams C players 4000+ NOT imported");
                System.out.println("  PES2008 Other Teams C players 4376+ fully preserved");
                
                // Copy Block 5 (Squads/Formations) - Only up to club 188
                int SQUAD_NUM_23 = 676624;
                int SQUAD_NUM_32 = 678303;
                int SQUAD_SLOT_23 = 683296;
                int SQUAD_SLOT_32 = 686654;
                int FORMATION_START = 696638;
                
                // National teams (0-66): 67 teams
                System.arraycopy(ImportPanel.this.of2.data, SQUAD_NUM_23, 
                               ImportPanel.this.of.data, SQUAD_NUM_23, 67 * 23);
                
                // Club teams 67-188: 122 clubs (189-206 are PES6 Other Teams C)
                System.arraycopy(ImportPanel.this.of2.data, SQUAD_NUM_32, 
                               ImportPanel.this.of.data, SQUAD_NUM_32, 122 * 32);
                
                System.arraycopy(ImportPanel.this.of2.data, SQUAD_SLOT_23, 
                               ImportPanel.this.of.data, SQUAD_SLOT_23, 67 * 46);
                
                System.arraycopy(ImportPanel.this.of2.data, SQUAD_SLOT_32, 
                               ImportPanel.this.of.data, SQUAD_SLOT_32, 122 * 64);
                
                // Formations 0-188: 189 teams
                System.arraycopy(ImportPanel.this.of2.data, FORMATION_START, 
                               ImportPanel.this.of.data, FORMATION_START, 189 * 364);
                
                System.out.println("  Squads/formations for teams 0-188 copied");
                System.out.println("  PES6 Other Teams C clubs 189-206 NOT imported");
                System.out.println("  PES2008 Other Teams C clubs 197-214 fully preserved");
                
                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.playerButtonNoPES6OTC.setEnabled(false);
            }
        });
        this.clubNameButton = new JButton("Club names");
        this.clubNameButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Check if OF2 is PES6
                boolean isPES6 = false;
                if (ImportPanel.this.of2.gameID != null) {
                    isPES6 = ImportPanel.this.of2.gameID.equals("PC_WE") || 
                             ImportPanel.this.of2.gameID.equals("PC_PES") || 
                             (ImportPanel.this.of2.gameID.startsWith("BESLES") && ImportPanel.this.of2.gameID.contains("PES6"));
                }
                
                if (isPES6) {
                    // PES6: Import all 140 clubs (clubs 0-139, which maps to teams 67-206)
                    // PES2008 has 148 clubs (clubs 0-147, teams 67-214)
                    // Preserve PES2008 clubs 140-147 (teams 207-214)
                    // NOTE: OF2 has been converted to PES2008 format, so use PES2008 club offset
                    int CLUB_START = 773820;  // PES2008 club offset (OF2 is already converted)
                    int CLUB_SIZE = 88;
                    int CLUB_NAME_SIZE = 57;  // First 57 bytes are the name
                    
                    System.out.println("Importing club names from PES6 - all 140 clubs...");
                    for (int i = 0; i < 140; i++) {
                        System.arraycopy(ImportPanel.this.of2.data, CLUB_START + (i * CLUB_SIZE), 
                                       ImportPanel.this.of.data, CLUB_START + (i * CLUB_SIZE), CLUB_NAME_SIZE);
                    }
                    System.out.println("  All 140 PES6 club names imported (clubs 0-139 / teams 67-206)");
                    System.out.println("  PES2008 clubs 140-147 (teams 207-214) preserved");
                } else {
                    // PES2008 to PES2008: Use standard import
                    Clubs.importNames(ImportPanel.this.of, ImportPanel.this.of2);
                }
                
                ImportPanel.this.teamPan.refresh();
                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.clubNameButton.setEnabled(false);
            }
        });
        
        // OPTION 2: Club names - Don't overwrite PES2008 Other Teams C (PES6 only)
        this.clubNameButtonNoPES2008OTC = new JButton("Club names (Don't overwrite PES2008 Other Teams C)");
        this.clubNameButtonNoPES2008OTC.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Import clubs 0-129 (teams 67-196), preserve PES2008 OTC clubs 130-147 (teams 197-214)
                int CLUB_START = 773820;  // PES2008 club offset (OF2 is already converted)
                int CLUB_SIZE = 88;
                int CLUB_NAME_SIZE = 57;
                
                System.out.println("Importing club names from PES6 - clubs 0-129 (preserving PES2008 Other Teams C)...");
                for (int i = 0; i < 130; i++) {
                    System.arraycopy(ImportPanel.this.of2.data, CLUB_START + (i * CLUB_SIZE), 
                                   ImportPanel.this.of.data, CLUB_START + (i * CLUB_SIZE), CLUB_NAME_SIZE);
                }
                System.out.println("  130 PES6 club names imported (clubs 0-129 / teams 67-196)");
                System.out.println("  PES2008 Other Teams C clubs 130-147 (teams 197-214) preserved");
                
                ImportPanel.this.teamPan.refresh();
                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.clubNameButtonNoPES2008OTC.setEnabled(false);
            }
        });
        
        // OPTION 3: Club names - No Other Teams C from PES6 (PES6 only)
        this.clubNameButtonNoPES6OTC = new JButton("Club names (No Other Teams C from PES6)");
        this.clubNameButtonNoPES6OTC.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Import clubs 0-121 (teams 67-188), exclude PES6 OTC clubs 122-139 (teams 189-206)
                // Preserve PES2008 clubs 122-147 (teams 189-214)
                int CLUB_START = 773820;  // PES2008 club offset (OF2 is already converted)
                int CLUB_SIZE = 88;
                int CLUB_NAME_SIZE = 57;
                
                System.out.println("Importing club names from PES6 - clubs 0-121 (no PES6 Other Teams C)...");
                for (int i = 0; i < 122; i++) {
                    System.arraycopy(ImportPanel.this.of2.data, CLUB_START + (i * CLUB_SIZE), 
                                   ImportPanel.this.of.data, CLUB_START + (i * CLUB_SIZE), CLUB_NAME_SIZE);
                }
                System.out.println("  122 PES6 club names imported (clubs 0-121 / teams 67-188)");
                System.out.println("  PES6 Other Teams C clubs 122-139 (teams 189-206) NOT imported");
                System.out.println("  PES2008 clubs 122-147 (teams 189-214) fully preserved");
                
                ImportPanel.this.teamPan.refresh();
                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.clubNameButtonNoPES6OTC.setEnabled(false);
            }
        });
        this.allKitButton = new JButton("Kits / Emblems / Logos / Club stadiums, flags, colours");
        this.allKitButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Check if OF2 is PES6
                boolean isPES6 = false;
                if (ImportPanel.this.of2.gameID != null) {
                    isPES6 = ImportPanel.this.of2.gameID.equals("PC_WE") || 
                             ImportPanel.this.of2.gameID.equals("PC_PES") || 
                             (ImportPanel.this.of2.gameID.startsWith("BESLES") && ImportPanel.this.of2.gameID.contains("PES6"));
                }
                
                if (isPES6) {
                    // PES6: Import all 140 clubs' kit data
                    System.out.println("Importing kits/flags/colours from PES6 - all 140 clubs...");
                    
                    // Import club stadium/flag/colour data (31 bytes per club, starting at byte 57)
                    int CLUB_START = 773820;  // PES2008 club offset
                    int CLUB_SIZE = 88;
                    int CLUB_DATA_OFFSET = 57;  // Stadium/flag/colour starts at byte 57
                    int CLUB_DATA_SIZE = 31;
                    
                    for (int i = 0; i < 140; i++) {
                        System.arraycopy(ImportPanel.this.of2.data, CLUB_START + (i * CLUB_SIZE) + CLUB_DATA_OFFSET, 
                                       ImportPanel.this.of.data, CLUB_START + (i * CLUB_SIZE) + CLUB_DATA_OFFSET, CLUB_DATA_SIZE);
                    }
                    
                    // Import kits (Block 7) - National kits (all 64) + Club kits (140 clubs)
                    int KIT_NATIONAL_START = 786856;
                    int KIT_NATIONAL_COUNT = 64;
                    int KIT_NATIONAL_SIZE = 352;
                    int KIT_CLUB_START = 809384;
                    int KIT_CLUB_SIZE = 544;
                    
                    // Copy all national kits
                    System.arraycopy(ImportPanel.this.of2.data, KIT_NATIONAL_START, 
                                   ImportPanel.this.of.data, KIT_NATIONAL_START, KIT_NATIONAL_COUNT * KIT_NATIONAL_SIZE);
                    
                    // Copy club kits for 140 clubs
                    System.arraycopy(ImportPanel.this.of2.data, KIT_CLUB_START, 
                                   ImportPanel.this.of.data, KIT_CLUB_START, 140 * KIT_CLUB_SIZE);
                    
                    // NOTE: Block 8 (Emblems/Logos) NOT copied - PES6 doesn't have this data
                    
                    System.out.println("  All PES6 kits/flags/colours imported (140 clubs)");
                    System.out.println("  PES2008 clubs 140-147 preserved");
                } else {
                    // PES2008 to PES2008: Standard import
                    Clubs.importData(ImportPanel.this.of, ImportPanel.this.of2);
                    System.arraycopy(ImportPanel.this.of2.data, OptionFile.block[7], 
                                   ImportPanel.this.of.data, OptionFile.block[7], OptionFile.blockSize[7]);
                    System.arraycopy(ImportPanel.this.of2.data, OptionFile.block[8], 
                                   ImportPanel.this.of.data, OptionFile.block[8], OptionFile.blockSize[8]);
                }
                
                ImportPanel.this.flagPan.refresh();
                ImportPanel.this.imagePan.refresh();
                ImportPanel.this.teamPan.refresh();
                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.allKitButton.setEnabled(false);
            }
        });
        
        // OPTION 2: Kits - Don't overwrite PES2008 Other Teams C (PES6 only)
        this.allKitButtonNoPES2008OTC = new JButton("Kits / Flags / Colours (Don't overwrite PES2008 Other Teams C)");
        this.allKitButtonNoPES2008OTC.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Importing kits/flags/colours from PES6 - clubs 0-129...");
                
                // Import club stadium/flag/colour data for clubs 0-129
                int CLUB_START = 773820;
                int CLUB_SIZE = 88;
                int CLUB_DATA_OFFSET = 57;
                int CLUB_DATA_SIZE = 31;
                
                for (int i = 0; i < 130; i++) {
                    System.arraycopy(ImportPanel.this.of2.data, CLUB_START + (i * CLUB_SIZE) + CLUB_DATA_OFFSET, 
                                   ImportPanel.this.of.data, CLUB_START + (i * CLUB_SIZE) + CLUB_DATA_OFFSET, CLUB_DATA_SIZE);
                }
                
                // Import kits - National (all 64) + Club kits (130 clubs)
                int KIT_NATIONAL_START = 786856;
                int KIT_NATIONAL_COUNT = 64;
                int KIT_NATIONAL_SIZE = 352;
                int KIT_CLUB_START = 809384;
                int KIT_CLUB_SIZE = 544;
                
                System.arraycopy(ImportPanel.this.of2.data, KIT_NATIONAL_START, 
                               ImportPanel.this.of.data, KIT_NATIONAL_START, KIT_NATIONAL_COUNT * KIT_NATIONAL_SIZE);
                System.arraycopy(ImportPanel.this.of2.data, KIT_CLUB_START, 
                               ImportPanel.this.of.data, KIT_CLUB_START, 130 * KIT_CLUB_SIZE);
                
                // NOTE: Block 8 (Emblems/Logos) NOT copied - PES6 doesn't have this data
                
                System.out.println("  Kits/flags/colours for clubs 0-129 imported");
                System.out.println("  PES2008 Other Teams C clubs 130-147 preserved");
                
                ImportPanel.this.flagPan.refresh();
                ImportPanel.this.imagePan.refresh();
                ImportPanel.this.teamPan.refresh();
                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.allKitButtonNoPES2008OTC.setEnabled(false);
            }
        });
        
        // OPTION 3: Kits - No Other Teams C from PES6 (PES6 only)
        this.allKitButtonNoPES6OTC = new JButton("Kits / Flags / Colours (No Other Teams C from PES6)");
        this.allKitButtonNoPES6OTC.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Importing kits/flags/colours from PES6 - clubs 0-121...");
                
                // Import club stadium/flag/colour data for clubs 0-121
                int CLUB_START = 773820;
                int CLUB_SIZE = 88;
                int CLUB_DATA_OFFSET = 57;
                int CLUB_DATA_SIZE = 31;
                
                for (int i = 0; i < 122; i++) {
                    System.arraycopy(ImportPanel.this.of2.data, CLUB_START + (i * CLUB_SIZE) + CLUB_DATA_OFFSET, 
                                   ImportPanel.this.of.data, CLUB_START + (i * CLUB_SIZE) + CLUB_DATA_OFFSET, CLUB_DATA_SIZE);
                }
                
                // Import kits - National (all 64) + Club kits (122 clubs)
                int KIT_NATIONAL_START = 786856;
                int KIT_NATIONAL_COUNT = 64;
                int KIT_NATIONAL_SIZE = 352;
                int KIT_CLUB_START = 809384;
                int KIT_CLUB_SIZE = 544;
                
                System.arraycopy(ImportPanel.this.of2.data, KIT_NATIONAL_START, 
                               ImportPanel.this.of.data, KIT_NATIONAL_START, KIT_NATIONAL_COUNT * KIT_NATIONAL_SIZE);
                System.arraycopy(ImportPanel.this.of2.data, KIT_CLUB_START, 
                               ImportPanel.this.of.data, KIT_CLUB_START, 122 * KIT_CLUB_SIZE);
                
                // NOTE: Block 8 (Emblems/Logos) NOT copied - PES6 doesn't have this data
                
                System.out.println("  Kits/flags/colours for clubs 0-121 imported");
                System.out.println("  PES6 Other Teams C clubs 122-139 NOT imported");
                System.out.println("  PES2008 clubs 122-147 fully preserved");
                
                ImportPanel.this.flagPan.refresh();
                ImportPanel.this.imagePan.refresh();
                ImportPanel.this.teamPan.refresh();
                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.allKitButtonNoPES6OTC.setEnabled(false);
            }
        });
        // NEW: ML Young players import button (PES2008 to PES2008 only)
        this.mlYoungButton = new JButton("Import ML Young Players Only (IDs 4978-5154)");
        this.mlYoungButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Copy player data for IDs 4978-5154 (ML Young players)
                // Player data in block 4 at offset 34704, each player is 124 bytes
                int PLAYER_START = OptionFile.block[4];  // 34704
                int PLAYER_SIZE = 124;
                int FIRST_YOUNG = 4978;
                int LAST_YOUNG = 5154;  // inclusive
                int COUNT = LAST_YOUNG - FIRST_YOUNG + 1;  // 177 players

                int srcOffset = PLAYER_START + FIRST_YOUNG * PLAYER_SIZE;
                int copySize = COUNT * PLAYER_SIZE;

                System.out.println("Importing ML Young players (IDs " + FIRST_YOUNG + "-" + LAST_YOUNG + ")...");
                System.out.println("  Offset: " + srcOffset + ", Size: " + copySize + " bytes (" + COUNT + " players)");

                System.arraycopy(ImportPanel.this.of2.data, srcOffset,
                               ImportPanel.this.of.data, srcOffset, copySize);

                // Also copy the edited player flags for these players (block 3)
                // Block 3 starts at 11876, stores edited player data for IDs 32768+
                // ML Young players are regular IDs (4978-5154), their edit flags are in block 4 data

                System.out.println("  ML Young players imported successfully");

                ImportPanel.this.tranPan.refresh();
                ImportPanel.this.mlYoungButton.setEnabled(false);
            }
        });
        jPanel.add(this.optionsButton);
        jPanel.add(this.stadiumButton);
        jPanel.add(this.leagueButton);
        jPanel.add(this.bootsButton);
        jPanel.add(this.playerButton);
        jPanel.add(this.playerButtonNoPES2008OTC);
        jPanel.add(this.playerButtonNoPES6OTC);
        jPanel.add(this.clubNameButton);
        jPanel.add(this.clubNameButtonNoPES2008OTC);
        jPanel.add(this.clubNameButtonNoPES6OTC);
        jPanel.add(this.allKitButton);
        jPanel.add(this.allKitButtonNoPES2008OTC);
        jPanel.add(this.allKitButtonNoPES6OTC);
        jPanel.add(this.mlYoungButton);

        // Hide PES6-specific buttons by default
        this.playerButtonNoPES2008OTC.setVisible(false);
        this.playerButtonNoPES6OTC.setVisible(false);
        this.clubNameButtonNoPES2008OTC.setVisible(false);
        this.clubNameButtonNoPES6OTC.setVisible(false);
        this.allKitButtonNoPES2008OTC.setVisible(false);
        this.allKitButtonNoPES6OTC.setVisible(false);
        // Hide ML Young button by default (only shown for PES2008-to-PES2008)
        this.mlYoungButton.setVisible(false);
        
        this.panel.add(jPanel);
        this.add((Component)this.importFile, "North");
        this.add((Component)this.panel, "Center");
        this.panel.setEnabled(false);
    }

    public void refresh() {
        if (this.of2.fileName == null) {
            this.panel.setVisible(false);
            this.importFile.setText("To use the import options you must first use File > Open OF2...");
        } else {
            this.importFile.setText("From:  " + this.of2.fileName);
            this.panel.setVisible(true);
            
            // Check if OF2 is PES6 file
            boolean isPES6 = false;
            if (this.of2.gameID != null) {
                isPES6 = this.of2.gameID.equals("PC_WE") || 
                         this.of2.gameID.equals("PC_PES") || 
                         (this.of2.gameID.startsWith("BESLES") && this.of2.gameID.contains("PES6"));
            }
            
            if (this.of.gameID.equals(this.of2.gameID)) {
                this.optionsButton.setEnabled(true);
            } else {
                this.optionsButton.setEnabled(false);
            }
            
            // Disable boots, stadium, and league buttons for PES6 files
            // Change allKitButton text based on source
            // Show/hide PES6-specific import buttons
            if (isPES6) {
                this.stadiumButton.setEnabled(false);
                this.leagueButton.setEnabled(false);
                this.bootsButton.setEnabled(false);
                this.allKitButton.setText("Kits / Flags / Colours");

                // Show PES6-specific player import options
                this.playerButtonNoPES2008OTC.setVisible(true);
                this.playerButtonNoPES6OTC.setVisible(true);
                this.playerButtonNoPES2008OTC.setEnabled(true);
                this.playerButtonNoPES6OTC.setEnabled(true);

                // Show PES6-specific club name import options
                this.clubNameButtonNoPES2008OTC.setVisible(true);
                this.clubNameButtonNoPES6OTC.setVisible(true);
                this.clubNameButtonNoPES2008OTC.setEnabled(true);
                this.clubNameButtonNoPES6OTC.setEnabled(true);

                // Show PES6-specific kit import options
                this.allKitButtonNoPES2008OTC.setVisible(true);
                this.allKitButtonNoPES6OTC.setVisible(true);
                this.allKitButtonNoPES2008OTC.setEnabled(true);
                this.allKitButtonNoPES6OTC.setEnabled(true);

                // Hide ML Young button for PES6
                this.mlYoungButton.setVisible(false);
            } else {
                this.stadiumButton.setEnabled(true);
                this.leagueButton.setEnabled(true);
                this.bootsButton.setEnabled(true);
                this.allKitButton.setText("Kits / Emblems / Logos / Club stadiums, flags, colours");

                // Hide PES6-specific player import options
                this.playerButtonNoPES2008OTC.setVisible(false);
                this.playerButtonNoPES6OTC.setVisible(false);

                // Hide PES6-specific club name import options
                this.clubNameButtonNoPES2008OTC.setVisible(false);
                this.clubNameButtonNoPES6OTC.setVisible(false);

                // Hide PES6-specific kit import options
                this.allKitButtonNoPES2008OTC.setVisible(false);
                this.allKitButtonNoPES6OTC.setVisible(false);

                // Show ML Young button for PES2008-to-PES2008
                this.mlYoungButton.setVisible(true);
                this.mlYoungButton.setEnabled(true);
            }
            
            this.clubNameButton.setEnabled(true);
            this.playerButton.setEnabled(true);
            this.allKitButton.setEnabled(true);
        }
    }

    public void disableAll() {
        this.optionsButton.setEnabled(false);
        this.stadiumButton.setEnabled(false);
        this.leagueButton.setEnabled(false);
        this.bootsButton.setEnabled(false);
        this.clubNameButton.setEnabled(false);
        this.clubNameButtonNoPES2008OTC.setEnabled(false);
        this.clubNameButtonNoPES6OTC.setEnabled(false);
        this.playerButton.setEnabled(false);
        this.playerButtonNoPES2008OTC.setEnabled(false);
        this.playerButtonNoPES6OTC.setEnabled(false);
        this.allKitButton.setEnabled(false);
        this.allKitButtonNoPES2008OTC.setEnabled(false);
        this.allKitButtonNoPES6OTC.setEnabled(false);
        this.mlYoungButton.setEnabled(false);
    }
}

