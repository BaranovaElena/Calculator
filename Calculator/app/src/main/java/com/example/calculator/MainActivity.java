package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ThemeConstants {
    private final Double PI = 3.1415;
    public final String CALC_KEY = "calculator_key";
    private final String SHARED_PREF_KEY = "main_activity";
    private final String TOGGLE_THEME_KEY = "toggle_theme";
    private final int SETTINGS_REQUEST_CODE = 111;

    private Calculator calculator;
    private boolean nightTheme;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        if (savedInstanceState != null && savedInstanceState.containsKey(CALC_KEY)) {
            calculator = savedInstanceState.getParcelable(CALC_KEY);
            updateState();
        } else {
            calculator = new Calculator(getApplicationContext());
        }
        setButtonsListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //ставим сохраненную тему
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        nightTheme = sharedPref.getBoolean(TOGGLE_THEME_KEY, false);
        if (nightTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            //обрабатываем полученную от настроек тему
            nightTheme = data.getBooleanExtra(NEW_THEME_EXTRA_KEY,false);
            setAndSaveTheme();
        }
    }

    private void setAndSaveTheme() {
        if (nightTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(TOGGLE_THEME_KEY, nightTheme);
        editor.apply();
    }

    private void updateState() {
        textView.setText(calculator.getTextField());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CALC_KEY, calculator);
        super.onSaveInstanceState(outState);
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

        findViewById(R.id.button_pi).setOnClickListener(v -> showNumber(PI));

        Button buttonDot = findViewById(R.id.button_dot);
        buttonDot.setOnClickListener(v -> showDot(buttonDot));

        Button buttonPlus = findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> enterSign(buttonPlus));

        Button buttonMinus = findViewById(R.id.button_minus);
        buttonMinus.setOnClickListener(v -> enterSign(buttonMinus));

        Button buttonMultiply = findViewById(R.id.button_multiply);
        buttonMultiply.setOnClickListener(v -> enterSign(buttonMultiply));

        Button buttonDivide = findViewById(R.id.button_divide);
        buttonDivide.setOnClickListener(v -> enterSign(buttonDivide));

        findViewById(R.id.button_clear).setOnClickListener(v -> clearEditText());

        findViewById(R.id.button_undo).setOnClickListener(v -> clearLastSymbol());

        findViewById(R.id.button_percent).setOnClickListener(v -> showPercentFromLastNumber());

        findViewById(R.id.button_result).setOnClickListener(v -> showResult());

        findViewById(R.id.button_settings).setOnClickListener(v -> {
            //открываем окно настроек, передавая текущаю тему
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            settings.putExtra(CURRENT_THEME_EXTRA_KEY, nightTheme);
            startActivityForResult(settings, SETTINGS_REQUEST_CODE);
        });
    }

    private void showDot(Button button) {
        if (!calculator.isLastNumberDotted()) {
            showSymbol(button);
        }
    }

    private void enterSign(Button button) {
        if (calculator.isLastSymbolSign()) {
            clearLastSymbol();
        }
        showSymbol(button);
    }

    private void showResult() {
        if (!textView.getText().equals("")) {
            if (!calculator.isLastSymbolEqualSign()) {
                showSymbol(findViewById(R.id.button_result));
            }
            try {
                Double result = calculator.getResult();
                textView.setText(String.format("%s%s", textView.getText(), result.toString()));
                calculator.setTextField(new StringBuilder(textView.getText()));
            } catch (ArithmeticException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.division_by_zero), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showPercentFromLastNumber() {
        calculator.getPercentFromLastNumber();

        textView.setText(calculator.getTextField());
    }

    private void clearLastSymbol() {
        String string = textView.getText().toString();
        if (string.length() > 0) {
            string = string.substring(0,string.length()-1);
        }
        textView.setText(string);
        calculator.setTextField(new StringBuilder(string));
    }

    private void clearEditText() {
        textView.setText("");
        calculator.setTextField(new StringBuilder());
    }

    private void showSymbol(Button button) {
        textView.setText(String.format("%s%s", textView.getText(), button.getText().toString()));
        calculator.setTextField(new StringBuilder(textView.getText()));
    }

    private void showNumber(Double digit) {
        if (calculator.isLastSymbolNumber()) {
            Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
        } else {
            textView.setText(String.format("%s%s", textView.getText(), digit.toString()));
            calculator.setTextField(new StringBuilder(textView.getText()));
        }
    }

}