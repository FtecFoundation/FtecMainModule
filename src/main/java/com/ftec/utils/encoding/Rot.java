package com.ftec.utils.encoding;

import java.util.ArrayList;

public class Rot {
    private ArrayList<String> letters;
    private ArrayList<String> encryptedLetters;

    public Rot() {
        letters = new ArrayList<String>();
        encryptedLetters = new ArrayList<String>();
    }

    /**
     * Encrypt text
     *
     * @param message      Message to encrypt
     * @param rotN         Rot number
     * @param encryptDigit True or False
     * @return encrypted message
     */
    public String encrypt(String message, int rotN, boolean encryptDigit) {
        StringBuilder out = new StringBuilder();
        char encryptedCurrent;
        char current;
        int typeAlpha;

        //clear arrays
        letters.clear();
        encryptedLetters.clear();

        for (int i = 0; i < message.length(); i++) {
            current = message.charAt(i);
            if ((typeAlpha = typeAlpha(current)) != 0) {
                encryptedCurrent = (char) (current + rotN);
                if (typeAlpha == 1 && encryptedCurrent > 'z' || typeAlpha == 2 && encryptedCurrent > 'Z')
                    encryptedCurrent -= 'z' - 'a' + 1;
            } else if (encryptDigit && isNumeric(current)) {
                encryptedCurrent = (char) (current + rotN % 10);
                if (encryptedCurrent > '9')
                    encryptedCurrent -= '9' - '0' + 1;
            } else {
                encryptedCurrent = current;
            }

            if ((typeAlpha != 0 || (isNumeric(current) && encryptDigit)) && letters.indexOf(current + "") == -1) {
                letters.add(current + "");
                encryptedLetters.add(encryptedCurrent + "");
            }

            out.append(encryptedCurrent);
        }
        return out.toString();
    }

    /**
     * Decrypt text
     *
     * @param message      Message to decrypt
     * @param rotN         Rot number
     * @param decryptDigit True or False
     * @return decrypted message
     */
    public String decrypt(String message, int rotN, boolean decryptDigit) {

        StringBuilder out = new StringBuilder();
        char encryptedCurrent;
        char current;
        int typeAlpha;

        //clear arrays
        letters.clear();
        encryptedLetters.clear();

        for (int i = 0; i < message.length(); i++) {
            current = message.charAt(i);
            if ((typeAlpha = typeAlpha(current)) != 0) {
                encryptedCurrent = (char) (current - rotN);
                if (typeAlpha == 1 && encryptedCurrent < 'a' || typeAlpha == 2 && encryptedCurrent < 'A')
                    encryptedCurrent += 'z' - 'a' + 1;
            } else if (decryptDigit && isNumeric(current)) {
                encryptedCurrent = (char) (current - rotN % 10);
                if (encryptedCurrent < '0')
                    encryptedCurrent += '9' - '0' + 1;
            } else {
                encryptedCurrent = current;
            }

            if ((typeAlpha != 0 || (isNumeric(current) && decryptDigit)) && letters.indexOf(current + "") == -1) {
                letters.add(current + "");
                encryptedLetters.add(encryptedCurrent + "");
            }

            out.append(encryptedCurrent);
        }
        return out.toString();
    }

    /**
     * Check if the character is alphabetic
     *
     * @param c Character
     * @return 1 => Lowercase, 2 => Uppercase, 3 => Not alpha
     */
    private int typeAlpha(char c) {
        if (c >= 'a' && c <= 'z')
            return 1;
        if (c >= 'A' && c <= 'Z')
            return 2;
        return 0;
    }

    /**
     * Check if character is a number
     *
     * @param c Character
     * @return true or false
     */
    private boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }
}
