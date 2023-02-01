package de.snowii;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.snowii.parse.MojangJSONParser;
import de.snowii.utils.NameAvailability;
import de.snowii.utils.UUIDConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("unused")
public class MojangAPI {
    private static final Map<UUID, GameProfile> cachedUUIDProfiles = new HashMap<>();
    private static final Map<String, GameProfile> cachedNameProfiles = new HashMap<>();

    private static final Map<String, NameAvailability> cachedNameAvailabilities = new HashMap<>();


    public static @Nullable GameProfile getGameProfile(final @NotNull UUID uuid, final boolean cache) {
        if (cachedUUIDProfiles.containsKey(uuid)) return cachedUUIDProfiles.get(uuid);
        JsonObject object = MojangJSONParser.parseURL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
        if (object == null) return null;
        GameProfile gameProfile = new GameProfile(object.get("name").getAsString(), uuid);
        gameProfile.setTextureProperties(parseTextureProperties(object));
        if (cache) cachedUUIDProfiles.put(uuid, gameProfile);
        return gameProfile;
    }

    public static @Nullable GameProfile getGameProfile(final @NotNull String name, final boolean cache) {
        if (cachedNameProfiles.containsKey(name)) return cachedNameProfiles.get(name);
        JsonObject object = MojangJSONParser.parseURL("https://api.mojang.com/users/profiles/minecraft/" + name + "?unsigned=false");
        if (object == null) return null;
        GameProfile gameProfile = new GameProfile(name, UUIDConverter.fromStringWithoutDashes(object.get("id").getAsString()));
        gameProfile.setTextureProperties(parseTextureProperties(object));
        if (cache) cachedNameProfiles.put(name, gameProfile);
        return gameProfile;
    }

    private static List<TextureProperty> parseTextureProperties(final JsonObject object) {
        List<TextureProperty> textureProperties = new ArrayList<>();
        JsonArray jsonProperties = object.get("properties").getAsJsonArray();
        for (JsonElement element : jsonProperties) {
            JsonObject property = element.getAsJsonObject();

            String name = property.get("name").getAsString();
            String value = property.get("value").getAsString();
            String signature = null;
            if (property.has("signature")) {
                signature = property.get("signature").getAsString();
            }
            textureProperties.add(new TextureProperty(name, value, signature));
        }
        return textureProperties;
    }


    /**
     * <a href="https://wiki.vg/Mojang_API#Name_Availability">Name Availability</a>
     *
     * @return NameAvailability Enum
     */
    public static @Nullable NameAvailability getNameAvailability(final @NotNull String name, final boolean cache) {
        if (cachedNameAvailabilities.containsKey(name)) return cachedNameAvailabilities.get(name);
        JsonObject object = MojangJSONParser.parseURL("https://api.minecraftservices.com/minecraft/profile/" + name + "/available");
        if (object == null) return null;
        NameAvailability availability = NameAvailability.valueOf(object.get("status").getAsString());
        if (cache) cachedNameAvailabilities.put(name, availability);
        return availability;
    }

    // Microsoft

    /**
     * <a href="https://wiki.vg/Microsoft_Authentication_Scheme#Obtain_XSTS_token_for_Minecraft">Obtain XSTS token for Minecraft</a>
     *
     * @return XBL Token
     */
    public static XBoxLiveToken getXBLToken(final @NotNull URL xbl_token_url, final @NotNull String msftAccessToken)
            throws LoginException {
        JsonObject properties = new JsonObject();
        properties.addProperty("AuthMethod", "RPS");
        properties.addProperty("SiteName", "user.auth.xboxlive.com");
        properties.addProperty("RpsTicket", msftAccessToken);

        JsonObject postData = new JsonObject();
        postData.addProperty("RelyingParty", "http://auth.xboxlive.com");
        postData.addProperty("TokenType", "JWT");
        postData.add("Properties", properties);

        String request = postData.toString();

        try {
            URLConnection connection = MojangJSONParser.openConnection(xbl_token_url);

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);

            try (OutputStream out = connection.getOutputStream()) {
                out.write(request.getBytes(StandardCharsets.US_ASCII));
            }

            JsonObject json = MojangJSONParser.parseConnection(connection).getAsJsonObject();

            String token = json.get("Token").getAsString();
            String userhash = json.getAsJsonObject("DisplayClaims").getAsJsonArray("xui").get(0).getAsJsonObject().get("userhash").getAsString();

            return new XBoxLiveToken(token, userhash);

        } catch (IOException e) {
            throw new LoginException("Connection failed: " + e);

        }
    }

    /**
     * <a href="https://wiki.vg/Microsoft_Authentication_Scheme#Authenticate_with_Minecraft">Authenticate with Minecraft</a>
     *
     * @return Minecraft Access Token
     */
    public static String getMinecraftAccessToken(final @NotNull String userhash, final @NotNull String xstsToken)
            throws LoginException {
        JsonObject postData = new JsonObject();
        postData.addProperty("identityToken",
                "XBL3.0 x=" + userhash + ";" + xstsToken);

        String request = postData.toString();

        try {
            URLConnection connection = MojangJSONParser.openConnection(new URL("https://api.minecraftservices.com/authentication/login_with_xbox"));

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);

            try (OutputStream out = connection.getOutputStream()) {
                out.write(request.getBytes(StandardCharsets.US_ASCII));
            }

            JsonObject json = MojangJSONParser.parseConnection(connection).getAsJsonObject();
            return json.get("access_token").getAsString();

        } catch (IOException e) {
            throw new LoginException("Connection failed: " + e);
        }
    }

    public record XBoxLiveToken(String token, String userhash) {

    }


    public static void clearCache() {
        cachedUUIDProfiles.clear();
        cachedNameProfiles.clear();
        cachedNameAvailabilities.clear();
    }


}