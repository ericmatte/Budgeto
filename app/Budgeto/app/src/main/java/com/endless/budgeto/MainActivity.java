package com.endless.budgeto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.endless.bank.BankScraper;
import com.endless.bank.Tangerine;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BankScraper bank;
    Map<String, String> userInfo = new HashMap<String, String>();
    WebView webView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userInfo.put("username", extras.getString("username"));
            userInfo.put("password", extras.getString("password"));
        }

        webView = (WebView) findViewById(R.id.webView);
        bank = new Tangerine(webView, this, userInfo);
        //bank.requestTransactions();

        showCategories();
    }


    public void addBill(View view) {
        Tangerine b = (Tangerine) bank;
        b.logout();
    }


    private JSONObject bankResponse = tempCreateJson();

    public void showCategories() {
        Set<String> cats = getCategories();
        List<String> c = new ArrayList<>();
        c.addAll(cats);
        ListAdapter adapter = new CustomAdapter(this, c);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        //showCategories();
    }

    private Set<String> getCategories() {
        Set<String> categories = new HashSet<String>();
        try {
            JSONArray transactions = bankResponse.getJSONArray("transactions");
            for (int i = 0; i < transactions.length(); i++) {
                JSONObject transaction = transactions.getJSONObject(i);
                categories.add(transaction.getString("cat"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return categories;
    }


    private JSONObject tempCreateJson() {
        try {
            return new JSONObject("{\n" +
                    "   \"bank\":\"Tangerine\",\n" +
                    "   \"transactions\":[\n" +
                    "      {\n" +
                    "         \"uuid\":\"93935f2c-c229-3cf8-957d-eef7078591d8\",\n" +
                    "         \"date\":\"10-09-2016\",\n" +
                    "         \"desc\":\"Yuzu sherbrooke sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-restaurants\",\n" +
                    "         \"amount\":\"40,36\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"d8cac272-97bd-34c5-a44a-856fba5bed84\",\n" +
                    "         \"date\":\"10-09-2016\",\n" +
                    "         \"desc\":\"Depanneur dunant sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-others\",\n" +
                    "         \"amount\":\"36,41\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"b8c5994b-fcd4-308e-8917-38dded3aebb2\",\n" +
                    "         \"date\":\"10-09-2016\",\n" +
                    "         \"desc\":\"Provigo sherbrooke #84 sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-groceries\",\n" +
                    "         \"amount\":\"31,82\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"545bfa2b-3b4b-3799-9d13-683af492f916\",\n" +
                    "         \"date\":\"09-09-2016\",\n" +
                    "         \"desc\":\"Brullerie de cafe de s sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-restaurants\",\n" +
                    "         \"amount\":\"4,55\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"751645c9-e7a0-3866-94b7-4ee80f0f4794\",\n" +
                    "         \"date\":\"07-09-2016\",\n" +
                    "         \"desc\":\"Relais pneus mecanique sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-others\",\n" +
                    "         \"amount\":\"121,39\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"3e321b02-2c20-3cd6-9a35-36ba880ab271\",\n" +
                    "         \"date\":\"04-09-2016\",\n" +
                    "         \"desc\":\"Paiement - merci\",\n" +
                    "         \"cat\":\"\",\n" +
                    "         \"amount\":\"-337,34\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"ecf5b952-f0b3-31af-a7f3-d991c05a2fba\",\n" +
                    "         \"date\":\"04-09-2016\",\n" +
                    "         \"desc\":\"Beavertix 888-8378604 qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-entertainment\",\n" +
                    "         \"amount\":\"26,25\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"03cabb02-5646-38a9-9c3b-cea69d0c2cd5\",\n" +
                    "         \"date\":\"03-09-2016\",\n" +
                    "         \"desc\":\"Maxi\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-groceries\",\n" +
                    "         \"amount\":\"20,89\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"0f6d8e01-21cf-3283-86c4-e3a964e7ee4a\",\n" +
                    "         \"date\":\"02-09-2016\",\n" +
                    "         \"desc\":\"Paypal *netlinkcomp 4029357733 bc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-others\",\n" +
                    "         \"amount\":\"54,89\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"428d93e0-570f-36a6-a55d-b77dad3172a1\",\n" +
                    "         \"date\":\"31-08-2016\",\n" +
                    "         \"desc\":\"Ultramar #26295 sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-gas\",\n" +
                    "         \"amount\":\"33,77\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"3840f86f-bb56-3a06-ad53-8656d679c381\",\n" +
                    "         \"date\":\"31-08-2016\",\n" +
                    "         \"desc\":\"Maxi\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-groceries\",\n" +
                    "         \"amount\":\"7,88\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"ac45ec28-9225-36f3-aa05-54c2724c39fd\",\n" +
                    "         \"date\":\"29-08-2016\",\n" +
                    "         \"desc\":\"Mag express magog qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-others\",\n" +
                    "         \"amount\":\"172,47\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"2273b1a9-7c3a-30b1-8e21-8563582b775d\",\n" +
                    "         \"date\":\"28-08-2016\",\n" +
                    "         \"desc\":\"Pharmaprix #0009 sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-drugstore\",\n" +
                    "         \"amount\":\"3,44\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"0af24a34-3e18-3d7c-8159-58ec746f0cda\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"A30 express salaberry-de- qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-parking\",\n" +
                    "         \"amount\":\"2,50\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"ca004021-406a-31fc-ae92-5a701562ff56\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Louis luncheonette sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-restaurants\",\n" +
                    "         \"amount\":\"10,98\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"7ecd09bb-3453-37fd-b0ec-e8281ff94960\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Maxi\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-groceries\",\n" +
                    "         \"amount\":\"24,87\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"179d2f43-5351-32c6-9103-226aff0d4e61\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Esso grenville qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-gas\",\n" +
                    "         \"amount\":\"22,29\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"21ad70a1-f4da-3b43-97b2-daffe06a79b6\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Magasin cdn tire #0009 sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-improvement\",\n" +
                    "         \"amount\":\"12,69\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"uuid\":\"cfd9bac5-ef5f-3933-9c17-f06a890ba46d\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Magasin cdn tire #0009 sherbrooke qc\",\n" +
                    "         \"cat\":\"Tan-icon-cc-cat-improvement\",\n" +
                    "         \"amount\":\"4,13\"\n" +
                    "      }\n" +
                    "   ]\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
