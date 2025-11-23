package com.github.stoynko.easydoc.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GenerationalUtilities {

    private static final int DIGITS_COUNT = 9;

    public static String extractDigits(String uuid) {

        if (uuid == null) {
            return "";
        }

        StringBuilder digits = new StringBuilder(DIGITS_COUNT);

        int index = 0;

        while (digits.length() < DIGITS_COUNT) {
            if (index < uuid.length()) {
                char character = uuid.charAt(index );
                if (Character.isDigit(character)) {
                    digits.append(character);
                }
                index++;
            } else {
                digits.append('0');
            }
        }

        return digits.toString();
    }
}
