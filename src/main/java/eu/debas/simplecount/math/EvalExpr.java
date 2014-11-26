package eu.debas.simplecount.math;

import eu.debas.simplecount.exception.MalFormedExpression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by debas on 10/11/14.
 */
public class EvalExpr {
    private HashMap<String, String> mOpAssociation = new HashMap<String, String>();

    public EvalExpr() {
        mOpAssociation.put("+", "addition");
        mOpAssociation.put(UnicodeMath.DIVIDIDE, "division");
        mOpAssociation.put("-", "subtraction");
        mOpAssociation.put("%", "modulo");
        mOpAssociation.put(UnicodeMath.MULTIPLY, "multiplication");
        mOpAssociation.put("log", "logarithm");
        mOpAssociation.put("tan", "tangente");
        mOpAssociation.put("sin", "sinus");
        mOpAssociation.put("cos", "cosinus");
        mOpAssociation.put("tanh", "tangente_hyperbolique");
        mOpAssociation.put("sinh", "sinus_hyperbolique");
        mOpAssociation.put("cosh", "cosinus_hyperbolique");
        mOpAssociation.put("atan", "tangente_inverse");
        mOpAssociation.put("asin", "sinus_inverse");
        mOpAssociation.put("acos", "cosinus_inverse");
        mOpAssociation.put(UnicodeMath.SQUARE_ROOT, "square_root");
        mOpAssociation.put(UnicodeMath.SQUARE, "square");
        mOpAssociation.put(UnicodeMath.CUBIC_ROOT, "cubic_square");
        mOpAssociation.put("^", "pow");
        mOpAssociation.put("ln", "logarithm_neperien");
        mOpAssociation.put("exp", "exponential");
    }
    
    private void removeAndInsert(int index, int nbToRemove, List<String> exprList, double result) {
        for (int i = 0; i < nbToRemove; i++) {
            exprList.remove(index);
        }
        exprList.add(index, String.valueOf(result));
    }

    private String extractFromParanthesis(int index, List<String> exprList) {
        Pattern pattern = Pattern.compile("\\(([^)]*)\\)");
        Matcher m = pattern.matcher(exprList.get(index));

        m.find();
        return replaceMathValue(m.group(1));
    }

    public void multiplication(int index, List<String> exprList) {
        double left, right, result;

        left = Double.valueOf(replaceMathValue(exprList.get(index - 1)));
        right = Double.valueOf(replaceMathValue(exprList.get(index + 1)));
        result = left * right;

        removeAndInsert(index - 1, 3, exprList, result);
    }

    public void addition(int index, List<String> exprList) {
        double left, right, result;

        left = Double.valueOf(replaceMathValue(exprList.get(index - 1)));
        right = Double.valueOf(replaceMathValue(exprList.get(index + 1)));
        result = left + right;

        removeAndInsert(index - 1, 3, exprList, result);
    }

    public void division(int index, List<String> exprList) throws MalFormedExpression {
        double left, right, result;

        left = Double.valueOf(replaceMathValue(exprList.get(index - 1)));
        right = Double.valueOf(replaceMathValue(exprList.get(index + 1)));
        if (right == 0.0) {
            throw new MalFormedExpression("Division by zero not authorized");
        }
        result = left / right;

        removeAndInsert(index - 1, 3, exprList, result);
    }

    public void subtraction(int index, List<String> exprList) {
        double left, right, result;

        left = Double.valueOf(replaceMathValue(exprList.get(index - 1)));
        right = Double.valueOf(replaceMathValue(exprList.get(index + 1)));
        result = left - right;

        removeAndInsert(index - 1, 3, exprList, result);
    }

    public void modulo(int index, List<String> exprList) throws MalFormedExpression{
        double left, right, result;

        left = Double.valueOf(replaceMathValue(exprList.get(index - 1)));
        right = Double.valueOf(replaceMathValue(exprList.get(index + 1)));
        result = left % right;

        removeAndInsert(index - 1, 3, exprList, result);
    }

