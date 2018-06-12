package rules;

import com.google.common.collect.ImmutableList;
import org.sonar.plugins.java.api.JavaCheck;

import java.util.List;

public final class RulesList {

    public RulesList() {
    }

    static List<Class> getChecks() {
        return ImmutableList.<Class>builder().addAll(getJavaChecks()).addAll(getJavaTestChecks()).build();
    }

    static List<Class<? extends JavaCheck>> getJavaChecks() {
        return ImmutableList.<Class<? extends JavaCheck>>builder()
                .add(R1RefactoringRule.class)
                .build();
    }

    static List<Class<? extends JavaCheck>> getJavaTestChecks() {
        return ImmutableList.<Class<? extends JavaCheck>>builder()
                .build();
    }
}