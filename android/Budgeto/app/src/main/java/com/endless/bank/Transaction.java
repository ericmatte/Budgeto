package com.endless.bank;

import com.endless.tools.Sanitizer;
import com.endless.bank.BankScraper.Bank;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Eric on 2016-09-11.
 */
public class Transaction {
    private Bank bank;
    private String date, desc, amount, cat;

    public Transaction(Bank bank, String date, String desc, String amount, String cat) {
        this.bank = bank;
        setDate(date);
        setDesc(desc);
        setAmount(amount);
        setCat(cat);
    }

    private void setDate(String date) { this.date = Sanitizer.clean(date); }
    private void setDesc(String desc) { this.desc = Sanitizer.clean(desc); }
    private void setAmount(String amount) { this.amount = Sanitizer.clean(amount); }
    // Category can be set later
    public void setCat(String cat) { this.cat = Sanitizer.clean(cat); }

    public String getCat() { return cat; }
    public String getDesc() { return desc; }
    public String getAmount() { return amount; }

    private String getTransactionUuid() {
        String s = date + desc + amount + bank;
        return UUID.nameUUIDFromBytes(s.getBytes()).toString();
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uuid", getTransactionUuid());
            obj.put("date", date);
            obj.put("desc", desc);
            obj.put("cat", cat);
            obj.put("amount", amount);
            obj.put("bank", bank);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
