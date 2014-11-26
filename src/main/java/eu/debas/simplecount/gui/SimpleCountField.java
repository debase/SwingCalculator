package eu.debas.simplecount.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by debas on 23/11/14.
 */
public class SimpleCountField extends JTextField {
    public SimpleCountField(Dimension dim, Font font) {
        setPreferredSize(dim);
        setFont(font);
        setHorizontalAlignment(JTextField.RIGHT);
        setBorder(BorderFactory.createEmptyBorder());
        setEditable(false);
    }
}
