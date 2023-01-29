package de.snowii;

import java.util.List;
import java.util.UUID;

public class GameProfile {
    private final String name;
    private final UUID uuid;
    private List<TextureProperty> textureProperties;

    public GameProfile(final String name, final UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setTextureProperties(List<TextureProperty> textureProperties) {
        this.textureProperties = textureProperties;
    }

    public List<TextureProperty> getTextureProperties() {
        return textureProperties;
    }
}
