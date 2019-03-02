package com.technologies.francesco.xplotter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.lang.reflect.Method;
import java.util.ArrayList;

import formulaCalc.Calculator;
import formulaCalc.Operator;

/**
 * Input text view with autocompletion by word.
 *
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class PlotterInputTextView extends AutoCompleteTextView {

    public PlotterInputTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSingleLine();
        setupAutoComplete();
    }

    public PlotterInputTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        setupAutoComplete();
    }

    public PlotterInputTextView(Context context) {
        super(context);
        setSingleLine();
        setupAutoComplete();
    }

    private static String[] kKeywords;
    private int lastWordStartIndex;
    private int lastCursorPosition;

    /**
     * Setup autocomplete.
     */
    protected void setupAutoComplete() {
        kKeywords = Calculator.getFunctionsName();
        for(int i=0; i<kKeywords.length; i++){
            kKeywords[i] = kKeywords[i]+'(';
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_dropdown_item_1line, kKeywords);
        setAdapter(arrayAdapter);
        setThreshold(1);
    }

    protected void resetText() {
        setText("", BufferType.EDITABLE);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        // Detect the beginning of the last word in the line.
        Log.i("filtering", "starting");
        lastCursorPosition = getSelectionEnd();
        lastWordStartIndex = 0;
        for (int i = lastCursorPosition-1; i >= 0; i--) {
            char c = text.charAt(i);
            if (c == ' ' ||
                    c == '(' ||
                    c == ')' ||
                    c == '+' ||
                    c == '-' ||
                    c == '*' ||
                    c == '/' ||
                    c == '^') {
                lastWordStartIndex = i+1;
                break;
            }
        }

        // Extract last word.
        CharSequence lastWord = null;
        lastWord = text.subSequence(lastWordStartIndex, lastCursorPosition);

        // Perform filtering if last word length is bigger than filtering threshold.
        if (lastWord.length() >= this.getThreshold()) {
            super.performFiltering(lastWord, keyCode);
        }
    }

    @Override
    protected void replaceText(CharSequence text) {
        CharSequence currentText = getText();

        // Replace currently auto-completed word.
        CharSequence textKeptOnLeft = currentText.subSequence(0, lastWordStartIndex);
        CharSequence textKeptOnRight = currentText.subSequence(lastCursorPosition, currentText.length());
        super.replaceText(textKeptOnLeft + text.toString() + textKeptOnRight);

        // Set selection just after replaced word.
        setSelection(lastWordStartIndex + text.length());
    }

}