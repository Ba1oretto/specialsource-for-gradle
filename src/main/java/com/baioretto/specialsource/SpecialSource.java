package com.baioretto.specialsource;

import com.baioretto.specialsource.exception.SpecialSourceInternalException;
import com.baioretto.specialsource.task.MojangMappingToMojangObfuscated;
import com.baioretto.specialsource.task.MojangObfuscatedToSpigotObfuscated;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@SuppressWarnings({"unused", "NullableProblems"})
public class SpecialSource implements Plugin<Project> {
    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;

        transferMd5SpecialSource();

        addExtensions();
    }

    private void transferMd5SpecialSource() {
        String mavenLocal = project.getRepositories().mavenLocal().getUrl().getPath();

        File md5SpecialsourceJarFolder = new File(mavenLocal, "net/md-5/SpecialSource/1.11.0");

        String md5SpecialSourceName = "SpecialSource-1.11.0-shaded.jar";

        Path target = new File(md5SpecialsourceJarFolder, md5SpecialSourceName).toPath();

        if (!md5SpecialsourceJarFolder.exists()) {
            System.out.printf("The md_5 specialsource does not exist, plugin will transfer it to %s", md5SpecialsourceJarFolder.getPath());
            if (md5SpecialsourceJarFolder.mkdirs()) {
                copyJar(md5SpecialSourceName, target);
            } else
                throw new RuntimeException(String.format("Could mot make directory %s\n", md5SpecialsourceJarFolder.getPath()));
        }

        project.getExtensions().getExtraProperties().set("md5SpecialSourceJarPath", target.toString());
    }

    private void copyJar(String md5SpecialSourceName, Path target) {
        try (InputStream resource = getClass().getResourceAsStream(String.format("/%s", md5SpecialSourceName))) {
            if (null != resource) {
                Files.copy(resource, target, StandardCopyOption.REPLACE_EXISTING);
            } else
                throw new SpecialSourceInternalException(String.format("could not get resource %s", md5SpecialSourceName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addExtensions() {
        project.getExtensions().getExtraProperties().set("MojangMappingToMojangObfuscated", MojangMappingToMojangObfuscated.class);
        project.getExtensions().getExtraProperties().set("MojangObfuscatedToSpigotObfuscated", MojangObfuscatedToSpigotObfuscated.class);
    }
}