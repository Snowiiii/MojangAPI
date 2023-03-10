package de.snowii.parse;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class MojangJSONParser {
    private static final Gson GSON = new Gson();

    private static Proxy proxy;

    public static @Nullable JsonObject parseURL(final @NotNull String purl) {
        try {
            final URL url = new URL(purl);
            final HttpURLConnection connection = (HttpURLConnection) (proxy != null ? url.openConnection(proxy) : url.openConnection());
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IllegalStateException("Failed to do a HTTP request to: " + purl + " Response code: " + connection.getResponseCode());
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.US_ASCII))) {
                String inputLine;
                final StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                return GSON.fromJson(sb.toString(), JsonObject.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URLConnection openConnection(URL url) throws IOException {
       return  (proxy != null ? url.openConnection(proxy) : url.openConnection());
    }

    public static JsonElement parseConnection(URLConnection connection)
            throws IOException {
        try (InputStream input = connection.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return JsonParser.parseReader(bufferedReader);
        }
    }

    public static void setProxy(Proxy proxy) {
        MojangJSONParser.proxy = proxy;
    }
}
