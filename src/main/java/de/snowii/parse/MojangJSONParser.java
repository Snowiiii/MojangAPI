package de.snowii.parse;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class MojangJSONParser {
    private static HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private static final Gson GSON = new Gson();

    public static @Nullable JsonObject parseURL(final @NotNull String purl) {
        try {
            URI uri = new URI(purl);
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IllegalStateException("Failed to do an HTTP request to: " + purl + " Response code: " + response.statusCode());
            }

            String responseBody = response.body();
            return JsonParser.parseString(responseBody).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URLConnection openConnection(URL url) throws IOException {
       return url.openConnection(); // TODO
    }

    public static JsonElement parseConnection(URLConnection connection)
            throws IOException {
        try (InputStream input = connection.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return JsonParser.parseReader(bufferedReader);
        }
    }

    public static void setProxy(ProxySelector proxy) {
        HTTP_CLIENT = HttpClient.newBuilder().proxy(proxy).build();
    }
}
