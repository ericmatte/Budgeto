package com.endless.activities.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.endless.bank.BankScraper;
import com.endless.bank.Categorizer;
import com.endless.bank.Tangerine;
import com.endless.budgeto.R;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // bank.requestTransactions();

        showCategories();
    }


    public void addBill(View view) {
        Tangerine b = (Tangerine) bank;
        b.logout();
    }


    private JSONObject bankResponse = tempCreateJson();

    public void showCategories() {
        try {
            List<JSONObject> cats = Categorizer.listerize(Categorizer.categorize(bankResponse.getJSONArray("transactions")));
            ListAdapter adapter = new CategoryAdapter(this, cats);
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject tempCreateJson() {
        try {
            return new JSONObject("{  \n" +
                    "   \"bank\":\"Tangerine\",\n" +
                    "   \"transactions\":[  \n" +
                    "      {  \n" +
                    "         \"uuid\":\"543ca339-db1b-3ef0-86d0-c28f2d4514d4\",\n" +
                    "         \"date\":\"13-09-2016\",\n" +
                    "         \"desc\":\"U d s centre culturel sherbrooke qc\",\n" +
                    "         \"cat\":\"Entertainment\",\n" +
                    "         \"amount\":\"45,00\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"a1c205ef-1a9e-3621-9dc2-075463825c92\",\n" +
                    "         \"date\":\"12-09-2016\",\n" +
                    "         \"desc\":\"Paiement - merci\",\n" +
                    "         \"cat\":\"Null\",\n" +
                    "         \"amount\":\"-336,56\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"c7c02eaf-7bda-305d-a3a8-2cc0cb360859\",\n" +
                    "         \"date\":\"10-09-2016\",\n" +
                    "         \"desc\":\"Yuzu sherbrooke sherbrooke qc\",\n" +
                    "         \"cat\":\"Restaurants\",\n" +
                    "         \"amount\":\"40,36\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"3f06c72f-274f-3807-bc52-83add021b3e4\",\n" +
                    "         \"date\":\"10-09-2016\",\n" +
                    "         \"desc\":\"Depanneur dunant sherbrooke qc\",\n" +
                    "         \"cat\":\"Others\",\n" +
                    "         \"amount\":\"36,41\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"d01b6762-0e79-386a-b871-c362e659ec82\",\n" +
                    "         \"date\":\"10-09-2016\",\n" +
                    "         \"desc\":\"Provigo sherbrooke #84 sherbrooke qc\",\n" +
                    "         \"cat\":\"Groceries\",\n" +
                    "         \"amount\":\"31,82\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"bb91ac9c-354a-3644-8e22-899a27d99033\",\n" +
                    "         \"date\":\"09-09-2016\",\n" +
                    "         \"desc\":\"Brullerie de cafe de s sherbrooke qc\",\n" +
                    "         \"cat\":\"Restaurants\",\n" +
                    "         \"amount\":\"4,55\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"2f343818-41b8-3e58-814d-1759e728ff1f\",\n" +
                    "         \"date\":\"07-09-2016\",\n" +
                    "         \"desc\":\"Relais pneus mecanique sherbrooke qc\",\n" +
                    "         \"cat\":\"Others\",\n" +
                    "         \"amount\":\"121,39\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"e57c5b87-d2f6-39e2-8ee5-312821774fe5\",\n" +
                    "         \"date\":\"04-09-2016\",\n" +
                    "         \"desc\":\"Paiement - merci\",\n" +
                    "         \"cat\":\"Null\",\n" +
                    "         \"amount\":\"-337,34\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"b4c1017b-25c2-38fb-b08a-aa3a002dfeb8\",\n" +
                    "         \"date\":\"04-09-2016\",\n" +
                    "         \"desc\":\"Beavertix 888-8378604 qc\",\n" +
                    "         \"cat\":\"Entertainment\",\n" +
                    "         \"amount\":\"26,25\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"db78b53a-5b68-3ef8-9f5c-a1f659a33ca4\",\n" +
                    "         \"date\":\"03-09-2016\",\n" +
                    "         \"desc\":\"Maxi\",\n" +
                    "         \"cat\":\"Groceries\",\n" +
                    "         \"amount\":\"20,89\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"c0520c84-7262-3426-8745-da4599f3d1a5\",\n" +
                    "         \"date\":\"02-09-2016\",\n" +
                    "         \"desc\":\"Paypal *netlinkcomp 4029357733 bc\",\n" +
                    "         \"cat\":\"Others\",\n" +
                    "         \"amount\":\"54,89\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"5b4a1e70-253a-39e5-839d-61153f1a89ab\",\n" +
                    "         \"date\":\"31-08-2016\",\n" +
                    "         \"desc\":\"Ultramar #26295 sherbrooke qc\",\n" +
                    "         \"cat\":\"Gas\",\n" +
                    "         \"amount\":\"33,77\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"f95ed804-ebd8-3506-800e-7f30b858da70\",\n" +
                    "         \"date\":\"31-08-2016\",\n" +
                    "         \"desc\":\"Maxi\",\n" +
                    "         \"cat\":\"Groceries\",\n" +
                    "         \"amount\":\"7,88\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"e0163da1-b61d-3aff-81a8-744472152692\",\n" +
                    "         \"date\":\"29-08-2016\",\n" +
                    "         \"desc\":\"Mag express magog qc\",\n" +
                    "         \"cat\":\"Others\",\n" +
                    "         \"amount\":\"172,47\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"202dd521-72dc-34e9-a75b-014a90e8f4e4\",\n" +
                    "         \"date\":\"28-08-2016\",\n" +
                    "         \"desc\":\"Pharmaprix #0009 sherbrooke qc\",\n" +
                    "         \"cat\":\"Drugstore\",\n" +
                    "         \"amount\":\"3,44\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"9e05c98a-13df-3fe9-8588-ec51c15346f7\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"A30 express salaberry-de- qc\",\n" +
                    "         \"cat\":\"Parking\",\n" +
                    "         \"amount\":\"2,50\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"128c3b2a-fc0f-3b22-8a63-cd907e4cc3ca\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Louis luncheonette sherbrooke qc\",\n" +
                    "         \"cat\":\"Restaurants\",\n" +
                    "         \"amount\":\"10,98\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"71907563-d999-33df-9ad2-62aea5f7e202\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Maxi\",\n" +
                    "         \"cat\":\"Groceries\",\n" +
                    "         \"amount\":\"24,87\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"f047a4c6-6b78-3796-b173-73c23887e28e\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Esso grenville qc\",\n" +
                    "         \"cat\":\"Gas\",\n" +
                    "         \"amount\":\"22,29\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"d6491193-e3c6-3dcd-b80f-c1570b06653f\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Magasin cdn tire #0009 sherbrooke qc\",\n" +
                    "         \"cat\":\"Improvement\",\n" +
                    "         \"amount\":\"12,69\"\n" +
                    "      },\n" +
                    "      {  \n" +
                    "         \"uuid\":\"f812f6b0-0281-3657-b630-b69bbfe7fcc5\",\n" +
                    "         \"date\":\"27-08-2016\",\n" +
                    "         \"desc\":\"Magasin cdn tire #0009 sherbrooke qc\",\n" +
                    "         \"cat\":\"Improvement\",\n" +
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
