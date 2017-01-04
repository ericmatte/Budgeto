package com.endless.bank;

import com.endless.tools.Sanitizer;
import com.endless.bank.BankScraper.Bank;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent a bank transaction
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class Transaction {
    private Bank bank;
    private String date, desc, cat;
    private float amount;

    public Transaction(Bank bank, String date, String desc, String amount, String cat) {
        this.bank = bank;
        setDate(date);
        setDesc(desc);
        setAmount(amount);
        if (cat != null) setCat(cat);
    }

    private void setDate(String date) { this.date = Sanitizer.stringDateFormat(date); }
    private void setDesc(String desc) { this.desc = Sanitizer.cleanString(desc); }
    private void setAmount(String amount) { this.amount = Sanitizer.stringToFloat(amount); }
    // Category can be set later
    public void setCat(String cat) { this.cat = Sanitizer.cleanString(cat); }

    public String getCat() { return cat; }
    public String getDate() { return date; }
    public String getDesc() { return desc; }
    public float getAmount() { return amount; }

//    private String getTransactionUuid() {
//        String s = date + desc + amount + bank;
//        return UUID.nameUUIDFromBytes(s.getBytes()).toString();
//    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("date", date);
            obj.put("desc", desc);
            obj.put("cat", cat);
            obj.put("amount", String.valueOf(amount));
            obj.put("bank", bank);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
