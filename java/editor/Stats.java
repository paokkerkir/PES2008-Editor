package editor;

public class Stats {
  static final Stat hair = new Stat(0, 45, 0, 2047, "");
  
  static final Stat faceType = new Stat(0, 55, 0, 3, "");
  
  static final Stat skin = new Stat(0, 41, 6, 3, "");
  
  static final Stat face = new Stat(0, 53, 5, 511, "");
  
  // ADDED: Shirt untucked (Camisa por fuera)  
  // VB: byte 88, shift 16, mask 1 → extracts bit (32-1-16)=15
  // Java: byte 41, shift 15, mask 1
  static final Stat shirtUntucked = new Stat(0, 41, 15, 1, "Shirt Untucked");
  
  // ADDED: Sleeves (Auto/Always Short/Always Long)
  // VB: byte 96, shift 24, mask 2 → extracts bit (32-2-24)=6
  // Java: byte 49, shift 6, mask 3 (values 0-2)
  static final Stat sleeves = new Stat(0, 49, 6, 3, "Sleeves");
  
  static final Stat nameEdited = new Stat(0, 3, 0, 1, "");
  
  static final Stat callEdited = new Stat(0, 3, 2, 1, "");
  
  static final Stat shirtEdited = new Stat(0, 3, 1, 1, "");
  
  static final Stat abilityEdited = new Stat(0, 40, 4, 1, "");
  
  static final Stat callName = new Stat(0, 1, 0, 65535, "");
  
  static final Stat height = new Stat(1, 41, 0, 63, "Height");
  
  static final Stat foot = new Stat(4, 5, 0, 1, "Foot");
  
  static final Stat favSide = new Stat(0, 33, 14, 3, "Fav side");
  
  static final Stat wfa = new Stat(5, 33, 11, 7, "W Foot Acc");
  
  static final Stat wff = new Stat(5, 33, 3, 7, "W Foot Freq");
  
  static final Stat injury = new Stat(6, 33, 6, 3, "Injury T");
  
  static final Stat dribSty = new Stat(5, 6, 0, 3, "");
  
  static final Stat freekick = new Stat(5, 5, 1, 15, "");
  
  static final Stat pkStyle = new Stat(5, 5, 5, 7, "");
  
  static final Stat dkSty = new Stat(5, 6, 2, 3, "");
  
  static final Stat age = new Stat(2, 65, 9, 31, "Age");
  
  static final Stat weight = new Stat(0, 41, 8, 127, "Weight");
  
  static final Stat nationality = new Stat(3, 65, 0, 127, "Nationality");
  
  static final Stat consistency = new Stat(5, 33, 0, 7, "Consistency");
  
  static final Stat condition = new Stat(5, 33, 8, 7, "Condition");
  
  static final Stat regPos = new Stat(0, 6, 4, 15, "");
  
  static final Stat gk = new Stat(0, 7, 7, 1, "GK");
  
  static final Stat cwp = new Stat(0, 7, 15, 1, "CWP");
  
  static final Stat cbt = new Stat(0, 9, 7, 1, "CBT");
  
  static final Stat sb = new Stat(0, 9, 15, 1, "SB");
  
  static final Stat dm = new Stat(0, 11, 7, 1, "DM");
  
  static final Stat wb = new Stat(0, 11, 15, 1, "WB");
  
  static final Stat cm = new Stat(0, 13, 7, 1, "CM");
  
  static final Stat sm = new Stat(0, 13, 15, 1, "SM");
  
  static final Stat am = new Stat(0, 15, 7, 1, "AM");
  
  static final Stat wg = new Stat(0, 15, 15, 1, "WG");
  
  static final Stat ss = new Stat(0, 17, 7, 1, "SS");
  
  static final Stat cf = new Stat(0, 17, 15, 1, "CF");
  
  static final Stat[] roles = new Stat[] { 
      gk, new Stat(0, 7, 15, 1, "?"), cwp, cbt, sb, dm, wb, cm, sm, am, 
      wg, ss, cf };
  
  static final Stat attack = new Stat(0, 7, 0, 127, "Attack");
  
  static final Stat defence = new Stat(0, 8, 0, 127, "Defense");
  
  static final Stat balance = new Stat(0, 9, 0, 127, "Balance");
  
  static final Stat stamina = new Stat(0, 10, 0, 127, "Stamina");
  
  static final Stat speed = new Stat(0, 11, 0, 127, "Speed");
  
  static final Stat accel = new Stat(0, 12, 0, 127, "Accel");
  
  static final Stat response = new Stat(0, 13, 0, 127, "Response");
  
  static final Stat agility = new Stat(0, 14, 0, 127, "Agility");
  
  static final Stat dribAcc = new Stat(0, 15, 0, 127, "Drib Acc");
  
  static final Stat dribSpe = new Stat(0, 16, 0, 127, "Drib Spe");
  
  static final Stat sPassAcc = new Stat(0, 17, 0, 127, "S Pass Acc");
  
  static final Stat sPassSpe = new Stat(0, 18, 0, 127, "S Pass Spe");
  