    public void logarithm(int index, List<String> exprList) throws MalFormedExpression, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        double logValue = new EvalExpr().eval(extractFromParanthesis(index, exprList));
        if (logValue <= 0) {
            throw new MalFormedExpression("Log value cant't be negative");
        }
        double result = Math.log10(logValue);
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void tangente(int index, List<String> exprList) throws NoSuchMethodException, MalFormedExpression, IllegalAccessException, InvocationTargetException {
        double result = Math.tan(Math.toRadians(new EvalExpr().eval(extractFromParanthesis(index, exprList))));
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void sinus(int index, List<String> exprList) throws NoSuchMethodException, MalFormedExpression, IllegalAccessException, InvocationTargetException {
        double result = Math.sin(Math.toRadians(new EvalExpr().eval(extractFromParanthesis(index, exprList))));
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void cosinus(int index, List<String> exprList) throws NoSuchMethodException, MalFormedExpression, IllegalAccessException, InvocationTargetException {
        double result = Math.cos(Math.toRadians(new EvalExpr().eval(extractFromParanthesis(index, exprList))));
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void tangente_hyperbolique(int index, List<String> exprList) throws NoSuchMethodException, MalFormedExpression, IllegalAccessException, InvocationTargetException {
        double result = Math.tanh(new EvalExpr().eval(extractFromParanthesis(index, exprList)));
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void sinus_hyperbolique(int index, List<String> exprList) throws NoSuchMethodException, MalFormedExpression, IllegalAccessException, InvocationTargetException {
        double result = Math.sinh(new EvalExpr().eval(extractFromParanthesis(index, exprList)));
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void cosinus_hyperbolique(int index, List<String> exprList) throws NoSuchMethodException, MalFormedExpression, IllegalAccessException, InvocationTargetException {
        System.out.println(new EvalExpr().eval(extractFromParanthesis(index, exprList)));
        double result = Math.cosh(new EvalExpr().eval(extractFromParanthesis(index, exprList)));
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void tangente_inverse(int index, List<String> exprList) throws NoSuchMethodException, MalFormedExpression, IllegalAccessException, InvocationTargetException {
        double result = Math.atan(new EvalExpr().eval(extractFromParanthesis(index, exprList)));
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, Math.toDegrees(result));
    }

    public void sinus_inverse(int index, List<String> exprList) throws NoSuchMethodException, MalFormedExpression, IllegalAccessException, InvocationTargetException {
        double value = new EvalExpr().eval(extractFromParanthesis(index, exprList));
        if (value > 1 || value < -1) {
            throw new MalFormedExpression("Inverse sine is undefined for values outside [-1, 1]");
        }
        double result = Math.asin(value);
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, Math.toDegrees(result));
    }

    public void cosinus_inverse(int index, List<String> exprList) throws NoSuchMethodException, MalFormedExpression, IllegalAccessException, InvocationTargetException {
        double value = new EvalExpr().eval(extractFromParanthesis(index, exprList));
        if (value > 1 || value < -1) {
            throw new MalFormedExpression("Inverse cosine is undefined for values outside [-1, 1]");
        }
        double result = Math.acos(value);
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, Math.toDegrees(result));
    }

    public void square_root(int index, List<String> exprList) throws MalFormedExpression, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        double logValue = new EvalExpr().eval(extractFromParanthesis(index, exprList));
        if (logValue < 0) {
            throw new MalFormedExpression("Square root value cant't be negative");
        }
        double result = Math.sqrt(logValue);
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void square(int index, List<String> exprList) {
        String value = replaceMathValue(exprList.get(index).substring(0, exprList.get(index).length() - 1));

        double result = Double.valueOf(value);

        result = Math.pow(result, 2);
        removeAndInsert(index, 1, exprList, result);
    }

    public void cubic_square(int index, List<String> exprList) throws MalFormedExpression, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        double logValue = new EvalExpr().eval(extractFromParanthesis(index, exprList));
        if (logValue < 0) {
            throw new MalFormedExpression("Cubic square value cant't be negative");
        }
        double result = Math.cbrt(logValue);
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void logarithm_neperien(int index, List<String> exprList) throws MalFormedExpression, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        double logNeperienValue = new EvalExpr().eval(extractFromParanthesis(index, exprList));
        if (logNeperienValue <= 0) {
            throw new MalFormedExpression("Logarithm neperien is undefined for values outside [0, -\u221E]");
        }
        double result = Math.log(logNeperienValue);
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void exponential(int index, List<String> exprList) throws MalFormedExpression, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        double expValue = new EvalExpr().eval(extractFromParanthesis(index, exprList));
        double result = Math.exp(expValue);
        if (exprList.get(index).charAt(0) == '-' && result != 0)
            result = -result;
        removeAndInsert(index, 1, exprList, result);
    }

    public void pow(int index, List<String> exprList) {
        double left, right, result;

        left = Double.valueOf(exprList.get(index - 1));
        right = Double.valueOf(exprList.get(index + 1));

        result = Math.pow(left, right);
        removeAndInsert(index - 1, 3, exprList, result);
    }

    private String replaceMathValue(String expr) {
        StringBuilder stringBuilder = new StringBuilder(expr);
        String mult = " " + UnicodeMath.MULTIPLY + " ";
        HashMap<String, Double> valueReplace = new HashMap<String, Double>();

        valueReplace.put(UnicodeMath.PI, Math.PI);
        valueReplace.put("e", Math.exp(1));

        for (int i = stringBuilder.length() - 1 ; i >= 0 ; i--) {
            for (Map.Entry<String, Double> m : valueReplace.entrySet()) {
                if (String.valueOf(expr.charAt(i)).equals(m.getKey())) {
                    stringBuilder.deleteCharAt(i);
                    if (i == 0) {
                        stringBuilder.insert(i, m.getValue());
                    }
                    else {
                        stringBuilder.insert(i, mult + m.getValue());
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    public Double eval(String expr) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MalFormedExpression, NumberFormatException {
        List<String> exprList = new ArrayList<String>(Arrays.asList(expr.split(" ")));
        EvalPriorityUtils mEvalUtils = new EvalPriorityUtils();
        String op = null;
        int indexOfOp = 0;
        int nbOp = 0;

        mEvalUtils.pushPriority(new String[] {"log", "tanh", "sinh", "cosh", "acos", "asin", "atan", "cos", "sin", "tan", "ln", "exp",
                UnicodeMath.SQUARE_ROOT, UnicodeMath.CUBIC_ROOT, UnicodeMath.SQUARE});
        mEvalUtils.pushPriority(new String[] {"^"}, 1);
        mEvalUtils.pushPriority(new String[] {UnicodeMath.MULTIPLY, UnicodeMath.DIVIDIDE, "%"}, 1);
        mEvalUtils.pushPriority(new String[] {"+", "-"}, 1);
        while ((op = mEvalUtils.getNextOp(exprList)) != null) {
            System.out.println(exprList.toString());
            indexOfOp = mEvalUtils.getOpIndex();
            execOperation(indexOfOp, op, exprList);
            nbOp++;
        }
        if (nbOp == 0 && (expr.contains(UnicodeMath.PI) || expr.contains("e")))
            return Double.valueOf(new EvalExpr().eval(replaceMathValue(expr)));
        return Double.valueOf(exprList.get(0));
    }

    private void execOperation(int indexOfOp, String op, List<String> exprList) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, MalFormedExpression {
        String methodName = mOpAssociation.get(op);
        System.out.println(methodName.toString());
        Method m = getClass().getMethod(methodName, int.class, List.class);
        try {
            m.invoke(this, indexOfOp, exprList);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof MalFormedExpression) {
                throw (MalFormedExpression) e.getCause();
            } else if (e.getCause() instanceof NumberFormatException) {
                throw (NumberFormatException) e.getCause();
            }
        }
    }

    private class EvalPriorityUtils {
        private List<EvalStruct> mOpPriorty = new ArrayList<EvalStruct>();
        private int mIndexOp = 0;

        private class EvalStruct {
            public int mOpSize;
            public String[] mOpTab;

            EvalStruct (String[] opTab, int minElem){
                mOpSize = minElem;
                mOpTab = opTab;
            }
        }

        EvalPriorityUtils() {
        }

        public void pushPriority(String[] op, int maxOpSize) {
            mOpPriorty.add(new EvalStruct(op, maxOpSize));
        }


        public void pushPriority(String[] op) {
            mOpPriorty.add(new EvalStruct(op, -1));
        }

        public String getNextOp(List<String> exprList) {
            for (EvalStruct listOpToFind : mOpPriorty) {
                for (int i = 0; i < exprList.size(); i++) {
                    for (int j = 0; j < listOpToFind.mOpTab.length; j++) {
                        if (exprList.get(i).contains(listOpToFind.mOpTab[j])) {
                            if (listOpToFind.mOpSize == -1 || (exprList.get(i).length() == listOpToFind.mOpSize)) {
                                mIndexOp = i;
                                return listOpToFind.mOpTab[j];
                            }
                        }
                    }
                }
            }
            return null;
        }

        public int getOpIndex() {
            return mIndexOp;
        }
    }
}
