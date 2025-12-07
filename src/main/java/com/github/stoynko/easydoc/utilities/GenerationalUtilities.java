package com.github.stoynko.easydoc.utilities;

import com.github.stoynko.easydoc.user.model.User;
import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class GenerationalUtilities {

    private static final int DIGITS_COUNT = 10;

    public static String normalizeEmailAddress(String emailAddress) {
        return emailAddress.trim().toLowerCase(Locale.ROOT);
    }

    public static String extractName(User user) {
        String first = user.getFirstName() != null ? user.getFirstName().trim() : "";
        String last  = user.getLastName()  != null ? user.getLastName().trim()  : "";

        String fullName = (first + " " + last).trim();
        if (fullName.isEmpty()) {
            fullName = "Not Provided";
        }
        return fullName;
    }

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
