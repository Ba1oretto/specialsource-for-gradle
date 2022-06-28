package com.baioretto.specialsource.task;

import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecSpec;

public abstract class MojangObfuscatedToSpigotObfuscated extends BaseTask {
    @TaskAction
    public void compile() {
        initialize(this);
        executor().exec(this::command);
    }

    private void command(ExecSpec execSpec) {
        execSpec.commandLine(
                "java",
                "-cp", md5SpecialSourceJarPath() + systemSeparator() + remappedObfJarPath(),
                "net.md_5.specialsource.SpecialSource",
                "--live",
                "-i", obfuscatedPath(),
                "-o", outputPath(),
                "-m", mapsSpigotCsrgPath()
        );
    }
}