package com.example.calculator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView textViewExpression;
    private TextView textViewNumeric;

    private final String EXPRESSION_KEY = "1";
    private final String NUMBER_KEY = "2";


    private ExpressionHelper expressionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab_1);

        textViewExpression = findViewById(R.id.textViewExpression);
        textViewNumeric = findViewById(R.id.textViewNumeric);

        String expression = "";
        String currentNumber = "0";
        if(savedInstanceState != null){
            expression = savedInstanceState.getString(EXPRESSION_KEY);
            currentNumber = savedInstanceState.getString(NUMBER_KEY);

            textViewNumeric.setText(currentNumber);
            textViewExpression.setText(expression);

        }

        expressionHelper = new ExpressionHelper(expression, currentNumber);

        initializeNumericButtons();
        initializeOperationButtons();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(EXPRESSION_KEY, expressionHelper.getCurrentExpression());
        outState.putString(NUMBER_KEY, expressionHelper.getCurrentNumber());
    }

    private void initializeNumericButtons(){

        View.OnClickListener onClickListener = view -> {
            Button btn = (Button) view;
            char number = btn.getText().charAt(0);

            expressionHelper.appendNumber(number);
            textViewNumeric.setText(expressionHelper.getCurrentNumber());
        };

        findViewById(R.id.button_one).setOnClickListener(onClickListener);
        findViewById(R.id.button_two).setOnClickListener(onClickListener);
        findViewById(R.id.button_three).setOnClickListener(onClickListener);
        findViewById(R.id.button_four).setOnClickListener(onClickListener);
        findViewById(R.id.button_five).setOnClickListener(onClickListener);
        findViewById(R.id.button_six).setOnClickListener(onClickListener);
        findViewById(R.id.button_seven).setOnClickListener(onClickListener);
        findViewById(R.id.button_eight).setOnClickListener(onClickListener);
        findViewById(R.id.button_nine).setOnClickListener(onClickListener);
        findViewById(R.id.button_zero).setOnClickListener(onClickListener);
    }

    private void initializeOperationButtons(){

        View.OnClickListener onClickListener = view -> {
            Button btn = (Button) view;
            String operation = btn.getText().toString();
            switch (operation){
                case "CE":
                    expressionHelper.resetCurrentNumber();
                    break;
                case "C":
                    expressionHelper.resetAllIntermediates();
                    break;
                case "+/-":
                    expressionHelper.negateNumber();
                    break;
                case ".":
                    expressionHelper.makeFractional();
                    break;
                case "DEL":
                    expressionHelper.deleteDigit();
                    break;
                case "=":
                    expressionHelper.evaluateExpression();
                    break;
                default:
                    expressionHelper.declareOperation(operation);
            }

            textViewExpression.setText(expressionHelper.getCurrentExpression());
            textViewNumeric.setText(expressionHelper.getCurrentNumber());
        };

        findViewById(R.id.button_remove_all).setOnClickListener(onClickListener);
        findViewById(R.id.button_remove_last_number).setOnClickListener(onClickListener);
        findViewById(R.id.button_negation).setOnClickListener(onClickListener);
        findViewById(R.id.button_division).setOnClickListener(onClickListener);
        findViewById(R.id.button_multiplication).setOnClickListener(onClickListener);
        findViewById(R.id.button_sum).setOnClickListener(onClickListener);
        findViewById(R.id.button_substraction).setOnClickListener(onClickListener);
        findViewById(R.id.button_comma).setOnClickListener(onClickListener);
        findViewById(R.id.button_equality).setOnClickListener(onClickListener);
        findViewById(R.id.button_delete_digit).setOnClickListener(onClickListener);
    }
}