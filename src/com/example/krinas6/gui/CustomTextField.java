package com.example.krinas6.gui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

class CustomTextField extends JTextField {

    public enum PATTERNS {
        ONLY_DIGITS, DOUBLES, NAMES;
    }


    CustomTextField(int columns, PATTERNS pattern) {
        super(columns);
        this.setDocument(createDefaultModel(pattern));
    }


    private Document createDefaultModel(PATTERNS pattern) {
        return new NumericDocument(pattern);
    }

    String getFormatted() {
        return this.getText().strip();
    }

    int getInt() {
        String stringToBeParsed = this.getText().strip();
        return Integer.parseInt(stringToBeParsed);
    }

    double getDouble() {
        String stringToBeParsed = this.getText().strip();
        if (stringToBeParsed.contains(",")) {
            stringToBeParsed = stringToBeParsed.replaceFirst(",", ".");
        }
        BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(stringToBeParsed));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
        return bigDecimal.doubleValue();
    }


    private class NumericDocument extends PlainDocument {
        // The regular expression to match input against strings not containing numbers


        private final Pattern thePattern;

        NumericDocument(PATTERNS pattern) {
            switch (pattern) {
                case ONLY_DIGITS:
                    thePattern = Pattern.compile("\\d*");
                    break;
                case DOUBLES:
                    thePattern = Pattern.compile("[0-9]*[.,]?[0-9]{0,2}");
                    break;
                case NAMES:
                    thePattern = Pattern.compile("[^0-9]");
                    break;
                default:
                    thePattern = Pattern.compile("");
            }
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            // Only insert the text if it matches the regular expression
            if (str != null && thePattern.matcher(str).matches())
                super.insertString(offs, str, a);
        }
    }
}
