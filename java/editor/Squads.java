/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Formations;
import editor.OptionFile;
import editor.Stat;
import editor.Stats;

public class Squads {
    static final int num23 = 676624;
    static final int num32 = 678303;
    static final int slot23 = 683296;
    static final int slot32 = 686654;

    public static void fixForm(OptionFile optionFile, int n, boolean bl) {
        byte[] byArray = new byte[64];
        byte[] byArray2 = new byte[32];
        if (n >= 0 && n < 64 || n >= 73 && n < 221) {
            int n2;
            int n3;
            int n4;
            int n5 = n;
            if (n > 72) {
                n5 -= 9;
            }
            if (n < 64) {
                n4 = 23;
                n3 = 683296 + n * n4 * 2;
                n2 = 676624 + n * n4;
            } else {
                n4 = 32;
                n3 = 686654 + (n - 73) * n4 * 2;
                n2 = 678303 + (n - 73) * n4;
            }
            System.arraycopy(optionFile.data, n3, byArray, 0, n4 * 2);
            System.arraycopy(optionFile.data, n2, byArray2, 0, n4);
            int n6 = 0;
            while (n6 < n4) {
                System.arraycopy(byArray, Formations.getSlot(optionFile, n5, n6) * 2, optionFile.data, n3 + n6 * 2, 2);
                System.arraycopy(byArray2, Formations.getSlot(optionFile, n5, n6), optionFile.data, n2 + n6, 1);
                ++n6;
            }
            if (bl) {
                int n7 = 0;
                while (n7 < 6) {
                    n6 = 0;
                    byte by = 0;
                    while (n6 == 0 && by < 32) {
                        if (Formations.getSlot(optionFile, n5, by) == Formations.getJob(optionFile, n5, n7)) {
                            Formations.setJob(optionFile, n5, n7, by);
                            n6 = 1;
                        }
                        by = (byte)(by + 1);
                    }
                    ++n7;
                }
            }
            n6 = 0;
            while (n6 < 32) {
                Formations.setSlot(optionFile, n5, n6, (byte)n6);
                n6 = (byte)(n6 + 1);
            }
        }
    }

    public static void fixAll(OptionFile optionFile) {
        int n = 0;
        while (n < 221) {
            Squads.fixForm(optionFile, n, true);
            ++n;
        }
    }

    public static void tidy(OptionFile optionFile, int n) {
        if (n >= 0 && n < 64 || n >= 73 && n < 221) {
            int n2;
            int n3;
            int n4;
            if (n < 64) {
                n4 = 23;
                n3 = 683296 + n * n4 * 2;
                n2 = 676624 + n * n4;
            } else {
                n4 = 32;
                n3 = 686654 + (n - 73) * n4 * 2;
                n2 = 678303 + (n - 73) * n4;
            }
            byte[] byArray = new byte[(n4 - 11) * 2];
            byte[] byArray2 = new byte[n4 - 11];
            int n5 = 0;
            int n6 = 11;
            while (n6 < n4) {
                int n7 = n3 + n6 * 2;
                int n8 = n2 + n6;
                if (optionFile.data[n7] != 0 || optionFile.data[n7 + 1] != 0) {
                    byArray[n5 * 2] = optionFile.data[n7];
                    byArray[n5 * 2 + 1] = optionFile.data[n7 + 1];
                    byArray2[n5] = optionFile.data[n8];
                    ++n5;
                }
                ++n6;
            }
            n6 = n5;
            while (n6 < n4 - 11) {
                byArray2[n6] = -1;
                ++n6;
            }
            System.arraycopy(byArray, 0, optionFile.data, n3 + 22, byArray.length);
            System.arraycopy(byArray2, 0, optionFile.data, n2 + 11, byArray2.length);
        }
    }

