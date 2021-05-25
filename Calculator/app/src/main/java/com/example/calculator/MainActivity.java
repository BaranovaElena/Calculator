package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final Double PI = 3.1415;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setButtonsListeners();
    }

    private void setButtonsListeners() {
        Button button0 = findViewById(R.id.button_0);
        button0.setOnClickListener(v -> showSymbol(button0));

        Button button1 = findViewById(R.id.button_1);
        button1.setOnClickListener(v -> showSymbol(button1));

        Button button2 = findViewById(R.id.button_2);
        button2.setOnClickListener(v -> showSymbol(button2));

        Button button3 = findViewById(R.id.button_3);
        button3.setOnClickListener(v -> showSymbol(button3));

        Button button4 = findViewById(R.id.button_4);
        button4.setOnClickListener(v -> showSymbol(button4));

        Button button5 = findViewById(R.id.button_5);
        button5.setOnClickListener(v -> showSymbol(button5));

        Button button6 = findViewById(R.id.button_6);
        button6.setOnClickListener(v -> showSymbol(button6));

        Button button7 = findViewById(R.id.button_7);
        button7.setOnClickListener(v -> showSymbol(button7));

        Button button8 = findViewById(R.id.button_8);
        button8.setOnClickListener(v -> showSymbol(button8));

        Button button9 = findViewById(R.id.button_9);
        button9.setOnClickListener(v -> showSymbol(button9));

        Button buttonPi = findViewById(R.id.button_pi);
        buttonPi.setOnClickListener(v -> showNumber(PI));

        Button buttonDot = findViewById(R.id.button_dot);
        buttonDot.setOnClickListener(v -> showSymbol(buttonDot));

        Button buttonPlus = findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> showSymbol(buttonPlus));

        Button buttonMinus = findViewById(R.id.button_minus);
        buttonMinus.setOnClickListener(v -> showSymbol(buttonMinus));

        Button buttonMultiply = findViewById(R.id.button_multiply);
        buttonMultiply.setOnClickListener(v -> showSymbol(buttonMultiply));

        Button buttonDivide = findViewById(R.id.button_divide);
        buttonDivide.setOnClickListener(v -> showSymbol(buttonDivide));

        findViewById(R.id.button_clear).setOnClickListener(v -> clearEditText());

        findViewById(R.id.button_undo).setOnClickListener(v -> clearLastSymbol());

        findViewById(R.id.button_percent).setOnClickListener(v -> getPercentFromLastNumber());

        findViewById(R.id.button_result).setOnClickListener(v -> getResult());
    }

    private void getResult() {
    }

    private void getPercentFromLastNumber() {
    }

    private void clearLastSymbol() {
        TextView textView = findViewById(R.id.text_view);
        StringBuilder string = new StringBuilder(textView.getText());
        if (string.length() > 0) {
            string.deleteCharAt(string.length()-1);
        }
        textView.setText(string.toString());
    }

    private void clearEditText() {
        TextView textView = findViewById(R.id.text_view);
        textView.setText("");
    }

    @SuppressLint("SetTextI18n")
    private void showSymbol(Button button) {
        TextView textView = findViewById(R.id.text_view);
        textView.setText(textView.getText()+button.getText().toString());
    }
    @SuppressLint("SetTextI18n")
    private void showNumber(Double digit) {
        TextView textView = findViewById(R.id.text_view);
        //todo если до этого ввели число, выводить ошибку
        textView.setText(textView.getText()+digit.toString());
    }

}