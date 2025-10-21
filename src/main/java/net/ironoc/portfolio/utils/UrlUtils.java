package net.ironoc.portfolio.utils;

import module java.base;

import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.stereotype.Component;

@Component
public class UrlUtils extends AbstractLogger {

    public boolean isValidURL(String urlString) {
        try {
            URL url = URI.create(urlString).toURL();
            url.toURI();
            return true;
        } catch (Exception e) {
            error("Unexpected exception occurred.", e);
            return false;
        }
    }
}
