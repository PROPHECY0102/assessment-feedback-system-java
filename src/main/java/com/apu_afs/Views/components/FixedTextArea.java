package com.apu_afs.Views.components;

import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class FixedTextArea {
    
    public static JTextArea createFixedTextArea(int rows, int cols, int maxChars) {
        JTextArea textArea = new JTextArea(rows, cols);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);
        
        // Add document filter to limit characters
        ((AbstractDocument) textArea.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= maxChars) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                int currentLength = fb.getDocument().getLength();
                int newLength = currentLength - length + (text != null ? text.length() : 0);
                if (newLength <= maxChars) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        
        return textArea;
    }
}
