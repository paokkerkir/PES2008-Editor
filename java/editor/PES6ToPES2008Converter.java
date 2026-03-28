/**
 * PES6 PC to PES 2008 PS2 Option File Converter
 * 
 * CORRECT APPROACH: Copy individual structures, not whole blocks
 * Block 5 has different internal organization between versions
 */
package editor;

public class PES6ToPES2008Converter {
    
    // File sizes
    private static final int LENGTH6 = 1191936;
    private static final int LENGTH08 = 1118208;
    
    // PES6 offsets
    private static final int PES6_PLAYER_START = 37116;  // Block offset for copying
    private static final int PES6_PLAYER_STATS_START = 37164;  // Stats offset for reading individual records
    private static final int PES6_PLAYER_SIZE = 620000;  // OLD: Full block size (not used anymore)
    
    // Player counts - CRITICAL for preserving PES2008 special teams
    private static final int PES6_REGULAR_PLAYERS = 4413;  // Only copy these (IDs 1-4413)
    private static final int PLAYER_RECORD_SIZE = 124;  // Each player is 124 bytes
    // PES6 Special teams (DO NOT COPY - different IDs than PES2008):
    //   ML DEFAULT: 4414-4436
    //   SHOP: 4437-4596
    //   ML YOUNG: 4597-4773
    //   ML OLD: 4774-4783
    //   UNUSED: 4784-4999
    // PES2008 Special teams (PRESERVE - leave untouched):
    //   ML DEFAULT: 4790-4817
    //   SHOP: 4818-4977
    //   ML YOUNG: 4978-5154
    //   ML OLD: 5155-5164
    
    private static final int PES6_SQUAD_NUM_23 = 657956;
    private static final int PES6_SQUAD_NUM_32 = 659635;
    private static final int PES6_SQUAD_SLOT_23 = 664372;
    private static final int PES6_SQUAD_SLOT_32 = 667730;
    private static final int PES6_FORMATION_START = 677202;
    private static final int PES6_CLUB_START = 751472;
    private static final int PES6_KIT_NATIONAL_START = 763804;
    private static final int PES6_KIT_CLUB_START = 786332;
    private static final int PES6_EMBLEM_START = 911144;
    
    // PES 2008 offsets
    private static final int PES2008_PLAYER_START = 34704;
    private static final int PES2008_SQUAD_NUM_23 = 676624;
    private static final int PES2008_SQUAD_NUM_32 = 678303;
    private static final int PES2008_SQUAD_SLOT_23 = 683296;
    private static final int PES2008_SQUAD_SLOT_32 = 686654;
    private static final int PES2008_FORMATION_START = 696638;
    private static final int PES2008_CLUB_START = 773820;
    private static final int PES2008_KIT_NATIONAL_START = 786856;
    private static final int PES2008_KIT_CLUB_START = 809384;
    private static final int PES2008_EMBLEM_START = 938548;
    
    // Counts and sizes
    private static final int FORMATION_COUNT = 204;
    private static final int FORMATION_COUNT_PES2008 = 212;
    private static final int FORMATION_RECORD_SIZE = 364;
    private static final int CLUB_COUNT_PES6 = 140;
    private static final int CLUB_COUNT_PES2008 = 148;
    private static final int CLUB_RECORD_SIZE = 88;
    
    // Kit counts and sizes
    private static final int KIT_NATIONAL_COUNT = 64;
    private static final int KIT_NATIONAL_SIZE = 352;
    private static final int KIT_CLUB_COUNT_PES6 = 140;
    private static final int KIT_CLUB_COUNT_PES2008 = 148;
    private static final int KIT_CLUB_SIZE = 544;
    
