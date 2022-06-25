package dev.barroit.specialsource.data;

import org.gradle.api.provider.Property;

public abstract class SpecialSourceDataExtension {
    public abstract Property<String> getVer();
    public abstract Property<Boolean> getEnableLog();
}
