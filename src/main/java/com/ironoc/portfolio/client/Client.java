package com.ironoc.portfolio.client;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;

public interface Client {

    HttpsURLConnection createConn(String url) throws IOException;

    InputStream readInputStream(HttpsURLConnection conn) throws IOException;

    void closeConn(InputStream inputStream) throws IOException;
}
