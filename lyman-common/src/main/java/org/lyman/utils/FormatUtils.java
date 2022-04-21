package org.lyman.utils;


import org.lyman.enums.MobilNumerRegion;

public class FormatUtils {

    public static boolean validPhoneNumber(String number) {
        return validPhoneNumber(number, null);
    }

    public static boolean validPhoneNumber(String number, MobilNumerRegion region) {
        if (StringUtils.isEmpty(number))
            return false;
        if (region == null)
            region = MobilNumerRegion.CHINA;
        return number.matches(region.regex());
    }

    public static boolean validEmail(String email) {
        if (StringUtils.isEmpty(email))
            return false;
        return email.matches("");
    }

}
