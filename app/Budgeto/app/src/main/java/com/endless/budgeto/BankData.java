package com.endless.budgeto;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Eric on 2016-09-08.
 * TODO: Change for a better storing solution
 */
public class BankData {

    public static String bank = "Tangerine";
    public static String url = "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogin&device=web&locale=fr_CA";
    public static List<String> tangerineCalls = Arrays.asList(
            "input = $('#ACN'); input.val('%s'); input.closest('form').submit();", // {username}
            "$(\"div.content-main-wrapper .CB_DoNotShow:first\").html()",
            "input = $('#Answer'); input.val('%s'); input.closest('form').submit();"); // {answer}

}
