package com.ftec.exceptions;

public class TwoStepVerificationException extends Exception {
    public TwoStepVerificationException(String wrong2faCode) {
        super(wrong2faCode);
    }
}
