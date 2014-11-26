package eu.debas.simplecount.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by debas on 21/11/14.
 */
public class SimpleCountButton extends JButton {

    SimpleCountButton(String name, ActionListener listener, String toolTipText) {
        super(name);
        addActionListener(listener);
        setAttr(toolTipText);
    }

    SimpleCountButton(String name, String actionCommand, ImageIcon icon, ActionListener listener, String toolTipText) {
        super(name);
        setActionCommand(actionCommand);
        setIcon(icon);
        addActionListener(listener);
        setAttr(toolTipText);
    }

    private void setAttr(String toolTipText) {
        setFocusPainted(false);
        setFont(new Font("Calibri", Font.PLAIN | Font.BOLD, 14));
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        if (toolTipText != null) {
            setToolTipText(toolTipText);
        }
    }

}