  static final Stat lPassAcc = new Stat(0, 19, 0, 127, "L Pass Acc");
  
  static final Stat lPassSpe = new Stat(0, 20, 0, 127, "L Pass Spe");
  
  static final Stat shotAcc = new Stat(0, 21, 0, 127, "Shot Acc");
  
  static final Stat shotPow = new Stat(0, 22, 0, 127, "Shot Power");
  
  static final Stat shotTec = new Stat(0, 23, 0, 127, "Shot Tech");
  
  static final Stat fk = new Stat(0, 24, 0, 127, "FK Acc");
  
  static final Stat curling = new Stat(0, 25, 0, 127, "Swerve");
  
  static final Stat heading = new Stat(0, 26, 0, 127, "Heading");
  
  static final Stat jump = new Stat(0, 27, 0, 127, "Jump");
  
  static final Stat tech = new Stat(0, 29, 0, 127, "Tech");
  
  static final Stat aggress = new Stat(0, 30, 0, 127, "Aggression");
  
  static final Stat mental = new Stat(0, 31, 0, 127, "Mentality");
  
  static final Stat gkAbil = new Stat(0, 32, 0, 127, "GK");
  
  static final Stat team = new Stat(0, 28, 0, 127, "Team Work");
  
  static final Stat[] ability99 = new Stat[] { 
      attack, defence, balance, stamina, speed, accel, response, agility, dribAcc, dribSpe, 
      sPassAcc, sPassSpe, lPassAcc, lPassSpe, shotAcc, shotPow, shotTec, fk, curling, heading, 
      jump, tech, aggress, mental, gkAbil, team };
  
  static final Stat[] abilitySpecial = new Stat[] { 
      new Stat(0, 21, 7, 1, "Dribbling"), new Stat(0, 21, 15, 1, "Tactical Drib"), new Stat(0, 23, 7, 1, "Positioning"), new Stat(0, 23, 15, 1, "Reaction"), new Stat(0, 25, 7, 1, "Playmaking"), new Stat(0, 25, 15, 1, "Passing"), new Stat(0, 27, 7, 1, "Scoring"), new Stat(0, 27, 15, 1, "1-1 Scoring"), new Stat(0, 29, 7, 1, "Post"), new Stat(0, 29, 15, 1, "Line Position"), 
      new Stat(0, 31, 7, 1, "Mid shooting"), new Stat(0, 31, 15, 1, "Side"), new Stat(0, 19, 15, 1, "Centre"), new Stat(0, 19, 7, 1, "Penalties"), new Stat(0, 35, 0, 1, "1-T Pass"), new Stat(0, 35, 1, 1, "Outside"), new Stat(0, 35, 2, 1, "Marking"), new Stat(0, 35, 3, 1, "Sliding"), new Stat(0, 35, 4, 1, "Cover"), new Stat(0, 35, 5, 1, "D-L Control"), 
      new Stat(0, 35, 6, 1, "Penalty GK"), new Stat(0, 35, 7, 1, "1-on-1 GK"), new Stat(0, 37, 7, 1, "Long Throw") };
  
  static final String[] nation = new String[] { 
      "Austria", "Belgium", "Bulgaria", "Croatia", "Czech Republic", "Denmark", "England", "Finland", "France", "Germany", 
      "Greece", "Hungary", "Ireland", "Israel", "Italy", "Netherlands", "Northern Ireland", "Norway", "Poland", "Portugal", 
      "Romania", "Russia", "Scotland", "Serbia and Montenegro", "Slovakia", "Slovenia", "Spain", "Sweden", "Switzerland", "Turkey", 
      "Ukraine", "Wales", "Angola", "Cameroon", "Cote d'Ivoire", "Ghana", "Nigeria", "South Africa", "Togo", "Tunisia", 
      "Costa Rica", "Mexico", "Trinidad and Tobago", "USA", "Argentina", "Brazil", "Chile", "Colombia", "Ecuador", "Paraguay", 
      "Peru", "Uruguay", "Australia", "Iran", "Japan", "Saudi Arabia", "South Korea", "Montenegro", "Benin", "Burkina Faso", 
      "Burundi", "Cape Verde", "Congo", "DR Congo", "Equatorial Guinea", "Gabon", "Gambia", "Guinea", "Kenya", "Liberia", 
      "Mali", "Rwanda", "Sierra Leone", "Zambia", "Zimbabwe", "Canada", "Grenada", "Martinique", "Netherlands Antilles", "New Zealand", 
      "Albania", "Andorra", "Armenia", "Azerbaijan", "Belarus", "Bosnia and Herzegovina", "Cyprus", "Estonia", "Faroe Islands", "Georgia", 
      "Iceland", "Kazakhstan", "Latvia", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Malta", "Moldova", "San Marino", 
      "Algeria", "Egypt", "Morocco", "Senegal", "Honduras", "Jamaica", "Bolivia", "Venezuela", "Bahrain", "China", 
      "Indonesia", "Iraq", "Malaysia", "Oman", "Qatar", "Thailand", "United Arab Emirates", "Uzbekistan", "Vietnam", "No Nationality" };
  
