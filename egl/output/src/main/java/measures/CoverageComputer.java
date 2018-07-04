package measures;

import org.sonar.api.ce.measure.Issue;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import java.util.List;
import java.util.Objects;

import static measures.CoverageMetrics.ARCHITECTURAL_TECHNICAL_DEBT;
import static org.sonar.api.ce.measure.Component.Type.FILE;

public class CoverageComputer implements MeasureComputer {
    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext defContext) {
        return defContext.newDefinitionBuilder()
                .setOutputMetrics(ARCHITECTURAL_TECHNICAL_DEBT.key())
                .build();
    }


    @Override
    public void compute(MeasureComputerContext context) {
        if (context.getComponent().getType() == FILE) {
            List<? extends Issue> fileIssues = context.getIssues();
            long sum = 0;
            for (Issue i : fileIssues) {
                if (i.ruleKey().rule().contains("RefactoringRule"))
                    sum += Objects.requireNonNull(i.effort()).toMinutes();
            }
            context.addMeasure(ARCHITECTURAL_TECHNICAL_DEBT.key(), sum);
            return;
        }
        long totalSum = 0;
        for (Measure measure : context.getChildrenMeasures(ARCHITECTURAL_TECHNICAL_DEBT.key())) {
            totalSum += measure.getLongValue();
        }

        context.addMeasure(ARCHITECTURAL_TECHNICAL_DEBT.key(), totalSum);
    }
}