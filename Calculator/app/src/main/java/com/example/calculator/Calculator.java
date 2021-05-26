package com.example.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

enum Sign {
    plus,
    minus,
    multiply,
    divide
}

public class Calculator  implements Parcelable {
    private Context context;
    private StringBuilder textField;
    private Double number1;
    private Double number2;
    private Sign operation;
    private Double result;

    protected Calculator(Parcel in) {
    }

    Calculator(Context context) {
        textField = new StringBuilder();
        number1 = 0d;
        number2 = 0d;
        result = 0d;
        this.context = context;
    }

    public static final Creator<Calculator> CREATOR = new Creator<Calculator>() {
        @Override
        public Calculator createFromParcel(Parcel in) {
            return new Calculator(in);
        }

        @Override
        public Calculator[] newArray(int size) {
            return new Calculator[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public void setTextField(StringBuilder textField) {
        this.textField = textField;
    }

    public StringBuilder getTextField() {
        return textField;
    }

    public boolean isLastSymbolNumber() {
        StringBuilder lastSymbol = new StringBuilder();
        if (textField.length() > 0) {
            lastSymbol.append(textField.substring(textField.length() - 1));
        }
        return (isDigit(lastSymbol.toString())
                || lastSymbol.toString().equals(context.getString(R.string.dot)));
    }

    public boolean isDigit(String s) {
        return (s.equals(context.getString(R.string._0)) || s.equals(context.getString(R.string._1))
             || s.equals(context.getString(R.string._2)) || s.equals(context.getString(R.string._3))
             || s.equals(context.getString(R.string._4)) || s.equals(context.getString(R.string._5))
             || s.equals(context.getString(R.string._6)) || s.equals(context.getString(R.string._7))
             || s.equals(context.getString(R.string._8)) || s.equals(context.getString(R.string._9)));
    }

    public boolean isLastSymbolSign() {
        System.out.println(context.getString(R.string.divide));
        StringBuilder lastSymbol = new StringBuilder();
        if (textField.length() > 0) {
            lastSymbol.append(textField.substring(textField.length() - 1));
        }

        return isSign(lastSymbol.toString());
    }

    private boolean isSign(String s) {
        return (s.equals(context.getString(R.string.plus))
                || s.equals(context.getString(R.string.minus))
                || s.equals(context.getString(R.string.multiply))
                || s.equals(context.getString(R.string.divide)));
    }

    @SuppressLint("DefaultLocale")
    public void getPercentFromLastNumber() {
        String lastNumberStr = getLastNumber();
        if (!lastNumberStr.isEmpty()) {
            try {
                double lastNumber = Double.parseDouble(lastNumberStr);

                textField.delete(textField.length()-lastNumberStr.length(), textField.length());
                textField.append(round(lastNumber/100d, 3));
            }
            catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    private double round(double value, int num) {
//        BigDecimal a = new BigDecimal(value);
//        BigDecimal roundOff = a.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
//        return roundOff.doubleValue();
        double scale = Math.pow(10, num);
        return result = Math.ceil(value * scale) / scale;
    }

    private String getLastNumber() {
        int pos = textField.length()-1;
        boolean endNumber = false;

        while (!endNumber) {
            char lastSymbol = textField.charAt(pos);
            if (isDigit(String.valueOf(lastSymbol))
                    || String.valueOf(lastSymbol).equals(context.getString(R.string.dot))) {
                if (pos>0) {
                    pos--;
                }
                else {
                    endNumber = true;
                }
            }
            else {
                endNumber = true;
                pos++;
            }
        }

        return textField.substring(pos);
    }
}