  static final String[] modFoot = new String[] { "R", "L" };
  
  static final String[] modInjury = new String[] { "C", "B", "A" };
  
  static final String[] modFK = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };
  
  static final String[] mod18 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };
  
  // ADDED: Sleeves options
  static final String[] modSleeves = new String[] { "Auto", "Always Short", "Always Long" };

  // ADDED: Skin color options (same as PES6)
  static final String[] modSkin = new String[] { "Skin 1", "Skin 2", "Skin 3", "Skin 4" };

  // ADDED: Face type options (0=Build, 1=Original, 2=Preset)
  static final String[] modFaceType = new String[] { "Build", "Original", "Preset" };
  
  public static int getValue(OptionFile paramOptionFile, int paramInt, Stat paramStat) {
    int i = 34752 + paramInt * 124 + paramStat.offSet;
    if (paramInt >= 32768)
      i = 11924 + (paramInt - 32768) * 124 + paramStat.offSet; 
    int j = paramOptionFile.toInt(paramOptionFile.data[i]) << 8 | paramOptionFile.toInt(paramOptionFile.data[i - 1]);
    j >>>= paramStat.shift;
    j &= paramStat.mask;
    if (paramStat.type == 3)
      if (j == 80) {
        j = nation.length - 1;
      } else if (j >= 83) {
        j -= 3;
      }  
    return j;
  }
  
  public static void setValue(OptionFile paramOptionFile, int paramInt1, Stat paramStat, int paramInt2) {
    int i = 34752 + paramInt1 * 124 + paramStat.offSet;
    if (paramInt1 >= 32768)
      i = 11924 + (paramInt1 - 32768) * 124 + paramStat.offSet; 
    if (paramStat.type == 3)
      if (paramInt2 == nation.length - 1) {
        paramInt2 = 80;
      } else if (paramInt2 >= 80) {
        paramInt2 += 3;
      }  
    int j = paramOptionFile.toInt(paramOptionFile.data[i]) << 8 | paramOptionFile.toInt(paramOptionFile.data[i - 1]);
    int k = 0xFFFF & (paramStat.mask << paramStat.shift ^ 0xFFFFFFFF);
    j &= k;
    paramInt2 &= paramStat.mask;
    paramInt2 <<= paramStat.shift;
    paramInt2 = j | paramInt2;
    paramOptionFile.data[i - 1] = paramOptionFile.toByte(paramInt2 & 0xFF);
    paramOptionFile.data[i] = paramOptionFile.toByte(paramInt2 >>> 8);
  }
  
  public static void setValue(OptionFile paramOptionFile, int paramInt, Stat paramStat, String paramString) {
    int i = 0;
    try {
      if (paramStat.type == 0)
        i = (new Integer(paramString)).intValue(); 
      if (paramStat.type == 1)
        i = (new Integer(paramString)).intValue() - 148; 
      if (paramStat.type == 2)
        i = (new Integer(paramString)).intValue() - 15; 
      if (paramStat.type == 3)
        for (byte b = 0; b < nation.length; b++) {
          if (paramString.equals(nation[b]))
            i = b; 
        }  
      if (paramStat.type == 4)
        for (byte b = 0; b < modFoot.length; b++) {
          if (paramString.equals(modFoot[b]))
            i = b; 
        }  
      if (paramStat.type == 5)
        i = (new Integer(paramString)).intValue() - 1; 
      if (paramStat.type == 6)
        for (byte b = 0; b < modInjury.length; b++) {
          if (paramString.equals(modInjury[b]))
            i = b; 
        }  
      if (paramStat.type == 7)
        for (byte b = 0; b < modFK.length; b++) {
          if (paramString.equals(modFK[b]))
            i = b; 
        }  
      setValue(paramOptionFile, paramInt, paramStat, i);
    } catch (NumberFormatException numberFormatException) {}
  }
  
  public static String getString(OptionFile paramOptionFile, int paramInt, Stat paramStat) {
    String str = "";
    int i = getValue(paramOptionFile, paramInt, paramStat);
    if (paramStat.type == 0)
      str = String.valueOf(i); 
    if (paramStat.type == 1)
      str = String.valueOf(i + 148); 
    if (paramStat.type == 2)
      str = String.valueOf(i + 15); 
    if (paramStat.type == 3)
      if (i >= 0 && i < nation.length) {
        str = nation[i];
      } else {
        str = String.valueOf(String.valueOf(i)) + "?";
      }  
    if (paramStat.type == 4) {
      str = "R";
      if (i == 1)
        str = "L"; 
    } 
    if (paramStat.type == 5)
      str = String.valueOf(i + 1); 
    if (paramStat.type == 6) {
      str = "A";
      if (i == 1)
        str = "B"; 
      if (i == 0)
        str = "C"; 
    } 
    if (paramStat.type == 7)
      str = modFK[i]; 
    return str;
  }
}


/* Location:              C:\Users\anffs\Desktop\PES Editor 2008-1.jar!\editor\Stats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */