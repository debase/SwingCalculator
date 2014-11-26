package eu.debas.simplecount;

import eu.debas.simplecount.gui.SimpleCountUI;
import eu.debas.simplecount.model.DisplayModel;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        DisplayModel model = new DisplayModel();
        SimpleCountUI ui = new SimpleCountUI("Simple Count", model);

        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setVisible(true);
    }
}
