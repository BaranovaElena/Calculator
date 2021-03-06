package com.example.calculator;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

enum Sign {
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE
}

public class Calculator  implements Parcelable {
    private final int ROUND_NUM = 3;
    private Context context;
    private StringBuilder textField;
    private Double number1;
    private Double number2;
    private Sign operation;
    private Double result;


    Calculator(Context context) {
        textField = new StringBuilder();
        number1 = 0d;
        number2 = 0d;
        result = 0d;
        this.context = context;
    }


    protected Calculator(Parcel in) {
        if (in.readByte() == 0) {
            textField = new StringBuilder();
        } else {
            textField = new StringBuilder(in.readString());
        }
        if (in.readByte() == 0) {
            number1 = null;
        } else {
            number1 = in.readDouble();
        }
        if (in.readByte() == 0) {
            number2 = null;
        } else {
            number2 = in.readDouble();
        }
        if (in.readByte() == 0) {
            result = null;
        } else {
            result = in.readDouble();
        }
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
        dest.writeString(textField.toString());
        dest.writeDouble(number1);
        dest.writeDouble(number2);
        dest.writeDouble(result);
    }


    public void setTextField(StringBuilder textField) {
        this.textField = textField;
    }

    public StringBuilder getTextField() {
        return textField;
    }

    public boolean isLastSymbolNumber() {
        String lastSymbol = "";
        if (textField.length() > 0) {
            lastSymbol = textField.substring(textField.length() - 1);
        }
        return (isNumber(lastSymbol)
                || lastSymbol.equals(context.getString(R.string.dot)));
    }

    public boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }

    }

    public boolean isLastSymbolSign() {
        String lastSymbol = "";
        if (textField.length() > 0) {
            lastSymbol = textField.substring(textField.length() - 1);
        }

        return isSign(lastSymbol);
    }

    private boolean isSign(String s) {
        return (s.equals(context.getString(R.string.plus))
                || s.equals(context.getString(R.string.minus))
                || s.equals(context.getString(R.string.multiply))
                || s.equals(context.getString(R.string.divide)));
    }

    public void getPercentFromLastNumber() {
        String lastNumberStr = getLastNumber();
        if (!lastNumberStr.isEmpty()) {
            try {
                double lastNumber = Double.parseDouble(lastNumberStr);

                textField.delete(textField.length()-lastNumberStr.length(), textField.length());
                textField.append(round(lastNumber/100d));
            }
            catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    private double round(double value) {
//        BigDecimal a = new BigDecimal(value);
//        BigDecimal roundOff = a.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
//        return roundOff.doubleValue();
        double scale = Math.pow(10, ROUND_NUM);
        return result = Math.ceil(value * scale) / scale;
    }

    private String getLastNumber() {
        int pos = textField.length()-1;
        boolean endNumber = false;

        while (!endNumber) {
            char lastSymbol = textField.charAt(pos);
            if (isNumber(String.valueOf(lastSymbol))
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

    public Double getResult() throws ArithmeticException {
        boolean number1Exists = false;
        boolean resultExists = false;
        int pos = 0;
        StringBuilder buf = new StringBuilder();

        while (!resultExists) {
            char s = textField.charAt(pos);
            //?????????????? ???????????? - ?????????? ?????? ??????????
            if (isNumber(String.valueOf(s))
                    || String.valueOf(s).equals(context.getString(R.string.dot))) {
                buf.append(s);
                pos++;
            }
            //?????????????? ???????????? - ???????? ????????????????
            else if (isSign(String.valueOf(s))) {
                //???????????????????? 1-?? ??????????????
                if (!number1Exists) {
                    if (buf.length() == 0) {
                        number1 = 0d;
                    }
                    else {
                        number1 = Double.parseDouble(buf.toString());
                    }
                    number1Exists = true;
                }
                //?????????? ?????????????????????????? ?????????????????? ?? 1-?? ??????????????
                else {
                    if (buf.length() == 0) {
                        number2 = 0d;
                    }
                    else {
                        number2 = Double.parseDouble(buf.toString());
                    }
                    number1 = count();
                    number2 = 0d;
                }
                buf = new StringBuilder();
                rememberSign(s);
                pos++;
            }
            //?????????????? ???????????? =
            else if (String.valueOf(s).equals(context.getString(R.string.result))) {
                //???????? ???? ????????  1-???? ????????????????, ?????????????????? - 1-?? ??????????????
                if (!number1Exists) {
                    if (buf.length() == 0) {
                        result = 0d;
                    }
                    else {
                        result = Double.parseDouble(buf.toString());
                    }
                }
                //?????????????? ??????????????????
                else {
                    //???????? ?????? 2-???? ????????????????, ?????????????????? - 1-?? ??????????????
                    if (buf.length() == 0) {
                        result = number1;
                    }
                    else {
                        number2 = Double.parseDouble(buf.toString());
                        result = count();
                    }
                }
                resultExists = true;
                buf = new StringBuilder();
                number1 = 0d;
                number2 = 0d;
            }
        }
        return result;
    }

    private void rememberSign(char s) {
        if (String.valueOf(s).equals(context.getString(R.string.plus))) {
            operation = Sign.PLUS;
        }
        else if (String.valueOf(s).equals(context.getString(R.string.minus))) {
            operation = Sign.MINUS;
        }
        else if (String.valueOf(s).equals(context.getString(R.string.multiply))) {
            operation = Sign.MULTIPLY;
        }
        else if (String.valueOf(s).equals(context.getString(R.string.divide))) {
            operation = Sign.DIVIDE;
        }
    }

    private Double count() throws ArithmeticException {
        switch (operation) {
            case PLUS:
                result = round(number1+number2);
                break;
            case MINUS:
                result = round(number1-number2);
                break;
            case MULTIPLY:
                result = round(number1*number2);
                break;
            case DIVIDE:
                if (Math.abs(number2 - 0) < 0.0001) {
                    throw new ArithmeticException();
                }
                else {
                    result = round(number1/number2);
                }
                break;
        }
        return result;
    }

    public boolean isLastSymbolEqualSign() {
        String lastSymbol = "";
        if (textField.length() > 0) {
            lastSymbol = textField.substring(textField.length() - 1);
        }

        return lastSymbol.equals(context.getString(R.string.result));
    }

    public boolean isLastNumberDotted() {
        String lastNumberStr = getLastNumber();
        return lastNumberStr.contains(context.getString(R.string.dot));
    }
}
