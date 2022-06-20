package com.example.calculator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpressionHelper {

    private Map<String, Integer> priorities;
    private ArrayList<Double> numbers;
    private ArrayList<String> operands;

    private StringBuilder expression;
    private StringBuilder currentNumber;

    private String previousNumber = "";

    public ExpressionHelper(String savedExpression, String savedNumber) {
        expression = new StringBuilder(savedExpression);
        currentNumber = new StringBuilder(savedNumber);

        priorities = new HashMap<>();
        priorities.put("+", 1);
        priorities.put("-", 1);
        priorities.put("*", 2);
        priorities.put("/", 2);
    }

    public void appendNumber(char number) {
        if (currentNumIsStandAloneZero())
            currentNumber.deleteCharAt(0);
        else if (currentNumber.toString().equals(previousNumber)) {
            currentNumber = new StringBuilder("");
        }
        currentNumber.append(number);
    }

    public void resetCurrentNumber() {
        currentNumber = new StringBuilder("0");
    }

    public void resetAllIntermediates() {
        resetCurrentNumber();
        expression = new StringBuilder();
    }

    public void negateNumber() {
        char firstNum = currentNumber.charAt(0);
        if (currentNumIsStandAloneZero())
            return;
        else if (firstNum == '-')
            currentNumber.deleteCharAt(0);
        else
            currentNumber.insert(0, '-');
    }

    private boolean currentNumIsStandAloneZero() {
        return currentNumber.length() == 1 && currentNumber.charAt(0) == '0';
    }

    public void makeFractional() {
        int index = currentNumber.indexOf(".");
        if (index == -1)
            currentNumber.append('.');
        else
            return;
    }

    public void deleteDigit() {
        currentNumber.deleteCharAt(currentNumber.length() - 1);
        if (currentNumber.length() == 1 && currentNumber.charAt(0) == '-')
            currentNumber.deleteCharAt(0);

        if (currentNumber.length() == 0)
            currentNumber.append("0");
    }

    public void declareOperation(String operation) {
        boolean isExpressionEmpty = expression.length() == 0;
        if (!isExpressionEmpty && expression.charAt(expression.length() - 1) == '=') {
            expression = new StringBuilder();
            expression.append(currentNumber).append(operation);
            previousNumber = currentNumber.toString();
        } else if (isExpressionEmpty || !currentNumber.toString().equals(previousNumber)) {
            expression.append(currentNumber).append(operation);
            previousNumber = currentNumber.toString();
        } else if (previousNumber.equals(currentNumber.toString())) {
            expression.deleteCharAt(expression.length() - 1).append(operation);
        }
    }


    private Double tryParse(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void evaluateExpression() {

        if (expression.length() > 0 && expression.charAt(expression.length() - 1) != '=') {

            expression.append(currentNumber);
            convertExpression();
            try {
                while (numbers.size() != 1) {

                    int operandIndex = findOperandIndexWithBiggestPriority();
                    String operand = operands.get(operandIndex);
                    double firstNum = numbers.get(operandIndex);
                    double secondNum = numbers.get(operandIndex + 1);

                    double result = performOperation(operand, firstNum, secondNum);

                    operands.remove(operandIndex);
                    numbers.add(operandIndex, result);
                    numbers.remove(operandIndex + 1);
                    numbers.remove(operandIndex + 1);
                }
            } catch (ArithmeticException e) {
                currentNumber = new StringBuilder("Division by zero is not possible");
            }

            boolean hasFraction = numbers.get(0) % 1 != 0;
            if (hasFraction)
                currentNumber = new StringBuilder(String.valueOf(numbers.get(0)));
            else {
                currentNumber = new StringBuilder(String.valueOf(numbers.get(0).intValue()));
            }
            expression.append("=");
        }
    }

    private double performOperation(String operand, double num1, double num2) {

        switch (operand) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                throw new ArithmeticException();
        }
    }

    private void convertExpression() {
        numbers = new ArrayList<>();
        operands = new ArrayList<>();

        Set<String> operandsSet = priorities.keySet();
        int previousNumeriqueIndex = 0;

        for (int i = 0; i < expression.length(); i++) {

            String currentCharacter = String.valueOf(expression.charAt(i));
            if (operandsSet.contains(currentCharacter) && i != 0
                    && !operandsSet.contains(String.valueOf(expression.charAt(i - 1)))) {
                operands.add(currentCharacter);
                double number = Double.parseDouble(expression.substring(previousNumeriqueIndex, i));
                numbers.add(number);
                previousNumeriqueIndex = i + 1;
            }
        }
        double lastNumber = Double.parseDouble(expression.substring(previousNumeriqueIndex, expression.length()));
        numbers.add(lastNumber);
    }

    private int findOperandIndexWithBiggestPriority() {

        int maxPriority = 0;
        int index = 0;
        for (int i = 0; i < operands.size(); i++) {
            int priority = priorities.get(operands.get(i));
            if (priority > maxPriority) {
                maxPriority = priority;
                index = i;
            }
        }
        return index;
    }

    public String getCurrentExpression(){
        return expression.toString();
    }

    public String getCurrentNumber(){
        return currentNumber.toString();
    }

}
