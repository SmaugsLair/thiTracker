package com.smaugslair.thitracker.ui.powers.transformers;

public class TransformerException extends Exception {

    public TransformerException(String s, Throwable t) {
        super(s, t);
    }

    public TransformerException(String s) {
        super(s);
    }
}
