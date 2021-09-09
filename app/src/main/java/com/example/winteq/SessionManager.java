package com.example.winteq;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.winteq.model.user.UserData;

import java.util.HashMap;

public class SessionManager {

    private Context _context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";

    public SessionManager (Context context){
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(UserData user){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(USER_ID, user.getUserId());
        editor.putString(USERNAME, user.getUsername());
        editor.putString(FULLNAME, user.getFullname());
        editor.putString(EMAIL, user.getEmail());
        editor.commit();
    }

    //ambil user detail
    public HashMap<String,String> getUserDetail(){
        HashMap<String,String> user = new HashMap<>();
        user.put(USER_ID, sharedPreferences.getString(USER_ID, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(FULLNAME, sharedPreferences.getString(FULLNAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        return user;
    }

//    public void logoutSession(){
//        editor.clear();
//        editor.commit();
//    }
//
//    public boolean isLoggedIn(){
//        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
//    }

}
