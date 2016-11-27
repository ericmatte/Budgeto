package com.endless.bank;

import android.webkit.WebView;

import com.endless.tools.Logger;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import jodd.jerry.Jerry;

import static jodd.jerry.Jerry.jerry;

/**
 * Created by Eric on 2016-09-09.
 */
public class Desjardins extends BankScraper {

    private String baseUrl = "https://accesd.mouv.desjardins.com/sommaire-perso/sommaire/";
    private List<String> accountUrls = new ArrayList<>();
    private int accountUrlsIndex = 0;

    public Desjardins(WebView webView) {
        super(webView);
        this.bank = Bank.Desjardins;
        this.loginUrl = "https://accweb.mouv.desjardins.com/identifiantunique/identification";
    }

    @Override
    public void nextCall(String url, Document documentHTML) {
        // TODO: This code is break.
        String response = "";
        Logger.print(this.getClass(), url, "nextCall url");

        if (url.startsWith(baseUrl + "detention")) {
            // connected, getting to credit card
            if (response == null) {
                dialog.hide();
                getDocumentHTML(url);
            } else if (response.startsWith("<head>")) {
                // extracting data
                Jerry doc = jerry(response);
                String relativeUrl = doc.$("#produitCompte0 a").attr("href");
                accountUrls.add(relativeUrl);
                loadUrl(baseUrl + relativeUrl);
            }

        } else if (accountUrls.size() > 0 && url.contains(accountUrls.get(accountUrlsIndex))) {
            // on the transaction page
            if (response == null) {
                getDocumentHTML(url);
            } else if (response.startsWith("<head>")) {
                // extracting data
                Jerry table = jerry(response);
                table = table.$("tbody");

                BankResponse bankResponse = new BankResponse(bank);
                List<Transaction> transactions = new ArrayList<>();
                for (Jerry tr : table.$("tr")) {
                    Jerry column = tr.$("*");
                    String date = column.get(0).getInnerHtml();
                    String desc = column.get(1).getInnerHtml();
                    String amount = column.get(2).getInnerHtml();
                    amount = amount.equals("") ? amount = column.get(3).getInnerHtml() : "-" + amount;

                    bankResponse.addTransaction(date, desc, amount);
                }

                bankCallable.callBack(bankResponse.finalizeResponse());
                sendJavascript("$(\"#btn-deconnexion\").click()");
            }
        } else if (url.contains("autologout")) {
            Logger.print(this.getClass(), "Operations successfully completed for " + String.valueOf(bank));
            loadUrl("about:blank");
        } else {
//            Logger.print(this.getClass(), url, "User tried to ge else where! Canceling...");
//            if (!url.startsWith("https://secure.tangerine.ca/web/Tangerine.html") &&
//                    !url.startsWith("https://secure.tangerine.ca/web/InitialTangerine.html")) {
//                dialog.cancel();
//
//                if (!url.equals("about:blank")) loadUrl("about:blank");
//            }
        }
    }
}
