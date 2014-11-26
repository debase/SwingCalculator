package eu.debas.simplecount.gui;

import eu.debas.simplecount.controller.SimpleCountController;
import eu.debas.simplecount.math.UnicodeMath;
import eu.debas.simplecount.model.DisplayModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by debas on 21/11/14.
 */
public class SimpleCountUI extends JFrame implements Observer {
    DisplayModel m_model = null;
    public static int WINDOW_SIZE_X = 300, WINDOW_SIZE_Y = 300;
    public static int ALL_EXPRESSION = 0, CURRENT_TYPED = 1, ERROR = 2;
    private HashMap<Integer, JTextField> m_display_field = new HashMap<Integer, JTextField>();

    /**
     *
     * @param windowName
     * @param model
     */
    public SimpleCountUI(String windowName, DisplayModel model) {
        super(windowName);
        m_model = model;

        m_model.addObserver(this);
        m_model.addDisplay(ALL_EXPRESSION);
        m_model.addDisplay(CURRENT_TYPED);
        m_model.addDisplay(ERROR);

        setLayout(new GridLayout(2, 1));
        add(createDisplayPanel());
        add(createButtonPanel(new SimpleCountController(m_model)));

        setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
        pack();
        setResizable(false);
    }

    /**
     *
     * @param simpleCountController
     * @return
     */
    private JPanel createButtonPanel(ActionListener simpleCountController) {
        JPanel pOtherEqual = new JPanel(new GridBagLayout());
        JPanel buttonPanelBasic = new JPanel(new GridLayout(4, 5));
        JPanel buttonPanelScientific = new JPanel(new GridLayout(4, 5));
        Box mainBox = Box.createHorizontalBox();
        JPanel buttonPanel = new JPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.fill = GridBagConstraints.BOTH;

        mainBox.add(buttonPanelBasic);
        mainBox.add(pOtherEqual);
        mainBox.add(Box.createRigidArea(new Dimension(5, 0)));
        mainBox.add(buttonPanelScientific);
        buttonPanel.add(mainBox);

        buttonPanelBasic.add(new SimpleCountButton("7", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton("8", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton("9", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton(UnicodeMath.DIVIDIDE, simpleCountController, "Divide [/]")); /* div */

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pOtherEqual.add(new SimpleCountButton("", "history", new ImageIcon(getClass().getResource("/return_arrow_white.png")), simpleCountController, "Undo"), gridBagConstraints);

        buttonPanelScientific.add(new SimpleCountButton(UnicodeMath.SQUARE_ROOT, simpleCountController, "Square root")); /* square root */
        buttonPanelScientific.add(new SimpleCountButton("tan", simpleCountController, "Tangent"));
        buttonPanelScientific.add(new SimpleCountButton("tanh", simpleCountController, "Hyperbolic tangent"));
        buttonPanelScientific.add(new SimpleCountButton("atan", simpleCountController, "atan"));
        buttonPanelScientific.add(new SimpleCountButton("ln", simpleCountController, "Logarithm neperien"));

        buttonPanelBasic.add(new SimpleCountButton("6", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton("5", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton("4", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton(UnicodeMath.MULTIPLY, simpleCountController, "Multiply"));

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        pOtherEqual.add(new SimpleCountButton("", "erase", new ImageIcon(getClass().getResource("/remove_all_white.png")), simpleCountController, "Clear Display"), gridBagConstraints);

        buttonPanelScientific.add(new SimpleCountButton(UnicodeMath.CUBIC_ROOT, simpleCountController, "Cubic Root")); /*cube root*/
        buttonPanelScientific.add(new SimpleCountButton("cos", simpleCountController, "Cosine"));
        buttonPanelScientific.add(new SimpleCountButton("cosh", simpleCountController, "Hyperbolic Cosine"));
        buttonPanelScientific.add(new SimpleCountButton("acos", simpleCountController, "acos"));
        buttonPanelScientific.add(new SimpleCountButton("exp", simpleCountController, "exp"));

        buttonPanelBasic.add(new SimpleCountButton("3", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton("2", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton("1", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton("-", simpleCountController, "Subtract"));

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weighty = 1.0;
        pOtherEqual.add(new SimpleCountButton("=", simpleCountController, "Calculate Result"), gridBagConstraints);

        buttonPanelScientific.add(new SimpleCountButton(UnicodeMath.SQUARE, simpleCountController, "Square")); /* square */
        buttonPanelScientific.add(new SimpleCountButton("sin", simpleCountController, "Sine"));
        buttonPanelScientific.add(new SimpleCountButton("sinh", simpleCountController, "Hyperbolic Sine"));
        buttonPanelScientific.add(new SimpleCountButton("asin", simpleCountController, "asin"));
        buttonPanelScientific.add(new SimpleCountButton("", simpleCountController, ""));

        buttonPanelBasic.add(new SimpleCountButton("0", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton(".", simpleCountController, null));
        buttonPanelBasic.add(new SimpleCountButton("%", simpleCountController, "Modulus Divide"));
        buttonPanelBasic.add(new SimpleCountButton("+", simpleCountController, "Add"));

        buttonPanelScientific.add(new SimpleCountButton(UnicodeMath.POW, simpleCountController, "Exponent"));
        buttonPanelScientific.add(new SimpleCountButton("log", simpleCountController, "Logarithm"));
        buttonPanelScientific.add(new SimpleCountButton(UnicodeMath.PI, simpleCountController, "Pi"));
        buttonPanelScientific.add(new SimpleCountButton("e", simpleCountController, "Euler's number"));
        buttonPanelScientific.add(new SimpleCountButton("", simpleCountController, ""));

        setBackground(Color.DARK_GRAY);

        buttonPanelBasic.setBackground(Color.DARK_GRAY);
        buttonPanelScientific.setBackground(Color.DARK_GRAY);
        mainBox.setBackground(Color.DARK_GRAY);
        buttonPanel.setBackground(Color.DARK_GRAY);

        return buttonPanel;
    }

    private JPanel createDisplayPanel() {
        JPanel displayPanel = new JPanel(new GridLayout(3, 1));
        SimpleCountField mCurrentCalcArea = new SimpleCountField(new Dimension(WINDOW_SIZE_X, 30), new Font("Calibri", Font.ITALIC, 17)),
                mResultArea = new SimpleCountField(new Dimension(WINDOW_SIZE_X, 30), new Font("Calibri", Font.BOLD, 20)),
                mErrorArea = new SimpleCountField(new Dimension(WINDOW_SIZE_X, 15), new Font("Calibri", Font.ITALIC, 10));

        displayPanel.add(mCurrentCalcArea);
        displayPanel.add(mResultArea);
        displayPanel.add(mErrorArea);

        mCurrentCalcArea.setBackground(Color.DARK_GRAY);
        mCurrentCalcArea.setForeground(Color.WHITE);

        mResultArea.setForeground(Color.WHITE);
        mResultArea.setBackground(Color.DARK_GRAY);

        mErrorArea.setForeground(Color.WHITE);
        mErrorArea.setBackground(Color.DARK_GRAY);
        displayPanel.setBackground(Color.DARK_GRAY);

        m_display_field.put(ERROR, mErrorArea);
        m_display_field.put(ALL_EXPRESSION, mCurrentCalcArea);
        m_display_field.put(CURRENT_TYPED, mResultArea);
        return displayPanel;
    }

    @Override
    public void update(Observable observable, Object o) {
        HashMap<Integer, String> map = (HashMap) o;

        m_display_field.get(ALL_EXPRESSION).setText(map.get(ALL_EXPRESSION));
        m_display_field.get(ERROR).setText(map.get(ERROR));
        m_display_field.get(CURRENT_TYPED).setText(map.get(CURRENT_TYPED));
    }
}
