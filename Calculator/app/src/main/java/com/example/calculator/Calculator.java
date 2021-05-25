package com.example.calculator;

import android.os.Parcel;
import android.os.Parcelable;

public class Calculator  implements Parcelable {
    private StringBuilder textField;
    private Double result;

    protected Calculator(Parcel in) {
    }
    Calculator() {
        textField = new StringBuilder();
        result = 0d;
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

    public boolean isLastSymbolDigit() {
        StringBuilder lastSymbol = new StringBuilder();
        if (textField.length() > 0) {
            lastSymbol.append(textField.substring(textField.length() - 1));
        }
        return isDigit(lastSymbol.toString());
    }

    public boolean isDigit(String s) {
        return (s.equals("0") || s.equals("1") || s.equals("2") || s.equals("3")
                || s.equals("4") || s.equals("5") || s.equals("6") || s.equals("7")
                || s.equals("8") || s.equals("9"));
    }

}
