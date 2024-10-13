package net.ironoc.portfolio.utils;

import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class UrlUtils extends AbstractLogger {

    public boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            error("Unexpected exception occurred.", e);
            return false;
        }
    }
}
