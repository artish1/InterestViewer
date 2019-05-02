package me.artish1.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class Scraper {

    private Scraper(){}


    public static HtmlPage getPage(String URL){
        try(WebClient wc = new WebClient()){
            wc.getOptions().setCssEnabled(false);
            wc.getOptions().setJavaScriptEnabled(false);
            return wc.getPage(URL);
        }catch(IOException e){
            e.printStackTrace();
        }
       return null;
    }


}
