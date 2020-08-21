package com.grocery.gtohome.session;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grocery.gtohome.activity.Splash_Activity;
import com.grocery.gtohome.model.category_model.CategoryName;
import com.grocery.gtohome.model.login_model.CustomerInfo;
import com.grocery.gtohome.model.register_model.RegistrationModelData;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by Raghvendra sahu on 08-024-2020.
 */
public class SessionManager extends Object {
    private static final String TAG = SessionManager.class.getSimpleName();
    private static final String PREF_NAME = "Gtohome_pref";

    private static final String IS_LOGGEDIN = "isLoggedIn";
    private static final String Name = "name";
    private static final String Date = "date";
    private static final String City = "city";
    private static final String Zip = "Zip";
    private static final String Area = "Area";
    private static final String Age = "Age";
    private static final String ProfileUrl = "ProfileUrl";
    private static final String Image = "image";
    private static final String Mobile = "mobile";
    private static final String Email = "email";
    private static final String Gender = "gender";
    private static final String Token_Id = "token";
    private static final String Device_Id = "deviceId";
    private static final String Current_Position = "current_position";
    private Context _context;
    private SharedPreferences mypref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this._context = context;
        mypref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = mypref.edit();
        editor.apply();

    }

    public void createSession(CustomerInfo userInfoData) {
        Gson gson = new Gson();
        String json = gson.toJson(userInfoData);
        editor.putString("user", json);
        editor.putBoolean("IsLogin", true);
        editor.apply();
    }

    public CustomerInfo getUser() {
        Gson gson = new Gson();
        String str = mypref.getString("user", "");
        if (str.isEmpty())
            return null;
        return gson.fromJson(str, CustomerInfo.class);
    }


    public void setCategoryData(List<CategoryName> categoryList) {
        Gson gson = new Gson();
        String json = gson.toJson(categoryList);
        editor.putString(TAG, json);
        editor.commit();
        editor.apply();
    }

    public List<CategoryName> getCategoryData() {
        Gson gson = new Gson();
        String str = mypref.getString(TAG, "");
        if (str.isEmpty())
            return null;
        Type type = new TypeToken<List<CategoryName>>() {}.getType();
        List<CategoryName> arrayList = gson.fromJson(str, type);
        return arrayList;
    }

    public void setCurrent_Position(Integer name) {
        editor.putInt(Current_Position, name);
        editor.apply();
        editor.commit();
    }

    public Integer getCurrent_Position() {
        return mypref.getInt(Current_Position, 0);

    }
public void setName(String name) {
        editor.putString(Name, name);
        editor.apply();
        editor.commit();
    }

    public String getName() {
        return mypref.getString(Name, "");

    }


    public void setGender(String gender) {
        editor.putString(Gender, gender);
        editor.apply();
        editor.commit();
    }

    public String getGender() {
        return mypref.getString(Gender, "");

    }


    public void setDate(String date) {
        editor.putString(Date, date);
        editor.apply();
        editor.commit();
    }

    public String getDate() {
        return mypref.getString(Date, "");
    }


    public void setCity(String city) {
        editor.putString(City, city);
        editor.apply();
        editor.commit();
    }

    public String getCity() {
        return mypref.getString(City, "");
    }


    public void setZip(String zip) {
        editor.putString(Zip, zip);
        editor.apply();
        editor.commit();
    }

    public String getZip() {
        return mypref.getString(Zip, "");
    }


    public void setArea(String area) {
        editor.putString(Area, area);
        editor.apply();
        editor.commit();
    }

    public String getArea() {
        return mypref.getString(Area, "");
    }


    public void setAge(String age) {
        editor.putString(Age, age);
        editor.apply();
        editor.commit();
    }

    public String getAge() {
        return mypref.getString(Age, "");
    }


    public void setProfileUrl(String profileUrl) {
        editor.putString(ProfileUrl, profileUrl);
        editor.apply();
        editor.commit();
    }

    public String getProfileUrl() {
        return mypref.getString(ProfileUrl, "");
    }


    public void setImage(String image) {
        editor.putString(Image, String.valueOf(image));
        editor.apply();
        editor.commit();
    }

    public String getImage() {
        return mypref.getString(Image, "");

    }

    public void setMobile(String mobile, String email) {

        editor.putString(Mobile, mobile);
        editor.putString(Email, email);
        editor.apply();
        editor.commit();
    }


    public String getMobile() {
        return mypref.getString(Mobile, "");

    }

    public String getEmail() {
        return mypref.getString(Email, "");

    }


    public void saveToken(String token) {
        editor.putString(Token_Id, token);
        editor.apply();
        editor.commit();
    }

    public void saveDeviceId(String token) {
        editor.putString(Device_Id, token);
        editor.apply();
        editor.commit();
    }

    public String getTokenId() {
        return mypref.getString(Token_Id, "");
    }

    public String getDeviceId() {
        return mypref.getString(Device_Id, "");
    }

    public void logout() {
        editor.clear();
        editor.apply();
        Intent showLogin = new Intent(_context, Splash_Activity.class);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(showLogin);
    }

    public boolean isLoggedIn() {
        return mypref.getBoolean("IsLogin", false);
    }
}