package dev.barroit.specialsource.enumerate;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

@Accessors(fluent = true, makeFinal = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum MinecraftVersion {
    v1_8("1.8-R0.1-SNAPSHOT"),
    v1_8_3("1.8.3-R0.1-SNAPSHOT"),
    v1_8_8("1.8.8-R0.1-SNAPSHOT"),

    v1_9("1.9-R0.1-SNAPSHOT"),
    v1_9_2("1.9.2-R0.1-SNAPSHOT"),
    v1_9_4("1.9.4-R0.1-SNAPSHOT"),

    v1_10_2("1.10.2-R0.1-SNAPSHOT"),

    v1_11("1.11-R0.1-SNAPSHOT"),
    v1_11_2("1.11.2-R0.1-SNAPSHOT"),

    v1_12("1.12-R0.1-SNAPSHOT"),
    v1_12_1("1.12.1-R0.1-SNAPSHOT"),
    v1_12_2("1.12.2-R0.1-SNAPSHOT"),

    v1_13("1.13-R0.1-SNAPSHOT"),
    v1_13_1("1.13.1-R0.1-SNAPSHOT"),
    v1_13_2("1.13.2-R0.1-SNAPSHOT"),

    v1_14("1.14-R0.1-SNAPSHOT"),
    v1_14_2("1.14.2-R0.1-SNAPSHOT"),
    v1_14_3("1.14.3-R0.1-SNAPSHOT"),
    v1_14_4("1.14.4-R0.1-SNAPSHOT"),

    v1_15("1.15-R0.1-SNAPSHOT"),
    v1_15_1("1.15.1-R0.1-SNAPSHOT"),
    v1_15_2("1.15.2-R0.1-SNAPSHOT"),

    v1_16_1("1.16.1-R0.1-SNAPSHOT"),
    v1_16_2("1.16.2-R0.1-SNAPSHOT"),
    v1_16_3("1.16.3-R0.1-SNAPSHOT"),
    v1_16_4("1.16.4-R0.1-SNAPSHOT"),
    v1_165_("1.16.5-R0.1-SNAPSHOT"),

    v1_17("1.17-R0.1-SNAPSHOT"),
    v1_17_1("1.17.1-R0.1-SNAPSHOT"),

    v1_18("1.18-R0.1-SNAPSHOT"),
    v1_18_1("1.18.1-R0.1-SNAPSHOT"),
    v1_18_2("1.18.2-R0.1-SNAPSHOT"),

    v1_19("1.19-R0.1-SNAPSHOT");

    @Getter
    String version;

    @Getter
    boolean remapped;

    MinecraftVersion(String version) {
        this(version, false);
    }

    MinecraftVersion(String version, boolean remapped) {
        this.version = version;
        this.remapped = remapped;
    }

    private final static Map<String, MinecraftVersion> BY_NAME = new TreeMap<>();

    static {
        for (MinecraftVersion minecraftVersion : values()) {
            BY_NAME.put(minecraftVersion.version(), minecraftVersion);
        }
    }

    public static @Nullable MinecraftVersion getVersion(String version) {
        return BY_NAME.getOrDefault(version, null);
    }

    public static boolean contains(String version) {
        return BY_NAME.containsKey(version);
    }
}