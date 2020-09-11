package com.ewheelers.ewheelers.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionPreference {
    public static String tokenvalue = "token";
    public static String username = "username";
    public static String userid = "userid";
    public static String businessname = "businessname";
    public static String personname = "personname";
    public static String mobileno = "mobileno";
    public static String addressone = "addressone";
    public static String addresstwo = "addresstwo";

    //account setup
    public static String name = "fullname";
    public static String mobile_no = "phoneno";
    public static String dobs = "dob";
    public static String organise = "organise";
    public static String profileIs = "brief";
    public static String cityname = "city";
    public static String cityid = "cityid";
    public static String stateid = "stateid";
    public static String contryid = "contryid";

    //status
    public static String bankstatus = "BankSubmited";
    public static String accountsetup = "SetupSubmited";
    public static String partnerattributes = "AttributesSubmited";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStrings(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

    public static void clearString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, null);
        editor.apply();
    }
}
