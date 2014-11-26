package eu.debas.simplecount.controller;

import eu.debas.simplecount.SimpleCountHistory;
import eu.debas.simplecount.exception.MalFormedExpression;
import eu.debas.simplecount.gui.SimpleCountButton;
import eu.debas.simplecount.gui.SimpleCountUI;
import eu.debas.simplecount.math.EvalExpr;
import eu.debas.simplecount.math.UnicodeMath;
import eu.debas.simplecount.model.DisplayModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by debas on 08/11/14.
 */
public class SimpleCountController implements ActionListener {

    private String[] mOperator = {"+", "-", "%", UnicodeMath.DIVIDIDE, UnicodeMath.MULTIPLY, UnicodeMath.POW};
    private String[] mScientific = {"log", "cos", "sin", "tan", "acos", "asin", "atan", "cosh", "sinh", "tanh", "ln", "exp",
            UnicodeMath.SQUARE_ROOT, UnicodeMath.CUBIC_ROOT};
    private SimpleCountHistory mSimpleCountHistory = new SimpleCountHistory();
    private String mResultField = "", mCurrentCalcArea = "", mErrorArea = "";
    private boolean negative = false, mClearCalc = false;
    private int indexScientifiqueValue = -1;
    private EvalExpr mEvalExpr = new EvalExpr();
    private String mCurrentNumber = "";

    private DisplayModel m_modele = null;

    public SimpleCountController(DisplayModel modele) {
        m_modele = modele;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object obj = actionEvent.getSource();

        if (obj instanceof SimpleCountButton) {
            String buttonText = ((SimpleCountButton) obj).getText();
            if (actionEvent.getActionCommand().equals("history")) {
                mClearCalc = false;
                restoreInstance();
            } else if (actionEvent.getActionCommand().equals("erase")) {
                mResultField = "";
                mCurrentCalcArea = "";
                mErrorArea = "";
                indexScientifiqueValue = -1;
                mCurrentNumber = "";
                negative = false;
            } else if (Arrays.asList(mScientific).contains(buttonText)) {
                if (indexScientifiqueValue != -1) return;
                saveInstance();
                indexScientifiqueValue = Arrays.asList(mScientific).indexOf(buttonText);
                manageScientifique(buttonText);
            } else {
                if (buttonText.equals(UnicodeMath.SQUARE) && mCurrentNumber.length() == 0) return;
                if (mCurrentNumber.contains(UnicodeMath.SQUARE) && buttonText.equals(UnicodeMath.SQUARE)) return;
                manageAllOther(buttonText);
            }
        }
        m_modele.setDisplayValue(SimpleCountUI.ERROR, mErrorArea);
        m_modele.setDisplayValue(SimpleCountUI.CURRENT_TYPED, mResultField);
        m_modele.setDisplayValue(SimpleCountUI.ALL_EXPRESSION, mCurrentCalcArea);
        m_modele.notifChanged();
    }

    private void updateCurrentCalc(String number, String op) {
        mCurrentCalcArea += number + " " + (op.compareTo(UnicodeMath.POW) == 0 ? "^" : op) + " ";
        mErrorArea = "";
    }

    private void manageScientifique(String s) {
        if (mResultField.length() > 0) {
            s = (negative ? "-" : "") + s + "(" + mResultField + ")";
        } else {
            s = (negative ? "-" : "") + s + "()";
            negative = false;
        }
        mResultField = s;
    }

    private void manageAllOther(String s) {
        if (s.compareTo("=") == 0) {
            if (mCurrentNumber.length() == 0) {
                return;
            }

            if (mClearCalc) {
                mCurrentCalcArea = "";
                mClearCalc = false;
            }

            saveInstance();
            try {
                Double result = mEvalExpr.eval(mCurrentCalcArea + mResultField);
                mCurrentCalcArea = mCurrentCalcArea + mResultField + " =";
                mResultField = simplifyResult(result);
                mClearCalc = true;
                indexScientifiqueValue = -1;
                mCurrentNumber = result.toString();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (MalFormedExpression malFormedExpression) {
                mErrorArea = malFormedExpression.getMessage();
                System.out.println("catch");
            } catch (NumberFormatException e) {
                mErrorArea = "Malformed number";
            } finally {
                return;
            }
        }

        for (String op : mOperator) {
            if (s.compareTo(op) == 0) {

                if (mCurrentNumber.length() == 0) {
                    if (s.compareTo("-") == 0) {
                        negative = true;
                    }
                    if (s.compareTo("+") == 0) {
                        negative = false;
                    }
                    return;
                }

                if (mClearCalc) {
                    mCurrentCalcArea = "";
                    mClearCalc = false;
                }

                saveInstance();
                updateCurrentCalc(mResultField, s);
                mResultField = "";
                mCurrentNumber = "";
                indexScientifiqueValue = -1;
                return;
            }
        }
        mResultField = concatNumber(mResultField, s);
    }

    private String concatNumber(String field, String s) {
        if (indexScientifiqueValue >= 0) {
            if (negative == true && mCurrentNumber.length() == 0) {
                saveInstance();
                negative = false;
                mCurrentNumber = "-" + s;
                return field.substring(0, field.length() - 1) + "-" + s + ")";
            } else if (field.contains(UnicodeMath.SQUARE)) {
                return field;
            } else {
                saveInstance();
                mCurrentNumber += s;
                return field.substring(0, field.length() - 1) + s + ")";
            }
        } else {
            if (negative == true && mCurrentNumber.length() == 0) {
                saveInstance();
                negative = false;
                mCurrentNumber = "-" + s;
                return "-" + s;
            } else {
                saveInstance();
                mCurrentNumber += s;
                return field + s;
            }
        }
    }

    public void saveInstance() {
        HashMap<String, Object> hm = new HashMap<String, Object>();

        hm.put("input", new String(mResultField));
        hm.put("current_calc", new String(mCurrentCalcArea));
        hm.put("error", new String(mErrorArea));
        hm.put("scientific", indexScientifiqueValue);
        hm.put("current_number", mCurrentNumber);
        hm.put("clear_calc", mClearCalc);
        mSimpleCountHistory.pushHistory(hm);
    }

    public void restoreInstance() {
        HashMap<String, Object> hm = mSimpleCountHistory.getOld();

        if (hm != null) {
            mResultField = ((String) hm.get("input"));
            mCurrentCalcArea = ((String) hm.get("current_calc"));
            mErrorArea = ((String) hm.get("error"));
            indexScientifiqueValue = (Integer) hm.get("scientific");
            mCurrentNumber = (String) hm.get("current_number");
            mClearCalc = (Boolean) hm.get("clear_calc");
        }
    }

    private String simplifyResult(Double result) {
        DecimalFormat f = (DecimalFormat) NumberFormat.getInstance();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();

        symbols.setDecimalSeparator('.');
        f.setDecimalFormatSymbols(symbols);

        f.setMinimumFractionDigits(2);
        f.setMaximumFractionDigits(8);
        f.setGroupingUsed(false);

        String format = f.format(result);
        if (format.length() >= 20) {
            f.applyPattern("0.00E00");
            format = f.format(result);
        }
        return format;
    }
}
