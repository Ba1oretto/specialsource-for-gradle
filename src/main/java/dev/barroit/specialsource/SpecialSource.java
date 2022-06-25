package dev.barroit.specialsource;

import dev.barroit.specialsource.data.SpecialSourceDataExtension;
import dev.barroit.specialsource.task.RemapTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.*;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

import java.io.File;

@SuppressWarnings({"NullableProblems", "unused"})
public class SpecialSource implements Plugin<Project> {
    private static Project project;

    @Override
    public void apply(Project project) {
        SpecialSource.project = project;

        addRepositories(project);

        Configuration md5SpecialSourceConfiguration = getMd5SpecialSourceConfiguration(project);
        File md5SpecialSourceFile = getMd5SpecialSourceFile(md5SpecialSourceConfiguration);

        SpecialSourceDataExtension extension = getSpecialSourceDataExtension(project);

        registerRemapTask(project, extension, md5SpecialSourceFile);
    }

    private void registerRemapTask(Project project, SpecialSourceDataExtension extension, File md5SpecialSourceFile) {
        project.getTasks().register("remap", RemapTask.class, task -> {
            task.setGroup("SpecialSource");
            task.setDescription("Remap code");

            task.getVer().set(extension.getVer());
            task.getEnableLog().set(extension.getEnableLog());
            task.getTempDir().set(task.getTemporaryDir());
            task.getMd5SpecialSourceFile().set(md5SpecialSourceFile);
        });
    }

    private SpecialSourceDataExtension getSpecialSourceDataExtension(Project project) {
        return project.getExtensions().create("specialSourceData", SpecialSourceDataExtension.class);
    }

    private Configuration getMd5SpecialSourceConfiguration(Project project) {
        return project.getConfigurations().create("md5SpecialSource", configuration -> {
            configuration.setVisible(false);
            configuration.setCanBeConsumed(false);
            configuration.setCanBeResolved(true);
            configuration.getDependencies().add(project.getDependencies().create("net.md-5:SpecialSource:1.11.0:shaded"));
            configuration.resolve();
        });
    }

    private File getMd5SpecialSourceFile(Configuration md5SpecialSourceConfiguration) {
        return md5SpecialSourceConfiguration.resolve().stream().filter(file -> file.getName().equals("SpecialSource-1.11.0-shaded.jar")).toList().get(0);
    }

    private void addRepositories(Project project) {
        MavenArtifactRepository mavenCentral = project.getRepositories().mavenCentral();
        mavenCentral.artifactUrls("https://repo.maven.apache.org/maven2/net/md-5/SpecialSource/1.11.0/SpecialSource-1.11.0-shaded.jar");
        mavenCentral.metadataSources(metadataSources -> {
            metadataSources.mavenPom();
            metadataSources.artifact();
        });
        project.getRepositories().add(mavenCentral);
    }

    public static Project getProject() {
        return project;
    }
}