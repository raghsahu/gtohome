package com.grocery.gtohome.utils;

import java.util.regex.Pattern;

/**
 * Created by Raghvendra Sahu on 20-Apr-20.
 */
public interface AppConstant {

    /**
     * Validation ragular expression
     */
    Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("^([a-zA-Z0-9._-]+)@{1}(([a-zA-Z0-9_-]{1,67})|([a-zA-Z0-9-]+\\.[a-zA-Z0-9-]{1,67}))\\.(([a-zA-Z0-9]{2,6})(\\.[a-zA-Z0-9]{2,6})?)$");
    Pattern MOBILE_NUMBER_PATTERN = Pattern.compile("^[0-9]{8,14}$");

    String PayUMoney_MerchentKey="HBzjus8Q";
    String PayUMoney_SaltKey="4YxPZIqeAl";
    String PayUMoney_MerchentId="5733850";
}
