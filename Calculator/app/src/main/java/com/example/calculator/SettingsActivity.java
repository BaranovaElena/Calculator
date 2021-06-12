package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatToggleButton;

import android.content.Intent;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity implements ThemeConstants {
    private AppCompatToggleButton toggleTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toggleTheme = findViewById(R.id.toggle_theme);

        findViewById(R.id.button_close_settings).setOnClickListener(v -> closeActivity());
    }

    private void closeActivity() {
        //закрываем с передачей новой темы
        Intent intentResult = new Intent();
        intentResult.putExtra(NEW_THEME_EXTRA_KEY, toggleTheme.isChecked());
        setResult(RESULT_OK, intentResult);

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //значение текущей темы получаем при открытии от main activity
        boolean nightTheme = getIntent().getExtras().getBoolean(CURRENT_THEME_EXTRA_KEY);
        toggleTheme.setChecked(nightTheme);
    }
}

interface ThemeConstants {
    String CURRENT_THEME_EXTRA_KEY = "CURRENT_THEME_EXTRA_KEY";
    String NEW_THEME_EXTRA_KEY = "NEW_THEME_EXTRA_KEY";
}