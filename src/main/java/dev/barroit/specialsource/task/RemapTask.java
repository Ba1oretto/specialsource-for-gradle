package dev.barroit.specialsource.task;

import dev.barroit.specialsource.SpecialSource;
import dev.barroit.specialsource.exec.ProjectExecutor;
import dev.barroit.specialsource.util.TaskUtil;
import groovy.lang.MissingPropertyException;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.bundling.Jar;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static dev.barroit.specialsource.util.TaskUtil.illegalVersion;

public abstract class RemapTask extends Jar {
    @Input
    public abstract Property<String> getVer();

    @Input
    public abstract Property<Boolean> getEnableLog();

    @Input
    public abstract Property<File> getMd5SpecialSourceFile();

    @Input
    public abstract Property<File> getTempDir();

    private String version;

    private File mavenLocal;

    private ProjectExecutor executor;

    private String md5SpecialSourceFilePath;

    private String classpathSeparator;

    private Set<File> outputFile;

    private String inputPath;
    private String intermediatePath;
    private String outputPath;

    private String remappedMojangPath;
    private String mapsMojangPath;
    private String remappedObfPath;
    private String mapsSpigotPath;

    @TaskAction
    public void remapServerCode() {
        init();

        outputFile.forEach(file -> {
            inputPath = file.getPath();
            intermediatePath = TaskUtil.getFile(getTempDir().get(), file.getName().replace(".jar", "-obf.jar")).getPath();
            outputPath = inputPath;

            remapObf();

            remapSpigot();
        });

        System.out.println("\nRemapping is done");
    }

    private void remapSpigot() {
        executor.exec(execSpec ->
                execSpec.commandLine(
                        "java",
                        "-cp", md5SpecialSourceFilePath + classpathSeparator + remappedObfPath,
                        "net.md_5.specialsource.SpecialSource",
                        "--live",
                        "-i", intermediatePath,
                        "-o", outputPath,
                        "-m", mapsSpigotPath,
                        "--reverse"
                )
        );
    }

    private void remapObf() {
        executor.exec(execSpec ->
                execSpec.commandLine(
                        "java",
                        "-cp", md5SpecialSourceFilePath + classpathSeparator + remappedMojangPath,
                        "net.md_5.specialsource.SpecialSource",
                        "--live",
                        "-i", inputPath,
                        "-o", intermediatePath,
                        "-m", mapsMojangPath,
                        "--reverse"
                )
        );
    }

    private void init() {
        version = getVer().get();

        if (illegalVersion(version)) {
            throw new MissingPropertyException("The NMS version is empty. Make sure you configure it", "nmsVersion", String.class);
        }

        Project project = SpecialSource.getProject();

        mavenLocal = TaskUtil.getFile(project.getRepositories().mavenLocal().getUrl());

        md5SpecialSourceFilePath = getMd5SpecialSourceFile().get().getPath();

        executor = new ProjectExecutor(project);

        classpathSeparator = System.getProperty("path.separator", ":");

        if (outputFile == null) {
            outputFile = new HashSet<>(super.getOutputs().getFiles().getFiles());
        } else {
            outputFile.clear();
            outputFile.addAll(super.getOutputs().getFiles().getFiles());
        }

        remappedMojangPath = getRemappedMojangPath();
        mapsMojangPath = getMapsMojangPath();
        remappedObfPath = getRemappedObfPath();
        mapsSpigotPath = getMapsSpigotPath();

        if (getEnableLog().get()) {
            String info = String.format(
                    """
                            --------------Information--------------
                            version: %s
                            maven local: %s
                            md5 specialsource jar: %s
                            system separator: %s
                            remapped-mojang: %s
                            maps-mojang: %s
                            remapped-obf: %s
                            maps-spigot: %s
                            ---------------------------------------
                                    """,
                    version,
                    mavenLocal.getPath(),
                    md5SpecialSourceFilePath,
                    classpathSeparator,
                    remappedMojangPath,
                    mapsMojangPath,
                    remappedObfPath,
                    mapsSpigotPath
            );
            System.out.println(info);
        }
    }

    private String getRemappedMojangPath() {
        return getPath("org/spigotmc/spigot/%s/spigot-%s-remapped-mojang.jar");
    }

    private String getMapsMojangPath() {
        return getPath("org/spigotmc/minecraft-server/%s/minecraft-server-%s-maps-mojang.txt");
    }

    private String getRemappedObfPath() {
        return getPath("org/spigotmc/spigot/%s/spigot-%s-remapped-obf.jar");
    }

    private String getMapsSpigotPath() {
        return getPath("org/spigotmc/minecraft-server/%s/minecraft-server-%s-maps-spigot.csrg");
    }

    private String getPath(String temp) {
        return TaskUtil.getFile(mavenLocal, TaskUtil.setVersion(temp, version)).getPath();
    }
}