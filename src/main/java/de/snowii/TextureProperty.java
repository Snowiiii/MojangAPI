package de.snowii;

public class TextureProperty {
    private final String name;
    private final String value;
    private final String signature;

    public TextureProperty(final String name, final String value, final String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getSignature() {
        return this.signature;
    }
}
