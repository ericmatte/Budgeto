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

    public static String transTest = "\n" +
            "<tr class=\"recent\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">07-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">08-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">RELAIS PNEUS MECANIQUE SHERBROOKE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"Z\"><i class=\"tan-icon-cc-cat-others\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">121,39&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">1,21&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">04-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">06-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">PAIEMENT - MERCI\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">-337,34&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">04-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">06-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">BEAVERTIX 888-8378604 QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"E\"><i class=\"tan-icon-cc-cat-entertainment\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">26,25&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,26&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent               userSelected\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">03-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">06-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">MAXI &amp; CIE #8703 SHERBROOKE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"O\"><i class=\"tan-icon-cc-cat-groceries\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">20,89&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,42&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">02-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">06-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">PAYPAL *NETLINKCOMP 4029357733 BC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"Z\"><i class=\"tan-icon-cc-cat-others\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">54,89&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,55&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent               userSelected\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">31-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">01-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">ULTRAMAR #26295 SHERBROOKE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"G\"><i class=\"tan-icon-cc-cat-gas\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">33,77&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,68&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent               userSelected\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">31-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">01-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">MAXI &amp; CIE #8703 SHERBROOKE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"O\"><i class=\"tan-icon-cc-cat-groceries\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">7,88&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,16&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">29-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">02-09-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">MAG EXPRESS MAGOG QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"Z\"><i class=\"tan-icon-cc-cat-others\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">172,47&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">1,72&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">28-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">29-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">PHARMAPRIX #0009 SHERBROOKE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"D\"><i class=\"tan-icon-cc-cat-drugstore\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">3,44&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,03&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent\" data-page=\"1\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">27-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">29-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">A30 EXPRESS SALABERRY-DE- QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"P\"><i class=\"tan-icon-cc-cat-parking\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">2,50&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,03&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent               userSelected hidden\" data-page=\"2\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">27-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">29-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">LOUIS LUNCHEONETTE SHERBROOKE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"R\"><i class=\"tan-icon-restaurants\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">10,98&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,22&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent               userSelected hidden\" data-page=\"2\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">27-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">29-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">MAXI &amp; CIE #8703 SHERBROOKE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"O\"><i class=\"tan-icon-cc-cat-groceries\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">24,87&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,50&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent               userSelected hidden\" data-page=\"2\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">27-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">30-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">ESSO GRENVILLE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"G\"><i class=\"tan-icon-cc-cat-gas\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">22,29&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,45&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent                hidden\" data-page=\"2\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">27-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">30-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">MAGASIN CDN TIRE #0009 SHERBROOKE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"I\"><i class=\"tan-icon-cc-cat-improvement\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">12,69&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,13&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n" +
            "<tr class=\"recent                hidden\" data-page=\"2\">\n" +
            "<td class=\"tr-date\" data-title=\"Date de l'opération\">27-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td data-title=\"Date d'affichage\" class=\"tr-postedDate\">30-08-2016&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-desc tbl-cell-block\" data-title=\"Description\">MAGASIN CDN TIRE #0009 SHERBROOKE QC\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-icon\" data-title=\"Category\" data-category=\"I\"><i class=\"tan-icon-cc-cat-improvement\"></i>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t  &nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-amount text-right\" data-title=\"Amount\">4,13&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td><td class=\"tr-earned text-right\" data-title=\"Rewards&nbsp;earned\">0,04&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t  </td>\n" +
            "</tr>\n";

}
