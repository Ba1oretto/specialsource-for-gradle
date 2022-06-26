package dev.barroit.specialsource.task;

import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecSpec;

public abstract class MojangMappingToMojangObfuscated extends BaseTask {
    @TaskAction
    public void compile() {
        initialize(this);
        executor().exec(this::command);
    }

    private void command(ExecSpec execSpec) {
        execSpec.commandLine(
                "java",
                "-cp", md5SpecialSourceJarPath() + systemSeparator() + remappedMojangJarPath(),
                "net.md_5.specialsource.SpecialSource",
                "--live",
                "-i", inputPath(),
                "-o", obfuscatedPath(),
                "-m", mapsMojangTxtPath(),
                "--reverse"
        );
    }
}