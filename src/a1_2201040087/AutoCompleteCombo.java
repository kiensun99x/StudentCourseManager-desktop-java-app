package a1_2201040087;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.util.List;

public class AutoCompleteCombo {
    public static void enable(JComboBox<String> combo, List<String> dataList) {
        combo.setEditable(true);
        JTextField editor = (JTextField) combo.getEditor().getEditorComponent();

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = editor.getText();
                combo.hidePopup();

                combo.removeAllItems();

                for (String item : dataList) {
                    if (item.startsWith(text)) {
                        combo.addItem(item);
                    }
                }

                editor.setText(text);
                combo.showPopup();
            }
        });
    }
}

