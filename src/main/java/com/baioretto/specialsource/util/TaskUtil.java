package com.baioretto.specialsource.util;

import com.baioretto.specialsource.exception.SpecialSourceDataException;
import lombok.experimental.UtilityClass;
import org.gradle.api.provider.Property;

import java.io.File;

@UtilityClass
public class TaskUtil {
    public void check(boolean expected, boolean actual, String failureMessage) {
        if (expected != actual) throw new SpecialSourceDataException(failureMessage);
    }

    public String getPath(String mavenLocalPath, String jarPath) {
        return new File(mavenLocalPath, jarPath).getPath();
    }

    public String fillVersion(String temp, String minecraftVersion) {
        return String.format(temp, minecraftVersion, minecraftVersion);
    }

    public boolean isPresent(Property<?> target) {
        return target.isPresent();
    }
}
