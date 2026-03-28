/*
 * Decompiled with CFR 0.151.
 */
package editor;

import editor.NationalityList;
import editor.OptionFile;
import editor.Stats;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SelectByNation
extends JPanel {
    public NationalityList freeList;
    JComboBox nationBox;
    JButton sort;
    boolean alpha;

    public SelectByNation(OptionFile optionFile) {
        super(new BorderLayout());
        JScrollPane jScrollPane = new JScrollPane(22, 31);
        this.freeList = new NationalityList(optionFile);
        this.alpha = true;
        this.sort = new JButton("Alpha Order");
        this.sort.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (SelectByNation.this.alpha) {
                    SelectByNation.this.sort.setText("Index Order");
                    SelectByNation.this.alpha = false;
                } else {
                    SelectByNation.this.sort.setText("Alpha Order");
                    SelectByNation.this.alpha = true;
                }
                int n = SelectByNation.this.nationBox.getSelectedIndex();
                SelectByNation.this.freeList.refresh(n, SelectByNation.this.alpha);
            }
        });
        String[] stringArray = new String[Stats.nation.length + 5];
        stringArray[stringArray.length - 5] = "ML Old";
        stringArray[stringArray.length - 4] = "ML Young";
        stringArray[stringArray.length - 3] = "Duplicates?";
        stringArray[stringArray.length - 2] = "Free Agents";
        stringArray[stringArray.length - 1] = "All Players";
        System.arraycopy(Stats.nation, 0, stringArray, 0, Stats.nation.length);
        this.nationBox = new JComboBox<String>(stringArray);
        this.nationBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n = SelectByNation.this.nationBox.getSelectedIndex();
                if (n != -1) {
                    SelectByNation.this.freeList.refresh(n, SelectByNation.this.alpha);
                }
            }
        });
        this.nationBox.setMaximumRowCount(32);
        this.freeList.setSelectionMode(0);
        this.freeList.setLayoutOrientation(0);
        this.freeList.setVisibleRowCount(11);
        jScrollPane.setViewportView(this.freeList);
        this.add((Component)this.nationBox, "North");
        this.add((Component)jScrollPane, "Center");
        this.add((Component)this.sort, "South");
        this.setPreferredSize(new Dimension(164, 601));
    }

    public void refresh() {
        this.nationBox.setSelectedIndex(this.nationBox.getItemCount() - 1);
        this.freeList.refresh(this.nationBox.getSelectedIndex(), this.alpha);
    }
}

