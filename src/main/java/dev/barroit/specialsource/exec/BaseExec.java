package dev.barroit.specialsource.exec;

import org.gradle.api.Action;
import org.gradle.process.ExecResult;
import org.gradle.process.ExecSpec;

public interface BaseExec {
    @SuppressWarnings("UnusedReturnValue")
    ExecResult exec(Action<? super ExecSpec> action);
}
