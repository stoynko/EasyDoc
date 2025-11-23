package com.github.stoynko.easydoc.utilities;

import com.github.stoynko.easydoc.models.User;
import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class ValidationUtilities {

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

/* TODO: DELETE IF NOT USED

    public static boolean hasText(String string) {
        return string != null && !string.trim().isBlank();
    }

    public static boolean isBlank(String string) {
        return string == null || string.trim().isBlank();
    }
*/

}
