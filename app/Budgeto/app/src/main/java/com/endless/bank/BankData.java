package com.endless.bank;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Eric on 2016-09-08.
 * TODO: Change for a better storing solution
 */
public class BankData {

    public static String bank = "Tangerine";
    public static String tangerineLogin = "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogin&device=web&locale=fr_CA";
    public static String tangerineLogout = "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogout&device=web&locale=fr_CA";

    public static List<String> tangerineCalls = Arrays.asList(
            "input = $('#ACN'); input.val('%s'); input.closest('form').submit();", // {username}
            "$(\"div.content-main-wrapper .CB_DoNotShow:first\").html()",
            "input = $('#Answer'); input.val('%s'); input.closest('form').submit();", // {answer}
            "input = $('#PIN'); input.val('%s'); input.closest('form').submit()", // {password}
            "https://secure.tangerine.ca/web/Tangerine.html?command=goToCreditCardAccount&creditCardAccount=0",
            "$(\"table[data-target='#transactionTable'] tbody\").html()");

}