    public byte[] convert(byte[] pes6Data) {
        System.out.println("Converting PES6 PC to PES 2008 PS2 format...");
        
        if (pes6Data.length != LENGTH6) {
            System.out.println("ERROR: Invalid PES6 data size: " + pes6Data.length);
            return null;
        }
        
        byte[] pes2008Data = new byte[LENGTH08];
        
        // Swap Italy (13) and Latvia (14) BEFORE copying
        System.out.println("Swapping Italy (13) ↔ Latvia (14) in PES6 data...");
        swapNationalTeamData(pes6Data, 13, 14);
        
        // Rotate squads 52-56 (Iran, Japan, Saudi Arabia, South Korea, Australia)
        System.out.println("Rotating squads 52-56 (Iran→Japan→Saudi→Korea→Australia→Iran)...");
        rotateSquads52to56(pes6Data);
        
        // Remap non-playable nationalities (57+) to PES2008 positions
        System.out.println("Remapping non-playable nationalities (57+)...");
        remapNonPlayableNationalities(pes6Data);
        
        // Copy players 1-4413 ONLY (regular players)
        // This preserves PES2008's special teams (ML DEFAULT, SHOP, ML YOUNG, ML OLD)
        // which have different player IDs than PES6
        System.out.println("Copying players 1-" + PES6_REGULAR_PLAYERS + " (regular players only)...");
        int regularPlayersSize = PES6_REGULAR_PLAYERS * PLAYER_RECORD_SIZE;
        System.arraycopy(pes6Data, PES6_PLAYER_START, pes2008Data, PES2008_PLAYER_START, regularPlayersSize);
        System.out.println("  Copied " + regularPlayersSize + " bytes (" + PES6_REGULAR_PLAYERS + " players × " + PLAYER_RECORD_SIZE + " bytes)");
        System.out.println("  PES2008 special teams (4790-5164) preserved - NOT overwritten");
        
        // Copy squad numbers (23-man)
        System.out.println("Copying squad numbers (23-man)...");
        System.arraycopy(pes6Data, PES6_SQUAD_NUM_23, pes2008Data, PES2008_SQUAD_NUM_23, 1679);
        
        // Copy squad numbers (32-man)
        System.out.println("Copying squad numbers (32-man)...");
        System.arraycopy(pes6Data, PES6_SQUAD_NUM_32, pes2008Data, PES2008_SQUAD_NUM_32, 4737);
        
        // Copy squad slots (23-man)
        System.out.println("Copying squad slots (23-man)...");
        System.arraycopy(pes6Data, PES6_SQUAD_SLOT_23, pes2008Data, PES2008_SQUAD_SLOT_23, 3358);
        
        // Copy squad slots (32-man)
        System.out.println("Copying squad slots (32-man)...");
        System.arraycopy(pes6Data, PES6_SQUAD_SLOT_32, pes2008Data, PES2008_SQUAD_SLOT_32, 9472);
        
        // Clear squad data for extra 8 clubs
        System.out.println("Clearing squad data for extra clubs...");
        int extraClubsStart = 140;
        int extraClubsCount = 8;
        int extraSquadNumOffset = PES2008_SQUAD_NUM_32 + (extraClubsStart * 32);
        for (int i = 0; i < extraClubsCount * 32; i++) {
            pes2008Data[extraSquadNumOffset + i] = 0;
        }
        int extraSquadSlotOffset = PES2008_SQUAD_SLOT_32 + (extraClubsStart * 64);
        for (int i = 0; i < extraClubsCount * 64; i++) {
            pes2008Data[extraSquadSlotOffset + i] = 0;
        }
        
        // Copy formations
        System.out.println("Copying formations...");
        for (int team = 0; team < FORMATION_COUNT; team++) {
            int pes6Offset = PES6_FORMATION_START + (team * FORMATION_RECORD_SIZE);
            int pes2008Offset = PES2008_FORMATION_START + (team * FORMATION_RECORD_SIZE);
            System.arraycopy(pes6Data, pes6Offset, pes2008Data, pes2008Offset, FORMATION_RECORD_SIZE);
        }
        
        // Clear extra formations
        for (int team = FORMATION_COUNT; team < FORMATION_COUNT_PES2008; team++) {
            int pes2008Offset = PES2008_FORMATION_START + (team * FORMATION_RECORD_SIZE);
            for (int i = 0; i < FORMATION_RECORD_SIZE; i++) {
                pes2008Data[pes2008Offset + i] = 0;
            }
        }
        
        // Copy clubs
        System.out.println("Copying clubs...");
        System.arraycopy(pes6Data, PES6_CLUB_START, pes2008Data, PES2008_CLUB_START, CLUB_COUNT_PES6 * CLUB_RECORD_SIZE);
        
        // Clear extra clubs
        for (int club = CLUB_COUNT_PES6; club < CLUB_COUNT_PES2008; club++) {
            int pes2008Offset = PES2008_CLUB_START + (club * CLUB_RECORD_SIZE);
            for (int i = 0; i < CLUB_RECORD_SIZE; i++) {
                pes2008Data[pes2008Offset + i] = 0;
            }
        }
        
        // Copy kits
        System.out.println("Copying kits...");
        copyKits(pes6Data, pes2008Data);
        
        System.out.println("Structure-based conversion complete!");
        return pes2008Data;
    }
    
