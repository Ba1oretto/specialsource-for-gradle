package dev.barroit.specialsource;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.*;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

import java.io.File;

@SuppressWarnings({"NullableProblems", "unused"})
public class SpecialSource implements Plugin<Project> {
    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;

        addRepositories();
        addMd5SpecialSource();
    }

    private void addMd5SpecialSource() {
        File md5SpecialSource = getMd5SpecialSourceFile(getMd5SpecialSourceConfiguration());
        project.getExtensions().getExtraProperties().set("md5SpecialSourceJarPath", md5SpecialSource.getPath());
    }

    private Configuration getMd5SpecialSourceConfiguration() {
        return project.getConfigurations().create("md5SpecialSource", configuration -> {
            configuration.setVisible(false);
            configuration.setCanBeConsumed(false);
            configuration.setCanBeResolved(true);
            configuration.getDependencies().add(project.getDependencies().create("net.md-5:SpecialSource:1.11.0:shaded"));
        });
    }

    private File getMd5SpecialSourceFile(Configuration md5SpecialSourceConfiguration) {
        return md5SpecialSourceConfiguration.resolve().stream().filter(file -> file.getName().equals("SpecialSource-1.11.0-shaded.jar")).toList().get(0);
    }

    private void addRepositories() {
        MavenArtifactRepository mavenCentral = project.getRepositories().mavenCentral();
        mavenCentral.artifactUrls("https://repo.maven.apache.org/maven2/net/md-5/SpecialSource/1.11.0/SpecialSource-1.11.0-shaded.jar");
        mavenCentral.metadataSources(metadataSources -> {
            metadataSources.mavenPom();
            metadataSources.artifact();
        });
        project.getRepositories().add(mavenCentral);
    }
}