package com.endless.bank;

import com.endless.tools.Sanitizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Eric on 2016-09-11.
 */
public class Transaction {

    private String date, desc, cat, amount;

    public void setDate(String date) { this.date = Sanitizer.clean(date); }
    public void setDesc(String desc) { this.desc = Sanitizer.clean(desc); }
    public void setCat(String cat) { this.cat = Sanitizer.clean(cat); }
    public void setAmount(String amount) { this.amount = Sanitizer.clean(amount); }

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