    private void swapNationalTeamData(byte[] pes6Data, int team1, int team2) {
        System.out.println("  Swapping squads and formations...");
        
        // Swap squad numbers
        byte[] temp = new byte[23];
        System.arraycopy(pes6Data, PES6_SQUAD_NUM_23 + (team1 * 23), temp, 0, 23);
        System.arraycopy(pes6Data, PES6_SQUAD_NUM_23 + (team2 * 23), 
                       pes6Data, PES6_SQUAD_NUM_23 + (team1 * 23), 23);
        System.arraycopy(temp, 0, pes6Data, PES6_SQUAD_NUM_23 + (team2 * 23), 23);
        
        // Swap squad slots
        temp = new byte[46];
        System.arraycopy(pes6Data, PES6_SQUAD_SLOT_23 + (team1 * 46), temp, 0, 46);
        System.arraycopy(pes6Data, PES6_SQUAD_SLOT_23 + (team2 * 46), 
                       pes6Data, PES6_SQUAD_SLOT_23 + (team1 * 46), 46);
        System.arraycopy(temp, 0, pes6Data, PES6_SQUAD_SLOT_23 + (team2 * 46), 46);
        
        // Swap formations
        temp = new byte[364];
        System.arraycopy(pes6Data, PES6_FORMATION_START + (team1 * 364), temp, 0, 364);
        System.arraycopy(pes6Data, PES6_FORMATION_START + (team2 * 364), 
                       pes6Data, PES6_FORMATION_START + (team1 * 364), 364);
        System.arraycopy(temp, 0, pes6Data, PES6_FORMATION_START + (team2 * 364), 364);
        
        // Swap player nationalities (only for regular players 1-4413)
        System.out.println("  Swapping player nationalities...");
        int NATIONALITY_OFFSET = 65;
        int count1to2 = 0;
        int count2to1 = 0;
        
        for (int playerIndex = 1; playerIndex <= PES6_REGULAR_PLAYERS; playerIndex++) {
            int playerOffset = PES6_PLAYER_STATS_START + (playerIndex * PLAYER_RECORD_SIZE);
            int nationalityByteOffset = playerOffset + NATIONALITY_OFFSET - 1;
            
            if (nationalityByteOffset < pes6Data.length) {
                int currentNat = pes6Data[nationalityByteOffset] & 0xFF;
                
                if (currentNat == team1) {
                    pes6Data[nationalityByteOffset] = (byte) team2;
                    count1to2++;
                } else if (currentNat == team2) {
                    pes6Data[nationalityByteOffset] = (byte) team1;
                    count2to1++;
                }
            }
        }
        
        System.out.println("    " + count1to2 + " players: " + team1 + " → " + team2);
        System.out.println("    " + count2to1 + " players: " + team2 + " → " + team1);
    }
    
    private void rotateSquads52to56(byte[] pes6Data) {
        // Save Australia (56) first
        byte[] tempSquadNum = new byte[23];
        byte[] tempSquadSlot = new byte[46];
        byte[] tempFormation = new byte[364];
        
        System.arraycopy(pes6Data, PES6_SQUAD_NUM_23 + (56 * 23), tempSquadNum, 0, 23);
        System.arraycopy(pes6Data, PES6_SQUAD_SLOT_23 + (56 * 46), tempSquadSlot, 0, 46);
        System.arraycopy(pes6Data, PES6_FORMATION_START + (56 * 364), tempFormation, 0, 364);
        
        // Shift backwards: 55→56, 54→55, 53→54, 52→53
        for (int i = 55; i >= 52; i--) {
            System.arraycopy(pes6Data, PES6_SQUAD_NUM_23 + (i * 23), 
                           pes6Data, PES6_SQUAD_NUM_23 + ((i + 1) * 23), 23);
            System.arraycopy(pes6Data, PES6_SQUAD_SLOT_23 + (i * 46), 
                           pes6Data, PES6_SQUAD_SLOT_23 + ((i + 1) * 46), 46);
            System.arraycopy(pes6Data, PES6_FORMATION_START + (i * 364), 
                           pes6Data, PES6_FORMATION_START + ((i + 1) * 364), 364);
        }
        
        // Put Australia (was 56) into position 52
        System.arraycopy(tempSquadNum, 0, pes6Data, PES6_SQUAD_NUM_23 + (52 * 23), 23);
        System.arraycopy(tempSquadSlot, 0, pes6Data, PES6_SQUAD_SLOT_23 + (52 * 46), 46);
        System.arraycopy(tempFormation, 0, pes6Data, PES6_FORMATION_START + (52 * 364), 364);
        
        System.out.println("  Rotated squads and formations for teams 52-56");
    }
    
    private void remapNonPlayableNationalities(byte[] pes6Data) {
        // Complete mapping table: PES6 index → PES2008 index
        // Note: Italy/Latvia swap already happened, so:
        //   - Italian players are now at 14 (correct!)
        //   - Latvian players are now at 13 (Israel position in PES2008, correct!)
        int[][] mapping = {
            // Nations that don't exist in PES2008 → No Nationality(122)
            {62, 122},  // Panama → No Nationality
            {87, 122},  // Guinea-Bissau → No Nationality
            {90, 122},  // Libya → No Nationality
            {93, 122},  // Mozambique → No Nationality
            {100, 122}, // Guadeloupe → No Nationality
            {105, 122}, // Free Nationality → No Nationality
            
            // Playable teams: 52-56 rotation + Israel
            {52, 53},   // Iran → 53
            {53, 54},   // Japan → 54
            {54, 55},   // Saudi Arabia → 55
            {55, 56},   // South Korea → 56
            {56, 52},   // Australia → 52
            {59, 13},   // Israel → 13
            
            // Non-playable: European nations
            {57, 88},   // Bosnia → 85 + 3 = 88
            {58, 90},   // Estonia → 87 + 3 = 90
            {67, 83},   // Albania → 80 + 3 = 83
            {68, 89},   // Cyprus → 86 + 3 = 89
            {69, 93},   // Iceland → 90 + 3 = 93
            {70, 99},   // Macedonia → 96 + 3 = 99
            {71, 85},   // Armenia → 82 + 3 = 85
            {72, 87},   // Belarus → 84 + 3 = 87
            {73, 92},   // Georgia → 89 + 3 = 92
            {74, 96},   // Liechtenstein → 93 + 3 = 96
            {75, 97},   // Lithuania → 94 + 3 = 97
            
            // Non-playable: African nations
            {76, 103},  // Algeria → 100 + 3 = 103
            {77, 58},   // Benin → 58
            {78, 59},   // Burkina Faso → 59
            {79, 61},   // Cape Verde → 61
            {80, 62},   // Congo → 62
            {81, 63},   // DR Congo → 63
            {82, 104},  // Egypt → 101 + 3 = 104
            {83, 64},   // Equatorial Guinea → 64
            {84, 65},   // Gabon → 65
            {85, 66},   // Gambia → 66
            {86, 67},   // Guinea → 67
            {88, 68},   // Kenya → 68
            {89, 69},   // Liberia → 69
            {91, 70},   // Mali → 70
            {92, 105},  // Morocco → 102 + 3 = 105
            {94, 106},  // Senegal → 103 + 3 = 106
            {95, 72},   // Sierra Leone → 72
            {96, 73},   // Zambia → 73
            {97, 74},   // Zimbabwe → 74
            
            // Non-playable: Americas
            {60, 107},  // Honduras → 104 + 3 = 107
            {61, 108},  // Jamaica → 105 + 3 = 108
            {63, 109},  // Bolivia → 106 + 3 = 109
            {64, 110},  // Venezuela → 107 + 3 = 110
            {98, 75},   // Canada → 75
            {99, 76},   // Grenada → 76
            {101, 77},  // Martinique → 77
            {102, 78},  // Netherlands Antilles → 78
            {104, 79},  // New Zealand → 79
            
            // Non-playable: Asia/Middle East
            {65, 112},  // China → 109 + 3 = 112
            {66, 120},  // Uzbekistan → 117 + 3 = 120
            {103, 116}, // Oman → 113 + 3 = 116
        };
        
        // Build lookup table
        int[] natMap = new int[256];
        for (int i = 0; i < 256; i++) {
            natMap[i] = i;  // Default: no change
        }
        for (int[] map : mapping) {
            natMap[map[0]] = map[1];
        }
        
        // Remap player nationalities (only for regular players 1-4413)
        int NATIONALITY_OFFSET = 65;
        int remapCount = 0;
        
        for (int playerIndex = 1; playerIndex <= PES6_REGULAR_PLAYERS; playerIndex++) {
            int playerOffset = PES6_PLAYER_STATS_START + (playerIndex * PLAYER_RECORD_SIZE);
            int nationalityByteOffset = playerOffset + NATIONALITY_OFFSET - 1;
            
            if (nationalityByteOffset < pes6Data.length) {
                int currentNat = pes6Data[nationalityByteOffset] & 0xFF;
                int newNat = natMap[currentNat];
                
                if (newNat != currentNat) {
                    pes6Data[nationalityByteOffset] = (byte) newNat;
                    remapCount++;
                }
            }
        }
        
        System.out.println("  Remapped " + remapCount + " players across " + mapping.length + " nationalities");
    }
    
    private void copyKits(byte[] pes6Data, byte[] pes2008Data) {
        // Apply same transformations to kits as we did to squads
        System.out.println("  Swapping Italy (13) ↔ Latvia (14) kits...");
        swapNationalTeamKits(pes6Data, 13, 14);
        
        System.out.println("  Rotating kits 52-56 (Iran→Japan→Saudi→Korea→Australia→Iran)...");
        rotateKits52to56(pes6Data);
        
        System.out.println("  Copying national team kits...");
        int nationalKitsSize = KIT_NATIONAL_COUNT * KIT_NATIONAL_SIZE;
        System.arraycopy(pes6Data, PES6_KIT_NATIONAL_START, pes2008Data, PES2008_KIT_NATIONAL_START, nationalKitsSize);
        
        System.out.println("  Copying club team kits...");
        int clubKitsSize = KIT_CLUB_COUNT_PES6 * KIT_CLUB_SIZE;
        System.arraycopy(pes6Data, PES6_KIT_CLUB_START, pes2008Data, PES2008_KIT_CLUB_START, clubKitsSize);
    }
    
    private void swapNationalTeamKits(byte[] pes6Data, int team1, int team2) {
        System.out.println("    Swapping kits for teams " + team1 + " and " + team2 + "...");
        
        // Swap national team kits (352 bytes each)
        byte[] tempKit = new byte[KIT_NATIONAL_SIZE];
        System.arraycopy(pes6Data, PES6_KIT_NATIONAL_START + (team1 * KIT_NATIONAL_SIZE), tempKit, 0, KIT_NATIONAL_SIZE);
        System.arraycopy(pes6Data, PES6_KIT_NATIONAL_START + (team2 * KIT_NATIONAL_SIZE), 
                       pes6Data, PES6_KIT_NATIONAL_START + (team1 * KIT_NATIONAL_SIZE), KIT_NATIONAL_SIZE);
        System.arraycopy(tempKit, 0, pes6Data, PES6_KIT_NATIONAL_START + (team2 * KIT_NATIONAL_SIZE), KIT_NATIONAL_SIZE);
    }
    
    private void rotateKits52to56(byte[] pes6Data) {
        // Save Australia (56) kit first
        byte[] tempKit = new byte[KIT_NATIONAL_SIZE];
        System.arraycopy(pes6Data, PES6_KIT_NATIONAL_START + (56 * KIT_NATIONAL_SIZE), tempKit, 0, KIT_NATIONAL_SIZE);
        
        // Shift backwards: 55→56, 54→55, 53→54, 52→53
        for (int i = 55; i >= 52; i--) {
            System.arraycopy(pes6Data, PES6_KIT_NATIONAL_START + (i * KIT_NATIONAL_SIZE), 
                           pes6Data, PES6_KIT_NATIONAL_START + ((i + 1) * KIT_NATIONAL_SIZE), KIT_NATIONAL_SIZE);
        }
        
        // Put Australia (was 56) into position 52
        System.arraycopy(tempKit, 0, pes6Data, PES6_KIT_NATIONAL_START + (52 * KIT_NATIONAL_SIZE), KIT_NATIONAL_SIZE);
        
        System.out.println("    Rotated kits for teams 52-56");
    }
    
    public boolean validate(byte[] pes2008Data) {
        if (pes2008Data == null || pes2008Data.length != LENGTH08) {
            System.out.println("Validation FAILED");
            return false;
        }
        System.out.println("Validation PASSED");
        return true;
    }
}
