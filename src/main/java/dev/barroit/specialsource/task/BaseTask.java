package dev.barroit.specialsource.task;

import dev.barroit.specialsource.enumerate.MinecraftVersion;
import dev.barroit.specialsource.exec.ProjectExecutor;
import dev.barroit.specialsource.util.TaskUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;

import java.io.File;

import static dev.barroit.specialsource.util.TaskUtil.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public abstract class BaseTask extends DefaultTask {
    public BaseTask() {
        this.mavenLocalPath = super.getProject().getRepositories().mavenLocal().getUrl().getPath();
        this.systemSeparator = System.getProperty("path.separator", ";");
        this.executor = new ProjectExecutor(getProject());
    }

    @Input
    public abstract Property<String> getMinecraftVersion();

    @InputFile
    @Optional
    public abstract Property<File> getMd5SpecialSourceJar();

    @InputFile
    @Optional
    public abstract Property<File> getInput();

    @Input
    @Optional
    public abstract Property<File> getObfuscated();

    @Input
    @Optional
    public abstract Property<File> getOutput();

    @Getter
    String minecraftVersion;

    @Getter
    final String mavenLocalPath, systemSeparator;

    @Getter
    final ProjectExecutor executor;

    @Getter
    String md5SpecialSourceJarPath, inputPath, obfuscatedPath, outputPath;

    @Getter
    String remappedMojangJarPath, remappedObfJarPath, mapsMojangTxtPath, mapsSpigotCsrgPath;

    void initialize(BaseTask task) {
        this.minecraftVersion = getVersion();
        this.md5SpecialSourceJarPath = getMd5SpecialSourceJarPath();

        if (task instanceof MojangMappingToMojangObfuscated) initMojangMappingToMojangObfuscatedData();
        else if (task instanceof MojangObfuscatedToSpigotObfuscated) initMojangObfuscatedToSpigotObfuscatedData();

        this.remappedMojangJarPath = getRemappedMojangJarPath();
        this.remappedObfJarPath = getRemappedObfJarPath();
        this.mapsMojangTxtPath = getMapsMojangTxtPath();
        this.mapsSpigotCsrgPath = getMapsSpigotCsrgPath();
    }

    private void initMojangMappingToMojangObfuscatedData() {
        File input = getInputFile();
        this.inputPath = input.getPath();

        this.obfuscatedPath = isPresent(getOutput()) && !getOutput().get().isDirectory() ? getOutput().get().getPath() : inputPath.replace(".jar", "-obf.jar");
    }

    private void initMojangObfuscatedToSpigotObfuscatedData() {
        File input = getInputFile();
        this.obfuscatedPath = input.getPath();

        this.outputPath = getOutput().isPresent() && !getOutput().get().isDirectory() ? getOutput().get().getPath() : obfuscatedPath.replaceAll("(.*)-obf(.jar)$", "$1$2");
    }

    private String getMd5SpecialSourceJarPath() {
        if (isNormalFile(getMd5SpecialSourceJar())) return getMd5SpecialSourceJar().get().getPath();
        else return (String) getProject().getExtensions().getExtraProperties().get("md5SpecialSourceJarPath");
    }

    private String getVersion() {
        check(true, isPresent(getMinecraftVersion()), "property 'minecraftVersion' not present.");
        String minecraftVersion = getMinecraftVersion().get();
        check(false, minecraftVersion.isBlank(), "property 'minecraftVersion' is blank.");
        check(true, MinecraftVersion.contains(minecraftVersion), "property 'minecraftVersion' incorrect.");
        return minecraftVersion;
    }

    private File getInputFile() {
        check(true, isPresent(getInput()), "property 'input' not present.");
        File input = getInput().get();
        check(false, input.isDirectory(), "property 'input' must be a jar file.");
        return input;
    }

    private boolean isNormalFile(Property<File> file) {
        return file.isPresent() && !file.get().isDirectory();
    }

    private String getRemappedMojangJarPath() {
        return getPath("org/spigotmc/spigot/%s/spigot-%s-remapped-mojang.jar");
    }

    private String getRemappedObfJarPath() {
        return getPath("org/spigotmc/spigot/%s/spigot-%s-remapped-obf.jar");
    }

    private String getMapsMojangTxtPath() {
        return getPath("org/spigotmc/minecraft-server/%s/minecraft-server-%s-maps-mojang.txt");
    }

    private String getMapsSpigotCsrgPath() {
        return getPath("org/spigotmc/minecraft-server/%s/minecraft-server-%s-maps-spigot.csrg");
    }

    private String getPath(String temp) {
        return TaskUtil.getPath(mavenLocalPath, fillVersion(temp, minecraftVersion));
    }
}