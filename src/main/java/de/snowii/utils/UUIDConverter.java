package de.snowii.utils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.regex.Pattern;

public class UUIDConverter {
    private static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
    public static UUID fromStringWithoutDashes(final @NotNull String uuid) {
        String correctedUUID = UUID_PATTERN.matcher(uuid).replaceAll("$1-$2-$3-$4-$5");
        return UUID.fromString(correctedUUID);
    }
}
