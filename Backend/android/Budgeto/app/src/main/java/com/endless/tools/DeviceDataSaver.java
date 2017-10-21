package com.endless.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.endless.bank.Transaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Eric on 2016-10-21.
 */

public class DeviceDataSaver {
    private static final String FILEKEYNAME = "budgetoData";
    private static final String TRANSACTIONS_KEY = "transactions";
    private static final String PIN_KEY = "pin";

    private final Context context;
    private final SharedPreferences sharedPrefs;
    private final Gson gson;

    public DeviceDataSaver(Context context) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(FILEKEYNAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    private void saveObject(String key, Object object) {
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        String json = gson.toJson(object);
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }

    private Object retrieveObject(String key, Type type) {
        String json = sharedPrefs.getString(key, "");
        return gson.fromJson(json, type);
    }

    public void savePIN(int PIN) {
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        prefsEditor.putInt(PIN_KEY, PIN);
        prefsEditor.apply();
    }

    public int retrievePIN() {
        return sharedPrefs.getInt(PIN_KEY, -1);
    }

    public void saveTransactionsList(List<Transaction> transactions) {
        saveObject(TRANSACTIONS_KEY, transactions);
    }

    public List<Transaction> retrieveTransactionsList() {
        Type listType = new TypeToken<List<Transaction>>(){}.getType();
        return (List<Transaction>) retrieveObject(TRANSACTIONS_KEY, listType);
    }
}
