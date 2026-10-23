package com.tumbuhkembanganak.config;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // nama sharepreference
    private static final String PREF_NAME = "OrangTua";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void changeNama(String nama)
    {
        editor.putString(Config.TAG_NAMA, nama);
        editor.commit();
    }

    public HashMap<String, String> getDetailOrtu(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Config.TAG_NAMA, pref.getString(Config.TAG_NAMA, null));
        return user;
    }
}