    public static void tidy11(OptionFile optionFile, int n, int n2, int n3) {
        if (n >= 0 && n < 64 || n >= 73 && n < 221) {
            int n4;
            int n5;
            int n6;
            Stat stat = Stats.gk;
            int[] nArray = new int[21];
            int n7 = 0;
            if (n3 > 0 && n3 < 4 || n3 > 5 && n3 < 8) {
                stat = Stats.cbt;
                n7 = 1;
            }
            if (n3 == 4 || n3 == 5) {
                stat = Stats.cwp;
                n7 = 1;
            }
            if (n3 == 8) {
                stat = Stats.sb;
                n7 = 2;
            }
            if (n3 == 9) {
                stat = Stats.sb;
                n7 = 2;
            }
            if (n3 > 9 && n3 < 15) {
                stat = Stats.dm;
                n7 = 3;
            }
            if (n3 == 15) {
                stat = Stats.wb;
                n7 = 2;
            }
            if (n3 == 16) {
                stat = Stats.wb;
                n7 = 2;
            }
            if (n3 > 16 && n3 < 22) {
                stat = Stats.cm;
                n7 = 4;
            }
            if (n3 == 22) {
                stat = Stats.sm;
                n7 = 5;
            }
            if (n3 == 23) {
                stat = Stats.sm;
                n7 = 5;
            }
            if (n3 > 23 && n3 < 29) {
                stat = Stats.am;
                n7 = 6;
            }
            if (n3 > 35 && n3 < 41) {
                stat = Stats.cf;
                n7 = 7;
            }
            if (n3 > 30 && n3 < 36) {
                stat = Stats.ss;
                n7 = 6;
            }
            if (n3 == 29) {
                stat = Stats.wg;
                n7 = 8;
            }
            if (n3 == 30) {
                stat = Stats.wg;
                n7 = 8;
            }
            if (n < 64) {
                n6 = 23;
                n5 = 683296 + n * n6 * 2;
                n4 = 676624 + n * n6;
            } else {
                n6 = 32;
                n5 = 686654 + (n - 73) * n6 * 2;
                n4 = 678303 + (n - 73) * n6;
            }
            int n8 = 0;
            int n9 = -1;
            int[] nArray2 = new int[21];
            int n10 = 11;
            while (n9 != 0 && n10 < n6) {
                n8 = n10 - 11;
                int n11 = n5 + n10 * 2;
                n9 = optionFile.toInt(optionFile.data[n11 + 1]) << 8 | optionFile.toInt(optionFile.data[n11]);
                if (n9 != 0) {
                    nArray2[n8] = n9;
                    switch (n7) {
                        case 0: {
                            nArray[n8] = Stats.getValue(optionFile, n9, Stats.defence) + Stats.getValue(optionFile, n9, Stats.balance) + Stats.getValue(optionFile, n9, Stats.response) + Stats.getValue(optionFile, n9, Stats.gkAbil) + Stats.getValue(optionFile, n9, Stats.team);
                            break;
                        }
                        case 1: {
                            nArray[n8] = Stats.getValue(optionFile, n9, Stats.defence) + Stats.getValue(optionFile, n9, Stats.balance) + Stats.getValue(optionFile, n9, Stats.response) + Stats.getValue(optionFile, n9, Stats.speed) + Stats.getValue(optionFile, n9, Stats.team);
                            break;
                        }
                        case 2: {
                            nArray[n8] = Stats.getValue(optionFile, n9, Stats.defence) + Stats.getValue(optionFile, n9, Stats.balance) + Stats.getValue(optionFile, n9, Stats.response) + Stats.getValue(optionFile, n9, Stats.stamina) + Stats.getValue(optionFile, n9, Stats.speed) + Stats.getValue(optionFile, n9, Stats.lPassAcc) + Stats.getValue(optionFile, n9, Stats.team);
                            break;
                        }
                        case 3: {
                            nArray[n8] = Stats.getValue(optionFile, n9, Stats.defence) + Stats.getValue(optionFile, n9, Stats.balance) + Stats.getValue(optionFile, n9, Stats.response) + Stats.getValue(optionFile, n9, Stats.sPassAcc) + Stats.getValue(optionFile, n9, Stats.team);
                            break;
                        }
                        case 4: {
                            nArray[n8] = Stats.getValue(optionFile, n9, Stats.defence) + Stats.getValue(optionFile, n9, Stats.attack) + Stats.getValue(optionFile, n9, Stats.dribAcc) + Stats.getValue(optionFile, n9, Stats.sPassAcc) + Stats.getValue(optionFile, n9, Stats.tech) + Stats.getValue(optionFile, n9, Stats.team);
                            break;
                        }
                        case 5: {
                            nArray[n8] = Stats.getValue(optionFile, n9, Stats.attack) + Stats.getValue(optionFile, n9, Stats.speed) + Stats.getValue(optionFile, n9, Stats.stamina) + Stats.getValue(optionFile, n9, Stats.dribAcc) + Stats.getValue(optionFile, n9, Stats.lPassAcc) + Stats.getValue(optionFile, n9, Stats.team);
                            break;
                        }
                        case 6: {
                            nArray[n8] = Stats.getValue(optionFile, n9, Stats.attack) + Stats.getValue(optionFile, n9, Stats.dribAcc) + Stats.getValue(optionFile, n9, Stats.sPassAcc) + Stats.getValue(optionFile, n9, Stats.tech) + Stats.getValue(optionFile, n9, Stats.agility) + Stats.getValue(optionFile, n9, Stats.team);
                            break;
                        }
                        case 7: {
                            nArray[n8] = Stats.getValue(optionFile, n9, Stats.attack) + Stats.getValue(optionFile, n9, Stats.response) + Stats.getValue(optionFile, n9, Stats.shotAcc) + Stats.getValue(optionFile, n9, Stats.shotTec) + Stats.getValue(optionFile, n9, Stats.tech) + Stats.getValue(optionFile, n9, Stats.team);
                            break;
                        }
                        case 8: {
                            nArray[n8] = Stats.getValue(optionFile, n9, Stats.attack) + Stats.getValue(optionFile, n9, Stats.speed) + Stats.getValue(optionFile, n9, Stats.dribAcc) + Stats.getValue(optionFile, n9, Stats.dribSpe) + Stats.getValue(optionFile, n9, Stats.sPassAcc) + Stats.getValue(optionFile, n9, Stats.lPassAcc) + Stats.getValue(optionFile, n9, Stats.agility) + Stats.getValue(optionFile, n9, Stats.tech) + Stats.getValue(optionFile, n9, Stats.team);
                        }
                    }
                }
                ++n10;
            }
            n10 = 0;
            int n12 = 0;
            int n13 = 0;
            int n14 = 0;
            int n15 = 0;
            int n16 = 0;
            while (n16 < 21) {
                if (nArray2[n16] != 0) {
                    n15 = Stats.getValue(optionFile, nArray2[n16], stat);
                    if (n15 == 1 && nArray[n16] > n12) {
                        n12 = nArray[n16];
                        n10 = n16;
                    }
                    if (n15 == 0 && nArray[n16] > n14) {
                        n14 = nArray[n16];
                        n13 = n16;
                    }
                }
                ++n16;
            }
            if (n12 != 0) {
                n13 = n10;
            }
            optionFile.data[n5 + 2 * n2] = optionFile.toByte(nArray2[(n13 += 11) - 11] & 0xFF);
            optionFile.data[n5 + 2 * n2 + 1] = optionFile.toByte((nArray2[n13 - 11] & 0xFF00) >>> 8);
            optionFile.data[n5 + 2 * n13] = 0;
            optionFile.data[n5 + 2 * n13 + 1] = 0;
            optionFile.data[n4 + n2] = optionFile.data[n4 + n13];
            optionFile.data[n4 + n13] = -1;
            Squads.tidy(optionFile, n);
        }
    }
}

