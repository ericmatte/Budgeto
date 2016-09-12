package com.endless.bank;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Eric on 2016-09-11.
 */
public class Transaction {

    // Clean and trim a value
    public static String clean(String value) {
        if (value == null)
            return "";

        if (value.contains("\n"))
            value = value.substring(0, value.indexOf('\n'));
        if (value.contains("&"))
            value = value.substring(0, value.indexOf('&'));
        value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
        value = value.trim();

        return value;
    }

    private String date, desc, cat, amount;

    public void setDate(String date) { this.date = clean(date); }
    public void setDesc(String desc) { this.desc = clean(desc); }
    public void setCat(String cat) { this.cat = clean(cat); }
    public void setAmount(String amount) { this.amount = clean(amount); }

    private String generateUuid() {
        String s = date + desc + cat + amount;
        return UUID.nameUUIDFromBytes(s.getBytes()).toString();
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uuid", generateUuid());
            obj.put("date", date);
            obj.put("desc", desc);
            obj.put("cat", cat);
            obj.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
