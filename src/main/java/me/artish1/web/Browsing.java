package me.artish1.web;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Browsing {
    private Browsing() {
    }

    /**
     * Opens the web browser of the desktop and redirects to the given URI
     *
     * @param uri The URI to to redirect the web browser to.
     * @return True if the task completed. False otherwise.
     */
    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Uses #openWebPage(URI) method but transforms URL to URI
     *
     * @param url The URL to transform to URI.
     * @return True if the task completed. False otherwise.
     */
    public static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Uses #openWebPage(URL) method but transforms the String
     * to a URL and takes care of exception handling
     *
     * @param url The String to transform to URL
     * @return True if the task completed. False otherwise.
     */
    public static boolean openWebpage(String url) {
        try {
            URL u = new URL(url);

            return openWebpage(u.toURI());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
