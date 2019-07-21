package me.artish1.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class Scraper {

    private Scraper() {
    }

    /**
     * Attempts to download the html file of the given URL
     *
     * @param URL String format of the URL to download
     * @return HtmlPage object that contains data from the URL given.
     */
    public static HtmlPage getPage(String URL) {
        try (WebClient wc = new WebClient()) {
            wc.getOptions().setCssEnabled(false);
            wc.getOptions().setJavaScriptEnabled(false);
            return wc.getPage(URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
