package editor;

import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ExtraPanel extends JPanel {
   private static final long serialVersionUID = 1L;
   OptionFile of;
   int player;
   public JTextField faceField;
   public JTextField callField;
   public JTextField hairField;
   public javax.swing.JCheckBox shirtUntuckedCheck;  // ADDED: Shirt untucked checkbox
   public JComboBox<String> sleevesBox;  // ADDED: Sleeves dropdown
   public JComboBox<String> skinBox;     // ADDED: Skin color dropdown
   public JComboBox<String> faceTypeBox; // ADDED: Face type dropdown

   public ExtraPanel(final Frame owner, OptionFile of2) {
      super(new GridLayout(0, 2));
      this.of = of2;
      this.setBorder(BorderFactory.createTitledBorder("Appearance"));
      
      this.faceField = new JTextField(5);
      this.faceField.setInputVerifier(new Verifierface());
      this.faceField.setToolTipText("Face ID (0-511 for PES2008)");
      
      this.hairField = new JTextField(5);
      this.hairField.setInputVerifier(new Verifierhair());
      this.hairField.setToolTipText("Hair ID (0-2047)");
      
      this.callField = new JTextField(5);
      this.callField.setInputVerifier(new Verifiercall());
      this.callField.setToolTipText("Callname ID (0-65535, use 65535 to disable)");
      
      this.add(new JLabel("Face ID"));
      this.add(this.faceField);
      this.add(new JLabel("Hair ID"));
      this.add(this.hairField);
      this.add(new JLabel("Callname"));
      this.add(this.callField);
      
      // ADDED: Shirt untucked checkbox
      this.shirtUntuckedCheck = new javax.swing.JCheckBox("Shirt Untucked");
      this.shirtUntuckedCheck.setToolTipText("Check to have shirt untucked (Camisa por fuera)");
      this.add(new JLabel(""));  // Empty label for alignment
      this.add(this.shirtUntuckedCheck);
      
      // ADDED: Sleeves dropdown
      this.sleevesBox = new JComboBox<>(Stats.modSleeves);
      this.sleevesBox.setToolTipText("Sleeve length setting");
      this.add(new JLabel("Sleeves"));
      this.add(this.sleevesBox);

      // ADDED: Skin color dropdown
      this.skinBox = new JComboBox<>(Stats.modSkin);
      this.skinBox.setToolTipText("Skin color (1-4)");
      this.add(new JLabel("Skin Color"));
      this.add(this.skinBox);

      // ADDED: Face type dropdown
      this.faceTypeBox = new JComboBox<>(Stats.modFaceType);
      this.faceTypeBox.setToolTipText("0=Build, 1=Original (licensed face), 2=Preset (in-game editor face)");
      this.add(new JLabel("Face Type"));
      this.add(this.faceTypeBox);
   }

   public void load(int playerIndex) {
      this.player = playerIndex;
      // Load values using PES2008 Stats constants
      this.faceField.setText(Stats.getString(this.of, this.player, Stats.face));
      this.hairField.setText(Stats.getString(this.of, this.player, Stats.hair));
      this.callField.setText(Stats.getString(this.of, this.player, Stats.callName));
      
      // ADDED: Load shirt untucked value
      int shirtValue = Stats.getValue(this.of, this.player, Stats.shirtUntucked);
      this.shirtUntuckedCheck.setSelected(shirtValue == 1);
      
      // ADDED: Load sleeves value
      int sleevesValue = Stats.getValue(this.of, this.player, Stats.sleeves);
      this.sleevesBox.setSelectedIndex(sleevesValue);

      // ADDED: Load skin color value
      int skinValue = Stats.getValue(this.of, this.player, Stats.skin);
      if (skinValue >= 0 && skinValue < Stats.modSkin.length) {
         this.skinBox.setSelectedIndex(skinValue);
      }

      // ADDED: Load face type value
      int faceTypeValue = Stats.getValue(this.of, this.player, Stats.faceType);
      if (faceTypeValue >= 0 && faceTypeValue < Stats.modFaceType.length) {
         this.faceTypeBox.setSelectedIndex(faceTypeValue);
      }
   }

   class Verifiercall extends InputVerifier {
      public boolean verify(JComponent var1) {
         boolean var2 = false;
         JTextField var3 = (JTextField)var1;
         try {
            int var4 = new Integer(var3.getText());
            if (var4 >= 0 && var4 <= 65535) {
               var2 = true;
            }
         } catch (NumberFormatException var5) {
            var2 = false;
         }
         return var2;
      }
   }

   class Verifierface extends InputVerifier {
      public boolean verify(JComponent var1) {
         boolean var2 = false;
         JTextField var3 = (JTextField)var1;
         try {
            int var4 = new Integer(var3.getText());
            if (var4 >= 0 && var4 <= 511) {  // PES2008 face range
               var2 = true;
            }
         } catch (NumberFormatException var5) {
            var2 = false;
         }
         return var2;
      }
   }

   class Verifierhair extends InputVerifier {
      public boolean verify(JComponent var1) {
         boolean var2 = false;
         JTextField var3 = (JTextField)var1;
         try {
            int var4 = new Integer(var3.getText());
            if (var4 >= 0 && var4 <= 2047) {
               var2 = true;
            }
         } catch (NumberFormatException var5) {
            var2 = false;
         }
         return var2;
      }
   }
}
