/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.AttDefPanel;
import editor.Formations;
import editor.JobList;
import editor.OptionFile;
import editor.PESUtils;
import editor.PNGFilter;
import editor.PitchPanel;
import editor.Player;
import editor.PositionList;
import editor.SquadList;
import editor.SquadNumList;
import editor.StrategyPanel;
import editor.TeamSetPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
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
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FormPanel
extends JPanel
implements ListSelectionListener,
DropTargetListener,
DragSourceListener,
DragGestureListener {
    int team;
    OptionFile of;
    SquadList squadList;
    PositionList posList;
    JobList sFK;
    JobList lFK;
    JobList rCR;
    JobList lCR;
    JobList pk;
    JobList cap;
    private static final byte[] formData;
    private String[] formName = new String[]{"Formation", "4-5-1 A", "4-5-1 B", "4-4-2 A", "4-4-2 B", "4-4-2 C", "4-3-3 A", "4-3-3 B", "4-3-3 C", "3-6-1 A", "3-6-1 B", "3-5-2 A", "3-5-2 B", "3-4-3 A", "3-4-3 B", "3-4-3 C", "5-4-1 A", "5-4-1 B", "5-4-1 C", "5-3-2 A", "5-3-2 B", "5-3-2 C"};
    private JComboBox formBox;
    boolean ok = false;
    PitchPanel pitchPanel;
    static boolean fromPitch;
    private JButton snapButton;
    AttDefPanel adPanel;
    JComboBox roleBox;
    Role[] role;
    JComboBox altBox;
    SquadNumList numList;
    private JFileChooser chooserPNG = new JFileChooser();
    private PNGFilter pngFilter = new PNGFilter();
    int def = 0;
    int mid = 0;
    int att = 0;
    private TeamSetPanel teamSetPan;
    private StrategyPanel stratPan;
    private DataFlavor localPlayerFlavor;
    private int sourceIndex = -1;

    static {
        byte[] byArray = new byte[651];
        byArray[0] = 9;
        byArray[1] = 63;
        byArray[2] = 9;
        byArray[3] = 41;
        byArray[4] = 11;
        byArray[5] = 85;
        byArray[6] = 11;
        byArray[7] = 19;
        byArray[8] = 18;
        byArray[9] = 52;
        byArray[10] = 26;
        byArray[11] = 75;
        byArray[12] = 26;
        byArray[13] = 29;
        byArray[14] = 34;
        byArray[15] = 64;
        byArray[16] = 34;
        byArray[17] = 40;
        byArray[18] = 43;
        byArray[19] = 52;
        byArray[21] = 7;
        byArray[22] = 1;
        byArray[23] = 9;
        byArray[24] = 8;
        byArray[25] = 12;
        byArray[26] = 23;
        byArray[27] = 22;
        byArray[28] = 28;
        byArray[29] = 24;
        byArray[30] = 38;
        byArray[31] = 9;
        byArray[32] = 63;
        byArray[33] = 9;
        byArray[34] = 41;
        byArray[35] = 11;
        byArray[36] = 85;
        byArray[37] = 11;
        byArray[38] = 19;
        byArray[39] = 18;
        byArray[40] = 61;
        byArray[41] = 18;
        byArray[42] = 43;
        byArray[43] = 26;
        byArray[44] = 75;
        byArray[45] = 26;
        byArray[46] = 29;
        byArray[47] = 34;
        byArray[48] = 52;
        byArray[49] = 43;
        byArray[50] = 52;
        byArray[52] = 7;
        byArray[53] = 1;
        byArray[54] = 9;
        byArray[55] = 8;
        byArray[56] = 14;
        byArray[57] = 10;
        byArray[58] = 23;
        byArray[59] = 22;
        byArray[60] = 26;
        byArray[61] = 38;
        byArray[62] = 9;
        byArray[63] = 63;
        byArray[64] = 9;
        byArray[65] = 41;
        byArray[66] = 11;
        byArray[67] = 85;
        byArray[68] = 11;
        byArray[69] = 19;
        byArray[70] = 18;
        byArray[71] = 52;
        byArray[72] = 26;
        byArray[73] = 75;
        byArray[74] = 26;
        byArray[75] = 29;
        byArray[76] = 34;
        byArray[77] = 52;
        byArray[78] = 43;
        byArray[79] = 66;
        byArray[80] = 43;
        byArray[81] = 38;
        byArray[83] = 7;
        byArray[84] = 1;
        byArray[85] = 9;
        byArray[86] = 8;
        byArray[87] = 12;
        byArray[88] = 23;
        byArray[89] = 22;
        byArray[90] = 26;
        byArray[91] = 40;
        byArray[92] = 36;
        byArray[93] = 9;
        byArray[94] = 63;
        byArray[95] = 9;
        byArray[96] = 41;
        byArray[97] = 11;
        byArray[98] = 85;
        byArray[99] = 11;
        byArray[100] = 19;
        byArray[101] = 18;
        byArray[102] = 61;
        byArray[103] = 18;
        byArray[104] = 43;
        byArray[105] = 30;
        byArray[106] = 70;
        byArray[107] = 30;
        byArray[108] = 34;
        byArray[109] = 43;
        byArray[110] = 66;
        byArray[111] = 43;
        byArray[112] = 38;
        byArray[114] = 7;
        byArray[115] = 1;
        byArray[116] = 9;
        byArray[117] = 8;
        byArray[118] = 14;
        byArray[119] = 10;
        byArray[120] = 28;
        byArray[121] = 24;
        byArray[122] = 40;
        byArray[123] = 36;
        byArray[124] = 9;
        byArray[125] = 63;
        byArray[126] = 9;
        byArray[127] = 41;
        byArray[128] = 11;
        byArray[129] = 85;
        byArray[130] = 11;
        byArray[131] = 19;
        byArray[132] = 26;
        byArray[133] = 77;
        byArray[134] = 26;
        byArray[135] = 61;
        byArray[136] = 26;
        byArray[137] = 43;
        byArray[138] = 26;
        byArray[139] = 27;
        byArray[140] = 43;
        byArray[141] = 66;
        byArray[142] = 43;
        byArray[143] = 38;
        byArray[145] = 7;
        byArray[146] = 1;
        byArray[147] = 9;
        byArray[148] = 8;
        byArray[149] = 20;
        byArray[150] = 21;
        byArray[151] = 17;
        byArray[152] = 18;
        byArray[153] = 40;
        byArray[154] = 36;
        byArray[155] = 9;
        byArray[156] = 63;
        byArray[157] = 9;
        byArray[158] = 41;
        byArray[159] = 11;
        byArray[160] = 85;
        byArray[161] = 11;
        byArray[162] = 19;
        byArray[163] = 18;
        byArray[164] = 52;
        byArray[165] = 30;
        byArray[166] = 64;
        byArray[167] = 30;
        byArray[168] = 40;
        byArray[169] = 43;
        byArray[170] = 72;
        byArray[171] = 43;
        byArray[172] = 32;
        byArray[173] = 43;
        byArray[174] = 52;
        byArray[176] = 7;
        byArray[177] = 1;
        byArray[178] = 9;
        byArray[179] = 8;
        byArray[180] = 12;
        byArray[181] = 28;
        byArray[182] = 24;
        byArray[183] = 30;
        byArray[184] = 29;
        byArray[185] = 38;
        byArray[186] = 9;
        byArray[187] = 63;
        byArray[188] = 9;
        byArray[189] = 41;
        byArray[190] = 11;
        byArray[191] = 85;
        byArray[192] = 11;
        byArray[193] = 19;
        byArray[194] = 18;
        byArray[195] = 61;
        byArray[196] = 18;
        byArray[197] = 43;
        byArray[198] = 30;
        byArray[199] = 52;
        byArray[200] = 43;
        byArray[201] = 72;
        byArray[202] = 43;
        byArray[203] = 32;
        byArray[204] = 43;
        byArray[205] = 52;
        byArray[207] = 7;
        byArray[208] = 1;
        byArray[209] = 9;
        byArray[210] = 8;
        byArray[211] = 14;
        byArray[212] = 10;
        byArray[213] = 26;
        byArray[214] = 30;
        byArray[215] = 29;
        byArray[216] = 38;
        byArray[217] = 9;
        byArray[218] = 63;
        byArray[219] = 9;
        byArray[220] = 41;
        byArray[221] = 11;
        byArray[222] = 85;
        byArray[223] = 11;
        byArray[224] = 19;
        byArray[225] = 26;
        byArray[226] = 77;
        byArray[227] = 26;
        byArray[228] = 52;
        byArray[229] = 26;
        byArray[230] = 27;
        byArray[231] = 43;
        byArray[232] = 72;
        byArray[233] = 43;
        byArray[234] = 32;
        byArray[235] = 43;
        byArray[236] = 52;
        byArray[238] = 7;
        byArray[239] = 1;
        byArray[240] = 9;
        byArray[241] = 8;
        byArray[242] = 21;
        byArray[243] = 19;
        byArray[244] = 17;
        byArray[245] = 30;
        byArray[246] = 29;
        byArray[247] = 38;
        byArray[248] = 9;
        byArray[249] = 72;
        byArray[250] = 9;
        byArray[251] = 52;
        byArray[252] = 9;
        byArray[253] = 32;
        byArray[254] = 18;
        byArray[255] = 52;
        byArray[256] = 26;
        byArray[257] = 52;
        byArray[258] = 26;
        byArray[259] = 77;
        byArray[260] = 26;
        byArray[261] = 27;
        byArray[262] = 34;
        byArray[263] = 64;
        byArray[264] = 34;
        byArray[265] = 40;
        byArray[266] = 43;
        byArray[267] = 52;
        byArray[269] = 7;
        byArray[270] = 3;
        byArray[271] = 1;
        byArray[272] = 12;
        byArray[273] = 19;
        byArray[274] = 23;
        byArray[275] = 22;
        byArray[276] = 28;
        byArray[277] = 24;
        byArray[278] = 38;
        byArray[279] = 9;
        byArray[280] = 72;
        byArray[281] = 9;
        byArray[282] = 52;
        byArray[283] = 9;
        byArray[284] = 32;
        byArray[285] = 18;
        byArray[286] = 61;
        byArray[287] = 18;
        byArray[288] = 43;
        byArray[289] = 26;
        byArray[290] = 77;
        byArray[291] = 26;
        byArray[292] = 27;
        byArray[293] = 34;
        byArray[294] = 64;
        byArray[295] = 34;
        byArray[296] = 40;
        byArray[297] = 43;
        byArray[298] = 52;
        byArray[300] = 7;
        byArray[301] = 3;
        byArray[302] = 1;
        byArray[303] = 14;
        byArray[304] = 10;
        byArray[305] = 23;
        byArray[306] = 22;
        byArray[307] = 28;
        byArray[308] = 24;
        byArray[309] = 38;
        byArray[310] = 9;
        byArray[311] = 72;
        byArray[312] = 9;
        byArray[313] = 52;
        byArray[314] = 9;
        byArray[315] = 32;
        byArray[316] = 18;
        byArray[317] = 52;
        byArray[318] = 26;
        byArray[319] = 77;
        byArray[320] = 26;
        byArray[321] = 27;
        byArray[322] = 34;
        byArray[323] = 64;
        byArray[324] = 34;
        byArray[325] = 40;
        byArray[326] = 43;
        byArray[327] = 66;
        byArray[328] = 43;
        byArray[329] = 38;
        byArray[331] = 7;
        byArray[332] = 3;
        byArray[333] = 1;
        byArray[334] = 12;
        byArray[335] = 23;
        byArray[336] = 22;
        byArray[337] = 28;
        byArray[338] = 24;
        byArray[339] = 40;
        byArray[340] = 36;
        byArray[341] = 9;
        byArray[342] = 72;
        byArray[343] = 9;
        byArray[344] = 52;
        byArray[345] = 9;
        byArray[346] = 32;
        byArray[347] = 18;
        byArray[348] = 61;
        byArray[349] = 18;
        byArray[350] = 43;
        byArray[351] = 26;
        byArray[352] = 77;
        byArray[353] = 26;
        byArray[354] = 27;
        byArray[355] = 34;
        byArray[356] = 52;
        byArray[357] = 43;
        byArray[358] = 66;
        byArray[359] = 43;
        byArray[360] = 38;
        byArray[362] = 7;
        byArray[363] = 3;
        byArray[364] = 1;
        byArray[365] = 14;
        byArray[366] = 10;
        byArray[367] = 23;
        byArray[368] = 22;
        byArray[369] = 26;
        byArray[370] = 40;
        byArray[371] = 36;
        byArray[372] = 9;
        byArray[373] = 72;
        byArray[374] = 9;
        byArray[375] = 52;
        byArray[376] = 9;
        byArray[377] = 32;
        byArray[378] = 18;
        byArray[379] = 52;
        byArray[380] = 26;
        byArray[381] = 77;
        byArray[382] = 26;
        byArray[383] = 27;
        byArray[384] = 34;
        byArray[385] = 52;
        byArray[386] = 43;
        byArray[387] = 72;
        byArray[388] = 43;
        byArray[389] = 32;
        byArray[390] = 43;
        byArray[391] = 52;
        byArray[393] = 7;
        byArray[394] = 3;
        byArray[395] = 1;
        byArray[396] = 12;
        byArray[397] = 23;
        byArray[398] = 22;
        byArray[399] = 26;
        byArray[400] = 30;
        byArray[401] = 29;
        byArray[402] = 38;
        byArray[403] = 9;
        byArray[404] = 72;
        byArray[405] = 9;
        byArray[406] = 52;
        byArray[407] = 9;
        byArray[408] = 32;
        byArray[409] = 18;
        byArray[410] = 61;
        byArray[411] = 18;
        byArray[412] = 43;
        byArray[413] = 30;
        byArray[414] = 70;
        byArray[415] = 30;
        byArray[416] = 34;
        byArray[417] = 43;
        byArray[418] = 72;
        byArray[419] = 43;
        byArray[420] = 32;
        byArray[421] = 43;
        byArray[422] = 52;
        byArray[424] = 7;
        byArray[425] = 3;
        byArray[426] = 1;
        byArray[427] = 14;
        byArray[428] = 10;
        byArray[429] = 28;
        byArray[430] = 24;
        byArray[431] = 30;
        byArray[432] = 29;
        byArray[433] = 38;
        byArray[434] = 9;
        byArray[435] = 72;
        byArray[436] = 9;
        byArray[437] = 52;
        byArray[438] = 9;
        byArray[439] = 32;
        byArray[440] = 26;
        byArray[441] = 77;
        byArray[442] = 26;
        byArray[443] = 61;
        byArray[444] = 26;
        byArray[445] = 43;
        byArray[446] = 26;
        byArray[447] = 27;
        byArray[448] = 43;
        byArray[449] = 72;
        byArray[450] = 43;
        byArray[451] = 32;
        byArray[452] = 43;
        byArray[453] = 52;
        byArray[455] = 7;
        byArray[456] = 3;
        byArray[457] = 1;
        byArray[458] = 20;
        byArray[459] = 21;
        byArray[460] = 17;
        byArray[461] = 18;
        byArray[462] = 30;
        byArray[463] = 29;
        byArray[464] = 38;
        byArray[465] = 9;
        byArray[466] = 72;
        byArray[467] = 9;
        byArray[468] = 52;
        byArray[469] = 9;
        byArray[470] = 32;
        byArray[471] = 12;
        byArray[472] = 87;
        byArray[473] = 12;
        byArray[474] = 17;
        byArray[475] = 18;
        byArray[476] = 52;
        byArray[477] = 26;
        byArray[478] = 75;
        byArray[479] = 26;
        byArray[480] = 29;
        byArray[481] = 34;
        byArray[482] = 52;
        byArray[483] = 43;
        byArray[484] = 52;
        byArray[486] = 7;
        byArray[487] = 3;
        byArray[488] = 1;
        byArray[489] = 9;
        byArray[490] = 8;
        byArray[491] = 12;
        byArray[492] = 23;
        byArray[493] = 22;
        byArray[494] = 26;
        byArray[495] = 38;
        byArray[496] = 9;
        byArray[497] = 72;
        byArray[498] = 9;
        byArray[499] = 52;
        byArray[500] = 9;
        byArray[501] = 32;
        byArray[502] = 12;
        byArray[503] = 87;
        byArray[504] = 12;
        byArray[505] = 17;
        byArray[506] = 18;
        byArray[507] = 61;
        byArray[508] = 18;
        byArray[509] = 43;
        byArray[510] = 30;
        byArray[511] = 70;
        byArray[512] = 30;
        byArray[513] = 34;
        byArray[514] = 43;
        byArray[515] = 52;
        byArray[517] = 7;
        byArray[518] = 3;
        byArray[519] = 1;
        byArray[520] = 9;
        byArray[521] = 8;
        byArray[522] = 14;
        byArray[523] = 10;
        byArray[524] = 28;
        byArray[525] = 24;
        byArray[526] = 38;
        byArray[527] = 9;
        byArray[528] = 72;
        byArray[529] = 9;
        byArray[530] = 52;
        byArray[531] = 9;
        byArray[532] = 32;
        byArray[533] = 12;
        byArray[534] = 87;
        byArray[535] = 12;
        byArray[536] = 17;
        byArray[537] = 26;
        byArray[538] = 77;
        byArray[539] = 26;
        byArray[540] = 61;
        byArray[541] = 26;
        byArray[542] = 43;
        byArray[543] = 26;
        byArray[544] = 27;
        byArray[545] = 43;
        byArray[546] = 52;
        byArray[548] = 7;
        byArray[549] = 3;
        byArray[550] = 1;
        byArray[551] = 9;
        byArray[552] = 8;
        byArray[553] = 20;
        byArray[554] = 21;
        byArray[555] = 17;
        byArray[556] = 18;
        byArray[557] = 38;
        byArray[558] = 9;
        byArray[559] = 72;
        byArray[560] = 9;
        byArray[561] = 52;
        byArray[562] = 9;
        byArray[563] = 32;
        byArray[564] = 12;
        byArray[565] = 87;
        byArray[566] = 12;
        byArray[567] = 17;
        byArray[568] = 18;
        byArray[569] = 52;
        byArray[570] = 34;
        byArray[571] = 64;
        byArray[572] = 34;
        byArray[573] = 40;
        byArray[574] = 43;
        byArray[575] = 66;
        byArray[576] = 43;
        byArray[577] = 38;
        byArray[579] = 7;
        byArray[580] = 3;
        byArray[581] = 1;
        byArray[582] = 9;
        byArray[583] = 8;
        byArray[584] = 12;
        byArray[585] = 28;
        byArray[586] = 24;
        byArray[587] = 40;
        byArray[588] = 36;
        byArray[589] = 9;
        byArray[590] = 72;
        byArray[591] = 9;
        byArray[592] = 52;
        byArray[593] = 9;
        byArray[594] = 32;
        byArray[595] = 12;
        byArray[596] = 87;
        byArray[597] = 12;
        byArray[598] = 17;
        byArray[599] = 18;
        byArray[600] = 61;
        byArray[601] = 18;
        byArray[602] = 43;
        byArray[603] = 34;
        byArray[604] = 52;
        byArray[605] = 43;
        byArray[606] = 66;
        byArray[607] = 43;
        byArray[608] = 38;
        byArray[610] = 7;
        byArray[611] = 3;
        byArray[612] = 1;
        byArray[613] = 9;
        byArray[614] = 8;
        byArray[615] = 14;
        byArray[616] = 10;
        byArray[617] = 26;
        byArray[618] = 40;
        byArray[619] = 36;
        byArray[620] = 9;
        byArray[621] = 72;
        byArray[622] = 9;
        byArray[623] = 52;
        byArray[624] = 9;
        byArray[625] = 32;
        byArray[626] = 12;
        byArray[627] = 87;
        byArray[628] = 12;
        byArray[629] = 17;
        byArray[630] = 26;
        byArray[631] = 77;
        byArray[632] = 26;
        byArray[633] = 52;
        byArray[634] = 26;
        byArray[635] = 27;
        byArray[636] = 43;
        byArray[637] = 66;
        byArray[638] = 43;
        byArray[639] = 38;
        byArray[641] = 7;
        byArray[642] = 3;
        byArray[643] = 1;
        byArray[644] = 9;
        byArray[645] = 8;
        byArray[646] = 21;
        byArray[647] = 19;
        byArray[648] = 17;
        byArray[649] = 40;
        byArray[650] = 36;
        formData = byArray;
        fromPitch = false;
    }

    public FormPanel(OptionFile optionFile) {
        this.of = optionFile;
        String string = "application/x-java-jvm-local-objectref;class=editor.Player";
        try {
            this.localPlayerFlavor = new DataFlavor(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            System.out.println("FormTransferHandler: unable to create data flavor");
        }
        this.chooserPNG.addChoosableFileFilter(this.pngFilter);
        this.chooserPNG.setAcceptAllFileFilterUsed(false);
        this.chooserPNG.setDialogTitle("Save Snapshot");
        JPanel jPanel = new JPanel(new GridLayout(0, 6));
        JPanel jPanel2 = new JPanel(new BorderLayout());
        JPanel jPanel3 = new JPanel(new BorderLayout());
        JPanel jPanel4 = new JPanel(new BorderLayout());
        this.numList = new SquadNumList(this.of);
        String[] stringArray = new String[]{"Normal", "Strategy Plan A", "Strategy Plan B"};
        this.altBox = new JComboBox<String>(stringArray);
        this.altBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getActionCommand() == "y") {
                    FormPanel.this.posList.alt = FormPanel.this.altBox.getSelectedIndex();
                    FormPanel.this.posList.refresh(FormPanel.this.team);
                    FormPanel.this.updateRoleBox();
                    ((FormPanel)FormPanel.this).teamSetPan.alt = FormPanel.this.altBox.getSelectedIndex();
                    FormPanel.this.teamSetPan.refresh(FormPanel.this.team);
                    FormPanel.this.pitchPanel.repaint();
                    FormPanel.this.adPanel.repaint();
                }
            }
        });
        this.roleBox = new JComboBox();
        this.roleBox.setPreferredSize(new Dimension(56, 25));
        this.roleBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getActionCommand() == "y") {
                    int n = FormPanel.this.squadList.getSelectedIndex();
                    Role role = (Role)FormPanel.this.roleBox.getSelectedItem();
                    if (n >= 0 && n < 11 && role.index != -1) {
                        byte by = Formations.getPos(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n);
                        if (by != role.index) {
                            if (by < 10 && role.index > 9) {
                                if (role.index < 29) {
                                    Formations.setX(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n, 25);
                                } else {
                                    Formations.setX(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n, 41);
                                }
                            }
                            if (by > 9 && by < 29) {
                                if (role.index < 10) {
                                    Formations.setX(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n, 8);
                                } else if (role.index > 28) {
                                    Formations.setX(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n, 41);
                                }
                            }
                            if (by > 28 && role.index < 29) {
                                if (role.index < 10) {
                                    Formations.setX(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n, 8);
                                } else {
                                    Formations.setX(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n, 25);
                                }
                            }
                            if ((role.index == 8 || role.index == 15 || role.index == 22 || role.index == 29) && by != 8 && by != 15 && by != 22 && by != 29 && Formations.getY(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n) > 50) {
                                Formations.setY(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n, 28);
                            }
                            if ((role.index == 9 || role.index == 16 || role.index == 23 || role.index == 30) && by != 9 && by != 16 && by != 23 && by != 30 && Formations.getY(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n) < 54) {
                                Formations.setY(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n, 76);
                            }
                        }
                        Formations.setPos(FormPanel.this.of, FormPanel.this.team, FormPanel.this.altBox.getSelectedIndex(), n, role.index);
                        if (by > 0 && by < 8 && (role.index < 1 || role.index > 7) && FormPanel.this.altBox.getSelectedIndex() == 0 && n == Formations.getStratOlCB(FormPanel.this.of, FormPanel.this.team)) {
                            Formations.setStratOlCB(FormPanel.this.of, FormPanel.this.team, 0);
                        }
                        FormPanel.this.updateRoleBox();
                        FormPanel.this.posList.refresh(FormPanel.this.team);
                        FormPanel.this.teamSetPan.refresh(FormPanel.this.team);
                        FormPanel.this.stratPan.refresh(FormPanel.this.team);
                        FormPanel.this.pitchPanel.repaint();
                        FormPanel.this.adPanel.repaint();
                    }
                }
            }
        });
        this.squadList = new SquadList(this.of, true);
        this.squadList.addListSelectionListener(this);
        new DropTarget(this.squadList, this);
        DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this.squadList, 2, this);
        this.posList = new PositionList(this.of, false);
        this.teamSetPan = new TeamSetPanel(this.of);
        this.stratPan = new StrategyPanel(this.of, this.squadList, this.posList);
        this.adPanel = new AttDefPanel(this.of, this.altBox);
        this.adPanel.pitch = this.pitchPanel = new PitchPanel(this.of, this.squadList, this.adPanel, this.altBox, this.numList);
        this.lFK = new JobList(this.of, 0, " F-L ", Color.yellow);
        this.lFK.setToolTipText("Long free kick");
        this.sFK = new JobList(this.of, 1, " F-S ", Color.yellow);
        this.sFK.setToolTipText("Short free kick");
        this.lCR = new JobList(this.of, 2, " C-L ", Color.cyan);
        this.lCR.setToolTipText("Left corner");
        this.rCR = new JobList(this.of, 3, " C-R", Color.cyan);
        this.rCR.setToolTipText("Right corner");
        this.pk = new JobList(this.of, 4, " PK ", Color.green);
        this.pk.setToolTipText("Penalty");
        this.cap = new JobList(this.of, 5, " C ", Color.red);
        this.cap.setToolTipText("Captain");
        this.formBox = new JComboBox();
        this.formBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n = FormPanel.this.formBox.getSelectedIndex();
                if (n != -1 && actionEvent.getActionCommand() == "y") {
                    int n2 = 696756 + 364 * FormPanel.this.team + FormPanel.this.altBox.getSelectedIndex() * 82;
                    if (n != 0) {
                        System.arraycopy(formData, (n - 1) * 31, FormPanel.this.of.data, n2, 31);
                    }
                    byte by = Formations.getPos(FormPanel.this.of, FormPanel.this.team, 0, Formations.getStratOlCB(FormPanel.this.of, FormPanel.this.team));
                    if (FormPanel.this.altBox.getSelectedIndex() == 0 && (by < 1 || by > 7)) {
                        Formations.setStratOlCB(FormPanel.this.of, FormPanel.this.team, 0);
                    }
                    FormPanel.this.posList.refresh(FormPanel.this.team);
                    FormPanel.this.stratPan.refresh(FormPanel.this.team);
                    FormPanel.this.teamSetPan.refresh(FormPanel.this.team);
                    FormPanel.this.pitchPanel.repaint();
                    FormPanel.this.adPanel.repaint();
                    FormPanel.this.updateRoleBox();
                }
            }
        });
        this.snapButton = new JButton("Snapshot");
        this.snapButton.setToolTipText("Save the formation diagram to a .png image file");
        this.snapButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FormPanel.this.savePNG();
            }
        });
        jPanel.add(this.lFK);
        jPanel.add(this.sFK);
        jPanel.add(this.lCR);
        jPanel.add(this.rCR);
        jPanel.add(this.pk);
        jPanel.add(this.cap);
        JPanel jPanel5 = new JPanel(new BorderLayout());
        jPanel5.add((Component)this.numList, "West");
        jPanel5.add((Component)this.posList, "East");
        jPanel2.add((Component)jPanel5, "West");
        jPanel2.add((Component)this.squadList, "Center");
        jPanel2.add((Component)jPanel, "East");
        jPanel3.add((Component)jPanel2, "Center");
        jPanel3.add((Component)this.formBox, "North");
        JPanel jPanel6 = new JPanel();
        jPanel6.add(this.adPanel);
        jPanel6.add(this.roleBox);
        JPanel jPanel7 = new JPanel(new GridLayout(1, 3));
        jPanel7.add(this.altBox);
        jPanel7.add(this.formBox);
        jPanel7.add(this.snapButton);
        JPanel jPanel8 = new JPanel(new BorderLayout());
        jPanel8.add((Component)this.teamSetPan, "North");
        jPanel8.add((Component)this.pitchPanel, "Center");
        jPanel8.add((Component)jPanel6, "South");
        jPanel4.add((Component)jPanel7, "North");
        jPanel4.add((Component)jPanel8, "Center");
        jPanel4.add((Component)this.stratPan, "South");
        this.add(jPanel3);
        this.add(jPanel4);
    }

    public void refresh(int n) {
        this.team = n;
        this.altBox.setActionCommand("n");
        this.altBox.setSelectedIndex(0);
        this.altBox.setActionCommand("y");
        this.ok = false;
        this.squadList.refresh(n, false);
        this.ok = true;
        int n2 = n;
        if (n > 63) {
            n2 = n + 9;
        }
        this.numList.refresh(n2);
        this.posList.alt = this.altBox.getSelectedIndex();
        this.posList.refresh(n);
        this.updateRoleBox();
        this.sFK.refresh(n);
        this.lFK.refresh(n);
        this.rCR.refresh(n);
        this.lCR.refresh(n);
        this.pk.refresh(n);
        this.cap.refresh(n);
        this.teamSetPan.alt = this.altBox.getSelectedIndex();
        this.teamSetPan.refresh(n);
        this.stratPan.refresh(n);
        this.pitchPanel.selected = -1;
        this.pitchPanel.squad = n;
        this.pitchPanel.repaint();
        this.adPanel.selected = -1;
        this.adPanel.squad = n;
        this.adPanel.repaint();
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (fromPitch) {
            fromPitch = false;
            this.updateRoleBox();
        } else if (!listSelectionEvent.getValueIsAdjusting() && this.ok) {
            int n = this.squadList.getSelectedIndex();
            this.updateRoleBox();
            if (n >= 0 && n < 11) {
                this.pitchPanel.selected = n;
                this.adPanel.selected = n;
            } else {
                this.pitchPanel.selected = -1;
                this.adPanel.selected = -1;
            }
            this.pitchPanel.repaint();
            this.adPanel.repaint();
        }
    }

    public boolean saveComponentAsPNG(Component component, File file) {
        boolean bl = false;
        Dimension dimension = component.getSize();
        byte[] byArray = new byte[8];
        byte[] byArray2 = new byte[8];
        byte[] byArray3 = new byte[8];
        int n = 0;
        while (n < 8) {
            byArray[n] = (byte)this.pitchPanel.colour[n].getRed();
            byArray2[n] = (byte)this.pitchPanel.colour[n].getGreen();
            byArray3[n] = (byte)this.pitchPanel.colour[n].getBlue();
            ++n;
        }
        IndexColorModel indexColorModel = new IndexColorModel(8, 8, byArray, byArray2, byArray3);
        BufferedImage bufferedImage = new BufferedImage(dimension.width, dimension.height, 13, indexColorModel);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        component.paint(graphics2D);
        try {
            ImageIO.write((RenderedImage)bufferedImage, "png", file);
            bl = true;
        }
        catch (Exception exception) {
            // empty catch block
        }
        return bl;
    }

    private void updateRoleBox() {
        this.countForm();
        this.roleBox.setActionCommand("n");
        this.roleBox.removeAllItems();
        int n = this.squadList.getSelectedIndex();
        byte by = Formations.getPos(this.of, this.team, this.altBox.getSelectedIndex(), n);
        this.roleBox.setEnabled(true);
        if (n > 0 && n < 11) {
            int n2 = 0;
            boolean bl = false;
            Role role = null;
            Role role2 = new Role(by);
            this.roleBox.addItem(role2);
            byte by2 = 1;
            while (by2 < 41) {
                byte by3;
                int n3;
                boolean bl2 = true;
                if (by2 == 5) {
                    bl2 = false;
                } else {
                    if (by2 == 15) {
                        n3 = 0;
                        while (bl2 && n3 < 11) {
                            by3 = Formations.getPos(this.of, this.team, this.altBox.getSelectedIndex(), n3);
                            if (by3 != by && (by3 == 8 || by3 == 22)) {
                                bl2 = false;
                            }
                            ++n3;
                        }
                    }
                    if (by2 == 16) {
                        n3 = 0;
                        while (bl2 && n3 < 11) {
                            by3 = Formations.getPos(this.of, this.team, this.altBox.getSelectedIndex(), n3);
                            if (by3 != by && (by3 == 9 || by3 == 23)) {
                                bl2 = false;
                            }
                            ++n3;
                        }
                    }
                    if (by != 15 && (by2 == 8 || by2 == 22)) {
                        n3 = 0;
                        while (bl2 && n3 < 11) {
                            by3 = Formations.getPos(this.of, this.team, this.altBox.getSelectedIndex(), n3);
                            if (by3 == 15) {
                                bl2 = false;
                            }
                            ++n3;
                        }
                    }
                    if (by != 16 && (by2 == 9 || by2 == 23)) {
                        n3 = 0;
                        while (bl2 && n3 < 11) {
                            by3 = Formations.getPos(this.of, this.team, this.altBox.getSelectedIndex(), n3);
                            if (by3 == 16) {
                                bl2 = false;
                            }
                            ++n3;
                        }
                    }
                    if (this.isDef(by)) {
                        if (this.def <= 2 && !this.isDef(by2)) {
                            bl2 = false;
                        }
                        if (this.mid >= 6 && this.isMid(by2)) {
                            bl2 = false;
                        }
                        if (this.att >= 5 && this.isAtt(by2)) {
                            bl2 = false;
                        }
                    }
                    if (this.isMid(by)) {
                        if (this.mid <= 2 && !this.isMid(by2)) {
                            bl2 = false;
                        }
                        if (this.def >= 5 && this.isDef(by2)) {
                            bl2 = false;
                        }
                        if (this.att >= 5 && this.isAtt(by2)) {
                            bl2 = false;
                        }
                    }
                    if (this.isAtt(by)) {
                        if (this.att <= 1 && !this.isAtt(by2)) {
                            bl2 = false;
                        }
                        if (this.mid >= 6 && this.isMid(by2)) {
                            bl2 = false;
                        }
                        if (this.def >= 5 && this.isDef(by2)) {
                            bl2 = false;
                        }
                    }
                }
                n3 = 0;
                while (bl2 && n3 < 11) {
                    by3 = Formations.getPos(this.of, this.team, this.altBox.getSelectedIndex(), n3);
                    if (by3 == by2) {
                        bl2 = false;
                    }
                    ++n3;
                }
                if (bl2) {
                    Role role3 = new Role(by2);
                    if (!role2.name.equals(role3.name)) {
                        if (role == null) {
                            role = role3;
                            this.roleBox.addItem(role3);
                            ++n2;
                        } else if (!role.name.equals(role3.name) && (!role3.name.equals("CBT") || role3.name.equals("CBT") && !bl)) {
                            role = new Role(by2);
                            this.roleBox.addItem(role);
                            ++n2;
                        }
                        if (role3.name.equals("CBT")) {
                            bl = true;
                        }
                    }
                }
                by2 = (byte)(by2 + 1);
            }
        } else if (n == 0) {
            this.roleBox.addItem(new Role(0));
        } else {
            this.roleBox.setEnabled(false);
        }
        this.roleBox.setActionCommand("y");
    }

    private void countForm() {
        this.def = 0;
        this.mid = 0;
        this.att = 0;
        int n = 1;
        while (n < 11) {
            byte by = Formations.getPos(this.of, this.team, this.altBox.getSelectedIndex(), n);
            if (this.isDef(by)) {
                ++this.def;
            } else if (this.isMid(by)) {
                ++this.mid;
            } else if (this.isAtt(by)) {
                ++this.att;
            }
            ++n;
        }
        this.formBox.setActionCommand("n");
        this.formName[0] = String.valueOf(String.valueOf(String.valueOf(this.def))) + "-" + String.valueOf(this.mid) + "-" + String.valueOf(this.att) + " *";
        DefaultComboBoxModel<String> defaultComboBoxModel = new DefaultComboBoxModel<String>(this.formName);
        this.formBox.setModel(defaultComboBoxModel);
        this.formBox.setActionCommand("y");
    }

    private void savePNG() {
        boolean bl = false;
        int n = this.chooserPNG.showSaveDialog(null);
        if (n == 0) {
            File file = this.chooserPNG.getSelectedFile();
            if (PESUtils.getExtension(file) == null || !PESUtils.getExtension(file).equals("png")) {
                file = new File(String.valueOf(String.valueOf(file.getParent())) + File.separator + file.getName() + ".png");
            }
            if (file.exists()) {
                int n2 = JOptionPane.showConfirmDialog(null, String.valueOf(String.valueOf(file.getName())) + "\nAlready exists in:\n" + file.getParent() + "\nAre you sure you want to overwrite this file?", "Overwrite:  " + file.getName(), 0, 2, null);
                if (n2 == 0) {
                    boolean bl2 = file.delete();
                    if (!bl2) {
                        JOptionPane.showMessageDialog(null, "Could not access file", "Error", 0);
                        return;
                    }
                } else {
                    return;
                }
            }
            if (this.saveComponentAsPNG(this.pitchPanel, file)) {
                JOptionPane.showMessageDialog(null, String.valueOf(String.valueOf(file.getName())) + "\nSaved in:\n" + file.getParent(), "File Successfully Saved", 1);
            } else {
                bl = true;
            }
            if (bl) {
                JOptionPane.showMessageDialog(null, "Could not access file", "Error", 0);
            }
        }
    }

    private boolean isDef(int n) {
        boolean bl = false;
        if (n > 0 && n < 10) {
            bl = true;
        }
        return bl;
    }

    private boolean isMid(int n) {
        boolean bl = false;
        if (n > 9 && n < 29) {
            bl = true;
        }
        return bl;
    }

    private boolean isAtt(int n) {
        boolean bl = false;
        if (n > 28 && n < 41) {
            bl = true;
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
        int n = this.squadList.locationToIndex(dropTargetDragEvent.getLocation());
        Player player = (Player)this.squadList.getModel().getElementAt(n);
        this.squadList.setSelectedIndex(n);
        if (n != -1 && n != this.sourceIndex && player.index != 0) {
            dropTargetDragEvent.acceptDrag(2);
        } else {
            dropTargetDragEvent.rejectDrag();
        }
    }

    @Override
    public void drop(DropTargetDropEvent dropTargetDropEvent) {
        Transferable transferable = dropTargetDropEvent.getTransferable();
        int n = this.squadList.getSelectedIndex();
        if (transferable.isDataFlavorSupported(this.localPlayerFlavor)) {
            int n2;
            dropTargetDropEvent.acceptDrop(2);
            byte by = Formations.getSlot(this.of, this.team, this.sourceIndex);
            Formations.setSlot(this.of, this.team, this.sourceIndex, Formations.getSlot(this.of, this.team, n));
            Formations.setSlot(this.of, this.team, n, by);
            if (this.sourceIndex < 11 && n < 11) {
                n2 = 0;
                while (n2 < 6) {
                    if (Formations.getJob(this.of, this.team, n2) == this.sourceIndex) {
                        Formations.setJob(this.of, this.team, n2, this.of.toByte(n));
                    } else if (Formations.getJob(this.of, this.team, n2) == n) {
                        Formations.setJob(this.of, this.team, n2, this.of.toByte(this.sourceIndex));
                    }
                    ++n2;
                }
                this.sFK.refresh(this.team);
                this.lFK.refresh(this.team);
                this.rCR.refresh(this.team);
                this.lCR.refresh(this.team);
                this.pk.refresh(this.team);
                this.cap.refresh(this.team);
            }
            this.ok = false;
            n2 = this.team;
            if (this.team > 63) {
                n2 = this.team + 9;
            }
            this.numList.refresh(n2);
            this.squadList.refresh(this.team, false);
            this.teamSetPan.refresh(this.team);
            this.stratPan.refresh(this.team);
            this.pitchPanel.repaint();
            this.ok = true;
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
        this.sourceIndex = this.squadList.getSelectedIndex();
        Player player = (Player)this.squadList.getSelectedValue();
        if (this.sourceIndex != -1 && player.index != 0) {
            this.posList.selectPos(this.squadList, this.sourceIndex);
            this.roleBox.setActionCommand("n");
            this.roleBox.removeAllItems();
            this.roleBox.setEnabled(false);
            this.roleBox.setActionCommand("y");
            this.pitchPanel.selected = -1;
            this.adPanel.selected = -1;
            this.pitchPanel.repaint();
            this.adPanel.repaint();
            PlayerTransferable playerTransferable = new PlayerTransferable(player);
            dragGestureEvent.getDragSource().startDrag(dragGestureEvent, null, playerTransferable, this);
        }
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
        this.squadList.clearSelection();
        this.posList.clearSelection();
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
            return new DataFlavor[]{FormPanel.this.localPlayerFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
            return FormPanel.this.localPlayerFlavor.equals(dataFlavor);
        }
    }

    private class Role {
        String name = "---";
        int index;

        public Role(int n) {
            this.index = n;
            if (this.index == 0) {
                this.name = "GK";
            }
            if (this.index > 0 && this.index < 4 || this.index > 5 && this.index < 8) {
                this.name = "CBT";
            }
            if (this.index == 4) {
                this.name = "CWP";
            }
            if (this.index == 5) {
                this.name = "ASW";
            }
            if (this.index == 8) {
                this.name = "LB";
            }
            if (this.index == 9) {
                this.name = "RB";
            }
            if (this.index > 9 && this.index < 15) {
                this.name = "DM";
            }
            if (this.index == 15) {
                this.name = "LWB";
            }
            if (this.index == 16) {
                this.name = "RWB";
            }
            if (this.index > 16 && this.index < 22) {
                this.name = "CM";
            }
            if (this.index == 22) {
                this.name = "LM";
            }
            if (this.index == 23) {
                this.name = "RM";
            }
            if (this.index > 23 && this.index < 29) {
                this.name = "AM";
            }
            if (this.index == 29) {
                this.name = "LW";
            }
            if (this.index == 30) {
                this.name = "RW";
            }
            if (this.index > 30 && this.index < 36) {
                this.name = "SS";
            }
            if (this.index > 35 && this.index < 41) {
                this.name = "CF";
            }
            if (this.index > 40) {
                this.name = String.valueOf(this.index);
            }
        }

        public String toString() {
            return this.name;
        }
    }
}

