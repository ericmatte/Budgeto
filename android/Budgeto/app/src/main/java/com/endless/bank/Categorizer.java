package com.endless.bank;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Categorize transactions using JSON
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class Categorizer {
    
//  {  \n" +
//     \"uuid\":\"543ca339-db1b-3ef0-86d0-c28f2d4514d4\",\n" +
//     \"date\":\"13-09-2016\",\n" +
//     \"desc\":\"U d s centre culturel sherbrooke qc\",\n" +
//     \"cat\":\"Entertainment\",\n" +
//     \"amount\":\"45,00\"\n" +
//  },\n"


    public static JSONObject categorize(JSONArray transactions) {
        JSONObject categories = new JSONObject();

        try {
            for (int i = 0; i < transactions.length(); i++) {
                JSONObject transaction = transactions.getJSONObject(i);
                String cat = transaction.getString("cat");
                transaction.remove("cat");

                JSONArray catTrans;
                if (!categories.has(cat)) {
                    catTrans = new JSONArray();
                }
                else {
                    catTrans = (JSONArray) categories.get(cat);
                }
                catTrans.put(transaction);
                categories.put(cat, catTrans);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public static List<JSONObject> listerize(JSONObject categories) {
        List<JSONObject> cats = new ArrayList<>();

        try {
            Iterator<?> keys = categories.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                JSONArray catTrans = (JSONArray) categories.get(key);

                JSONObject cat = new JSONObject();
                cat.put("cat", key);
                cat.put("trans", catTrans);

                cats.add(cat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cats;
    }

}
