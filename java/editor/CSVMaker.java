/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.Clubs;
import editor.OptionFile;
import editor.Player;
import editor.Stats;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CSVMaker {
    private RandomAccessFile out;
    private static String[] team;
    private static int seperator;
    private OptionFile of;

    static {
        seperator = 44;
    }

    public boolean makeFile(OptionFile optionFile, File file, boolean bl, boolean bl2, boolean bl3) {
        this.of = optionFile;
        boolean bl4 = false;
        try {
            this.out = new RandomAccessFile(file, "rw");
            team = Clubs.getNames(this.of);
            if (bl) {
                this.writeHeadings();
                this.out.write(13);
                this.out.write(10);
            }
            int n = 1;
            while (n < 5165) {
                this.writePlayer(this.out, n);
                ++n;
            }
            if (bl2) {
                n = 5165;
                while (n < 5165) {
                    this.writePlayer(this.out, n);
                    ++n;
                }
            }
            if (bl3) {
                n = 32768;
                while (n < 32952) {
                    this.writePlayer(this.out, n);
                    ++n;
                }
            }
            this.out.close();
            bl4 = true;
        }
        catch (IOException iOException) {
            bl4 = false;
        }
        return bl4;
    }

    private void writeName(int n) throws IOException {
        Player player = new Player(this.of, n, 0);
        String string = player.name.replaceAll(",", "");
        this.out.writeBytes(string);
    }

    private void writeHeadings() throws IOException {
        String[] stringArray = new String[]{"GK  0", "CWP  2", "CBT  3", "SB  4", "DMF  5", "WB  6", "CMF  7", "SMF  8", "AMF  9", "WF 10", "SS  11", "CF  12", "REGISTERED POSITION", "HEIGHT", "STRONG FOOT", "FAVOURED SIDE", "WEAK FOOT ACCURACY", "WEAK FOOT FREQUENCY", "ATTACK", "DEFENSE", "BALANCE", "STAMINA", "TOP SPEED", "ACCELERATION", "RESPONSE", "AGILITY", "DRIBBLE ACCURACY", "DRIBBLE SPEED", "SHORT PASS ACCURACY", "SHORT PASS SPEED", "LONG PASS ACCURACY", "LONG PASS SPEED", "SHOT ACCURACY", "SHOT POWER", "SHOT TECHNIQUE", "FREE KICK ACCURACY", "SWERVE", "HEADING", "JUMP", "TECHNIQUE", "AGGRESSION", "MENTALITY", "GOAL KEEPING", "TEAM WORK", "CONSISTENCY", "CONDITION / FITNESS", "DRIBBLING", "TACTIAL DRIBBLE", "POSITIONING", "REACTION", "PLAYMAKING", "PASSING", "SCORING", "1-1 SCORING", "POST PLAYER", "LINES", "MIDDLE SHOOTING", "SIDE", "CENTRE", "PENALTIES", "1-TOUCH PASS", "OUTSIDE", "MARKING", "SLIDING", "COVERING", "D-LINE CONTROL", "PENALTY STOPPER", "1-ON-1 STOPPER", "LONG THROW", "INJURY TOLERANCE", "DRIBBLE STYLE", "FREE KICK STYLE", "PK STYLE", "DROP KICK STYLE", "AGE", "WEIGHT", "NATIONALITY", "INTERNATIONAL NUMBER", "CLASSIC NUMBER", "CLUB TEAM", "CLUB NUMBER"};
        this.out.writeBytes("NAME");
        int n = 0;
        while (n < stringArray.length) {
            this.out.write(seperator);
            this.out.writeBytes(stringArray[n]);
            ++n;
        }
    }

    private void writeInterStatus(int n) throws IOException {
        String string = "0";
        int n2 = Stats.getValue(this.of, n, Stats.nationality);
        if (n2 < 57) {
            int n3 = 0;
            while (n3 < 23) {
                int n4 = this.of.toInt(this.of.data[683296 + 46 * n2 + n3 * 2]);
                int n5 = this.of.toInt(this.of.data[683297 + 46 * n2 + n3 * 2]);
                if ((n5 << 8 | n4) == n) {
                    int n6 = this.of.toInt(this.of.data[676624 + 23 * n2 + n3]) + 1;
                    string = String.valueOf(n6);
                }
                ++n3;
            }
        }
        this.out.writeBytes(string);
    }

    private void writeClassicStatus(int n) throws IOException {
        String string = "0";
        int n2 = Stats.getValue(this.of, n, Stats.nationality);
        int n3 = 0;
        if (n2 == 6 || n2 == 8 || n2 == 9 || n2 == 13 || n2 == 15 || n2 == 44 || n2 == 45) {
            if (n2 == 6) {
                n3 = 57;
            }
            if (n2 == 8) {
                n3 = 58;
            }
            if (n2 == 9) {
                n3 = 59;
            }
            if (n2 == 13) {
                n3 = 60;
            }
            if (n2 == 15) {
                n3 = 61;
            }
            if (n2 == 44) {
                n3 = 62;
            }
            if (n2 == 45) {
                n3 = 63;
            }
            int n4 = 0;
            while (n4 < 23) {
                int n5 = this.of.toInt(this.of.data[683296 + 46 * n3 + n4 * 2]);
                int n6 = this.of.toInt(this.of.data[683297 + 46 * n3 + n4 * 2]);
                if ((n6 << 8 | n5) == n) {
                    int n7 = this.of.toInt(this.of.data[676624 + 23 * n3 + n4]) + 1;
                    string = String.valueOf(n7);
                }
                ++n4;
            }
        }
        this.out.writeBytes(string);
    }

    private void writeTeam(int n) throws IOException {
        String string = "0";
        String string2 = "";
        int n2 = 0;
        while (n2 < 148) {
            int n3 = 0;
            while (n3 < 32) {
                int n4 = this.of.toInt(this.of.data[686654 + 64 * n2 + n3 * 2]);
                int n5 = this.of.toInt(this.of.data[686655 + 64 * n2 + n3 * 2]);
                if ((n5 << 8 | n4) == n) {
                    int n6 = this.of.toInt(this.of.data[678303 + 32 * n2 + n3]) + 1;
                    string2 = team[n2];
                    string = String.valueOf(n6);
                }
                ++n3;
            }
            ++n2;
        }
        this.out.writeBytes(string2);
        this.out.write(seperator);
        this.out.writeBytes(string);
    }

    private void writePlayer(RandomAccessFile randomAccessFile, int n) throws IOException {
        this.writeName(n);
        randomAccessFile.write(seperator);
        int n2 = 0;
        while (n2 < Stats.roles.length) {
            if (n2 != 1) {
                randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.roles[n2]));
                randomAccessFile.write(seperator);
            }
            ++n2;
        }
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.regPos));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.height));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.foot));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(this.getSide(n));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.wfa));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.wff));
        randomAccessFile.write(seperator);
        n2 = 0;
        while (n2 < Stats.ability99.length) {
            randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.ability99[n2]));
            randomAccessFile.write(seperator);
            ++n2;
        }
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.consistency));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.condition));
        randomAccessFile.write(seperator);
        n2 = 0;
        while (n2 < Stats.abilitySpecial.length) {
            randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.abilitySpecial[n2]));
            randomAccessFile.write(seperator);
            ++n2;
        }
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.injury));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.dribSty));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.freekick));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.pkStyle));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.dkSty));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.age));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.weight));
        randomAccessFile.write(seperator);
        randomAccessFile.writeBytes(Stats.getString(this.of, n, Stats.nationality));
        randomAccessFile.write(seperator);
        this.writeInterStatus(n);
        randomAccessFile.write(seperator);
        this.writeClassicStatus(n);
        randomAccessFile.write(seperator);
        this.writeTeam(n);
        randomAccessFile.write(13);
        randomAccessFile.write(10);
    }

    private String getSide(int n) {
        String string = "B";
        int n2 = Stats.getValue(this.of, n, Stats.favSide);
        if (n2 == 0) {
            string = Stats.getString(this.of, n, Stats.foot);
        }
        if (n2 == 1) {
            string = Stats.getValue(this.of, n, Stats.foot) == 0 ? "L" : "R";
        }
        return string;
    }
}

