package de.snowii;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.snowii.parse.MojangJSONParser;
import de.snowii.utils.NameAvailability;
import de.snowii.utils.UUIDConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    public static @Nullable NameAvailability getNameAvailability(final @NotNull String name, final boolean cache) {
        if (cachedNameAvailabilities.containsKey(name)) return cachedNameAvailabilities.get(name);
        JsonObject object = MojangJSONParser.parseURL("https://api.minecraftservices.com/minecraft/profile/" + name + "/available");
        if (object == null) return null;
        NameAvailability availability = NameAvailability.valueOf(object.get("status").getAsString());
        if (cache) cachedNameAvailabilities.put(name, availability);
        return availability;
    }


    public static void clearCache() {
        cachedUUIDProfiles.clear();
        cachedNameProfiles.clear();
        cachedNameAvailabilities.clear();
    }


}