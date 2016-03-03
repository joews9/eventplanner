package com.event.joe.servicepackage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Joe Aimee and Baby on 18/02/2016.
 */
public class ImageDownloadService extends AbstractService{
    private String external;
    private static String internal;

    public ImageDownloadService(String external, String internal) {
        this.external = external;
        this.internal = internal;
    }

    @Override
    public void run() {
        FileOutputStream outStream = null;
        BufferedInputStream inStream = null;

        try {
            HttpURLConnection ucon = createConnection(external);
            File file = new File(internal);
            file.getParentFile().mkdirs();

            ucon.setReadTimeout(0);
            ucon.setConnectTimeout(120000);

            InputStream is = ucon.getInputStream();
            inStream = new BufferedInputStream(is, 1024 * 5);
            outStream = new FileOutputStream(file);
            byte[] buff = new byte[5 * 1024];

            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }
            outStream.flush();
            super.serviceCallComplete(false);
        } catch (Exception e) {
            super.serviceCallComplete(true);
        } finally {
            if (outStream != null && inStream != null) {
                try {
                    outStream.close();
                    inStream.close();
                } catch (IOException ex) {
                }
            }
        }

    }

    private HttpURLConnection createConnection(String urlString)
            throws MalformedURLException, IOException {

        URL url = new URL(urlString);
        HttpURLConnection conn = null;
        boolean defaultRedirect = HttpURLConnection.getFollowRedirects();

        HttpURLConnection.setFollowRedirects(false);
        if (url.getProtocol().equals("https")) {
            conn = (HttpURLConnection) url.openConnection();
                    } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.connect();

        String redirectTo = conn.getHeaderField("Location");
        if (redirectTo != null && redirectTo.length() > 0) {
            conn.disconnect();
            conn = createConnection(redirectTo);
        }

        HttpURLConnection.setFollowRedirects(defaultRedirect);
        return conn;
    }

    public String getExternal() {
        return external;
    }

    public static String getLocal() {
        return internal;
    }
}
