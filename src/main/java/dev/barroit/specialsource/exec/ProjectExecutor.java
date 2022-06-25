package dev.barroit.specialsource.exec;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.process.ExecResult;
import org.gradle.process.ExecSpec;

public class ProjectExecutor implements BaseExec {
    private final Project project;

    public ProjectExecutor(Project project) {
        this.project = project;
    }

    @Override
    public ExecResult exec(Action<? super ExecSpec> action) {
        return project.exec(action);
    }
}
